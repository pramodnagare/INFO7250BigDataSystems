package Project;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

public class Driver extends Mapper<Text, Text, Text, IntWritable> {

    public static void main (String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "INFO7250 Final Project");

        job.setJarByClass(Driver.class);
        job.setMapperClass(ProjectMapper.class);
//        job.setMapperClass(NormalMapper.class);
        job.setReducerClass(ProjectReducer.class);

        job.setCombinerClass(ProjectReducer.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        DistributedCache.addCacheFile(new Path(args[2]).toUri(), job.getConfiguration());
        DistributedCache.addCacheFile(new Path(args[3]).toUri(), job.getConfiguration());

        System.exit(job.waitForCompletion(true) ? 0:1);
    }
}
