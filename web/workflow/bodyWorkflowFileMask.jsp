<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<br><br>
<center>
<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td class="formTitle" colspan="2" align="center"> General Information for the workflow </td>
	</tr>
	<tr >
		<td class="leftBorderedFormFieldGrey"><b>Assay Type</b> </td>
		<td class="leftBorderedFormFieldGrey"><bean:write name="fileMaskForm" property="assayType" /> </td>
	</tr>
	<tr >
		<td class="leftBorderedFormFieldWhite"><b>Assay</b></td>
		<td class="leftBorderedFormFieldWhite"><bean:write name="fileMaskForm" property="assay" /></td>
	</tr>
	<tr >
		<td class="leftBorderedFormFieldGrey"><b>Run</b></td>
		<td class="leftBorderedFormFieldGrey"><bean:write name="fileMaskForm" property="run" /></td>
	</tr>

</table>	
 <br><br>
<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
 	  <td colspan="3" class="formTitle" align="center"> Uploaded <bean:write name="fileMaskForm" property="inout"/> Files
	  </td>
	<tr>
   <tr>
      <td class="dataTableSecondaryLabel"> File Name</td>
      <td class="dataTableSecondaryLabel"> Uploaded Date</td>
      <td class="dataTableSecondaryLabel" align="right"> Mask Action</td>
    </tr>
    <% int i = 0; %>
    <logic:iterate id="fileInfo" name="fileMaskForm" property="fileInfoList">
    <tr >
    	<td class=<%= ((i%2)==0)?"leftBorderedFormFieldWhite":"leftBorderedFormFieldGrey" %>>  <bean:write name="fileInfo" property="fileName"/>
    	</td>
    	<td class=<%= ((i%2)==0)?"leftBorderedFormFieldWhite":"leftBorderedFormFieldGrey" %>>  <bean:write name="fileInfo" property="uploadDate"/>
    	</td>
    	<td align="right" class=<%= ((i%2)==0)?"leftBorderedFormFieldWhite":"leftBorderedFormFieldGrey" %>> <b> <a href="<bean:write name='fileInfo' property='action'/>"> Mask </a> </b>
    	</td>
    </tr>	
    </logic:iterate>
</table>
</center>