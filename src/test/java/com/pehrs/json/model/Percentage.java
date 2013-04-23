package com.pehrs.json.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * this is a percentage value wich is represented by an integer from 0 to 1000 which represents 0 to 100.0 percent (%)
 * 
 * @author matti
 *
 */
public class Percentage {

	private Integer value;
	
	public Percentage() {
		// Have to make a DTO of this as Jackson will not work with it otherwise
	}
	
	public Percentage(Integer val) {
		this.value = val;		
	}
	
	private static final Pattern VALUE_PATTERN = Pattern.compile("([0-9]+)(\\.[0-9])?");

	public Percentage(String str) {
		Matcher matcher = VALUE_PATTERN.matcher(str);
		if(!matcher.matches()) {
			throw new IllegalArgumentException("'"+str+"' is not a "+Percentage.class.getSimpleName());
		}
		String g1 = matcher.group(1);
		if(g1==null) {
			throw new IllegalArgumentException("'"+str+"' is not a "+Percentage.class.getSimpleName());
		}
		value = 10 * Integer.parseInt(g1);
		String g2 = matcher.group(2);
		if(g2!=null) {
			value += Integer.parseInt(g2.substring(1));
		}
	}
	
	public Percentage add(double val) {
		return new Percentage(value + (((int)val) * 10));
	}

	public Percentage subtract(double val) {
		return new Percentage(value - (((int)val) * 10));
	}
	
	public void setValue(Integer val) {
		this.value = val;
	}

	public Integer getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Percentage other = (Percentage) obj;
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ""+(value / 10) + "."+(value % 10);
	}
}
