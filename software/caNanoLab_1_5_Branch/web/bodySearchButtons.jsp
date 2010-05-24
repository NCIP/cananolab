<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table width="100%" class="invisibleTable">
	<tr>
		<td align="center">
			<span class="formMessage"> <em>Searching without any
					parameters would return all ${dataType}s.</em> </span>
		</td>
	</tr>
	<tr>
		<td align="right">
			<input type="reset" value="Reset"
				onclick="${resetOnclick}" />
			&nbsp;
			<c:choose>
				<c:when test="${!empty searchOnclick }">
					<input type="button" value="Search" onclick="${searchOnclick}">
				</c:when>
				<c:otherwise>
					<html:submit value="Search" />
					<input type="hidden" name="dispatch" value="${hiddenDispatch}">
					<input type="hidden" name="page" value="${hiddenPage}">
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>