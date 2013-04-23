package com.pehrs.json.model;

import java.io.Serializable;

public class GenericSample<T> implements Serializable {
	
	private static final long serialVersionUID = -531209634131137877L;
	
	private String id;
	private T data;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GenericSample [id=" + id + ", data=" + data + "]";
	}
	
}
