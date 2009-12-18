<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="gov.nih.nci.cananolab.util.Constants"%>

<table summary="" cellpadding="0" cellspacing="0" border="0"
	width="100%" height="100%">
	<jsp:include page="${itemDescription}" />
	<tr>
		<td valign="top" width="40%">
			<!-- sidebar begins -->
			<table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
				<tr>
					<td>
						<br>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<c:if test="${!empty user && user.admin}">
							<table summary="" cellpadding="0" cellspacing="0" border="0"
								width="100%" height="100%" class="sidebarSection">
								<tr>
									<td class="sidebarTitle" height="20">
										<c:out value="${fn:toUpperCase(item)}" /> LINKS
									</td>
								</tr>
								<tr>
									<td class="sidebarContent">
										<a href="study.do?dispatch=setupNew">
											Submit a New Study
										</a>
										<br>
										Click to submit a new study.
									</td>
								</tr>
							</table>
						</c:if>
					</td>
				</tr>
			</table>
		</td>
		<td width="60%"></td>
	</tr>
</table>
<br>

