<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<br><br>

<center>
	<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
			<td class="dataTablePrimaryLabel" colspan="2">
				General Information for the workflow
			</td>
		</tr>
		<tr>
			<td class="leftBorderedFormFieldGrey">
				<b>Assay Type</b>
			</td>
			<td class="leftBorderedFormFieldGrey">
				<bean:write name="fileUploadForm" property="assayType" />
			</td>
		</tr>
		<tr>
			<td class="leftBorderedFormFieldWhite">
				<b>Assay</b>
			</td>
			<td class="leftBorderedFormFieldWhite">
				<bean:write name="fileUploadForm" property="assay" />
			</td>
		</tr>
		<tr>
			<td class="leftBorderedFormFieldGrey">
				<b>Run</b>
			</td>
			<td class="leftBorderedFormFieldGrey">
				<bean:write name="fileUploadForm" property="run" />
			</td>
		</tr>
	</table>
	<br>
	<br>
	<table border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
			<td class="dataTablePrimaryLabel">
				<bean:write name="fileUploadForm" property="inout" />
				File Upload
			</td>
		<tr>
		<tr>
			<td>
				<jsp:plugin type="applet" 
				     code="gov.nih.nci.caarray.services.util.httpfileuploadapplet.HttpFileUploadApplet.class" 
				     codebase="workflow" name="HttpFileUploadApplet" 
				     archive="SignedHttpUploadApplet.jar" 
				     height="500" width="500" 
				     align="top" jreversion="1.3">
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

