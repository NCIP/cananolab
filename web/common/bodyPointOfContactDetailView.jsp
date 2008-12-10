<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html:form action="/submitOrganization">
<table width="100%" align="center">
	<tr>
		<td>
			<h4>
				<br>
				Organization
			</h4>
		</td>
		<td align="right" width="35%">
			<jsp:include page="/helpGlossary.jsp">
				<jsp:param name="topic" value="organization_page_help" />
				<jsp:param name="glossaryTopic" value="glossary_help" />
			</jsp:include>

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
										<c:when test="${!empty docParticleId }">
											${fn:toUpperCase(param.location)} ${particleName}
										</c:when>
										<c:otherwise>
											${fn:toUpperCase(param.location)}
										</c:otherwise>
									</c:choose>
								</td>
								<td align="right" class="formTitle">

									<c:url var="url" value="submitOrganization.do">
										<c:param name="page" value="0" />
										<c:param name="dispatch" value="setupUpdate" />
										<c:param name="particleId" value="${param.particleId}" />
										<c:param name="fileId" value="${param.organizationId}" />
										<c:param name="submitType" value="${param.submitType}" />
										<c:param name="location" value="${param.location}" />
									</c:url>

									<c:if
										test="${canCreateNanoparticle eq 'true' && param.location eq 'local'}">
										<td>
											<a href="${url}"><img src="images/icon_edit_23x.gif"
													alt="edit organization" title="edit contact organization"
													border="0"> </a>
										</td>
									</c:if>
								</td>
								<td>
									<a href="javascript:printPage('${printDetailViewLinkURL}')"><img
											src="images/icon_print_23x.gif"
											alt="print organization detail"
											title="print contact organization detail" border="0"> </a>
								</td>
								<td>
									<c:url var="exportUrl" value="submitOrganization.do">
										<c:param name="page" value="0" />
										<c:param name="dispatch" value="exportDetail" />
										<c:param name="submitType" value="${submitType}" />
										<c:param name="location" value="${param.location}" />
									</c:url>
									<a href="${exportUrl}"><img src="images/icon_excel_23x.gif"
											alt="export organization detail"
											title="export organization detail" border="0"> </a>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Primary Organization
					</th>
					<td class="rightLabel">
						${submitOrganizationForm.map.orga.domain.name }&nbsp;
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Address
					</th>
					<td class="rightLabel">
						<bean:write name="submitOrganizationForm" property="orga.address" />
						&nbsp;
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Point of Contact
					</th>
					<td class="rightLabel">
						<table class="smalltable" border="0" width="100%">
							<tr class="smallTableHeader">
								<th>
									Contact Type
								</th>
								<th>
									First Name
								</th>
								<th>
									Middle Initial
								</th>
								<th>
									Last Name
								</th>
								<th>
									Email
								</th>
								<th>
									Phone
								</th>
							</tr>

							<logic:iterate name="submitOrganizationForm" property="orga.pocs"
								id="author" indexId="pocInd">
								<tr>
									<td>
										${submitOrganizationForm.map.orga.pocs[pocInd].role}&nbsp;
									</td>
									<td>
										${submitOrganizationForm.map.orga.pocs[pocInd].firstName}&nbsp;
									</td>
									<td>
										${submitOrganizationForm.map.orga.pocs[pocInd].middleInitial}&nbsp;
									</td>
									<td>
										${submitOrganizationForm.map.orga.pocs[pocInd].lastName}&nbsp;
									</td>
									<td>
										${submitOrganizationForm.map.orga.pocs[pocInd].email}&nbsp;
									</td>
									<td>
										${submitOrganizationForm.map.orga.pocs[pocInd].phone}&nbsp;
									</td>
								</tr>
							</logic:iterate>

						</table>
						&nbsp;
					</td>
				</tr>

			</table>
		</td>
	</tr>
</table>
</html:form>