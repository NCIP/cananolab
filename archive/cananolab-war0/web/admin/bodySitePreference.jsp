<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="gov.nih.nci.cananolab.util.Constants"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Site Preference" />
	<jsp:param name="topic" value="site_preference_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="/admin" enctype="multipart/form-data">
	<table width="100%" align="center">
		<tr>
			<td colspan="4">
				<jsp:include page="/bodyMessage.jsp?bundle=admin" />
				<table width="100%" align="center" class="submissionView">
					<tr>
						<td class="cellLabel">
							Upload Logo File
						</td>
						<td colspan="3">
							<html:file property="siteLogo" />&nbsp;&nbsp;
							(Recommended image dimension: 304 x 83 pixels, maximum image size: <%=Constants.MAX_LOGO_SIZE%> bytes.)
						</td>
					</tr>
					<tr>
						<td class="cellLabel">
							Site Name
						</td>
						<td colspan="3">
							<html:text styleId="fileTitle" property="siteName" size="50" />
						</td>
					</tr>
				</table>
				<br>
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" class="topBorderOnly" summary="">
					<tr>
						<td width="30%">
							<table width="498" height="15" border="0" align="right"
								cellpadding="4" cellspacing="0">
								<tr>
									<td width="490" height="15">
										<div align="right">
											<div align="right">
												<input type="hidden" name="dispatch" value="update">
												<input type="reset" value="Reset"
													onclick="javascript:location.href='admin.do?dispatch=setupNew'">
												<input type="button" value="Delete"
													onclick="javascript:location.href='admin.do?dispatch=remove'">
												<html:submit />
											</div>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</html:form>
