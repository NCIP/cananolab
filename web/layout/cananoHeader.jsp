<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.io.File"%>
<%@page import="gov.nih.nci.cananolab.util.PropertyReader"%>
<%@page import="gov.nih.nci.cananolab.util.Constants"%>

<%
	/**
	 * Because this page is used by 3 tiles, "canano.login", "canano.home" & "canano.disclaimer".
	 * And the logic of loading customized logo is simply, so implement the logic here.
	 */
	String siteName = PropertyReader.getProperty(
			Constants.FILEUPLOAD_PROPERTY, Constants.SITE_NAME);
	if (siteName != null && siteName.length() > 0) {
		pageContext.setAttribute(Constants.SITE_NAME, siteName);
	}
	String fileRoot = PropertyReader.getProperty(
		Constants.FILEUPLOAD_PROPERTY, Constants.FILE_REPOSITORY_DIR);
	StringBuilder sb = new StringBuilder();
	sb.append(fileRoot).append(File.separator).append(Constants.SITE_LOGO_FILENAME);
	File logo = new File(sb.toString());
	if (logo.exists()) {
		pageContext.setAttribute("hasSiteLogo", Boolean.TRUE);
	}
%>
<table class="subhdrBG" cellspacing="0" cellpadding="0" width="100%" border="0">
	<tbody>
		<c:if test="${! empty siteName}">
			<tr>
				<td colspan="2" class="subMenuPrimaryTitle" height="21">
					${siteName}
				</td>
			</tr>
		</c:if>	
		<tr>
			<td class="subhdrBG" align="left" width="543" background="images/background.gif" height="60">
				<div align="left">
					<img height="83" src="images/appLogo-nanolab.gif" width="304">
				</div>
			</td>
			<c:if test="${hasSiteLogo}">
				<td class="subhdrBG" align="left" width="543" background="images/background.gif" >
					<div align="right">
						<img height="80" width="299" src="admin.do?dispatch=siteLogo">
					</div>
				</td>
			</c:if>
		</tr>
	</tbody>
</table>

