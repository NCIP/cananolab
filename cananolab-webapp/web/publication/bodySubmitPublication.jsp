<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type="text/javascript" src="javascript/PublicationManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/PublicationManager.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<c:set var="sampleTitle" value=""/>
<c:if test="${!empty param.sampleId}">
	<c:set var="sampleTitle" value="for ${sampleName}"/>
</c:if>
<c:choose>
	<c:when test="${param.dispatch eq 'setupUpdate'}">
		<c:set var="title" value="Update Publication ${sampleTitle}" />
		<jsp:include page="/bodyTitle.jsp">
			<jsp:param name="pageTitle" value="${title}" />
			<jsp:param name="topic" value="submit_publication_help" />
			<jsp:param name="glossaryTopic" value="glossary_help" />
			<jsp:param name="other" value="Back" />
			<jsp:param name="otherLink"
				value="javascript:gotoPage('publicationResults.do')" />
		</jsp:include>
	</c:when>
	<c:otherwise>
		<c:set var="title" value="Submit Publication ${sampleTitle}" />
		<jsp:include page="/bodyTitle.jsp">
			<jsp:param name="pageTitle" value="${title}" />
			<jsp:param name="topic" value="submit_publication_help" />
			<jsp:param name="glossaryTopic" value="glossary_help" />
		</jsp:include>
	</c:otherwise>
</c:choose>

