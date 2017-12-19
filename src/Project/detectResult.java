package Project;

import java.io.IOException;

public class detectResult {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String groundTruthPath = "C:/Users/15T-J000/Desktop/Malware_Classes_Ground_Truth.csv";
		String resultPath = "C:/Users/15T-J000/Desktop/sysnumresult.csv";
		String outPath = "C:/Users/15T-J000/Desktop/correction.csv";
		
		//int[] eachResult = {16, 17};     // Don't count index and name column (-2)
		
		Mapping mapping = new Mapping();
		mapping.read(groundTruthPath, resultPath);
		mapping.compare(outPath, 17);

	}

}
