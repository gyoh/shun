<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<html:html>

<!-- Header information -->
<tiles:insert attribute="header"/>

<!-- Title information -->
<tiles:insert attribute="title"/>

<!-- Menu bar -->
<tiles:insert attribute="menu"/>

<!-- Main body information -->
<tiles:insert attribute="body"/>

<!-- Copyright information -->
<tiles:insert attribute="footer"/>

</html:html>
