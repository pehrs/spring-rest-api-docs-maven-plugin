package com.pehrs.spring.api.doc.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PkgDesc implements Comparable<PkgDesc>{
	
	private String name;
	
	List<ControllerDesc> controllers = new ArrayList<ControllerDesc>();


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}



	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}



	/**
	 * @return the controllers
	 */
	public List<ControllerDesc> getControllers() {
		return controllers;
	}



	/**
	 * @param controllers the controllers to set
	 */
	public void setControllers(List<ControllerDesc> controllers) {
		this.controllers = controllers;
	}

	public void addController(ControllerDesc d) {
		this.controllers.add(d);
	}

	public void removeController(ControllerDesc d) {
		this.controllers.remove(d);
	}

	public Iterator<ControllerDesc> getControllerIterator() {
		return controllers.iterator();
	}

	@Override
	public int compareTo(PkgDesc other) {
		return name.compareTo(other.name);
	}

}
