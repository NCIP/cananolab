<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<script type="text/javascript" src="javascript/calendar2.js"></script>

<html:form action="/searchWorkflow">
	<h2>
		<br>
		<strong>Search Workflow </strong>
	</h2>
	<html:errors />
	<logic:messagesPresent message="true">
		<ul>
			<font color="red"> <html:messages id="msg" message="true" bundle="search">
					<li>
						<bean:write name="msg" />
					</li>
				</html:messages> </font>
		</ul>
	</logic:messagesPresent>
	<blockquote>
		<TABLE WIDTH=100% BORDER=0 align="center" CELLPADDING=1 CELLSPACING=0>
			<TR>
				<td width="64%" class="dataTablePrimaryLabel">
					Search
				</td>
				<td width="36%" height="20" class="dataTablePrimaryLabel">
					<div align="center">
						* Search for Wildcards
					</div>
				</td>
			</TR>
		</TABLE>
		<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" summary="">
			<tr>
				<td colspan="2" class="formLabel">
					<div align="center">
						<label for="label">
							<strong>Assay Name<span class="formFieldWhite"><img src="images/help.gif" width="15" height="15" onmouseover="s_show('workflowSearch_assayName',event)" onmouseout="s_hide()"> </span> <html:text property="assayName" size="20" /> &nbsp; &nbsp; &nbsp;
								&nbsp;Assay Type<span class="formFieldWhite"><img src="images/help.gif" width="15" height="15" onmouseover="s_show('workflowSearch_assayType',event)" onmouseout="s_hide()"></span> <span class="formFieldWhite"> <html:select property="assayType">
										<option value=""></option>
										<html:options name="allAssayTypes" />
									</html:select> </span>&nbsp; &nbsp; </strong>
						</label>
					</div>
				</td>
			</tr>
			<tr>
				<td width="38%" class="formLabelWhite">
					<strong>Assay Run Date</strong>
					<img src="images/help.gif" width="15" height="15" onmouseover="s_show('workflowSearch_assayRunDate',event)" onmouseout="s_hide()">
				</td>
				<td width="62%" valign="top" class="formFieldWhite">
					<span class="formField"><html:text property="assayRunDateBegin" size="10" /> <a href="javascript:cal.popup();"><img src="images/calendar-icon.gif" width="22" height="18" border="0" align="middle"></a> <label>
							&nbsp;&nbsp; to &nbsp;&nbsp;
							<html:text property="assayRunDateEnd" size="10" />

							<a href="javascript:cal2.popup();"><img src="images/calendar-icon.gif" width="22" height="18" border="0" align="middle"></a>
						</label> </span>
				</td>
			</tr>
			<tr>
				<td class="formLabel">
					<strong><strong>Aliquot ID </strong><img src="images/help.gif" width="15" height="15" onmouseover="s_show('workflowSearch_aliquotId',event)" onmouseout="s_hide()">
				</td>
				<td class="formField">
					<span class="formFieldWhite"> <label>
							<strong> <html:text property="aliquotId" size="20" /> </strong>
						</label> </span>
				</td>
			</tr>
			<tr>
				<td class="formLabelWhite">
					<strong>Include Masked Aliquots?<img src="images/help.gif" width="15" height="15" onmouseover="s_show('workflowSearch_maskedAliquots',event)" onmouseout="s_hide()"></strong>
				</td>
				<td class="formFieldWhite">
					<span class="formField"> <html:checkbox property="includeMaskedAliquots" /></span>
				</td>
			</tr>
			<tr>
				<td class="formLabel">
					<strong>File Name <img src="images/help.gif" width="15" height="15" onmouseover="s_show('workflowSearch_fileName',event)" onmouseout="s_hide()"></strong>
				</td>
				<td class="formField">
					<span class="formFieldWhite"><strong> <html:text property="fileName" size="20" /> &nbsp; &nbsp; &nbsp; <html:radio property="isFileIn" value="In" /> In <strong> &nbsp; &nbsp; &nbsp; <html:radio property="isFileIn" value="Out" /> Out </strong></strong></span>
				</td>
			</tr>
			<tr>
				<td class="formLabelWhite">
					<strong>File Submission Date</strong>
					<img src="images/help.gif" width="15" height="15" onmouseover="s_show('workflowSearch_fileSubmissionDate',event)" onmouseout="s_hide()">
				</td>
				<td class="formFieldWhite">
					<span class="formField"> <html:text property="fileSubmissionDateBegin" size="10" /> <a href="javascript:cal3.popup();"><img src="images/calendar-icon.gif" width="22" height="18" border="0" align="middle"></a> <label>
							&nbsp;&nbsp; to &nbsp;&nbsp;
							<html:text property="fileSubmissionDateEnd" size="10" />

							<a href="javascript:cal4.popup();"><img src="images/calendar-icon.gif" width="22" height="18" border="0" align="middle"></a>
						</label> </span>
				</td>
			</tr>
			<tr>
				<td class="formLabel">
					<strong>File Submitter </strong>
					<img src="images/help.gif" width="15" height="15" onmouseover="s_show('workflowSearch_fileSubmitter',event)" onmouseout="s_hide()">
				</td>
				<td class="formField">
					<span class="formFieldWhite"><strong> <html:select property="fileSubmitter">
								<option value=""></option>
								<html:options name="allFileSubmitters" />
							</html:select> </strong> </span>
				</td>
			</tr>
			<tr>
				<td class="formLabelWhite">
					<strong> Include Masked Files? </strong>
					<label for="label2"></label>
					<img src="images/help.gif" width="15" height="15" onmouseover="s_show('workflowSearch_maskedFiles',event)" onmouseout="s_hide()">
				</td>
				<td class="formFieldWhite">
					<span class="formField"> <strong> <html:checkbox property="includeMaskedFiles" /> </strong> </span>
				</td>
			</tr>
			<tr>
				<td align="center" colspan="3">
					<!-- action buttons begins -->
				</td>
			</tr>
		</table>
		<br>
		<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
			<tr>
				<td width="30%">
					<span class="formMessage"> </span>
					<br>
					<table width="498" height="32" border="0" align="right" cellpadding="4" cellspacing="0">
						<tr>
							<td width="490" height="32">
								<div align="right">
									<html:reset />
									<html:submit />
								</div>
							</td>
						</tr>
					</table>
					<div align="right"></div>
				</td>
			</tr>
		</table>
		<br>
		<p>
			&nbsp;
		</p>
		<p>
			&nbsp;
		</p>
		<p>
			&nbsp;
		</p>
	</blockquote>
</html:form>
<script language="JavaScript">
<!-- //
 var cal = new calendar2(document.forms['searchWorkflowForm'].elements['assayRunDateBegin']);
 cal.year_scroll = true;
 cal.time_comp = false;
 
 var cal2 = new calendar2(document.forms['searchWorkflowForm'].elements['assayRunDateEnd']);
 cal2.year_scroll = true;
 cal2.time_comp = false;
 
 var cal3 = new calendar2(document.forms['searchWorkflowForm'].elements['fileSubmissionDateBegin']);
 cal3.year_scroll = true;
 cal3.time_comp = false;
 
 var cal4 = new calendar2(document.forms['searchWorkflowForm'].elements['fileSubmissionDateEnd']);
 cal4.year_scroll = true;
 cal4.time_comp = false;
//-->
</script>
