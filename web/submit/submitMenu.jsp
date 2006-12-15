<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:choose>
	<c:when test="${!empty param.submitType}">
		<c:set var="submitType" value="${param.submitType}" scope="session" />
	</c:when>
	<c:otherwise>
		<c:set var="submitType" value="${sessionScope.submitType}" />
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${!empty param.particleName}">
		<c:set var="particleName" value="${param.particleName}" scope="session" />
	</c:when>
</c:choose>
<c:choose>
	<c:when test="${!empty param.particleType}">
		<c:set var="particleType" value="${param.particleType}" scope="session" />
	</c:when>
</c:choose>
<logic:present name="submitType">
	<logic:equal name="submitType" value="physical">
		<bean:define id="actions" name="physicalActions" type="java.util.List" />
	</logic:equal>
	<logic:equal name="submitType" value="tox">
		<bean:define id="actions" name="toxActions" type="java.util.List" />
	</logic:equal>
	<logic:equal name="submitType" value="cytoTox">
		<bean:define id="actions" name="cytoToxActions" type="java.util.List" />
	</logic:equal>
	<logic:equal name="submitType" value="bloodContactTox">
		<bean:define id="actions" name="bloodContactActions" type="java.util.List" />
	</logic:equal>
	<logic:equal name="submitType" value="immuneCellFuncTox">
		<bean:define id="actions" name="immuneCellFuncActions" type="java.util.List" />
	</logic:equal>
	<logic:equal name="submitType" value="metabolic">
		<bean:define id="actions" name="metabolicActions" type="java.util.List" />
	</logic:equal>
	<logic:present name="actions">
		<c:choose>
			<c:when test="${canUserUpdateParticle eq 'true'}">

				<table border="0" cellspacing="0" cellpadding="0">
					<tr>
						<logic:iterate name="actions" id="item" type="org.apache.struts.tiles.beans.MenuItem">
							<td class="secondMenuItem" height="20">
								<c:url var="link" value="${item.link}">
									<c:param name="particleName" value="${particleName}" />
									<c:param name="particleType" value="${particleType}" />
									<c:param name="submitType" value="${submitType}" />
								</c:url>
								<a href="${link}" class="secondMenuLink"><bean:write name="item" property="value" /></a>
							</td>
							<td>
								<img height="16" alt="" src="images/mainMenuSeparator.gif" width="1">
							</td>
						</logic:iterate>
					</tr>
				</table>
			</c:when>
		</c:choose>
	</logic:present>
</logic:present>

