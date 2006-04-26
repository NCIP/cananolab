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
		<table width="90%" border="0" align="center" cellpadding="3" cellspacing="0" summary="">
			<tr>
				<td class="dataTablePrimaryLabel">
					Search
				</td>
				<td class="dataTablePrimaryLabel">
					* Search for Wildcards
				</td>
			</tr>
			<tr>
				<td class="formLabel">
					<strong>Assay Name<strong>
				</td>
				<td class="formField">
					<html:text property="assayName" size="15" />
				</td>
			</tr>
			<tr>
				<td class="formLabelWhite">
					<strong>Assay Type </strong>
				</td>
				<td class="formFieldWhite">
					<html:select property="assayType">
						<option value=""></option>
						<html:options name="allAssayTypes" />
					</html:select>
				</td>
			</tr>
			<tr>
				<td class="formLabel">
					<strong>Assay Run Date</strong>
				</td>
				<td valign="top" class="formField">
					<html:text property="assayRunDateBegin" size="10" />
					<a href="javascript:cal.popup();"><img src="images/calendar-icon.gif" width="22" height="18" border="0" align="middle"></a> &nbsp;&nbsp; to &nbsp;&nbsp;
					<html:text property="assayRunDateEnd" size="10" />
					<a href="javascript:cal2.popup();"><img src="images/calendar-icon.gif" width="22" height="18" border="0" align="middle"></a>
				</td>
			</tr>
			<tr>
				<td class="formLabelWhite">
					<strong>Aliquot ID</strong>
				</td>
				<td class="formFieldWhite">
					<strong> <html:text property="aliquotName" size="20" /> </strong>
				</td>
			</tr>
			<tr>
				<td class="formLabel">
					<strong>File Name </strong>
				</td>
				<td class="formField">
					<strong> <html:text property="fileName" size="20" /> </strong>
				</td>
			</tr>
			<tr>
				<td class="formLabelWhite">
					<strong>File Submission Date</strong>
				</td>
				<td class="formFieldWhite">
					<html:text property="fileSubmissionDateBegin" size="10" />
					<a href="javascript:cal3.popup();"><img src="images/calendar-icon.gif" width="22" height="18" border="0" align="middle"></a> &nbsp;&nbsp; to &nbsp;&nbsp;
					<html:text property="fileSubmissionDateEnd" size="10" />
					<a href="javascript:cal4.popup();"><img src="images/calendar-icon.gif" width="22" height="18" border="0" align="middle"></a>

				</td>
			</tr>
			<tr>
				<td class="formLabel">
					<strong>File Submitter </strong>
				</td>
				<td class="formField">
					<strong> <html:select property="fileSubmitter">
							<option value=""></option>
							<html:options name="allFileSubmitters" />
						</html:select> </strong>
				</td>
			</tr>
			<tr>
				<td class="formLabelWhite">
					<strong> Join Search Criteria by </strong>
				</td>
				<td class="formFieldWhite">
					<strong> <html:radio property="criteriaJoin" value="and" /> &nbsp; &nbsp; And &nbsp;&nbsp; <html:radio property="criteriaJoin" value="or" /> Or </strong>
				</td>
			</tr>
		</table>
		<br>
		<table width="90%" border="0" align="center" cellpadding="3" cellspacing="0" summary="">
			<tr>
				<td class="dataTablePrimaryLabel" colspan="2">
					and Additional Filtering of Above Data
				</td>
			</tr>
			<tr>
				<td class="formLabel">
					<div align="center">
						<strong> <html:checkbox property="isFileIn" /> Input Files?&nbsp; &nbsp; <html:checkbox property="isFileOut" /> Output Files? &nbsp;&nbsp; <html:checkbox
								property="includeMaskedAliquots" />Include Masked Aliquots? <html:checkbox property="includeMaskedFiles" />Include Masked Files?</strong>
					</div>
				</td>
			</tr>
		</table>
		<br>
		<table width="90%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
			<tr>
				<td width="30%">
					<table width="498" height="32" border="0" align="right" cellpadding="4" cellspacing="0">
						<tr>
							<td width="490" height="32">
								<div align="right">
									<html:reset />
									<html:submit value="Search" />
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
		<p>
			&nbsp;
		</p>
		<p>
			&nbsp;
		</p>
</html:form>
</blockquote>
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
