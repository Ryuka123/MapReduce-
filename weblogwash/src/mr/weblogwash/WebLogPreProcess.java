package mr.weblogwash;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WebLogPreProcess {

	static class WebLogPreProcessMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
		private Text k = new Text();
		private NullWritable v = NullWritable.get();
		
		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			WebLogBean bean = WebLogParser.parser(line);
			
			if (!bean.isValid()) {
				return;
			}
			
			k.set(bean.toString());
			context.write(k, v);
		}
	}
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		
		job.setJarByClass(WebLogPreProcess.class);
		
		job.setMapperClass(WebLogPreProcessMapper.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(NullWritable.class);
		
		job.setNumReduceTasks(0);
		
		FileInputFormat.setInputPaths(job, new Path("hdfs://master/mr/weblogwash/input"));
		FileOutputFormat.setOutputPath(job, new Path("hdfs://master/mr/weblogwash/output"));
		
		boolean res = job.waitForCompletion(true);
		System.exit(res ? 0 : 1);
	}
}
