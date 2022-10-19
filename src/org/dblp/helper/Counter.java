package org.dblp.helper;

import java.util.Comparator;

/**
 * A wrapper class for the int primitive type.<br/>
 * It is used as counter Object inside maps.
 * @author SergeOliver
 *
 */
public class Counter implements Comparator<Counter>, Comparable<Counter> {
	
	private int number = 0;
	
	public Counter(){
		number = 1;
	}
	
	/**
	 * ++number
	 */
	public void increment(){
		++number;
	}
	
	/**
	 * --number
	 */
	public void decrement(){
		--number;
	}
	
	public int getCounterValue(){
		return number;
	}
	
	public void setCounterValue(int number){
		this.number = number;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null || !(o instanceof Counter)) return false;
		Counter c = (Counter)o;
		return number == c.number;
	}
	
	@Override
	public int hashCode(){
		return number;
	}
	
	@Override
	public int compareTo(Counter o) {
		return getCounterValue() - o.getCounterValue();
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Counter o1, Counter o2) {
		return o1.getCounterValue() - o2.getCounterValue();
	}
	
	@Override
	public String toString(){
		return number+"";
	}
}
