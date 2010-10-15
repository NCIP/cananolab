<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:url var="printUrl" value="publication.do">
	<c:param name="dispatch" value="summaryPrint" />
	<c:param name="sampleId" value="${sampleId}" />
</c:url>
<c:url var="exportUrl" value="publication.do">
	<c:param name="dispatch" value="summaryExport" />
	<c:param name="sampleId" value="${sampleId}" />
</c:url>
<c:if test="${not empty theSample}">
	<jsp:include page="/bodyTitle.jsp">
		<jsp:param name="pageTitle"
			value="Sample ${theSample.domain.name} Publication" />
		<jsp:param name="topic" value="publications_all_tab_help" />
		<jsp:param name="glossaryTopic" value="glossary_help" />
		<jsp:param name="printLink" value="${printUrl}" />
		<jsp:param name="exportLink" value="${exportUrl}" />
	</jsp:include>
</c:if>
<jsp:include page="/bodyMessage.jsp?bundle=sample" />
<div class="shadetabs" id="summaryTabALL">
	<ul>
		<li class="selected">
			<a
				href="javascript:showSummary('ALL', ${fn:length(publicationCategories)})"
				title="All"><span>All</span> </a>
		</li>
		<li>
			<c:forEach var="type" items="${publicationCategories}"
				varStatus="ind">
				<a
					href="javascript:showSummary(${ind.count}, ${fn:length(publicationCategories)})"
					title="${type}"><span>${type}</span>
				</a>
				<a href="javascript:printPage('${printUrl}&type=${type}')"
					id="printUrl${ind.count}" style="display: none;"></a>
				<a href="${exportUrl}&type=${type}" id="exportUrl${ind.count}"
					style="display: none;"></a>
			</c:forEach>
		</li>
		<li>
			<a href="publication.do?dispatch=setupNew&sampleId=${sampleId}"><span>other</span>
			</a>
		</li>
	</ul>
</div>
<c:forEach var="type" items="${publicationCategories}" varStatus="ind">
	<div class="shadetabs" id="summaryTab${ind.count}"
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
						href="javascript:showSummary(${ind2.count}, ${fn:length(publicationCategories)})"
						title="${type}"><span>${type}</span> </a>
				</li>
			</c:forEach>
			<li>
				<a href="publication.do?dispatch=setupNew&sampleId=${sampleId}"><span>Other</span>
				</a>
			</li>
		</ul>
	</div>
</c:forEach>
<table class="summaryViewNoTop" width="100%">
	<%--
	<c:if test="${! empty publicationCategories && empty printView}">
		<tr>
			<td>
				<a href="javascript:printPage('${printUrl}')" id="printLink">Print</a>&nbsp;&nbsp;
				<a href="${exportUrl}" id="exportLink">Export</a>
			</td>
		</tr>
	</c:if>
--%>
	<tr>
		<td>
			<c:forEach var="type" items="${publicationCategories}"
				varStatus="ind">
				<table id="summarySection${ind.count}" class="summaryViewNoGrid"
					width="100%" align="center">
					<tr>
						<th align="left">
							<a name="${type}" id="${type}"><span
								class="summaryViewHeading">${type}</span></a>&nbsp;&nbsp;
							<a
								href="publication.do?dispatch=setupNew&sampleId=${sampleId}&type=${type}"
								class="addlink"><img align="middle" src="images/btn_add.gif"
									border="0" /></a>&nbsp;&nbsp;
							<%--
							<c:if test="${! empty publicationSummaryView.category2Publications[type]}">
								<a><img align="middle" src="images/btn_delete.gif"
										border="0" /> </a>
							</c:if>
							--%>
						</th>
					</tr>
					<tr>
						<td>
							<c:choose>
								<c:when
									test="${! empty publicationSummaryView.category2Publications[type]}">
									<table width="99%" align="center" class="summaryViewNoGrid"
										bgcolor="#dbdbdb">
										<tr>
											<th valign="top" align="left" height="6">
											</th>
										</tr>
										<tr>
											<td>
												<c:forEach var="pubBean"
													items="${publicationSummaryView.category2Publications[type]}"
													varStatus="pubBeanInd">
													<c:set var="pubObj" value="${pubBean.domainFile}" />
													<table class="summaryViewNoGrid" width="99%" align="center"
														bgcolor="#F5F5f5">
														<tr>
															<td></td>
															<td width="95%"></td>
															<td align="right">
																<c:url var="pubUrl" value="publication.do">
																	<c:param name="sampleId" value="${sampleId}" />
																	<c:param name="dispatch" value="setupUpdate" />
																	<c:param name="publicationId" value="${pubObj.id}" />
																</c:url>
																<c:if test="${pubBean.userUpdatable}">
																	<a href="${pubUrl}">Edit</a>
																</c:if>
															</td>
														</tr>
														<tr>
															<td class="cellLabel" width="10%">
																Bibliography Info
															</td>
															<td>
																${pubBean.displayName}&nbsp;
															</td>
															<td></td>
														</tr>
														<tr>
															<td class="cellLabel" width="10%">
																Research Category
															</td>
															<td colspan="2">
																<c:choose>
																	<c:when test="${pubObj.researchArea}">
																		<c:out
																			value="${fn:replace(pubObj.researchArea, ';', '<br>')}"
																			escapeXml="false" />
																&nbsp;
																</c:when>
																	<c:otherwise>N/A</c:otherwise>
																</c:choose>
															</td>
														</tr>
														<tr>
															<td class="cellLabel" width="10%">
																Description
															</td>
															<td>
																<c:choose>
																	<c:when test="${pubObj.description}">
																		<c:set var="desc" value="${fn:replace(pubObj.deescription, '<', '&lt;')}" />
																		<c:out
																			value="${fn:replace(desc, cr, '<br>')}"
																			escapeXml="false" />
																&nbsp;</c:when>
																	<c:otherwise>N/A</c:otherwise>
																</c:choose>

															</td>
															<td></td>
														</tr>
														<tr>
															<td class="cellLabel" width="10%">
																Keywords
															</td>
															<td>
																<c:choose>
																	<c:when test="${pubBean.keywordsStr}">
																		<c:set var="keywordsStr" value="${fn:replace(pubBean.keywordsStr, '<', '&lt;')}" />
																		<c:out
																			value="${fn:replace(keywordsStr, cr, '<br>')}"
																			escapeXml="false" />
																&nbsp;</c:when>
																	<c:otherwise>N/A</c:otherwise>
																</c:choose>

															</td>
															<td></td>
														</tr>
														<tr>
															<td class="cellLabel" width="10%">
																Publication Status
															</td>
															<td>
																${pubObj.status}&nbsp;
															</td>
															<td></td>
														</tr>
													</table>
													<c:if
														test="${pubBeanInd.count<fn:length(publicationSummaryView.category2Publications[type])}">
														<br />
													</c:if>
												</c:forEach>
											</td>
										</tr>
										<tr>
											<th valign="top" align="left" height="6">
											</th>
										</tr>
									</table>
								</c:when>
								<c:otherwise>
									<div class="indented4">
										N/A
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