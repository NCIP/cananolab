<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<br>
<br>
<c:choose>
	<c:when test="${!empty param.menuType}">
		<c:set var="menuType" value="${param.menuType}" />
	</c:when>
	<c:otherwise>
		<c:set var="menuType" value="${sessionScope.menuType}" />
	</c:otherwise>
</c:choose>
<c:set var="helpFlag" value="${param.helpFlag}" />
<%-- for debug use
<c:out value="${pageContext.request.method}" /><br>
<c:forEach var="paramItem" items="${paramValues}" varStatus="ind">
	<c:choose>
		<c:when test="${ind.count>1 || paramItem.key ne 'forwardPage'}">
			<c:out value="${paramItem.key}" />:
									<c:out value="${paramItem.value[0]}" />
			<br>
		</c:when>
	</c:choose>
</c:forEach>
<br>
--%>
<logic:present name="menuType">
	<logic:equal name="menuType" value="in">
		<bean:define id="actions" name="inActions" type="java.util.List" />
		<c:set var="help_topic" scope="session" value="in_folder"/>
	</logic:equal>
	<logic:equal name="menuType" value="out">
		<bean:define id="actions" name="outActions" type="java.util.List" />
		<c:set var="help_topic" scope="session" value="out_folder"/>
	</logic:equal>
	<logic:equal name="menuType" value="run">
		<bean:define id="actions" name="runActions" type="java.util.List" />
		<c:set var="help_topic" scope="session" value="upload_files"/>
	</logic:equal>
	<logic:present name="actions">
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<logic:iterate name="actions" id="item" type="org.apache.struts.tiles.beans.MenuItem">
					<td class="secondMenuItem" height="20">
						<a href="${item.link}" class="secondMenuLink"><bean:write name="item" property="value" /></a>
					</td>
					<td>
						<img height="16" alt="" src="images/mainMenuSeparator.gif" width="1">
					</td>
				</logic:iterate>
			</tr>
		</table><br>
		<logic:present name="helpFlag">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td align="right">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=${help_topic}')" class="helpText">Help</a>&nbsp;
			</td>
			</tr>
		</table>
		</logic:present>
	</logic:present>
</logic:present>

