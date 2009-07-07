<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${not empty theSample}">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					${fn:toUpperCase(location)} Sample ${theSample.domain.name}
					Publication
				</h4>
			</td>
			<td align="right" width="15%">
				<jsp:include page="/helpGlossary.jsp">
					<jsp:param name="topic" value="publications_all_tab_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>
			</td>
		</tr>
	</table>
</c:if>
<jsp:include page="/bodyMessage.jsp?bundle=particle" />
<c:set var="publicationCategories"
	value="${publicationSummaryView.publicationCategories}" />
<c:if test="${empty printView}">
	<div class="animatedtabs" id="summaryTabALL">
		<ul>
			<li class="selected">
				<a
					href="javascript:showSummary('ALL', ${fn:length(publicationCategories)})"
					title="All"><span>All</span> </a>
				<c:url var="printUrl" value="${actionName}">
					<c:param name="dispatch" value="summaryPrint" />
					<c:param name="sampleId" value="${sampleId}" />
					<c:param name="location" value="${location}" />
				</c:url>
				<c:url var="exportUrl" value="${actionName}">
					<c:param name="dispatch" value="summaryExport" />
					<c:param name="sampleId" value="${sampleId}" />
					<c:param name="location" value="${location}" />
				</c:url>
				<a href="javascript:printPage('${printUrl}')" id="printUrlAll"
					style="display: none;"></a>
				<a href="${exportUrl}" id="exportUrlAll" style="display: none;"></a>
			</li>
			<li>
				<c:forEach var="type" items="${publicationCategories}"
					varStatus="ind">
					<a
						href="javascript:showSummary('${ind.count}', ${fn:length(publicationCategories)})"
						title="${type}"><span>${type}</span>
					</a>
					<a href="javascript:printPage('${printUrl}&type=${type}')"
						id="printUrl${ind.count}" style="display: none;"></a>
					<a href="${exportUrl}&type=${type}" id="exportUrl${ind.count}"
						style="display: none;"></a>
				</c:forEach>
			</li>
		</ul>
	</div>
	<c:forEach var="type" items="${publicationCategories}" varStatus="ind">
		<div class="animatedtabs" id="summaryTab${ind.count}"
			style="display: none;">
			<ul>
				<li>
					<a
						href="javascript:showSummary('ALL', ${fn:length(publicationCategories)})"
						title="All"><span>All</span> </a>
				</li>
				<c:forEach var="type" items="${publicationCategories}"
					varStatus="ind2">
					<c:choose>
						<c:when test="${ind.count eq ind2.count }">
							<c:set var="selectedClass" value="selected" />
						</c:when>
						<c:otherwise>
							<c:set var="selectedClass" value="" />
						</c:otherwise>
					</c:choose>
					<li class="${selectedClass}">
						<a
							href="javascript:showSummary('${ind2.count}', ${fn:length(publicationCategories)})"
							title="${type}"><span>${type}</span> </a>
					</li>
				</c:forEach>
			</ul>
		</div>
	</c:forEach>
</c:if>
<table class="summaryViewLayer1" width="100%">
	<c:if test="${! empty publicationCategories && empty printView}">
		<tr>
			<td>
				<a href="javascript:printPage('${printUrl}')" id="printLink">Print</a>&nbsp;&nbsp;
				<a href="${exportUrl}" id="exportLink">Export</a>
			</td>
		</tr>
	</c:if>
	<tr>
		<td>
			<c:forEach var="type" items="${publicationCategories}"
				varStatus="ind">
				<table id="summarySection${ind.count}" class="smalltable3"
					cellpadding="0" cellspacing="0" border="0" width="100%">
					<tr>
						<th align="left">
							<a name="${type}" id="${type}">${type}</a>
						</th>
					</tr>
					<tr>
						<td>
							<c:choose>
								<c:when
									test="${! empty publicationSummaryView.category2Publications[type]}">
									<div class="indented4">
										<table class="summarytable" width="100%" border="0"
											cellpadding="0" cellspacing="0" summary="">
											<tr>
												<th width="70%">
													Bibliography Info
												</th>
												<th width="15%">
													Abstract/Download Link
												</th>
												<th>
													Research Category
												</th>
												<th width="5%">
													&nbsp;
												</th>
											</tr>
											<c:forEach var="pubBean"
												items="${publicationSummaryView.category2Publications[type]}">
												<c:set var="pubObj" value="${pubBean.domainFile}" />
												<tr>
													<td valign="top">
														${pubBean.displayName}&nbsp;
													</td>
													<td valign="top">
														<c:choose>
															<c:when test="${! empty pubObj.pubMedId}">
																<a target="_abstract"
																	href="http://www.ncbi.nlm.nih.gov/pubmed/${pubObj.pubMedId}">PMID:
																	${pubObj.pubMedId }</a>&nbsp;
															</c:when>
															<c:otherwise>
																<c:choose>
																	<c:when test="${! empty pubObj.digitalObjectId}">
																		<a target="_abstract"
																			href="http://dx.doi.org/${pubObj.digitalObjectId}">PMID:
																			${pubObj.digitalObjectId }</a>&nbsp;
																	</c:when>
																	<c:otherwise>
																		<a
																			href="searchPublication.do?dispatch=download&amp;publicationId=${pubObj.id}&amp;location=${param.location}"
																			target="${pubBean.urlTarget}"> ${pubOjb.uri}</a>&nbsp;
																	</c:otherwise>
																</c:choose>
															</c:otherwise>
														</c:choose>
														&nbsp;
													</td>
													<td valign="top">
														${pubObj.researchArea}&nbsp;
													</td>
													<td valign="top">
													</td>
												</tr>
											</c:forEach>
										</table>
									</div>
								</c:when>
								<c:otherwise>
									<div class="indented4">
										<table class="summarytable" cellpadding="0" cellspacing="0"
											border="0" width="100%">
											<tr>
												<td colspan="2" valign="top" align="left"
													class="borderlessLabel">
													N/A
												</td>
											</tr>
										</table>
									</div>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</table>
				<div id="summarySeparator${ind.count}">
					<br>
				</div>
			</c:forEach>
		</td>
	</tr>
</table>
