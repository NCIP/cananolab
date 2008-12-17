<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type="text/javascript" src="javascript/POCManager.js"></script>

<html:form action="/submitPointOfContact">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					Point of Contact
				</h3>
			</td>
			<td align="right" width="20%">
				<jsp:include page="/helpGlossary.jsp">
					<jsp:param name="topic" value="submit_publication_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>
				<c:set var="cancelUrl"
					value="submitPointOfContact.do?page=0&particleId=${particleId }&dispatch=setup&location=local" />
				<c:if test="${!empty pocId}">
					<c:set var="cancelUrl"
						value="submitPointOfContact.do?page=0&particleId=${particleId }&dispatch=setupUpdate&location=local&pocId=${pocId}" />
				</c:if>
				<c:url var="cancelUrl" value="submitPointOfContact.do">
					<c:param name="page" value="0" />
					<c:param name="dispatch" value="cancel" />					
				</c:url>
				<a href="${cancelUrl}" class="helpText">Back</a>
			</td>			
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=pointOfContact" />

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
													onclick="javascript:addComponent(submitPointOfContactForm, 'submitPointOfContact', 'addPointOfContact'); return false;">
													<span class="addLink">Add Secondary<br>Point of Contact</span> </a>
											</td>
											<td id="compEleTd">
												<jsp:include page="/common/bodyPointOfContact.jsp" >
													<jsp:param name="pocTitle" value="Primary Point of Contact" />
													<jsp:param name="pocBean" value="poc" />
												</jsp:include>
												<br>
												<c:if test="${!empty submitPointOfContactForm.map.otherPoc &&
					 								!empty submitPointOfContactForm.map.otherPoc.otherPointOfContacts}">	
												<logic:iterate name="submitPointOfContactForm"
													property="otherPoc.otherPointOfContacts" id="pocs" indexId="orgaIndex">

													<jsp:include page="/common/bodyPointOfContact.jsp" >
														<jsp:param name="pocIndex" value="${orgaIndex}" />
														<jsp:param name="pocTitle" value="Secondary Point of Contact #${orgaIndex + 1}" />
														<jsp:param name="pocBean" value="otherPoc.otherPointOfContacts[${orgaIndex}]" />
													</jsp:include>
													<br>
												</logic:iterate>
												</c:if>
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
													value="submitPointOfContact.do?page=0&particleId=${particleId }&dispatch=setup&location=local" />
												<c:if test="${!empty pocId}">
													<c:set var="origUrl"
														value="submitPointOfContact.do?page=0&particleId=${particleId }&dispatch=setupUpdate&location=local&pocId=${pocId}" />
												</c:if>
												<input type="button" value="Cancel"
													onclick="javascript:window.location.href='${cancelUrl}'">
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
