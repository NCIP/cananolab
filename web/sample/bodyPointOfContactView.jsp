<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="primaryPOC"
	value="${sampleForm.map.sampleBean.primaryPOCBean}" />
<c:choose>
	<c:when
		test="${empty primaryPOC.domain.organization.name && empty sampleForm.map.sampleBean.otherPOCBeans}">
	N/A
	</c:when>
	<c:otherwise>
		<table class="editTableWithGrid" width="95%" align="center">
			<tr>
				<th>
					Primary Contact?
				</th>
				<th>
					Contact Person
				</th>
				<th>
					Organization
				</th>
				<th>
					Role
				</th>
				<th></th>
			</tr>
			<c:if
				test="${! empty sampleForm.map.sampleBean.primaryPOCBean.domain.id}">
				<tr>
					<td>
						Yes
					</td>
					<td>
						${primaryPOC.personDisplayName}
					</td>
					<td>
						${primaryPOC.organizationDisplayName}
					</td>
					<td>
						${primaryPOC.domain.role}
					</td>
					<c:if test="${edit eq 'true'}">
						<td align="right">
							<a
								href="javascript:setThePointOfContact(${primaryPOC.domain.id}, true);">Edit</a>&nbsp;
						</td>
					</c:if>
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
							${poc.personDisplayName}
						</td>
						<td>
							${poc.organizationDisplayName}
						</td>
						<td>
							${poc.domain.role}
						</td>
						<c:if test="${edit eq 'true'}">
							<td align="right">
								<a
									href="javascript:setThePointOfContact(${poc.domain.id}, false);">Edit</a>&nbsp;
							</td>
						</c:if>
					</tr>
				</c:forEach>
			</c:if>
		</table>
	</c:otherwise>
</c:choose>