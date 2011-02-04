<%@ page import="java.io.*, java.net.*, org.hamamoto.album.vo.File, org.hamamoto.album.vo.Files" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<table align=center border=0 cellpadding=5 cellspacing=0 width="100%">
  <!-- display images and directories. -->
  <% int i = 0; %>
  <logic:iterate id="file" name="files" property="list">
    <%
    if (i % 4 == 0) {
      if (i / 4 % 2 == 0) {
    %>
    <tr bgcolor=#e7eefc>
    <%
    } else {
    %>
    <tr>
    <%
    }
  }
  %>
  <td align=center valign=middle width="23%"><br>
  <logic:present name="file" property="size">
    <html:link action="/displayImage" paramId="path" paramName="file" paramProperty="path">
      <logic:equal name="file" property="thumbnailExists" value="true">
        <img src="/~shun/thumbnails/<bean:write name="file" property="path"/>"/>
      </logic:equal>
      <logic:equal name="file" property="thumbnailExists" value="false">
        <html:img action="/transformImage" name="transformParam" paramId="path" paramName="file" paramProperty="path"/>
      </logic:equal>
    </html:link>
    &nbsp;<br>
    <b><bean:write name="file" property="name"/></b><br>
    <font size=-1><bean:write name="file" property="width"/>&nbsp;x&nbsp;
    <bean:write name="file" property="height"/>&nbsp;pixels&nbsp;-
    <bean:write name="file" property="size"/>k<br>
    <font color=#008000>
      <logic:present name="file" property="dateTimeOriginal">
        <bean:write name="file" property="dateTimeOriginal" format="yyyy/MM/dd HH:mm:ss"/>
      </logic:present>
      <logic:notPresent name="file" property="dateTimeOriginal">
        <bean:write name="file" property="lastModified" format="yyyy/MM/dd HH:mm:ss"/>
      </logic:notPresent>
    </font></font><br><br>
  </logic:present>
  <logic:notPresent name="file" property="size">
    <html:link action="/displayThumbnails" paramId="path" paramName="file" paramProperty="path">
      <html:img page="/images/dir.png" border="0"/>
    </html:link>
    &nbsp;<br>
    <b><bean:write name="file" property="name"/></b></font><br><br>
  </logic:notPresent>
  </td>
  <%
  if (i % 4 == 3 || i == ((Files)request.getAttribute("files")).getList().size() - 1) {
  %>
  </tr>
  <%
  }
  i++;
  %>
  </logic:iterate>
</table>
