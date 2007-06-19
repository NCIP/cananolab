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
				Nanoparticle Report Remote Search Results
			</h3>
		</td>
		<td align="right" width="15%">
			<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=remote_reports_search_results_help')" class="helpText">Help</a>&nbsp;&nbsp; <a href="remoteSearchReport.do?dispatch=setup" class="helpText">back</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyMessage.jsp?bundle=search" />
			<display:table name="remoteReports" id="report" requestURI="remoteSearchReport.do" pagesize="25" class="displaytable" decorator="gov.nih.nci.calab.dto.search.ReportDecorator">
				<display:column title="Report Title" property="remoteDownloadURL" sortable="true" />
				<display:column title="Report Type" property="instanceType" sortable="true" />
				<display:column title="Report Description" property="description" sortable="true" />
				<display:column title="Report Comments" property="comments" sortable="true" />
				<display:column title="Grid Node Host" property="gridNode" sortable="true" />
			</display:table>
		</td>
	</tr>
</table>

