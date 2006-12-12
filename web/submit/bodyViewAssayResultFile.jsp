<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table width="100%" align="center">
	<tr>
		<td>
			<h3>
				<br>
				Assay Result File
			</h3>
		</td>
		<td align="right" width="15%">
			<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=create_nanoparticle')" class="helpText">Help</a>&nbsp;&nbsp;
			<a href="javascript:history.go(-1)" class="helpText">back</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyMessage.jsp?bundle=submit" />
			<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
				<tbody>
					<tr class="topBorder">
						<td class="formTitle" colspan="4">
							<div align="justify">
								File Information
							</div>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Assay Result File Name*</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<logic:present name="characterizationFile${param.chartInd}">
								<bean:define id="fileId" name='characterizationFile${param.chartInd}' property='id' type="java.lang.String" />
								<a href="${actionName}.do?dispatch=download&amp;fileId=${fileId}"><bean:write name="characterizationFile${param.chartInd}" property="name" /></a>
							</logic:present>
						</td>
					<tr>
						<td class="leftLabel">
							<strong>Assay Result File Title*</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<bean:write name="characterizationFile${param.chartInd}" property="title" />
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Assay Result File Description</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<bean:write name="characterizationFile${param.chartInd}" property="description" />
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Keywords <em>(one per line)</em></strong>
						</td>
						<td class="rightLabel" colspan="3">
							<bean:write name="characterizationFile${param.chartInd}" property="keywordsStr" />
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Visibility</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<bean:write name="characterizationFile${param.chartInd}" property="visibilityStr" />
							&nbsp;
						</td>
					</tr>
				</tbody>
			</table>
			<br>
		</td>
	</tr>
</table>
