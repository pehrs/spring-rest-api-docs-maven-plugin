<!HTML>
<html>
  <head>
    <title>${pom_group_id}.${pom_artifact_id} ${pom_version}</title>

    <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,900,200,300,600,700,200italic,300italic,400italic,600italic,700italic,900italic' rel='stylesheet' type='text/css'/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="http://google-code-prettify.googlecode.com/svn/trunk/src/prettify.css" type="text/css" rel="stylesheet" />
    <link href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.1.0/css/bootstrap-combined.min.css" type="text/css" rel="stylesheet" />
    <style>
      body {
      font-family: 'Source Sans Pro', sans-serif;
      font-size: 18px;
      position: relative;
      padding-top: 40px;
      }

      table.json {
      width: 1024px;

      -moz-border-radius: 15px;
      border-radius: 15px;

      /* background: #eef;*/

      margin-bottom: 15px;
      }

      table.json pre {
      background: #fff;
      }


      table.json td.method {
      border-bottom: 2px solid #eee;
      /* background: #44a;*/
      color: #fff;
      padding: 10px;
      }
      table.json td.url {
      width: 100%;
      border-bottom: 2px dashed #fff;
      color: #000;
      -moz-border-radius-topright: 10px;
      border-top-right-radius: 10px;
      padding-left: 10px;
      padding-top: 10px;
      padding-bottom: 10px;
      }

      table.param {
      width: 100%;
      border: 2px solid #EFEFEF;

      -moz-border-radius: 15px;
      border-radius: 15px;
      margin-bottom: 15px;
      }

      table.json th {
      text-align: left;
      color: #777;
      border-bottom: 2px solid #000;
      padding-right: 40px;
      font-weight: normal;
      }
      table.json td {
      color: #000;
      font-weight: bold;
      }

      .content table {
      width: 100%;
      }

      h2 {
      font-size: 18px;
      }


/* Sidenav for Docs
-------------------------------------------------- */

/* prevent column from collapsing when affixing */
.span3 {
  min-height: 1px;
}

.bs-docs-sidenav {
  width: 228px;
  margin: 30px 0 0;
  padding: 0;
  background-color: #fff;
  -webkit-border-radius: 6px;
     -moz-border-radius: 6px;
          border-radius: 6px;
  -webkit-box-shadow: 0 1px 4px rgba(0,0,0,.065);
     -moz-box-shadow: 0 1px 4px rgba(0,0,0,.065);
          box-shadow: 0 1px 4px rgba(0,0,0,.065);
}
.bs-docs-sidenav > li > a {
  display: block;
  *width: 190px;
  margin: 0 0 -1px;
  padding: 8px 14px;
  border: 1px solid #e5e5e5;
}
.bs-docs-sidenav > li:first-child > a {
  -webkit-border-radius: 6px 6px 0 0;
     -moz-border-radius: 6px 6px 0 0;
          border-radius: 6px 6px 0 0;
}
.bs-docs-sidenav > li:last-child > a {
  -webkit-border-radius: 0 0 6px 6px;
     -moz-border-radius: 0 0 6px 6px;
          border-radius: 0 0 6px 6px;
}
.bs-docs-sidenav > .active > a {
  position: relative;
  z-index: 2;
  padding: 9px 15px;
  border: 0;
  text-shadow: 0 1px 0 rgba(0,0,0,.15);
  -webkit-box-shadow: inset 1px 0 0 rgba(0,0,0,.1), inset -1px 0 0 rgba(0,0,0,.1);
     -moz-box-shadow: inset 1px 0 0 rgba(0,0,0,.1), inset -1px 0 0 rgba(0,0,0,.1);
          box-shadow: inset 1px 0 0 rgba(0,0,0,.1), inset -1px 0 0 rgba(0,0,0,.1);
}
/* Chevrons */
.bs-docs-sidenav .icon-chevron-right {
  float: right;
  margin-top: 2px;
  margin-right: -6px;
  opacity: .25;
}
.bs-docs-sidenav > li > a:hover {
  background-color: #f5f5f5;
}
.bs-docs-sidenav a:hover .icon-chevron-right {
  opacity: .5;
}
.bs-docs-sidenav .active .icon-chevron-right,
.bs-docs-sidenav .active a:hover .icon-chevron-right {
  background-image: url(../img/glyphicons-halflings-white.png);
  opacity: 1;
}
.bs-docs-sidenav.affix {
  top: 40px;
}
.bs-docs-sidenav.affix-bottom {
  position: absolute;
  top: auto;
  bottom: 270px;
}


.service-header {
color: #888;
border-bottom: 2px solid #eee;
font-weight: bold;
}

