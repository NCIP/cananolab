<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="4">
				<div align="justify">
					Buckyball Information
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Structure </strong>
			</td>
			<td class="rightLabel" colspan="3">
				<html:text property="buckyball.structure" />
			</td>			
		</tr>
	</tbody>
</table>
