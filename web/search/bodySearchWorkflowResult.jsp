<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<link rel="stylesheet" type="text/css" href="css/displaytag.css" />
<table width="100%" align="center">
	<tr>
		<td>
			<h3>
				<br>
				Search Results
			</h3>
		</td>
		<td align="right" width="15%">
			<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caLAB_1.0_OH&amp;topic=workflow_search_results')" class="helpText">Help</a> &nbsp; &nbsp;
			<a href="searchWorkflow.do?dispatch=setup&page=0&rememberSearch=true"
				class="helpText">Back</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<table width="90%" border="0" align="right" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<logic:present name="workflows">
							<display:table name="workflows" id="workflow" requestURI="searchWorkflow.do" pagesize="100" class="displaytable">
								<display:column title="File<br>Name" property="self" sortable="true" decorator="gov.nih.nci.calab.dto.search.FileURLDecorator" />
								<display:column property="assay.assayType" title="Assay<br>Type" sortable="true" />
								<display:column property="assay.assayName" title="Assay<br>Name" sortable="true" />
								<display:column property="run.sortableName" title="Assay<br>Run<br>Name" sortable="true" />
								<display:column property="run.runDate" title="Assay<br>Run<br>Date" sortable="true" format="{0,date,MM-dd-yyyy}" />
								<display:column property="file.inoutType" title="In/Out<br>File" sortable="true" />
								<display:column property="file.createdDate" title="File<br>Submission<br>Date" sortable="true" format="{0,date,MM-dd-yyyy}" />
								<display:column property="file.fileSubmitter" title="File<br>Submitter" sortable="true" />
								<display:column property="file.fileMaskStatus" title="File<br>Status" sortable="true" />
								<display:column property="aliquot.sortableName" title="Aliquot<br>ID" sortable="true" />
								<display:column property="aliquot.maskStatus" title="Aliquot<br>Status" sortable="true" />
							</display:table>
							<%--
				<table border="0" align="center" cellpadding="0" cellspacing="0">
					<tr align="center">
						<td width="111" class="formTitle">
							File Name
						</td>
						<td width="33" class="formTitle">
							Assay Type
						</td>
						<td width="51" class="formTitle">
							Assay Name
						</td>
						<td width="51" class="formTitle">
							Assay Run Name
						</td>
						<td width="51" class="formTitle">
							Assay Run Date
						</td>
						<td width="51" class="formTitle">
							Input File or Output File
						</td>
						<td width="53" class="formTitle">
							File Submission Date
						</td>
						<td width="61" class="formTitle">
							File Submitter
						</td>
						<td width="44" class="formTitle">
							File Status
						</td>
						<td width="53" class="formTitle">
							Aliquot ID
						</td>
						<td width="53" class="formTitle">
							Aliquot Status
						</td>
					</tr>
					<logic:iterate name="workflows" id="workflow" type="gov.nih.nci.calab.dto.search.WorkflowResultBean" indexId="rowNum">
						<c:choose>
							<c:when test="${rowNum % 2 == 0}">
								<c:set var="style" value="formFieldGrey" />
								<c:set var="style0" value="leftBorderedFormFieldGrey" />
							</c:when>
							<c:otherwise>
								<c:set var="style" value="formFieldWhite" />
								<c:set var="style0" value="leftBorderedFormFieldWhite" />
							</c:otherwise>
						</c:choose>
						<tr>
							<td class="${style0}">
								<a href="${pageContext.request.contextPath}/downloadSearchedFile.do?method=downloadFile&fileName=${workflow.file.filename}&runId=${workflow.run.id}&runName=${workflow.run.name}&inout=${workflow.file.inoutType}&assayType=${workflow.assay.assayType}&assayName=${workflow.assay.assayName}"><bean:write name="workflow" property="file.filename" /></a>&nbsp;
							</td>
							<td class="${style}">
								<bean:write name="workflow" property="assay.assayType" />
								&nbsp;
							</td>
							<td class="${style}">
								<bean:write name="workflow" property="assay.assayName" />
								&nbsp;
							</td>
							<td class="${style}">
								<bean:write name="workflow" property="run.name" />
								&nbsp;
							</td>
							<td class="${style}">
								<bean:write name="workflow" property="run.runDate" />
								&nbsp;
							</td>
							<td class="${style}">
								<bean:write name="workflow" property="file.inoutType" />
								&nbsp;
							</td>
							<td class="${style}">
								<bean:write name="workflow" property="file.createDateStr" />
								&nbsp;
							</td>
							<td class="${style}">
								<bean:write name="workflow" property="file.fileSubmitter" />
								&nbsp;
							</td>
							<td class="${style}">
								<bean:write name="workflow" property="file.fileMaskStatus" />
								&nbsp;
							</td>
							<td class="${style}">
								<bean:write name="workflow" property="aliquot.aliquotName" />
								&nbsp;
							</td>
							<td class="${style}">
								<bean:write name="workflow" property="aliquot.maskStatus" />
								&nbsp;
							</td>
						</tr>
					</logic:iterate>
				</table>
				--%>
						</logic:present>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