.service {
margin-top: 4px;
   padding: 4px;
}

.service-title {
      font-weight: bold;
}
      
.service-title-POST {
      color: #393;
}
.service-title-GET {
      color: #339;
}
.service-title-DELETE {
      color: #933;
}

.service-title-PUT {
      color: #993;
}

.service-POST {
   background: #efe;
}

.service-GET {
   background: #eef;
}

.service-DELETE {
   background: #fee;
}

.service-PUT {
   background: #ffe;
}

.service-rest-method {
  margin-right: 24px; 
  width: 50px;
}

.logo {
      color: #000;
      font-size: 36px;
      font-weight: bold;
      /* text-shadow: 1px 1px #888; */
      text-shadow: #fff 0.05em 3px 0.4em;
}

.link {
      cursor: pointer;
}

    </style>

  </head>
  <body  data-spy="scroll" data-target=".bs-docs-sidebar">
    
    <!-- Navbar
    ================================================== -->
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="brand" href="#"><span class="logo">${pom_artifact_id} ${pom_version} JSON REST API</span></a>
	  <!--
          <div class="nav-collapse collapse">
            <ul class="nav">
              <li class="">
                <a href="#byController">By Controller Class</a>
              </li>
              <li class="">
                <a href="#byPath">By Path</a>
              </li>
            </ul>
          </div>
	  -->
        </div>
      </div>
    </div>

    <div class="container">
      <!--
      <div class="row">
	<div class="span12">
	  <h1>${pom_group_id}.${pom_artifact_id} ${pom_version} JSON REST API</h1>
	</div>
      </div>
      -->
      
      
      <#foreach controller in controllers>
	
	<div class="row">
	  <div class="span12">
	    
	    <div class="row">
	      <div class="span12">
		<div class="service-header">
		  <span onClick="$('#${controller.getJSId()}').toggle('slow');" class="link url">${controller.getPathInfoRoot()}</span><span onClick="$('#${controller.getJSId()}').toggle('slow');" class="link pull-right">${controller.getComment()}</span>
		</div>
	      </div>
	    </div>
	    <div id="${controller.getJSId()}" class="services row">
	      <div class="span12">

		<#foreach path in controller.getPathInfoIterator()>
		
		<div class="row">
		  <div class="span12">
		    <div class="service service-${path.getRequestMethod()}"><button onClick="$('#${path.getMethodId()}').toggle('slow');" style="width: 80px;" class="btn ${path.getBootstrapRequestMethodBtnStyle()} service-rest-method">${path.getRequestMethod()}</button><span class="service-url">${path.getRequestPath()}</span><span class="service-title service-title-${path.getRequestMethod()} pull-right">${path.getMethod().getCommentTextFirstSentence()}</span></div>
		  </div>
		</div>
		<div class="row">
		  <div class="span12">
		    <div id="${path.getMethodId()}" class="service-details service-${path.getRequestMethod()}">
		      <p>${path.getMethod().getCommentTextWithNoAnnotations()}</p>
		      
		      <table class="json">

			<#if path.getMethod().hasPathVariables()>
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
				  <#foreach param in path.getMethod().getPathVariableIterator()>
				    <tr>
				      <td>${param.getTypeName()}</td>
				      <td>${param.getPathVariableValue()}</td>
				    </tr>
				  </#foreach>
			      </table>
			    </td>
			  </tr>
			</#if>
			
			<#if path.getMethod().hasRequestParameters()>
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
				  <#foreach param in path.getMethod().getRequestParameterIterator()>
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
			
			<#if path.getMethod().hasRequestBodyParameter()>
			  <tr>
			    <td >
			      <h2>Request Body Sample</h2>
			    </td>
			  </tr>
			  <tr>
			    <td >
			      <pre class="prettyprint lang-js">
				${path.getMethod().getRequestJSONSample()}
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
			      ${path.getMethod().getResponseJSONSample()}
			    </pre>
			  </td>
			</tr>
		      </table>
		      
		    </div>
		    
		  </div>
		</div>


		</#foreach>
		
		
	      </div>
	    </div>
	    
	  </div>
	</div>
	
      </#foreach>
      
    </div> <!-- end of container -->
      
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.23/jquery-ui.min.js"></script>
    <script src="http://google-code-prettify.googlecode.com/svn/trunk/src/prettify.js"></script>
    <script src="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.1.0/js/bootstrap.min.js"></script>

    <script type="text/javascript">

      (function() {

      prettyPrint();

      $(".service-details").hide();
      $(".services").hide();


      $(".head").click(function() {
         /* $(this).find("td").css("background", "red"); */
      
          var content = $(this).siblings("tr").find(".content");
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
