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
<c:if test="${!empty param.particleId}">
	<c:set var="particleId" value="${param.particleId}" scope="request"/>
</c:if>
<html:form action="/submitPublication" enctype="multipart/form-data">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					${action} Publication
				</h3>
			</td>
			<td align="right" width="20%">
				<jsp:include page="/webHelp/helpGlossary.jsp">
					<jsp:param name="topic" value="submit_publication_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>					
			</td>
		</tr>
		<c:choose>
			<c:when
				test="${empty allUserParticleNames && param.dispatch eq 'setup'}">
				<tr>
					<td colspan="2">
						<font color="blue" size="-1"><b>MESSAGE: </b>There are no
							nanoparticle samples in the database. Please make sure to 
							<html:link page="/submitNanoparticleSample.do?dispatch=setup&page=0&location=${location}" scope="page" >create
							a new nanoparticle sample</html:link> first. </font>
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:if test="${!empty submitPublicationForm.map.file.domainFile.id}">
					<html:hidden property="file.domainFile.id" />
				</c:if>
				<tr>
					<td colspan="2">
						<jsp:include page="/bodyMessage.jsp?bundle=document" />
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
										<html:select property="file.domainFile.category"
											onchange="javascript:callPrompt('Publication Category', 'file.domainFile.category');"
											styleId="file.domainFile.category">
											<option value=""></option>
											<html:options name="publicationCategories" />
											<option value="other">
												[Other]
											</option>
										</html:select>
									</td>								
									<td class="label">
										<strong>Publication Status*</strong>
									</td>
									<td class="rightLabel">
										<html:select property="file.domainFile.status"
											onchange="javascript:callPrompt('Publication status', 'file.domainFile.status');"
											styleId="file.domainFile.status">
											<option value=""></option>
											<html:options name="publicationStatuses" />
											<option value="other">
												[Other]
											</option>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Research Category*</strong><br>
									</td>
									<td class="rightLabel" colspan="3">
										<c:forEach var="data" items="${publicationResearchAreas}">
											<html:multibox property="file.researchAreas">
												${data}
											</html:multibox>${data}
										</c:forEach>&nbsp;
									</td>	
								</tr>
								<tr>
									<td class="leftLabel" valign="top">
										<strong>PubMed ID</strong>
									</td>
									<td class="rightLabel" colspan="3">	
										<a
												href="http://www.ncbi.nlm.nih.gov/pubmed/${file.domainFile.pubMedId}"
												target="_pubmed">
												Click to look up PubMed Identifier</a>
										<br>									
										<html:text property="file.domainFile.pubMedId" size="30" styleId="pubmedId"
											onkeydown="return filterInteger(event)"/>
										<a href="#"
												onclick="javascript:addPubmed(submitPublicationForm, '${param.particleId}'); return false;">
												<span class="addLink2">Auto Populate PubMed Fields</span> </a>
									</td>
								</tr>
								<tr>
									<td class="leftLabel" valign="top">
										<strong>Digital Object ID</strong>
									</td>
									<td class="rightLabel" colspan="3">
										<html:text property="file.domainFile.digitalObjectId" size="30" />
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Title*
										</strong>
									</td>
									<td class="rightLabel" colspan="3">
										<html:text property="file.domainFile.title" size="80" />
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Journal
										</strong>
									</td>
									<td class="rightLabel" colspan="3">
										<html:text property="file.domainFile.journalName" size="80" />
									</td>
								</tr>
								<tr>
									<td class="leftLabel" valign="top">
										<strong>Authors</strong><br>
									</td>
									<td class="label" colspan="2" valign="top">
										<table class="smalltable" border="0">
<%--											<logic:notEmpty name="submitPublicationForm"
														property="file.authors" > --%>
											<tr class="smallTableHeader"><th>First Name</th><th>Last Name</th><th>Initials</th>
											
												<tr>
													<logic:iterate name="submitPublicationForm"
														property="file.authors" id="author" indexId="authorInd">
														<tr>
														<td><html:text property="file.authors[${authorInd}].firstName" size="17"/></td>
														<td><html:text property="file.authors[${authorInd}].lastName" size="17"/></td>
														<td><html:text property="file.authors[${authorInd}].middleInitial" size="17"/></td>
														</tr>													
													</logic:iterate>
												</tr>
<%--											</logic:notEmpty> --%>
										</table>&nbsp;																			
									</td>
									<td class="rightLabel" colspan="2" valign="top">
										<a href="#"
												onclick="javascript:addComponent(submitPublicationForm, 'submitPublication', 'addAuthor'); return false;">
												<span class="addLink2">Add Author</span> </a>
