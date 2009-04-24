<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table width="100%" align="center">
	<tr>
		<td>
			<h3>
				${fn:toUpperCase(param.location)} ${sampleForm.map.sampleBean.domain.name}
				General Info
			</h3>
		</td>
		<td align="right" width="15%">
			<jsp:include page="/helpGlossary.jsp">
				<jsp:param name="topic" value="manage_nanoparticles_help" />
				<jsp:param name="glossaryTopic" value="glossary_help" />
			</jsp:include>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyMessage.jsp?bundle=particle" />
			<table class="topBorderOnly" cellspacing="0" cellpadding="3"
				width="100%" align="center" summary="" border="0">
				<tbody>
					<tr class="topBorder">
						<td class="formTitle" colspan="2">
							<div align="justify">
								Sample Information
							</div>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Sample Name </strong>
						</td>
						<td class="rightLabel">
							<bean:write name="sampleForm"
								property="sampleBean.domain.name" />
							&nbsp;
						</td>
					</tr>
					<c:if test="${!empty primaryPoc}">
						<tr>
							<td class="leftLabel" valign="top">
								<strong>Primary<br>Point of Contact</strong>
							</td>
							<td class="rightLabel">
								<table class="smalltable" border="0" width="100%">
									<c:if test="${!empty primaryPoc.domain.firstName ||
										!empty primaryPoc.domain.middleInitial ||
										!empty primaryPoc.domain.lastName}">
										<tr class="smallTableHeader">
											<td width="4">
												<strong>Name</strong>
											</td>
											<td>
												${primaryPoc.domain.firstName}&nbsp;
												<c:if test="${!empty primaryPoc.domain.middleInitial}">
													${primaryPoc.domain.middleInitial}&nbsp;
												</c:if>
												${primaryPoc.domain.lastName}&nbsp;
											</td>
										</tr>
									</c:if>
									<c:if test="${!empty primaryPoc.domain.email}">
										<tr class="smallTableHeader">
											<td>
												<strong>Email</strong>
											</td>
											<td colspan="3">
												${primaryPoc.domain.email}&nbsp;
											</td>
										</tr>
									</c:if>
									<c:if test="${!empty primaryPoc.domain.phone}">
										<tr class="smallTableHeader">
											<td>
												<strong>Phone</strong>
											</td>
											<td colspan="3">
												${primaryPoc.domain.phone}&nbsp;
											</td>
										</tr>
									</c:if>
									<tr class="smallTableHeader">
										<td>
											<strong>Organization</strong>
										</td>
										<td colspan="3">
											${primaryPoc.domain.organization.name}&nbsp;
										</td>
									</tr>
									<c:if test="${!empty primaryPoc.domain.organization.name &&
										!empty primaryPoc.domain.organization.streetAddress1}">
										<tr class="smallTableHeader">
											<td>
												<strong>&nbsp;</strong>
											</td>
											<td colspan="3">
												${primaryPoc.domain.organization.streetAddress1}&nbsp;
												<br>
												${primaryPoc.domain.organization.streetAddress2}&nbsp;
												<br>
												${primaryPoc.domain.organization.city}&nbsp;
												${primaryPoc.domain.organization.state}&nbsp;
												${primaryPoc.domain.organization.postalCode}&nbsp;
												${primaryPoc.domain.organization.country}&nbsp;
											</td>
										</tr>
									</c:if>
									<c:if test="${!empty primaryPoc.domain.role}">
										<tr class="smallTableHeader">
											<td>
												<strong>Role</strong>
											</td>
											<td>
												${primaryPoc.domain.role}&nbsp;
											</td>
										</tr>
									</c:if>
								</table>
							</td>
						</tr>
					</c:if>
					<c:if test="${!empty otherPoc}">
						<logic:iterate name="otherPoc" property="otherPointOfContacts" id="otherPOC" indexId="pocInd">
							<tr>
								<td class="leftLabel" valign="top">
									<strong>Secondary<br>Point of Contact #${pocInd+1}</strong>
								</td>
								<td class="rightLabel">
									<table class="smalltable" border="0" width="100%">
										<c:if test="${!empty otherPoc.otherPointOfContacts[pocInd].domain.firstName ||
											!empty otherPoc.otherPointOfContacts[pocInd].domain.middleInitial ||
											!empty otherPoc.otherPointOfContacts[pocInd].domain.lastName}">
											<tr class="smallTableHeader">
												<td width="4">
													<strong>Name</strong>
												</td>
												<td>
													${otherPoc.otherPointOfContacts[pocInd].domain.firstName}&nbsp;
													<c:if
														test="${!empty otherPoc.otherPointOfContacts[pocInd].domain.middleInitial}">
														${otherPoc.otherPointOfContacts[pocInd].domain.middleInitial}&nbsp;
													</c:if>
													${otherPoc.otherPointOfContacts[pocInd].domain.lastName}&nbsp;
												</td>
											</tr>
										</c:if>
										<tr class="smallTableHeader">
											<td>
												<strong>Organization</strong>
											</td>
											<td colspan="3">
												${otherPoc.otherPointOfContacts[pocInd].domain.organization.name}&nbsp;
											</td>
										</tr>
										<c:if test="${!empty otherPoc.otherPointOfContacts[pocInd].domain.organization.name &&
											!empty otherPoc.otherPointOfContacts[pocInd].domain.organization.streetAddress1}">
											<tr class="smallTableHeader">
												<td>
													<strong>&nbsp;</strong>
												</td>
												<td colspan="3">
													${otherPoc.otherPointOfContacts[pocInd].domain.organization.streetAddress1}&nbsp;
													<br>
													${otherPoc.otherPointOfContacts[pocInd].domain.organization.streetAddress2}&nbsp;
													<br>
													${otherPoc.otherPointOfContacts[pocInd].domain.organization.city}&nbsp;
													${otherPoc.otherPointOfContacts[pocInd].domain.organization.state}&nbsp;
													${otherPoc.otherPointOfContacts[pocInd].domain.organization.postalCode}&nbsp;
													${otherPoc.otherPointOfContacts[pocInd].domain.organization.country}&nbsp;
												</td>
											</tr>
										</c:if>
										<c:if test="${!empty otherPoc.otherPointOfContacts[pocInd].domain.role}">
											<tr class="smallTableHeader">
												<td>
													<strong>Role</strong>
												</td>
												<td>
													${otherPoc.otherPointOfContacts[pocInd].domain.role}&nbsp;
												</td>
											</tr>
										</c:if>
										<c:if test="${!empty otherPoc.otherPointOfContacts[pocInd].domain.email}">
											<tr class="smallTableHeader">
												<td>
													<strong>Email</strong>
												</td>
												<td colspan="3">
													${otherPoc.otherPointOfContacts[pocInd].domain.email}&nbsp;
												</td>
											</tr>
										</c:if>
										<c:if test="${!empty otherPoc.otherPointOfContacts[pocInd].domain.phone}">
											<tr class="smallTableHeader">
												<td>
													<strong>Phone</strong>
												</td>
												<td colspan="3">
													${otherPoc.otherPointOfContacts[pocInd].domain.phone}&nbsp;
												</td>
											</tr>
										</c:if>
									</table>
								</td>
							</tr>
						</logic:iterate>
					</c:if>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Keywords</strong> <i>(one keyword per line)</i>
						</td>
						<td class="rightLabel">
							<c:forEach var="keyword"
								items="${sampleForm.map.sampleBean.keywordSet}">
							${keyword}
							<br>
							</c:forEach>
							&nbsp;
						</td>
					</tr>
				</tbody>
			</table>
		</td>
	</tr>
</table>
