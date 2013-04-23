/*
 * Copyright (c) 2012 InInBo Inc. All Rights Reserved.
 */
package com.pehrs.json.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * The ExamGradePlan Entity
 * 
 * Table [name=ExamGradePlan, remarks=, isRelationship=false]
 */
public class Details implements Serializable {

    private static final long serialVersionUID = 3653838307855859964L;


    // Default Constructor
    public Details() {
    }

	/** VARCHAR id */
  private String id;
	/** TIMESTAMP date_created */
  private Timestamp dateCreated;
	/** TIMESTAMP updated */
  private Timestamp updated;
	/** TIMESTAMP deleted */
  private Timestamp deleted;
	/** VARCHAR Exam_id */
  private String examId;
	/** VARCHAR grade */
  private Level grade;
	/** INT min_percentage */
  private Percentage minPercentage;
	/** INT max_percentage */
  private Percentage maxPercentage;







    /**
   * Column [typeName=VARCHAR, columName=id, primaryKey=true, columSqlType=12, columnSize=255, decimalDigits=0, nullable=false, remarks=, ordinalPosition=1, isMapped=false, mappedTypeName=null]
   */

  public String getId() {
    return  this.id;
  }

  /**
   * VARCHAR id
   *
   * <br/><b>Required Field</b>
   */
  public void setId(String value) {
    this.id = value;
  }







    /**
   * Column [typeName=TIMESTAMP, columName=date_created, primaryKey=false, columSqlType=93, columnSize=19, decimalDigits=0, nullable=false, remarks=, ordinalPosition=2, isMapped=false, mappedTypeName=null]
   */

  public Timestamp getDateCreated() {
    return  this.dateCreated;
  }

  /**
   * TIMESTAMP date_created
   *
   * <br/><b>Required Field</b>
   */
  public void setDateCreated(Timestamp value) {
    this.dateCreated = value;
  }







    /**
   * Column [typeName=TIMESTAMP, columName=updated, primaryKey=false, columSqlType=93, columnSize=19, decimalDigits=0, nullable=true, remarks=, ordinalPosition=3, isMapped=false, mappedTypeName=null]
   */

  public Timestamp getUpdated() {
    return  this.updated;
  }

  /**
   * TIMESTAMP updated
   *
   */
  public void setUpdated(Timestamp value) {
    this.updated = value;
  }







    /**
   * Column [typeName=TIMESTAMP, columName=deleted, primaryKey=false, columSqlType=93, columnSize=19, decimalDigits=0, nullable=true, remarks=, ordinalPosition=4, isMapped=false, mappedTypeName=null]
   */

  public Timestamp getDeleted() {
    return  this.deleted;
  }

  /**
   * TIMESTAMP deleted
   *
   */
  public void setDeleted(Timestamp value) {
    this.deleted = value;
  }







    /**
   * Column [typeName=VARCHAR, columName=Exam_id, primaryKey=false, columSqlType=12, columnSize=255, decimalDigits=0, nullable=false, remarks=, ordinalPosition=5, isMapped=false, mappedTypeName=null]
   * <br/><b>FOREIGN KEY</b>
   */

  public String getExamId() {
    return  this.examId;
  }

  /**
   * VARCHAR Exam_id
   *
   * <br/><b>Required Field</b>
   * <br/><b>FOREIGN KEY</b>
   */
  public void setExamId(String value) {
    this.examId = value;
  }







    /**
   * Guide for the instructor on how to grade
   */

  public Level getGrade() {
    return  this.grade;
  }

    /**
   * Guide for the instructor on how to grade
   */

  public void setGrade(Level value) {
    this.grade = value;
  }








    /**
   * Column [typeName=INT, columName=min_percentage, primaryKey=false, columSqlType=4, columnSize=10, decimalDigits=0, nullable=false, remarks=, ordinalPosition=7, isMapped=true, mappedTypeName=Percentage]
   */

  public Percentage getMinPercentage() {
    return  this.minPercentage;
  }

    /**
   * Column [typeName=INT, columName=min_percentage, primaryKey=false, columSqlType=4, columnSize=10, decimalDigits=0, nullable=false, remarks=, ordinalPosition=7, isMapped=true, mappedTypeName=Percentage]
   */

  public void setMinPercentage(Percentage value) {
    this.minPercentage = value;
  }






    /**
   * Column [typeName=INT, columName=max_percentage, primaryKey=false, columSqlType=4, columnSize=10, decimalDigits=0, nullable=false, remarks=, ordinalPosition=8, isMapped=true, mappedTypeName=Percentage]
   */

  public Percentage getMaxPercentage() {
    return  this.maxPercentage;
  }


    /**
   * Column [typeName=INT, columName=max_percentage, primaryKey=false, columSqlType=4, columnSize=10, decimalDigits=0, nullable=false, remarks=, ordinalPosition=8, isMapped=true, mappedTypeName=Percentage]
   */

  public void setMaxPercentage(Percentage value) {
    this.maxPercentage = value;
  }




  @Override
  public String toString() {
    StringBuilder out = new StringBuilder();
    out.append(this.getClass().getSimpleName());
    out.append(" {");
    out.append("id=");
    out.append(""+id);
    out.append(" ");
    out.append("dateCreated=");
    out.append(""+dateCreated);
    out.append(" ");
    out.append("updated=");
    out.append(""+updated);
    out.append(" ");
    out.append("deleted=");
    out.append(""+deleted);
    out.append(" ");
    out.append("examId=");
    out.append(""+examId);
    out.append(" ");
    out.append("grade=");
    out.append(""+grade);
    out.append(" ");
    out.append("minPercentage=");
    out.append(""+minPercentage);
    out.append(" ");
    out.append("maxPercentage=");
    out.append(""+maxPercentage);
    out.append(" ");
    out.append("}");
    return out.toString();
  }


}

