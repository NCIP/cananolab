<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript" src="javascript/calendar2.js"></script>
<script type="text/javascript">

function refreshContainers() {
  document.createSampleForm.action="createSample.do?dispatch=update&page=0";
  document.createSampleForm.submit();
}

</script>

<html:form action="/createSample">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					<br>
					Create Sample
				</h3>
			</td>
			<td align="right" width="15%">
				<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caLAB_1.0_OH&amp;topic=create_sample')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=inventory" />
				<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle">
								<div align="justify">
									Description
								</div>
							</td>
						</tr>

						<tr>
							<td class="formLabel">
								<div align="justify">
									<strong> Sample ID Prefix(<bean:write name="createSampleForm" property="configuredSampleNamePrefix" />X) *<span class="formField"><span class="formFieldWhite"><html:text property="sampleNamePrefix" size="15" /></span></span> &nbsp; &nbsp; Sample
										Type* <span class="formFieldWhite"> <html:select property="sampleType">
												<option value=""></option>
												<html:options name="allSampleTypes" />
											</html:select> &nbsp; &nbsp; &nbsp;</strong>
								</div>
							</td>
						</tr>
						<tr>
							<td class="formLabelWhite">
								<div align="justify">
									<strong>Description <br> <span class="formField"><span class="formFieldWhite"><html:textarea property="sampleDescription" cols="70" /></span></span></strong>
								</div>
							</td>
						</tr>
						<tr>
							<td class="formLabel">
								<div align="justify">
									<strong>Source* <span class="formFieldWhite"> <html:select property="sampleSource" onchange="javascript:updateOtherField(createSampleForm, 'sampleSource', 'otherSampleSource')">
												<option />
													<html:options name="allSampleSources" />
											</html:select> &nbsp; &nbsp; Other Source <c:choose>
												<c:when test="${createSampleForm.map.sampleSource eq 'Other'}">
													<html:text property="otherSampleSource" disabled="false" />
												</c:when>
												<c:otherwise>
													<html:text property="otherSampleSource" disabled="true" />
												</c:otherwise>
											</c:choose> &nbsp; &nbsp;Source ID <span class="formFieldWhite"><html:text property="sourceSampleId" size="10" /> </span> &nbsp; &nbsp; </strong>
								</div>
							</td>
						</tr>
						<tr>
							<td class="formLabelWhite">
								<div align="justify">
									<strong>Date Received <html:text property="dateReceived" size="9" /> <span class="formFieldWhite"> <a href="javascript:cal.popup();"><img height="18" src="images/calendar-icon.gif" width="22" border="0" alt="Click Here to Pick up the date"></a></span>&nbsp;
										&nbsp; &nbsp; SOP <html:select property="sampleSOP">
											<option value=""></option>
											<html:options name="allSampleSOPs" />
										</html:select></strong>
								</div>
							</td>
						</tr>
						<tr>
							<td class="formLabel">
								<div align="justify">
									<strong>Lot ID*&nbsp; <html:text property="lotId" size="5" /> &nbsp; &nbsp; &nbsp; Lot Description <span class="formFieldWhite"><html:text property="lotDescription" size="50" /></span> </strong>
								</div>
							</td>
						</tr>

						<tr>
							<td class="formLabelWhite">
								<div align="justify">
									<strong>Solubility <br> <span class="formFieldWhite"><html:textarea property="solubility" cols="70" /></span> &nbsp;</strong>
								</div>
							</td>
						</tr>


						<tr>
							<td class="formLabel">
								<div align="justify">
									<strong>Number of Containers*<span class="formFieldWhite"> &nbsp; <html:text property="numberOfContainers" size="2" /> &nbsp;</strong>(Please click on "Update Sample Containers" button below if number of containers has been changed.)
								</div>
							</td>
						</tr>
						<tr>
							<td class="formLabelWhite">
								<div align="left">
									<strong>General Comments</strong>
									<br>
									<span class="formField"><span class="formFieldWhite"> <html:textarea property="generalComments" cols="70" /></span></span>
								</div>
							</td>
						</tr>

					</tbody>
				</table>
				<br>
				<%--create container for each container number --%>
				<c:forEach var="containers" items="${createSampleForm.map.containers}" varStatus="status">
					<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
						<tbody>
							<tr class="topBorder">
								<td class="formTitle" width="30%">
									<div align="justify">
										Container
										<c:out value="${status.index+1}" />
										<html:hidden name="containers" indexed="true" property="containerName" value="${status.index+1}" />
										<c:choose>
											<c:when test="${status.index== 0}">
											(Template Container)
										</c:when>
										</c:choose>
									</div>
								</td>
							</tr>
							<tr>
								<td class="formLabel">
									<div align="justify">
										<strong>Container Type* <span class="formFieldWhite"> <html:select name="containers" indexed="true" property="containerType"
													onchange="javascript:updateOtherField(createSampleForm, 'containers[${status.index}].containerType', 'containers[${status.index}].otherContainerType')">
													<option value=""></option>
													<html:options name="allSampleContainerTypes" />
												</html:select></span> &nbsp; &nbsp; &nbsp; Other <span class="formFieldWhite"> <c:choose>
													<c:when test="${createSampleForm.map.containers[status.index].containerType eq 'Other'}">
														<html:text name="containers" indexed="true" property="otherContainerType" size="8" disabled="false" /></span> &nbsp; &nbsp; &nbsp; </strong>
										</c:when>
										<c:otherwise>
											<html:text name="containers" indexed="true" property="otherContainerType" size="8" disabled="true" />
											</span> &nbsp; &nbsp; &nbsp; </strong>
										</c:otherwise>
										</c:choose>
									</div>
								</td>
							</tr>
							<tr>
								<td class="formLabelWhite style1">
									<div align="left">
										<strong>Quantity <span class="formFieldWhite"><html:text size="5" name="containers" indexed="true" property="quantity" /></span><span class="formFieldWhite"> <html:select name="containers" indexed="true" property="quantityUnit">
													<option value=""></option>
													<html:options name="sampleContainerInfo" property="quantityUnits" />
												</html:select> </span> &nbsp; Concentration <span class="formFieldWhite"><html:text size="8" name="containers" indexed="true" property="concentration" /></span><span class="formFieldWhite"> <html:select name="containers" indexed="true"
													property="concentrationUnit">
													<option value=""></option>
													<html:options name="sampleContainerInfo" property="concentrationUnits" />
												</html:select> </span>&nbsp; Volume <span class="formFieldWhite"><html:text size="8" name="containers" indexed="true" property="volume" /></span><span class="formFieldWhite"> <html:select name="containers" indexed="true" property="volumeUnit">
													<option value=""></option>
													<html:options name="sampleContainerInfo" property="volumeUnits" />
												</html:select></span></strong> &nbsp;&nbsp;&nbsp;
									</div>

									<div align="justify"></div>
								</td>
							</tr>
							<tr>
								<td class="formLabel">
									<div align="justify">
										<strong>Diluents/Solvent <html:text name="containers" indexed="true" property="solvent" size="10" /> &nbsp; &nbsp; &nbsp; Safety Precautions <html:text name="containers" indexed="true" property="safetyPrecaution" size="30" /> &nbsp; &nbsp;
											&nbsp;</strong>
									</div>
								</td>
							</tr>

							<tr>
								<td class="formLabelWhite style1">
									<div align="justify">
										<strong>Storage Conditions <span class="formField"><html:text name="containers" indexed="true" property="storageCondition" size="50" /></span></strong>
									</div>

									<div align="justify"></div>
								</td>
							</tr>
							<tr>
								<td class="formLabel">
									<strong>Storage Location<br> <br>
										<table class="topBorderOnly" cellspacing="0" cellpadding="3" align="left" summary="" border="0">
											<strong>
											<tr>
												<td class="borderlessLabel" width="20%">
													<strong>Room</strong>
												</td>
												<td class="borderlessLabel">
													<html:select name="containers" indexed="true" property="storageLocation.room" onchange="javascript:updateOtherField(createSampleForm, 'containers[${status.index}].storageLocation.room', 'containers[${status.index}].storageLocation.otherRoom')">
														<option value=""></option>
														<html:options name="sampleContainerInfo" property="storageRooms" />
													</html:select>
												</td>
												<td class="borderlessLabel">
													<strong>Other</strong>&nbsp;
													<c:choose>
														<c:when test="${createSampleForm.map.containers[status.index].storageLocation.room eq 'Other'}">
															<html:text name="containers" indexed="true" property="storageLocation.otherRoom" disabled="false" />
														</c:when>
														<c:otherwise>
															<html:text name="containers" indexed="true" property="storageLocation.otherRoom" disabled="true" />
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
											<tr>
												<td class="borderlessLabel">
													<strong>Freezer</strong>
												</td>
												<td class="borderlessLabel">
													<html:select name="containers" indexed="true" property="storageLocation.freezer"
														onchange="javascript:updateOtherField(createSampleForm, 'containers[${status.index}].storageLocation.freezer', 'containers[${status.index}].storageLocation.otherFreezer')">
														<option value=""></option>
														<html:options name="sampleContainerInfo" property="storageFreezers" />
													</html:select>
												</td>
												<td class="borderlessLabel">
													<strong>Other</strong>&nbsp;
													<c:choose>
														<c:when test="${createSampleForm.map.containers[status.index].storageLocation.freezer eq 'Other'}">
															<html:text name="containers" indexed="true" property="storageLocation.otherFreezer" disabled="false" />
														</c:when>
														<c:otherwise>
															<html:text name="containers" indexed="true" property="storageLocation.otherFreezer" disabled="true" />
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
											<tr>
												<td class="borderlessLabel">
													<strong>Shelf</strong>
												</td>
												<td class="borderlessLabel">
													<html:select name="containers" indexed="true" property="storageLocation.shelf"
														onchange="javascript:updateOtherField(createSampleForm, 'containers[${status.index}].storageLocation.shelf', 'containers[${status.index}].storageLocation.otherShelf')">
														<option value=""></option>
														<html:options name="sampleContainerInfo" property="storageShelves" />
													</html:select>
												</td>
												<td class="borderlessLabel">
													<strong>Other</strong>&nbsp;
													<c:choose>
														<c:when test="${createSampleForm.map.containers[status.index].storageLocation.shelf eq 'Other'}">
															<html:text name="containers" indexed="true" property="storageLocation.otherShelf" disabled="false" />
														</c:when>
														<c:otherwise>
															<html:text name="containers" indexed="true" property="storageLocation.otherShelf" disabled="true" />
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
											<tr>
												<td class="borderlessLabel">
													<strong>Box</strong>
												</td>
												<td class="borderlessLabel">
													<html:select name="containers" indexed="true" property="storageLocation.box" onchange="javascript:updateOtherField(createSampleForm, 'containers[${status.index}].storageLocation.box', 'containers[${status.index}].storageLocation.otherBox')">
														<option value=""></option>
														<html:options name="sampleContainerInfo" property="storageBoxes" />
													</html:select>
												</td>
												<td class="borderlessLabel">
													<strong>Other</strong>&nbsp;
													<c:choose>
														<c:when test="${createSampleForm.map.containers[status.index].storageLocation.box eq 'Other'}">
															<html:text name="containers" indexed="true" property="storageLocation.otherBox" disabled="false" />
														</c:when>
														<c:otherwise>
															<html:text name="containers" indexed="true" property="storageLocation.otherBox" disabled="true" />
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
										</table>
								</td>
							</tr>
							<tr>
								<td class="formLabelWhite">
									<div align="left">
										<strong>Comments</strong>
										<br>
										<span class="formField"><span class="formFieldWhite"> <html:textarea name="containers" indexed="true" property="containerComments" cols="70" /> </span></span>
										<br>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
					<br>
				</c:forEach>
				<%--		</logic:iterate>--%>

				<table cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
					<tbody>
						<tr>
							<td width="30%" class="formMessage" valign="top">
								Accessioned by:
								<bean:write name="creator" />
								<br>
								Accession Date:
								<bean:write name="creationDate" />
							</td>
							<td align="right">
								<table height="32" cellspacing="0" cellpadding="4 align=" right"
															order="0">
									<tbody>
										<tr>
											<td height="32">
												<div align="right">
													<input type="button" value="Update Containers" onClick="javascript:refreshContainers();">
													<input type="button" value="Reset" onClick="javascript:location.href='createSample.do?dispatch=setup&page=0'">
													<input type="hidden" name="dispatch" value="create">
													<input type="hidden" name="page" value="1">
													<html:submit />
												</div>
											</td>
										</tr>
									</tbody>
								</table>

								<div align="right"></div>
							</td>
						</tr>
					</tbody>
				</table>
		</td>
		</tr>
	</table>
</html:form>

<script language="JavaScript">
<!-- //
 var cal = new calendar2(document.forms['createSampleForm'].elements['dateReceived']);
 cal.year_scroll = true;
 cal.time_comp = false;
 cal.context = '${pageContext.request.contextPath}';
//-->
</script>
