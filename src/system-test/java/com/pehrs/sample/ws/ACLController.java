package com.pehrs.sample.ws;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.URI;
import java.net.URISyntaxException;

import org.joda.time.DateTime;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Test controller for function test of the maven plugin
 * 
 * @author matti
 *
 */
@Controller
@RequestMapping("/core")
public class ACLController {

	/**
     * Create a new ACL
     */
    @RequestMapping(value = "/ACL", method = RequestMethod.POST)
    public @ResponseBody ACL post(
	              final HttpServletRequest request, 
				  final HttpServletResponse response,
				  @RequestBody ACL entity)
	throws IOException, URISyntaxException {
	
	    // ... the code
	
	return new ACL(new URI("http://java.sun.com/j2se/1.3"), "requestId", 42, DateTime.parse("2010-10-10"), "data");
    }
    
	/**
     * GET a new ACL
     */
    @RequestMapping(value = "/ACL", method = RequestMethod.GET)
    public @ResponseBody ACL get(
	              final HttpServletRequest request, 
				  final HttpServletResponse response)
	throws IOException, URISyntaxException {
	
	    // ... the code
	
	    return new ACL(new URI("http://java.sun.com/j2se/1.3"), "requestId", 42, DateTime.parse("2010-10-10"), "data");
    }

	/**
     * Update a new ACL
     */
    @RequestMapping(value = "/ACL", method = RequestMethod.PUT)
    public @ResponseBody ACL put(
	              final HttpServletRequest request, 
				  final HttpServletResponse response,
				  @RequestBody ACL entity)
	throws IOException, URISyntaxException {
	
	    // ... the code
	
	    return new ACL(new URI("http://java.sun.com/j2se/1.3"), "requestId", 42, DateTime.parse("2010-10-10"), "data");
    }
    
    /**
     * Delete
     */
    @RequestMapping(value = "/ACL/{aclId}", method = RequestMethod.DELETE)
    public void delete(final HttpServletRequest request, 
		       final HttpServletResponse response,
		       @PathVariable("aclId") String aclId
		       )
	throws IOException, URISyntaxException {
	
	    // ... the code
	
    }

}
