<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page
	import="gov.nih.nci.cananolab.service.sample.helper.CompositionServiceHelper"%>
<table class="summaryViewNoGrid" width="99%" align="center"
	bgcolor="#F5F5f5">
	<tr>
		<td></td>
		<td width="95%"></td>
		<td align="right">
			<a
				href="functionalizingEntity.do?dispatch=setupUpdate&sampleId=${sampleId}&dataId=${functionalizingEntity.domainEntity.id}">Edit</a>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Name
		</td>
		<td colspan="2">
			<c:choose>
				<c:when test="${!empty functionalizingEntity.name}">
													${functionalizingEntity.name}
													</c:when>
				<c:otherwise>
														N/A
												</c:otherwise>
			</c:choose>
		</td>
	</tr>

	<tr>
		<td class="cellLabel" width="10%">
			PubChem ID
		</td>
		<td colspan="2">
			<c:choose>
				<c:when
					test="${!empty functionalizingEntity.domainEntity.pubChemId &&
																functionalizingEntity.domainEntity.pubChemId != 0}">
					<c:set var="pubChemId"
						value="${functionalizingEntity.domainEntity.pubChemId}" />
					<c:set var="pubChemDS"
						value="${functionalizingEntity.domainEntity.pubChemDataSourceName}" />
					<a
						href='<%=CompositionServiceHelper.getPubChemURL(
							(String) pageContext.getAttribute("pubChemDS"),
							(Long) pageContext.getAttribute("pubChemId"))%>'
						target="caNanoLab - View PubChem">${pubChemId}</a>
													&nbsp;(${pubChemDS})
												</c:when>
				<c:otherwise>
													N/A
												</c:otherwise>
			</c:choose>
		</td>
	</tr>

	<tr>
		<td class="cellLabel" width="10%">
			Amount
		</td>
		<td colspan="2">
			<c:choose>
				<c:when test="${!empty functionalizingEntity.value}">
													${functionalizingEntity.value} ${functionalizingEntity.valueUnit}
													</c:when>
				<c:otherwise>
														N/A
												</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Molecular Formula
		</td>
		<td colspan="2" style="word-wrap: break-word; max-width: 280px;">
			<c:choose>
				<c:when
					test="${!empty functionalizingEntity.molecularFormulaDisplayName}">
													${functionalizingEntity.molecularFormulaDisplayName}
													</c:when>
				<c:otherwise>
														N/A
												</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<c:if test="${functionalizingEntity.withProperties }">
		<tr>
			<td class="cellLabel" width="10%">
				Properties
			</td>
			<td colspan="2">
				<%
					String detailPage = gov.nih.nci.cananolab.ui.sample.InitCompositionSetup
								.getInstance()
								.getDetailPage(
										(String) pageContext.getAttribute("entityType"),
										"functionalizingEntity");
						pageContext.setAttribute("detailPage", detailPage);
				%>
				<c:set var="functionalizingEntity" value="${functionalizingEntity}"
					scope="session" />
				<jsp:include page="${detailPage}">
					<jsp:param name="summary" value="true" />
				</jsp:include>
			</td>
		</tr>
	</c:if>
	<tr>
		<td class="cellLabel" width="10%">
			Function(s)
		</td>
		<td colspan="2">
			<c:choose>
				<c:when test="${!empty functionalizingEntity.functions}">
					<c:set var="entity" value="${functionalizingEntity }" />
					<%@ include file="bodyFunctionView.jsp"%>
				</c:when>
				<c:otherwise>
													N/A
											</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Activation Method
		</td>
		<td colspan="2">
			<c:choose>
				<c:when
					test="${!empty functionalizingEntity.activationMethodDisplayName}">
													${functionalizingEntity.activationMethodDisplayName}
													</c:when>
				<c:otherwise>
														N/A
												</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Description
		</td>
		<td colspan="2">
			<c:choose>
				<c:when test="${!empty fn:trim(functionalizingEntity.description)}">
					<c:out
						value="${fn:replace(functionalizingEntity.description, cr, '<br>')}"
						escapeXml="false" />
				</c:when>
				<c:otherwise>N/A
												</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Files
		</td>
		<td colspan="2">
			<c:choose>
				<c:when test="${! empty functionalizingEntity.files}">
					<c:set var="files" value="${functionalizingEntity.files }" />
					<c:set var="entityType" value="functionalizing entity" />
					<%@include file="../../bodyFileView.jsp"%>
				</c:when>
				<c:otherwise>
					N/A
					</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>