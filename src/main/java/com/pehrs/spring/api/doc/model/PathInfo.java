package com.pehrs.spring.api.doc.model;


public class PathInfo implements Comparable<PathInfo>{
	private final ControllerDesc controller;
	public final MethodDesc method;
	public final String requestPath;
	public final String requestMethod;
	public final String controllerPath;
	public final String methodId;
	
	public PathInfo(ControllerDesc controller, MethodDesc method, String requestPath, String requestMethod,
			String controllerPath, String methodId) {
		super();
		this.controller = controller;
		this.method = method;
		this.requestPath = requestPath;
		this.requestMethod = requestMethod;
		this.controllerPath = controllerPath;
		this.methodId = methodId;
	}
	
	public String getBootstrapRequestMethodBtnStyle() {
		if(requestMethod==null) {
			return "";
		}
		if(requestMethod.equals("POST")) {
			return "btn-success";
		}
		if(requestMethod.equals("GET")) {
			return "btn-primary";
		}
		if(requestMethod.equals("PUT")) {
			return "btn-warning";
		}
		if(requestMethod.equals("DELETE")) {
			return "btn-danger";
		}
		return "";
	}
	
	public MethodDesc getMethod() {
		return method;
	}

	public String getRequestPath() {
		return requestPath;
	}



	public String getRequestMethod() {
		return requestMethod;
	}



	public String getControllerPath() {
		return controllerPath;
	}



	public String getMethodId() {
		return methodId;
	}



	public String getHref() {
		return this.controllerPath+"#"+methodId;
	}

	@Override
	public int compareTo(PathInfo other) {
		return (this.requestPath+this.requestMethod).compareTo((other.requestPath+other.requestMethod));
	}



	@Override
	public String toString() {
		return "PathInfo [requestPath=" + requestPath + ", requestMethod="
				+ requestMethod + ", controllerPath=" + controllerPath
				+ ", methodId=" + methodId + "]";
	}

	
}

