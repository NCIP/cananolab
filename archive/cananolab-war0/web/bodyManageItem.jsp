<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table summary="" cellpadding="0" cellspacing="0" border="0"
	width="100%" height="100%">
	<jsp:include page="${itemDescription}" />
	<tr>
		<td valign="top" width="40%">
			<!-- sidebar begins -->
			<table summary="" cellpadding="0" cellspacing="0" border="0"
				height="100%">
				<tr>
					<td>
						<br>
					</td>
				</tr>
				<tr>
					<td>
						<br>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<table summary="" cellpadding="0" cellspacing="0" border="0"
							width="100%" height="100%" class="sidebarSection">
							<tr>
								<td class="sidebarTitle" height="20">
									<c:out value="${fn:toUpperCase(item)}" />
									LINKS
								</td>
							</tr>
							<c:choose>
								<c:when test="${!empty user && user.curator}">
									<tr>
										<td class="sidebarContent">
											<a href="${createLink}"> Submit a New <c:out
													value="${item}" /> </a>
											<br>
											Click to submit a new
											<c:out value="${fn:toLowerCase(item)}" />
										</td>
									</tr>
								</c:when>
							</c:choose>
							<tr>
								<td class="sidebarContent">
									<a href="${searchLink}">Search Existing <c:out
											value="${item}" />s </a>
									<br>
									Enter search criteria to obtain information on
									<c:out value="${fn:toLowerCase(item)}" />s of interest. 
								</td>
							</tr>
							<c:if test="${item eq 'Sample'}">
								<tr>
									<td class="sidebarContent">
										<a href="advancedSampleSearch.do?dispatch=setup">Advanced Sample Search</a>
										<br>
										Enter advanced search criteria to obtain meta information on samples of interest.
									</td>
								</tr>
							</c:if>
						</table>
					</td>
				</tr>
			</table>
		</td>
		<td width="60%"></td>
	</tr>
</table>
<br>