<jsp:include page="/bodyMessage.jsp?bundle=publication" />
<html:form action="/publication" enctype="multipart/form-data"
	onsubmit="return validateSavingTheData('newAuthor', 'Authors');" styleId="publicationForm">
	<table width="100%" align="center" class="submissionView" summary="layout">
		<tr>
			<td class="cellLabel" width="100">
				<label for="category">Publication Type *</label>
			</td>
			<td>
				<table class="invisibleTable" summary="layout">
					<tr>
						<td>
							<div id="categoryPrompt">
								<html:select property="publication.domainFile.category"
									onchange="javascript:callPrompt('Publication Type', 'category', 'categoryPrompt');
														clearPublication();updateSubmitFormBasedOnCategory();enableAutoFields();"
									styleId="category">
									<option value=""></option>
									<html:options name="publicationCategories" />
									<option value="other">
										[other]
									</option>
								</html:select>
							</div>
						</td>
						<td class="cellLabel">
							<label for="status">Publication Status*</label>
						</td>
						<td colspan="3">
							<div id="statusPrompt">
								<html:select property="publication.domainFile.status"
									onchange="javascript:callPrompt('Publication status', 'status', 'statusPrompt');"
									styleId="status">
									<option value=""></option>
									<html:options name="publicationStatuses" />
									<option value="other">
										[other]
									</option>
								</html:select>
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr id="pubMedRow" style="display: none">
			<td class="cellLabel">
				<label for="domainFile.pubMedId">PubMed ID</label>
			</td>
			<td>
				<a
					href="http://www.ncbi.nlm.nih.gov/pubmed/${publicationForm.map.publication.domainFile.pubMedId}"
					target="_pubmed"> Click to look up PubMed Identifier</a>
				<br>
				<html:text property="publication.domainFile.pubMedId" size="50"
					styleId="domainFile.pubMedId"
					onchange="javascript:fillPubMedInfo()" />
				<br>
				<i> clicking outside of the text field after entering a valid
					PubMed ID enables auto-population of PubMed related fields</i>
			</td>
		</tr>
		<tr id="doiRow" style="display: none">
			<td class="cellLabel">
				<label for="domainFile.digitalObjectId">Digital Object ID</label>
			</td>
			<td>
				<html:text property="publication.domainFile.digitalObjectId"
					styleId="domainFile.digitalObjectId" size="30"
					onchange="javascript:updateWithExistingDOI()" />
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				<label for="domainFile.title">Title*</label>
			</td>
			<td>
				<html:text property="publication.domainFile.title"
					styleId="domainFile.title" size="120" />
			</td>
		</tr>
		<tr id="journalRow" style="display: none">
			<td class="cellLabel">
				<label for="domainFile.journalName">Journal&nbsp;</label>
			</td>
			<td>
				<html:text property="publication.domainFile.journalName"
					styleId="domainFile.journalName" size="120" />
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				<label for="domainFile.year">Year of Publication</label>
			</td>
			<td>
				<html:text property="publication.domainFile.year" size="5"
					styleId="domainFile.year" onkeydown="return filterInteger(event)" />
			</td>
		</tr>
		<tr id="volumePageRow" style="display: none">
			<td class="cellLabel">
				<label for="domainFile.volume">Volume</label>
			</td>
			<td>
				<table class="invisibleTable" summary="layout">
					<tr>
						<td>
							<html:text property="publication.domainFile.volume" size="8"
								styleId="domainFile.volume" />
							&nbsp;
						</td>
						<td class="cellLabel">
							<label for="domainFile.startPage">Start Page</label>
						</td>
						<td>
							<html:text property="publication.domainFile.startPage" size="8"
								styleId="domainFile.startPage" />
							&nbsp;
						</td>
						<td class="cellLabel">
							<label for="domainFile.endPage"><strong id="epageTitle" style="">End Page&nbsp; </strong></label>
						</td>
						<td>
							<html:text property="publication.domainFile.endPage" size="8"
								styleId="domainFile.endPage" />
							&nbsp;
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Authors
			</td>
			<td>
				<div id="authorSection" style="position: relative;">
					<a style="display: block" id="addAuthor"
						href="javascript:clearAuthor();openSubmissionForm('Author');">Add</a>
					<br>
					<table id="authorTable" class="summaryViewNoGrid" width="85%"
						style="display: none;">
						<tbody id="authorRows">
							<tr id="patternHeader">
								<td class="cellLabel" scope="col">
									First Name
								</td>
								<td class="cellLabel" scope="col">
									Last Name
								</td>
								<td class="cellLabel" scope="col">
									Initials
								</td>
								<td>
								</td>
							</tr>
							<tr id="pattern" style="display: none;">
								<td>
									<span id="firstNameValue">First Name</span>
								</td>
								<td>
									<span id="lastNameValue">Last Name</span>
								</td>
								<td>
									<span id="initialsValue">Initials</span>
								</td>
								<td>
									<input class="noBorderButton" id="edit" type="button"
										value="Edit" style="display: block"
										onclick="editAuthor(this.id); openSubmissionForm('Author');" />
								</td>
							</tr>
						</tbody>
					</table>
					<table id="newAuthor" style="display: none;" class="promptbox" summary="layout">
						<tbody>
							<tr>
								<html:hidden property="publication.theAuthor.id" styleId="id" />
								<td class="cellLabel">
									<label for="firstName">First Name</label>
								</td>
								<td>
									<html:text property="publication.theAuthor.firstName"
										styleId="firstName" />
								</td>
								<td class="cellLabel">
									<label for="lastName">Last Name</label>
								</td>
								<td>
									<html:text property="publication.theAuthor.lastName"
										styleId="lastName" />
								</td>
								<td class="cellLabel">
									<label for="initial">Initials</label>
								</td>
								<td>
									<html:text property="publication.theAuthor.initial"
										styleId="initial" />
								</td>
							</tr>
							<tr>
								<td>
									<input style="display: none;" class="promptButton"
										id="deleteAuthor" type="button" value="Remove"
										onclick="deleteTheAuthor()" />
								</td>
								<td>
									<div align="right">
										<input class="promptButton" type="button" value="Save"
											onclick="addAuthor();show('authorTable');closeSubmissionForm('Author');" />
										<input class="promptButton" type="button" value="Cancel"
											onclick="clearAuthor();closeSubmissionForm('Author');" />
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				<label for="keywordsStr">Keywords</label>
				<br>
				<i>(one keyword per line)</i>
			</td>
			<td>
				<html:textarea styleId="keywordsStr"
					property="publication.keywordsStr" rows="3" cols="80" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				<label for="domainFile.description">Description</label>
			</td>
			<td>
				<html:textarea styleId="domainFile.description"
					property="publication.domainFile.description" rows="8" cols="120" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				<label for="researchAreas">Research Category</label>
			</td>
			<td>
				<html:select property="publication.researchAreas" multiple="true"
					size="7" styleId="researchAreas">
					<html:options name="publicationResearchAreas" />
				</html:select>
				&nbsp;
			</td>
		</tr>
	</table>
	<div id="fileSection" style="display: block">
		<br><c:set var="theFile"
					value="${publicationForm.map.publication}" />
			<c:set var="fileBeanProperty" value="publication"/>
			<c:set var="fileJavascript" value="updateWithExistingNonPubMedDOIPublication('${publicationForm.map.publication.domainFile.uri}')" />
			<c:set var="textJavascript" value="updateWithExistingNonPubMedDOIPublication('${publicationForm.map.publication.domainFile.uri}')" />
			<c:set var="actionName" value="publication"/>
			<c:set var="fileId" value="${publicationForm.map.publication.domainFile.id}"/>
		<table width="100%" align="center" class="submissionView" summary="layout">
			<tr>
				<td class="cellLabel" width="100">File
				</td>
				<td><%@ include file="../bodySubmitFile.jsp"%>
				</td>
			</tr>
		</table>
	</div>
	<br>
	<c:choose>
		<c:when test="${empty publicationForm.map.sampleId}">
			<a name="sampleNameField" class="anchorLink"></a>
			<table width="100%" align="center" class="submissionView" summary="layout">
				<tr>
					<td class="cellLabel" width="100">
						<label for="associatedSampleNames">Sample Name</label>
					</td>
					<td>
						<table class="invisibleTable" summary="layout">
							<tr>
								<td>
									<html:textarea property="publication.sampleNamesStr" cols="60"
										rows="5" styleId="associatedSampleNames" />
									<br>
									<em>one name per line</em>
								</td>
								<td width="5">
									<a href="#sampleNameField"
										onclick="showMatchedSampleNameDropdown()"><img
											src="images/icon_browse.jpg" align="middle"
											alt="search existing samples" border="0" /></a>
								</td>
								<td>
									<table class="invisibleTable" summary="layout">
										<tr>
											<td>
												<img src="images/ajax-loader.gif" border="0" class="counts"
													id="loaderImg" style="display: none" alt="load existing samples">												
												<html:select property="publication.sampleNamesStr"
													multiple="true" size="5" styleId="matchedSampleSelect"
													style="display: none">
												</html:select>
											</td>
											<td><label for="matchedSampleSelect">
												<a href="#" onclick="updateAssociatedSamples()"
													id="selectMatchedSampleButton" style="display: none">select</a></label>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</c:when>
		<c:otherwise>
			<jsp:include
				page="/sample/bodyAnnotationCopy.jsp?annotation=publication" />
		</c:otherwise>
	</c:choose>
	<br>
	<table width="100%" align="center" class="submissionView" summary="layout"
		id="accessBlock">
		<c:set var="groupAccesses"
			value="${publicationForm.map.publication.groupAccesses}" />
		<c:set var="userAccesses"
			value="${publicationForm.map.publication.userAccesses}" />
		<c:set var="accessParent" value="publication" />
		<c:set var="dataType" value="Publication" />
		<c:set var="parentFormName" value="publicationForm" />
		<c:set var="parentAction" value="publication" />
		<c:set var="parentPage" value="2" />
		<c:set var="protectedData"
			value="${publicationForm.map.publication.domainFile.id}" />
		<c:set var="isPublic"
			value="${publicationForm.map.publication.publicStatus}" />
		<c:set var="isOwner"
			value="${publicationForm.map.publication.userIsOwner}" />
		<c:set var="ownerName"
			value="${publicationForm.map.publication.domainFile.createdBy}" />
		<c:set var="newData" value="true" />
		<c:if test="${updatePublication}">
			<c:set var="newData" value="false" />
		</c:if>
		<%@include file="../bodyManageAccessibility.jsp"%>
	</table>
	<br>
	<c:set var="updateId" value="${param.publicationId}" />
	<c:set var="hiddenDispatch" value="create" />
	<c:set var="hiddenPage" value="1" />
	<c:set var="resetSampleIdStr" value="" />
	<c:if test="${!empty publicationForm.map.sampleId}">
		<c:set var="resetSampleIdStr"
			value="&sampleId=${publicationForm.map.sampleId}" />
	</c:if>
	<c:set var="resetOnclick"
		value="javascript: location.href = 'publication.do?dispatch=setupNew&page=0${resetSampleIdStr}'" />
	<c:if test="${!empty param.publicationId }">
		<c:set var="resetOnclick"
			value="javascript: location.href = 'publication.do?dispatch=setupUpdate&page=0&publicationId=${param.publicationId}${resetSampleIdStr}'" />
	</c:if>
	<c:set var="deleteOnclick"
		value="deleteData('publication', 'publicationForm', 'publication', 'delete')" />
	<c:set var="deleteButtonName" value="Delete" />

	<c:if test="${!empty param.sampleId}">
		<c:set var="deleteButtonName" value="Remove from Sample" />
		<c:set var="deleteOnclick"
			value="deleteData('sample publication association', 'publicationForm', 'publication', 'removeFromSample')" />
		<html:hidden property="sampleId" value="${param.sampleId}" />
	</c:if>
	<c:if test="${review && !empty param.sampleId}">
		<c:set var="submitForReviewOnclick"
			value="submitReview('publicationForm', 'publication', '${publicationForm.map.publication.domainFile.id}', '${publicationForm.map.publication.domainFile.title}', 'publication')" />
	</c:if>
	<c:if test="${review && empty param.sampleId}">
		<c:set var="submitForReviewOnclick"
			value="submitReview('publicationForm', 'publication', '${publicationForm.map.publication.domainFile.id}', '${publicationForm.map.publication.domainFile.title}', 'publication', 'publicationMessage')" />
	</c:if>
	<c:set var="validate" value="false" />
	<c:if
		test="${!user.curator && publicationForm.map.publication.publicStatus}">
		<c:set var="validate" value="true" />
	</c:if>
	<c:set var="showDelete" value="false" />
	<c:if
		test="${publicationForm.map.publication.userDeletable && !empty param.publicationId}">
		<c:set var="showDelete" value="true" />
	</c:if>
	<%@include file="../bodySubmitButtons.jsp"%>
</html:form>
