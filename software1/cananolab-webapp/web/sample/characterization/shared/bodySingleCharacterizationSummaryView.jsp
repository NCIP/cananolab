<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/sample/bodyHideSearchDetailView.jsp"%>
<c:set var="charObj" value="${charBean.domainChar}" />
<c:set var="charName" value="${charBean.characterizationName}" />
<c:set var="charType" value="${charBean.characterizationType}" />

<table class="summaryViewNoGrid" width="99%" align="center"
	bgcolor="#F5F5f5">
	<c:choose>
		<c:when test="${!empty charObj.assayType}">
			<tr>
				<td class="cellLabel" width="10%">
					Assay Type
				</td>
				<td>
					<c:out value="${charObj.assayType}" />
				</td>
			</tr>
		</c:when>
		<c:otherwise>
			<c:if
				test="${'physico chemical characterization' eq charBean.characterizationType}">
				<tr>
					<td class="cellLabel" width="10%">
						Assay Type
					</td>
					<td>
						<c:out value="${charName}" />
					</td>
				</tr>
			</c:if>
		</c:otherwise>
	</c:choose>
	<c:if test="${!empty charBean.pocBean.displayName}">
		<tr>
			<td class="cellLabel" width="10%">
				Point of Contact
			</td>
			<td>
				<c:out value="${charBean.pocBean.displayName}" />
			</td>
		</tr>
	</c:if>
	<c:if test="${!empty charBean.dateString}">
		<tr>
			<td class="cellLabel" width="10%">
				Characterization Date
			</td>
			<td>
				<c:out value="${charBean.dateString}" />
			</td>
		</tr>
	</c:if>
	<c:if test="${!empty charBean.protocolBean.displayName}">
		<tr>
			<td class="cellLabel" width="10%">
				Protocol
			</td>
			<td>
				<c:out value="${charBean.protocolBean.displayName}" />
			</td>
		</tr>
	</c:if>
	<c:if test="${charBean.withProperties }">
		<tr>
			<td class="cellLabel" width="10%">
				Properties
			</td>
			<td>
				<%
					String detailPage = gov.nih.nci.cananolab.ui.sample.InitCharacterizationSetup
								.getInstance().getDetailPage(
										(String) pageContext.getAttribute("charType"),
										(String) pageContext.getAttribute("charName"));
						pageContext.setAttribute("detailPage", detailPage);
				%>
				<c:set var="charBean" value="${charBean}" scope="session" />
				<jsp:include page="${detailPage}">
					<jsp:param name="summary" value="true" />
				</jsp:include>
			</td>
		</tr>
	</c:if>
	<c:if test="${!empty fn:trim(charObj.designMethodsDescription)}">
		<tr>
			<td class="cellLabel" width="10%">
				Design Description
			</td>
			<td>
				<c:choose>
					<c:when test="${!empty fn:trim(charObj.designMethodsDescription)}">
						<c:out value="${charBean.descriptionDisplayName}" escapeXml="false" />
					</c:when>
					<c:otherwise>
						N/A
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</c:if>
	<c:if test="${!empty charBean.experimentConfigs}">
		<tr>
			<td class="cellLabel" width="10%">
				Experiment Configurations
			</td>
			<td>
				<%@ include file="bodyExperimentConfigView.jsp"%>
			</td>
		</tr>
	</c:if>
	<c:if test="${!empty charBean.findings}">
		<tr>
			<td class="cellLabel" width="10%">
				Characterization Results
			</td>
			<td>
				<%@ include file="bodyFindingView.jsp"%>
			</td>
		</tr>
	</c:if>
	<c:if test="${!empty charBean.conclusion}">
		<tr>
			<td class="cellLabel" width="10%">
				Analysis and Conclusion
			</td>
			<td>
				<c:out value="${charBean.conclusion}" />
			</td>
		</tr>
	</c:if>
</table>