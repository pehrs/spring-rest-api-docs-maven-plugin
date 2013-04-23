/**
 * Copyright (c) 2012 by InInBo Inc.
 * All rights reserved
 */
package com.pehrs.json.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author matti
 * 
 */
public class SampleClassWithDetails {

	private SampleClass exam;
	private Event event;
	private List<Details> details = new ArrayList<Details>();
	
	private Map<String,Level> levels = new HashMap<String, Level>();
	
	private GenericSample<String> sample = new GenericSample<String>();

	public SampleClassWithDetails() {

	}
	
	
	/**
	 * @return the sample
	 */
	public GenericSample<String> getSample() {
		return sample;
	}




	/**
	 * @param sample the sample to set
	 */
	public void setSample(GenericSample<String> sample) {
		this.sample = sample;
	}




	/**
	 * @return the levels
	 */
	public Map<String, Level> getLevels() {
		return levels;
	}



	/**
	 * @param levels the levels to set
	 */
	public void setLevels(Map<String, Level> levels) {
		this.levels = levels;
	}



	/**
	 * @return the exam
	 */
	public SampleClass getExam() {
		return exam;
	}

	/**
	 * @param exam
	 *            the exam to set
	 */
	public void setExam(SampleClass exam) {
		this.exam = exam;
	}

	/**
	 * @return the event
	 */
	public Event getEvent() {
		return event;
	}

	/**
	 * @param event
	 *            the event to set
	 */
	public void setEvent(Event event) {
		this.event = event;
	}

	/**
	 * @return the gradePlan
	 */
	public List<Details> getDetails() {
		return details;
	}

	/**
	 * @param gradePlan the gradePlan to set
	 */
	public void setDetails(List<Details> gradePlan) {
		this.details = gradePlan;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SampleClassWithDetails [exam=" + exam + ", event=" + event
				+ ", details=" + details + ", levels=" + levels + ", sample="
				+ sample + "]";
	}



}
