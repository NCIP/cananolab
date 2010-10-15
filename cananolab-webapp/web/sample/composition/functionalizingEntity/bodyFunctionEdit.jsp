<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>

<table class="editTableWithGrid" align="center" width="95%">
	<tr>
		<th>
			Type
		</th>
		<c:if test="${entity.withImagingFunction eq 'true'}">
			<th class="cellLabel">
				Image Modality
			</th>
		</c:if>
		<c:if test="${entity.withTargetingFunction eq 'true'}">
			<th class="cellLabel">
				Targets
			</th>
		</c:if>
		<th>
			Description
		</th>
		<th>
		</th>
	</tr>
	<c:forEach var="function" items="${entity.functions}">
		<tr>
			<td>
				${function.type}
			</td>
			<c:if test="${entity.withImagingFunction eq 'true'}">
				<td>
					<c:if test="${! empty function.imagingFunction.modality }">
					${function.imagingFunction.modality}
				</c:if>
				</td>
			</c:if>
			<c:if test="${entity.withTargetingFunction eq 'true'}">
				<td>
					<c:if test="${! empty function.targetDisplayNames}">
						<c:forEach var="targetDisplayName"
							items="${function.targetDisplayNames}">
							${targetDisplayName}<br><br>
						</c:forEach>
					</c:if>
				</td>
			</c:if>
			<td>
				<c:if test="${! empty function.description}">
				<c:set var="desc" value="${fn:replace(function.description, '<', '&lt;')}" />
				<c:out
					value="${fn:replace(desc, cr, '<br>')}"
					escapeXml="false" />
			</c:if>
			</td>
			<c:if test="${edit eq 'true'}">
				<td align="right">
					<a href="javascript:setTheFunction(${function.domainFunction.id});">Edit</a>&nbsp;
				</td>
			</c:if>
		</tr>
	</c:forEach>
</table>
