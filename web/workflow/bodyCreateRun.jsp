<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<script type="text/javascript">
<!--//
function refreshTree() {
  
  document.createRunForm.submit();
}
  var cal1 = new calendar2(document.forms['createRun'].elements['runDate']);
  cal1.year_scroll = true;
  cal1.time_comp = false;
//-->
</script>
<h2>
<strong>Create Run </strong>
</h2>
<html:errors />
<logic:messagesPresent message="true">
	<ul>
		<font color="red"> <html:messages id="msg" message="true" bundle="workflow">
				<li>
					<bean:write name="msg" />
				</li>
			</html:messages> </font>
	</ul>
</logic:messagesPresent>
<blockquote>
	<html:form action="/createRun">
	<table width="90%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
		<tr class="topBorder">
			<td colspan="2" class="dataTablePrimaryLabel">
				<div align="justify">
					<em>DESCRIPTION</em>
				</div>
			</td>
		</tr>
		<tr>
				<td class="formLabel">
					<div align="justify">
						<strong>Assay Type<span class="formFieldWhite"> <html:select property="assayTypeId">
									<option value=""></option>
									<html:options name="allAssayTypeIds" />
								</html:select></span></strong>&nbsp; &nbsp; &nbsp; &nbsp; <strong>Assay<span class="formFieldWhite"> <html:select property="assayId">
									<option value=""></option>
									<html:options name="allAssayIds" />
								</html:select></span></strong>
					</div>
				</td>				
			
		</tr>
		<tr>
			<td width="27%" class="formLabelWhite">
				<div align="left">
					<strong>Aliquots</strong>
				</div>
			</td>
			<td width="73%" class="formFieldWhite">
				<strong><span class="mainMenu"> </span></strong>
				<table width="41%" align="left" cellpadding="0" cellspacing="0">
					<tr>
						<td width="28%" height="39" valign="top">
							<div align="center">
								<span class="mainMenu"> <span class="formMessage">Aliquots</span> 								
								<html:select multiple="true" property="availableAliquotIds">
									<html:options name="allAvailableAliquotIds" />
								</html:select>
								</span>
							</div>
						</td>
						<td width="10%" align="center" valign="middle">
							<table border="0" cellspacing="0" cellpadding="10">
								<tr>
									<td>
									<!-- 
										<input type="button" onClick="moveItems(this.form.allAliquotsIds,this.form.aliquotsIds)" value="<<"><input type="button" onClick="moveItems(this.form.aliquotsIds,this.form.allAliquotsIds)" value=">>">
										-->
									</td>
								</tr>
							</table>
						</td>
						<td width="62%" valign="top">
							<div align="center">
								<span class="formMessage">Use Aliquots</span>
								<html:select multiple="true" property="assignedAliquotIds">
									<html:options name="allAssignedAliquotIds" />
								</html:select>
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="formLabel">
				<div align="left">
					<strong>Inputs </strong>
				</div>
			</td>
			<td class="formField">
				<div align="left">
					<strong> <span class="mainMenu"> <select multiple size="4" name="select3" style="width:100">
								<option value="12">
									&nbsp;
								</option>
								<option value="54">
									&nbsp;
								</option>
							</select> </span>&nbsp; <input name="Submit22232" type="submit" onClick="javascript:location.href='uploadFiles.htm';" value="Upload Files"> </strong>
				</div>
			</td>
		</tr>
		<tr>
			<td class="formLabelWhite">
				<div align="left">
					<strong>Outputs</strong>
				</div>
			</td>
			<td class="formFieldWhite">
				<strong>
				<span class="mainMenu"> <select multiple size="4" name="select4" style="width:100">
							<option value="12">	</option>
							<option value="54">
								&nbsp;
							</option>
				</select> </span>&nbsp; 
						<input name="Submit222322" type="submit" onClick="javascript:location.href='uploadFiles.htm';" value="Upload Files"> </strong>
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
	<br>
	<table width="82%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
		<tr>
			<td width="30%">
				<span class="formMessage"> </span>
				<br>
				<table width="498" height="32" border="0" align="right" cellpadding="4" cellspacing="0">
					<tr>
						<td width="490" height="32">
								<div align="right"><div align="right">
									<html:reset />
									<html:submit />
								</div>
							</div>
						</td>
					</tr>
				</table>
				<div align="right"></div>
			</td>
		</tr>
	</table>
	<p>
		&nbsp;
	</p>
	</html:form>
</blockquote>
