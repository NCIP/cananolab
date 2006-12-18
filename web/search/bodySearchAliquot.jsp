<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<script type="text/javascript" src="javascript/calendar2.js"></script>

<html:form action="searchAliquot">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					<br>
					Search Aliquots
				</h3>
			</td>
			<td align="right" width="15%">
				<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caLAB_1.0_OH&amp;topic=search_aliquot_help')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=search" />
				<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" summary="">
					<TR>
						<td class="formTitle" width="30%">
							Search
						</td>
						<td class="formTitle">
							* Search for Wildcards
						</td>
					</TR>
					<tr>
						<td class="formLabelWhite" width="30%">
							<strong>Aliquot ID
						</td>
						<td class="formFieldWhite">
							<html:text property="aliquotName" size="15" />
						</td>
					</tr>
					<tr>
						<td class="formLabel" width="30%">
							<strong> Sample Type </strong>
						</td>
						<td class="formField">
							<strong> <html:select property="sampleType">
									<option value="" />
										<html:options name="allSampleTypes" />
								</html:select></strong>
						</td>
					</tr>
					<tr>
						<td class="formLabelWhite" width="30%">
							<strong>Source</strong>
						</td>
						<td class="formFieldWhite">
							<strong> <html:select property="sampleSource">
									<option value="" />
										<html:options name="allSampleSources" />
								</html:select></strong>
						</td>
					</tr>
					<tr>
						<td class="formLabel" width="30%">
							<strong>Source ID</strong>
						</td>
						<td class="formField">
							<strong> <html:select property="sourceSampleId">
									<option value="" />
										<html:options name="allSourceSampleIds" />
								</html:select> </strong>
						</td>
					</tr>
					<tr>
						<td class="formLabelWhite" width="30%">
							<strong> Date Accessioned</strong>
						</td>
						<td class="formFieldWhite">
							<html:text property="dateAccessionedBegin" size="10" />
							<a href="javascript:cal1.popup();"><img src="images/calendar-icon.gif" width="22" height="18" border="0" alt="Click Here to Pick up the date" align="middle"></a>
							<label>
								&nbsp; to &nbsp;
								<html:text property="dateAccessionedEnd" size="10" />
								<a href="javascript:cal2.popup();"><img src="images/calendar-icon.gif" width="22" height="18" border="0" alt="Click Here to Pick up the date" align="middle"></a>
							</label>
						</td>
					</tr>
					<tr>
						<td class="formLabel" width="30%">
							<strong> Creator </strong>
						</td>
						<td class="formField">
							<html:select property="sampleSubmitter">
								<option value="" />
									<html:options collection="allUsers" property="loginName" labelProperty="firstName" />
							</html:select>
						</td>
					</tr>
					<tr>
						<td colspan="2" class="formLabelWhite">
							<div align="center">
								<strong>Storage Location<br> <br> Room&nbsp; <html:select property="storageLocation.room">
										<option value="" />
											<html:options name="aliquotContainerInfo" property="storageRooms" />
									</html:select> &nbsp; Freezer&nbsp; <html:select property="storageLocation.freezer">
										<option value="" />
											<html:options name="aliquotContainerInfo" property="storageFreezers" />
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
											<input type="button" value="Reset" onClick="javascript:location.href='searchAliquot.do?dispatch=setup&page=0'">
											<input type="hidden" name="dispatch" value="search">
											<input type="hidden" name="page" value="1">
											<html:submit value="Search" />
										</div>
									</td>
								</tr>
							</table>
							<div align="right"></div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</html:form>
<script language="JavaScript">
					  <!-- //
					  var cal1 = new calendar2(document.forms['searchSampleForm'].elements['dateAccessionedBegin']);
	            	  cal1.year_scroll = true;
				      cal1.time_comp = false;
					  cal1.context = '${pageContext.request.contextPath}';
				      var cal2 = new calendar2(document.forms['searchSampleForm'].elements['dateAccessionedEnd']);
	            	  cal2.year_scroll = true;
				      cal2.time_comp = false;
				      cal2.context = '${pageContext.request.contextPath}';
  				      //-->
					  </script>

<!--_____ main content ends _____-->
