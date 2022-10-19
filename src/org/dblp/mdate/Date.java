package org.dblp.mdate;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.dblp.helper.Counter;

/**
 * Counts the number of modifications per year, month and year.
 * VIP: checks if there unreliable modification dates for a later analysis.
 * e.g. month < 1 or month > 12 or day < 1 or day > 31 or year.length() != 4
 * @author SergeOliver
 *
 */
public class Date {
	
	String year;
	String month;
	String day;
	String key;
	
	public static Map<String, Counter> mYears = new HashMap<>();
	public static double[] mMonths = new double[12];
	public static int[] mDays = new int[31];
	public static Set<String> setOfUnreliableDates = new TreeSet<>();

	public Date(String y, int m, int d, String key){
		this.key = key;
		if(m < 1 || m > 12 || d < 1 || d > 31 || y.length() != 4){
			setOfUnreliableDates.add("[Key: "+key+"] [Date: "+y+"-"+m+"-"+d+"]");
		}
		if(!mYears.containsKey(y))
			mYears.put(y, new Counter());
		else{
			mYears.get(y).increment();
		}
		mMonths[m-1] += 1;
		mDays[d-1] += 1;
	}
	
	public String getYear() {
		return year;
	}


	public void setYear(String year) {
		this.year = year;
	}


	public String getMonth() {
		return month;
	}


	public void setMonth(String month) {
		this.month = month;
	}


	public String getDay() {
		return day;
	}


	public void setDay(String day) {
		this.day = day;
	}
	
	
	public String toString(){
		return year+"-"+month+"-"+day;
	}
	
	
}