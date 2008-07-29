<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="StyleSheet" type="text/css" href="css/printExport.css">
<script type="text/javascript" src="javascript/printExport.js"></script>
<table width="100%" align="center">
	<tr>
		<td>
			<h4>
				<br>
				Documents
			</h4>
		</td>
		<td align="right" width="20%">
			<jsp:include page="/webHelp/helpGlossary.jsp">
				<jsp:param name="topic" value="char_summary_help" />
				<jsp:param name="glossaryTopic" value="glossary_help" />
			</jsp:include>
		</td>
	</tr>
	<tr>
		<td colspan="2" align="left">
			<jsp:include page="/bodyMessage.jsp?bundle=particle" />
			<table>
				<tr>
					<td>
						<ul class="pemenu" id="printChara">
							<li class="pelist">
								<a href="#"><img src="images/icon_print_23x.gif" border="0"
										align="middle"> </a>
								<ul>
									<li>
										<a href="javascript:printPage('${printSummaryViewLinkURL}')">Print
											Summary </a>
									</li>
									<li>
										<a href="javascript:printPage('${printFullSummaryViewLinkURL}')">Print
											Full Summary </a>
									</li>
								</ul>
							</li>
						</ul>
					</td>
					<td>&nbsp;</td>
					<td>
						<c:url var="sumUrl" value="${actionName}.do">
							<c:param name="particleId" value="${particleId}" />
							<c:param name="submitType" value="${submitType}" />
							<c:param name="page" value="0" />
							<c:param name="dispatch" value="exportSummary" />
							<c:param name="location" value="${location}" />
						</c:url>
						<c:url var="fullSumUrl" value="${actionName}.do">
							<c:param name="particleId" value="${particleId}" />
							<c:param name="submitType" value="${submitType}" />
							<c:param name="page" value="0" />
							<c:param name="dispatch" value="exportFullSummary" />
							<c:param name="location" value="${location}" />
						</c:url>
						<ul class="pemenu" id="exportChara">
							<li class="pelist">
								<a href="#"><img src="images/icon_excel_23x.gif" border="0"
										align="middle"> </a>
								<ul>
									<li>
										<a href="${sumUrl}">Export Summary</a>
									</li>
									<li>
										<a href="${fullSumUrl}">Export Full Summary</a>
									</li>
								</ul>
							</li>
						</ul>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<table width="100%" border="0" align="center" cellpadding="3"
				cellspacing="0" class="topBorderOnly" summary="">
				<tr>
					<td class="formTitle"
						colspan="4">
						<div align="justify">
							${fn:toUpperCase(location)} ${particleName} - Documents
						</div>
					</td>
				</tr>
				<tr>
					<th class="leftLabel">
						Identifier
					</th>
					<th class="label">
						First Author
					</th>
					<th class="label">
						Title
					</th>
					<th class="rightLabel">
						Year
					</th>
				</tr>
				<c:forEach var="pubObj" items="${nanoparticleSampleForm.map.particleSampleBean.domainParticleSample.publicationCollection}">
				<tr>
					<td class="leftLabel">
						<c:url var="pubUrl" value="submitPublication.do">
							<c:param name="submitType" value="${submitType}" />
							<c:param name="dispatch" value="detailView" />
							<c:param name="pubId" value="${pubObj.id}" />
							<c:param name="location" value="${location}" />
						</c:url>
						<c:choose>
							<c:when test="${!empty pubObj.pubMedId && pubObj.pubMedId != 0}">
								<a href="${pubUrl }">PMID: ${pubObj.pubMedId }</a>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${!empty pubObj.digitalObjectId && pubObj.digitalObjectId != null}}">
										<a href="${pubUrl }">DOI: ${pubObj.digitalObjectId }</a>
									</c:when>
									<c:otherwise>
										<a href="${pubUrl }">Publication: ${pubObj.title }</a>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					</td>
					<td class="label">
						<c:if test="$(!empty pubObj.documentAuthorCollection}">
							<c:set var="authorCount" value="0" />
							<c:forEach var="author"
									items="${pubObj.documentAuthorCollection}">
								<c:if test="${authorCount < 1 }">
									${author.firstName }&nbsp;${author.middleInitial }.&nbsp;${author.lastName }
								</c:if>
								<c:set var="authorCount" value="${authorCount} + 1" />
							</c:forEach>
						</c:if>
						&nbsp;
					</td>
					<td class="label">
					${pubObj.title}&nbsp;
					</td>
					<td class="rightLabel">
						${pubObj.year}&nbsp;
					</td>
				</tr>
				</c:forEach>
				<c:forEach var="report" items="${nanoparticleSampleForm.map.particleSampleBean.domainParticleSample.reportCollection}" >
				<tr>
					<td class="leftLabel">
						<a href="#">REPORT: ${report.title}</a>
					</td>
					<td class="label">
						&nbsp;
					</td>
					<td class="label">
						${report.title}&nbsp;
					</td>
					<td class="rightLabel">
						&nbsp;
					</td>
				</tr>
				</c:forEach>
			</table>
		</td>
	</tr>
</table>