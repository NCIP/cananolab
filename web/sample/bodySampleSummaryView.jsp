<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle"
		value="${fn:toUpperCase(location)} Sample ${sampleForm.map.sampleBean.domain.name}" />
	<jsp:param name="topic" value="submit_sample_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<jsp:include page="/bodyMessage.jsp?bundle=sample" />

<table class="summaryViewNoGrid" width="100%" align="center"
	bgcolor="#dbdbdb">
	<tr>
		<th>
			Sample Information
		</th>
	</tr>
	<tr>
		<td>
			<jsp:include page="bodyBareSampleSummaryView.jsp" />
			<br/>
		</td>
	</tr>
</table>
