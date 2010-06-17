<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table summary="" cellpadding="0" cellspacing="0" border="0"
	width="100%" height="100%">
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyTitle.jsp">
				<jsp:param name="pageTitle" value="Manage Curation" />
				<jsp:param name="topic" value="manage_Curation_help" />
				<jsp:param name="glossaryTopic" value="glossary_help" />
			</jsp:include>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="welcomeContent">
			This is the manage curation section which allows curators ...
			<br>
		</td>
	</tr>

	<tr>
		<td valign="top" width="40%">
			<!-- sidebar begins -->
			<table summary="" cellpadding="0" cellspacing="0" border="0"
				height="100%">
				<tr>
					<td height="30">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td valign="top">
						<table summary="" cellpadding="0" cellspacing="0" border="0"
							width="100%" height="100%" class="sidebarSection">
							<tr>
								<td height="20" class="sidebarTitle">
									CURATION LINKS
								</td>
							</tr>
							<tr>
								<td class="sidebarContent">
									<a href="reviewData.do?dispatch=setup">Review
										Data Pending Release to Public</a>
									<br>
									Review samples, publications and protocols submitted by
									researchers and assign them to be readable by Public when
									appropriate.
								</td>
							</tr>
							<tr>
								<td class="sidebarContent">
									<a href="${createLink}">Review Data Pending Data Availability Metrics Generation</a>
									<br>
									Review samples that don't yet have data availability metrics and generate the metrics.
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
		<td width="60%"></td>
	</tr>
</table>
<br>

