<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
			response.setHeader("Pragma", "no-cache"); //HTTP 1.0
			response.setDateHeader("Expires", 0); //prevents caching at the proxy server
		%>
<center>
	<table width="80%" align="center">
		<tr>
			<td width="10%">
				&nbsp;
			</td>
			<td>
				<br>
				<h3>
					Upload Files
				</h3>
			</td>
			<td align="right" width="10%">
				<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=file_upload')" class="helpText">Help</a>
			</td>
	</table>

	<jsp:include page="/workflow/bodyWorkflowInfo.jsp" />
	<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
			<td class="formTitle" align="center">
				<bean:write name="fileUploadForm" property="inout" />
				File Upload
			</td>
		<tr>
		<tr>
			<td>
				<jsp:plugin type="applet" code="gov.nih.nci.caarray.services.util.httpfileuploadapplet.HttpFileUploadApplet.class" codebase="workflow" name="HttpFileUploadApplet" archive="SignedHttpUploadApplet.jar" height="500" width="500" align="top"
					jreversion="1.3">
					<jsp:params>
						<jsp:param name="uploadURL" value="${fileUploadForm.map.servletURL}" />
						<jsp:param name="notifyURL" value="${fileUploadForm.map.notifyURL}" />
						<jsp:param name="sid" value="${fileUploadForm.map.sid}" />
						<jsp:param name="module" value="${fileUploadForm.map.module}" />
						<jsp:param name="defaultURL" value="${fileUploadForm.map.defaultURL}" />
						<jsp:param name="permissibleFileExtension" value="${fileUploadForm.map.permissibleFileExtension}" />
					</jsp:params>
					<jsp:fallback>
		<p> The file upload applet couldn't be loaded correctly, please contact caLAB administrator </p>
					</jsp:fallback>
				</jsp:plugin>
			</td>
		</tr>
	</table>
</center>

