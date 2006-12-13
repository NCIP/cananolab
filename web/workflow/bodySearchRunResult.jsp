<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<link rel="stylesheet" type="text/css" href="css/displaytag.css" />

<html:form action="/selectRun">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					Search Run Results
				</h3>
			</td>
			<td align="right" width="10%">
				<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=create_assay_run')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<display:table name="selectedRuns" id="run" requestURI="selectRun.do" pagesize="25" class="displaytable">
					<display:column title="Select">
						<input type="radio" name="runId" value="${run.id}" checked>
					</display:column>
					<display:column title="Sample Source" property="sampleSourceName" sortable="true" />
					<display:column title="Assay Type" property="assayBean.assayType" sortable="true" />
					<display:column title="Assay Name" property="assayBean.assayName" sortable="true" />
					<display:column title="Run Name" property="name" sortable="true" />
					<display:column title="Run by" property="runBy" sortable="true" />
					<display:column title="Run Date" property="runDate" sortable="true" format="{0,date,MM-dd-yyyy}" />
					<display:column title="Aliquots" property="aliquotNames" sortable="true" />
				</display:table>
				<div align="right">
					<html:submit value="View Details" />
				</div>
			</td>
		</tr>
	</table>
</html:form>
