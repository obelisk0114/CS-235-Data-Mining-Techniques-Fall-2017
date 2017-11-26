package Assignment;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Comparator;

public class WikiCFP1 {
	public WikiCFP1(String s, String s2) throws IOException {
		//readWrite(s, s2);
	}
	
	class ValueComparator implements Comparator<String> {
	    Map<String, Integer> base;

	    public ValueComparator(Map<String, Integer> base) {
	        this.base = base;
	    }

	    // Note: this comparator imposes orderings that are inconsistent with
	    // equals.
	    public int compare(String a, String b) {
	        if (base.get(a) >= base.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        } // returning 0 would merge keys
	    }
	}
	
	public void readWrite(String s, String s2) throws IOException {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<String, Integer> map2 = new TreeMap<String, Integer>(bvc);
		FileReader fr = new FileReader(s);
		BufferedReader br = new BufferedReader(fr);
		while (br.ready()) {
			String[] line = br.readLine().split("\t");
			int count = map.getOrDefault(line[2], 0);
			map.put(line[2], count + 1);
		}
		fr.close();
		
		File writename = new File(s2);
		writename.createNewFile();  
        BufferedWriter out = new BufferedWriter(new FileWriter(writename));
        
        map2.putAll(map);
        for (Map.Entry<String, Integer> entry : map2.entrySet()) {  
            String location = entry.getKey() + ": " + entry.getValue();
            out.write(location);
            out.newLine();
        }
        
        out.close();
	}

}
