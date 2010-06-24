<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:url var="printUrl" value="characterization.do" scope="session">
	<c:param name="dispatch" value="summaryPrint" />
	<c:param name="sampleId" value="${sampleId}" />
</c:url>
<c:url var="exportUrl" value="characterization.do" scope="session">
	<c:param name="dispatch" value="summaryExport" />
	<c:param name="sampleId" value="${sampleId}" />
</c:url>

<c:if test="${not empty theSample}">
	<jsp:include page="/bodyTitle.jsp">
		<jsp:param name="pageTitle"
			value="Sample ${theSample.domain.name} Characterization" />
		<jsp:param name="topic" value="char_all_tab_help" />
		<jsp:param name="glossaryTopic" value="glossary_help" />
		<jsp:param name="printLink"	value="${printUrl}" />
		<jsp:param name="exportLink" value="${exportUrl}" />
	</jsp:include>
</c:if>
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
					title="${type}"><span>${type}</span>
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
						title="${type}"><span>${type}</span></a>
				</li>
			</c:forEach>
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
						<td align="left">
							<b>${type}</b>
							<br />
							<c:forEach var="charName"
								items="${characterizationSummaryView.type2CharacterizationNames[type]}">
								<a href="#${charName}">${charName}
									(${characterizationSummaryView.charName2Counts[charName]})</a> &nbsp;
	            </c:forEach>
						</td>
					</tr>
				</table>
				<div id="summaryHeaderSeparator${ind.count}">
				</div>
			</c:forEach>
			<br/>
			<jsp:include page="shared/bodyCharacterizationSummaryPrintViewTable.jsp" />
		</td>
	</tr>
</table>