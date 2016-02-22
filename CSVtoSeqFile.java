package NaiveBayes;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.io.Text;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;


public class CSVtoSeqFile {
    public static void main(String args[]) throws Exception {
        if (args.length != 2) {
            System.err.println("Arguments: [input tsv file] [output sequence file]");
            return;
        }
        String inputFileName = args[0];
        String outputDirName = args[1];
 
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(configuration);
        Writer writer = new SequenceFile.Writer(fs, configuration, new Path(outputDirName + "/chunk-0.txt"),
                Text.class, Text.class);
 
        int count = 0;
        BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
        Text key = new Text();
        Text value = new Text();
        while(count < 1900000) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
//PostId,                             PostCreationDate,OwnerUserId,OwnerCreationDate,ReputationAtPostCreation, 4
//OwnerUndeletedAnswerCountAtPostTime,Title,           BodyMarkdown,Tag1,            Tag2,                     9
//Tag3,                               Tag4,            Tag5,        PostClosedDate,  OpenStatus                14
            String[] tokens = line.split(",", 15);
            if (tokens.length != 15) {
                System.out.println("Skip line: " + line);
                continue;
            }
            String openStatus = tokens[14];
            String id = tokens[0];
            String title = tokens[6];
            String body = tokens[7].replace("/", " ");
            //key.set("/" + category + "/" + id);
            //double[] d = new double[c.length];
            // Get the feature set
//            for (int i = 1; i < c.length; i++)
//            	d[i] = Double.parseDouble(c[i]);
//            Vector vec = new RandomAccessSparseVector(5);
//            vec.assign(d);
//            VectorWritable writable = new VectorWritable();
//            writable.set(vec);
            
            // Write all in the seqfile
            //writer.append(new Text(label), writable);
            if(!openStatus.equals("open")) {
            	openStatus = "close";
            }
            key.set("/" + openStatus + "/" + id);
            value.set(body);
            writer.append(key, value);
            //writer.append(key, writable);
            count++;
        }
        writer.close();
        System.out.println("Wrote " + count + " entries.");
        reader.close();
    }
}