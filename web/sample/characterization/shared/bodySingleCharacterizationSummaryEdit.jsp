<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/sample/bodyHideAdvancedSearchDetailView.jsp"%>
<c:set var="charObj" value="${charBean.domainChar}" />
<c:set var="charName" value="${charBean.characterizationName}" />
<c:set var="charType" value="${charBean.characterizationType}" />

<table class="summaryViewNoGrid" width="99%" align="center"
	bgcolor="#F5F5f5">
	<tr>		
		<td></td><td width="95%"></td><td align="right">
			<a
				href="characterization.do?dispatch=setupUpdate&sampleId=${sampleId}&charId=${charBean.domainChar.id}&charClassName=${charBean.className}&charType=${charBean.characterizationType}">Edit</a>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Assay Type
		</td>
		<td colspan="2">
			<c:choose>
				<c:when test="${!empty charObj.assayType}">
																${charObj.assayType}
															</c:when>
				<c:otherwise>
					<c:choose>
						<c:when
							test="${charBean.characterizationType eq 'physico chemical characterization'}">
																	${charName}
																</c:when>
						<c:otherwise>N/A</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Point of Contact
		</td>
		<td colspan="2">
			<c:choose>
				<c:when test="${!empty charBean.pocBean.displayName}">
																${charBean.pocBean.displayName}
															</c:when>
				<c:otherwise>
															N/A
															</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Characterization Date
		</td>
		<td colspan="2">
			<c:choose>
				<c:when test="${!empty charBean.dateString}">
																${charBean.dateString}
															</c:when>
				<c:otherwise>
															N/A
															</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Protocol
		</td>
		<td colspan="2">
			<c:choose>
				<c:when test="${!empty charBean.protocolBean.displayName}">
																${charBean.protocolBean.displayName}
																</c:when>
				<c:otherwise>
																N/A
																</c:otherwise>
			</c:choose>

		</td>
	</tr>
	<c:if test="${charBean.withProperties }">
		<tr>
			<td class="cellLabel" width="10%">
				Properties
			</td>
			<td colspan="2">
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
	<tr>
		<td class="cellLabel" width="10%">
			Design Description
		</td>
		<td colspan="2">
			<c:choose>
				<c:when test="${!empty fn:trim(charObj.designMethodsDescription)}">
					<c:out
						value="${fn:replace(charObj.designMethodsDescription, cr, '<br>')}"
						escapeXml="false" />
				</c:when>
				<c:otherwise>N/A
															</c:otherwise>
			</c:choose>
		</td>
	</tr>

	<tr>
		<td class="cellLabel" width="10%">
			Techniques and Instruments
		</td>
		<td colspan="2">
			<c:choose>
				<c:when test="${!empty charBean.experimentConfigs}">
					<%@ include file="bodyExperimentConfigView.jsp"%>
				</c:when>
				<c:otherwise>N/A
																</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Characterization Results
		</td>
		<td colspan="2">
			<c:choose>
				<c:when test="${!empty charBean.findings}">
					<%@ include file="bodyFindingView.jsp"%>
				</c:when>
				<c:otherwise>N/A</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Analysis and Conclusion
		</td>
		<td colspan="2">
			<c:choose>
				<c:when test="${!empty charBean.conclusion}">${charBean.conclusion}															</c:when>
				<c:otherwise>N/A</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>