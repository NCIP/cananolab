<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="gov.nih.nci.cananolab.util.Constants"%>
<%@page import="gov.nih.nci.cananolab.util.PropertyUtils"%>
<%@page import="gov.nih.nci.cananolab.ui.admin.AdministrationAction"%>
<%@page import="java.io.File"%>

<%
	/**
	 * Because this page is used by 3 tiles, "canano.login", "canano.home" & "canano.disclaimer".
	 * And there is no one generic action for all pages, so implement the logic here.
	 */
	try {
		// Get site name from property.
		String siteName = 
			PropertyUtils.getProperty(Constants.FILEUPLOAD_PROPERTY, Constants.SITE_NAME);
		if (siteName != null && siteName.length() > 0) {
			pageContext.setAttribute(Constants.SITE_NAME, siteName);
		}
		
		// Construct site logo file name.
		File logo = new File(AdministrationAction.getLogoFileName());
		if (logo.exists()) {
			pageContext.setAttribute("hasSiteLogo", Boolean.TRUE);
		}
	} catch (Exception exp) {
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
					<img width="304" height="83" src="images/appLogo-nanolab.gif">
				</div>
			</td>
			<c:if test="${hasSiteLogo}">
				<td class="subhdrBG" align="left" width="543" background="images/background.gif" >
					<div align="right">
						<img width="299" height="80" src="admin.do?dispatch=getSiteLogo">
					</div>
				</td>
			</c:if>
		</tr>
	</tbody>
</table>

