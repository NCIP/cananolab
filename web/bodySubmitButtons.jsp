<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td width="600">
			<input type="reset" value="Reset"
				onclick="${resetLink}" />
			&nbsp;&nbsp;
			<c:set var="submitButtonName" value="Submit" />
			<c:if test="${!empty updateId}">
				<c:set var="submitButtonName" value="Update" />
			</c:if>
			<c:choose>
				<c:when test="${!empty submitOnclick }">
					<input type="button" value="${submitButtonName}" onclick="${submitOnclick}">
				</c:when>
				<c:otherwise>
					<html:submit value="${submitButtonName}" />
					<input type="hidden" name="dispatch" value="${hiddenDispatch}">
					<input type="hidden" name="page" value="${hiddenPage}">
				</c:otherwise>
			</c:choose>

		</td>
		<td align="right" width="200">
			<c:if test="${!empty user && !empty updateId && user.admin}">
				<input type="button" value="${deleteButtonName}"
					onclick="${deleteOnclick}">
			</c:if>
			<c:if test="${!empty updateId && !empty cloneOnclick }">
				<input type="button" value="Clone" onclick="${cloneOnclick}">
			</c:if>
		</td>
	</tr>
</table>

