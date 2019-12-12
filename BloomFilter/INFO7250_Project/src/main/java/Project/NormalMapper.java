package Project;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.*;
import java.util.ArrayList;

public class NormalMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    ArrayList<String> positiveWords = new ArrayList();
    ArrayList<String> negativeWords = new ArrayList();
    BufferedReader br1, br2 = null;
    Text word = new Text();
    IntWritable one = new IntWritable(1);

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        try{
            Path[] Files = DistributedCache.getLocalCacheFiles(context.getConfiguration());

            br1 = new BufferedReader(new FileReader(Files[0].getName()));

            String line;
            while ((line = br1.readLine()) != null) {
                positiveWords.add(line);
            }
            br1.close();

            br2 = new BufferedReader(new FileReader(Files[1].getName()));

            while ((line = br2.readLine()) != null) {
                negativeWords.add(line);
            }
            br2.close();

        } catch(IOException ex) {
            System.err.println("Exception in mapper setup: " + ex.getMessage());
        }
    }

    public void map(LongWritable key, Text value, ProjectMapper.Context context) throws IOException, InterruptedException{
        String line = value.toString();
        String[] tokens = line.split("\t");
        if (tokens.length >= 13){
            String[] words = tokens[13].trim().toLowerCase().split(" ");
            for (String token : words){
                if(!token.matches(".*\\d.*")){
                    if(positiveWords.contains(token)) {
                        word.set("Positive: " + token.replaceAll("\\W", ""));
                        context.write(word, one);
                    }
                    else if(negativeWords.contains(token)) {
                        word.set("Negative: " + token.replaceAll("\\W", ""));
                        context.write(word, one);
                    }
                }
            }

        }
    }
}