<%--										<html:button property="file.domainFile.pubMedId" value="add authors"/>--%>
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Year of Publication
										</strong>
									</td>
									<td class="label">
										<html:text property="file.domainFile.year" size="17" 
											onkeydown="return filterInteger(event)"/>
									</td>
									<td class="label">
										<strong>Volume
										</strong>
									</td>
									<td class="rightLabel">
										<html:text property="file.domainFile.volume" size="17"/>
									</td>
								</tr>								
								<tr>
									<td class="leftLabel">
										<strong>Start Page
										</strong>
									</td>
									<td class="label">
										<html:text property="file.domainFile.startPage" size="17" 
											onkeydown="return filterInteger(event)"/>
									</td>
									<td class="label">
										<strong>End Page
										</strong>
									</td>
									<td class="rightLabel">
										<html:text property="file.domainFile.endPage" size="17" 
											onkeydown="return filterInteger(event)"/>
									</td>
								</tr>
								<tr>
									<td class="leftLabel" valign="top">
										<strong>Keywords<br><em>(one keyword per line)</em></strong>
									</td>
									<td class="rightLabel" colspan="3">
										<html:textarea property="file.keywordsStr"
											rows="3" cols="60"/>
									</td>
								</tr>
								<tr>
									<td class="leftLabel" valign="top">
										<strong>Description</strong>
									</td>
									<td class="rightLabel" colspan="3">
										<html:textarea property="file.domainFile.description" rows="3"
											cols="60" />
									</td>
								</tr>			
							</tbody>
						</table>

						<br>
						
						
						<c:choose>
							<c:when test="${empty param.particleId}">
								<table class="topBorderOnly" cellspacing="0" cellpadding="3"
									width="100%" align="center" summary="" border="0">		
									<tbody>
										<tr class="topBorder">
											<td class="formTitle" colspan="4">
												<div align="justify">
													&nbsp;
												</div>
											</td>
										</tr>																
										<tr>
											<td class="leftLabel" valign="top" width="20%">
												<strong>Nanoparticle Sample Name*</strong>
											</td>
											<td class="rightLabel">
												<html:select property="file.particleNames" multiple="true"
													size="5">
													<html:options name="allUserParticleNames" />
												</html:select>
											</td>
										</tr>
									</tbody>
								</table>
							</c:when>
							<c:otherwise>
								<table class="topBorderOnly" cellspacing="0" cellpadding="3"
									width="100%" align="center" summary="" border="0">		
									<tbody>
										<tr class="topBorder">
											<td class="formTitle" colspan="4">
												<div align="justify">
													Copy
												</div>
											</td>
										</tr>																
										<tr>
											<input type="hidden" name="file.particleNames" value="${particleName}">
											<td class="leftLabel" valign="top" width="20%">
												<strong>Copy to other ${particleSource} nanoparticle</strong>
											</td>
											<td class="rightLabel">										
												<html:select property="file.particleNames" multiple="true"
													size="5">
													<html:options collection="otherParticleNames" property="name"
														labelProperty="name" />
												</html:select>
											</td>
										</tr>
									</tbody>
								</table>
							</c:otherwise>								
						</c:choose>
						<br>
						<table class="topBorderOnly" cellspacing="0" cellpadding="3"
							width="100%" align="center" summary="" border="0">		
							<tbody>
								<tr class="topBorder">
									<td class="formTitle" colspan="4">
										<div align="justify">
											Visibilities
										</div>
									</td>
								</tr>		
								
								<tr>
									<td class="leftLabel">
										<strong>Visibility</strong>
									</td>
									<td class="rightLabel">
										<html:select property="file.visibilityGroups" multiple="true"
											size="6">
											<html:options name="allVisibilityGroups" />
										</html:select>
										<br>
										<i>(${applicationOwner}_Researcher and
											${applicationOwner}_DataCurator are always selected by default.)</i>
									</td>
								</tr>
							</tbody>
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
												<c:set var="dataId" value="${submitPublicationForm.map.file.domainFile.id}" />
												<c:set var="origUrl" value="submitPublication.do?page=0&particleId=${particleId }&dispatch=setup&location=${location}" />
												<c:if test="${!empty dataId}">
													<c:set var="origUrl" value="submitPublication.do?page=0&particleId=${particleId }&dispatch=setupUpdate&location=${location}&fileId=${dataId }" />
												</c:if>
												<input type="reset" value="Reset"
													onclick="javascript:window.location.href='${origUrl}'">
														<input type="hidden" name="dispatch" value="create">
														<input type="hidden" name="submitType" value="documents">
														<input type="hidden" name="page" value="2">
														<input type="hidden" name="location" value="local">
														<input type="hidden" name="particleId" value="${particleId}">
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
