<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html:form action="/useAliquot">

	<table width="80%" align="center">
		<tr>
			<td width="10%">
				&nbsp;
			</td>
			<td>
				<br>
				<h3>
					Use Aliquot
				</h3>
			</td>
			<td align="right" width="10%">
				<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=create_assay_run')" class="helpText">Help</a>
			</td>
	</table>
	<jsp:include page="/workflow/bodyWorkflowInfo.jsp" />
	<table width="80%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
		<tr class="topBorder">
			<td colspan="2" class="formTitle">
				<div align="justify">
					Use Aliquot
				</div>
			</td>
		</tr>
		<tr>
			<td class="formLabel">
				<strong>Aliquot ID</strong>
			</td>
			<td class="formField">
				<div align="left">
					* Hold down the shift key for multiple selections.
					<br>
					<span class="formField" align="left"><span class="mainMenu"><span class="formMessage"><strong> <html:select property="aliquotIds" multiple="true" size="10">
										<html:options collection="allUnmaskedAliquots" property="aliquotId" labelProperty="aliquotName" />
									</html:select> </strong></span></span></span> <span class="formFieldWhite"> </span>
				</div>
			</td>
		</tr>
	</table>
	<br>

	<table width="80%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
		<tr class="topBorder">
			<td class="formTitle">
				<div align="justify">
					General Comments
				</div>
			</td>
		</tr>
		<tr>
			<td class="formLabel">
				<div align="left">
					<span class="formField"><html:textarea property="comments" rows="3"/></span>
				</div>
			</td>
		</tr>
		<tr>
			<td width="30%">
				<table border="0" align="right" cellpadding="4" cellspacing="0">
					<tr>
						<td>
							<div align="left">
								<input type="hidden" name="runId" value="${param.runId}" />
								<input type="hidden" name="runName" value="${param.runName}">
								<input type="hidden" name="assayName" value="${param.assayName}">
								<input type="hidden" name="assayType" value="${param.assayType}">
								<html:reset />
								<html:submit />
								<input type="button" value="Cancel" onclick="javascript:history.go(-1)">
							</div>
						</td>
					</tr>
				</table>
				<div align="right"></div>
			</td>
		</tr>
	</table>
	<p>
		&nbsp;
	</p>
	<p>
		&nbsp;
	</p>
</html:form>
