<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type="text/javascript" src="javascript/POCManager.js"></script>
<table width="100%" border="0" align="center" cellpadding="3"
	cellspacing="0" class="topBorderOnly" summary="">
	<tr>
		<td class="completeLabelNoTop">
			<table width="50%" border="0" align="center" cellpadding="3"
				cellspacing="0" class="topBorderOnly" summary="">
				<tr>
					<td class="leftLabelWithTop">
						<strong>Technique</strong>
					</td>
					<td class="rightLabelWithTop">
						<html:select
							property="achar.theExperimentConfig.domain.technique.abbreviation"
							styleId="techniqueType"
							onchange="javascript:callPrompt('Technique Type', 'techniqueType');">
							<option value=""></option>
							<html:options collection="allTechniques" labelProperty="type"
								property="id" />
							<option value="other">
								[Other]
							</option>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="leftLabel">
						<strong>Abbreviation</strong>
					</td>
					<td class="rightLabel">
						<html:text
							property="achar.theExperimentConfig.domain.technique.abbreviation"
							size="30" />
					</td>
				</tr>
				<tr>
					<td class="completeLabel" colspan="2" valign="top">
						<table class="smalltable" border="0" width="100%">
							<tr class="smallTableHeader">
								<th>
									Instrument Type
								</th>
								<th>
									Manufacturer
								</th>
								<th colspan="2">
									Model Name
								</th>
							</tr>
							<c:choose>
								<c:when test="${!empty achar.theExperimentConfig.instruments}">
									<tr>
										<logic:iterate name="${param.formName}"
											property="achar.theExperimentConfig.instruments"
											id="instrument" indexId="instrumentInd">
											<tr>
												<td>
													<html:text
														property="achar.theExperimentConfig.instruments[${instrumentInd}].type"
														size="17" />
												</td>
												<td>
													<html:text
														property="achar.theExperimentConfig.instruments[${instrumentInd}].type"
														size="17" />
												</td>
												<td>
													<html:text
														property="achar.theExperimentConfig.instruments[${instrumentInd}].type"
														size="17" />
												</td>
												<td>
													<a style="" id="removeInstrument" href="#"> <span
														class="addLink2">remove</span> </a> &nbsp;
												</td>
											</tr>
										</logic:iterate>
									</tr>
								</c:when>
							</c:choose>
							<tr>
								<td class="completeLabel" colspan="4">
									<a href="#"
										onclick="javascript:addComponent(${param.formName}, 'submitExperimentConfig', 'addInstrument'); return false;">
										<span class="addLink2">Add Instrument</span>
								</td>
							</tr>
						</table>
					</td>
				</tr>

				<tr>
					<td class="leftLabel" valign="top">
						<strong>Description</strong>
					</td>
					<td class="rightLabel">
						<html:textarea
							property="achar.theExperimentConfig.domain.description" rows="3"
							cols="80" />
					</td>
				</tr>


			</table>
			<br>
			<table width="100%" border="0" align="center" cellpadding="3"
				cellspacing="0" class="topBorderOnly" summary="">
				<tr>
					<td>
						<span class="formMessage"> </span>
						<br>
						<table border="0" align="right" cellpadding="4" cellspacing="0">
							<tr>
								<td>
									<div align="right">
										<input type="reset" value="Reset"
											onclick="javascript:window.location.href='${origUrl}'">
										<input type="button" value="Submit"
											onclick="javascript:submitAction(characterizationForm, 
										'invitroCharacterization', 'submitExperimentConfig');">
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

