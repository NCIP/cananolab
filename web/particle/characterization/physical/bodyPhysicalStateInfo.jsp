<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="6">
				<div align="justify">
					Physical State Property
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Type </strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
						<html:select property="achar.physicalState.type"
							styleId="physicalStateType"
							onchange="javascript:callPrompt('Type', 'physicalStateType');">
							<option name=""></option>
							<html:options name="physicalStateTypes" />
							<option value="other">
								[Other]
							</option>
						</html:select>
					</c:when>
					<c:otherwise>
						${characterizationForm.map.physicalState.type}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</tbody>
</table>
<br>
