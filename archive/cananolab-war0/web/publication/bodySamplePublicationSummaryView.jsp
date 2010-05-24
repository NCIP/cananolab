<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${not empty theSample}">
	<jsp:include page="/bodyTitle.jsp">
		<jsp:param name="pageTitle"
			value="${fn:toUpperCase(location)} Sample ${theSample.domain.name} Publication" />
		<jsp:param name="topic" value="publications_all_tab_help" />
		<jsp:param name="glossaryTopic" value="glossary_help" />
	</jsp:include>
</c:if>
<jsp:include page="/bodyMessage.jsp?bundle=sample" />
<c:set var="publicationCategories"
	value="${publicationSummaryView.publicationCategories}" />
<c:if test="${empty printView}">
	<div class="shadetabs" id="summaryTabALL">
		<ul>
			<li class="selected">
				<a
					href="javascript:showSummary('ALL', ${fn:length(publicationCategories)})"
					title="All"><span>All</span> </a>
				<c:url var="printUrl" value="publication.do">
					<c:param name="dispatch" value="summaryPrint" />
					<c:param name="sampleId" value="${sampleId}" />
					<c:param name="location" value="${location}" />
				</c:url>
				<c:url var="exportUrl" value="publication.do">
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
						title="${type}"><span>${type}</span> </a>
					<a href="javascript:printPage('${printUrl}&type=${type}')"
						id="printUrl${ind.count}" style="display: none;"></a>
					<a href="${exportUrl}&type=${type}" id="exportUrl${ind.count}"
						style="display: none;"></a>
				</c:forEach>
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
				<table id="summarySection${ind.count}" class="summaryViewLayer2"
					align="center" width="95%">
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
									<table class="summaryViewLayer3" align="center" width="95%">
										<tr>
											<th>
												Bibliography Info
											</th>
											<th>
												Research Category
											</th>
											<th>
												Description
											</th>
											<th>
												Publication Status
											</th>
										</tr>
										<c:forEach var="pubBean"
											items="${publicationSummaryView.category2Publications[type]}"
											varStatus="pubBeanInd">
											<c:set var="pubObj" value="${pubBean.domainFile}" />
											<tr>
												<td valign="top">
													${pubBean.displayName}&nbsp;
												</td>
												<td valign="top">
													<c:out
														value="${fn:replace(pubObj.researchArea, ';', '<br>')}"
														escapeXml="false" />
													&nbsp;
												</td>
												<td valign="top">
													<c:if test="${!empty pubObj.description}">
														<div id="descriptionSection" style="position: relative;">
															<a style="display: block" id="viewDetail" href="#"
																onmouseOver="javascript: show('publicationDescription${pubBeanInd.count}');"
																onmouseOut="javascript: hide('publicationDescription${pubBeanInd.count}');">View</a>
															<table id="publicationDescription${pubBeanInd.count}"
																style="display: none; position: absolute; left: -510px; top: -50px; width: 500px; z-index: 5; border-width: 1px; border-color: #cccccc; border-style: solid; background-color: #FFFFFF;"
																class="promptbox">
																<tr>
																	<td>
																		${pubObj.description}
																	</td>
																</tr>
															</table>
														</div>
													</c:if>
												</td>
												<td valign="top">
													<c:out value="${pubObj.status}" />
													&nbsp;
												</td>
											</tr>
										</c:forEach>
									</table>
								</c:when>
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
