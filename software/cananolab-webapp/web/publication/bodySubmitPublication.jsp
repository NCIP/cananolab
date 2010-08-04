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

<c:set var="action" value="Submit" scope="request" />
<c:if test="${param.dispatch eq 'setupUpdate'}">
	<c:set var="action" value="Update" scope="request" />
</c:if>
<c:choose>
	<c:when test="${!empty publicationForm.map.publication.domainFile.id}">
		<c:set var="publicationTitle"
			value="${sampleName} ${fn:toUpperCase(publicationForm.map.publication.domainFile.category)}" />
	</c:when>
	<c:otherwise>
		<c:set var="publicationTitle"
			value="${sampleName} Publication" />
	</c:otherwise>
</c:choose>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="${publicationTitle}" />
	<jsp:param name="topic" value="submit_publication_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<jsp:include page="/bodyMessage.jsp?bundle=publication" />
<html:form action="/publication" enctype="multipart/form-data"
	onsubmit="return validateSavingTheData('newAuthor', 'Authors');">
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td class="cellLabel" width="100">
				Publication Type *
			</td>
			<td>
				<table class="invisibleTable">
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
							Publication Status*
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
				PubMed ID
			</td>
			<td>
				<a
					href="http://www.ncbi.nlm.nih.gov/pubmed/${publicationForm.map.publication.domainFile.pubMedId}"
					target="_pubmed"> Click to look up PubMed Identifier</a>
				<br>
				<html:text property="publication.domainFile.pubMedId" size="50"
					styleId="domainFile.pubMedId"
					onchange="javascript:fillPubMedInfo('true')" />
				<br>
				<i> clicking outside of the text field after entering a valid
					PubMed ID enables auto-population of PubMed related fields</i>
			</td>
		</tr>
		<tr id="doiRow" style="display: none">
			<td class="cellLabel">
				Digital Object ID
			</td>
			<td>
				<html:text property="publication.domainFile.digitalObjectId"
					styleId="domainFile.digitalObjectId" size="30"
					onchange="javascript:updateWithExistingDOI('true')" />
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Title*
			</td>
			<td>
				<html:text property="publication.domainFile.title"
					styleId="domainFile.title" size="120" />
			</td>
		</tr>
		<tr id="journalRow" style="display: none">
			<td class="cellLabel">
				Journal&nbsp;
			</td>
			<td>
				<html:text property="publication.domainFile.journalName"
					styleId="domainFile.journalName" size="120" />
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Year of Publication
			</td>
			<td>
				<html:text property="publication.domainFile.year" size="5"
					styleId="domainFile.year" onkeydown="return filterInteger(event)" />
			</td>
		</tr>
		<tr id="volumePageRow" style="display: none">
			<td class="cellLabel">
				Volume
			</td>
			<td>
				<table class="invisibleTable">
					<tr>
						<td>
							<html:text property="publication.domainFile.volume" size="8"
								styleId="domainFile.volume" />
							&nbsp;
						</td>
						<td class="cellLabel">
							Start Page
						</td>
						<td>
							<html:text property="publication.domainFile.startPage" size="8"
								styleId="domainFile.startPage" />
							&nbsp;
						</td>
						<td class="cellLabel">
							<strong id="epageTitle" style="">End Page&nbsp; </strong>
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
								<td class="cellLabel">
									First Name
								</td>
								<td class="cellLabel">
									Last Name
								</td>
								<td class="cellLabel">
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
					<table id="newAuthor" style="display: none;" class="promptbox">
						<tbody>
							<tr>
								<html:hidden property="publication.theAuthor.id" styleId="id" />
								<td class="cellLabel">
									First Name
								</td>
								<td>
									<html:text property="publication.theAuthor.firstName"
										styleId="firstName" />
								</td>
								<td class="cellLabel">
									Last Name
								</td>
								<td>
									<html:text property="publication.theAuthor.lastName"
										styleId="lastName" />
								</td>
								<td class="cellLabel">
									Initials
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
				Keywords
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
				Description
			</td>
			<td>
				<html:textarea styleId="domainFile.description"
					property="publication.domainFile.description" rows="8" cols="120" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Research Category
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
		<br>
		<table width="100%" align="center" class="submissionView">
			<c:choose>
				<c:when
					test="${publicationForm.map.publication.domainFile.uriExternal eq 'true' }">
					<c:set var="linkStyle" value="display: block" />
					<c:set var="loadStyle" value="display: none" />
				</c:when>
				<c:otherwise>
					<c:set var="linkStyle" value="display: none" />
					<c:set var="loadStyle" value="display: block" />
				</c:otherwise>
			</c:choose>
			<tr>
				<td class="cellLabel" width="110">
					<html:radio styleId="external0"
						property="publication.domainFile.uriExternal" value="false"
						onclick="displayFileRadioButton()" />
					Upload File
					<br>
					&nbsp;&nbsp;or
					<br>
					<html:radio styleId="external1"
						property="publication.domainFile.uriExternal" value="true"
						onclick="displayFileRadioButton()" />
					Enter File URL
				</td>
				<td colspan="2">
					<span id="load"> <html:file
							property="publication.uploadedFile" styleId="uploadedFileField"
							size="60"
							onchange="javascript:updateWithExistingNonPubMedDOIPublication('true', '${applicationOwner}', '${publicationForm.map.publication.domainFile.uri}');" />
						&nbsp;&nbsp; </span>
					<br>
					<br>
					<span id="link" style=""><html:text
							property="publication.externalUrl" styleId="externalUrlField"
							size="60"
							onchange="javascript:updateWithExistingNonPubMedDOIPublication('true', '${applicationOwner}', '${publicationForm.map.publication.domainFile.uri}');" />
					</span>&nbsp;
				</td>
			</tr>
			<tr>
				<td colspan="3">
					<span id="existingFileInfo" style="display: none"></span>
				</td>
			</tr>
			<c:if
				test="${!empty publicationForm.map.publication.domainFile.uri }">
				<tr>
					<td colspan="3">
						<div id="existingFileInfoFromUpdate" style="display: block">
						<c:choose>
							<c:when test="${publicationForm.map.publication.image eq 'true'}">
						 				${publicationForm.map.publication.domainFile.title}<br>
								<br>
								<a href="#"
									onclick="popImage(event, 'publication.do?dispatch=download&amp;fileId=${publicationForm.map.publication.domainFile.id}',
														${publicationForm.map.publication.domainFile.id})"><img
										src="publication.do?dispatch=download&amp;fileId=${publicationForm.map.publication.domainFile.id}"
										border="0" width="150"> </a>
							</c:when>
							<c:otherwise>
											Submitted Publication &nbsp;&nbsp;
										<a
									href="publication.do?dispatch=download&amp;fileId=${publicationForm.map.publication.domainFile.id}"
									target="${publicationForm.map.publication.urlTarget}">
									${publicationForm.map.publication.domainFile.uri}</a>
								<br>
							</c:otherwise>
						</c:choose>
						</div>
					</td>
				</tr>
			</c:if>
		</table>
	</div>
	<br>
	<c:choose>
		<c:when test="${empty publicationForm.map.sampleId}">
			<a name="sampleNameField"></a>
			<table width="100%" align="center" class="submissionView">
				<tr>
					<td class="cellLabel" width="110">
						Sample Name
					</td>
					<td>
						<table class="invisibleTable">
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
											alt="search existing samples" border="0" /> </a>
								</td>
								<td>
									<table class="invisibleTable">
										<tr>
											<td>
												<img src="images/ajax-loader.gif" border="0" class="counts"
													id="loaderImg" style="display: none">
												<html:select property="publication.sampleNamesStr"
													multiple="true" size="5" styleId="matchedSampleSelect"
													style="display: none">
												</html:select>
											</td>
											<td>
												<a href="#" onclick="updateAssociatedSamples()"
													id="selectMatchedSampleButton" style="display: none">select</a>
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
	<table width="100%" align="center" class="submissionView">
	<c:set var="groupAccesses"
		value="${publicationForm.map.publication.groupAccesses}" />
	<c:set var="userAccesses"
		value="${publicationForm.map.publication.userAccesses}" />
	<c:set var="accessParent" value="publication" />
	<c:set var="dataType" value="Publication" />
	<c:set var="parentForm" value="publicationForm" />
	<c:set var="parentAction" value="publication" />
	<c:set var="parentPage" value="2"/>
	<c:set var="protectedData"  value="${publicationForm.map.publication.domainFile.id}" />
	<c:set var="newData" value="true"/>
	<c:if test="${updatePublication}">
		<c:set var="newData" value="false" />
	</c:if>
	<%@include file="../bodyManageAccessibility.jsp"%>
	</table>
	<br>
	<c:set var="updateId" value="${param.publicationId}" />
	<c:set var="hiddenDispatch" value="create" />
	<c:set var="hiddenPage" value="1" />

	<c:set var="resetOnclick"
		value="javascript: location.href = 'publication.do?dispatch=setupNew&page=0&sampleId=${publicationForm.map.sampleId}'" />
	<c:if test="${!empty param.publicationId }">
		<c:set var="resetOnclick"
			value="javascript: location.href = 'publication.do?dispatch=setupUpdate&page=0&sampleId=${publicationForm.map.sampleId}&publicationId=${param.publicationId}'" />
	</c:if>
	<c:set var="deleteOnclick"
		value="deleteData('publication', publicationForm, 'publication', 'delete')" />
	<c:set var="deleteButtonName" value="Delete" />

	<c:if test="${!empty param.sampleId}">
		<c:set var="deleteButtonName" value="Remove from Sample" />
		<c:set var="deleteOnclick"
			value="deleteData('sample publication association', publicationForm, 'publication', 'removeFromSample')" />
		<html:hidden property="sampleId" value="${param.sampleId}" />
	</c:if>
    <c:if test="${review}">
		<c:set var="submitForReviewOnclick"
			value="submitReview(publicationForm, 'publication', '${publicationForm.map.publication.domain.id}', '${publicationForm.map.publication.domain.name}', 'publication')" />
	</c:if>
	<c:set var="validate" value="false" />
	<c:if test="${!user.curator && publicationForm.map.publication.publicStatus}">
		<c:set var="validate" value="true" />
	</c:if>
	<%@include file="../bodySubmitButtons.jsp"%>
</html:form>
