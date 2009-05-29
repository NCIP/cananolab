<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="gov.nih.nci.cananolab.util.PropertyReader"%>
<%@page import="gov.nih.nci.cananolab.util.Constants"%>
<%@page import="java.io.File"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.BufferedInputStream"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.util.Properties"%>

<%
	/**
	 * Because this page is used by 3 tiles, "canano.login", "canano.home" & "canano.disclaimer".
	 * And some page, so implement the logic here.
	 */
	Properties properties = new Properties();
	StringBuilder sb = new StringBuilder();
	String siteName = null;
	InputStream istream = null;
	try {
		// Construct property file name.
		String path = Thread.currentThread().getContextClassLoader().
					getResource(Constants.FILEUPLOAD_PROPERTY).getPath();
		sb.append(path);
		sb.deleteCharAt(0);  // First char is a forward slash, remove it.
		
		// Manully load settings from property file as PropertyReader won't reload it.
		istream = new BufferedInputStream(new FileInputStream(sb.toString()));
		properties.load(istream);
		
		// Get site name from property.
		siteName = (String) properties.get(Constants.SITE_NAME);
		if (siteName != null && siteName.length() > 0) {
			pageContext.setAttribute(Constants.SITE_NAME, siteName);
		}
		
		// Construct site logo file name.
		sb.setLength(0);
		String fileRoot = (String) properties.get(Constants.FILE_REPOSITORY_DIR);
		sb.append(fileRoot).append(File.separator).append(Constants.SITE_LOGO_FILENAME);
		File logo = new File(sb.toString());
		if (logo.exists()) {
			pageContext.setAttribute("hasSiteLogo", Boolean.TRUE);
		}
	} catch (Exception exp) {
	} finally {
		if (istream != null) {
			try {
				istream.close();
			} catch (Exception e) {
			}
		}
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

