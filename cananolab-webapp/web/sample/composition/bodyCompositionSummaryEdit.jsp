<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:url var="printUrl" value="composition.do">
	<c:param name="dispatch" value="summaryPrint" />
	<c:param name="sampleId" value="${sampleId}" />
</c:url>
<c:url var="exportUrl" value="composition.do">
	<c:param name="dispatch" value="summaryExport" />
	<c:param name="sampleId" value="${sampleId}" />
</c:url>
<c:set var="compositionSections" value="${allCompositionSections}" />
<c:if test="${not empty theSample}">
	<jsp:include page="/bodyTitle.jsp">
		<jsp:param name="pageTitle"
			value="Sample ${theSample.domain.name} Composition" />
		<jsp:param name="topic" value="composition_all_tab_help" />
		<jsp:param name="glossaryTopic" value="glossary_help" />
		<jsp:param name="printLink" value="${printUrl}" />
		<jsp:param name="exportLink" value="${exportUrl}" />
	</jsp:include>
</c:if>
<c:set var="sectionTitles" value="" />
<jsp:include page="/bodyMessage.jsp?bundle=sample" />
<div class="shadetabs" id="summaryTabALL">
	<ul>
		<li class="selected">
			<a
				href="javascript:showSummary('ALL', ${fn:length(compositionSections)})"
				title="All"><span>All</span> </a>
		</li>
		<c:forEach var="type" items="${compositionSections}" varStatus="ind">
			<li>
				<a
					href="javascript:showSummary('${ind.count}', ${fn:length(compositionSections)})"
					title="${type}"> <span><c:out value="${type}"/></span> </a>
				<a href="javascript:printPage('${printUrl}&type=${type}')"
					id="printUrl${ind.count}" style="display: none;"></a>
				<a href="${exportUrl}&type=${type}" id="exportUrl${ind.count}"
					style="display: none;"></a>
			</li>
		</c:forEach>
	</ul>
</div>
<c:forEach var='item' begin='1' end='4'>
	<div class="shadetabs" id="summaryTab${item}" style="display: none;">
		<ul>
			<li>
				<a
					href="javascript:showSummary('ALL', ${fn:length(compositionSections)})"
					title="All"><span>All</span> </a>
			</li>
			<c:forEach var="type" items="${compositionSections}" varStatus="ind">
				<c:choose>
					<c:when test="${item eq ind.count }">
						<c:set var="selectedClass" value="selected" />
					</c:when>
					<c:otherwise>
						<c:set var="selectedClass" value="" />
					</c:otherwise>
				</c:choose>
				<li class="${selectedClass}">
					<a
						href="javascript:showSummary('${ind.count}', ${fn:length(compositionSections)})"
						title="${type}"> <span><c:out value="${type}"/></span> </a>
				</li>
			</c:forEach>
		</ul>
	</div>
</c:forEach>
<table class="summaryViewNoTop" width="100%">
<%--
	<c:if
		test="${!empty compositionForm.map.comp.nanomaterialEntities ||
				!empty compositionForm.map.comp.functionalizingEntities ||
				!empty compositionForm.map.comp.chemicalAssociations ||
				!empty compositionForm.map.comp.files}">
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
			<jsp:include
				page="nanomaterialEntity/bodyNanomaterialEntitySummaryEdit.jsp">
				<jsp:param name="sampleId" value="${param.sampleId}" />
			</jsp:include>
			<jsp:include
				page="functionalizingEntity/bodyFunctionalizingEntitySummaryEdit.jsp">
				<jsp:param name="sampleId" value="${param.sampleId}" />
			</jsp:include>
			<jsp:include page="bodyChemicalAssociationSummaryEdit.jsp">
				<jsp:param name="sampleId" value="${param.sampleId}" />
			</jsp:include>
			<jsp:include page="bodyCompositionFileSummaryEdit.jsp">
				<jsp:param name="sampleId" value="${param.sampleId}" />
			</jsp:include>
		</td>
	</tr>
</table>