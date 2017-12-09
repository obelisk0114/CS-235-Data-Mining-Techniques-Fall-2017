package Project;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Mapping {
	private Map<String, String> groundTruthMap;
	private Map<String, List<String>> resultMap;
	
	public Mapping() {
		groundTruthMap = new HashMap<String, String>();
		resultMap = new HashMap<String, List<String>>();
	}
	
	public void read(String groundTruthPath, String resultPath) throws IOException {
		FileReader fr = new FileReader(groundTruthPath);
		BufferedReader br = new BufferedReader(fr);
		while (br.ready()) {
			String[] cell = br.readLine().split(",");
			if (groundTruthMap.containsKey(cell[1])) {
				System.out.println("Error occurs in ground truth !!!");
				System.exit(1);
			}
			groundTruthMap.put(cell[1], cell[0]);
		}
		fr.close();
		
		FileReader fr2 = new FileReader(resultPath);
		BufferedReader br2 = new BufferedReader(fr2);
		int counter = 1;
		int index = 1;
		br2.readLine();
		while (br2.ready()) {
			String[] cell2 = br2.readLine().split(",");
			if (groundTruthMap.containsKey(cell2[1])) {
				counter++;
				if (resultMap.containsKey(cell2[1])) {
					System.out.println("Duplicate in result index = " + index + " !!!");
					counter--;
				}
				else {
					List<String> list = new ArrayList<String>();
					for (int i = 2; i < cell2.length; i++) {
						list.add(cell2[i]);
					}
					
					resultMap.put(cell2[1], list);
				}
			}
			index++;
		}
		fr2.close();
		
		if (counter != 56) {
			System.out.println("Not match !!!");
		}
	}
	
	public void compare() {
		;
	}

}
