<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<table width="100%" border=0 cellpadding=0 cellspacing=0>
  <tr>
    <td bgcolor=#224499><img width=1 height=1 alt=""></td>
  </tr>
</table>

<table width="100%" border=0 cellpadding=0 cellspacing=0 bgcolor=#bbcced>
  <tr>
    <td bgcolor=#bbcced nowrap>
      <font size=+1>&nbsp;<b><bean:message key="global.title"/></b></font> <font size=-1>version <bean:message key="global.version"/></font>&nbsp;
    </td>
    <td bgcolor=#bbcced align=right nowrap>
    <logic:present name="images" scope="request">
      <font size=-1>Images:&nbsp;<b><bean:write name="imgCount" scope="request"/></b>&nbsp;&nbsp;Directories:&nbsp;<b><bean:write name="dirCount" scope="request"/></b></font>&nbsp;
    </logic:present>
    </td>
  </tr>
</table>
