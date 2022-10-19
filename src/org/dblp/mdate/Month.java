package org.dblp.mdate;

/**
 * Contains the twelve months of an year.
 * @author SergeOliver
 *
 */
public enum Month {
	
	JANUARY(1),
	FEBRUARY(2),
	MARCH(3),
	APRIL(4),
	MAY(5),
	JUNI(6),
	JULY(7),
	AUGUST(8),
	SEPTEMBER(9),
	OKTOBER(10),
	NOVEMBER(11),
	DECEMBER(12);
	
	private final int month;
	
	private Month(int m){
		month = m;
	}
	
	public boolean sameMonth(int otherMonth){
		if(otherMonth < 1 || otherMonth > 12 ) return false;
		return month == otherMonth;
	}
	
	public String toString(){
		return String.valueOf(month);
	}
}
