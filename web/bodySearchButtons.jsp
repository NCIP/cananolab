<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0" summary="">
	<tr>
		<td align="center">
			<span class="formMessage"> <em>Searching without any
					parameters would return all ${dataType}s.</em> </span>
		</td>
	</tr>
	<tr>
		<td>
			<input type="reset" value="Reset"
				onclick="javascript: location.href = '${resetLink}';" />
			&nbsp;&nbsp;
			<html:submit value="Search" />
			<input type="hidden" name="dispatch" value="${hiddenDispatch}">
			<input type="hidden" name="page" value="${hiddenPage}">
		</td>
	</tr>
</table>