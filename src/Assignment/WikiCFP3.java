package Assignment;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;

public class WikiCFP3 {
	public WikiCFP3(String s, String s2) throws IOException {
		//readWrite(s, s2);
	}
	
	public void readWrite(String s, String s2) throws IOException {
		Map<String, HashSet<String>> map = new HashMap<String, HashSet<String>>();
		FileReader fr = new FileReader(s);
		BufferedReader br = new BufferedReader(fr);
		while (br.ready()) {
			String[] line = br.readLine().split("\t");
			String[] part = line[0].split("\\d{4}");
			String target = part[0];
			if (map.containsKey(target)) {
				HashSet<String> list = map.get(target);
				list.add(line[2]);
				map.put(target, list);
			}
			else {
				HashSet<String> list = new HashSet<String>();
				list.add(line[2]);
				map.put(target, list);
			}
		}
		fr.close();
		
		File writename = new File(s2);
		writename.createNewFile();  
        BufferedWriter out = new BufferedWriter(new FileWriter(writename));
        
        for (Map.Entry<String, HashSet<String>> entry : map.entrySet()) {  
            String location = entry.getKey() + ": ";
            out.write(location);
            out.newLine();
            for (String sss : entry.getValue()) {
            	out.write("    " + sss);
            	out.newLine();
            }
        }
        
        out.close();
	}

}
