<%--L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="${itemDescription}" />
<br>
<table summary="layout" cellpadding="0" cellspacing="0" border="0"
	width="40%" height="100%" class="sidebarSection">
	<tr>
		<th class="sidebarTitle" scope="col" align="left"><c:out
				value="${fn:toUpperCase(item)}" /> LINKS</th>
	</tr>
	<c:choose>
		<c:when test="${!empty user}">
			<tr>
				<td class="sidebarContent"><a href="${createLink}"> Submit
						a New <c:out value="${item}" /> </a> <br> Select to submit a new
					<c:out value="${fn:toLowerCase(item)}" />.</td>
			</tr>
			<c:if test="${item eq 'Sample'}">
				<tr>
					<td class="sidebarContent"><a
						href="sample.do?dispatch=setupClone&page=0">Copy an Existing
							Sample</a> <br> Select to copy information from an existing
						sample to a new sample.</td>
				</tr>
			</c:if>
		</c:when>
	</c:choose>
	<tr>
		<td class="sidebarContent"><a href="${searchLink}">Search
				Existing <c:out value="${item}" />s </a> <br> Enter search
			criteria to obtain information on <c:out
				value="${fn:toLowerCase(item)}" />s of interest.</td>
	</tr>
</table>