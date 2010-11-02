<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<table align="left" class="invisibleTable">
	<tr>
		<td class="cellLabel">
			Primary Contact?
		</td>
		<td class="cellLabel">
			Contact Person
		</td>
		<td class="cellLabel">
			Organization
		</td>
		<td class="cellLabel">
			Role
		</td>
	</tr>
	<c:if
		test="${! empty sampleForm.map.sampleBean.primaryPOCBean.domain.id}">
		<c:set var="primaryPOC"
			value="${sampleForm.map.sampleBean.primaryPOCBean}" />
		<tr>
			<td>
				Yes
			</td>
			<td>
				<c:out value="${primaryPOC.personDisplayName}"/>
			</td>
			<td>
				<c:out value="${primaryPOC.organizationDisplayName}"/>
			</td>
			<td>
				<c:out value="${primaryPOC.domain.role}"/>
			</td>
		</tr>
	</c:if>
	<c:if test="${!empty sampleForm.map.sampleBean.otherPOCBeans}">
		<c:forEach var="poc"
			items="${sampleForm.map.sampleBean.otherPOCBeans}">
			<tr>
				<td>
					No
				</td>
				<td>
					<c:out value="${poc.personDisplayName}"/>
				</td>
				<td>
					<c:out value="${poc.organizationDisplayName}"/>
				</td>
				<td>
					<c:out value="${poc.domain.role}"/>
				</td>
			</tr>
		</c:forEach>
	</c:if>
</table>