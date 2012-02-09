<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table cellspacing="0" cellpadding="0" border="0" width="100%">
	<tr>
		<th class="subMenuPrimaryTitle" scope="col">QUICK
			LINKS</th>
	</tr>
	<jsp:include page="/html/cananoBaseSidemenu.html" />
	<tr>
		<td class="subMenuFill">&nbsp;</td>
	</tr>
	<c:if test="${showVisitorCount}">
		<tr>
			<td class="subMenuSecondaryTitleFill" height="20">Visitor Count</td>
		</tr>
		<tr>
			<td class="subMenuSecondaryTitleFill" height="20"><c:choose>
					<c:when test="${fn:length(countString) > 1}">
						<%--Do NOT reformat code style below, otherwise it will introduce gaps between counter images--%>
						<c:forEach var="index" begin="0"
							end="${fn:length(countString) - 1}">
							<c:set var="counterImg"
								value="${fn:substring(countString, index, index + 1)}" />
							<img alt="Visitor Counter"
								src="images/visitorCounter${counterImg}.png" />
						</c:forEach>
					</c:when>
					<c:otherwise>
						<img alt="Visitor Counter"
							src="images/visitorCounter${countString}.png" />
					</c:otherwise>
				</c:choose></td>
		</tr>
		<tr>
			<td class="subMenuCommentText" height="30"><em>Since
					${counterStartDate}</em></td>
		</tr>
	</c:if>
	<c:if test="${!empty user}">
		<tr>
			<td class="subMenuCommentText" height="20">Logged in as <i>${user.loginName}</i>
				<c:if test="${!empty user.groupNames}">
					<br>Associated Groups:<br />
					<c:forEach var="group" items="${user.groupNames}">
						<span class="indented1"><i>${group}</i> </span>
						<br />
					</c:forEach>
				</c:if></td>
		</tr>
	</c:if>
</table>
