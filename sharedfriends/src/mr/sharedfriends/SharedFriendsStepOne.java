package mr.sharedfriends;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

/**
以下是qq的好友列表数据，冒号前是一个用，冒号后是该用户的所有好友（数据中的好友关系是单向的）
A:B,C,D,F,E,O
B:A,C,E,K
C:F,A,D,I
D:A,E,F,L
E:B,C,D,M,L
F:A,B,C,D,E,O,M
G:A,C,D,E,F
H:A,C,D,E,O
I:A,O
J:B,O
K:A,C,D
L:D,E,F
M:E,F,G
O:A,H,I,J

求出哪些人两两之间有共同好友，及他俩的共同好友都有谁？

 * @author: yangzheng
 */

public class SharedFriendsStepOne {

	static class SharedFriendsStepOneMapper extends Mapper<LongWritable, Text, Text, Text> {
		
		private Text map_key = new Text();
		private Text map_value = new Text();
		
		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();	// A:B,C,D,F,E,O
			String[] fields = line.split(":");
			
			map_value.set(fields[0]);	// A
			
			String[] arr = fields[1].split(",");	//B,C,D,F,E,O
			
			for (String str : arr) {
				map_key.set(str);
				context.write(map_key, map_value);  // <B,A><C,A><D,A><F,A><E,A><O,A>
			}
			
		}
	}
	
	static class SharedFriendsStepOneReducer extends Reducer<Text, Text, Text, Text> {
		private Text reduce_value = new Text();
		
		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			//<C,A><C,B><C,E><C,F><C,G>......
			StringBuilder sb = new StringBuilder();
			
			for (Text value : values) {
				sb.append(value.toString()).append(",");
			}
			
			reduce_value.set(sb.toString());
			// C  A,B,E,F,G,
			context.write(key, reduce_value);
		}
	}
	
	
	public static void main(String[] args) {
		
	}
}
