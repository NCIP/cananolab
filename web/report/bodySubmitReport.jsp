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

<html:form action="/submitReport" enctype="multipart/form-data">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					${action} Report
				</h3>
			</td>
			<td align="right" width="20%">
				<jsp:include page="/webHelp/helpGlossary.jsp">
					<jsp:param name="topic" value="nano_report_help" />
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
				<tr>
					<td colspan="2">
						<jsp:include page="/bodyMessage.jsp?bundle=report" />
						<table class="topBorderOnly" cellspacing="0" cellpadding="3"
							width="100%" align="center" summary="" border="0">
							<tbody>
								<tr class="topBorder">
									<td class="formTitle" colspan="4">
										<div align="justify">
											Report Information
										</div>
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
										<span id="load" style="display:none"> <html:file
												property="file.uploadedFile" size="60" /> &nbsp;&nbsp; </span>
										<br>
										<br>
										<span id="link" style="display:none"><html:text
												property="file.externalUrl" size="60" /> </span>&nbsp;
									</td>
								</tr>
								<c:if test="${!empty submitReportForm.map.file.domainFile.uri }">
									<tr>
										<td class="completeLabel" colspan="4">
											<strong>Submitted Report</strong> &nbsp;&nbsp;
											<a
												href="searchReport.do?dispatch=download&amp;fileId=${submitReportForm.map.file.domainFile.id}&amp;location=local"
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
								
								<%-- remove report category 
								<tr>
									<td class="leftLabel">
										<strong>Report Category*</strong>
									</td>
									<td class="rightLabel"">
										<html:select property="file.domainFile.category"
											onchange="javascript:callPrompt('Report Category', 'file.domainFile.category');"
											styleId="file.domainFile.category">
											<option value=""></option>
											<html:options name="reportCategories" />
											<option value="other">
												[Other]
											</option>
										</html:select>
									</td>
								</tr>
								--%>
								
								<tr>
									<td class="leftLabel">
										<strong>Report File Title*</strong>
									</td>
									<td class="rightLabel"">
										<html:text property="file.domainFile.title" size="80" />
									</td>
								</tr>
								<c:choose>
									<c:when test="${empty param.particleId}">
										<tr>
											<td class="leftLabel" valign="top" width="20%">
												<strong>Nanoparticle Sample Name*</strong>
											</td>
											<td class="rightLabel"">
												<html:select property="file.particleNames" multiple="true"
													size="5">
													<html:options name="allUserParticleNames" />
												</html:select>
											</td>
										</tr>
									</c:when>
									<c:otherwise>
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
									</c:otherwise>
								</c:choose>
								<tr>
									<td class="leftLabel">
										<strong>Report File Description</strong>
									</td>
									<td class="rightLabel"">
										<html:textarea property="file.domainFile.description" rows="3"
											cols="60" />
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
														<input type="reset" value="Reset" onclick="javascript:window.location.reload()">
														<input type="hidden" name="dispatch" value="create">
														<input type="hidden" name="submitType" value="documents">
														<input type="hidden" name="page" value="2">
<%--														<c:if test="${!empty param.particleId}">--%>
<%--															<input type="hidden" name="particleId"--%>
<%--																value="${param.particleId}">--%>
<%--														</c:if>--%>
														<html:hidden property="file.domainFile.category"/>
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
