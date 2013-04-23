/*
 * Copyright (c) 2012 InInBo Inc. All Rights Reserved.
 */
package com.pehrs.json.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * The Event Entity
 * 
 * Table [name=Event, remarks=, isRelationship=false]
 */
public class Event implements Serializable {

	private static final long serialVersionUID = 5903089663176133625L;

	// Default Constructor
	public Event() {
	}

	/** VARCHAR id */
	private String id;
	/** TIMESTAMP date_created */
	private Timestamp dateCreated;
	/** TIMESTAMP updated */
	private Timestamp updated;
	/** TIMESTAMP deleted */
	private Timestamp deleted;
	/** TEXT location_text */
	private String locationText;
	/** DATETIME start_date_time */
	private org.joda.time.DateTime startDateTime;
	/** DATETIME end_date_time */
	private org.joda.time.DateTime endDateTime;
	/** VARCHAR title */
	private String title;
	/** TEXT description */
	private String description;
	/** VARCHAR event_type */
	private EventType eventType;
	/** VARCHAR Course_id */
	private String courseId;
	/** INT hours_required */
	private Integer hoursRequired;
	/** VARCHAR event_state */
	private EventState eventState;

	/**
	 * Column [typeName=VARCHAR, columName=id, primaryKey=true, columSqlType=12,
	 * columnSize=255, decimalDigits=0, nullable=false, remarks=,
	 * ordinalPosition=1, isMapped=false, mappedTypeName=null]
	 */

	public String getId() {
		return this.id;
	}

	/**
	 * VARCHAR id
	 * 
	 * <br/>
	 * <b>Required Field</b>
	 */
	public void setId(String value) {
		this.id = value;
	}

	/**
	 * Column [typeName=TIMESTAMP, columName=date_created, primaryKey=false,
	 * columSqlType=93, columnSize=19, decimalDigits=0, nullable=false,
	 * remarks=, ordinalPosition=2, isMapped=false, mappedTypeName=null]
	 */

	public Timestamp getDateCreated() {
		return this.dateCreated;
	}

	/**
	 * TIMESTAMP date_created
	 * 
	 * <br/>
	 * <b>Required Field</b>
	 */
	public void setDateCreated(Timestamp value) {
		this.dateCreated = value;
	}

	/**
	 * Column [typeName=TIMESTAMP, columName=updated, primaryKey=false,
	 * columSqlType=93, columnSize=19, decimalDigits=0, nullable=true, remarks=,
	 * ordinalPosition=3, isMapped=false, mappedTypeName=null]
	 */

	public Timestamp getUpdated() {
		return this.updated;
	}

	/**
	 * TIMESTAMP updated
	 * 
	 */
	public void setUpdated(Timestamp value) {
		this.updated = value;
	}

	/**
	 * Column [typeName=TIMESTAMP, columName=deleted, primaryKey=false,
	 * columSqlType=93, columnSize=19, decimalDigits=0, nullable=true, remarks=,
	 * ordinalPosition=4, isMapped=false, mappedTypeName=null]
	 */

	public Timestamp getDeleted() {
		return this.deleted;
	}

	/**
	 * TIMESTAMP deleted
	 * 
	 */
	public void setDeleted(Timestamp value) {
		this.deleted = value;
	}


	/**
	 * Column [typeName=TEXT, columName=location_text, primaryKey=false,
	 * columSqlType=-1, columnSize=65535, decimalDigits=0, nullable=true,
	 * remarks=, ordinalPosition=6, isMapped=false, mappedTypeName=null]
	 */

	public String getLocationText() {
		return this.locationText;
	}

	/**
	 * TEXT location_text
	 * 
	 */
	public void setLocationText(String value) {
		this.locationText = value;
	}

	/**
	 * Column [typeName=DATETIME, columName=start_date_time, primaryKey=false,
	 * columSqlType=93, columnSize=19, decimalDigits=0, nullable=true, remarks=,
	 * ordinalPosition=7, isMapped=true, mappedTypeName=org.joda.time.DateTime]
	 */

	public org.joda.time.DateTime getStartDateTime() {
		return this.startDateTime;
	}

	/**
	 * Column [typeName=DATETIME, columName=start_date_time, primaryKey=false,
	 * columSqlType=93, columnSize=19, decimalDigits=0, nullable=true, remarks=,
	 * ordinalPosition=7, isMapped=true, mappedTypeName=org.joda.time.DateTime]
	 */

