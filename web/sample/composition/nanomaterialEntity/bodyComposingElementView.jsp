<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<logic:iterate name="nanomaterialEntity" property="composingElements"
	id="composingElement" indexId="ind">
	<table class="summaryViewLayer4" width="95%" align="center">
		<tr>
			<th>
				${composingElement.displayName}
			</th>
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
							items="${composingElement.functionDisplayNames}">
				${function}
				<br>
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
</logic:iterate>
