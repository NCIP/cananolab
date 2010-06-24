<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type='text/javascript' src='javascript/addDropDownOptions.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<c:set var="title" value="Submit" />
<c:if test="${!empty updateSample}">
	<c:set var="title" value="Update" />
</c:if>

<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="${title} Sample" />
	<jsp:param name="topic" value="submit_sample_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="/sample"
	onsubmit="return validateSavingTheData('newPointOfContact', 'point of contact');">
	<jsp:include page="/bodyMessage.jsp?bundle=sample" />
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td class="cellLabel" width="20%">
				Sample Name *
			</td>
			<td>
				<html:text property="sampleBean.domain.name" size="80" />
				<c:if test="${!empty sampleForm.map.sampleBean.domain.id}">
					<html:hidden styleId="sampleId" property="sampleBean.domain.id"
						value="${sampleForm.map.sampleBean.domain.id}" />
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Point of Contact *
			</td>
			<td>
				<c:set var="newAddPOCButtonStyle" value="display:block" />
				<c:if
					test="${empty sampleForm.map.sampleBean.primaryPOCBean.domain.id}">
					<c:set var="newAddPOCButtonStyle" value="display:none" />
				</c:if>
				<a href="#"
					onclick="javascript:confirmAddNew('PointOfContact', 'Point Of Contact', 'clearPointOfContact()');"
					id="addPointOfContact" style="${newAddPOCButtonStyle}"><img
						align="top" src="images/btn_add.gif" border="0" /> </a>
			</td>
		</tr>
		<c:if
			test="${!empty sampleForm.map.sampleBean.primaryPOCBean.domain.id || ! empty sampleForm.map.sampleBean.otherPOCBeans }">
			<tr>
				<td colspan="2">
					<c:set var="edit" value="true" />
					<%@ include file="bodyPointOfContactEdit.jsp"%>
				</td>
			</tr>
		</c:if>
		<tr>
			<td colspan="2">
				<c:set var="newPOCStyle" value="display:none" />
				<c:if
					test="${empty sampleForm.map.sampleBean.primaryPOCBean.domain.id}">
					<c:set var="newPOCStyle" value="display:block" />
				</c:if>
				<div style="${newPOCStyle}" id="newPointOfContact">
					<a name="submitPointOfContact"><%@ include
							file="bodySubmitPointOfContact.jsp"%></a>
				</div>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Keywords
				<i>(one keyword per line)</i>
			</td>
			<td>
				<html:textarea property="sampleBean.keywordsStr" rows="6" cols="80" />
			</td>
		</tr>
		<%@include file="../community/bodyManageAccessibility.jsp" %>
		<tr>
			<td class="cellLabel">
				Data Availability Metrics
			</td>
			<td>
			<c:set var="updateAvailabilityOnclick"
		value="updateDataAvailability('dataAvailability');"/>
			<c:if test="${!empty user && !sampleForm.map.sampleBean.hasDataAvailability && user.admin}">
				<input type="button" value="Generate"
					onclick="${generateDataAvailabilityOnclick}">
			</c:if>
			<c:if test="${!empty user && sampleForm.map.sampleBean.hasDataAvailability && user.admin}">
				<input type="button" value="Edit"
					onclick="javascript:showhide('dataAvailability');" >

			</c:if>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<c:set var="dataAvailabilityStyle" value="display:none" />
				<div style="${dataAvailabilityStyle}" id="dataAvailability">
					<a name="viewDataAvailability"><%@ include
							file="bodyDataAvailability.jsp"%></a>
				</div>
			</td>
		</tr>
	</table>
	<br>
	<c:if test="${!empty updateSample}">
		<c:set var="updateId" value="${sampleForm.map.sampleBean.domain.id}" />
	</c:if>
	<c:set var="resetOnclick" value="this.form.reset();" />
	<c:set var="deleteOnclick"
		value="deleteData('sample', sampleForm, 'sample', 'delete')" />
	<c:set var="deleteButtonName" value="Delete" />
	<c:set var="cloneOnclick"
		value="gotoPage('sample.do?dispatch=setupClone&page=0&cloningSample=${sampleForm.map.sampleBean.domain.name}')" />
	<c:set var="hiddenDispatch" value="create" />
	<c:set var="hiddenPage" value="2" />
	<c:set var="review" value="false"/>
	<c:set var="assignPublic" value="true"/>
	<%@include file="../bodySubmitButtons.jsp"%>
</html:form>

