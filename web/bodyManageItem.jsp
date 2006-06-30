<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<br>
<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
	<tr>
		<td valign="top">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td width="5%"></td>
					<td colspan="2">
						<h4>Manage <c:out value="${item}" /> Home </h4>				
						<span class="welcomeContent"><br><jsp:include page="${itemDescription}" /></span>
					</td>
				</tr>
				<tr>
					<td width="5%"></td>
					<td valign="top" width="40%">
						<!-- sidebar begins -->
						<table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
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
									<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%" class="sidebarSection">
										<tr>

											<td class="sidebarTitle" height="20">
												<c:out value="${fn:toUpperCase(item)}" />
												LINKS
											</td>
										</tr>
										<tr>
											<td class="sidebarContent">
												<a href="${createLink}">Create a New <c:out value="${item}" /> </a>
												<br>
												Click to add a new
												<c:out value="${fn:toLowerCase(item)}" />
												.
											</td>
										</tr>
										<tr>
											<td class="sidebarContent">
												<a href="${searchLink}">Select an Existing <c:out value="${item}" /> </a>
												<br>
												Enter search criteria to find the
												<c:out value="${fn:toLowerCase(item)}" />
												you wish to operate on.
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
					<td width="55%"></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<br>

