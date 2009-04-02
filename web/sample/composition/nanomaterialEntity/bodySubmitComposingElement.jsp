<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="entity"
	value="${compositionForm.map.comp.nanomaterialEntities[param.entityIndex]}" />

<logic:iterate name="entity" property="composingElements"
	id="composingElement" indexId="ind">
	<table class="summaryViewLayer4" width="95%">
		<tr>
			<th>
				${composingElement.displayName}
			</th>
		</tr>
		<c:choose>
			<c:when test="${!empty composingElement.molecularFormulaDisplayName}">
				<tr>
					<td>
						Molecular Formula: ${composingElement.molecularFormulaDisplayName}
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:if test="${param.dispatch ne 'summaryView'}">
					<tr>
						<td>
							Molecular Formula: N/A
						</td>
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
							items="${composingElement.functionDisplayNames}">
				${function}
				<br>
						</c:forEach>
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:if test="${param.dispatch ne 'summaryView'}">
					<tr>
						<td>
							Function: N/A
						</td>
					</tr>
				</c:if>
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when
				test="${!empty composingElement.domain.description}">
				<tr>
					<td>
						Description:
						${composingElement.domain.description}
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:if test="${param.dispatch ne 'summaryView'}">
					<tr>
						<td>
							Description: N/A
						</td>
					</tr>
				</c:if>
			</c:otherwise>
		</c:choose>
	</table>
	<br>
</logic:iterate>
