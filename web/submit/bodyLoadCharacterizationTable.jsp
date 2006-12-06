<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html:form action="/loadFile" enctype="multipart/form-data">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					<br>
					Load Characterization File
				</h3>
			</td>
			<td align="right" width="15%">
				<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=create_nanoparticle')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=submit" />
				<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify">
									File Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<strong> <html:radio property="fileSource" value="chooseExisting">Choose File From caLAB workflow</html:radio>
									<table cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
										<%--
										<tr>
											<td class="borderlessLabel" width="50">
											</td>										
											<td class="borderlessLabel">
												<strong>Assay Type</strong>
											</td>
											<td class="borderlessLabel">
												<html:select property="assayType" onchange="javascript:doubleDropdown(document.loadDerivedBioAssayDataForm.assayType, document.loadDerivedBioAssayDataForm.assayName, assayTypeAssays);">
													<option value=""></option>
													<html:options name="allAvailableAssayTypes" />
												</html:select>
											</td>											
											<td class="borderlessLabel">
												<strong>=> Assay Name</strong>
											</td>											
											<td class="borderlessLabel" colspan="3">
												<html:select property="assayName">
													<option value="${loadDerivedBioAssayDataForm.map.assayName}" selected>
														${loadDerivedBioAssayDataForm.map.assayName}
													</option>
												</html:select>
											</td>
										</tr>
										--%>
										<tr>
											<td class="borderlessLabel" width="50">
											</td>
											<td class="borderlessLabel" width="20%">
												<strong>=>&nbsp; Assay Result File*</strong>
											</td>
											<td class="borderlessLabel">
												<html:select property="fileId">
													<html:options collection="allRunFiles" property="id" labelProperty="displayName" />
												</html:select>
											</td>
										</tr>
									</table> <br> <br> <html:radio property="fileSource" value="new">Upload New File</html:radio> </strong>
								<table cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
									<tr>
										<td class="borderlessLabel" width="50">
										</td>
										<td class="borderlessLabel" valign="top" width="20%">
											<strong>=>&nbsp; Assay Result File*</strong>
										</td>
										<td class="borderlessLabel" valign="top">
											<html:file property="file" />
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Assay Result File Title*</strong>
							</td>
							<td class="rightLabel" colspan="3">
								<html:text property="title" />
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Assay Result File Description</strong>
							</td>
							<td class="rightLabel" colspan="3">
								<html:textarea property="description" rows="3" cols="60" />
							</td>
						</tr>			
						<tr>
							<td class="leftLabel">
								<strong>Keywords <em>(one per line)</em></strong>
							</td>
							<td class="rightLabel" colspan="3">
								<html:textarea property="keywords" rows="3" />
							</td>
						</tr>
						<tr>
							<td class="leftLabel" valign="top">
								<strong>Visibility</strong>
							</td>
							<td class="rightLabel" colspan="3">
								<html:select property="visibilities" multiple="true" size="6">
									<html:options name="allVisibilityGroups" />
								</html:select>
							</td>
						</tr>
					</tbody>
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
											<div align="right">
												<input type="button" value="Cancel" onclick="javascript:history.go(-1);">
												<input type="reset" value="Reset" onclick="javascript:resetSelect(document.submitReportForm.particleNames));">
												<input type="hidden" name="dispatch" value="submit">
												<input type="hidden" name="page" value="1">
												<html:hidden property="forwardPage" />
												<html:hidden property="fileNumber" />
												<html:hidden property="particleName" />
												<html:hidden property="characterization" />
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
			</td>
		</tr>
	</table>
</html:form>
<script language="JavaScript">
<!--//

/* populate a hashtable containing assayType assays */ 
var assayTypeAssays=new Array();
<c:forEach var="item" items="${allAssayTypeAssays}">
    var assays=new Array();
    <c:forEach var="assay" items="${allAssayTypeAssays[item.key]}" varStatus="count">
  		assays[${count.index}]='${assay.assayName}';  	
    </c:forEach>
    assayTypeAssays['${item.key}']=assays;
  </c:forEach>
//-->
</script>
