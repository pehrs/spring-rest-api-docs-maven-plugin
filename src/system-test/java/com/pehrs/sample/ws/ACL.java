package com.pehrs.sample.ws;

import org.joda.time.DateTime;
import java.net.URI;

/**
 * Value object used during the function test of the maven plugin
 * 
 * @author matti
 *
 */
public class ACL {
	
	public final URI uri;
	public final String requestId;
	public final int returnCode;
	public final DateTime timestamp;
	public final String data;

	
	public ACL(URI uri, String requestId, int returnCode, DateTime timestamp, String data) {
		super();
		this.uri = uri;
		this.requestId = requestId;
		this.returnCode = returnCode;
		this.timestamp = timestamp;
		this.data = data;
	}

	public String getRequestId() {
		return requestId;
	}

	public int getReturnCode() {
		return returnCode;
	}


	public DateTime getTimestamp() {
		return timestamp;
	}


	public String getData() {
		return data;
	}
	
}
