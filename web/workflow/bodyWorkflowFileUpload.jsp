<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
			response.setHeader("Pragma", "no-cache"); //HTTP 1.0
			response.setDateHeader("Expires", 0); //prevents caching at the proxy server

		%>
<table width="100%" align="center">
	<tr>
		<td>
			<br>
			<h3>
				Upload Files
			</h3>
		</td>
		<td align="right" width="15%">
			<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=file_upload')" class="helpText">Help</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<jsp:include page="/workflow/bodyWorkflowInfo.jsp" />
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td class="formTitle" align="center">
						${inout}
						File Upload
					</td>
				<tr>
				<tr>
					<td align="center">
						<jsp:plugin type="applet" code="gov.nih.nci.caarray.services.util.httpfileuploadapplet.HttpFileUploadApplet.class" codebase="workflow" name="HttpFileUploadApplet" archive="SignedHttpUploadApplet.jar" height="500" width="600" align="top"
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
		<p> The file upload applet couldn't be loaded correctly, please contact caNanoLab administrator </p>
					</jsp:fallback>
						</jsp:plugin>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
