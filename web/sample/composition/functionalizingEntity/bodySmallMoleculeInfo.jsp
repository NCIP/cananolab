<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:choose>
	<c:when test="${param.summary eq 'true'}">
		<table class="summaryViewNoGrid" align="left" >
			<tr>
				<td class="cellLabel">
					Alternate Name
				</td>			
				<td>
					${functionalizingEntity.smallMolecule.alternateName}
				</td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
		<table width="100%" align="center" class="submissionView">
			<tr>
				<th colspan="4">
					Small Molecule Properties
				</th>
			</tr>
			<tr>
				<td class="cellLabel">
					<strong>Alternate Name</strong>
				</td>
				<td>
					<input type="text"
						name="functionalizingEntity.smallMolecule.alternateName" size="90" 
						value="${functionalizingEntity.smallMolecule.alternateName}" />
				</td>
			</tr>
		</table>
		<br>
	</c:otherwise>
</c:choose>