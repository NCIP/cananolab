<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<script type="text/javascript" src="javascript/calendar2.js"> </script>

<html:form action="/createRun">
<table width="90%" align="center"><tr>
	<td width="10%">&nbsp;</td>
	<td >
		<h3>Create Run </h3>
	</td>
	<td align="right" width="10%">
		<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=create_assay_run')" class="helpText">Help</a>
	</td>
</table>
	<blockquote>
		<jsp:include page="/bodyMessage.jsp?bundle=workflow" />
		<table width="90%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
			<tr class="topBorder">
				<td colspan="2" class="formTitle">
					<div align="justify">
						Description
					</div>
				</td>
			</tr>

			<tr>
				<td class="formLabel">
					<strong>Assay Type : Assay*</strong>
				</td>
				<TD class="formField">
					<html:select property="assayId">
						<option value=""></option>
						<html:options collection="allAssayBeans" property="assayId" labelProperty="assayStr" />
					</html:select>
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
					<table width="60%" align="left" cellpadding="0" cellspacing="0">
						<tr>
							<td align="center" width="40%">
								<span class="formMessage">Aliquots</span>
							</td>
							<td>
								&nbsp;
							</td>
							<td width="40%">
								<span class="formMessage">Use Aliquots</span>
							</td>
						</tr>
						<tr>
							<td width="40%" height="39" valign="top" rowspan="2">
								<div align="center">
									<span class="mainMenu"><html:select multiple="true" property="availableAliquot" size="4">
											<html:options collection="allUnmaskedAliquots" property="aliquotId" labelProperty="aliquotName" />
										</html:select> </span>
								</div>
							</td>
							<td align="center">
								<input type="button" onClick="assignAliquots(document.createRunForm.availableAliquot, document.createRunForm.assignedAliquot)" value=">>" />
							</td>
							<td width="40%" valign="top" rowspan="2">
								<html:select multiple="true" property="assignedAliquot" size="4">							
								</html:select>
							</td>
						</tr>
						<tr>
							<td align="center">
								<input  type="button" onClick="assignAliquots(document.createRunForm.assignedAliquot, document.createRunForm.availableAliquot)" value=&lt;&lt; />
							</td>
						
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td class="formLabel">
					<div align="left">
						<strong>Aliquot Comments </strong>
					</div>
				</td>
				<td class="formField">
					<div align="justify">
						<span class="formFieldWhite"><html:textarea property="aliquotComment" cols="40" /></span>
					</div>
				</td>
			</tr>
			<tr>
				<td class="formLabelWhite">
					<strong>Run By*</strong>
				</td>
				<td class="formFieldWhite">
					<html:select property="runBy">
						<option value=""></option>
						<html:options collection="allUserBeans" property="loginId" labelProperty="fullName" />
					</html:select>
				</td>
			</tr>
			<tr>
				<td class="formLabel">
					<strong>Run Date* </strong>
				</td>
				<td class="formField">
					<html:text property="runDate" size="10"/>
					<span class="formFieldWhite"> <a href="javascript:cal.popup();"> <img height="18" src="images/calendar-icon.gif" width="22" border="0" alt="Click Here to Pick up the date"> </a> </span>
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
								<div align="right">
									<div align="right">
										<input type="reset" value="Reset" onclick="resetObject(document.createRunForm.assignedAliquot, document.createRunForm.availableAliquot);">
										<input type="button" value="Submit" onclick="submitAction();" >
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
	</blockquote>
</html:form>
<script language="JavaScript">
<!--//

  /* 
  	 cal is java script variable instantiated from calendar2 function
  	 in script.js file
  */ 
  var cal = new calendar2(document.forms['createRunForm'].elements['runDate']);
  cal.year_scroll = true;
  cal.time_comp = false;
  cal.context = '${pageContext.request.contextPath}';
  
  /* assignAliquots function moves the selected aliquots 
     from available aliquot list to the assigned select list box.
  */ 
  function assignAliquots(fromObj,toObj)
  {
  		moveItems(fromObj, toObj);
  }
  
  
  /* 
  	 resetObject function re assigns the items of assignedAliquot select box 
  	 to availableAliquot select box.     
  */ 
  function resetObject(fromObj,toObj) 
  {
  		for(i = 0; i < fromObj.options.length; i++){		
			fromObj.options[i].selected=true;	
		}
		moveItems(fromObj,toObj);
  		return true;		
  }

  function submitAction() 
  {
  		for(i = 0; i < document.createRunForm.assignedAliquot.options.length; i++){		
			document.createRunForm.assignedAliquot.options[i].selected=true;	
		}
		document.createRunForm.submit();
  }

//-->
</script>
