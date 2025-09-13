package com.example.controller;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.example.WordMapper;
import com.example.WordReducer;

public class Controller {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: Controller <input path> <output path>");
            System.exit(-1);
        }

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Word Count with Sorting");

        job.setJarByClass(Controller.class);
        job.setMapperClass(WordMapper.class);

        // Use built-in combiner for efficiency (sums intermediate counts)
        job.setCombinerClass(org.apache.hadoop.mapreduce.lib.reduce.IntSumReducer.class);

        job.setReducerClass(WordReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
