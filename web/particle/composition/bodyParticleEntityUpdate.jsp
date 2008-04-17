<html:form action="/nanoparticleEntity">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					Sample Comosition - Nanoparticle Entity
				</h4>
			</td>
			<td align="right" width="15%">
				<a
					href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=composition_help')"
					class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<h5 align="center">
					Entity#2:Dendrimer
				</h5>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=particle" />
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" class="topBorderOnly" summary="" id="summary">
					<tr>
					<tr class="topBorder">
						<td class="formTitle" colspan="4">
							<div align="justify">
								Summary
							</div>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Nanoparticle Entity Type</strong>
						</td>
						<td class="rightLabel">
							<html:select styleId="peType" property="entity.type"
								onchange="javascript:callPrompt('Particle Entity Type', 'peType'); 
											setEntityInclude(); getComposingElementOptions('peType', 'compElemType');">
								<option value=""></option>
								<html:options name="nanoparticleEntityTypes" />
								<option value="other">[Other]</option>
							</html:select>
							
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Description</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<html:textarea property="entity.description" rows="3"
								cols="80" />
						</td>
					</tr>
				</table>

				<br>
                <div id="entityInclude"></div>
                <br> 
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify" id="compEleInfoTitle">
									Composing Element Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<table border="0" width="100%">
									<tr>
										<td valign="bottom">
											<a href="#"
												onclick="javascript:addComposingElement(); return false;">
												<span class="addLink">Add Composing Element</span>
											</a>
										</td>
										<td id="compEleTd">
											<span id="compEleCount" style="display:none">1</span>
											<div id="compEle1">
												<table class="topBorderOnly" cellspacing="0" cellpadding="3"
													width="100%" align="center" summary="" border="0">
													<tbody>
														<tr>
															<td class="formSubTitleNoRight" colspan="3">
																<span>Composing Element #1</span>
															</td>
															<td class="formSubTitleNoLeft" align="right">
																<a href="#" id="removeCE1"
																	onclick="removeComposingElement();"> <img
																		src="images/delete.gif" border="0"
																		alt="remove this composing element"> </a>
															</td>

														</tr>
														<tr>
															<td class="leftLabelWithTop" valign="top">
																<strong>Composing Element Type*</strong>
															</td>
															<td class="labelWithTop" valign="top">
																<html:select styleId="compElemType"
																	property="entity.composingElements[0].domainComposingElement.type"
																	onchange="javascript:callPrompt('Composing Element Type', 'compElemType');">
																	<option />
																	<option value="other">
																		[Other]
																	</option>
																</html:select>
																<%--																	<html:options name="defaultComposingElementTypes" />--%>
															</td>

															<td class="labelWithTop" valign="top">
																<strong>Chemical Name</strong>
															</td>
															<td class="rightLabelWithTop" valign="top">
																<html:text
																	property="entity.composingElements[0].domainComposingElement.name"
																	size="30" />
															</td>
														</tr>
														<tr>
															<td class="leftLabel" valign="top">
																<strong>Molecular Formula Type</strong>
															</td>
															<td class="label" valign="top">
																<html:select styleId="molFormulaType"
																	property="entity.composingElements[0].domainComposingElement.molecularFormulaType"
																	onchange="javascript:callPrompt('Molecular Formula Type', 'molFormulaType');">
																	<option value="" />
																	<option value="other">
																		[Other]
																	</option>
																</html:select>
															</td>
															<td class="label" valign="top">
																<strong>Molecular Formula</strong>
															</td>
															<td class="rightLabel" valign="top">
																<html:text
																	property="entity.composingElements[0].domainComposingElement.molecularFormula"
																	size="30" />
															</td>
														</tr>
														<tr>
															<td class="leftLabel" valign="top">
																<strong>Value</strong>
															</td>
															<td class="label" valign="top">
																<html:text
																	property="entity.composingElements[0].domainComposingElement.value"
																	size="30" />
															</td>
															<td class="label" valign="top">
																<strong>Unit</strong>
															</td>
															<td class="rightLabel" valign="top">
																<html:select styleId="compEleUnit"
																	property="entity.composingElements[0].domainComposingElement.valueUnit"
																	onchange="javascript:callPrompt('Unit', 'compEleUnit');">
																	<option value="" />																																
																	<option value="other">
																		[Other]
																	</option>
																</html:select>
															</td>
														</tr>
														<tr>
															<td class="leftLabel" valign="top" colspan="1">
																<strong>Description</strong>
															</td>
															<td class="rightLabel" colspan="3">
																<html:textarea
																	property="entity.composingElements[0].domainComposingElement.description"
																	rows="3" cols="65" />
															</td>
														</tr>
														<tr>
															<td valign="bottom" class="leftLabel">
																<a href="#" id="inherentFuncLink1"><span style="display:none">1</span>
																<span class="addLink2">Add
																		Inherent Function</span> </a>
															</td>
															<td colspan="3" class="rightLabel">
																&nbsp;
																<span style="display: none">0</span>
																<input type="hidden" name="inherentFunctionNumbers"
																	value="1" id="inherentFuncNum" />

																<div style="display: none">
																	<table class="topBorderOnly" cellspacing="0"
																		cellpadding="3" width="100%" align="center" summary=""
																		border="0">
																		<tbody>
																			<tr>
																				<td class="leftLabelWithTop" valign="top">
																					<strong>Function Type</strong>
																				</td>
																				<td class="labelWithTop" valign="top">
																					<strong>Description</strong>
																				</td>
																				<td class="rightLabelWithTop">
																					<Strong>&nbsp;</Strong>
																				</td>
																			</tr>
																			<tr style="display: none">
																				<td class="leftLabel" valign="top">
																					<html:select property="entity.composingElements[0].inherentFunctions[0].type" size="1"
																						styleId="funcType"
																						onchange="javascript:callPrompt('Function Type', 'functionType');">																						
																						<option value="imaging">
																							Imaging
																						</option>
																						<option value="targeting">
																							Targeting
																						</option>
																						<option value="therapeutic">
																							Therapeutic
																						</option>
																						<option value="other">
																							[Other]
																						</option>
																					</html:select>
																				</td>
																				<td class="label">
																					<html:textarea
																						property="entity.composingElements[0].inherentFunctions[0].description"
																						rows="1" cols="30" />
																				</td>
																				<td class="rightLabel">
																					<a href="#"><span class="addLink2">remove</span>
																					</a>
																				</td>
																			</tr>
																		</tbody>
																	</table>
																</div>
															</td>
														</tr>
													</tbody>
												</table>
												<br>
											</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
				</table>



				<%--Particle Entity File Information --%>
				<br>
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify" id="peFileTitle">
									Particle Entity File Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">

								<table border="0" width="100%">
									<tr>
										<td valign="bottom">
											<a href="#"
												onclick="javascript:addFileClone(); return false;"> <span
												class="addLink">Add File</span> </a>
										</td>
										<td id="fileTd">
											<div id="filePatternDiv" style="display: none;">
												<table class="topBorderOnly" cellspacing="0" cellpadding="3"
													width="100%" align="center" summary="" border="0">
													<tbody>
														<tr>
															<td class="formSubTitleNoRight">
																<span id="fileCount">0</span>
															</td>
															<td class="formSubTitleNoLeft" align="right">
																<a href="#"> <img src="images/delete.gif" border="0"
																		alt="remove this file table"> </a>
															</td>
														</tr>
														<tr>
															<td class="leftLabelWithTop" valign="top">
																<strong>File Name</strong>

															</td>
															<td class="rightLabelWithTop" valign="top">
																&nbsp;&nbsp;Click on "Load File" button
																&nbsp;&nbsp;&nbsp;&nbsp;
																<input type="button"
																	onclick="javascript:loadFile(this.form, 'particleEntity', 0)"
																	value="Load File">
															</td>
														</tr>
														<tr>
															<td class="leftLabel" valign="top">
																<strong>File Type</strong>
															</td>
															<td class="rightLabel" valign="rightLabelWithtop">
																<select name="fileType" id="fileType">
																	<option value="" />
																	<option value="other">
																		Other
																	</option>
																</select>
															</td>
														</tr>
													</tbody>
												</table>
												<br>
											</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
				</table>
				<br>
				<jsp:include page="/particle/shared/bodyCharacterizationCopy.jsp" />
				<br>
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" class="topBorderOnly" summary="">
					<tr>
						<td width="30%">
							<span class="formMessage"> </span>
							<br>
									<table height="32" border="0" align="left" cellpadding="4"
										cellspacing="0">
										<tr>
											<td height="32">
												<div align="left">
													<input type="button" value="Delete"
														onclick="confirmDeletion();">
												</div>
											</td>
										</tr>
									</table>
							<table width="498" height="32" border="0" align="right"
								cellpadding="4" cellspacing="0">
								<tr>
									<td width="490" height="32">
										<div align="right">
											<div align="right">
												<input type="reset" value="Reset" onclick="">
												<input type="hidden" name="dispatch" value="create">
												<input type="hidden" name="page" value="2">
												<input type="hidden" name="submitType"
													value="${param.submitType}" />
<%--												<html:hidden property="particle.sampleId" />--%>
<%--												<html:hidden property="particle.sampleName" />--%>
<%--												<html:hidden property="particle.sampleSource" />--%>
<%--												<html:hidden property="particle.sampleType" />--%>
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
