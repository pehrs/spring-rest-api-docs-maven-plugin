/*
 * Copyright (c) 2012 InInBo Inc. All Rights Reserved.
 */
package com.pehrs.json.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Sample Entity
 * 
 * Table [name=Sample, remarks=, isRelationship=false]
 */
public class SampleClass implements Serializable {

    private static final long serialVersionUID = -4463856041344937736L;


    // Default Constructor
    public SampleClass() {
    }

	/** VARCHAR id */
  private String id;
	/** TIMESTAMP date_created */
  private Timestamp dateCreated;
	/** TIMESTAMP updated */
  private Timestamp updated;
	/** TIMESTAMP deleted */
  private Timestamp deleted;
	/** VARCHAR type_of_exam */
  private SampleType typeOfSample;
	/** BIT reviewed */
  private Boolean reviewed;
	/** INT percent_of_final_exam_grade */
  private Percentage percentOfFinalExamGrade;







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
   * One of: 'Timed','Online','Classroom'
   */

  public SampleType getTypeOfSample() {
    return  this.typeOfSample;
  }


    /**
   * One of: 'Timed','Online','Classroom'
   */

  public void setTypeOfSample(SampleType value) {
    this.typeOfSample = value;
  }



    /**
   * Column [typeName=BIT, columName=reviewed, primaryKey=false, columSqlType=-7, columnSize=0, decimalDigits=0, nullable=false, remarks=, ordinalPosition=6, isMapped=false, mappedTypeName=null]
   */

  public Boolean getReviewed() {
    return  this.reviewed;
  }

  /**
   * BIT reviewed
   *
   * <br/><b>Required Field</b>
   */
  public void setReviewed(Boolean value) {
    this.reviewed = value;
  }









    /**
   * Column [typeName=INT, columName=percent_of_final_exam_grade, primaryKey=false, columSqlType=4, columnSize=10, decimalDigits=0, nullable=true, remarks=, ordinalPosition=8, isMapped=true, mappedTypeName=Percentage]
   */

  public Percentage getPercentOfFinalExamGrade() {
    return  this.percentOfFinalExamGrade;
  }

    /**
   * Column [typeName=INT, columName=percent_of_final_exam_grade, primaryKey=false, columSqlType=4, columnSize=10, decimalDigits=0, nullable=true, remarks=, ordinalPosition=8, isMapped=true, mappedTypeName=Percentage]
   */

  public void setPercentOfFinalExamGrade(Percentage value) {
    this.percentOfFinalExamGrade = value;
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
    out.append("typeOfSample=");
    out.append(""+typeOfSample);
    out.append(" ");
    out.append("reviewed=");
    out.append(""+reviewed);
    out.append(" ");
    out.append("percentOfFinalExamGrade=");
    out.append(""+percentOfFinalExamGrade);
    out.append(" ");
    out.append("}");
    return out.toString();
  }

}

