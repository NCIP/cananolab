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
									${fn:toUpperCase(param.location)} ${particleName}&nbsp;
								</td>
								<td align="right" class="formTitle">

									<c:url var="url" value="submitPublication.do">
										<c:param name="page" value="0" />
										<c:param name="dispatch" value="setupUpdate" />
										<c:param name="particleId" value="${param.particleId}" />
										<c:param name="fileId" value="${param.publicationId}" />
										<c:param name="submitType" value="${param.submitType}" />
										<c:param name="location" value="${param.location}" />
									</c:url>
									
									<c:if
										test="${canCreateNanoparticle eq 'true' && param.location eq 'local'}">
										<td>
											<a href="${url}"><img src="images/icon_edit_23x.gif"
													alt="edit characterization" 
													title="edit characterization" border="0"> </a>
										</td>
									</c:if>
								<td>
									<a href="javascript:printPage('${printDetailViewLinkURL}')"><img
											src="images/icon_print_23x.gif"
											alt="print publication detail"
											title="print publication detail" border="0"> </a>
								</td>
								<td>
									<c:url var="exportUrl" value="submitPublication.do">
										<c:param name="page" value="0" />
										<c:param name="dispatch" value="exportDetail" />
										<c:param name="particleId" value="${particleId}" />
										<c:param name="publicationId"
											value="${submitPublicationForm.map.file.domainFile.id}" />
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
							<c:when test="${submitPublicationForm.map.file.domainFile.pubMedId != null && 
										submitPublicationForm.map.file.domainFile.pubMedId != 0}">
								PMID: ${submitPublicationForm.map.file.domainFile.pubMedId }
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${submitPublicationForm.map.file.domainFile.digitalObjectId != null
										&& submitPublicationForm.map.file.domainFile.digitalObjectId ne ''}">
										DOI: ${submitPublicationForm.map.file.domainFile.digitalObjectId }
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
						<bean:write name="submitPublicationForm"
								property="file.domainFile.category" />
							&nbsp;
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Publication Status
					</th>
					<td class="rightLabel">
						<bean:write name="submitPublicationForm"
								property="file.domainFile.status" />
							&nbsp;
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Research Category
					</th>
					<td class="rightLabel">
						<bean:write name="submitPublicationForm"
								property="file.domainFile.researchArea" />
						&nbsp;				
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Title
					</th>
					<td class="rightLabel">
						<bean:write name="submitPublicationForm"
								property="file.domainFile.title" />
							&nbsp;
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Journal
					</th>
					<td class="rightLabel">
						<bean:write name="submitPublicationForm"
								property="file.domainFile.journalName" />
						&nbsp;
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Authors
					</th>
					<td class="rightLabel">
						<c:if test="${!empty submitPublicationForm.map.file.authors}">
							<c:forEach var="author"
								items="${submitPublicationForm.map.file.authors}">
									${author.firstName}&nbsp;${author.middleInitial}&nbsp;${author.lastName}<br>
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
						<c:if test="${submitPublicationForm.map.file.domainFile.year!=0}">
							<bean:write name="submitPublicationForm"
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
						<bean:write name="submitPublicationForm"
								property="file.domainFile.volume" />
						&nbsp;
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Pages
					</th>
					<td class="rightLabel">
						<c:if test="${submitPublicationForm.map.file.domainFile.startPage != null && 
										submitPublicationForm.map.file.domainFile.startPage != 0}">
						<bean:write name="submitPublicationForm"
								property="file.domainFile.startPage" /> - <bean:write name="submitPublicationForm"
								property="file.domainFile.endPage" />
						</c:if>
						&nbsp;
					</td>
				</tr>

				<c:choose>
					<c:when
						test="${submitPublicationForm.map.file.domainFile.pubMedId != null && 
										submitPublicationForm.map.file.domainFile.pubMedId != 0}">
						<tr>
							<th class="leftLabel" valign="top">
								Abstract in
								<br>
								PubMed
							</th>
							<td class="rightLabel">
								<a target="_pubmed" href="http://www.ncbi.nlm.nih.gov/pubmed/${submitPublicationForm.map.file.domainFile.pubMedId}">PMID:
									${submitPublicationForm.map.file.domainFile.pubMedId }</a> &nbsp;
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
						<bean:write name="submitPublicationForm"
								property="file.domainFile.description" />
							&nbsp;
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
