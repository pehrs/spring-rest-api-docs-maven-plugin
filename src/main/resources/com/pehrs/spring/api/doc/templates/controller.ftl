<!HTML>
<html>
  <head>
    <title>${pom_group_id}.${pom_artifact_id} ${pom_version} API: ${controller.getPkgName()}.${controller.getName()}</title>

    <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,900,200,300,600,700,200italic,300italic,400italic,600italic,700italic,900italic' rel='stylesheet' type='text/css'/>

    <link href="http://google-code-prettify.googlecode.com/svn/trunk/src/prettify.css" type="text/css" rel="stylesheet" />
    <link href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.1.0/css/bootstrap-combined.min.css" type="text/css" rel="stylesheet" />
    <style>
      body {
      font-family: 'Source Sans Pro', sans-serif;
      font-size: 18px;
      }
      table.json {
	  width: 1024px;
	  
	      -moz-border-radius: 15px;
	  border-radius: 15px;
	  
	  background: #eef;
	  
	  margin-bottom: 15px;
      }

table.json pre {
    background: #fff;
}

table.json td.method {
    background: #44a;
    color: #fff;
	-moz-border-radius-topleft: 10px;
    border-top-left-radius: 10px;
    padding: 10px;
}
table.json td.url {
    width: 100%;
    background: #ddf;
    color: #000;
	-moz-border-radius-topright: 10px;
    border-top-right-radius: 10px;
    padding-left: 10px;
    padding-top: 10px;
    padding-bottom: 10px;
}

table.param {
    border: 2px solid #EFEFEF;
    
	-moz-border-radius: 15px;
    border-radius: 15px;
    margin-bottom: 15px;
}

      table.json th {
      border-bottom: 2px solid #000;
      padding-right: 40px;
      }
      table.json td {
      }

      .content table {
      width: 100%;
      }
      .service-btn {
      margin-right: 1em;
      margin-left: 1em;
      }
    </style>

  </head>
  <body>

  <div class="container">
  
    <div class="row">

      <div class="span12">

    <h1>${controller.getPkgName()}.${controller.getName()}</h1>

    <p>${controller.getComment()}</p>

    <p>${controller.getUrlRoot()}</p>

    <#foreach method in controller.getMethodIterator()>

      <table class="json">
	<tr class="head" id="api_${method.getRequestMappingMethod()}_${method.getName()}">
	  <td class="method">${method.getRequestMappingMethod()}</td>
	  <td class="url">${controller.getUrlRoot()}${method.getRequestMappingUrl()} <a class="service-btn btn btn-small pull-right" href="#"><i class="icon-plus"></i></a>
	  </td>
	</tr>

	<tr>
	  <td colspan="2">
	    <div class="content">
	      <table>
		<tr>
		  <td >
		    <p>${method.getCommentText()}</p>
		  </td>
		</tr>
		<#if method.hasPathVariables()>
		  <tr>
		    <td >
		      <h2>Path Variables</h2>
		    </td>
		  </tr>
		  <tr>
		    <td >
		      <table class="param">
			<tr>
			  <th>Type</th>
			  <th>Name</th>
			  <#foreach param in method.getPathVariableIterator()>
			    <tr>
			      <td>${param.getTypeName()}</td>
			      <td>${param.getPathVariableValue()}</td>
			    </tr>
			  </#foreach>
		      </table>
		    </td>
		  </tr>
		</#if>
		
		<#if method.hasRequestParameters()>
		  <tr>
		    <td >
		      <h2>Request Parameters</h2>
		    </td>
		  </tr>
		  <tr>
		    <td >
		      <table class="param">
			<tr>
			  <th>Required</th>
			  <th>Type</th>
			  <th>Name</th>
			  <th>Default value</th>
			  <#foreach param in method.getRequestParameterIterator()>
			    <tr>
			      <td>
				<#if param.isRequestParamRequired()>
				  <b>REQUIRED</b>
				</#if>
			      </td>
			      <td>${param.getTypeName()}</td>
			      <td>${param.getRequestParamValue()}</td>
			      <td>${param.getRequestParamDefaultValue()}</td>
			    </tr>
			  </#foreach>
		      </table>
		    </td>
		  </tr>
		</#if>
		
		<#if method.hasRequestBodyParameter()>
		  <tr>
		    <td >
		      <h2>Request Body Sample</h2>
		    </td>
		  </tr>
		  <tr>
		    <td >
		      <pre class="prettyprint lang-js">
			${method.getRequestJSONSample()}
		      </pre>
		    </td>
		  </tr>
		</#if>


		<tr>
		  <td >
		    <h2>Response Body Sample</h2>
		  </td>
		</tr>
		<tr>
		  <td >
		    <pre class="prettyprint lang-js">
		      ${method.getResponseJSONSample()}
		    </pre>
		  </td>
		</tr>
	      </table>
	    </div>
	  </td>
	</tr>
      </table>

    </#foreach>

    </div>
    </div>
  </div>



    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.23/jquery-ui.min.js"></script>
    <script src="http://google-code-prettify.googlecode.com/svn/trunk/src/prettify.js"></script>
    <script src="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.1.0/js/bootstrap.min.js"></script>

    <script type="text/javascript">

      (function() {

      prettyPrint();

	  $(".content").hide();

      $(".service-btn").click(function() {
         /* $(this).find("td").css("background", "red"); */
      
          var content = $(this).parent().parent().siblings("tr").find(".content");
	  if(content.is(":visible")) {
              content.hide(400);
              var el = $(this).find(".icon-minus");
	      el.removeClass("icon-minus");
	      el.addClass("icon-plus");
	  } else {
              content.show(400);
              var el = $(this).find(".icon-plus");
	      el.removeClass("icon-plus");
	      el.addClass("icon-minus");
	  }
      });

      })();

    </script>

  <body>
</html>    
