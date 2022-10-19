package org.dblp.field;

import java.util.Set;
import java.util.TreeSet;

import org.dblp.helper.IntKey;

/**
 * Contains informations about a title element. Such as the title, the number of characters <br/>
 * and the number of words it contains.
 * VIP: checks unreliable titles with <br/>
 * more than one space between two words.<br/>
 * e.g. I am an&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;unreliable title.<br/>
 * or less than three characters<br/>
 * e.g. " &nbsp;&nbsp;&nbsp;&nbsp;", "a" or "ab"<br/>
 * Unreliable title are stored in the log file for a later analysis.
 * @author SergeOliver
 *
 */
public class Title implements Comparable<Title> {

	private IntKey characters;
	private IntKey words;
	private String title;
	private int spaces = 0;
	
	/**
	 * Stores unreliable titles for a later analysis.
	 */
	private static Set<String> setOfUnreliableTitle = new TreeSet<String>();
	
	/**
	 * Constructs a title objection which contains informations about the number<br/>
	 * of characters and words it contains.
	 * @param title of the publication
	 * @param key of the publication
	 */
	public Title(String title, String key){
		
		this.title = title.trim();
		int length = this.title.length();
		boolean isSpace = false; //to check if there is more than one space between two words
		int extraSpace = 0;
		for(int i = 0; i < length; i++){
			if(Character.isSpaceChar(this.title.charAt(i))){
				if(!isSpace){
					++spaces;
					isSpace = true;
				}else
					++extraSpace;
			}else
				isSpace = false;
		}
		
		words = (length > 0)? new IntKey(spaces + 1) : new IntKey(-1);
		characters = (length > 0)?new IntKey(length - (spaces+extraSpace)) : new IntKey(-1);

		if(length < 3)
			setOfUnreliableTitle.add(" [Length: "+length+"] Title: "+this.title+" [Key: "+key+"]");
		if(extraSpace > 0)
			setOfUnreliableTitle.add(" [Extra White Space: "+extraSpace+"] Title: "+this.title+" [Key: "+key+"]");
	}
	
	public static Set<String> getSetOfUnreliableTitle(){
		return setOfUnreliableTitle;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null || !(o instanceof Title)) return false;
		Title t = (Title)o;
		return title.equals(t.title);
	}
	
	@Override 
	public int hashCode(){
		return title.hashCode();
	}
	
	@Override
	public int compareTo(Title o) {
		return title.compareTo(o.title);
	}
	
	public IntKey getCharacters() {
		return characters;
	}
	public void setCharacters(IntKey characters) {
		this.characters = characters;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public IntKey getWords() {
		return words;
	}
	public void setWords(IntKey words) {
		this.words = words;
	}
	
	public int getSpaces(){
		return spaces;
	}
	
	@Override
	public String toString(){
		return "Title: "+title;
	}
	
	/**
	 * To test if the functionalities are well implemented
	 * @param args
	 */
	public static void main(String[] args){
		Title t1  = new Title("            ", "1365");
		Title t2  = new Title("     a      ", "1366");
		Title t3  = new Title(" 28 characters 7 words and 6              spaces        ", "1367");
		System.out.println("t1 : "+t1.getTitle());
		System.out.println("characters -1: "+t1.getCharacters());
		System.out.println("words -1: "+t1.getWords());
		System.out.println("spaces 0: "+t1.getSpaces());
		System.out.println("t2 a: "+t2.getTitle());
		System.out.println("characters 1: "+t2.getCharacters());
		System.out.println("words 1: "+t2.getWords());
		System.out.println("spaces 0: "+t2.getSpaces());
		System.out.println("t3  28 characters 7 words and 6              spaces        : "+t3.getTitle());
		System.out.println("characters 28: "+t3.getCharacters());
		System.out.println("words 7: "+t3.getWords());
		System.out.println("spaces 6: "+t3.getSpaces());
	}

}
