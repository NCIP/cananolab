<html:form action="/chemicalAssociation">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					Nanoparticle Sample Comosition - Chemical Association
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
					<%--					Entity#1:Dendrimer--%>
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
								Chemical Association Information
							</div>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Association Type</strong>
						</td>
						<td class="label">
							<html:select styleId="assoType"
								property="composition.characterizationSource"
								onchange="javascript:callPrompt('Association Type', 'assoType');">
								<option value=""></option>
								<option value="attachment">
									attachment
								</option>
								<option value="encapsulation">
									encapsulation
								</option>
								<option value="other">
									[Other]
								</option>
							</html:select>
						</td>
						<td class="label" valign="top">
							&nbsp;
							<Strong style="display:none">Bond Type</Strong>
						</td>
						<td class="rightLabel">
							&nbsp;
							<html:select styleId="bondType" style="display:none"
								property="composition.characterizationSource"
								onchange="javascript:callPrompt('Bond Type', 'bondType');">
								<option value=""></option>
								<option value="covalent">
									covalent
								</option>
								<option value="other">
									[Other]
								</option>
							</html:select>
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<table width="100%" border="0" align="center" cellpadding="3"
								cellspacing="0" class="topBorderOnly">
								<tr>

									<td class="label">
										<html:select styleId="particleFuncEntityTypeA"
											property="composition.characterizationSource"
											onchange="selectEntityTypeOptionsA()">
											<option value=""></option>
											<option value="nanoparticleEntity">
												nanoparticle entity
											</option>
											<option value="functionalizingEntity">
												functionalizing entity
											</option>
										</html:select>
									</td>
									<td class="label" valign="top">
										&nbsp;
										<html:select styleId="entityTypeA" style="display:none"
											property="composition.characterizationSource">
											<option value="">
												- Select Entity Type First -
											</option>
										</html:select>
									</td>
									<td class="rightLabel">
										&nbsp;
										<html:select styleId="compEleTypeA" style="display:none"
											property="composition.characterizationSource"
											onchange="javascript:callPrompt('Composing Element A', 'compEleTypeA');">
											<option value=""></option>
											<html:options name="allComposingElementTypes" />
											<option value="other">
												[Other]
											</option>
										</html:select>
									</td>
								</tr>
							</table>
						</td>
						<td>
							<strong>Associated With</strong>
						</td>
						<td>
							<table width="100%" border="0" align="center" cellpadding="3"
								cellspacing="0" class="topBorderOnly">
								<tr>

									<td class="label">
										<html:select styleId="particleFuncEntityTypeB"
											property="composition.characterizationSource"
											onchange="selectEntityTypeOptionsB()">
											<option value=""></option>
											<option value="nanoparticleEntity">
												nanoparticle entity
											</option>
											<option value="functionalizingEntity">
												functionalizing entity
											</option>
										</html:select>
									</td>
									<td class="label">
										&nbsp;
										<html:select styleId="entityTypeB" style="display:none"
											property="composition.characterizationSource">
											<option value="">
												- Select Entity Type First -
											</option>
										</html:select>
									</td>
									<td class="rightLabel">
										&nbsp;
										<html:select styleId="compEleTypeB" style="display:none"
											property="composition.characterizationSource"
											onchange="javascript:callPrompt('Composing Element B', 'compEleTypeB');">
											<option value=""></option>
											<html:options name="allComposingElementTypes" />
											<option value="other">
												[Other]
											</option>
										</html:select>
									</td>
								</tr>
							</table>
						</td>
					</tr>

					<tr>
						<td class="leftLabel" valign="top">
							<strong>Association Description</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<html:textarea property="composition.description" rows="2"
								cols="60" />
						</td>
					</tr>
				</table>
				<br>
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify" id="peFileTitle">
									Chemical Association File Information
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
																		[Other]
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
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" class="topBorderOnly" summary="">
					<tr>
						<td width="30%">
							<span class="formMessage"> </span>
							<br>
							<c:choose>
								<c:when
									test="${param.dispatch eq 'setupUpdate' && canUserDelete eq 'true'}">
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
								</c:when>
							</c:choose>
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
