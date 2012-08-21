<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<table class="summaryViewNoGrid" width="99%" align="left">
	<c:forEach var="composingElement" items="${entity.composingElements}">
		<tr>
			<th class="cellLabel" colspan="2" scope="row">
				<c:out value="${composingElement.displayName}"/>
			</th>
		</tr>
		<c:if test="${!empty composingElement.pubChemLink}">
			<tr>
				<th scope="row">
					PubChem ID
				</th>
				<td>
					<a href="${composingElement.pubChemLink}"
						target="caNanoLab - View PubChem"><c:out value="${composingElement.domain.pubChemId}"/></a>
					&nbsp;(<c:out value="${composingElement.domain.pubChemDataSourceName}"/>)
				</td>
			</tr>
		</c:if>
		<c:if test="${!empty composingElement.molecularFormulaDisplayName}">
			<tr>

				<th scope="row">
					Molecular Formula
				</th>
				<td>
					<c:out value="${composingElement.molecularFormulaDisplayName}"/>
				</td>
			</tr>
		</c:if>

		<c:if test="${!empty composingElement.domain.description}">
			<tr>
				<th scope="row">
					Description
				</th>
				<td>
					<c:out value="${composingElement.description}" escapeXml="false" />
				</td>
			</tr>
		</c:if>
		<c:if test="${!empty composingElement.functionDisplayNames}">
			<tr>
				<th scope="row">
					Function
				</th>
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
