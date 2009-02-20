<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="entity"
	value="${compositionForm.map.comp.nanoparticleEntities[param.entityIndex]}" />

<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td valign="top">
			Polymer Name:&nbsp;${entity.emulsion.polymerName} &nbsp;
			<c:choose>
				<c:when test="${entity.emulsion.polymerized eq 'true' }">
					(Polymerized)
				</c:when>
				<c:otherwise>
					(Not Polymerized)
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>