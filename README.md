<link href="docs/css/markdown.css" rel="stylesheet"></link>
<link href="http://google-code-prettify.googlecode.com/svn/trunk/src/prettify.css" type="text/css" rel="stylesheet" />
<style>
table th {
border-bottom: 2px solid black;
}
table td {
padding-top: 0.5em;
padding-right: 0.8em;
vertical-align: top;
border: 1px solid #eee;
}
</style>

# spring-rest-api-docs-maven-plugin

This is a plugin for Maven 2 that uses javadoc and freemarker to generate documentation for SpringFramework MVC Controllers that are used for JSON REST Services.

NOTE: __This is still work-in-progress__



# Install

Get the plugin from github:

<pre class="prettyprint lang-sh">
$ git clone git@github.com:pehrs/spring-rest-api-docs-maven-plugin.git
</pre>

Then install into your local maven repository

<pre class="prettyprint lang-sh">
$ mvn install
</pre>

# Example

Here's a sample Spring Controller which shows the style of controller that the plugin supports.

<!--  linenums -->
<pre class="prettyprint java">

/**
 * Sample controller
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
    
	...
}

</pre>

The sample code can be found in <a href="src/system-test/java/com/pehrs/sample/ws/ACLController.java">src/system-test/java/com/pehrs/sample/ws/ACLController.java</a>

# Usage

Add the following to your pom.xml:

<pre class="prettyprint lang-xml">


&lt;build>
    &lt;plugins>

	...

      &lt;plugin>
        &lt;!-- This plugin generates a HTML dictionary of the Controllers in the ininbo system -->
        &lt;groupId>com.pehrs&lt;/groupId>
        &lt;artifactId>spring-rest-api-docs-maven-plugin&lt;/artifactId>
        &lt;version>1.0.0&lt;/version>
        &lt;configuration>
          &lt;!-- This is where the resulting HTML file ends up -->
          &lt;targetDir>target/sample-rest-api&lt;/targetDir>
          &lt;!-- URL Prefix -->
          &lt;urlPrefix>/api&lt;/urlPrefix>
          &lt;pkgRoot>com.pehrs.sample.ws&lt;/pkgRoot>
          &lt;!-- exclude these classes from the docs -->
          &lt;!-- &lt;excludePattern>com.ininbo.ws.rest.controller.core.*&lt;/excludePattern>-->
          &lt;loggingLevel>INFO&lt;/loggingLevel>
          &lt;typeSamples>
            &lt;!-- Custom samples -->
            &lt;param>java.lang.String=STRING&lt;/param>
            &lt;param>java.net.URI=scheme://Path/to/object#fragment&lt;/param>
            &lt;param>java.sql.Timestamp=2012-10-02T09:00:00+02:00&lt;/param>
            &lt;param>org.joda.time.DateTime=2012-10-04T16:06:55+0200&lt;/param>
            &lt;param>int=42&lt;/param>
            &lt;param>java.lang.Boolean=false&lt;/param>
            &lt;param>java.math.BigDecimal=0.42&lt;/param>
          &lt;/typeSamples>        
        &lt;/configuration>          
      &lt;/plugin>

    ...
    &lt;/plugins>
  &lt;/build>
</pre>

Then run:

<pre class="prettyprint lang-sh">
$ mvn com.pehrs:spring-rest-api-docs-maven-plugin:docs 
</pre>

View the <a href="http://htmlpreview.github.com/?https://github.com/pehrs/spring-rest-api-docs-maven-plugin/blob/master/docs/sample-rest-api/com.pehrs.spring-rest-api-docs-maven-plugin-test-1.0.0.html">Sample Result</a>.

## Plugin configuration

<table>
  <tr>
    <th>Option</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>targetDir</td>
    <td>- The target directory to generate the html documentation to</td>
  </tr>
  <tr>
    <td>pkgRoot</td>
    <td>- The root package to scan for Spring REST Controllers</td>
  </tr>
  <tr>
    <td>loggingLevel</td>
    <td>- The Logging level (OFF, FATAL, ERROR, WARN, INFO, DEBUG, TRACE, ALL) Default is INFO</td>
  </tr>
  
  <tr>
    <td>typeSamples</td>
    <td>
	 - JSON Samples to use for data types<br/>
	  <br/>
	  These will override the generated random values.
	</td>
  </tr>
</table>



# Build 

To build the plugin just run `mvn package`

# Test 

There is a bash script in the root that will run the plugin on a embedded Spring REST test project and compare the result.
Just run:
<pre class="prettyprint lang-sh">
$ ./run-test.sh
</pre>

If you are on an OS that does not support bash-scripts then you can manually do: 
<pre class="prettyprint lang-sh">
$ mvn -f pom-test.xml compile
$ mvn -f pom-test.xml com.pehrs:spring-rest-api-docs-maven-plugin:docs
$ diff docs/sample-rest-api/com.pehrs.spring-rest-api-docs-maven-plugin-test-1.0.0.html target/sample-rest-api/com.pehrs.spring-rest-api-docs-maven-plugin-test-1.0.0.html
</pre>

<script src="http://google-code-prettify.googlecode.com/svn/trunk/src/prettify.js"></script>

<script type="text/javascript">
  
  (function() {
    prettyPrint();
  })();
  
</script>

