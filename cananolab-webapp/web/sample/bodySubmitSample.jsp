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
<script type='text/javascript' src='javascript/addDropDownOptions.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>
<script type='text/javascript' src='javascript/SampleManager.js'></script>
<script type="text/javascript"
	src="/caNanoLab/dwr/interface/SampleManager.js"></script>
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
			<td class="cellLabel" width="15%">
				Sample Name *
			</td>
			<td colspan="3">
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
			<td colspan="3">
				<c:set var="disableOuterButtons" value="false" />
				<c:set var="newAddPOCButtonStyle" value="display:block" />
				<c:if
					test="${empty sampleForm.map.sampleBean.primaryPOCBean.domain.id}">
					<c:set var="newAddPOCButtonStyle" value="display:none" />
					<c:set var="disableOuterButtons" value="true" />
				</c:if>
				<a href="#"
					onclick="javascript: confirmAddNew( [ 'Access' ], 'PointOfContact', 'Point Of Contact', 'clearPointOfContact()');" id="addPointOfContact" style="${newAddPOCButtonStyle}"><img
						align="top" src="images/btn_add.gif" border="0" /> </a>
			</td>
		</tr>
		<c:if
			test="${!empty sampleForm.map.sampleBean.primaryPOCBean.domain.id || ! empty sampleForm.map.sampleBean.otherPOCBeans }">
			<tr>
				<td colspan="4">
					<c:set var="edit" value="true" />
					<%@ include file="bodyPointOfContactEdit.jsp"%>
				</td>
			</tr>
		</c:if>
		<tr>
			<td colspan="4">
				<c:set var="newPOCStyle" value="display:none" />
				<c:if
					test="${empty sampleForm.map.sampleBean.primaryPOCBean.domain.id}">
					<c:set var="newPOCStyle" value="display:block" />
				</c:if>
				<c:if test="${openPOC eq true }">
					<c:set var="newPOCStyle" value="display:block" />
				</c:if>
				<div style="${newPOCStyle}" id="newPointOfContact">
					<a name="submitPointOfContact"><%@ include
							file="bodySubmitPointOfContact.jsp"%></a>
				</div>
			</td>
		</tr>
		<tr>
			<c:set var="ind" value="1" />
			<td class="cellLabel">
				Keywords
			</td>
			<td width="40%">
				<html:textarea property="sampleBean.keywordsStr" rows="6" cols="60"
					styleId="keywordsStr"></html:textarea>
				<em>one keyword per line</em>
			</td>
			<td width="15%">
				<a href="#keywordField" onclick="show('termBrowser${ind}')"><img
						src="images/icon_browse.jpg" align="middle"
						alt="search terms in NPO" border="0" /> </a>
			</td>
			<td>
				<c:set var="source" value="NPO" />
				<c:set var="searchFunction" value="showMatchedKeywords(${ind});" />
				<c:set var="addFunction" value="populateKeywords(${ind});" />
				<%@include file="../bodySelectMatchedTerms.jsp"%>
			</td>
		</tr>
	</table>
	<c:if test="${!empty updateSample}">
		<br />
		<table width="100%" align="center" class="submissionView"
			id="accessBlock">
			<tr>
				<c:set var="showDataAvailabilityMetrics"
					value="${sampleForm.map.sampleBean.hasComposition
						|| sampleForm.map.sampleBean.hasCharacterizations || sampleForm.map.sampleBean.hasPublications }" />
				<c:if test="${!empty user }">
					<td class="cellLabel" width="15%" >
						Data Availability Metrics
					</td>
				</c:if>
				<td>
					<c:if
						test="${!empty user && !sampleForm.map.sampleBean.hasDataAvailability }">
						<input type="image" value="Generate" src="images/btn_generate.gif"
							onclick="javascript:generateDataAvailability(sampleForm, 'sample','generateDataAvailability');">
					</c:if>
				</td>
			</tr>
			<c:if
				test="${!empty user && sampleForm.map.sampleBean.hasDataAvailability eq 'true'}">
				<tr>
					<td colspan="2">
						<table class="editTableWithGrid" width="95%" align="center">
							<tr>
								<th>
									caNanoLab Availability Score
								</th>
								<th>
									MINChar Availability Score
								</th>
								<th></th>

							</tr>
							<tr>
								<td>
									<c:out value="${sampleForm.map.sampleBean.caNanoLabScore}" />
								</td>
								<td>
									<c:out value="${sampleForm.map.sampleBean.mincharScore}" />
								</td>
								<td>
									<a href="#"
										onclick="javascript:manageDataAvailability('${sampleForm.map.sampleBean.domain.id}', 'sample', 'dataAvailabilityView');">
										Edit</a>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</c:if>
			<tr>
				<td colspan="4" align="center">
					<c:set var="dataAvailabilityStyle" value="display:none" />
					<div style="${dataAvailabilityStyle}" id="dataAvailability"></div>
				</td>
			</tr>
		</table>
	</c:if>
	<br />
	<c:set var="groupAccesses"
		value="${sampleForm.map.sampleBean.groupAccesses}" />
	<c:set var="userAccesses"
		value="${sampleForm.map.sampleBean.userAccesses}" />
	<c:set var="accessParent" value="sampleBean" />
	<c:set var="dataType" value="Sample" />
	<c:set var="parentAction" value="sample" />
	<c:set var="parentForm" value="sampleForm" />
	<c:set var="parentPage" value="4" />
	<c:set var="protectedData"
		value="${sampleForm.map.sampleBean.domain.id}" />
	<c:set var="newData" value="true" />
	<c:set var="isPublic" value="${sampleForm.map.sampleBean.publicStatus}" />
	<c:set var="isOwner" value="${sampleForm.map.sampleBean.userIsOwner}" />
	<c:set var="ownerName"
		value="${sampleForm.map.sampleBean.domain.createdBy}" />
	<c:if test="${updateSample}">
		<c:set var="newData" value="false" />
	</c:if>
	<%@include file="../bodyManageAccessibility.jsp"%>
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
	<c:if test="${review}">
		<c:set var="submitForReviewOnclick"
			value="submitReview(sampleForm, 'sample', '${sampleForm.map.sampleBean.domain.id}', '${sampleForm.map.sampleBean.domain.name}', 'sample')" />
	</c:if>
	<c:set var="validate" value="false" />
	<c:if test="${!user.curator && sampleForm.map.sampleBean.publicStatus}">
		<c:set var="validate" value="true" />
	</c:if>
	<c:set var="showDelete" value="false" />
	<c:if
		test="${sampleForm.map.sampleBean.userDeletable && !empty updateId}">
		<c:set var="showDelete" value="true" />
	</c:if>
	<%@include file="../bodySubmitButtons.jsp"%>
</html:form>

