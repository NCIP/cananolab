<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Transfer Ownership" />
	<jsp:param name="topic" value="site_preference_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="/transferOwner">
	<jsp:include page="/bodyMessage.jsp?bundle=admin" />
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td class="cellLabel" width="30%">
				Current Owner Login Name *
			</td>
			<td>
				<html:text styleId="currentOwner" property="currentOwner" size="30" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				New Owner Login Name *
			</td>
			<td>
				<html:text styleId="newOwner" property="newOwner" size="30" />
			</td>

		</tr>
		<tr>
			<td class="cellLabel">
				Data Type *
			</td>
			<td>
				<html:select styleId="dataType" property="dataType" >
					<html:option value="Sample" />
					<html:option value="Publication" />
					<html:option value="Protocol" />
					<html:option value="Collaboration Group" />
				</html:select>
			</td>
		</tr>
	</table>
	<br>
	<c:set var="hiddenDispatch" value="transfer" />
	<c:set var="hiddenPage" value="1" />
	<c:set var="resetOnclick"
		value="javascript:location.href='transferOwner.do?dispatch=setupNew&page=0'" />
	<%@include file="../bodySubmitButtons.jsp"%>
</html:form>
