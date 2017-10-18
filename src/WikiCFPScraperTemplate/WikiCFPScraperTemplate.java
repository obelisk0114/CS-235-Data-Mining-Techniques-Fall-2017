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
			
			// create the output file to record missing data
			String checkName = s1 + "_check.txt";
			File file = new File(checkName);
			file.createNewFile();
			FileWriter writer2 = new FileWriter(file); 
			writer2.write("Start to collect missing data...");
			writer2.write(System.getProperty( "line.separator" ));
			
			// now start crawling the all 'numOfPages' pages from 'firstPage'
			for (int i = firstPage; i <= numOfPages; i++) {
				// Create the initial request to read the first page
				// and get the number of total results
				String linkToScrape = "http://www.wikicfp.com/cfp/call?conference="
						+ URLEncoder.encode(s, "UTF-8") + "&page=" + i;
				String content = getPageFromUrl(linkToScrape);
				// parse or store the content of page 'i' here in 'content'
				// YOUR CODE GOES HERE
				ArrayList<List<List<String>>> out = fetch(content);
				for (int i1 = 0; i1 < out.get(0).size(); i1++) {
					for (int i2 = 0; i2 < out.get(0).get(i1).size(); i2++) {
						String tmp = out.get(0).get(i1).get(i2);
						writer.write(tmp);
						//System.out.print(tmp + "\t");
						
						if (i2 < out.get(0).get(i1).size() - 1) {
							writer.write("\t");
						}
					}
					writer.write("\n");
					//System.out.println();
				}
				
				// Missing occurs
				if (out.size() == 2) {
					for (int i11 = 0; i11 < out.get(1).size(); i11++) {
						for (int i22 = 0; i22 < out.get(1).get(i11).size(); i22++) {
							String tmp2 = out.get(1).get(i11).get(i22);
							writer2.write(tmp2);
							//System.out.print(tmp2 + "\t");
							
							if (i22 < out.get(1).get(i11).size() - 1) {
								writer2.write(" ");
							}
						}
						writer2.write(System.getProperty( "line.separator" ));
						//System.out.println();
					}
				}
				
				// IMPORTANT! Do not change the following:
				Thread.sleep(DELAY * 1000); // rate-limit the queries
			}
			
			writer.close();
			
			writer2.write(System.getProperty( "line.separator" ));
			writer2.write("Collecting missing data end.");
			writer2.flush();
			writer2.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<List<List<String>>> fetch(String content) {
		ArrayList<List<List<String>>> pack = new ArrayList<List<List<String>>>();
		
		ArrayList<List<String>> interesting = new ArrayList<List<String>>(); // crawl
		ArrayList<List<String>> empty = new ArrayList<List<String>>();  // empty
		
		// Select the table
		int ini = content.indexOf("table cellpadding=\"3\"");
		int end = content.indexOf("/table", ini);
		int i = 1;
		
		// crawl the table (20 items per page)
		while (true) {
			List<String> element = new ArrayList<String>();
			List<String> emptyOne = new ArrayList<String>();
			
			// Use hyperlink to get acronym
			int pre = content.indexOf("a href=", ini);
			if (pre >= end || pre == -1) {
				break;
			}
			pre = content.indexOf(">", pre);
			int post = content.indexOf("</", pre);
			String acronym = content.substring(pre + 1, post);
			element.add(acronym);
			// Check acronym
			String tmp1 = i + "_acronym";
			if (acronym.equals("")) {
				emptyOne.add(tmp1);
			}
			
			// Get name
			pre = content.indexOf("td align=\"left\"", post);
			pre = content.indexOf(">", pre);
			post = content.indexOf("</", pre);
			String name = content.substring(pre + 1, post);
			element.add(name);
			// Check name
			String tmp2 = i + "_name";
			if (acronym.equals("")) {
				emptyOne.add(tmp2);
			}
			
			// get location
			pre = content.indexOf("td align=\"left\"", post);
			pre = content.indexOf("td align=\"left\"", pre + 1);
			pre = content.indexOf(">", pre);
			post = content.indexOf("</", pre);
			String location = content.substring(pre + 1, post);
			element.add(location);
			// Check location
			String tmp3 = i + "_location";
			if (acronym.equals("")) {
				emptyOne.add(tmp3);
			}
			
			// If location is 'N/A', it is a journal
			if (!location.equals("N/A")) {
				interesting.add(element);
				if (!emptyOne.isEmpty()) {
					empty.add(emptyOne);
				}
			}
			
			ini = post;
			i++;
		}
		
		pack.add(interesting);
		if (!empty.isEmpty()) {
			pack.add(empty);
		}
		
		return pack;
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
