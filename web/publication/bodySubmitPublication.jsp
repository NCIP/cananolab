<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type="text/javascript" src="javascript/PublicationManager.js"></script>

<c:set var="action" value="Submit" scope="request" />
<c:if test="${param.dispatch eq 'setupUpdate'}">
	<c:set var="action" value="Update" scope="request" />
</c:if>
<c:choose>
	<c:when
		test="${publicationForm.map.file.domainFile.category eq 'report'}">
		<c:set var="reportStyle" value="display:none" />
	</c:when>
	<c:otherwise>
		<c:set var="reportStyle" value="" />
	</c:otherwise>
</c:choose>
<html:form action="/publication" enctype="multipart/form-data">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					${action} Publication
				</h3>
			</td>
			<td align="right" width="20%">
				<jsp:include page="/helpGlossary.jsp">
					<jsp:param name="topic" value="submit_publication_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>
			</td>
		</tr>
		<c:choose>
			<c:when test="${empty allSampleNames && param.dispatch eq 'setup'}">
				<tr>
					<td colspan="2">
						<font color="blue" size="-1"><b>MESSAGE: </b>There are no
							samples in the database. Please make sure to <html:link
								page="/sample.do?dispatch=setupNew&page=0&location=${applicationOwner}"
								scope="page">create
							a new sample</html:link> first. </font>
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:if test="${!empty publicationForm.map.file.domainFile.id}">
					<html:hidden property="file.domainFile.id" />
				</c:if>
				<tr>
					<td colspan="2">
						<jsp:include page="/bodyMessage.jsp?bundle=publication" />
						<table width="100%" align="center" class="submissionView">
							<tr>
								<td class="cellLabel">
									Publication Type*
								</td>
								<td>
									<div id="categoryPrompt">
										<html:select property="file.domainFile.category"
											onchange="javascript:callPrompt('Publication Category', 'file.domainFile.category', 'categoryPrompt');
														setReportFields('file.domainFile.category', 'file.domainFile.status');"
											styleId="file.domainFile.category">
											<option value=""></option>
											<html:options name="publicationCategories" />
											<option value="other">
												[Other]
											</option>
										</html:select>
									</div>
								</td>
								<td class="cellLabel">
									Publication Status*
								</td>
								<td>
									<div id="statusPrompt">
										<html:select property="file.domainFile.status"
											onchange="javascript:callPrompt('Publication status', 'file.domainFile.status', 'statusPrompt');"
											styleId="file.domainFile.status">
											<option value=""></option>
											<html:options name="publicationStatuses" />
											<option value="other">
												[Other]
											</option>
										</html:select>
									</div>
								</td>
							</tr>
							<tr>
								<td class="cellLabel">
									Research Category
								</td>
								<td colspan="3">
									<c:forEach var="data" items="${publicationResearchAreas}">
										<html:multibox property="file.researchAreas">
												${data}
											</html:multibox>${data}
										</c:forEach>
									&nbsp;
								</td>
							</tr>
							<tr id="pubMedRow" style="">
								<td class="cellLabel">
									PubMed ID
								</td>
								<td colspan="3">
									<a
										href="http://www.ncbi.nlm.nih.gov/pubmed/${file.domainFile.pubMedId}"
										target="_pubmed"> Click to look up PubMed Identifier</a>
									<br>
									<html:text property="file.domainFile.pubMedId" size="50"
										styleId="pubmedId"
										onchange="javascript:addPubmed(publicationForm, '${docSampleId}'); return false;" />
									<br>
									<i> clicking outside of the text field after entering a
										valid PubMed ID enables auto-population of PubMed related
										fields</i>
								</td>
							</tr>
							<c:choose>
								<c:when
									test="${empty publicationForm.map.file.bibliographyInfo}">
									<tr id="doiRow" style="">
										<td class="cellLabel">
											Digital Object ID
										</td>
										<td colspan="3">
											<html:text property="file.domainFile.digitalObjectId"
												size="30" />
											&nbsp;
										</td>
									</tr>
									<tr>
										<td class="cellLabel">
											Title*
										</td>
										<td colspan="3">
											<html:text property="file.domainFile.title" size="80" />
										</td>
									</tr>
									<tr id="journalRow" style="">
										<td class="cellLabel">
											Journal&nbsp;
										</td>
										<td colspan="3">
											<html:text property="file.domainFile.journalName" size="80" />
											&nbsp;
										</td>
									</tr>
									<tr>
										<td class="cellLabel">
											Year of Publication
										</td>
										<td>
											<html:text property="file.domainFile.year" size="5"
												onkeydown="return filterInteger(event)" />
										</td>
										<td class="cellLabel">
											Volume
										</td>
										<td>
											<html:text property="file.domainFile.volume" size="8"
												styleId="volumeValue" style="${reportStyle }" />
											&nbsp;
										</td>
									</tr>
									<tr>
										<td class="cellLabel">
											Start Page
										</td>
										<td>
											<html:text property="file.domainFile.startPage" size="8"
												style="${reportStyle }"
												onkeydown="return filterInteger(event)" styleId="spageValue" />
											&nbsp;
										</td>
										<td class="cellLabel">
											<strong id="epageTitle" style="">End Page&nbsp;
										</td>
										<td>
											<html:text property="file.domainFile.endPage" size="8"
												style="${reportStyle }"
												onkeydown="return filterInteger(event)" styleId="epageValue" />
											&nbsp;
										</td>
									</tr>
									<tr>
										<td class="cellLabel">
											Authors
											<br>
										</td>
										<td colspan="3">
											<table class="summaryViewLayer4" width="100%">
												<tr>
													<th>
														First Name
													</th>
													<th>
														Last Name
													</th>
													<th>
														Initials
													</th>
												<tr>
													<logic:iterate name="publicationForm"
														property="file.authors" id="author" indexId="authorInd">
														<tr>
															<td>
																<html:text
																	property="file.authors[${authorInd}].firstName"
																	size="17" />
															</td>
															<td>
																<html:text
																	property="file.authors[${authorInd}].lastName"
																	size="17" />
															</td>
															<td>
																<html:text property="file.authors[${authorInd}].initial"
																	size="17" />
															</td>
														</tr>
													</logic:iterate>
												</tr>
											</table>
											&nbsp;
											<i>To remove author, please clear first name, last name
												and initials fields</i>
										</td>
										<td>
											<a href="#"
												onclick="javascript:addComponent(publicationForm, 'publication', 'addAuthor'); return false;">
												<img align="top" src="images/btn_add.gif" border="0" /></a>
										</td>
									</tr>
								</c:when>
								<c:otherwise>
									<tr id="doiRow">
										<td class="cellLabel" valign="top">
											Digital Object ID
										</td>
										<td colspan="3">
											${publicationForm.map.file.domainFile.digitalObjectId }&nbsp;
											<%--										<html:text property="file.domainFile.digitalObjectId"--%>
											<%--											size="30" />--%>
										</td>
									</tr>
									<tr>
										<td class="cellLabel">
											Title*
										</td>
										<td colspan="3">
											${publicationForm.map.file.domainFile.title }&nbsp;
										</td>
									</tr>
									<tr>
										<td class="cellLabel">
											Journal
										</td>
										<td colspan="3">
											${publicationForm.map.file.bibliographyInfo}&nbsp;
										</td>
									</tr>
									<tr>
										<td class="cellLabel">
											Authors
											<br>
										</td>
										<td colspan="3">
											<c:if test="${!empty publicationForm.map.file.authors}">
												<c:forEach var="author"
													items="${publicationForm.map.file.authors}">
									${author.lastName}, ${author.firstName} ${author.initial}<br>
												</c:forEach>
											</c:if>
											&nbsp;
										</td>
									</tr>
								</c:otherwise>
							</c:choose>
							<tr>
								<td class="cellLabel">
									Keywords
									<br>
									<i>(one keyword per line)</i>
								</td>
								<td colspan="7">
									<html:textarea property="file.keywordsStr" rows="3" cols="70" />
								</td>
							</tr>
							<tr>
								<td class="cellLabel">
									Description
								</td>
								<td colspan="7">
									<html:textarea property="file.domainFile.description" rows="3"
										cols="70" />
								</td>
							</tr>
						</table>

						<br>
						<table width="100%" align="center" class="submissionView">
							<c:choose>
								<c:when
									test="${publicationForm.map.file.domainFile.uriExternal eq 'true' }">
									<c:set var="linkDisplay" value="display: inline" />
									<c:set var="loadDisplay" value="display: none" />
								</c:when>
								<c:otherwise>
									<c:set var="linkDisplay" value="display: none" />
									<c:set var="loadDisplay" value="display: inline" />
								</c:otherwise>
							</c:choose>
							<tr>
								<td class="cellLabel">
									<html:radio styleId="external0"
										property="file.domainFile.uriExternal" value="false"
										onclick="displayFileRadioButton()" />
									Upload File
									<br>
									&nbsp;&nbsp;or
									<br>
									<html:radio styleId="external1"
										property="file.domainFile.uriExternal" value="true"
										onclick="displayFileRadioButton()" />
									Enter File URL
								</td>
								<td colspan="2">
									<span id="load"> <html:file property="file.uploadedFile"
											size="60" /> &nbsp;&nbsp; </span>
									<br>
									<br>
									<span id="link" style=""><html:text
											property="file.externalUrl" size="60" /> </span>&nbsp;
								</td>
							</tr>
							<c:if test="${!empty publicationForm.map.file.domainFile.uri }">
								<tr>
									<td class="completeLabel" colspan="3">
										<c:choose>
											<c:when test="${publicationForm.map.file.image eq 'true'}">
						 				${publicationForm.map.file.domainFile.title}<br>
												<br>
												<a href="#"
													onclick="popImage(event, 'compositionFile.do?dispatch=download&amp;fileId=${publicationForm.map.file.domainFile.id}&amp;location=${applicationOwner}',
														${publicationForm.map.file.domainFile.id}, 100, 100)"><img
														src="compositionFile.do?dispatch=download&amp;fileId=${publicationForm.map.file.domainFile.id}&amp;location=${applicationOwner}"
														border="0" width="150"> </a>
											</c:when>
											<c:otherwise>
											Submitted Publication &nbsp;&nbsp;
										<a
													href="publication.do?dispatch=download&amp;fileId=${publicationForm.map.file.domainFile.id}&amp;location=${applicationOwner}"
													target="${publicationForm.map.file.urlTarget}">
													${publicationForm.map.file.domainFile.uri}</a>


												<br>
											</c:otherwise>
										</c:choose>
									</td>

								</tr>
							</c:if>
						</table>
						<br>
						<c:choose>
							<c:when test="${empty docSampleId}">
								<table width="100%" align="center" class="submissionView">
									<tr>
										<td class="cellLabel" width="20%">
											Sample Name
										</td>
										<td>
											<html:select property="file.sampleNames" multiple="true"
												size="5">
												<html:options name="allSampleNames" />
											</html:select>
										</td>
									</tr>
								</table>
							</c:when>
							<c:otherwise>
								<table width="100%" align="center" class="submissionView">
									<tr>
										<th colspan="8">
											Copy
										</th>
									</tr>
									<c:choose>
										<c:when test="${!empty otherSampleNames}">
											<tr>
												<input type="hidden" name="file.sampleNames"
													value="${sampleName}">
												<td class="cellLabel" valign="top" width="20%">
													Copy to other ${samplePointOfContact} nanoparticle
												</td>
												<td class="rightLabel">
													<html:select property="file.sampleNames" multiple="true"
														size="5">
														<html:options collection="otherSampleNames"
															property="name" labelProperty="name" />
													</html:select>
												</td>
											</tr>
										</c:when>
										<c:otherwise>
											<tr>
												<td class="completeLabel" colspan="2">
													There are no other samples from source
													${samplePointOfContact} to copy annotation to.
												</td>
											</tr>
										</c:otherwise>
									</c:choose>
								</table>
							</c:otherwise>
						</c:choose>
						<br>
						<table width="100%" align="center" class="submissionView">
							<tr>
								<td class="cellLabel">
									Visibility
								</td>
								<td>
									<html:select property="file.visibilityGroups" multiple="true"
										size="6">
										<html:options name="allVisibilityGroups" />
									</html:select>
									<br>
									<i>(${applicationOwner}_Researcher and
										${applicationOwner}_DataCurator are always selected by
										default.)</i>
								</td>
							</tr>
						</table>

						<table width="100%" border="0" align="center" cellpadding="3"
							cellspacing="0" class="topBorderOnly" summary="">
							<tr>
								<td width="30%">
									<span class="formMessage"> </span>
									<br>
									<table width="498" height="32" border="0" align="right"
										cellpadding="4" cellspacing="0">
										<tr>
											<td width="490" height="32">
												<div align="right">
													<div align="right">
														<c:set var="dataId"
															value="${publicationForm.map.file.domainFile.id}" />
														<c:set var="origUrl"
															value="publication.do?page=0&sampleId=${docSampleId }&dispatch=setupNew&location=${applicationOwner }" />
														<c:if test="${!empty dataId}">
															<c:set var="origUrl"
																value="publication.do?page=0&sampleId=${docSampleId }&dispatch=setupUpdate&location=${applicationOwner}&fileId=${dataId }" />
														</c:if>
														<input type="reset" value="Reset"
															onclick="javascript:window.location.href='${origUrl}'">
														<input type="hidden" name="dispatch" value="create">
														<input type="hidden" name="submitType"
															value="publications">
														<input type="hidden" name="page" value="2">
														<input type="hidden" name="location" value="${applicationOwner}">
														<input type="hidden" name="sampleId"
															value="${docSampleId}">
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
					</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</table>
</html:form>
