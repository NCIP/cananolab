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
				Nanoparticle Report Search Results
			</h3>
		</td>
		<td align="right" width="15%">
			<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=sample_search_results')" class="helpText">Help</a>&nbsp;&nbsp; 
			<a href="searchReport.do?dispatch=setup" class="helpText">back</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyMessage.jsp?bundle=search" />
			<display:table name="reports" id="report" requestURI="searchReport.do" pagesize="25" class="displaytable">
				<display:column title="Report Title" property="title" sortable="true" href="searchReport.do?dispatch=download" paramId="fileId" paramProperty="id"/>
				<display:column title="Report Description" property="description" sortable="true" />
			</display:table>
		</td>
	</tr>
</table>

