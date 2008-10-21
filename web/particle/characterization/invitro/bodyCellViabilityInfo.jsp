<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
	<tr class="topBorder">
		<td class="formTitle" colspan="4">
			<div align="justify">
				Detail Information
			</div>
		</td>
	</tr>
	<tr>
		<td class="leftLabel">
			<strong>Cell Line</strong>
		</td>
		<c:choose>
			<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
				<td class="label">
					<html:select property="achar.cellViability.cellLine" styleId="cellLine"
											onchange="javascript:callPrompt('Cell Line', 'cellLine');">
						<option value="" />
						<html:options name="cellLines" />
						<option value="other">[Other]</option>
					</html:select>
				</td>
			</c:when>
			<c:otherwise>
				<td class="label">
					${characterizationForm.map.achar.cellViability.cellLine}&nbsp;
				</td>
			</c:otherwise>
		</c:choose>
		<td class="rightLabel" colspan="2">&nbsp;</td>
	</tr>
</table>
<br>


