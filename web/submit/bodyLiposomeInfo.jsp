<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="4">
				<div align="justify">
					Liposome Information
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Is Polymerized </strong>
			</td>
			<td class="label">
				<html:select property="liposome.polymerized">
				<option value="yes">Yes</option>
				<option value="no">No</option>
				</html:select>
			</td>
			<td class="label">
				<strong>Number of Components</strong>
			</td>
			<td class="rightLabel">
				<html:text property="liposome.numberOfComponents" />
			</td>
		</tr>
	</tbody>
</table>

