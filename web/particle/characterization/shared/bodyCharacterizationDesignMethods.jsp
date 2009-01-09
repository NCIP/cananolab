<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table border="0" align="center" cellpadding="3" cellspacing="0"
	width="100%" class="topBorderOnly" summary="">
	<tr>
	<tr class="topBorder">
		<td class="formTitle" colspan="4">
			<div align="justify">
				Technique and Instrument Information
			</div>
		</td>
	</tr>
	<tr>
		<td class="leftLabel" valign="top" width="20%">
			<strong>Technique</strong>
		</td>
		<td class="rightLabel" colspan="3">
			<c:choose>
				<c:when
					test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
					<html:select styleId="technique" property="achar.techniqueTypes"
						onchange="javascript:callPrompt('Instrument Type', 'instrumentType');"
						multiple="true" size="5">
						<html:options collection="allTechniques"
							labelProperty="type" property="type" />
						<option value="other">
							[Other]
						</option>
					</html:select>
					&nbsp;
					<a href="#" onclick="return false;"><span class="addLink3">View
							Detail</span> </a>
				</c:when>
				<c:otherwise>
					${characterizationForm.map.achar.instrumentConfiguration.instrument.type}&nbsp;
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
<br>

