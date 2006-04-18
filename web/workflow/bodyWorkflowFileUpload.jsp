<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<table width="100%" class="dataTable">
	<tr>
		<td class="dataTablePrimaryLabel" colspan="2"> General Information for the workflow </td>
	</tr>
	<tr class="dataRowLight">
		<td><b>Assay Type</b> </td>
		<td class="datraCellText"><bean:write name="fileUploadForm" property="assayType" /> </td>
	</tr>
	<tr class="dataRowDark">
		<td><b>Assay</b></td>
		<td class="datraCellText"><bean:write name="fileUploadForm" property="assay" /></td>
	</tr>
	<tr class="dataRowLight">
		<td><b>Run</b></td>
		<td class="datraCellText"><bean:write name="fileUploadForm" property="run" /></td>
	</tr>
	<tr class="dataRowDark">
		<td><b>Run by</b></td>
		<td class="datraCellText"><bean:write name="fileUploadForm" property="runby" /></td>
	</tr>
	<tr class="dataRowLight" >
		<td><b>Run time</b></td>
		<td class="datraCellText"><bean:write name="fileUploadForm" property="rundate" /></td>
	<tr>
</table>	

 <center>
<table class="dataTable">
	<tr>
	<td class="dataTablePrimaryLabel"> <bean:write name="fileUploadForm" property="inout"/> File Upload
	</td>
	<tr>
   <tr>
    <td>

   <OBJECT classid="clsid:CAFEEFAC-0014-0002-0000-ABCDEFFEDCBA"  
        WIDTH = "500" HEIGHT = "700"    codebase="http://java.sun.com/products/plugin/autodl/jinstall-1_4_2-windows-i586.cab#Version=1,4,0,0">                  
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

        </td>
    </tr>
</table>
   </center>