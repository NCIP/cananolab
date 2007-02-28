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
			<a href="searchWorkflow.do?dispatch=setup&page=0"
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
								<display:column title="File<br>Name" property="self" sortable="true" decorator="gov.nih.nci.calab.dto.workflow.FileURLDecorator" />
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
						</logic:present>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
