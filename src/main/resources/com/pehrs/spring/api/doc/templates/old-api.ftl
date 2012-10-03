<!HTML>
<html>
  <head>
    <title>${pom_group_id}.${pom_artifact_id} ${pom_version}</title>

    <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,900,200,300,600,700,200italic,300italic,400italic,600italic,700italic,900italic' rel='stylesheet' type='text/css'/>

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

      background: #eef;

      margin-bottom: 15px;
      }

      table.json pre {
      background: #fff;
      }


      table.json td.method {
      border-bottom: 2px solid #eee;
      background: #44a;
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
          <a class="brand" href="#">${pom_artifact_id} API</a>
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
        </div>
      </div>
    </div>

  <div class="container">
    <div class="row">
      <div class="span12">
	<h1>${pom_group_id}.${pom_artifact_id} ${pom_version} JSON REST API</h1>
      </div>
    </div>
    
    <div class="row">
      <!--
      <div class="span3 bs-docs-sidebar">
	<ul class="nav nav-list bs-docs-sidenav">
	  <li><a href="#byController"><i class="icon-chevron-right"></i> By Controller Class</a></li>
	  <li><a href="#byPath"><i class="icon-chevron-right"></i> By Path</a></li>
	</ul>
      </div>
      <div class="span9">
      -->
      <div class="span12">
	<h2 id="byController">By Controller Class</h2>
	<ul class="nav nav-tabs nav-stacked">
	  <#foreach controller in controllers>
	    <li class="dropdown">
		<a class="dropdown-toggle"
		   data-toggle="dropdown"
		   href="#">
		  ${controller.getPkgName()}.${controller.getName()}
		  <b class="caret"></b>
		</a>
		<ul class="dropdown-menu" role="menu">
		  <!-- links -->
		  <#foreach path in controller.getPathInfoIterator()>
		    <li>
		      <span class="method">${path.getRequestMethod()}</span><span class="url"><a href="${path.getHref()}">${path.getRequestPath()}</a></span>
		    </li>
		  </#foreach>	    
		</ul>
	    </li>	    
	  </#foreach>
	</ul>
	<h2 id="byPath">By Path</h2>
	<table class="json">				   
	  <#foreach path in paths>
	    <tr class="head">
	      <td class="method">${path.getRequestMethod()}</td>
	      <td class="url"><a href="${path.getHref()}">${path.getRequestPath()}</a></td>
	    </tr>
	  </#foreach>
	</table>
      </div>
    </div>

  </div> <!-- end of container -->
      
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.23/jquery-ui.min.js"></script>
    <script src="http://google-code-prettify.googlecode.com/svn/trunk/src/prettify.js"></script>
    <script src="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.1.0/js/bootstrap.min.js"></script>

    <script type="text/javascript">

      (function() {

      prettyPrint();

	  $(".content").hide();

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
