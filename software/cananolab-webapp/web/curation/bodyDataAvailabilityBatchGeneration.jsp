<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type='text/javascript' src='javascript/addDropDownOptions.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>
<script type="text/javascript" src="javascript/SampleManager.js"></script>
<script type="text/javascript"
	src="/caNanoLab/dwr/interface/SampleManager.js"></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<c:set var="title" value="Copy" />
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Manage Batch Data Availability" />
	<jsp:param name="topic" value="copy_sample_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="/generateBatchDataAvailability">
	<jsp:include page="/bodyMessage.jsp" />
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td class="cellLabel" width="20%">
				<!--<html:radio styleId="option1" property="option" value="option1"  />
				Generate data availability for samples without data availability.
				<br>
				
				&nbsp;&nbsp;or
				<br>
				--><html:radio styleId="option1" property="option" value="option1" />
				Re-generate data availability for all samples.
				<br>
				&nbsp;&nbsp;or
				<br>

				<html:radio styleId="option2" property="option" value="option2" />
				Re-generate data availability for samples with existing data availability.
				<br>
				&nbsp;&nbsp;or
				<br>
				<html:radio styleId="option3" property="option" value="option3" />
				Delete data availability for all samples.
				
			</td>

		</tr>
	</table>
	<br />
	<c:set var="hiddenDispatch" value="generate" />
	<c:set var="hiddenPage" value="1" />
	<c:set var="resetOnclick"
		value="javascript:location.href='generateBatchDataAvailability.do?dispatch=setupNew&page=0'" />
	<%@include file="../bodySubmitButtons.jsp"%>
</html:form>

