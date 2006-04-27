<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<script type="text/javascript">
<!--//
function refreshTree() {  
  document.editRunForm.submit();
}
//-->
</script>
<h3>
<strong>Run Information</strong>
</h3>
<blockquote>
	<table width="90%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
		<tr class="topBorder">
			<td colspan="2" class="dataTablePrimaryLabel">
				<div align="justify">
					<em>RUN INFORMATION</em>
				</div>
			</td>
		</tr>
		<tr>
			<td class="formLabelWhite">
				<div align="left">
					<strong>Run Created By*</strong>
				</div>
			</td>
			<td class="formFieldWhite">				
				<html:text property="createdBy" size="15" />
			</td>	
		</tr>
		<tr>
			<td class="formLabelWhite">
			
				<div align="left">
					<strong>Run Created Date*</span> </strong>
				</div>
			</td>
			<td class="formFieldWhite">				
				<html:text property="createdDate" size="10" />
				<span class="formFieldWhite">
				 <a href="javascript:cal.popup();">
				 	<img height="18" src="images/calendar-icon.gif" width="22" border="0" alt="Click Here to Pick up the date">
				 </a>
				</span>
			</td>	
		</tr>
		<tr>
			<td class="formLabelWhite">
				<div align="left">
					<strong>Run By*</strong>
				</div>
			</td>
			<td class="formFieldWhite">				
				<html:text property="runBy" size="15" />
			</td>	
		</tr>
		<tr>
			<td class="formLabelWhite">
			
				<div align="left">
					<strong>Run Date*</span> </strong>
				</div>
			</td>
			<td class="formFieldWhite">				
				<html:text property="runDate" size="10" />
				<span class="formFieldWhite">
				 <a href="javascript:cal.popup();">
				 	<img height="18" src="images/calendar-icon.gif" width="22" border="0" alt="Click Here to Pick up the date">
				 </a>
				</span>
			</td>	
		</tr>
	</table>
	<p>
		&nbsp;
	</p>
</blockquote>
