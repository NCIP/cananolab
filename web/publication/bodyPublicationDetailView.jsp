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
		<td align="right" width="35%">
			<jsp:include page="/helpGlossary.jsp">
				<jsp:param name="topic" value="publication_page_help" />
				<jsp:param name="glossaryTopic" value="glossary_help" />
			</jsp:include>
			<c:if test="${empty docSampleId}">
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
										<c:when test="${!empty docSampleId }">
											${fn:toUpperCase(param.location)} ${sampleName}
										</c:when>
										<c:otherwise>
											${fn:toUpperCase(param.location)}
										</c:otherwise>
									</c:choose>
								</td>
								<td align="right" class="formTitle">

									<c:url var="url" value="publication.do">
										<c:param name="page" value="0" />
										<c:param name="dispatch" value="setupUpdate" />
										<c:param name="sampleId" value="${param.sampleId}" />
										<c:param name="publicationId" value="${param.publicationId}" />
										<c:param name="submitType" value="${param.submitType}" />
										<c:param name="location" value="${param.location}" />
									</c:url>

									<c:if
										test="${!empty user && user.curator && param.location eq '${applicationOwner}'}">
										<td>
											<a href="${url}"><img src="images/icon_edit_23x.gif"
													alt="edit publication"
													title="edit publication" border="0"> </a>
										</td>
									</c:if>
								<td>
									<a href="javascript:printPage('${printDetailViewLinkURL}')"><img
											src="images/icon_print_23x.gif"
											alt="print publication detail"
											title="print publication detail" border="0"> </a>
								</td>
								<td>
									<c:url var="exportUrl" value="publication.do">
										<c:param name="page" value="0" />
										<c:param name="dispatch" value="exportDetail" />
										<c:param name="sampleId" value="${sampleId}" />
										<c:param name="publicationId"
											value="${publicationForm.map.file.domainFile.id}" />
										<c:param name="submitType" value="${submitType}" />
										<c:param name="location" value="${param.location}" />
									</c:url>
									<a href="${exportUrl}"><img src="images/icon_excel_23x.gif"
											alt="export publication detail"
											title="export publication detail" border="0"> </a>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Publication Identifer
					</th>
					<td class="rightLabel">
						<c:choose>
							<c:when test="${publicationForm.map.file.domainFile.pubMedId != null &&
										publicationForm.map.file.domainFile.pubMedId != 0}">
								PMID: ${publicationForm.map.file.domainFile.pubMedId }
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${publicationForm.map.file.domainFile.digitalObjectId != null
										&& publicationForm.map.file.domainFile.digitalObjectId ne ''}">
										DOI: ${publicationForm.map.file.domainFile.digitalObjectId }
									</c:when>
									<c:otherwise>
										&nbsp;
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
							&nbsp;
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Publication Type
					</th>
					<td class="rightLabel">
						<bean:write name="publicationForm"
								property="file.domainFile.category" />
							&nbsp;
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Publication Status
					</th>
					<td class="rightLabel">
						<bean:write name="publicationForm"
								property="file.domainFile.status" />
							&nbsp;
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Research Category
					</th>
					<td class="rightLabel">
						<bean:write name="publicationForm"
								property="file.domainFile.researchArea" />
						&nbsp;
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Title
					</th>
					<td class="rightLabel">
						<bean:write name="publicationForm"
								property="file.domainFile.title" />
							&nbsp;
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Journal
					</th>
					<td class="rightLabel">
						<bean:write name="publicationForm"
								property="file.domainFile.journalName" />
						&nbsp;
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Authors
					</th>
					<td class="rightLabel">
						<c:if test="${!empty publicationForm.map.file.authors}">
							<c:forEach var="author"
								items="${publicationForm.map.file.authors}">
									${author.firstName}&nbsp;${author.lastName}&nbsp;${author.initial}<br>
							</c:forEach>
						</c:if>
						&nbsp;
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Year
					</th>
					<td class="rightLabel">
						<c:if test="${publicationForm.map.file.domainFile.year!=0}">
							<bean:write name="publicationForm"
								property="file.domainFile.year" />
						</c:if>
						&nbsp;
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Volume
					</th>
					<td class="rightLabel">
						<bean:write name="publicationForm"
								property="file.domainFile.volume" />
						&nbsp;
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Pages
					</th>
					<td class="rightLabel">
						<c:if test="${publicationForm.map.file.domainFile.startPage != null}">
						<bean:write name="publicationForm"
								property="file.domainFile.startPage" /> - <bean:write name="publicationForm"
								property="file.domainFile.endPage" />
						</c:if>
						&nbsp;
					</td>
				</tr>

				<c:choose>
					<c:when
						test="${publicationForm.map.file.domainFile.pubMedId != null &&
										publicationForm.map.file.domainFile.pubMedId != 0}">
						<tr>
							<th class="leftLabel" valign="top">
								Abstract in
								<br>
								PubMed
							</th>
							<td class="rightLabel">
								<a target="_pubmed" href="http://www.ncbi.nlm.nih.gov/pubmed/${publicationForm.map.file.domainFile.pubMedId}">PMID:
									${publicationForm.map.file.domainFile.pubMedId }</a> &nbsp;
								&nbsp;
							</td>
						</tr>
					</c:when>
				</c:choose>

				<tr>
					<th class="leftLabel" valign="top">
						Description
					</th>
					<td class="rightLabel">
						<bean:write name="publicationForm"
								property="file.domainFile.description" />
							&nbsp;
					</td>
				</tr>

				<c:choose>
					<c:when
						test="${publicationForm.map.file.domainFile.pubMedId == null ||
										publicationForm.map.file.domainFile.pubMedId == 0}">
						<tr>
							<th class="leftLabel" valign="top">
								<c:choose>
									<c:when
										test="${submitReportForm.map.file.domainFile.uriExternal==true}">
								Publication URL&nbsp;
							</c:when>
									<c:otherwise>
								Download Publication&nbsp;
							</c:otherwise>
								</c:choose>
							</th>
							<td class="rightLabel">
								<a
									href="publication.do?dispatch=download&amp;publicationId=${publicationForm.map.file.domainFile.id}&amp;location=${param.location}"
									target="${publicationForm.map.file.urlTarget}">
									${publicationForm.map.file.domainFile.uri}</a>&nbsp;
							</td>
						</tr>
					</c:when>
				</c:choose>
			</table>
		</td>
	</tr>
</table>