	public void setStartDateTime(org.joda.time.DateTime value) {
		this.startDateTime = value;
	}

	/**
	 * Column [typeName=DATETIME, columName=end_date_time, primaryKey=false,
	 * columSqlType=93, columnSize=19, decimalDigits=0, nullable=true, remarks=,
	 * ordinalPosition=8, isMapped=true, mappedTypeName=org.joda.time.DateTime]
	 */

	public org.joda.time.DateTime getEndDateTime() {
		return this.endDateTime;
	}

	/**
	 * Column [typeName=DATETIME, columName=end_date_time, primaryKey=false,
	 * columSqlType=93, columnSize=19, decimalDigits=0, nullable=true, remarks=,
	 * ordinalPosition=8, isMapped=true, mappedTypeName=org.joda.time.DateTime]
	 */

	public void setEndDateTime(org.joda.time.DateTime value) {
		this.endDateTime = value;
	}

	/**
	 * Column [typeName=VARCHAR, columName=title, primaryKey=false,
	 * columSqlType=12, columnSize=128, decimalDigits=0, nullable=true,
	 * remarks=, ordinalPosition=9, isMapped=false, mappedTypeName=null]
	 */

	public String getTitle() {
		return this.title;
	}

	/**
	 * VARCHAR title
	 * 
	 */
	public void setTitle(String value) {
		this.title = value;
	}

	/**
	 * Column [typeName=TEXT, columName=description, primaryKey=false,
	 * columSqlType=-1, columnSize=65535, decimalDigits=0, nullable=true,
	 * remarks=, ordinalPosition=10, isMapped=false, mappedTypeName=null]
	 */

	public String getDescription() {
		return this.description;
	}

	/**
	 * TEXT description
	 * 
	 */
	public void setDescription(String value) {
		this.description = value;
	}

	/**
	 * Lecture,Lab,Exam,Assignment
	 */

	public EventType getEventType() {
		return this.eventType;
	}

	/**
	 * Lecture,Lab,Exam,Assignment
	 */

	public void setEventType(EventType value) {
		this.eventType = value;
	}

	/**
	 * Column [typeName=VARCHAR, columName=Course_id, primaryKey=false,
	 * columSqlType=12, columnSize=255, decimalDigits=0, nullable=false,
	 * remarks=, ordinalPosition=12, isMapped=false, mappedTypeName=null] <br/>
	 * <b>FOREIGN KEY</b>
	 */

	public String getCourseId() {
		return this.courseId;
	}

	/**
	 * VARCHAR Course_id
	 * 
	 * <br/>
	 * <b>Required Field</b> <br/>
	 * <b>FOREIGN KEY</b>
	 */
	public void setCourseId(String value) {
		this.courseId = value;
	}

	/**
	 * Number of hours required for this event
	 */

	public Integer getHoursRequired() {
		return this.hoursRequired;
	}

	/**
	 * INT hours_required
	 * 
	 */
	public void setHoursRequired(Integer value) {
		this.hoursRequired = value;
	}

	/**
	 * One of: Draft, Published
	 */

	public EventState getEventState() {
		return this.eventState;
	}


	/**
	 * One of: Draft, Published
	 */

	public void setEventState(EventState value) {
		this.eventState = value;
	}


	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		out.append(this.getClass().getSimpleName());
		out.append(" {");
		out.append("id=");
		out.append("" + id);
		out.append(" ");
		out.append("dateCreated=");
		out.append("" + dateCreated);
		out.append(" ");
		out.append("updated=");
		out.append("" + updated);
		out.append(" ");
		out.append("deleted=");
		out.append("" + deleted);
		out.append(" ");
		out.append("locationText=");
		out.append("" + locationText);
		out.append(" ");
		out.append("startDateTime=");
		out.append("" + startDateTime);
		out.append(" ");
		out.append("endDateTime=");
		out.append("" + endDateTime);
		out.append(" ");
		out.append("title=");
		out.append("" + title);
		out.append(" ");
		out.append("description=");
		out.append("" + description);
		out.append(" ");
		out.append("eventType=");
		out.append("" + eventType);
		out.append(" ");
		out.append("courseId=");
		out.append("" + courseId);
		out.append(" ");
		out.append("hoursRequired=");
		out.append("" + hoursRequired);
		out.append(" ");
		out.append("eventState=");
		out.append("" + eventState);
		out.append(" ");
		out.append("}");
		return out.toString();
	}

}
