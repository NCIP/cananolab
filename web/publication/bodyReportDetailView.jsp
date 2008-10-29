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
				Report
			</h4>
		</td>
		<td align="right" width="35%">
			<jsp/helpGlossary.jspelpGlossary.jsp">
				<jsp:param name="topic" value="report_page_help" />
				<jsp:param name="glossaryTopic" value="glossary_help" />
			</jsp:include>
			<c:if test="${empty docParticleId}">
			<c:url var="url" value="searchPublication.do">
				<c:param name="page" value="1" />
				<c:param name="dispatch" value="search" />
				<c:param name="invokeMethod" value="back" />
			</c:url>
			<a href="${url}" class="helpText">Back</a>
			</c:if>
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
									<c:url var="url" value="submitReport.do">
										<c:param name="page" value="0" />
										<c:param name="dispatch" value="setupUpdate" />
										<c:param name="particleId" value="${param.particleId}" />
										<c:param name="reportId" value="${param.reportId}" />
										<c:param name="submitType" value="${param.submitType}" />
										<c:param name="location" value="${param.location}" />
									</c:url>
									<c:if
										test="${canCreateNanoparticle eq 'true' && param.location eq 'local'}">
										<td>
											<a href="${url}"><img src="images/icon_edit_23x.gif"
													alt="edit report"
													title="edit report" border="0"> </a>
										</td>
									</c:if>
								<td>
									<a href="javascript:printPage('${printDetailViewLinkURL}')"><img
											src="images/icon_print_23x.gif"
											alt="print report detail" 
											title="print report detail"  border="0"> </a>
								</td>
								<td>
									<c:url var="exportUrl" value="submitReport.do">
										<c:param name="page" value="0" />
										<c:param name="dispatch" value="exportDetail" />
										<c:param name="particleId" value="${particleId}" />
										<c:param name="reportId"
											value="${param.reportId}" />
										<c:param name="submitType" value="${submitType}" />
										<c:param name="location" value="${param.location}" />
									</c:url>
									<a href="${exportUrl}"><img src="images/icon_excel_23x.gif"
											alt="export report detail" 
											title="export report detail" border="0"> </a>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			
				<tr>
					<th class="leftLabel" valign="top">
						Title
					</th>
					<td class="rightLabel">
						<bean:write name="submitReportForm"
							property="file.domainFile.title" />
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						<c:choose>
							<c:when test="${submitReportForm.map.file.domainFile.uriExternal==true}">
								Report URL
							</c:when>
							<c:otherwise>
								Report File
							</c:otherwise>
						</c:choose>
					</th>
					<td class="rightLabel">
						<a
							href="searchReport.do?dispatch=download&amp;fileId=${submitReportForm.map.file.domainFile.id}&amp;location=${param.location}"
							target="${submitReportForm.map.file.urlTarget}">
							${submitReportForm.map.file.domainFile.uri}</a>
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Description
					</th>
					<td class="rightLabel">
						<bean:write name="submitReportForm"
							property="file.domainFile.description" />&nbsp;
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
