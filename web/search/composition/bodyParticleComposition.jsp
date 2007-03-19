<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table width="100%" align="center">
	<tr>
		<td>
			<h4>
				<br>
				Physical Characterization - Composition
			</h4>
		</td>
		<td align="right" width="15%">
			<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caLAB_1.0_OH&amp;topic=composition_help')" class="helpText">Help</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<h5 align="center">
				${nanoparticleCompositionForm.map.particleName} (${nanoparticleCompositionForm.map.particleType})
			</h5>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyMessage.jsp?bundle=submit" />
			<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
				<tr>
				<tr class="topBorder">
					<td class="formTitle" colspan="4">
						<div align="justify">
							Summary
						</div>
					</td>
				</tr>
				<tr>
					<td class="leftLabel">
						<strong>Characterization Source* </strong>
					</td>
					<td class="label">
						${nanoparticleCompositionForm.map.characterizationSource}&nbsp;
					</td>
					<td class="label">
						<strong>View Title*</strong>
						<br>
						<em>(text is truncated after 20 characters)</em>
					</td>
					<td class="rightLabel">
						${nanoparticleCompositionForm.map.viewTitle}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="leftLabel" valign="top">
						<strong>Description</strong>
					</td>
					<td class="rightLabel" colspan="3">
						${nanoparticleCompositionForm.map.description}&nbsp;
					</td>
				</tr>
			</table>
			<br>
			<jsp:include page="${nanoparticleCompositionForm.map.particlePage}" />

		</td>
	</tr>
</table>
