<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<tr>
	<td>
		<h4>
			Submit a New Publication
		</h4>
	</td>
	<td align="right" width="20%">
		<jsp:include page="/webHelp/helpGlossary.jsp">
			<jsp:param name="topic" value="manage_reports_help" />
			<jsp:param name="glossaryTopic" value="glossary_help" />
		</jsp:include>		
	</td>
</tr>
<tr><td colspan="2">
	<table summary="" cellpadding="0" cellspacing="0" border="0"
							width="50%" height="100%" class="sidebarSection">
		<tr>
			<td class="sidebarTitle" height="20">&nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="2" class="sidebarContent">
			<html:link page="/submitPublication.do?dispatch=setup&amp;page=0&amp;submitType=publications&amp;particleId=${particleId}&amp;location=local" scope="page">
				Submit a New Publication</html:link> 
			
		</tr>
		<tr>
			<td colspan="2" class="sidebarContent">
			<html:link page="/submitReport.do?dispatch=setup&amp;page=0&amp;submitType=publications&amp;particleId=${particleId}&amp;location=local" scope="page">
				Submit a New Report</html:link> 
			
		</tr>
	</table>
</td></tr>
