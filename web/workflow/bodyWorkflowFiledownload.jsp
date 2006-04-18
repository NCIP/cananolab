<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<center>
<table width="80%" class="dataTable">
	<tr>
		<td class="dataTablePrimaryLabel" colspan="2"> General Information for the workflow </td>
	</tr>
	<tr class="dataRowLight">
		<td><b>Assay Type</b> </td>
		<td class="datraCellText"><bean:write name="fileDownloadForm" property="assayType" /> </td>
	</tr>
	<tr class="dataRowDark">
		<td><b>Assay</b></td>
		<td class="datraCellText"><bean:write name="fileDownloadForm" property="assay" /></td>
	</tr>
	<tr class="dataRowLight">
		<td><b>Run</b></td>
		<td class="datraCellText"><bean:write name="fileDownloadForm" property="run" /></td>
	</tr>

</table>	
 <br><br>
<table width="80%" class="dataTable">
	<tr>
 	  <td colspan="3" class="dataTablePrimaryLabel"> Uploaded <bean:write name="fileDownloadForm" property="inout"/> Files
	  </td>
	<tr>
   <tr>
      <td class="dataTableSecondaryLabel"> File Name</td>
      <td class="dataTableSecondaryLabel"> Uploaded Date</td>
      <td class="dataTableSecondaryLabel" align="right"> Download Action</td>
    </tr>
    <% int i = 0; %>
    <logic:iterate id="fileInfo" name="fileDownloadForm" property="fileInfoList">
    <tr class=<%= ((i%2)==0)?"dataRowLight":"dataRowDark" %>>
    	<td>  <bean:write name="fileInfo" property="fileName"/>
    	</td>
    	<td>  <bean:write name="fileInfo" property="uploadDate"/>
    	</td>
    	<td align="right">  <a href="<bean:write name='fileInfo' property='action'/>"> Download </a>
    	</td>
    </tr>	
    <% i++; %>
    </logic:iterate>
    <tr>
     <td colspan="3"> </td>
    </tr>
    <tr>
    	<td colSpan="3" align="right" > <b> 
    	    <% if ( i > 0) {%>
    	    <a href="<bean:write name='fileDownloadForm' property='downloadAll'/>"> Download All Files </a>
    	    <% } %> </b>
    	</td>
    </tr>
</table>
</center>