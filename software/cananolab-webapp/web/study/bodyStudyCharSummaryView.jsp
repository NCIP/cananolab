<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>
<c:url var="printUrl" value="studyCharacterization.do" scope="session">
	<c:param name="dispatch" value="summaryPrint" />
	<c:param name="studyId" value="${studyId}" />
</c:url>
<c:url var="exportUrl" value="studyCharacterization.do" scope="session">
	<c:param name="dispatch" value="summaryExport" />
	<c:param name="studyId" value="${studyId}" />
</c:url>

	<jsp:include page="/bodyTitle.jsp">
		<jsp:param name="pageTitle"
			value="Characterizations in Study ${studyName}" />
		<jsp:param name="topic" value="char_all_tab_help" />
		<jsp:param name="glossaryTopic" value="glossary_help" />
		<jsp:param name="printLink"	value="${printUrl}" />
		<jsp:param name="exportLink" value="${exportUrl}" />
	</jsp:include>

<jsp:include page="/bodyMessage.jsp?bundle=sample" />
<div class="shadetabs" id="summaryTabALL">
	<ul>
		<li class="selected">
			<a	href="javascript:showSummary('ALL', ${fn:length(characterizationTypes)})"
				title="All"><span>All</span></a>
		</li>
		<c:forEach var="type" items="${characterizationTypes}" varStatus="ind">
			<li>
				<a
					href="javascript:showSummary('${ind.count}', ${fn:length(characterizationTypes)})"
					title="${type}"><span><c:out value="${type}"/></span>
				</a>
				<a href="javascript:printPage('${printUrl}&type=${type}')"
					id="printUrl${ind.count}" style="display: none;"></a>
				<a href="${exportUrl}&type=${type}" id="exportUrl${ind.count}"
					style="display: none;"></a>
			</li>
		</c:forEach>
	</ul>
</div>
<c:forEach var="type" items="${characterizationTypes}" varStatus="ind">
	<div class="shadetabs" id="summaryTab${ind.count}"
		style="display: none;">
		<ul>
			<li>
				<a	href="javascript:showSummary('ALL',${fn:length(characterizationTypes)})"
					title="All"><span>All</span></a>
			</li>
			<c:forEach var="type" items="${characterizationTypes}" varStatus="ind2">
				<c:choose>
					<c:when test="${ind.count eq ind2.count }">
						<c:set var="selectedClass" value="selected" />
					</c:when>
					<c:otherwise>
						<c:set var="selectedClass" value="" />
					</c:otherwise>
				</c:choose>
				<li class="${selectedClass}">
					<a	href="javascript:showSummary('${ind2.count}', ${fn:length(characterizationTypes)})"
						title="${type}"><span><c:out value="${type}"/></span></a>
				</li>
			</c:forEach>
		</ul>
	</div>
</c:forEach>
<table class="summaryViewNoTop" width="100%">

	<tr>
		<td>
			<c:forEach var="type" items="${characterizationTypes}"
				varStatus="ind">
				<table id="summarySectionHeader${ind.count}" width="100%"
					align="center" style="display: block" class="summaryViewHeader">
					<tr>
						<td align="left">
							<b>${type}</b>
							<br />
							<c:forEach var="charName"
								items="${studyCharacterizationSummaryView.type2CharacterizationNames[type]}">
								<a href="#${charName}"><c:out value="${charName}"/>
									(<c:out value="${studyCharacterizationSummaryView.charName2Counts[charName]}"/>)</a> &nbsp;
	            			</c:forEach>
						</td>
					</tr>
				</table>
				<div id="summaryHeaderSeparator${ind.count}">
				</div>
			</c:forEach>
			<br/>
			<c:forEach var="type" items="${characterizationTypes}" varStatus="ind">
				<table id="summarySection${ind.count}" width="100%" align="center"
					style="display: block" class="summaryViewNoGrid">
					<tr>
						<th align="left">
							<span class="summaryViewHeading">${type}</span>
						</th>
					</tr>
					<tr>
						<td>
							<c:forEach var="charName"
								items="${studyCharacterizationSummaryView.type2CharacterizationNames[type]}">
								<a name="${charName}"></a>
								<table width="99%" align="center" class="summaryViewNoGrid"
									bgcolor="#dbdbdb">
									<tr>
										<th align="left">
											${charName}
										</th>
									</tr>
									<tr>
										<td>
											<c:forEach var="charBean"
												items="${studyCharacterizationSummaryView.charName2Characterizations[charName]}"
												varStatus="charBeanInd">
												<%@ include file="bodySingleCharacterizationSummaryView.jsp"%>
												<c:if
													test="${charBeanInd.count<fn:length(studyCharacterizationSummaryView.charName2Characterizations[charName])}">
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
						</td>
					</tr>
					<tr>
						<th valign="top" align="left" height="6">
						</th>
					</tr>
				</table>
				<div id="summarySeparator${ind.count}">
					<br>
				</div>
			</c:forEach>
			<jsp:include page="shared/bodyCharacterizationSummaryPrintViewTable.jsp" />
		</td>
	</tr>
</table>
