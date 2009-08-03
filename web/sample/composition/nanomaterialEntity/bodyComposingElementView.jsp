<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="gov.nih.nci.cananolab.service.sample.helper.CompositionServiceHelper"%>

<c:forEach var="composingElement" items="${entity.composingElements}">
	<table class="summaryViewLayer4" width="95%" align="center">
		<tr>
			<td class="cellLabel">
				${composingElement.displayName}
			</td>
			<c:choose>
				<c:when test="${edit eq 'true'}">
					<td align="right" width="3%">
						<a href="#submitComposingElement"
							onclick="javascript:setTheComposingElement(${composingElement.domain.id});">Edit</a>&nbsp;
					</td>
				</c:when>
				<c:otherwise>
					<td></td>
				</c:otherwise>
			</c:choose>
		</tr>
		<c:choose>
			<c:when test="${!empty composingElement.domain.pubChemId &&
							composingElement.domain.pubChemId != 0}">
				<tr>
					<td>
						<c:set var="pubChemId" value="${composingElement.domain.pubChemId}"/>
						<c:set var="pubChemDS" value="${composingElement.domain.pubChemDataSourceName}"/>
						PubChem ID: 
						<a href='<%=CompositionServiceHelper.getPubChemURL((String)pageContext.getAttribute("pubChemDS"), (Long)pageContext.getAttribute("pubChemId"))%>' target="caNanoLab - View PubChem">${pubChemId}</a>
						&nbsp;(${pubChemDS})
					</td>
					<td></td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:if test="${param.dispatch ne 'summaryView'}">
					<tr>
						<td>
							PubChem: N/A
						</td>
						<td></td>
					</tr>
				</c:if>
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${!empty composingElement.molecularFormulaDisplayName}">
				<tr>
					<td>
						Molecular Formula: ${composingElement.molecularFormulaDisplayName}
					</td>
					<td></td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:if test="${param.dispatch ne 'summaryView'}">
					<tr>
						<td>
							Molecular Formula: N/A
						</td>
						<td></td>
					</tr>
				</c:if>
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${!empty composingElement.functionDisplayNames}">
				<tr>
					<td>
						Function:
						<c:forEach var="function"
							items="${composingElement.functionDisplayNames}" varStatus="ind">
				${function}
				<c:if test="${ind.count !=fn:length(composingElement.functionDisplayNames)}">
;&nbsp;</c:if>
						</c:forEach>
					</td>
					<td></td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:if test="${param.dispatch ne 'summaryView'}">
					<tr>
						<td>
							Function: N/A
						</td>
						<td></td>
					</tr>
				</c:if>
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${!empty composingElement.domain.description}">
				<tr>
					<td>
						Description: ${composingElement.domain.description}
					</td>
					<td></td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:if test="${param.dispatch ne 'summaryView'}">
					<tr>
						<td>
							Description: N/A
						</td>
						<td></td>
					</tr>
				</c:if>
			</c:otherwise>
		</c:choose>
	</table>
	<br>
</c:forEach>

<%-- use this format in export instead
<table class="summaryViewLayer4" width="95%" align="center">
	<tr>
		<th>
			Type
		</th>
		<th>
			Name
		</th>
		<th>
			Amount
		</th>
		<th>
			Molecular Formula
		</th>
		<th>
			Function
		</th>
		<th>
			Description
		</th>
		<th></th>
	</tr>
	<logic:iterate name="nanomaterialEntity" property="composingElements"
		id="composingElement" indexId="ind">
		<tr>
			<td>
				${composingElement.domain.type}
			</td>
			<td>
				${composingElement.domain.name}
			</td>
			<td>
				${composingElement.domain.value}
				${composingElement.domain.valueUnit}
			</td>
			<td>
				<c:choose>
					<c:when
						test="${!empty composingElement.molecularFormulaDisplayName}">
						${composingElement.molecularFormulaDisplayName}
				</c:when>
					<c:otherwise>
						<c:if test="${param.dispatch ne 'summaryView'}">
								N/A
						</c:if>
					</c:otherwise>
				</c:choose>
			</td>
			<td>
				<c:choose>
					<c:when test="${!empty composingElement.functionDisplayNames}">
						<c:forEach var="function"
							items="${composingElement.functionDisplayNames}">
				${function}
				<br>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<c:if test="${param.dispatch ne 'summaryView'}">
							N/A
					</c:if>
					</c:otherwise>
				</c:choose>
			</td>
			<td>
				<c:choose>
					<c:when test="${!empty composingElement.domain.description}">
										${composingElement.domain.description}
								</c:when>
					<c:otherwise>
						<c:if test="${param.dispatch ne 'summaryView'}">
											N/A
									</c:if>
					</c:otherwise>
				</c:choose>
			</td>
			<c:choose>
				<c:when test="${edit eq 'true'}">
					<td align="right" width="3%">
						<a
							href="javascript:setTheComposingElement(${composingElement.domain.id});">Edit</a>&nbsp;
					</td>
				</c:when>
				<c:otherwise>
					<td></td>
				</c:otherwise>
			</c:choose>
		</tr>
	</logic:iterate>
</table>
<br>
--%>