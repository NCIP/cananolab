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
			<jsp:include page="/html/cananoBaseSidemenu.html"/>
			</td>
		</tr>
		<tr>
			<td class="subMenuFill" height="5">
				&nbsp;
			</td>
		</tr>
		<c:if test="${'true' eq param.showVisitorCount}">
			<tr>
				<td class="subMenuPrimarySubTitle" height="20">
					VISITOR COUNT
				</td>
			</tr>
			<tr>
				<td class="subMenuSecondaryTitleFill" height="20">
					${visitorCount}
				</td>
			</tr>
			<tr>
				<td class="subMenuSecondaryTitleFill" height="20">
					since ${counterStartDate}
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