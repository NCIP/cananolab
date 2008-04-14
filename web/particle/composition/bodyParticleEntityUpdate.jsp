<html:form action="/composition">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					Nanoparticle Sample Comosition - Particle Entity
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
					Entity#1:Dendrimer
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

							<html:select styleId="peType"
								property="entity.characterizationSource"
								onchange="javascript:callPrompt('Particle Entity Type', 'peType'); displayCompositionProperty(this);">
								<option value=""></option>
								<option value="biopolymer">
									Biopolymer
								</option>
								<option value="carbonNanotube">
									Carbon Nanotube
								</option>
								<option value="dendrimer">
									Dendrimer
								</option>
								<option value="emulsion">
									Emulsion
								</option>
								<option value="fullerene">
									Fullerene
								</option>
								<option value="liposome">
									Liposome
								</option>
								<option value="metalParticle">
									Metal Particle
								</option>
								<option value="polymer">
									Polymer
								</option>
								<option value="quantumDot">
									Quantum Dot
								</option>
								<option value="other">
									Other
								</option>
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
				<div style="display: none" id="carbonNanotubeTable">
					<table class="topBorderOnly" cellspacing="0" cellpadding="3"
						width="100%" align="center" summary="" border="0">
						<tbody>
							<tr class="topBorder">
								<td class="formTitle" colspan="6">
									<div align="justify">
										Carbon Nanotube Properties
									</div>
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Average Length</strong>
								</td>
								<td class="label">
									<input type="text" name="entity.carbonNanotube.averageLength" />
								</td>
								<td class="label">
									<strong>Average Length Unit</strong>
								</td>
								<td class="label">
									<input type="text" name="entity.carbonNanotube.averageLengthUnit" />
								</td>
								<td class="label">
									<strong>Chirality</strong>
								</td>
								<td class="rightLabel">
									<input type="text" name="entity.carbonNanotube.chirality" />
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Growth Diameter</strong>
								</td>
								<td class="label">
									<input type="text" name="entity.carbonNanotube.growthDiameter" />
								</td>
								<td class="label">
									<strong>Growth Diameter Unit</strong>
								</td>
								<td class="label">
									<input type="text" name="entity.carbonNanotube.growthDiameterUnit" />
								</td>
								<td class="label">
									<strong>Wall Type</strong>
								</td>
								<td class="rightLabel">
									<input type="text" name="entity.carbonNanotube.wallType" />
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div id="biopolymerTable" style="display: none">
					<table class="topBorderOnly" cellspacing="0" cellpadding="3"
						width="100%" align="center" summary="" border="0">
						<tbody>
							<tr class="topBorder">
								<td class="formTitle" colspan="4">
									<div align="justify">
										Biopolymer Properties
									</div>
								</td>
							</tr>
							<tr>
								<td class="LeftLabel">
									<strong>Name</strong>
								</td>
								<td class="label">
									<input type="text" name="entity.biopolymer.name" />
								</td>
								<td class="label">
									<strong>Biopolymer Type</strong>
								</td>
								<td class="rightLabel">
									<select name="entity.biopolymer.type" id="biopolymerType"
										onchange="javascript:callPrompt('Biopolymer Type', 'biopolymerType');">
										<option value="dna">
											DNA
										</option>
										<option value="peptide">
											Peptide
										</option>
										<option value="protein">
											Protein
										</option>
										<option value="other">
											Other
										</option>
									</select>
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Sequence</strong>
								</td>
								<td class="rightLabel" colspan="3">
									<textarea name="entity.biopolymer.sequence" cols="80" rows="3"></textarea>
								</td>
							</tr>
						</tbody>
					</table>
				</div>

				<div id="dendrimerTable" style="display: none">
					<table class="topBorderOnlyTable" cellspacing="0" cellpadding="3"
						width="100%" align="center" summary="" border="0">
						<tbody>
							<tr class="topBorder">
								<td class="formTitle" colspan="4">
									<div align="justify">
										Dendrimer Properties
									</div>
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Branch</strong>
								</td>
								<td class="label">
									<input type="text" name="entity.dendrimer.branch" />
								</td>
								<td class="label">
									<strong>Generation</strong>
								</td>
								<td class="rightLabel">
									<input type="text" name="entity.dendrimer.generation" />
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div id="emulsionTable" style="display: none">
					<table class="topBorderOnlyTable" cellspacing="0" cellpadding="3"
						width="100%" align="center" summary="" border="0">
						<tbody>
							<tr class="topBorder">
								<td class="formTitle" colspan="6">
									<div align="justify">
										Emulsion Properties
									</div>
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Is Polymerized</strong>
								</td>
								<td class="label">
									<input type="text" name="entity.emulsion.polymerized" id="initiator" />
								</td>
								<td class="label">
									<strong>Polymer Name</strong>
								</td>
								<td class="label">
									<input type="text" name="entity.emulsion.polymerName" />
								</td>
								<td class="label">
									<strong>Type</strong>
								</td>
								<td class="rightLabel">
									<input type="text" name="entity.emulsion.emulsionType" />
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div style="display: none" id="fullereneTable">
					<table class="topBorderOnlyTable" cellspacing="0" cellpadding="3"
						width="100%" align="center" summary="" border="0">
						<tbody>
							<tr class="topBorder">
								<td class="formTitle" colspan="6">
									<div align="justify">
										Fullerene Properties
									</div>
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Average Diameter</strong>
								</td>
								<td class="label">
									<input type="text" name="entity.fullerene.averageDiameter"
										id="averageDiameter" />
								</td>
								<td class="label">
									<strong>Average Diameter Unit</strong>
								</td>
								<td class="label">
									<input type="text" name="entity.fullerene.averageDiameterUnit"
										id="averageDiameter" />
								</td>
								<td class="label">
									<strong>Number of Carbons</strong>
								</td>
								<td class="rightLabel">
									<input type="text" name="entity.fullerene.numberOfCarbons"
										id="numberOfCarbon" />
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div style="display: none" id="liposomeTable">
					<table class="topBorderOnly" cellspacing="0" cellpadding="3"
						width="100%" align="center" summary="" border="0">
						<tbody>
							<tr class="topBorder">
								<td class="formTitle" colspan="4">
									<div align="justify">
										Liposome Properties
									</div>
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Polymer Name</strong>
								</td>
								<td class="label">
									<input type="text" name="entity.liposome.polymerName" />
								</td>
								<td class="label">
									<strong>Is Polymerized</strong>
								</td>
								<td class="rightLabel">
									<input type="text" name="entity.liposome.polymerized" />
								</td>
							</tr>
						</tbody>
					</table>
				</div>

				<div style="display: none" id="polymerTable">
					<table class="topBorderOnly" cellspacing="0" cellpadding="3"
						width="100%" align="center" summary="" border="0">
						<tbody>
							<tr class="topBorder">
								<td class="formTitle" colspan="6">
									<div align="justify">
										Polymer Properties
									</div>
								</td>
							</tr>
							<tr>
								<td class="leftLabel">
									<strong>Initiator</strong>
								</td>
								<td class="label">
									<input type="text" name="entity.polymer.initiator" />
								</td>
								<td class="label">
									<strong>Cross Link Degree</strong>
								</td>
								<td class="label">
									<input type="text" name="entity.polymer.crosslinkDegree" />
								</td>
								<td class="label">
									<strong>Is Cross Linked</strong>
								</td>
								<td class="rightLabel">
									<input type="text" name="entity.polymer.crosslinked" />
								</td>
							</tr>
						</tbody>
					</table>
				</div>

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
																	property="entity.composingElements[0].type">
																	<html:options name="allComposingElementTypes" />
																	<c:if
																		test="${nanoparticleCompositionForm.map.particle.sampleType eq 'Complex Particle'}">
																		<html:options name="allParticleElementTypes" />
																	</c:if>
																	<option value="other">
																		Other
																	</option>
																</html:select>
															</td>

															<td class="labelWithTop" valign="top">
																<strong>Chemical Name</strong>
															</td>
															<td class="rightLabelWithTop" valign="top">
																<html:text
																	property="entity.composingElements[0].chemicalName"
																	size="30" />
															</td>
														</tr>
														<tr>
															<td class="leftLabel" valign="top">
																<strong>Molecular Formula Type</strong>
															</td>
															<td class="label" valign="top">
																<html:select styleId="molFormulaType"
																	property="entity.composingElements[0].molecularFormulaType"
																	onchange="javascript:callPrompt('Molecular Formula Type', 'molFormulaType');">
																	<option value="" />
																	<option value="other">
																		Other
																	</option>
																</html:select>
															</td>
															<td class="label" valign="top">
																<strong>Molecular Formula</strong>
															</td>
															<td class="rightLabel" valign="top">
																<html:text
																	property="entity.composingElements[0].molecularFormula"
																	size="30" />
															</td>
														</tr>
														<tr>
															<td class="leftLabel" valign="top">
																<strong>Value</strong>
															</td>
															<td class="label" valign="top">
																<html:text
																	property="entity.composingElements[0].value"
																	size="30" />
															</td>
															<td class="label" valign="top">
																<strong>Unit</strong>
															</td>
															<td class="rightLabel" valign="top">
																<html:select styleId="compEleUnit"
																	property="entity.composingElements[0].valueUnit">
																	<option value="" />
																	<option value="other">
																		Other
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
																	property="entity.composingElements[0].description"
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
																					<select name="entity.composingElements[0].inherentFunctionCollection[0].inherentFuncType" size="1"
																						id="funcType0">
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
																							Other
																						</option>
																					</select>
																				</td>
																				<td class="label">
																					<html:textarea
																						property="entity.composingElements[0].inherentFunctionCollection[0].description"
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
												<html:hidden property="particle.sampleId" />
												<html:hidden property="particle.sampleName" />
												<html:hidden property="particle.sampleSource" />
												<html:hidden property="particle.sampleType" />
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
