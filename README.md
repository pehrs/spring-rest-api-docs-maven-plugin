<link href="docs/css/markdown.css" rel="stylesheet"></link>
<link href="http://google-code-prettify.googlecode.com/svn/trunk/src/prettify.css" type="text/css" rel="stylesheet" />

# spring-rest-api-docs-maven-plugin

This is a plugin for Maven 2 that uses javadoc and freemarker to generate documentation for SpringFramework MVC Controllers that are used for JSON REST Services.

# Example

Here's a sample Spring Controller:
<!--  linenums -->
<pre class="prettyprint java">

@Controller
public class AssignmentController {

/**
 * The ACL JSON Rest Controller
 * 
 */
@Controller
@RequestMapping("/core")
public class ACLController {

   /**
     * Create a new ACL
     */
    @RequestMapping(value = "/ACL", method = RequestMethod.POST)
    public @ResponseBody RestResponse<ACL> insert(
	              final HttpServletRequest request, 
				  final HttpServletResponse response,
				  @RequestBody ACL entity)
	throws IOException {
	
	    // ... the code
	
	    return new RestResponse<ACL>(entity);
    }

    
}

</pre>


# Install

Get the plugin from github:

<pre class="prettyprint lang-sh">
$ git clone git@github.com:pehrs/spring-rest-api-docs-maven-plugin.git
</pre>

Then install into your local maven repository

<pre class="prettyprint lang-sh">
$ mvn install
</pre>

# Usage

Add the following to your pom.xml:

<pre class="prettyprint lang-xml">
  &lt;build>
    &lt;plugins>

	...
	
	&lt;plugin>
	  &lt;groupId>com.pehrs&lt;/groupId>
	  &lt;artifactId>spring-rest-api-docs-maven-plugin&lt;/artifactId>
	  &lt;version>1.0.0&lt;/version>
	  &lt;configuration>
	    &lt;targetDir>docs/rest-api&lt;/targetDir>
	    &lt;pkgRoot>my.package.root&lt;/pkgRoot>
	    &lt;typeSamples>
	      &lt;param>java.net.URI="http://sample/url""&lt;/param>
	      &lt;param>java.lang.Boolean=false&lt;/param>
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
    <td>typeSamples</td>
    <td>- JSON Samples to use for data types</td>
  </tr>
</table>


View a <a href="docs/sample.html">Sample Result</a>.

<script src="http://google-code-prettify.googlecode.com/svn/trunk/src/prettify.js"></script>

<script type="text/javascript">
  
  (function() {
    prettyPrint();
  })();
  
</script>

