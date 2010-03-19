<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="gov.nih.nci.cananolab.service.sample.helper.CompositionServiceHelper"%>

<table id="summarySection${index}" width="100%" align="center"
	style="display: block" class="summaryViewNoGrid">
	<tr>
		<th align="left">
			<span class="summaryViewHeading">functionalizing entity</span>
		</th>
	</tr>
	<logic:iterate name="compositionForm"
		property="comp.functionalizingEntities" id="functionalizingEntity"
		indexId="ind">
	 	<%@include file="bodySingleFunctionalizingEntitySummaryView.jsp" %>
	</logic:iterate>
</table>
<div id="summarySeparator${index}">
	<br>
</div>
