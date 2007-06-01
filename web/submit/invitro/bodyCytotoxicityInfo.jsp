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
			<c:when test="${canUserSubmit eq 'true'}">
				<td class="label">
					<html:select property="cytotoxicity.cellLine" onchange="javascript:updateOtherField(this.form,'cytotoxicity.cellLine','cytotoxicity.otherCellLine')">
						<option value=""></option>
						<html:options name="allCellLines" />
					</html:select>
				</td>
				<td class="rightLabel" colspan="2">
					<strong>Other Cell Line</strong>&nbsp;
					<c:choose>
						<c:when test="${nanoparticleCharacterizationForm.map.cytotoxicity.cellLine eq 'Other'}">
							<html:text property="cytotoxicity.otherCellLine"  disabled="false" />
						</c:when>
						<c:otherwise>
							<html:text property="cytotoxicity.otherCellLine"  disabled="true" />
						</c:otherwise>
					</c:choose>
				</td>
			</c:when>
			<c:otherwise>
				<td class="rightLabel" colspan="3">
					${nanoparticleCharacterizationForm.map.cytotoxicity.cellLine}&nbsp;
				</td>
			</c:otherwise>
		</c:choose>
	</tr>
</table>
<br>


