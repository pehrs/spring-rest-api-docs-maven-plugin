package com.pehrs.spring.api.doc.model;

/**
 * Parameter info
 * 
 * @author matti
 *
 */
public class ParameterDesc {

	String name;
	String typeName;
	boolean requestParam = false;
	boolean requestParamRequired;
	String requestParamDefaultValue=null;
	String requestParamValue = null;
	
	boolean pathVariable = false;
	String pathVariableValue = "";
	
	boolean requestBody = false;
	String requestBodyJSONSample="";
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String type) {
		this.typeName = type;
	}

	public boolean isRequestParamRequired() {
		return requestParamRequired;
	}

	public void setRequestParamRequired(boolean requestParamRequired) {
		this.requestParamRequired = requestParamRequired;
	}

	public String getRequestParamDefaultValue() {
		if(requestParamDefaultValue==null) return "";
		return requestParamDefaultValue;
	}

	public void setRequestParamDefaultValue(String requestParamDefaultValue) {
		this.requestParamDefaultValue = requestParamDefaultValue;
	}

	public String getRequestParamValue() {
		if(requestParamValue==null) {
			return name;
		}
		return requestParamValue;
	}

	public void setRequestParamValue(String requestParamValue) {
		this.requestParamValue = requestParamValue;
	}

	public String getRequestBodyJSONSample() {
		return requestBodyJSONSample;
	}

	public void setRequestBodyJSONSample(String requestBodyJSONSample) {
		this.requestBodyJSONSample = requestBodyJSONSample;
	}

	public boolean isRequestParam() {
		return requestParam;
	}

	public void setRequestParam(boolean requestParam) {
		this.requestParam = requestParam;
	}

	public boolean isRequestBody() {
		return requestBody;
	}

	public void setRequestBody(boolean requestBody) {
		this.requestBody = requestBody;
	}

	public boolean isPathVariable() {
		return pathVariable;
	}

	public void setPathVariable(boolean pathParam) {
		this.pathVariable = pathParam;
	}

	public String getPathVariableValue() {
		return pathVariableValue;
	}

	public void setPathVariableValue(String pathParamValue) {
		if(pathParamValue==null) {
			pathVariableValue = "";
			return;
		}
		this.pathVariableValue = pathParamValue;
	}

	@Override
	public String toString() {
		return "ParameterDesc [name=" + name + ", typeName=" + typeName
				+ ", requestParam=" + requestParam + ", requestParamRequired="
				+ requestParamRequired + ", requestParamDefaultValue="
				+ requestParamDefaultValue + ", requestParamValue="
				+ requestParamValue + ", pathParam=" + pathVariable
				+ ", pathParamValue=" + pathVariableValue + ", requestBody="
				+ requestBody + ", requestBodyJSONSample="
				+ requestBodyJSONSample + "]";
	}

	
}
