<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html:form action="/submitPointOfContact">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					<br>
					Point Of Contact
				</h4>
			</td>
			<td align="right" width="35%">
				<jsp:include page="/helpGlossary.jsp">
					<jsp:param name="topic" value="organization_page_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>

				<c:choose>
					<c:when test="${!empty user && user.curator}">
						<c:url var="url" value="submitPointOfContact.do">
							<c:param name="page" value="0" />
							<c:if test="${!empty param.sampleId}">
								<c:param name="sampleId" value="${param.sampleId}" />
							</c:if>
							<c:param name="dispatch" value="cancel" />
						</c:url>
					</c:when>
					<c:otherwise>
						<c:url var="url" value="sample.do">
							<c:param name="page" value="0" />
							<c:param name="sampleId" value="${param.sampleId}" />
							<c:param name="dispatch" value="setupView" />
							<c:param name="location" value="${param.location}" />
						</c:url>
					</c:otherwise>
				</c:choose>
				<a href="${url}" class="helpText">Back</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=particle" />
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" class="topBorderOnly" summary="">
					<tr>
						<td class="formTitle" colspan="2">
							<table width="100%">
								<tr>
									<td class="formTitle" width="100%">
										<c:choose>
											<c:when test="${!empty docSampleId }">
											${fn:toUpperCase(param.location)} ${sampleName}
										</c:when>
											<c:otherwise>
											${fn:toUpperCase(param.location)}
										</c:otherwise>
										</c:choose>
									</td>
									<td align="right" class="formTitle">

										<c:url var="url" value="submitPointOfContact.do">
											<c:param name="page" value="0" />
											<c:param name="dispatch" value="setupUpdate" />
											<c:param name="sampleId" value="${param.sampleId}" />
											<c:param name="pocId" value="${submitPointOfContactForm.map.poc.domain.id}" />
											<c:param name="location" value="${param.location}" />
										</c:url>

										<c:if
											test="${!empty user && user.curator && param.location eq 'local'}">
											<td>
												<a href="${url}"><img src="images/icon_edit_23x.gif"
														alt="edit organization" title="edit point of contact"
														border="0"> </a>
											</td>
										</c:if>
									</td>
									<!--
									<td>
										<a href="javascript:printPage('${printDetailViewLinkURL}')"><img
												src="images/icon_print_23x.gif"
												alt="print organization detail"
												title="print contact organization detail" border="0">
										</a>
									</td>
									<td>
										<c:url var="exportUrl" value="submitPointOfContact.do">
											<c:param name="page" value="0" />
											<c:param name="dispatch" value="exportDetail" />
											<c:param name="submitType" value="${submitType}" />
											<c:param name="location" value="${param.location}" />
										</c:url>
										<a href="${exportUrl}"><img
												src="images/icon_excel_23x.gif"
												alt="export organization detail"
												title="export organization detail" border="0"> </a>
									</td>
									 -->
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<th class="leftLabel" valign="top">
							Primary Point Of Contact
						</th>
						<td class="rightLabel">
							<table class="smalltable" border="0" width="100%">
								<c:if
									test="${!empty submitPointOfContactForm.map.poc.domain.firstName ||
									!empty submitPointOfContactForm.map.poc.domain.middleInitial
									||!empty submitPointOfContactForm.map.poc.domain.lastName}">
									<tr class="smallTableHeader">
										<td width="4">
											<strong>Name</strong>
										</td>
										<td>
											${submitPointOfContactForm.map.poc.domain.firstName}&nbsp;
											<c:if
												test="${!empty submitPointOfContactForm.map.poc.domain.middleInitial}">
												${submitPointOfContactForm.map.poc.domain.middleInitial}&nbsp;
											</c:if>
											${submitPointOfContactForm.map.poc.domain.lastName}&nbsp;
										</td>
									</tr>
								</c:if>
								<c:if
									test="${!empty submitPointOfContactForm.map.poc.domain.email}">
									<tr class="smallTableHeader">
										<td>
											<strong>Email</strong>
										</td>
										<td colspan="3">
											${submitPointOfContactForm.map.poc.domain.email}&nbsp;
										</td>
									</tr>
								</c:if>
								<c:if
									test="${!empty submitPointOfContactForm.map.poc.domain.phone}">
									<tr class="smallTableHeader">
										<td>
											<strong>Phone</strong>
										</td>
										<td colspan="3">
											${submitPointOfContactForm.map.poc.domain.phone}&nbsp;
										</td>
									</tr>
								</c:if>
								<tr class="smallTableHeader">
									<td>
										<strong>Organization</strong>
									</td>
									<td colspan="3">
										${submitPointOfContactForm.map.poc.domain.organization.name}&nbsp;
									</td>
								</tr>
								<c:if
									test="${!empty submitPointOfContactForm.map.poc.domain.organization.name &&
									!empty submitPointOfContactForm.map.poc.domain.organization.streetAddress1}">
									<tr class="smallTableHeader">
										<td>
											<strong>&nbsp;</strong>
										</td>
										<td colspan="3">
											${submitPointOfContactForm.map.poc.domain.organization.streetAddress1}&nbsp;
											<br>
											${submitPointOfContactForm.map.poc.domain.organization.streetAddress2}&nbsp;
											<br>
											${submitPointOfContactForm.map.poc.domain.organization.city}&nbsp;
											${submitPointOfContactForm.map.poc.domain.organization.state}&nbsp;
											${submitPointOfContactForm.map.poc.domain.organization.postalCode}&nbsp;
											${submitPointOfContactForm.map.poc.domain.organization.country}&nbsp;
										</td>
									</tr>
								</c:if>
								<c:if
									test="${!empty submitPointOfContactForm.map.poc.domain.role}">
									<tr class="smallTableHeader">
										<td>
											<strong>Role</strong>
										</td>
										<td>
											${submitPointOfContactForm.map.poc.domain.role}&nbsp;
										</td>
									</tr>
								</c:if>
							</table>
						</td>
					</tr>

					<c:if
						test="${!empty submitPointOfContactForm.map.otherPoc &&
					 !empty submitPointOfContactForm.map.otherPoc.otherPointOfContacts}">
						<logic:iterate name="submitPointOfContactForm"
							property="otherPoc.otherPointOfContacts" id="otherPOC"
							indexId="pocInd">
							<tr>
								<th class="leftLabel" valign="top">
									Secondary Point of Contact #${pocInd+1}
								</th>
								<td class="rightLabel">
									<table class="smalltable" border="0" width="100%">
									<c:if
										test="${!empty submitPointOfContactForm.map.otherPoc.otherPointOfContacts[pocInd].domain.firstName ||
										!empty submitPointOfContactForm.map.otherPoc.otherPointOfContacts[pocInd].domain.middleInitial
										||!empty submitPointOfContactForm.map.otherPoc.otherPointOfContacts[pocInd].domain.lastName}">
											<tr class="smallTableHeader">
												<td width="4">
													<strong>Name</strong>
												</td>
												<td>
													${submitPointOfContactForm.map.otherPoc.otherPointOfContacts[pocInd].domain.firstName}&nbsp;
													<c:if
														test="${!empty submitPointOfContactForm.map.otherPoc.otherPointOfContacts[pocInd].domain.middleInitial}">
														${submitPointOfContactForm.map.otherPoc.otherPointOfContacts[pocInd].domain.middleInitial}&nbsp;
													</c:if>
													${submitPointOfContactForm.map.otherPoc.otherPointOfContacts[pocInd].domain.lastName}&nbsp;
												</td>
											</tr>
									</c:if>

										<tr class="smallTableHeader">
											<td>
												<strong>Organization</strong>
											</td>
											<td colspan="3">
												${submitPointOfContactForm.map.otherPoc.otherPointOfContacts[pocInd].domain.organization.name}&nbsp;
											</td>
										</tr>
										<c:if
											test="${!empty submitPointOfContactForm.map.otherPoc.otherPointOfContacts[pocInd].domain.organization.name &&
										!empty submitPointOfContactForm.map.otherPoc.otherPointOfContacts[pocInd].domain.organization.streetAddress1}">
											<tr class="smallTableHeader">
												<td>
													<strong>&nbsp;</strong>
												</td>
												<td colspan="3">
													${submitPointOfContactForm.map.otherPoc.otherPointOfContacts[pocInd].domain.organization.streetAddress1}&nbsp;
													<br>
													${submitPointOfContactForm.map.otherPoc.otherPointOfContacts[pocInd].domain.organization.streetAddress2}&nbsp;
													<br>
													${submitPointOfContactForm.map.otherPoc.otherPointOfContacts[pocInd].domain.organization.city}&nbsp;
													${submitPointOfContactForm.map.otherPoc.otherPointOfContacts[pocInd].domain.organization.state}&nbsp;
													${submitPointOfContactForm.map.otherPoc.otherPointOfContacts[pocInd].domain.organization.postalCode}&nbsp;
													${submitPointOfContactForm.map.otherPoc.otherPointOfContacts[pocInd].domain.organization.country}&nbsp;
												</td>
											</tr>
										</c:if>
										<c:if
											test="${!empty submitPointOfContactForm.map.otherPoc.otherPointOfContacts[pocInd].domain.role}">
											<tr class="smallTableHeader">
												<td>
													<strong>Role</strong>
												</td>
												<td>
													${submitPointOfContactForm.map.otherPoc.otherPointOfContacts[pocInd].domain.role}&nbsp;
												</td>
											</tr>
										</c:if>
										<c:if
											test="${!empty submitPointOfContactForm.map.otherPoc.otherPointOfContacts[pocInd].domain.email}">
											<tr class="smallTableHeader">
												<td>
													<strong>Email</strong>
												</td>
												<td colspan="3">
													${submitPointOfContactForm.map.otherPoc.otherPointOfContacts[pocInd].domain.email}&nbsp;
												</td>
											</tr>
										</c:if>
										<c:if
											test="${!empty submitPointOfContactForm.map.otherPoc.otherPointOfContacts[pocInd].domain.phone}">

											<tr class="smallTableHeader">
												<td>
													<strong>Phone</strong>
												</td>
												<td colspan="3">
													${submitPointOfContactForm.map.otherPoc.otherPointOfContacts[pocInd].domain.phone}&nbsp;
												</td>
											</tr>
										</c:if>
									</table>
								</td>
							</tr>
						</logic:iterate>
					</c:if>
				</table>
			</td>
		</tr>
	</table>
</html:form>