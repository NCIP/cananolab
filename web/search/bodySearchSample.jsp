<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<script type="text/javascript" src="javascript/calendar2.js"></script>
<html:form action="searchSample">
	<h2>
		<br>
		Search Sample Container
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
						<strong>Sample ID <span class="formFieldWhite"><img src="images/help.gif" width="15" height="15"> <html:select property="sampleName">
									<option value="" />
									<option value="all">all</option>
										<html:options collection="allSamples" property="sampleName" labelProperty="sampleName"/>
								</html:select> &nbsp; &nbsp; &nbsp; </span></strong> or<strong>&nbsp; &nbsp; &nbsp; Aliquot ID<img src="images/help.gif" width="15" height="15"> <html:select property="aliquotName">
								<option value="" />
								<option value="all">all</option>
									<html:options collection="allAliquots" property="aliquotName" labelProperty="aliquotName"/>
							</html:select> </strong>
					</div>
				</td>
			</tr>
			<tr>
				<td width="36%" class="formLabelWhite">
					<strong> <label for="label2">
							Sample Type
						</label> <span class="formFieldWhite"><img src="images/help.gif" width="15" height="15"></span> </strong>
				</td>
				<td width="64%" class="formFieldWhite">
					<span class="formField"><strong> <html:select property="sampleType">
								<option value="" />
									<html:options name="allSampleTypes" />
							</html:select></strong></span>
				</td>
			</tr>
			<tr>
				<td class="formLabel">
					<strong>Source</strong>
				</td>
				<td class="formField">
					<span class="formFieldWhite"> <strong> <html:select property="sampleSource">
								<option value="" />
									<html:options name="allSampleSources" />
							</html:select></strong> </span>
				</td>
			</tr>
			<tr>
				<td class="formLabelWhite">
					<strong>Source ID</strong>
				</td>
				<td class="formFieldWhite">
					<span class="formField"> <strong> <html:select property="sourceSampleId">
								<option value="" />
									<html:options name="allSourceSampleIds" />
							</html:select> </strong> </span>
				</td>
			</tr>
			<tr>
				<td class="formLabel">
					<strong> Date Accessioned<img src="images/help.gif" width="15" height="15"></strong>
				</td>
				<td class="formField">
					<span class="formField"> <html:text property="dateAccessionedBegin" size="10" /> <a href="javascript:cal1.popup();"><img src="images/calendar-icon.gif" width="22" height="18" border="0" alt="Click Here to Pick up the date" align="middle"></a> <label>
							&nbsp; to &nbsp;
							<html:text property="dateAccessionedEnd" size="10" />
							<a href="javascript:cal2.popup();"><img src="images/calendar-icon.gif" width="22" height="18" border="0" alt="Click Here to Pick up the date" align="middle"></a>
						</label> </span>
				</td>
			</tr>
			<tr>
				<td class="formLabelWhite">
					<strong> Submitter <span class="formFieldWhite"><img src="images/help.gif" width="15" height="15"></span> </strong>
				</td>
				<td class="formFieldWhite">
					<html:select property="sampleSubmitter">
						<option value="" />
							<html:options name="allSampleSubmitters" />
					</html:select>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="formLabel">
					<div align="center">
						<strong>Storage Location<br> <br> Room&nbsp; <html:select property="storageLocation.room">
								<option value="" />
									<html:options name="sampleContainerInfo" property="storageRooms" />
							</html:select> &nbsp; Freezer&nbsp; <html:select property="storageLocation.freezer">
								<option value="" />
									<html:options name="sampleContainerInfo" property="storageFreezers" />
							</html:select> &nbsp; &nbsp; Shelf &nbsp; <html:text property="storageLocation.shelf" size="8" /> &nbsp; Box &nbsp; <html:text property="storageLocation.box" size="8" /> &nbsp; <label for="label2"></label> </strong>
					</div>
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
				<td>
					<span class="formMessage"> </span>
					<br>
					<table border="0" align="right" cellpadding="4" cellspacing="0">
						<tr>
							<td>
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
					  var cal1 = new calendar2(document.forms['searchSampleForm'].elements['dateAccessionedBegin']);
	            	  cal1.year_scroll = true;
				      cal1.time_comp = false;
				      var cal2 = new calendar2(document.forms['searchSampleForm'].elements['dateAccessionedEnd']);
	            	  cal2.year_scroll = true;
				      cal2.time_comp = false;
  				      //-->
					  </script>

<!--_____ main content ends _____-->
