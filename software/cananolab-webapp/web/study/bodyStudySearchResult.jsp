<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<link rel="stylesheet" type="text/css" href="css/displaytag.css" />
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Study Search Results" />
	<jsp:param name="topic" value="study_search_results_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
	<jsp:param name="other" value="Back" />
	<jsp:param name="otherLink" value="searchStudy.do?dispatch=setup" />
</jsp:include>
<table width="100%" align="center">
	<tr>
		<td colspan="2">
			<display:table name="studies" id="study" requestURI="searchStudy.do"
				pagesize="25" class="displaytable" partialList="true"
				size="resultSize"
				decorator="gov.nih.nci.cananolab.dto.common.StudyDecorator">
				<display:column title="" property="detailURL" />
				<display:column title="Study Name" property="studyName"
					sortable="true" escapeXml="true" />
				<display:column title="Study Title" property="studyTitle"
					sortable="true" escapeXml="true" />
				<display:column title="Sample Name" property="sampleName"
					sortable="true" />
				<display:column title="Primary<br>Point Of Contact"
					property="pointOfContactName" sortable="true" escapeXml="true" />
				<display:column title="Diseases" property="diseasesStr"
					sortable="true" />
				<display:column title="Owned By" property="ownerStr" />
			</display:table>

		</td>
	</tr>
</table>

