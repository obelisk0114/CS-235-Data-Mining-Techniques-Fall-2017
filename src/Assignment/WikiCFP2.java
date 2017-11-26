package Assignment;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class WikiCFP2 {
	public WikiCFP2(String s, String s2) throws IOException {
		//readWrite(s, s2);
	}
	
	public void readWrite(String s, String s2) throws IOException {
		Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		FileReader fr = new FileReader(s);
		BufferedReader br = new BufferedReader(fr);
		while (br.ready()) {
			String[] line = br.readLine().split("\t");
			if (map.containsKey(line[2])) {
				ArrayList<String> list = map.get(line[2]);
				list.add(line[0]);
				map.put(line[2], list);
			}
			else {
				ArrayList<String> list = new ArrayList<String>();
				list.add(line[0]);
				map.put(line[2], list);
			}
		}
		fr.close();
		
		File writename = new File(s2);
		writename.createNewFile();  
        BufferedWriter out = new BufferedWriter(new FileWriter(writename));
        
        for (Map.Entry<String, ArrayList<String>> entry : map.entrySet()) {  
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
