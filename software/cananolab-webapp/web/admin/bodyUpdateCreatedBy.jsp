<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Update Created By" />
	<jsp:param name="topic" value="transfer_data_ownership_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="/updateCreatedBy">
	<jsp:include page="/bodyMessage.jsp" />
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td class="cellLabel" width="30%">
				Current Created By *
			</td>
			<td>
				<html:text styleId="currentCreatedBy" property="currentCreatedBy" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				New Created By *
			</td>
			<td>
				<html:text styleId="newOwner" property="newOwner" onchange="" />
			</td>
		</tr>
	</table>
	<br>
	<c:set var="hiddenDispatch" value="update" />
	<c:set var="hiddenPage" value="1" />
	<c:set var="resetOnclick"
		value="javascript:location.href='updateCreatedBy.do?dispatch=setupNew&page=0'" />
	<%@include file="../bodySubmitButtons.jsp"%>
</html:form>
