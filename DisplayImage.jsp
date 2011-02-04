<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert definition="default" flush="true">
  <tiles:put name="header" value="/DisplayImageHeader.jsp"/>
  <tiles:put name="body" value="/DisplayImageBody.jsp"/>
</tiles:insert>