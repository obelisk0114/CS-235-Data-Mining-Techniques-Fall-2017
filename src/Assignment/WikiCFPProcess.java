package Assignment;

import java.io.IOException;

public class WikiCFPProcess {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		String s = "C:/Users/15T-J000/Documents/GitHub/CS-235-Data-Mining-Techniques-Fall-2017/CrawlFiles/wikicfp_crawl.txt";
		String s1 = "C:/Users/15T-J000/Documents/GitHub/CS-235-Data-Mining-Techniques-Fall-2017/CrawlFiles/result1.txt";
		String s2 = "C:/Users/15T-J000/Documents/GitHub/CS-235-Data-Mining-Techniques-Fall-2017/CrawlFiles/result2.txt";
		String s3 = "C:/Users/15T-J000/Documents/GitHub/CS-235-Data-Mining-Techniques-Fall-2017/CrawlFiles/result3.txt";
		String s4 = "C:/Users/15T-J000/Documents/GitHub/CS-235-Data-Mining-Techniques-Fall-2017/CrawlFiles/result4.txt";
		
		WikiCFP1 process1 = new WikiCFP1(s, s1);
		process1.readWrite(s, s1);
		
		WikiCFP2 process2 = new WikiCFP2(s, s2);
		process2.readWrite(s, s2);
		
		WikiCFP3 process3 = new WikiCFP3(s, s3);
		process3.readWrite(s, s3);
		
		WikiCFP4 process4 = new WikiCFP4(s, s4);
		process4.readWrite(s, s4);
	}

}
