<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table border="0" cellpadding="3" cellspacing="0" width="100%"
	align="left" class="topBorderOnly" summary="">
	<tr>
	<tr class="topBorder">
		<td class="formTitle" colspan="2">
			<div align="justify">
				Design and Methods
			</div>
		</td>
	</tr>
	<tr>
		<td class="leftLabelNoBottom" valign="top">
			<strong>Description</strong>
		</td>
		<td class="rightLabelNoBottom">
			<html:textarea property="achar.description" cols="120" rows="8" />
		</td>
	</tr>
	<tr>
		<td class="completeLabelNoTopBottom" valign="top" colspan="2">
			<strong>Technique and Instrument</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a style="" id="addTechniqueInstrument"
				href="javascript:resetTheExperimentConfig(true);"><img
					align="top" src="images/btn_add.gif" border="0" /> </a>
		</td>
	</tr>
	<tr>
		<td class="completeLabelNoTopBottom" valign="top" colspan="2">
			<c:set var="charBean" value="${characterizationForm.map.achar}"/>
			<c:set var="edit" value="true"/>
			<%@ include file="bodyExperimentConfigView.jsp" %>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="completeLabelNoTop" valign="top" colspan="2">
			<div id="newExperimentConfig" style="display: none;">
				<jsp:include page="bodyExperimentConfigEdit.jsp" />
			</div>
			&nbsp;
		</td>
	</tr>
</table>
<br>