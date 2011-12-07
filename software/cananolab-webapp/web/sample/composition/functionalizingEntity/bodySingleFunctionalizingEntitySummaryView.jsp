<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/sample/bodyHideSearchDetailView.jsp"%>
<table class="summaryViewNoGrid" width="99%" align="center"
	bgcolor="#F5F5f5">
	<c:if test="${!empty functionalizingEntity.name}">
		<tr>
			<td class="cellLabel" width="10%">
				Name
			</td>
			<td>
				<c:out value="${functionalizingEntity.name}" />
			</td>
		</tr>
	</c:if>
	<c:if test="${!empty functionalizingEntity.pubChemId}">
		<tr>
			<td class="cellLabel" width="10%">
				PubChem ID
			</td>
			<td>
				<a href="${functionalizingEntity.pubChemLink}"
					target="caNanoLab - View PubChem"><c:out value="${pubChemId}" />
				</a> &nbsp;(
				<c:out value="${pubChemDS}" />
				)
			</td>
		</tr>
	</c:if>
	<c:if test="${!empty functionalizingEntity.value}">
		<tr>
			<td class="cellLabel" width="10%">
				Amount
			</td>
			<td>
				<c:out value="${functionalizingEntity.value}" />
				<c:out value="${functionalizingEntity.valueUnit}" />
			</td>
		</tr>
	</c:if>
	<c:if
		test="${!empty functionalizingEntity.molecularFormulaDisplayName}">
		<tr>
			<td class="cellLabel" width="10%">
				Molecular Formula
			</td>
			<td style="word-wrap: break-word; max-width: 280px;">
				<c:out value="${functionalizingEntity.molecularFormulaDisplayName}" />
			</td>
		</tr>
	</c:if>
	<c:choose>
		<c:when test="${!empty entityDetailPage}">
			<tr>
				<td class="cellLabel" width="10%">
					Properties
				</td>
				<td>
					<c:set var="functionalizingEntity" value="${functionalizingEntity}"
						scope="session" />
					<jsp:include page="${entityDetailPage}">
						<jsp:param name="summary" value="true" />
					</jsp:include>
				</td>
			</tr>
		</c:when>
		<c:otherwise>
			<c:if test="${functionalizingEntity.withProperties }">
				<tr>
					<td class="cellLabel" width="10%">
						Properties
					</td>
					<td>
						<%
							String detailPage = gov.nih.nci.cananolab.ui.sample.InitCompositionSetup
												.getInstance().getDetailPage(
														(String) pageContext
																.getAttribute("entityType"),
														"functionalizingEntity");
										pageContext.setAttribute("detailPage", detailPage);
						%>
						<c:set var="functionalizingEntity"
							value="${functionalizingEntity}" scope="session" />
						<jsp:include page="${detailPage}">
							<jsp:param name="summary" value="true" />
						</jsp:include>
					</td>
				</tr>
			</c:if>
		</c:otherwise>
	</c:choose>
	<c:if test="${!empty functionalizingEntity.functions}">
		<tr>
			<td class="cellLabel" width="10%">
				Function(s)
			</td>
			<td>
				<c:set var="entity" value="${functionalizingEntity }" />
				<%@ include file="bodyFunctionView.jsp"%>
			</td>
		</tr>
	</c:if>
	<c:if
		test="${!empty functionalizingEntity.activationMethodDisplayName}">
		<tr>
			<td class="cellLabel" width="10%">
				Activation Method
			</td>
			<td>
				<c:out value="${functionalizingEntity.activationMethodDisplayName}" />
			</td>
		</tr>
	</c:if>
	<c:if test="${!empty fn:trim(functionalizingEntity.description)}">
		<tr>
			<td class="cellLabel" width="10%">
				Description
			</td>
			<td>
				<c:out value="${functionalizingEntity.descriptionDisplayName}"
					escapeXml="false" />
			</td>
		</tr>
	</c:if>
	<c:if test="${! empty functionalizingEntity.files}">
		<tr>
			<td class="cellLabel" width="10%">
				Files
			</td>
			<td>
				<c:set var="files" value="${functionalizingEntity.files }" />
				<c:set var="entityType" value="functionalizing entity" />
				<c:set var="downloadAction" value="composition" />
				<%@include file="../../bodyFileView.jsp"%>
			</td>
		</tr>
	</c:if>
</table>