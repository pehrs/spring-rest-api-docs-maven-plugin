package com.pehrs.spring.api.doc.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MethodDesc {

	String name;
	String requestMappingUrl;
	String requestMappingMethod;
	
	String responseJSONSample = "";
	String requestJSONSample = "";

	String commentText = "";
	
	List<ParameterDesc> parameters = new ArrayList<ParameterDesc>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRequestMappingUrl() {
		return requestMappingUrl;
	}

	public void setRequestMappingUrl(String requestMappingUrl) {
		this.requestMappingUrl = requestMappingUrl;
	}

	public String getRequestMappingMethod() {
		return requestMappingMethod;
	}

	public void setRequestMappingMethod(String requestMappingMethod) {
		this.requestMappingMethod = requestMappingMethod;
	}

	public String getResponseJSONSample() {
		return responseJSONSample;
	}

	public void setResponseJSONSample(String responseJSONSample) {
		this.responseJSONSample = responseJSONSample;
	}

	public String getRequestJSONSample() {
		return requestJSONSample;
	}

	public void setRequestJSONSample(String requestJSONSample) {
		this.requestJSONSample = requestJSONSample;
	}

	public void addParameter(ParameterDesc parameter) {
		parameters.add(parameter);
	}

	public List<ParameterDesc> getParameters() {
		return parameters;
	}

	public void setParameters(List<ParameterDesc> parameters) {
		this.parameters = parameters;
	}

	public Iterator<ParameterDesc> getParametersIterator() {
		return parameters.iterator();
	}
	
	public Iterator<ParameterDesc> getRequestParameterIterator() {
		ArrayList<ParameterDesc> list = new ArrayList<ParameterDesc>();
		
		for(ParameterDesc parameter: parameters) {
			if(parameter.isRequestParam()) {
				list.add(parameter);
			}
		}
		
		return list.iterator();
	}
	public boolean hasRequestParameters() {		
		for(ParameterDesc parameter: parameters) {
			if(parameter.isRequestParam()) {
				return true;
			}
		}
		
		return false;
	}

	public Iterator<ParameterDesc> getPathVariableIterator() {
		ArrayList<ParameterDesc> list = new ArrayList<ParameterDesc>();
		
		for(ParameterDesc parameter: parameters) {
			if(parameter.isPathVariable()) {
				list.add(parameter);
			}
		}
		
		return list.iterator();
	}
	public boolean hasPathVariables() {
		
		for(ParameterDesc parameter: parameters) {
			if(parameter.isPathVariable()) {
				return true;
			}
		}
		
		return false;
	}

	public ParameterDesc getRequestBodyParameter() {
		for(ParameterDesc parameter: parameters) {
			if(parameter.isRequestBody()) {
				return parameter;
			}
		}
		
		return null;
	}

	public boolean hasRequestBodyParameter() {
		for(ParameterDesc parameter: parameters) {
			if(parameter.isRequestBody()) {
				return true;
			}
		}
		
		return false;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
	
	public String getCommentTextFirstSentence() {
		if(commentText==null) {
			return "";
		}
		int index = commentText.indexOf('.');
		if(index!=-1) {
			return commentText.substring(0, index + 1);
		}
		return commentText;
	}

	public String getCommentTextWithNoAnnotations() {
		if(commentText==null) {
			return "";
		}
		
		StringBuilder out = new StringBuilder();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new StringReader(commentText));
			for(String line = in.readLine(); line!=null;line=in.readLine()) {
				String trimmedLine = line.trim();
				if(!trimmedLine.startsWith("@")) {
					out.append(line).append("\n");
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if(in!=null) {
					in.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return out.toString();
	}


	@Override
	public String toString() {
		return "MethodDesc [name=" + name + ", requestMappingUrl="
				+ requestMappingUrl + ", requestMappingMethod="
				+ requestMappingMethod + ", responseJSONSample="
				+ responseJSONSample + ", requestJSONSample="
				+ requestJSONSample + ", commentText=" + commentText
				+ ", parameters=" + parameters + "]";
	}
	
	
}
