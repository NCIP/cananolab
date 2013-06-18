<%--L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="javascript/calendar2.js"></script>
<script type='text/javascript' src='javascript/addDropDownOptions.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>
<script type='text/javascript' src='javascript/StudyManager.js'></script>
<script type="text/javascript"
	src="/caNanoLab/dwr/interface/StudyManager.js"></script>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<c:set var="title" value="Submit" />
<c:if test="${!empty updateStudy}">
	<c:set var="title" value="Update" />
</c:if>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="${title} Study" />
	<jsp:param name="topic" value="submit_sample_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>

<html:form action="/study"
	onsubmit="return validateSavingTheData('newPointOfContact', 'point of contact');">
	<jsp:include page="/bodyMessage.jsp?bundle=study" />
	<table width="100%" align="center" class="submissionView" border="0">
		<tr>
			<td class="cellLabel" width="20%">
				Study Name *
			</td>
			<td colspan="3">
				<html:text property="studyBean.domain.name" size="50" />
				<%--
				<c:if test="${!empty sampleForm.map.sampleBean.domain.id}">
					<html:hidden styleId="sampleId" property="sampleBean.domain.id"
						value="${sampleForm.map.sampleBean.domain.id}" />
				</c:if>
				--%>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Study Title *
			</td>
			<td colspan="3">
				<html:text property="studyBean.domain.title" size="100" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Study Type
			</td>
			<td colspan="3">
				<html:select property="studyBean.domain.type">
					<option />
						<html:options name="studyTypes" />
				</html:select>
			</td>
			<%--
			<td class="cellLabel" width="15%">
				Is Animal Study?
			</td>
			<td>
				<input type="checkbox">
			</td>
			--%>
		</tr>
		<tr>
		    <c:set var="ind" value="1"/>
			<td class="cellLabel">
				Study Design Types
			</td>
			<td width="30%">
				<html:textarea property="studyBean.designTypes" cols="40" rows="3" styleId="designTypes" />
				<em>one type per line</em>
			</td>
			<td>
				<a href="#designTypeField" onclick="show('termBrowser${ind}')"><img
						src="images/icon_browse.jpg" align="middle"
						alt="search existing design types" border="0" /> </a>
			</td>
			<td>
			    <c:set var="source" value="caNanoLab database"/>
				<c:set var="searchFunction" value="showMatchedDesignTypes(${ind});"/>
				<c:set var="addFunction" value="populateDesignTypes(${ind});"/>
				<%@include file="../bodySelectMatchedTerms.jsp" %>
			</td>
		</tr>
		<tr>
		    <c:set var="ind" value="2"/>
			<td class="cellLabel">
				Diseases
			</td>
			<td>
				<html:textarea property="studyBean.domain.diseases" cols="40"
					rows="3" styleId="diseases"/>
				<em>one name per line</em>
			</td>
			<td>
				<a href="#diseaseField" onclick="show('termBrowser${ind}')"><img
						src="images/icon_browse.jpg" align="middle"
						alt="search diseases in EVS" border="0" /> </a>
			</td>
			<td>
				<c:set var="searchFunction" value="showMatchedDiseases(${ind});"/>
				<c:set var="addFunction" value="populateDiseases(${ind});"/>
				<c:set var="source" value="NCI Thesaurus"/>
				<%@include file="../bodySelectMatchedTerms.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Start Date
			</td>
			<td>
				<html:text property="studyBean.startDateStr" size="10"
					styleId="startDate" />
				<a href="javascript:cal1.popup();"><img
						src="images/calendar-icon.gif" width="22" height="18" border="0"
						alt="Click Here to Pick up the date"
						title="Click Here to Pick up the date" align="top"> </a>
			</td>
			<td class="cellLabel" width="15%">
				End Date
			</td>
			<td>
				<html:text property="studyBean.endDateStr" size="10" styleId="endDate" />
				<a href="javascript:cal2.popup();"><img
						src="images/calendar-icon.gif" width="22" height="18" border="0"
						alt="Click Here to Pick up the date"
						title="Click Here to Pick up the date" align="top"> </a>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Public Release Date
			</td>
			<td>
				<html:text property="studyBean.publicReleaseDateStr" size="10"
					styleId="publicReleaseDate" />
				<a href="javascript:cal3.popup();"><img
						src="images/calendar-icon.gif" width="22" height="18" border="0"
						alt="Click Here to Pick up the date"
						title="Click Here to Pick up the date" align="top"> </a>
			</td>
			<td class="cellLabel">
				Submission Date
			</td>
			<td>
				<html:text property="studyBean.submissionDateStr" size="10"
					styleId="submissionDate" />
				<a href="javascript:cal4.popup();"><img
						src="images/calendar-icon.gif" width="22" height="18" border="0"
						alt="Click Here to Pick up the date"
						title="Click Here to Pick up the date" align="top"> </a>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Study Description
			</td>
			<td colspan="3">
				<html:textarea property="studyBean.domain.description" rows="6"
					cols="80" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Study Outcome
			</td>
			<td colspan="3">
				<html:textarea property="studyBean.domain.outcome" rows="6"
					cols="80" />
			</td>
		</tr>
		<tr>
		    <c:set var="source" value="study" />
			<td class="cellLabel">
				Point of Contact *
			</td>
			<td colspan="3">
				<c:set var="disableOuterButtons" value="false"/>
				<c:set var="newAddPOCButtonStyle" value="display:block" />
				<c:if
					test="${empty studyForm.map.studyBean.primaryPOCBean.domain.id}">
					<c:set var="newAddPOCButtonStyle" value="display:none" />
					<c:set var="disableOuterButtons" value="true"/>
				</c:if>
				<a href="#"
					onclick="javascript:confirmAddNew(['Access'], 'PointOfContact', 'Point Of Contact', 'clearPointOfContact()', '${source}');"
					id="addPointOfContact" style="${newAddPOCButtonStyle}"><img
						align="top" src="images/btn_add.gif" border="0" /></a>
			</td>
		</tr>
		<c:if
			test="${!empty studyForm.map.studyBeanBean.primaryPOCBean.domain.id || ! empty studyForm.map.studyBeanBean.otherPOCBeans }">
			<tr>
				<td colspan="4">
					<c:set var="primaryPOC"
						value="${studyForm.map.studyBeanBean.primaryPOCBean}" />
					<c:set var="otherPOCs"
						value="${studyForm.map.studyBeanBean.otherPOCBeans}" />
					<c:set var="edit" value="true" />
					<%@ include file="../bodyPointOfContactEdit.jsp"%>
				</td>
			</tr>
		</c:if>
		<tr>
			<td colspan="4">
				<c:set var="newPOCStyle" value="display:none" />
				<c:if
					test="${empty studyForm.map.studyBean.primaryPOCBean.domain.id}">
					<c:set var="newPOCStyle" value="display:block" />
				</c:if>
				<c:if test="${openPOC eq true }">
					<c:set var="newPOCStyle" value="display:block" />
				</c:if>
				<div style="${newPOCStyle}" id="newPointOfContact">
					<a name="submitPointOfContact"> <c:set var="pocForm"
							value="studyForm" /> <c:set var="poc" value="studyBean.thePOC" />
						<%@include
							file="../bodySubmitPointOfContact.jsp"%>
				</div>
			</td>
		</tr>
	</table>
	<br/>
	<c:set var="groupAccesses"
		value="${studyForm.map.studyBean.groupAccesses}" />
	<c:set var="userAccesses"
		value="${studyForm.map.studyBean.userAccesses}" />
	<c:set var="accessParent" value="studyBean" />
	<c:set var="dataType" value="Study" />
	<c:set var="parentAction" value="study" />
	<c:set var="parentForm" value="studyForm" />
	<c:set var="parentPage" value="4" />
	<c:set var="protectedData" value="${studyForm.map.studyBean.domain.id}" />
	<c:set var="newData" value="true" />
	<c:set var="isPublic" value="${studyForm.map.studyBean.publicStatus}" />
	<c:set var="isOwner" value="${studyForm.map.studyBean.userIsOwner}" />
	<c:set var="ownerName"
		value="${studyForm.map.studyBean.domain.createdBy}" />
	<c:if test="${updateStudy}">
		<c:set var="newData" value="false" />
	</c:if>
	<%@include file="../bodyManageAccessibility.jsp"%>
	<br>
	<c:if test="${!empty updateStudy}">
		<c:set var="updateId" value="0" />
	</c:if>
	<%@include file="../bodySubmitButtons.jsp"%>
</html:form>
<script language="JavaScript">
	<!-- //
		var cal1 = new calendar2(document.getElementById('startDate'));
	    cal1.year_scroll = true;
		cal1.time_comp = false;
		cal1.context = '${pageContext.request.contextPath}';
		var cal2 = new calendar2(document.getElementById('endDate'));
	    cal2.year_scroll = true;
		cal2.time_comp = false;
		cal2.context = '${pageContext.request.contextPath}';
		var cal3 = new calendar2(document.getElementById('publicReleaseDate'));
	    cal3.year_scroll = true;
		cal3.time_comp = false;
		cal3.context = '${pageContext.request.contextPath}';
		var cal4 = new calendar2(document.getElementById('submissionDate'));
	    cal4.year_scroll = true;
		cal4.time_comp = false;
		cal4.context = '${pageContext.request.contextPath}';
  	//-->
</script>