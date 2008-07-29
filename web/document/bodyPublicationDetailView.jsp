<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table width="100%" align="center">
	<tr>
		<td>
			<h4>
				<br>
				Publication
			</h4>
		</td>
		<td align="right" width="20%">
			<jsp:include page="/webHelp/helpGlossary.jsp">
				<jsp:param name="topic" value="char_details_help" />
				<jsp:param name="glossaryTopic" value="glossary_help" />
			</jsp:include>
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
									${fn:toUpperCase(param.location)} ${particleName}
								</td>
								<td align="right" class="formTitle">

									<c:url var="url" value="submitReport.do">
										<!-- FIXME: hardcode action -->
										<c:param name="page" value="0" />
										<c:param name="dispatch" value="setupUpdate" />
										<c:param name="particleId" value="${particleId}" />
										<c:param name="fileId" value="${param.fileId}" />
										<c:param name="submitType" value="${param.submitType}" />
										<c:param name="location" value="${location}" />
									</c:url>
									<c:if
										test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
										<td>
											<a href="${url}"><img src="images/icon_edit_23x.gif"
													alt="edit characterization" border="0"> </a>
										</td>
									</c:if>
								<td>
									<a href="javascript:printPage('${printDetailViewLinkURL}')"><img
											src="images/icon_print_23x.gif"
											alt="print characterization detail" border="0"> </a>
								</td>
								<td>
									<c:url var="exportUrl" value="${actionName}.do">
										<c:param name="page" value="0" />
										<c:param name="dispatch" value="exportDetail" />
										<c:param name="particleId" value="${particleId}" />
										<c:param name="dataId"
											value="${characterizationForm.map.achar.domainChar.id}" />
										<c:param name="dataClassName" value="${param.dataClassName }" />
										<c:param name="submitType" value="${submitType}" />
										<c:param name="location" value="${location}" />
									</c:url>
									<a href="${exportUrl}"><img src="images/icon_excel_23x.gif"
											alt="export characterization detail" border="0"> </a>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Publication Type
					</th>
					<td class="rightLabel">
						report
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Publication Status
					</th>
					<td class="rightLabel">
						Submitted
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						First Author
					</th>
					<td class="rightLabel">
						Scott E. McNeil
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Title
					</th>
					<td class="rightLabel">
						DENDRITIC NANOTECHNOLOGIES
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						File URL
					</th>
					<td class="rightLabel">
						<a href="#">reports/200612_8-06-33-125_120406.pdf</a>
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Description
					</th>
					<td class="rightLabel">
						&nbsp;
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
