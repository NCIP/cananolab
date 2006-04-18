<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<center>
<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td class="dataTablePrimaryLabel" colspan="2"> General Information for the workflow </td>
	</tr>
	<tr >
		<td class="leftBorderedFormFieldGrey"><b>Assay Type</b> </td>
		<td class="leftBorderedFormFieldGrey"><bean:write name="fileDownloadForm" property="assayType" /> </td>
	</tr>
	<tr >
		<td class="leftBorderedFormFieldWhite"><b>Assay</b></td>
		<td class="leftBorderedFormFieldWhite"><bean:write name="fileDownloadForm" property="assay" /></td>
	</tr>
	<tr >
		<td class="leftBorderedFormFieldGrey"><b>Run</b></td>
		<td class="leftBorderedFormFieldGrey"><bean:write name="fileDownloadForm" property="run" /></td>
	</tr>

</table>	
 <br><br>
<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
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
    <tr >
    	<td class=<%= ((i%2)==0)?"leftBorderedFormFieldWhite":"leftBorderedFormFieldGrey" %>>  <bean:write name="fileInfo" property="fileName"/>
    	</td>
    	<td class=<%= ((i%2)==0)?"leftBorderedFormFieldWhite":"leftBorderedFormFieldGrey" %>>  <bean:write name="fileInfo" property="uploadDate"/>
    	</td>
    	<td align="right" class=<%= ((i%2)==0)?"leftBorderedFormFieldWhite":"leftBorderedFormFieldGrey" %>> <b> <a href="<bean:write name='fileInfo' property='action'/>"> Download </a> </b>
    	</td>
    </tr>	
    <% i++; %>
    </logic:iterate>
    <tr>
     <td colspan="3"> </td>
    </tr>
    <tr>
    	<td colSpan="3" align="right" class="leftBorderedFormFieldWhite"> <b> 
    	    <% if ( i > 0) {%>
    	    <a href="<bean:write name='fileDownloadForm' property='downloadAll'/>"> Download All Files </a>
    	    <% } %> </b>
    	</td>
    </tr>
</table>
</center>