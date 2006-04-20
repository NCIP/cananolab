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
				<%--
   <OBJECT classid="clsid:CAFEEFAC-0014-0002-0000-ABCDEFFEDCBA"  
        WIDTH = "500" HEIGHT = "500"    codebase="http://java.sun.com/products/plugin/autodl/jinstall-1_4_2-windows-i586.cab#Version=1,4,0,0">                  
   <PARAM NAME = "code" VALUE ="gov.nih.nci.caarray.services.util.httpfileuploadapplet.HttpFileUploadApplet">
   <PARAM NAME = "archive" VALUE ="<bean:write name="fileUploadForm" property="archiveValue" />">
   <PARAM NAME ="type" VALUE="application/x-java-applet;jpi-version=1.3">
   <PARAM NAME ="uploadURL" VALUE ="<bean:write name="fileUploadForm" property="servletURL" />">
   <PARAM NAME ="notifyURL" VALUE ="<bean:write name="fileUploadForm" property="notifyURL" />">
   <PARAM NAME="sid" VALUE="<bean:write name="fileUploadForm" property="sid" />">
   <PARAM NAME="module" VALUE="<bean:write name="fileUploadForm" property="module" />">
   <PARAM NAME ="defaultURL" VALUE ="<bean:write name="fileUploadForm" property="defaultURL" />">
   <PARAM NAME="permissibleFileExtension" VALUE="<bean:write name="fileUploadForm" property="permissibleFileExtension" />">  
   <COMMENT>
   <EMBED type="application/x-java-applet;version=1.3"    
        java_CODE ="gov.nih.nci.caarray.services.util.httpfileuploadapplet.HttpFileUploadApplet"  
        java_ARCHIVE ="<bean:write name="fileUploadForm" property="archiveValue" />"               
        WIDTH ="700" HEIGHT ="500"               
        uploadURL="<bean:write name="fileUploadForm" property="servletURL" />"              
        notifyURL="<bean:write name="fileUploadForm" property="notifyURL" />"  
        sid="<bean:write name="fileUploadForm" property="sid" />" 
        module="<bean:write name="fileUploadForm" property="module" />"
        defaultURL="<bean:write name="fileUploadForm" property="defaultURL" />" 
        permissibleFileExtension="<bean:write name="fileUploadForm" property="permissibleFileExtension" />"                                        
        pluginspage="http://java.sun.com/products/plugin/1.3/plugin-install.html">
        <NOEMBED></COMMENT>      
    </NOEMBED></COMMENT>
   </OBJECT>  
--%>
				<jsp:plugin type="applet" 
				     code="gov.nih.nci.caarray.services.util.httpfileuploadapplet.HttpFileUploadApplet.class" codebase="" name="HttpFileUploadApplet" 
				     archive="SignedHttpUploadApplet.jar" 
				     height="500" width="500" 
				     align="top" jreversion="1.4">
					<jsp:params>
						<jsp:param name="code" value="gov.nih.nci.caarray.services.util.httpfileuploadapplet.HttpFileUploadApplet" />
						<jsp:param name="archive" value="${fileUploadForm.map.archiveValue}" />
						<jsp:param name="type" value="application/x-java-applet;jpi-version=1.3" />
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

