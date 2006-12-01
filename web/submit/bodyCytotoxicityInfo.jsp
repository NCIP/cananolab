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
		<td class="label">
			<c:choose>
				<c:when test="${canUserUpdateParticle eq 'true'}">
					<html:select property="achar.cellLine">
						<option value=""></option>
						<html:options name="allCellLines" />
						<option value="Other">
							Other
						</option>
					</html:select>
				</c:when>
				<c:otherwise>
					${achar.cellLine}&nbsp;
				</c:otherwise>
			</c:choose>
		</td>
		<td class="label" colspan="2">
			<strong>Other Cell Line</strong>&nbsp;<html:text property="achar.otherCellLine" />
		</td>
	</tr>
</table>
<br>


