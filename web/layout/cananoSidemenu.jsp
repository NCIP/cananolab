<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<table height="100%" width="150" cellspacing="0" cellpadding="0"
	summary="" border="0">
	<tbody>
		<tr>
			<td>
				<table height="100%" width="150" cellspacing="0" cellpadding="0"
					summary="" border="0">
					<tr>
						<td class="subMenuPrimaryTitle" height="22">
							QUICK LINKS
							<!-- anchor to skip sub menu -->
							<a href="#content"><img height="1" alt="Skip Menu"
									src="images/shim.gif" width="1" border="0"> </a>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<jsp:include page="/html/cananoBaseSidemenu.html" />
			</td>
		</tr>
		<tr>
			<td class="subMenuFill" height="5">
				&nbsp;
			</td>
		</tr>
		<c:if test="${'true' eq param.showVisitorCount}">
			<tr>
				<td class="subMenuSecondaryTitleFill" height="20">
					Visitor Count
				</td>
			</tr>
			<tr>
				<td class="subMenuSecondaryTitleFill" height="20">
					<%--Do NOT reformat code style below, otherwise it will introduce gaps between counter images--%>
					<c:forEach var="index" begin="0" end="${fn:length(countString) - 1}"><c:set var="counterImg" value="${fn:substring(countString, index, index + 1)}"/><img alt="Visitor Counter" src="images/visitorCounter${counterImg}.png"/></c:forEach>
				</td>
			</tr>
			<tr>
				<td class="subMenuCommentText" height="20">
					<em>since ${counterStartDate}</em>
				</td>
			</tr>
		</c:if>
		<tr>
			<td class="subMenuFill" height="100%">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="subMenuFooter" height="22">
				&nbsp;
			</td>
		</tr>
	</tbody>
</table>