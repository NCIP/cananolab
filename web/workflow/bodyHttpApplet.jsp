<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<COMMENT> A Java Plugin Approach is used with static versioning for File Upload. Hence, a static code base is used for which the reusable code has been tested </COMMENT>

<center> 
   <OBJECT classid="clsid:CAFEEFAC-0014-0002-0000-ABCDEFFEDCBA"  
    	WIDTH = "700" HEIGHT = "500" 	codebase="http://java.sun.com/products/plugin/autodl/jinstall-1_4_2-windows-i586.cab#Version=1,4,2,0">     	 	        
   <PARAM NAME = "code" VALUE ="gov.nih.nci.calab.service.workflow.httpfileuploadapplet.HttpFileUploadApplet">


   <PARAM NAME = "archive" VALUE ="<bean:write name="httpAppletParams" property="archiveValue" />">
   <PARAM NAME ="type" VALUE="application/x-java-applet;jpi-version=1.3">
   <PARAM NAME ="uploadURL" VALUE ="<bean:write name="httpAppletParams" property="uploadURL" />">
   <PARAM NAME ="notifyURL" VALUE ="<bean:write name="httpAppletParams" property="notifyURL" />">
   <PARAM NAME ="defaultURL" VALUE ="<bean:write name="httpAppletParams" property="defaultURL" />">
   <PARAM NAME="id" VALUE="<bean:write name="httpAppletParams" property="id" />"> 
   <PARAM NAME="sid" VALUE="<bean:write name="httpAppletParams" property="sid" />">
   <PARAM NAME="portNumber" VALUE="<bean:write name="httpAppletParams" property="portNumber" />">    

   <COMMENT>
   <EMBED type="application/x-java-applet;version=1.3"    
   		java_CODE ="gov.nih.nci.caarray.services.util.httpfileuploadapplet.HttpFileUploadApplet"  
   		java_ARCHIVE ="<bean:write name="httpAppletParams" property="archiveValue" />" 	   		     
   		WIDTH ="500" HEIGHT ="300"  		     
   		uploadURL="<bean:write name="httpAppletParams" property="uploadURL" />" 	         
   		notifyURL="<bean:write name="httpAppletParams" property="notifyURL" />"	 
   		defaultURL="<bean:write name="httpAppletParams" property="defaultURL" />"        
   		id="<bean:write name="httpAppletParams" property="id" />"
   		sid="<bean:write name="httpAppletParams" property="sid" />"         
   		portNumber="<bean:write name="httpAppletParams" property="portNumber" />"	                  		     
   		pluginspage="http://java.sun.com/products/plugin/1.3/plugin-install.html">
   		<NOEMBED></COMMENT>  	 
    </NOEMBED></COMMENT>
   </OBJECT>  
</center>