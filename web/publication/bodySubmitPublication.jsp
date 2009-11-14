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
	<c:when	test="${!empty publicationForm.map.publication.domainFile.id}">
		<c:set var="publicationTitle" 
		value="${fn:toUpperCase(param.location)} ${sampleName} ${fn:toUpperCase(publicationForm.map.publication.domainFile.category)}"/>
	</c:when>
	<c:otherwise>
		<c:set var="publicationTitle" 
		value="${fn:toUpperCase(param.location)} ${sampleName} Publication"/>
	</c:otherwise>
</c:choose>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle"	value="${publicationTitle}" />
	<jsp:param name="topic" value="submit_publication_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<jsp:include page="/bodyMessage.jsp?bundle=publication" />
<html:form action="/publication" enctype="multipart/form-data" onsubmit="return validateSavingTheData('newAuthor', 'Authors');">
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td class="cellLabel">
				Publication Type *
			</td>
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
		<tr>
			<td class="cellLabel">
				Research Category
			</td>
			<td colspan="5">
				<c:forEach var="data" items="${publicationResearchAreas}">
					<html:multibox property="publication.researchAreas">
												${data}
											</html:multibox>${data}
										</c:forEach>
				&nbsp;
			</td>
		</tr>
		<tr id="pubMedRow" style="display: none">
			<td class="cellLabel">
				PubMed ID
			</td>
			<td colspan="5">
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
				Digital Object ID
			</td>
			<td colspan="5">
				<html:text property="publication.domainFile.digitalObjectId"
					styleId="domainFile.digitalObjectId" size="30" />
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Title*
			</td>
			<td colspan="5">
				<html:text property="publication.domainFile.title"
					styleId="domainFile.title" size="120" />
			</td>
		</tr>
		<tr id="journalRow" style="display: none">
			<td class="cellLabel">
				Journal&nbsp;
			</td>
			<td colspan="5">
				<html:text property="publication.domainFile.journalName"
					styleId="domainFile.journalName" size="120" />
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Year of Publication
			</td>
			<td colspan="5">
				<html:text property="publication.domainFile.year" size="5"
					styleId="domainFile.year" onkeydown="return filterInteger(event)" />
			</td>
		</tr>
		<tr id="volumePageRow" style="display: none">
			<td class="cellLabel">
				Volume
			</td>
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
		<tr>
			<td class="cellLabel">
				Authors
			</td>
			<td colspan="5">
				<div id="authorSection" style="position: relative;">
					<a style="display: block" id="addAuthor"
						href="javascript:clearAuthor();openSubmissionForm('Author');">Add</a>
					<br>
					<table id="authorTable" class="summaryViewLayer4" width="85%"
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
								<td colspan="5">
									<div align="right">
										<input class="promptButton" type="button" value="Add"
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
			<td colspan="5">
				<html:textarea property="publication.keywordsStr" rows="3" cols="80" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Description
			</td>
			<td colspan="5">
				<html:textarea property="publication.domainFile.description"
					styleId="domainFile.description" rows="3" cols="120" />
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
				<td class="cellLabel">
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
							property="publication.uploadedFile" size="60" /> &nbsp;&nbsp; </span>
					<br>
					<br>
					<span id="link" style=""><html:text
							property="publication.externalUrl" size="60" /> </span>&nbsp;
				</td>
			</tr>
			<c:if
				test="${!empty publicationForm.map.publication.domainFile.uri }">
				<tr>
					<td class="completeLabel" colspan="3">
						<c:choose>
							<c:when test="${publicationForm.map.publication.image eq 'true'}">
						 				${publicationForm.map.publication.domainFile.title}<br>
								<br>
								<a href="#"
									onclick="popImage(event, 'publication.do?dispatch=download&amp;fileId=${publicationForm.map.publication.domainFile.id}&amp;location=${applicationOwner}',
														${publicationForm.map.publication.domainFile.id})"><img
										src="publication.do?dispatch=download&amp;fileId=${publicationForm.map.publication.domainFile.id}&amp;location=${applicationOwner}"
										border="0" width="150"> </a>
							</c:when>
							<c:otherwise>
											Submitted Publication &nbsp;&nbsp;
										<a
									href="publication.do?dispatch=download&amp;fileId=${publicationForm.map.publication.domainFile.id}&amp;location=${applicationOwner}"
									target="${publicationForm.map.publication.urlTarget}">
									${publicationForm.map.publication.domainFile.uri}</a>
								<br>
							</c:otherwise>
						</c:choose>
					</td>

				</tr>
			</c:if>
		</table>
	</div>
	<br>
	<c:choose>
		<c:when test="${empty publicationForm.map.sampleId}">
			<table width="100%" align="center" class="submissionView">
				<tr>
					<td class="cellLabel" width="20%">
						Sample Name
					</td>
					<td>
						<a href="#" onclick="javascript:showSampleNameDropdown();"
							id="browseSampleNames" style="display: block">Browse</a>
						<img src="images/ajax-loader.gif" border="0" class="counts"
							id="loaderImg" style="display: none">
						<html:select property="otherSamples" multiple="true"
							size="5" styleId="allSampleNameSelect" style="display: none">
							<c:if
								test="${!empty publicationForm.map.publication.sampleNames}">
								<c:forEach var="sampleName"
									items="${publicationForm.map.publication.sampleNames}">
									<option selected="selected" value="${sampleName}">
										${sampleName}
									</option>
								</c:forEach>
							</c:if>
						</html:select>
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
		<tr>
			<td class="cellLabel">
				Visibility
			</td>
			<td>
				<html:select property="publication.visibilityGroups" multiple="true"
					size="6">
					<html:options name="allVisibilityGroups" />
				</html:select>
				<br>
				<i>(${applicationOwner}_Researcher and
					${applicationOwner}_DataCurator are always selected by default.)</i>
			</td>
		</tr>
	</table>
	<br>
	<table width="100%" border="0" align="center" cellpadding="3"
		cellspacing="0" class="topBorderOnly" summary="">
		<tr>
			<td width="30%">
				<c:set var="dataId"	value="${publicationForm.map.publication.domainFile.id}" />
				<c:if test="${!empty publicationForm.map.sampleId && !empty dataId && 
							  !empty user && user.admin && user.curator}">
					<table height="32" border="0" align="left" cellpadding="4"
						cellspacing="0">
						<tr>
							<td height="32">
								<div align="left">
									<c:set var="formName" value="publicationForm" />
									<input type="button" value="Remove association"
										onclick="deleteData('sample publication association', ${formName}, 'publication')">
								</div>
							</td>
						</tr>
					</table>
				</c:if>
				<table width="498" height="32" border="0" align="right"
					cellpadding="4" cellspacing="0">
					<tr>
						<td width="490" height="32">
							<div align="right">
								<div align="right">
									<c:choose>
										<c:when test="${empty dataId}">
											<c:set var="origUrl"
												value="publication.do?dispatch=setupNew&page=0&sampleId=${publicationForm.map.sampleId}&location=${applicationOwner}" />
										</c:when>
										<c:otherwise>
											<c:set var="origUrl"
												value="publication.do?dispatch=setupUpdate&page=0&sampleId=${publicationForm.map.sampleId}&publicationId=${dataId}&location=${applicationOwner}" />
										</c:otherwise>
									</c:choose>
									<input type="reset" value="Reset"
										onclick="javascript:window.location.href='${origUrl}'">
									<input type="hidden" name="dispatch" value="create">
									<input type="hidden" name="page" value="1">
									<html:hidden property="sampleId" value="${publicationForm.map.sampleId}" />
									<html:hidden property="location" value="${publicationForm.map.location}" />
									<html:hidden property="addToSample" value="${publicationForm.map.addToSample}" />
									<html:submit />
								</div>
							</div>
						</td>
					</tr>
				</table>
				<div align="right"></div>
			</td>
		</tr>
	</table>
</html:form>
