import java.io.File;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class ScoreSort {

    public static class Map extends Mapper<Object, Text, IntWritable, IntWritable> {

        private static IntWritable data = new IntWritable();

        @Override
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
//            data.set(Integer.parseInt(line));
            context.write(new IntWritable(Integer.parseInt(line)), new IntWritable(1));
        }
    }

    public static class Reduce extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {
        private static IntWritable lineNum = new IntWritable(1);

        @Override
        public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            for (IntWritable val : values) {
                context.write(lineNum, key);
                lineNum = new IntWritable(lineNum.get() + 1);
            }
        }
    }

    private static boolean DeleteDirIfExists(String dir) {
        File file = new File(dir);
        if (!file.isDirectory()) {
            System.err.printf("%s is not a directory\n", dir);
            return false;
        }
        if (!file.exists()) {
            System.out.printf("%s dose not exists\n", dir);
            return false;
        }
        return file.delete();
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
//        conf.set("fs.defaultFS", "hdfs://localhost:9000");
//        String[] otherArgs = new String[]{"input2", "output2"};
//        if (otherArgs.length != 2) {
//            System.err.println("Usage: ContentSort <in> <out>");
//            System.exit(2);
//        }
        Job job = Job.getInstance(conf, "ScoreSort");
        job.setJarByClass(ScoreSort.class);
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);

        DeleteDirIfExists("/Users/licheng/data/res");
        FileInputFormat.addInputPath(job, new Path("/Users/licheng/data"));
        FileOutputFormat.setOutputPath(job, new Path("/Users/licheng/data/res"));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}