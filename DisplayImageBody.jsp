<%@ page import="org.hamamoto.album.vo.Files" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<!-- display previous & next image links. -->
<table align=center border=0 cellpadding=5 cellspacing=0>
  <tr>
    <td align=left valign=middle>
      <logic:present name="previousPathParam">
        <font size=-1>
        &lt;&nbsp;<html:link action="/displayImage" name="previousPathParam">Previous&nbsp;Image</html:link>
        </font>
      </logic:present>
      <logic:notPresent name="previousPathParam">
        <font size=-1 color="gray">&lt;&nbsp;Previous&nbsp;Image</font>
      </logic:notPresent>
    </td>
    <td align=center valign=middle>
      <logic:equal parameter="slideshow" value="on">
        <logic:present name="nextPathParam">
          <font size=-1>
          <html:link href="javascript:stop()">Stop&nbsp;Slideshow</html:link>
          </font>
        </logic:present>
        <logic:notPresent name="nextPathParam">
          <font size=-1 color="gray">
          Stop&nbsp;Slideshow
          </font>
        </logic:notPresent>
      </logic:equal>
      <logic:notEqual parameter="slideshow" value="on">
        <logic:present name="nextPathParam">      
          <font size=-1>
          <html:link href="javascript:start()">Start&nbsp;Slideshow</html:link>
          </font>
        </logic:present>
        <logic:notPresent name="nextPathParam">
          <font size=-1 color="gray">
          Start&nbsp;Slideshow
          </font>
        </logic:notPresent>
      </logic:notEqual>
    </td>
    <td align=right valign=middle>
      <logic:present name="nextPathParam">
        <font size=-1>
        <html:link action="/displayImage" name="nextPathParam">Next&nbsp;Image</html:link>&nbsp;&gt;
        </font>
      </logic:present>
      <logic:notPresent name="nextPathParam">
        <font size=-1 color="gray">Next&nbsp;Image&nbsp;&gt;</font>
      </logic:notPresent>
    </td>
  </tr>
</table>

<!-- display image. -->
<table align=center border=0 cellpadding=5 cellspacing=0>
  <tr>
    <td align=center valign=middle>
      <script langauge="javascript">
        size = Math.min(document.body.clientWidth, document.body.clientHeight) * 0.9;
        scaledImage = '<img src="<html:rewrite action="/transformImage" name="transformParam"/>&size=' + size + '"/>';
        document.write(scaledImage);
      </script>
      &nbsp;<br>
      <b><bean:write name="image" property="name"/></b><br>
      <font size=-1><bean:write name="image" property="width"/>&nbsp;x&nbsp;
      <bean:write name="image" property="height"/>&nbsp;pixels&nbsp;-
      <bean:write name="image" property="size"/>k<br>
      <font color=#008000>
        <logic:present name="image" property="dateTimeOriginal">
          <bean:write name="image" property="dateTimeOriginal" format="yyyy/MM/dd HH:mm:ss"/>
        </logic:present>
        <logic:notPresent name="image" property="dateTimeOriginal">
          <bean:write name="image" property="lastModified" format="yyyy/MM/dd HH:mm:ss"/>
        </logic:notPresent>
      </font></font>
    </td>
  </tr>
</table>
