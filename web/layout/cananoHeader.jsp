<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="gov.nih.nci.cananolab.util.Constants"%>
<%@page import="gov.nih.nci.cananolab.util.PropertyUtils"%>
<%
	/**
	 * Because this page is used by 3 tiles, "canano.login", "canano.home" & "canano.disclaimer".
	 * And there is no one generic action class for all pages, so implement the logic here.
	 */
	// Get site name from property.
	String siteName = 
		PropertyUtils.getProperty(Constants.CANANOLAB_PROPERTY, Constants.SITE_NAME);
	if (siteName != null && siteName.length() > 0) {
		pageContext.setAttribute(Constants.SITE_NAME, siteName);
	}
	
	// Get site logo from property.
	String siteLogo = 
		PropertyUtils.getProperty(Constants.CANANOLAB_PROPERTY, Constants.SITE_LOGO);
	if (siteLogo != null && siteLogo.length() > 0) {
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
					<img width="304" height="83" src="images/appLogo-nanolab.gif">
				</div>
			</td>
			<c:if test="${hasSiteLogo}">
				<td class="subhdrBG" align="left" width="543" background="images/background.gif" >
					<div align="right">
						<img src="admin.do?dispatch=getSiteLogo">
					</div>
				</td>
			</c:if>
		</tr>
	</tbody>
</table>