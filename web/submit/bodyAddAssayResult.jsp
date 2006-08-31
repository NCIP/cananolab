<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html:form action="/publishAssayResult">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					<br>
					Publish Assay Result File
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
									Assay Result Description
								</div>
							</td>
						</tr>
						<tr>
							<td class="leftLabel" valign="top">
								<strong>Particle ID*</strong>
							</td>
							<td class="rightLabel" colspan="3">
								<html:select property="particleNames" multiple="true" size="3">
									<html:options name="allSampleNames" />
								</html:select>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<strong> <html:radio property="fileSource" value="chooseExisting">Choose File From Existing Assays</html:radio>
									<table cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
										<tr>
											<td class="borderlessLabel" width="50">
											</td>
											<td class="borderlessLabel">
												<strong>Assay Type</strong>
											</td>
											<td class="borderlessLabel">
												<html:select property="assayType">
													<option value="Prescreening">
														Pre-Screening
													</option>
													<option value="In Vitro">
														In Vitro
													</option>
												</html:select>
											</td>
											<td class="borderlessLabel">
												<strong>Assay Name</strong>
											</td>
											<td class="borderlessLabel" colspan="3">
												<html:select property="assayName">
													<option value="PCC-1">
														PCC-1
													</option>
													<option value="STE-1">
														STE-1
													</option>
												</html:select>
											</td>
										</tr>
										<tr>
											<td class="borderlessLabel" width="50">
											</td>
											<td class="borderlessLabel">
												<strong>=>&nbsp; Assay Result File*</strong>
											</td>
											<td class="borderlessLabel">
												<html:select property="fileId">
													<option value="file1">
														PCC-1 Output File 1
													</option>
													<option value="file2">
														PCC-1 Output File 2
													</option>
												</html:select>
											</td>
										</tr>
									</table> <br> <br> <html:radio property="fileSource" value="new">Upload New File</html:radio> </strong>
								<table cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
									<td class="borderlessLabel" width="50">
									</td>
									<td class="borderlessLabel" valign="top">
										<strong>=>&nbsp; Assay Result File*</strong>
									</td>
									<td class="borderlessLabel" valign="top" colspan="3">
										<html:file property="file" />
									</td>
								</table>
							</td>
						</tr>
						<tr>
							<td class="leftLabel" valign="top">
								<strong> Characterization Type </strong>
							</td>
							<td class="label">
								<a href="#" onclick="javascript:dynamicDropdown('physical', document.searchNanoparticleForm.characterizations, charTypeChars)">Physical Characterization</a>
								<br>
								In Vitro Characterization
								<br>
								<span class="indented"><a href="#" onclick="javascript:dynamicDropdown('toxicity', document.searchNanoparticleForm.characterization, charTypeChars)">Toxicity</a> <br> <span class="indented2"><a href="#"
										onclick="javascript:dynamicDropdown('cytoTox', document.searchNanoparticleForm.characterization, charTypeChars)">Cytotoxicity</a> </span> <br> <span class="indented2">Immunotoxicity</span> <br> <span class="indented3"><a href="#"
										onclick="javascript:dynamicDropdown('bloodContactTox', document.searchNanoparticleForm.characterization, charTypeChars)">Blood Contact </a></span> <br> <span class="indented3"><a href="#"
										onclick="javascript:dynamicDropdown('immuneCellFuncTox', document.searchNanoparticleForm.characterization, charTypeChars)">Immune Cell Function </a></span> <br> <span class="indented2"><a href="#"
										onclick="javascript:dynamicDropdown('metabolicStabilityTox', document.searchNanoparticleForm.characterization, charTypeChars)">Metabolic Stability </a></span> <br> <a href="#"
									onclick="javascript:dynamicDropdown('invivo', document.searchNanoparticleForm.characterization, charTypeChars)">In Vivo Characterization</a>
							</td>
							<td class="label" valign="top">
								<strong>=>&nbsp; Assay Result File Type*</strong>
							</td>
							<td class="rightLabel" valign="top">
								<strong> <html:select property="assayResultFileType">
										<option value="DLS Size Distribution">
											DLS Size Distribution
										</option>
										<option value="Weight Distribution">
											Weight Distribution
										</option>
									</html:select></strong>
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
								<strong>Comments</strong>
							</td>
							<td class="rightLabel" colspan="3">
								<html:textarea property="comments" rows="3" cols="60" />
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
												<input type="reset" value="Reset" onclick="javascript:resetSelect(document.submitReportForm.particleNames));">
												<input type="hidden" name="dispatch" value="submit">
												<input type="hidden" name="page" value="1">
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
