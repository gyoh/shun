<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<table width="100%" border=0 cellpadding=5 cellspacing=0>
  <tr>
    <!-- display directory navigation links. -->
    <td>
      <font size=-1>
        <logic:present parameter="path">
          <html:link action="/displayThumbnails">Top</html:link>
        </logic:present>
        <logic:notPresent parameter="path">
          Top
        </logic:notPresent>
        <logic:iterate id="pathEntry" name="files" property="breadcrumb">
          &nbsp;&gt;&nbsp;
          <logic:notEqual name="pathEntry" property="key" value="<%=request.getParameter("path")%>">
            <html:link action="/displayThumbnails" paramId="path" paramName="pathEntry" paramProperty="key"><bean:write name="pathEntry" property="value"/></html:link>
          </logic:notEqual>
          <logic:equal name="pathEntry" property="key" value="<%=request.getParameter("path")%>">
            <bean:write name="pathEntry" property="value"/>
          </logic:equal>
        </logic:iterate>
      </font>
    </td>

    <!-- display sort or rotate buttons. -->
    <td align=right>
      <font size=-1>
        <logic:present name="sortByTypeParam">
        Sort by&nbsp;
        <logic:equal name="sort" value="type">
          Type
        </logic:equal>
        <logic:notEqual name="sort" value="type">
          <html:link action="/displayThumbnails" name="sortByTypeParam">Type</html:link>
        </logic:notEqual>
        &nbsp;|&nbsp;
        <logic:equal name="sort" value="name">
          Name
        </logic:equal>
        <logic:notEqual name="sort" value="name">
          <html:link action="/displayThumbnails" name="sortByNameParam">Name</html:link>
        </logic:notEqual>
        &nbsp;|&nbsp;
        <logic:equal name="sort" value="date">
          Date
        </logic:equal>
        <logic:notEqual name="sort" value="date">
          <html:link action="/displayThumbnails" name="sortByDateParam">Date</html:link>
        </logic:notEqual>
        </logic:present>
        <logic:present name="rotateCCWParam">
        Rotate 90 degrees&nbsp;
        <logic:equal parameter="degrees" value="270">
          CCW
        </logic:equal>
        <logic:notEqual parameter="degrees" value="270">
          <html:link action="/displayImage" name="rotateCCWParam">CCW</html:link>
        </logic:notEqual>
        &nbsp;|&nbsp;
        <logic:equal parameter="degrees" value="90">
          CW
        </logic:equal>
        <logic:notEqual parameter="degrees" value="90">
          <html:link action="/displayImage" name="rotateCWParam">CW</html:link>
        </logic:notEqual>
        </logic:present>        
      </font>
    </td>
  </tr>
</table>
