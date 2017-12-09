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
			// csv file is separated by ",".
			String[] cell = br.readLine().split(",");
			
			// cell[1] is name of the malware.
			// Duplicate element
			if (groundTruthMap.containsKey(cell[1])) {
				System.out.println("Error occurs in ground truth !!!");
				System.exit(1);
			}
			
			groundTruthMap.put(cell[1], cell[0]);
		}
		fr.close();
		
		FileReader fr2 = new FileReader(resultPath);
		BufferedReader br2 = new BufferedReader(fr2);
		
		int counter = 0;       // compare with ground truth
		int index = 1;         // row
		br2.readLine();        // First row is label.
		
		while (br2.ready()) {
			// csv file is separated by ",".
			String[] cell2 = br2.readLine().split(",");
			
			// Find the corresponding one in ground truth table.
			if (groundTruthMap.containsKey(cell2[1])) {
				counter++;
				
				if (resultMap.containsKey(cell2[1])) {  // Duplicate in result table
					System.out.println("Duplicate in result index = " + index + " !!!");
					counter--;         // minus 1
				}
				else {         // Record every cell in the row to the list.
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
		
		if (counter != 56) {        // There are 56 malware in ground truth table.
			System.out.println("counter = " + counter + "; Not match !!!");
			System.out.println("The following are lost.\n");
			findLost();
		}
		else {
			System.out.println("Process complete.");
		}
	}
	
	public void findLost() {
		Map<String, String> tmp = new HashMap<String, String>();
		tmp.putAll(groundTruthMap);
		
		for (String key : resultMap.keySet()) {
			tmp.remove(key);
		}
		
		for (String key : tmp.keySet()) {
			System.out.println(key);
		}
	}
	
	public void compare(String outPath, int col) {
		Map<String, List<String>> groundTruthClass = new HashMap<String, List<String>>();
		Map<String, List<String>> resultClass = new HashMap<String, List<String>>();
		
		// Classify ground truth
		for (Map.Entry<String, String> groundTruthEntry : groundTruthMap.entrySet()) {
			String classNumber = groundTruthEntry.getValue(); 
			if (groundTruthClass.containsKey(classNumber)) {
				List<String> list = groundTruthClass.get(classNumber);
				list.add(groundTruthEntry.getKey());
				groundTruthClass.put(classNumber, list);
			}
			else {
				List<String> list = new ArrayList<String>();
				list.add(groundTruthEntry.getKey());
				groundTruthClass.put(classNumber, list);
			}
		}
		
		// Classify result
		for (Map.Entry<String, List<String>> resultEntry : resultMap.entrySet()) {
			String classNumberResult = resultEntry.getValue().get(col);
			if (resultClass.containsKey(classNumberResult)) {
				List<String> list = resultClass.get(classNumberResult);
				list.add(resultEntry.getKey());
				resultClass.put(classNumberResult, list);
			}
			else {
				List<String> list = new ArrayList<String>();
				list.add(resultEntry.getKey());
				resultClass.put(classNumberResult, list);
			}
		}
	}

}
