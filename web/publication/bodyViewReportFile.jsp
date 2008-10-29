<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table width="100%" align="center">
	<tr>
		<td>
			<h3>
				<br>
				Report
			</h3>
		</td>
		<td align="right" width="20%">
			<jsp:/helpGlossary.jsplpGlossary.jsp">
				<jsp:param name="topic" value="report_file_page" />
				<jsp:param name="glossaryTopic" value="glossary_help" />
			</jsp:include>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyMessage.jsp?bundle=report" />
			<table class="topBorderOnly" cellspacing="0" cellpadding="3"
				width="100%" align="center" summary="" border="0">
				<tbody>
					<tr class="topBorder">
						<td class="formTitle" colspan="4">
							<div align="justify">
								File Information
							</div>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Report File Category</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<bean:write name="submitReportForm"
								property="file.domainFile.category" />
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>File URL</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<c:choose>
								<c:when test="${submitReportForm.map.file.hidden eq 'true' }">
									Private file
								</c:when>
								<c:otherwise>
									<a
										href="searchReport.do?dispatch=download&amp;fileId=${submitReportForm.map.file.domainFile.id}&amp;location=${location}"
										target="${submitReportForm.map.file.urlTarget}"> <bean:write
											name="submitReportForm" property="file.domainFile.uri" /> </a>&nbsp;
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Title</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<bean:write name="submitReportForm"
								property="file.domainFile.title" />
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Description</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<bean:write name="submitReportForm"
								property="file.domainFile.description" />
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Comments</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<bean:write name="submitReportForm"
								property="file.domainFile.comments" />
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Visibility</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<bean:write name="submitReportForm" property="file.visibilityStr"
								filter="false" />
							&nbsp;
						</td>
					</tr>
				</tbody>
			</table>
			<br>
		</td>
	</tr>
</table>
