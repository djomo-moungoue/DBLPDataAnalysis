package org.dblp.field;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.dblp.helper.IntKey;

/**
 * Counts the number of pages cross references which page elements have<br/>
 * the format: x (e.g. 127) or x-y (e.g. 175-192) with x < y.<br/>
 * VIP: other format types are considered to be unreliable and hence stored<br/>
 * in the log file for later analysis.
 * @author SergeOliver
 *
 */
public class Page {
	
	private IntKey pages;
	private String page;
	private String key;
	
	/**
	 * Stores unreliable number of page formats for a later analysis.
	 */
	private static Set<String> setOfUnreliablePage = new TreeSet<String>();
	
	/**
	 * Construct a page object which contains the number of pages of the cross reference
	 * @param page to be parsed
	 * @param key
	 */
	public Page(String page, String key){
		this.key = key;
		this.page = page;
		if(Pattern.matches("\\d+", page)){//e.g. 25
			pages = new IntKey(1);
		}else if(Pattern.matches("\\d+-\\d+", page)){//175-198
			String[] tokens = page.split("-");
			int diff = Integer.parseInt(tokens[1]) - Integer.parseInt(tokens[0]);
			if(diff == 0){//e.g. 14-14 = 0
				pages = new IntKey(1);
			}else if(diff > 0){//e.g. 14-17 = 3
				pages = new IntKey(diff);
			}else{//e.g. 17-14 = -3
				pages = new IntKey(-1);
				setOfUnreliablePage.add("[Page: "+page+"] [Key: "+key+"]");
			}
		} else{//anything else
			pages = new IntKey(-1);
			setOfUnreliablePage.add("[Page: "+page+"] [Key: "+key+"]");
		}
	}
	
	public String getPubKey(){
		return key;
	}
	
	public static Set<String> getSetOfUnreliablePage(){
		return setOfUnreliablePage;
	}
	
	public IntKey getPages(){
		return pages;
	}
	
	public void setPages(IntKey p){
		pages = p;
	}
	
	@Override 
	public String toString(){
		return "page:  "+page;
	}
	
	/**
	 * To test the reliability of methods
	 * @param args
	 */
	public static void main(String[] args){
		Page p1 = new Page("", "1");
		Page p2 = new Page("159", "1");
		Page p3 = new Page("195-58", "1");
		Page p4 = new Page("i-iv 14-46", "1");
		Page p5 = new Page("19-89", "1");
		System.out.println("p1: "+p1.toString());
		System.out.println("p1 -1: "+p1.getPages());
		System.out.println("p2: "+p2.toString());
		System.out.println("p2 1: "+p2.getPages());
		System.out.println("p3: "+p3.toString());
		System.out.println("p3 -1: "+p3.getPages());
		System.out.println("p4: "+p4.toString());
		System.out.println("p4 -1: "+p4.getPages());
		System.out.println("p5: "+p5.toString());
		System.out.println("p5 70: "+p5.getPages());
	}
}
