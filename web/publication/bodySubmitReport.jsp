<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>

<c:set var="action" value="Submit" scope="request" />
<c:if test="${param.dispatch eq 'setupUpdate'}">
	<c:set var="action" value="Update" scope="request" />
</c:if>

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
					<jsp:param name="topic" value="nano_report_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>
			</td>
		</tr>
		<c:choose>
			<c:when test="${empty allParticleNames && param.dispatch eq 'setup'}">
				<tr>
					<td colspan="2">
						<font color="blue" size="-1"><b>MESSAGE: </b>There are no
							samples in the database. Please make sure to <html:link
								page="/sample.do?dispatch=setupNew&page=0&location=${location}"
								scope="page">create
							a new sample</html:link> first. </font>
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<tr>
					<td colspan="2">
						<jsp:include page="/bodyMessage.jsp?bundle=report" />
						<table class="topBorderOnly" cellspacing="0" cellpadding="3"
							width="100%" align="center" summary="" border="0">
							<tbody>
								<tr class="topBorder">
									<td class="formTitle" colspan="4">
										<div align="justify">
											Publication Information
										</div>
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Publication Type*</strong>
									</td>
									<td class="label">
										<div id="categoryPrompt">
											<html:select property="file.domainFile.category"
												onchange="javascript:callPrompt('Publication Category', 'file.domainFile.category', 'categoryPrompt');"
												styleId="file.domainFile.category">
												<option value=""></option>
												<html:options name="publicationCategories" />
												<%--											<option value="report" selected>report</option>--%>
												<option value="other">
													[Other]
												</option>
											</html:select>
										</div>
									</td>
									<td class="label">
										<strong>Publication Status*</strong>
									</td>
									<td class="rightLabel">
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
									<td class="leftLabel">
										<strong>Research Category</strong>
										<br>
									</td>
									<td class="rightLabel" colspan="3">
										<c:forEach var="data" items="${publicationResearchAreas}">
											<html:multibox property="file.researchAreas">
												${data}
											</html:multibox>${data}
										</c:forEach>
										&nbsp;
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<html:radio styleId="external0"
											property="file.domainFile.uriExternal" value="false"
											onclick="radLinkOrUpload()" />
										<strong>Upload Report File</strong>
										<br>
										&nbsp;&nbsp;or
										<br>
										<html:radio styleId="external1"
											property="file.domainFile.uriExternal" value="true"
											onclick="radLinkOrUpload()" />
										<strong>Enter Report URL</strong>
									</td>
									<td class="rightLabel" colspan="3">
										<span id="load" style="display: none"> <html:file
												property="file.uploadedFile" size="60" /> &nbsp;&nbsp; </span>
										<br>
										<br>
										<span id="link" style="display: none"><html:text
												property="file.externalUrl" size="60" /> </span>&nbsp;
									</td>
								</tr>
								<c:if test="${!empty submitReportForm.map.file.domainFile.uri }">
									<tr>
										<td class="completeLabel" colspan="4">
											<strong>Submitted Publication</strong> &nbsp;&nbsp;
											<a
												href="searchReport.do?dispatch=download&amp;fileId=${submitReportForm.map.file.domainFile.id}&amp;location=${applicationOwner}"
												target="${submitReportForm.map.file.urlTarget}">
												${submitReportForm.map.file.domainFile.uri}</a>
											<html:hidden property="file.domainFile.uri" />
											<html:hidden property="file.domainFile.name" />
										</td>
									</tr>
								</c:if>
								<c:if test="${!empty submitReportForm.map.file.domainFile.id}">
									<html:hidden property="file.domainFile.id" />
								</c:if>

								<tr>
									<td class="leftLabel">
										<strong>Title*</strong>
									</td>
									<td class="rightLabel" colspan="3">
										<html:text property="file.domainFile.title" size="80" />
									</td>
								</tr>
								<tr>
									<td class="leftLabel" valign="top">
										<strong>Authors</strong>
										<br>
									</td>
									<td class="label" colspan="2" valign="top">
										<table class="smalltable" border="0">
											<%--											<logic:notEmpty name="publicationForm"
														property="file.authors" > --%>
											<tr class="smallTableHeader">
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
															<html:text property="file.authors[${authorInd}].lastName"
																size="17" />
														</td>
														<td>
															<html:text property="file.authors[${authorInd}].initial"
																size="17" />
														</td>
													</tr>
												</logic:iterate>
											</tr>
											<%--											</logic:notEmpty> --%>
										</table>
										&nbsp;
										<i>To remove author, please clear first name, last name
											and initials fields</i>
									</td>
									<td class="rightLabel" colspan="2" valign="top">
										<a href="#"
											onclick="javascript:addComponent(publicationForm, 'publication', 'addAuthor'); return false;">
											<span class="addLink2">Add Author</span> </a>
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Year of Publication </strong>
									</td>
									<td class="rightLabel" colspan="3">
										<html:text property="file.domainFile.year" size="17"
											onkeydown="return filterInteger(event)" />
									</td>
								</tr>
								<c:choose>
									<c:when test="${empty param.sampleId}">
										<tr>
											<td class="leftLabel" valign="top" width="20%">
												<strong>Sample Name*</strong>
											</td>
											<td class="rightLabel" colspan="3">
												<html:select property="file.particleNames" multiple="true"
													size="5">
													<html:options name="allParticleNames" />
												</html:select>
											</td>
										</tr>
									</c:when>
									<c:otherwise>
										<tr>
											<input type="hidden" name="file.sampleNames"
												value="${sampleName}">
											<td class="leftLabel" valign="top" width="20%">
												<strong>Copy to other ${samplePointOfContact}
													canano</strong>
											</td>
											<td class="rightLabel" colspan="3">
												<html:select property="file.sampleNames" multiple="true"
													size="5">
													<html:options collection="otherParticleNames"
														property="name" labelProperty="name" />
												</html:select>
											</td>
										</tr>
									</c:otherwise>
								</c:choose>
								<tr>
									<td class="leftLabel" valign="top">
										<strong>Keywords<br> </strong><i>(one keyword per
											line)</i>
									</td>
									<td class="rightLabel" colspan="3">
										<html:textarea property="file.keywordsStr" rows="3" cols="60" />
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Description</strong>
									</td>
									<td class="rightLabel" colspan="3">
										<html:textarea property="file.domainFile.description" rows="3"
											cols="60" />
									</td>
								</tr>

								<tr>
									<td class="leftLabel">
										<strong>Visibility</strong>
									</td>
									<td class="rightLabel" colspan="3">
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
							</tbody>
						</table>
						<br>
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
														<input type="reset" value="Reset"
															onclick="javascript:location.href='submitReport.do?dispatch=setup&page=0'">
														<input type="hidden" name="dispatch" value="create">
														<input type="hidden" name="submitType"
															value="publications">
														<input type="hidden" name="page" value="2">
														<%--														<c:if test="${!empty param.sampleId}">--%>
														<%--															<input type="hidden" name="sampleId"--%>
														<%--																value="${param.sampleId}">--%>
														<%--														</c:if>--%>
														<html:hidden property="file.domainFile.category" />
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
