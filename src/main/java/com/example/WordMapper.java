package com.example;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

public class WordReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    // Store word counts temporarily
    private Map<String, Integer> countMap = new HashMap<>();

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        int sum = 0;
        for (IntWritable val : values) {
            sum += val.get();
        }
        // Save lowercase word counts
        countMap.put(key.toString(), sum);
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        // Convert map to list for sorting
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(countMap.entrySet());

        // Sort by value (frequency) descending
        sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        // Write sorted output
        for (Map.Entry<String, Integer> entry : sortedList) {
            context.write(new Text(entry.getKey()), new IntWritable(entry.getValue()));
        }
    }
}
