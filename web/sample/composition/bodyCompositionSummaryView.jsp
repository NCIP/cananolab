<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	String[] compositionSections = { "Nanomaterial Entity",
			"Functionalizing Entity", "Chemical Association",
			"Composition File" };
	pageContext
			.setAttribute("compositionSections", compositionSections);
%>

<c:set var="sectionTitles" value="" />

<div class="animatedtabs" id="summaryTabALL">
	<ul>
		<li class="selected">
			<a href="javascript:showSummary('ALL', ${fn:length(compositionSections)})" title="All"><span>All</span>
			</a>
		</li>
		<c:forEach var="type" items="${compositionSections}" varStatus="ind">
			<li>
				<a href="javascript:showSummary('${ind.count}', ${fn:length(compositionSections)})" title="${type}">
					<span>${type}</span> </a>
			</li>
		</c:forEach>
	</ul>
</div>
<c:forEach var='item' begin='1' end='4'>
	<div class="animatedtabs" id="summaryTab${item}" style="display: none;">
		<ul>
			<li>
				<a href="javascript:showSummary('ALL', ${fn:length(compositionSections)})" title="All"><span>All</span>
				</a>
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
					<a href="javascript:showSummary('${ind.count}', ${fn:length(compositionSections)})" title="${type}">
						<span>${type}</span> </a>
				</li>
			</c:forEach>
		</ul>
	</div>
</c:forEach>
<jsp:include page="nanomaterialEntity/bodyNanomaterialEntityView.jsp">
	<jsp:param name="sampleId" value="${param.sampleId}" />
</jsp:include>
<jsp:include
	page="functionalizingEntity/bodyFunctionalizingEntityView.jsp">
	<jsp:param name="sampleId" value="${param.sampleId}" />
</jsp:include>
<jsp:include page="bodyChemicalAssociationView.jsp">
	<jsp:param name="sampleId" value="${param.sampleId}" />
</jsp:include>
<jsp:include page="bodyCompositionFileView.jsp">
	<jsp:param name="sampleId" value="${param.sampleId}" />
</jsp:include>



