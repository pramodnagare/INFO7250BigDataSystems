package Project;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.google.common.base.Charsets.UTF_8;

public class ProjectMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    BloomFilter postitiveBloomFilter, negativeBloomFilter;
    Text word = new Text();
    IntWritable one = new IntWritable(1);

    public ProjectMapper() throws IOException {
    }

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        try{
            Path[] Files = DistributedCache.getLocalCacheFiles(context.getConfiguration());
            InputStream bf = new FileInputStream(Files[0].getName());
            postitiveBloomFilter = BloomFilter.readFrom(bf,Funnels.stringFunnel(UTF_8));
            bf.close();

            InputStream bf1 = new FileInputStream(Files[1].getName());
            negativeBloomFilter = BloomFilter.readFrom(bf1,Funnels.stringFunnel(UTF_8));
            bf1.close();

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
                    if(postitiveBloomFilter.test(token)) {
                        word.set("Positive: "+ token.replaceAll("\\W", " "));
                        context.write(word, one);
                    }
                    else if(negativeBloomFilter.test(token)) {
                        word.set("Negative: "+ token.replaceAll("\\W", " "));
                        context.write(word, one);
                    }
                }
            }
        }
    }
}