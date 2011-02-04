<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 <title><bean:message key="global.title"/></title>
<script language="JavaScript">
<!--
<logic:present name="nextPathParam">
var sURL = "<html:rewrite action="/displayImage" name="nextPathParam"/>&slideshow=on";
</logic:present>

function start()
{
    // the timeout value should be the same as in the "refresh" meta-tag.
    setTimeout( "refresh()", 4*1000 );
}

function stop()
{
    // reload the same image without slideshow parameter.
    sURL = "<html:rewrite action="/displayImage" paramId="path" paramName="image" paramProperty="path"/>";
    refresh();
}

function refresh()
{
    //  This version of the refresh function will cause a new
    //  entry in the visitor's history.  It is provided for
    //  those browsers that only support JavaScript 1.0.
    //
    window.location.href = sURL;
}
//-->
</script>

<script language="JavaScript1.1">
<!--
function refresh()
{
    //  This version does NOT cause an entry in the browser's
    //  page view history.  Most browsers will always retrieve
    //  the document from the web-server whether it is already
    //  in the browsers page-cache or not.
    //  
    window.location.replace( sURL );
}
//-->
</script>
 <style>
 <!--
  body,td,div,.p,a{font-family:arial,sans-serif}
 //-->
 </style>  
</head>
<logic:equal parameter="slideshow" value="on">
  <logic:present name="nextPathParam">
    <body onload="start()">
  </logic:present>
  <logic:notPresent name="nextPathParam">
    <body>
  </logic:notPresent>
</logic:equal>
<logic:notEqual parameter="slideshow" value="on">
<body>
</logic:notEqual>
