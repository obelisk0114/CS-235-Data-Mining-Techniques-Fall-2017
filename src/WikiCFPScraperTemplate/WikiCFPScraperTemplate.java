package WikiCFPScraperTemplate;

import java.net.*;
import java.io.*;

import java.util.List;
import java.util.ArrayList;

public class WikiCFPScraperTemplate {
	public static int DELAY = 7;

	public static void main(String[] args) {
		WikiCFPScraperTemplate CFPcrawl = new WikiCFPScraperTemplate();
		
		// String category = "data mining";
		// String category = "databases";
		// String category = "machine learning";
		// String category = "artificial intelligence";
		
		String[] category = {"data mining"};
		//String[] category = {"data mining", "databases", "machine learning", "artificial intelligence"};
		int numOfPages = 20;
		int firstPage = 1;
		
		for (int i = 0; i < category.length; i++) {
			CFPcrawl.browse(category[i], firstPage, numOfPages);
			//CFPcrawl.browse(category[i], 13, 14);
		}
	}
	
	public void browse(String s, int firstPage, int numOfPages) {
		try {
			
			// create the output file and use 'UTF-8' encoding
			String s1 = s.replaceAll(" ", "_");
			String fileName = "wikicfp_crawl_" + s1 + ".txt";
			Writer writer = new BufferedWriter(new OutputStreamWriter(
				    new FileOutputStream(fileName), "UTF-8"));
			
			// now start crawling the all 'numOfPages' pages from 'firstPage'
			for (int i = firstPage; i <= numOfPages; i++) {
				// Create the initial request to read the first page
				// and get the number of total results
				String linkToScrape = "http://www.wikicfp.com/cfp/call?conference="
						+ URLEncoder.encode(s, "UTF-8") + "&page=" + i;
				String content = getPageFromUrl(linkToScrape);
				// parse or store the content of page 'i' here in 'content'
				// YOUR CODE GOES HERE
				ArrayList<List<String>> out = fetch(content);
				for (int i1 = 0; i1 < out.size(); i1++) {
					for (int i2 = 0; i2 < out.get(i1).size(); i2++) {
						String tmp = out.get(i1).get(i2);
						writer.write(tmp);
						//System.out.print(tmp + "\t");
						
						if (i2 < out.get(i1).size() - 1) {
							writer.write("\t");
						}
					}
					writer.write("\n");
					//System.out.println();
				}
				
				// IMPORTANT! Do not change the following:
				Thread.sleep(DELAY * 1000); // rate-limit the queries
			}
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<List<String>> fetch(String content) {
		ArrayList<List<String>> interesting = new ArrayList<List<String>>();
		// Select the table
		int ini = content.indexOf("table cellpadding=\"3\"");
		
		// 20 items per page
		for (int i = 1; i <= 20; i++) {
			List<String> element = new ArrayList<String>();
			
			// Use hyperlink to get acronym
			int pre = content.indexOf("a href=", ini);
			pre = content.indexOf(">", pre);
			int post = content.indexOf("</", pre);
			String acronym = content.substring(pre + 1, post);
			element.add(acronym);
			
			// Get name
			pre = content.indexOf("td align=\"left\"", post);
			pre = content.indexOf(">", pre);
			post = content.indexOf("</", pre);
			String name = content.substring(pre + 1, post);
			element.add(name);
			
			// get location
			pre = content.indexOf("td align=\"left\"", post);
			pre = content.indexOf("td align=\"left\"", pre + 1);
			pre = content.indexOf(">", pre);
			post = content.indexOf("</", pre);
			String location = content.substring(pre + 1, post);
			element.add(location);
			
			// If location is 'N/A', it is a journal
			if (!location.equals("N/A")) {
				interesting.add(element);
			}
			
			ini = post;
		}
		
		return interesting;
	}

	/**
	 * Given a string URL returns a string with the page contents Adapted from
	 * example in
	 * http://docs.oracle.com/javase/tutorial/networking/urls/readingWriting.html
	 * 
	 * @param link
	 * @return
	 * @throws IOException
	 */
	public String getPageFromUrl(String link) throws IOException {
		URL thePage = new URL(link);
		URLConnection yc = thePage.openConnection();
		// Change encoding to 'UTF-8'
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
		String inputLine;
		String output = "";
		while ((inputLine = in.readLine()) != null) {
			output += inputLine + "\n";
		}
		in.close();
		return output;
	}

}
