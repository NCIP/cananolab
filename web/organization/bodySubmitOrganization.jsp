<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type="text/javascript" src="javascript/OrganizationManager.js"></script>

<html:form action="/submitOrganization">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					${action} Contact Organization
				</h3>
			</td>
			<td align="right" width="20%">
				<jsp:include page="/helpGlossary.jsp">
					<jsp:param name="topic" value="submit_publication_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=organization" />

				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0" id="pubTable">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify">
									Organization and Point of Contact
								</div>
							</td>
						</tr>

						<tr>
							<td class="completeLabel" colspan="4">
								<table border="0" width="100%">
									<tbody>
										<tr>
											<td valign="bottom">
												<a href="#"
													onclick="javascript:addComponent(submitOrganizationForm, 'submitOrganization', 'addOrganization'); return false;">
													<span class="addLink">Add Organization</span> </a>
											</td>
											<td id="compEleTd">
												<jsp:include page="/organization/bodyOrganization.jsp" >
													<jsp:param name="orgTitle" value="Primary Organization" />
													<jsp:param name="orgBean" value="orga" />
												</jsp:include>
												<br>
												<logic:iterate name="submitOrganizationForm"
													property="otherOrga.ortherOrganizations" id="organizations" indexId="orgaIndex">

													<jsp:include page="/organization/bodyOrganization.jsp" >
														<jsp:param name="orgIndex" value="${orgaIndex}" />
														<jsp:param name="orgTitle" value="Organization #${orgaIndex + 2}" />
														<jsp:param name="orgBean" value="otherOrga.ortherOrganizations[${orgaIndex}]" />
													</jsp:include>
												</logic:iterate>
											</td>
										</tr>
									</tbody>
								</table>
							</td>
						</tr>
					</tbody>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="2">
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
													value="${submitOrganizationForm.map.orga.domain.id}" />
												<c:set var="origUrl"
													value="submitOrganization.do?page=0&particleId=${docParticleId }&dispatch=setup&location=local" />
												<c:if test="${!empty dataId}">
													<c:set var="origUrl"
														value="submitOrganization.do?page=0&particleId=${docParticleId }&dispatch=setupUpdate&location=local&fileId=${dataId }" />
												</c:if>
												<input type="reset" value="Reset"
													onclick="javascript:window.location.href='${origUrl}'">
												<input type="hidden" name="dispatch" value="create">
												<input type="hidden" name="submitType" value="organization">
												<input type="hidden" name="page" value="2">
												<input type="hidden" name="location" value="local">
												<html:submit />
											</div>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</html:form>
