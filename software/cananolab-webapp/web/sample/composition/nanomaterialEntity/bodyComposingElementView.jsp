<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<table class="summaryViewNoGrid" width="99%" align="left">
	<c:forEach var="composingElement" items="${entity.composingElements}">
		<tr>
			<td class="cellLabel" colspan="2">
				<c:out value="${composingElement.displayName}"/>
			</td>
		</tr>
		<c:if test="${!empty composingElement.pubChemLink}">
			<tr>
				<td>
					PubChem ID
				</td>
				<td>
					<a href="${composingElement.pubChemLink}"
						target="caNanoLab - View PubChem"><c:out value="${composingElement.domain.pubChemId}"/></a>
					&nbsp;(<c:out value="${composingElement.domain.pubChemDataSourceName}"/>)
				</td>
			</tr>
		</c:if>
		<c:if test="${!empty composingElement.molecularFormulaDisplayName}">
			<tr>

				<td>
					Molecular Formula
				</td>
				<td>
					<c:out value="${composingElement.molecularFormulaDisplayName}"/>
				</td>
			</tr>
		</c:if>

		<c:if test="${!empty composingElement.domain.description}">
			<tr>
				<td>
					Description
				</td>
				<td>
					<c:set var="desc" value="${fn:replace(composingElement.domain.description, '<', '&lt;')}" />
					<c:out
						value="${fn:replace(desc, cr, '<br>')}"
						escapeXml="false" />
				</td>
			</tr>
		</c:if>
		<c:if test="${!empty composingElement.functionDisplayNames}">
			<tr>
				<td>
					Function
				</td>
				<td>
					<c:forEach var="function"
						items="${composingElement.functionDisplayNames}" varStatus="ind">
							<c:out value="${function}"/> <br/>							
					</c:forEach>
				</td>
			</tr>
		</c:if>
		<tr>
			<td class="cellLabel" colspan="2" height="5"></td>
		</tr>
	</c:forEach>
</table>
