<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:url var="printUrl" value="characterization.do">
	<c:param name="dispatch" value="summaryPrint" />
	<c:param name="sampleId" value="${sampleId}" />
	<c:param name="location" value="${location}" />
</c:url>
<c:url var="exportUrl" value="characterization.do">
	<c:param name="dispatch" value="summaryExport" />
	<c:param name="sampleId" value="${sampleId}" />
	<c:param name="location" value="${location}" />
</c:url>

<c:if test="${not empty theSample}">
	<jsp:include page="/bodyTitle.jsp">
		<jsp:param name="pageTitle"
			value="${fn:toUpperCase(location)} Sample ${theSample.domain.name} Characterization" />
		<jsp:param name="topic" value="char_all_tab_help" />
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
				href="javascript:showSummary('ALL', ${fn:length(characterizationTypes)})"
				title="All"><span>&nbsp;All</span>
			</a>
		</li>
		<c:forEach var="type" items="${characterizationTypes}" varStatus="ind">
			<li>
				<a
					href="javascript:showSummary('${ind.count}', ${fn:length(characterizationTypes)})"
					title="${type}"><span>${type}</span></a>
				<a href="javascript:printPage('${printUrl}&type=${type}')"
					id="printUrl${ind.count}" style="display: none;"></a>
				<a href="${exportUrl}&type=${type}" id="exportUrl${ind.count}"
					style="display: none;"></a>
			</li>
		</c:forEach>
		<li>
			<a href="characterization.do?dispatch=setupNew&sampleId=${sampleId }"><span>other</span>
			</a>
		</li>
	</ul>
</div>
<c:forEach var="type" items="${characterizationTypes}" varStatus="ind">
	<div class="shadetabs" id="summaryTab${ind.count}"
		style="display: none;">
		<ul>
			<li>
				<a
					href="javascript:showSummary('ALL',${fn:length(characterizationTypes)})"
					title="All"><span>&nbsp;All</span>
				</a>
			</li>
			<c:forEach var="type" items="${characterizationTypes}"
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
						href="javascript:showSummary('${ind2.count}', ${fn:length(characterizationTypes)})"
						title="${type}"><span>${type}</span> </a>
				</li>
			</c:forEach>
			<li>
				<a
					href="characterization.do?dispatch=setupNew&sampleId=${sampleId }"><span>other</span>
				</a>
			</li>
		</ul>
	</div>
</c:forEach>
<table class="summaryViewNoTop" width="100%">
	<%--
	<c:if test="${! empty characterizationTypes}">
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
			<c:forEach var="type" items="${characterizationTypes}"
				varStatus="ind">
				<table id="summarySectionHeader${ind.count}" width="100%"
					align="center" style="display: block" class="summaryViewHeader">
					<tr>
						<c:if
							test="${!empty characterizationSummaryView.type2CharacterizationNames[type]}">
							<td align="left">
								<b>${type}</b>
								<br />
								<c:forEach var="charName"
									items="${characterizationSummaryView.type2CharacterizationNames[type]}">
									<a href="#${charName}">${charName}
										(${characterizationSummaryView.charName2Counts[charName]})</a> &nbsp;
	            </c:forEach>
							</td>
						</c:if>
					</tr>
				</table>
				<div id="summaryHeaderSeparator${ind.count}">
				</div>
			</c:forEach>
			<br />
			<c:forEach var="type" items="${characterizationTypes}"
				varStatus="ind">
				<table id="summarySection${ind.count}" width="100%" align="center"
					style="display: block" class="summaryViewNoGrid">
					<tr>
						<th align="left">
							<span class="summaryViewHeading">${type}</span>&nbsp;&nbsp;
							<a
								href="characterization.do?dispatch=setupNew&sampleId=${sampleId}&charType=${type}"
								class="addlink"><img align="middle" src="images/btn_add.gif"
									border="0" /></a>
							<%--
							<c:if
								test="${!empty characterizationSummaryView.type2Characterizations[type]}">
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
									test="${!empty characterizationSummaryView.type2Characterizations[type] }">
									<c:forEach var="charName"
										items="${characterizationSummaryView.type2CharacterizationNames[type]}">
										<a name="${charName}"></a>
										<table width="99%" align="center" class="summaryViewNoGrid" bgcolor="#dbdbdb">
											<tr>
												<th align="left">
													${charName}
												</th>
											</tr>
											<tr>
												<td>
													<c:forEach var="charBean"
														items="${characterizationSummaryView.charName2Characterizations[charName]}" varStatus="charBeanInd">
														<%@ include
															file="shared/bodySingleCharacterizationSummaryEdit.jsp"%>
														<c:if
															test="${charBeanInd.count<fn:length(characterizationSummaryView.charName2Characterizations[charName])}">
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
										<br/>
									</c:forEach>
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