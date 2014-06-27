<%--L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="editButton">
<a href="nanomaterialEntity.do?dispatch=setupUpdate&sampleId=${sampleId}&dataId=${nanomaterialEntity.domainEntity.id}">Edit</a>
</div>
<table class="summaryViewNoGrid" width="99%" align="center"
	bgcolor="#F5F5f5">
	<tr>
		<th class="cellLabel" width="10%" scope="row">
			Description
		</th>
		<td>
			<c:choose>
				<c:when test="${!empty fn:trim(nanomaterialEntity.description)}">
					<c:out value="${nanomaterialEntity.descriptionDisplayName}" escapeXml="false" />
				</c:when>
				<c:otherwise>N/A
												</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<c:choose>
		<c:when test="${!empty entityDetailPage}">
			<tr>
				<th class="cellLabel" width="10%" scope="row">
					Properties
				</th>
				<td>
					<c:set var="nanomaterialEntity" value="${nanomaterialEntity}"
						scope="session" />
					<jsp:include page="${entityDetailPage}">
						<jsp:param name="summary" value="true" />
					</jsp:include>
				</td>
			</tr>
		</c:when>
		<c:otherwise>
			<c:if test="${nanomaterialEntity.withProperties }">
				<tr>
					<th class="cellLabel" width="10%" scope="row">
						Properties
					</th>
					<td>
						<%
							String detailPage = gov.nih.nci.cananolab.restful.sample.InitCompositionSetup
																.getInstance().getDetailPage(
																		(String) pageContext
																				.getAttribute("entityType"),
																		"nanomaterialEntity");
														pageContext.setAttribute("detailPage", detailPage);
						%>
						<c:set var="nanomaterialEntity" value="${nanomaterialEntity}"
							scope="session" />
						<jsp:include page="${detailPage}">
							<jsp:param name="summary" value="true" />
						</jsp:include>
					</td>
				</tr>
			</c:if>
		</c:otherwise>
	</c:choose>
	<tr>
		<td class="cellLabel" width="10%">
			Composing Elements
		</td>
		<td>
			<c:set var="entity" value="${nanomaterialEntity}" />
			<%@include file="bodyComposingElementView.jsp"%>
		</td>
	</tr>
	<tr>
		<th class="cellLabel" width="10%" scope="row">
			Files
		</th>
		<td>
			<c:choose>
				<c:when test="${! empty nanomaterialEntity.files}">
					<c:set var="files" value="${nanomaterialEntity.files }" />
					<c:set var="entityType" value="nanomaterial entity" />
					<c:set var="downloadAction" value="composition" />
					<%@include file="../../bodyFileView.jsp"%>
				</c:when>
				<c:otherwise>
					N/A
					</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
