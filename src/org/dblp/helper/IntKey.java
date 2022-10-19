/**
 * 
 */
package org.dblp.helper;

/**
 * A wrapper class for the int primitive type.<br/>
 * It is used as key inside maps.
 * @author SergeOliver
 *
 */
public class IntKey implements Comparable<IntKey>{

	
	/**
	 * int value to be wrapped.
	 */
	private int key;
	
	public IntKey(int k){
		key = k;
	}
	
	/**
	 * @return the wrapped value
	 */
	public int getIntKeyValue(){
		return key;
	}
	
	public void setIntKeyValue(int k){
		key = k;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null || getClass() != o.getClass()) return false;
		IntKey k = (IntKey) o;
		return key == k.getIntKeyValue();
	}
	
	@Override
	public int hashCode(){
		return key;
	}

	@Override
	public int compareTo(IntKey o) {
		// TODO Auto-generated method stub
		return key - o.getIntKeyValue();
	}
	
	@Override
	public String toString(){
		return key+"";
	}
}
