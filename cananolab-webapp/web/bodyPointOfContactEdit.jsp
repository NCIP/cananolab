<%--L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:choose>
	<c:when
		test="${empty primaryPOC.domain.organization.name && empty otherPOCs}">
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
			<c:if test="${! empty primaryPOC.domain.id}">
				<tr>
					<td>
						Yes
					</td>
					<td>
						<c:out value="${primaryPOC.personDisplayName}" escapeXml="false" />
					</td>
					<td>
						<c:out value="${primaryPOC.organizationDisplayName}"
							escapeXml="false" />
					</td>
					<td>
						<c:out value="${primaryPOC.domain.role}" />
					</td>
					<c:if test="${edit eq 'true'}">
						<td align="right">
							<a
								href="javascript:setThePointOfContact(${primaryPOC.domain.id}, true);">Edit</a>&nbsp;
						</td>
					</c:if>
				</tr>
			</c:if>
			<c:if test="${!empty otherPOCs}">
				<c:forEach var="poc" items="${otherPOCs}">
					<tr>
						<td>
							No
						</td>
						<td>
							<c:out value="${poc.personDisplayName}" escapeXml="false" />
						</td>
						<td>
							<c:out value="${poc.organizationDisplayName}" escapeXml="false" />
						</td>
						<td>
							<c:out value="${poc.domain.role}" />
						</td>
						<c:if test="${edit eq 'true'}">
							<td align="right">
								<a
									href="javascript:setThePointOfContact(${poc.domain.id}, false, ${source});">Edit</a>&nbsp;
							</td>
						</c:if>
					</tr>
				</c:forEach>
			</c:if>
		</table>
	</c:otherwise>
</c:choose>