<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="gov.nih.nci.cananolab.service.sample.helper.CompositionServiceHelper"%>

<table class="summaryViewNoGrid" width="99%" align="left">
	<c:forEach var="composingElement" items="${entity.composingElements}">
		<tr>
			<td class="cellLabel" colspan="2">
				${composingElement.displayName}
			</td>			
		</tr>		
		<c:choose>
			<c:when test="${!empty composingElement.domain.pubChemId &&
							composingElement.domain.pubChemId != 0}">
				<tr>
					<td>
						<c:set var="pubChemId" value="${composingElement.domain.pubChemId}"/>
						<c:set var="pubChemDS" value="${composingElement.domain.pubChemDataSourceName}"/>
						PubChem ID
					</td>
					<td><a href='<%=CompositionServiceHelper.getPubChemURL((String)pageContext.getAttribute("pubChemDS"), (Long)pageContext.getAttribute("pubChemId"))%>' target="caNanoLab - View PubChem">${pubChemId}</a>
						&nbsp;(${pubChemDS})</td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:if test="${param.dispatch ne 'summaryView'}">
					<tr>
						<td>
							PubChem ID
						</td>
						<td>N/A</td>
					</tr>
				</c:if>
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${!empty composingElement.molecularFormulaDisplayName}">
				<tr><%--
					<td style="word-wrap:break-word;max-width:280px;">
					--%>
					<td>
						Molecular Formula
					</td>
					<td>${composingElement.molecularFormulaDisplayName}</td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:if test="${param.dispatch ne 'summaryView'}">
					<tr>
						<td>
							Molecular Formula
						</td>
						<td>N/A</td>
					</tr>
				</c:if>
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${!empty composingElement.domain.description}">
				<tr>
					<td>
						Description
					</td>
					<td><c:out
							value="${fn:replace(composingElement.domain.description, cr, '<br>')}"
							escapeXml="false" /></td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:if test="${param.dispatch ne 'summaryView'}">
					<tr>
						<td>
							Description
						</td>
						<td>N/A</td>
					</tr>
				</c:if>
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${!empty composingElement.functionDisplayNames}">
				<tr>
					<td>
						Function
					</td>
					<td>
						<c:forEach var="function"
							items="${composingElement.functionDisplayNames}" varStatus="ind">
							${function}
							<c:if
								test="${ind.count !=fn:length(composingElement.functionDisplayNames)}">
							;&nbsp;</c:if>
						</c:forEach>
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:if test="${param.dispatch ne 'summaryView'}">
					<tr>
						<td>
							Function
						</td>
						<td>N/A</td>
					</tr>
				</c:if>
			</c:otherwise>
		</c:choose>
		<tr>
			<td class="cellLabel" colspan="2"></td>			
		</tr>		
	</c:forEach>
</table>
