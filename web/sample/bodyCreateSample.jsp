<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript" src="javascript/calendar2.js"></script>
<script type="text/javascript" src="javascript/editableDropDown.js"></script>

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
				<a
					href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=create_sample')"
					class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=sample" />
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle">
								<div align="justify">
									Sample Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="formLabel">
								<strong> Sample ID * </strong> (
								<bean:write name="createSampleForm"
									property="configuredSampleNamePrefix" />
								X)
								<span class="formField"><span class="formFieldWhite"><html:text
											property="sample.sampleNamePrefix" size="15" /> </span> &nbsp;
									&nbsp; <strong>Sample Type* </strong><span
									class="formFieldWhite"><html:select
											property="sample.sampleType">
											<option />
												<html:options name="allSampleTypes" />
										</html:select> </span>&nbsp; &nbsp; &nbsp; 
							</td>
						</tr>
						<tr>
							<td class="formLabel">
								<strong>Description <br> <span class="formField"><span
										class="formFieldWhite"><html:textarea
												property="sample.sampleDescription" cols="70" /> </span> </span> </strong>
							</td>
						</tr>
						<tr>
							<td class="formLabel">
								<strong>Source* <span class="formFieldWhite"> <html:select
											property="sample.sampleSource"
											onkeydown="javascript:fnKeyDownHandler(this, event);"
											onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
											onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
											onchange="fnChangeHandler_A(this, event);">
											<option value="">
												--?--
											</option>
											<html:options name="allSampleSources" />
										</html:select> &nbsp; &nbsp;Source ID <span class="formFieldWhite"><html:text
												property="sample.sourceSampleId" size="10" /> </span> &nbsp;
										&nbsp; 
								</strong>
							</td>
						</tr>
						<tr>
							<td class="formLabel">
								<strong>Date Received <html:text
										property="sample.dateReceivedStr" size="9" /> <span
									class="formFieldWhite"> <a
										href="javascript:cal.popup();"><img height="18"
												src="images/calendar-icon.gif" width="22" border="0"
												alt="Click Here to Pick up the date"> </a> </span>&nbsp; &nbsp;
									&nbsp; SOP <html:select property="sample.sampleSOP">
										<option />
											<html:options name="allSampleSOPs" />
									</html:select> </strong>
							</td>
						</tr>
						<tr>
							<td class="formLabel">
								<strong>Lot ID</strong>&nbsp; (if entered, appended to sample
								ID)
								<html:text property="sample.lotId" size="5" />
								<br>
								<strong>Lot Description</strong>
								<span class="formFieldWhite"><html:text
										property="sample.lotDescription" size="50" /> </span>
							</td>
						</tr>

						<tr>
							<td class="formLabel">
								<strong>Solubility <br> <span
									class="formFieldWhite"><html:textarea
											property="sample.solubility" cols="70" /> </span> &nbsp;</strong>
							</td>
						</tr>
						<tr>
							<td class="formLabel">
								<strong>General Comments</strong>
								<br>
								<span class="formField"><span class="formFieldWhite">
										<html:textarea property="sample.generalComments" cols="70" />
								</span> </span>
							</td>
						</tr>
					</tbody>
				</table>
				<br>
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle">
								Container Information
							</td>
						</tr>
						<tr>
							<td class="completeLabel">
								<table border="0" width="100%">
									<tr>
										<td valign="bottom">
											<a href="#"
												onclick="javascript:addComponent(createSampleForm, 'createSample', 'addContainer')"><span
												class="addLink">Add Container</span> </a>
										</td>
										<td>
											<logic:iterate name="createSampleForm"
												property="sample.containers" id="container" indexId="ind">
												<table class="topBorderOnly" cellspacing="0" cellpadding="3"
													width="100%" align="center" summary="" border="0">
													<tbody>
														<tr class="topBorder">
															<c:choose>
																<c:when test="${ind==0}">
																	<td class="formSubTitle" width="100%" colspan="2">
																		<div align="justify">
																			Container ${ind+1}
																			<html:hidden
																				property="sample.containers[${ind}].containerName"
																				value="${ind+1}" />
																			(Template Container)
																</c:when>
																<c:otherwise>
																	<td class="formSubTitleNoRight" width="100%">
																		<div align="justify">
																			Container ${ind+1}
																			<html:hidden
																				property="sample.containers[${ind}].containerName"
																				value="${ind+1}" />
																	<td class="formSubTitleNoLeft" align="right">
																		<a href="#"
																			onclick="javascript:removeComponent(createSampleForm, 'createSample', ${ind}, 'removeContainer')">
																			<img src="images/delete.gif" border="0"
																				alt="remove this container"> </a>
																	</td>
																</c:otherwise>
															</c:choose>
															</div>
															</td>
														</tr>
														<tr>
															<td class="formLabel" colspan="2">
																<div align="justify">
																	<strong>Container Type* <span
																		class="formFieldWhite"> <html:select
																				property="sample.containers[${ind}].containerType"
																				onkeydown="javascript:fnKeyDownHandler(this, event);"
																				onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
																				onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
																				onchange="fnChangeHandler_A(this, event);">
																				<option value="">
																					--?--
																				</option>
																				<html:options name="allSampleContainerTypes" />
																			</html:select> </span>
																</div>
															</td>
														</tr>
														<tr>
															<td class="formLabel" colspan="2">
																<div align="left">
																	<strong>Quantity <span class="formFieldWhite"><html:text
																				size="5"
																				property="sample.containers[${ind}].quantity" /> </span><span
																		class="formFieldWhite"> <html:select
																				property="sample.containers[${ind}].quantityUnit">
																				<option />
																					<html:options name="sampleContainerInfo"
																						property="quantityUnits" />
																			</html:select> </span> &nbsp; Concentration <span class="formFieldWhite"><html:text
																				size="8"
																				property="sample.containers[${ind}].concentration" />
																	</span><span class="formFieldWhite"> <html:select
																				property="sample.containers[${ind}].concentrationUnit">
																				<option />
																					<html:options name="sampleContainerInfo"
																						property="concentrationUnits" />
																			</html:select> </span>&nbsp; Volume <span class="formFieldWhite"><html:text
																				size="8" property="sample.containers[${ind}].volume" />
																	</span><span class="formFieldWhite"> <html:select
																				property="sample.containers[${ind}].volumeUnit">
																				<option />
																					<html:options name="sampleContainerInfo"
																						property="volumeUnits" />
																			</html:select> </span> </strong> &nbsp;&nbsp;&nbsp;
																</div>

																<div align="justify"></div>
															</td>
														</tr>
														<tr>
															<td class="formLabel" colspan="2">
																<div align="justify">
																	<strong>Diluents/Solvent <html:text
																			property="sample.containers[${ind}].solvent"
																			size="10" /> &nbsp; &nbsp; &nbsp; Safety Precautions
																		<html:text
																			property="sample.containers[${ind}].safetyPrecaution"
																			size="30" /> &nbsp; &nbsp; &nbsp;</strong>
																</div>
															</td>
														</tr>

														<tr>
															<td class="formLabel style1" colspan="2">
																<div align="justify">
																	<strong>Storage Conditions <span
																		class="formField"><html:text
																				property="sample.containers[${ind}].storageCondition"
																				size="50" /> </span> </strong>
																</div>

																<div align="justify"></div>
															</td>
														</tr>
														<tr>
															<td class="formLabel" colspan="2">
																<strong>Storage Location<br> <br>
																	<table class="topBorderOnly" cellspacing="0"
																		cellpadding="3" align="left" summary="" border="0"
																		width="90%">
																		<strong>
																		<tr>
																			<td class="borderlessLabel">
																				<strong>Room</strong>
																			</td>
																			<td class="borderlessLabel">
																				<html:select
																					property="sample.containers[${ind}].storageLocation.room"
																					onkeydown="javascript:fnKeyDownHandler(this, event);"
																					onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
																					onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
																					onchange="fnChangeHandler_A(this, event);">
																					<option value="">
																						--?--
																					</option>
																					<html:options name="sampleContainerInfo"
																						property="storageRooms" />
																				</html:select>
																			</td>
																			<td class="borderlessLabel">
																				<strong>Freezer</strong>
																			</td>
																			<td class="borderlessLabel">
																				<html:select
																					property="sample.containers[${ind}].storageLocation.freezer"
																					onkeydown="javascript:fnKeyDownHandler(this, event);"
																					onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
																					onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
																					onchange="fnChangeHandler_A(this, event);">
																					<option value="">
																						--?--
																					</option>
																					<html:options name="sampleContainerInfo"
																						property="storageFreezers" />
																				</html:select>
																			</td>
																			<td class="borderlessLabel">
																				<strong>Shelf</strong>
																			</td>
																			<td class="borderlessLabel">
																				<html:select
																					property="sample.containers[${ind}].storageLocation.shelf"
																					onkeydown="javascript:fnKeyDownHandler(this, event);"
																					onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
																					onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
																					onchange="fnChangeHandler_A(this, event);">
																					<option value="">
																						--?--
																					</option>
																					<html:options name="sampleContainerInfo"
																						property="storageShelves" />
																				</html:select>
																			</td>
																			<td class="borderlessLabel">
																				<strong>Box</strong>
																			</td>
																			<td class="borderlessLabel">
																				<html:select
																					property="sample.containers[${ind}].storageLocation.box"
																					onkeydown="javascript:fnKeyDownHandler(this, event);"
																					onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
																					onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
																					onchange="fnChangeHandler_A(this, event);">
																					<option value="">
																						--?--
																					</option>
																					<html:options name="sampleContainerInfo"
																						property="storageBoxes" />
																				</html:select>
																			</td>
																		</tr>
																	</table>
															</td>
														</tr>
														<tr>
															<td class="formLabel" colspan="2">
																<div align="left">
																	<strong>Comments</strong>
																	<br>
																	<span class="formField"><span
																		class="formFieldWhite"> <html:textarea
																				property="sample.containers[${ind}].containerComments"
																				cols="70" /> </span> </span>
																	<br>
																</div>
															</td>
														</tr>
													</tbody>
												</table>
												<br>
											</logic:iterate>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</tbody>
				</table>
				<br>
				<table cellspacing="0" cellpadding="3" width="100%" align="center"
					summary="" border="0">
					<tbody>
						<tr>
							<td align="right">
								<table height="32" cellspacing="0" cellpadding="4" align="right"
									order="0">
									<tbody>
										<tr>
											<td height="32">
												<div align="right">
													<input type="button" value="Reset"
														onClick="javascript:location.href='createSample.do?dispatch=setup&page=0'">
													<input type="hidden" name="dispatch" value="create">
													<input type="hidden" name="page" value="2">
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
 var cal = new calendar2(document.forms['createSampleForm'].elements['sample.dateReceivedStr']);
 cal.year_scroll = true;
 cal.time_comp = false;
 cal.context = '${pageContext.request.contextPath}';
//-->
</script>
