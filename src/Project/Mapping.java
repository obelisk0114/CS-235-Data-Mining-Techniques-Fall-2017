package Project;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class Mapping {
	private Map<String, List<String>> groundTruthMap;
	private Map<String, List<String>> resultMap;
	private Set<String> ignore;
	private int total;
	
	public Mapping() {
		groundTruthMap = new HashMap<String, List<String>>();
		resultMap = new HashMap<String, List<String>>();
		
		ignore = new HashSet<String>();
		//ignore.add("bash0day-loT");
		//ignore.add("bash0day-fgt");
		
		total = 55;    // There are 55 malware in ground truth table.
	}
	
	public void read(String groundTruthPath, String resultPath) throws IOException {
		FileReader fr = new FileReader(groundTruthPath);
		BufferedReader br = new BufferedReader(fr);
		while (br.ready()) {
			// csv file is separated by ",".
			// class, name, number of system call 
			String[] cell = br.readLine().split(",");
			
			// cell[1] is name of the malware.
			// Duplicate element
			if (groundTruthMap.containsKey(cell[2])) {
				System.out.println("Error occurs in ground truth !!!");
				System.exit(1);
			}
			
			if (!ignore.contains(cell[1])) {
				List<String> list = new ArrayList<String>();
				list.add(cell[0]);
				list.add(cell[1]);
				
				groundTruthMap.put(cell[2], list);
			}
		}
		fr.close();
		
		FileReader fr2 = new FileReader(resultPath);
		BufferedReader br2 = new BufferedReader(fr2);
		
		Set<String> syscallNumber = new HashSet<String>();  // system call number
		
		int counter = 0;       // compare with ground truth
		int index = 1;         // row
		br2.readLine();        // First row is label.
		
		while (br2.ready()) {
			// csv file is separated by ",".
			String[] cell2 = br2.readLine().split(",");
			
			if (!syscallNumber.add(cell2[2])) {
				System.out.println("The number of system call isn't different.");
			}
			
			// Find the corresponding one in ground truth table.
			if (groundTruthMap.containsKey(cell2[2])) {
				counter++;
				
				if (resultMap.containsKey(cell2[2])) {  // Duplicate in result table
					System.out.println("Duplicate in result index = " + index + " !!!");
					counter--;         // minus 1
				}
				else if (!ignore.contains(cell2[1])) {         // Record every cell in the row to the list.
					List<String> list = new ArrayList<String>();
					for (int i = 1; i < cell2.length; i++) {
						if (i == 2) {
							continue;
						}
						
						//System.out.print(cell2[i] + ", ");
						list.add(cell2[i]);
					}
					
					//System.out.println();
					resultMap.put(cell2[2], list);
				}
			}
			index++;
		}
		fr2.close();
		
		if (counter != total) {
			System.out.println("counter = " + counter + "; Not match !!!\n");
			System.out.println("The following are lost:");
			findLost();
		}
		else {
			System.out.println("Process complete.");
		}
	}
	
	public void findLost() {
		Map<String, List<String>> tmp = new HashMap<String, List<String>>();
		tmp.putAll(groundTruthMap);
		
		for (String key : resultMap.keySet()) {
			tmp.remove(key);
		}
		
		for (List<String> name : tmp.values()) {
			System.out.println(name.get(1));
		}		
	}
	
	public void compare(String outPath, int col) {
		Map<String, Set<String>> groundTruthClass = new HashMap<String, Set<String>>();
		Map<String, Set<String>> resultClass = new HashMap<String, Set<String>>();
		
		// Classify ground truth
		for (Map.Entry<String, List<String>> groundTruthEntry : groundTruthMap.entrySet()) {
			String classNumber = groundTruthEntry.getValue().get(0); 
			if (groundTruthClass.containsKey(classNumber)) {
				Set<String> set = groundTruthClass.get(classNumber);
				set.add(groundTruthEntry.getKey());
				groundTruthClass.put(classNumber, set);
			}
			else {
				Set<String> set = new HashSet<String>();
				set.add(groundTruthEntry.getKey());
				groundTruthClass.put(classNumber, set);
			}
		}
		
		// Classify result
		for (Map.Entry<String, List<String>> resultEntry : resultMap.entrySet()) {
			String classNumberResult = resultEntry.getValue().get(col);
			if (resultClass.containsKey(classNumberResult)) {
				Set<String> set = resultClass.get(classNumberResult);
				set.add(resultEntry.getKey());
				resultClass.put(classNumberResult, set);
			}
			else {
				Set<String> set = new HashSet<String>();
				set.add(resultEntry.getKey());
				resultClass.put(classNumberResult, set);
			}
			
			//System.out.println("Class = " + classNumberResult);
		}
		
		// Compare result with ground truth
		Set<String> record = new HashSet<String>(); // Check duplicated
		List<Integer> correct = new ArrayList<Integer>(); // Record the number of correction
		
		for (Map.Entry<String, Set<String>> outerEntry : groundTruthClass.entrySet()) {
			Set<String> truthList = outerEntry.getValue();
			int maxCorrect = -1;
			String group = "";   // Record the group with max correction.
			System.out.println("\nClassify group " + outerEntry.getKey() + "; ");
			
			for (Map.Entry<String, Set<String>> innerEntry : resultClass.entrySet()) {
				Set<String> eachList = innerEntry.getValue();
				int count = 0;  // Count the number of corrections
				
				for (String s : eachList) {
					if (truthList.contains(s)) {
						count++;
					}
				}
				if (count > maxCorrect) {
					maxCorrect = count;
					group = innerEntry.getKey();
				}
				System.out.println(
					"Truth : " + outerEntry.getKey() + 
					"; Result : " + innerEntry.getKey() + "; match : " + count);
			}
			
			if (!record.add(group)) {
				System.out.println("\n!!! Class " + group + " is in the set.");
				System.out.println("Error hypothesis.");
			}
			else {
				correct.add(Integer.parseInt(group));
				System.out.println("Class " + group + " is added successfully.");
			}
		}
		
		double rate = 0;
		for (Integer i : correct) {
			rate += i;
		}
		rate /= total;
		
		System.out.println("\nAccuracy: " + rate);
	}

}
