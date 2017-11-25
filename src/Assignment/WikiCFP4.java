package Assignment;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
//import java.util.List;
//import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WikiCFP4 {
	public WikiCFP4(String s, String s2) throws IOException {
		readWrite(s, s2);
	}
	
	public void readWrite(String s, String s2) throws IOException {
		Map<String, Map<String, Integer>> map = new HashMap<String, Map<String, Integer>>();
		FileReader fr = new FileReader(s);
		BufferedReader br = new BufferedReader(fr);
		while (br.ready()) {
			String[] line = br.readLine().split("\t");
			//String[] part = line[0].split("\\d{4}");
			Pattern patt = Pattern.compile("\\d{4}");
			Matcher matcher = patt.matcher(line[0]);
			String year = "";
			if (matcher.find()) {
			    year = matcher.group(0); // you can get it from desired index as well
			}
			
			if (map.containsKey(line[2])) {
				Map<String, Integer> list = map.get(line[2]);
				int count = list.getOrDefault(year, 0);
				list.put(year, count + 1);
				map.put(line[2], list);
			}
			else {
				Map<String, Integer> list = new HashMap<String, Integer>();
				list.put(year, 1);
				map.put(line[2], list);
			}
		}
		fr.close();
		
		File writename = new File(s2);
		writename.createNewFile();  
        BufferedWriter out = new BufferedWriter(new FileWriter(writename));
        
        for (Map.Entry<String, Map<String, Integer>> entry : map.entrySet()) {  
            String location = entry.getKey() + ": ";
            out.write(location);
            out.newLine();
            for (Map.Entry<String, Integer> inner : entry.getValue().entrySet()) {
            	String time = "    " + inner.getKey() + ": " + inner.getValue();
            	out.write(time);
            	out.newLine();
            }
        }
        
        out.close();
	}

}
