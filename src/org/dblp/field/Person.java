package org.dblp.field;

import java.util.Set;
import java.util.TreeSet;

import org.dblp.helper.IntKey;

/**
 * Contains informations about a author/editor element. Such as the name <br/>
 * and the number of characters it contains.<br/>
 * VIP: checks unreliable name with <br/>
 * more than one space between two words.<br/>
 * e.g. I am an&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;unreliable name.<br/>
 * or less than three characters<br/>
 * e.g. "&nbsp;&nbsp;&nbsp;&nbsp;", "a" or "ab"<br/>
 * Unreliable names are stored in the log file for a later analysis.
 * @author SergeOliver
 *
 */
public class Person implements Comparable<Person> {

	/**
	 * Numbers of characters inside an author/editor name.
	 */
	private IntKey characters;

	/**
	 * Name of an author or editor.
	 */
	private String name;
	
	/**
	 * Number of single spaces between words.
	 */
	private int spaces;

	/**
	 *  Stores unreliable author/editor names for a later analysis.
	 */
	private static Set<String> setOfUnreliableName = new TreeSet<String>();
	
	/**
	 * Creates a person object which contains the name of this author/editor<br/>
	 * and its length.
	 * @param n: name of the current author/editor.
	 * @param key: key of the current publication.
	 */
	public Person(String name, String key){
		this.name = name.trim();
		int length = this.name.length();
		boolean isSpace = false; //to check if there is more than one space between two words
		int extraSpace = 0;
		for(int i = 0; i < length; i++){
			if(Character.isSpaceChar(this.name.charAt(i))){
				if(!isSpace){
					++spaces;
					isSpace = true;
				}else
					++extraSpace;
			}else
				isSpace = false;
		}
		
		characters = (length > 0)?new IntKey(length - (spaces+extraSpace)) : new IntKey(-1);

		if(length < 3 || extraSpace > 0)
			setOfUnreliableName.add(" ["+length+"] Name: "+this.name+" [Key: "+key+"]");
	}
	
	@Override 
	public boolean equals(Object o){
		if(o == null || !(o instanceof  Person)) return false;
		Person p = (Person) o;
		return name.equals(p.name);
	}
	
	public static Set<String> getSetOfUnreliableName(){
		return setOfUnreliableName;
	}
	
	public void setName(String n){
		name = n;
	}
	
	public String getName(){
		return name;
	}

	
	public IntKey getNameLength(){
		return characters;
	}
	
	@Override
	public int hashCode(){
		return name.hashCode();
	}

	@Override
	public int compareTo(Person o) {
		return name.compareTo(o.name);
	}

	public int getSpaces(){
		return spaces;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	/**
	 * To test the reliability of the implemented methods.
	 * @param args
	 */
	public static void main(String[] args){
		Person p1  = new Person("            ", "1365");
		Person p2  = new Person("     a      ", "1366");
		Person p3  = new Person(" 28 characters 7 words and 6              spaces        ", "1367");
		System.out.println("t1 : "+p1.getName());
		System.out.println("characters -1: "+p1.getNameLength());
		System.out.println("spaces 0: "+p1.getSpaces());
		System.out.println("t2 a: "+p2.getName());
		System.out.println("characters 1: "+p2.getNameLength());
		System.out.println("spaces 0: "+p2.getSpaces());
		System.out.println("t3  28 characters 7 words and 6              spaces        : "+p3.getName());
		System.out.println("characters 28: "+p3.getNameLength());
		System.out.println("spaces 6: "+p3.getSpaces());
	}
}
