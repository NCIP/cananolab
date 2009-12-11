<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<%--TODO: create online help topic for this page.--%>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Submit Study Sample" />
	<jsp:param name="topic" value="submit_study_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<jsp:include page="/bodyMessage.jsp?bundle=study" />
<table width="100%" align="center" class="submissionView">
	<tr>
		<td>
			<b><a href="sample.do?dispatch=setupNew&page=0">Create a new sample</a></b> or search existing sample below.
		</td>
	</tr>
</table>
<br>
<form name="advancedSampleSearchForm" method="post" action="/caNanoLab/study.do" enctype="multipart/form-data" onsubmit="return validateDatumValue()"><div><input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="7bd73e9b844f8e8d0ecd5cf23dff9f1c"></div>
	<table width="100%" align="center" class="submissionView">
		<tr>
			<th>
				Sample Criteria
			</th>
		</tr>
		<tr>
			<td>
				<table id="sampleQueryTable" class="summaryViewLayer4" width="85%"
					style="display: none;">

					<tbody id="sampleQueryRows">
						<tr id="samplePattern" style="display: none;">
							<td>
								<span id="nameTypeValue">Sample POC Type</span>
							</td>
							<td>
								<span id="sampleOperandValue">Sample Operand</span>
							</td>

							<td>
								<span id="nameValue">Sample POC Value</span>
							</td>
							<td>
								<input class="noBorderButton" id="sampleEdit" type="button"
									value="Edit" onclick="editSampleQuery(this.id);" />
							</td>
						</tr>
					</tbody>

				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table id="newSampleQuery" style="display: block;" class="promptbox">
					<tr>
						<td>
							<input type="hidden" name="searchBean.theSampleQuery.id" value="" id="sampleQueryId">

							<select name="searchBean.theSampleQuery.nameType" id="nameType"><option value="">
									-- Please Select --
								</option>
								<option value="sample name">
									sample name
								</option>
								<option value="point of contact name">
									point of contact name
								</option></select>
						</td>

						<td>
							<select name="searchBean.theSampleQuery.operand" id="sampleOperand"><option value="">
									-- Please Select --
								</option>
								<option value="equals">equals</option>
<option value="contains">contains</option></select>
						</td>
						<td>
							<input type="text" name="searchBean.theSampleQuery.name" size="50" value="" id="name">

						</td>
					</tr>
					<tr>
						<td colspan="3">
							<table cellspacing="0" align="left">
								<tr>
									<td>
										<input class="promptButton" type="button" value="Add"
											onclick="addSampleQuery();show('sampleQueryTable');" />
									</td>

									<td>
										<input class="promptButton" type="button" value="Reset"
											onclick="clearSampleQuery();" />
									</td>
									<td>
										<input style="display: none;" class="promptButton"
											id="deleteSampleQuery" type="button" value="Remove"
											onclick="deleteTheSampleQuery()" />
									</td>
								</tr>
							</table>
						</td>

					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<div id="sampleLogicalOperator" style="display: none">
					<input type="radio" name="searchBean.sampleLogicalOperator" value="and" checked="checked">
					AND
					<input type="radio" name="searchBean.sampleLogicalOperator" value="or">

					OR
				</div>
			</td>
		</tr>
	</table>
	<br />
	<table width="100%" align="center" class="submissionView">
		<tr>
			<th>

				Composition Criteria
			</th>
		</tr>
		<tr>
			<td>
				<table id="compositionQueryTable" class="summaryViewLayer4"
					width="85%" style="display: none;">
					<tbody id="compositionQueryRows">
						<tr id="compPattern" style="display: none;">
							<td width="25%">

								<span id="compTypeValue">Composition Type</span>
							</td>
							<td width="20%">
								<span id="entityTypeValue">Entity Type</span>
							</td>
							<td width="20%">
								<span id="compOperandValue">Operand</span>

							</td>
							<td width="20%">
								<span id="chemicalNameValue">Chemical Name</span>
							</td>
							<td>
								<input class="noBorderButton" id="compEdit" type="button"
									value="Edit" onclick="editCompositionQuery(this.id);" />
							</td>
						</tr>

					</tbody>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table id="newCompositionQuery" style="display: block;"
					class="promptbox">
					<tr>
						<td>

							<input type="hidden" name="searchBean.theCompositionQuery.id" value="" id="compQueryId">
							<select name="searchBean.theCompositionQuery.compositionType" onchange="javascript:setCompositionEntityOptions();" id="compType"><option value="">
									-- Please Select --
								</option>
								<option value="nanomaterial entity">
									nanomaterial entity
								</option>
								<option value="functionalizing entity">
									functionalizing entity
								</option>

								<option value="function">
									function
								</option></select>
						</td>
						<td>
							<select name="searchBean.theCompositionQuery.entityType" id="entityType"><option value="">
									-- Please Select --
								</option></select>
						</td>
						<td class="cellLabel">

							<span id="compChemicalNameLabel">with chemical name</span>
						</td>
						<td>
							<select name="searchBean.theCompositionQuery.operand" id="compOperand"><option value="">
									-- Please Select --
								</option>
								<option value="equals">equals</option>
<option value="contains">contains</option></select>

						</td>
						<td>
							<input type="text" name="searchBean.theCompositionQuery.chemicalName" size="50" value="" id="chemicalName">
						</td>
					</tr>
					<tr>
						<td colspan="5">
							<table cellspacing="0" align="left">
								<tr>

									<td>
										<input class="promptButton" type="button" value="Add"
											onclick="addCompositionQuery();show('characterizationQueryTable');" />
									</td>
									<td>
										<input class="promptButton" type="button" value="Reset"
											onclick="clearCompositionQuery();" />
									</td>
									<td>
										<input style="display: none;" class="promptButton"
											id="deleteCompositionQuery" type="button" value="Remove"
											onclick="deleteTheCompositionQuery()" />
									</td>

								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>

				<div id="compositionLogicalOperator" style="display: none">
					<input type="radio" name="searchBean.compositionLogicalOperator" value="and" checked="checked">
					AND
					<input type="radio" name="searchBean.compositionLogicalOperator" value="or">
					OR
				</div>
			</td>
		</tr>
	</table>
	<br />

	<table width="100%" align="center" class="submissionView">
		<tr>
			<th>
				Characterization Criteria
			</th>
		</tr>
		<tr>
			<td>
				<table id="characterizationQueryTable" class="summaryViewLayer4"
					width="85%" style="display: none;">

					<tbody id="characterizationQueryRows">
						<tr id="charPattern" style="display: none;">
							<td width="15%">
								<span id="charTypeValue">Characterization Type</span>
							</td>
							<td width="15%">
								<span id="charNameValue">Characterization Name</span>
							</td>

							<td width="15%">
								<span id="datumNameValue">Datum Name</span>
							</td>
							<td width="10%">
								<span id="charOperandValue">Operand</span>
							</td>
							<td width="15%">
								<span id="datumValueValue">Datum Value</span>

							</td>
							<td>
								<span id="datumValueUnitValue">Datum Unit</span>
							</td>
							<td>
								<input class="noBorderButton" id="charEdit" type="button"
									value="Edit" onclick="editCharacterizationQuery(this.id);" />
							</td>
						</tr>

					</tbody>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table id="newCharacterizationQuery" style="display: block;"
					class="promptbox">
					<tr>
						<td>

							<input type="hidden" name="searchBean.theCharacterizationQuery.id" value="" id="charQueryId">
							<select name="searchBean.theCharacterizationQuery.characterizationType" onchange="javascript:setCharacterizationOptions();" id="charType"><option value="">
									-- Please Select --
								</option>
								<option value="physico chemical characterization">physico chemical characterization</option>
<option value="invitro characterization">invitro characterization</option>
<option value="invivo characterization">invivo characterization</option>
<option value="ex vivo">[ex vivo]</option></select>
						</td>

						<td>
							<select name="searchBean.theCharacterizationQuery.characterizationName" onchange="javascript:setDatumNameOptionsByCharName();" id="charName"><option value="">
									-- Please Select --
								</option></select>
						</td>
						<td>
							<select name="searchBean.theCharacterizationQuery.datumName" onchange="javascript:setDatumValueUnitOptions();setCharacterizationOperandOptions();setDatumValueOptions()" id="datumName"><option value="">
									-- Please Select --
								</option></select>
						</td>

						<td>
							<select name="searchBean.theCharacterizationQuery.operand" id="charOperand"><option value="">
									-- Please Select --
								</option>
								<option value="=">=</option>
<option value="&gt;">&gt;</option>
<option value="&gt;=">&gt;=</option>
<option value="&lt;">&lt;</option>
<option value="&lt;=">&lt;=</option></select>
						</td>

						<td>
							<div id="datumValueTextBlock" style="display: block">
								<input type="text" name="searchBean.theCharacterizationQuery.datumValue" size="10" value="" id="datumValue">
								
							</div>
							<div id="datumValueSelectBlock" style="display: none">
								<select name="searchBean.theCharacterizationQuery.datumValue" id="datumValueSelect"><option value="">
										-- Please Select --
									</option>
									<option value="1">true</option>

<option value="0">false</option></select>
							</div>
						</td>
						<td>
							<div id="datumValueUnitBlock" style="display: none">
								<select name="searchBean.theCharacterizationQuery.datumValueUnit" id="datumValueUnit"><option value="">
									</option></select>
							</div>
						</td>

					</tr>
					<tr>
						<td colspan="6">
							<table cellspacing="0" align="left">
								<tr>
									<td>
										<input class="promptButton" type="button" value="Add"
											onclick="addCharacterizationQuery();show('characterizationQueryTable');" />
									</td>
									<td>

										<input class="promptButton" type="button" value="Reset"
											onclick="clearCharacterizationQuery();" />
									</td>
									<td>
										<input style="display: none;" class="promptButton"
											id="deleteCharacterizationQuery" type="button" value="Remove"
											onclick="deleteTheCharacterizationQuery()" />
									</td>
								</tr>
							</table>
						</td>
					</tr>

				</table>
			</td>
		</tr>
		<tr>
			<td>
				<div id="characterizationLogicalOperator" style="display: none">
					<input type="radio" name="searchBean.characterizationLogicalOperator" value="and" checked="checked">
					AND
					<input type="radio" name="searchBean.characterizationLogicalOperator" value="or">

					OR
				</div>
			</td>
		</tr>
	</table>
	<br />
	<table width="100%" align="center" class="submissionView"
		style="border: 0">
		<tr>
			<td>
				<div id="logicalOperator" style="display: block">
					<input type="radio" name="searchBean.logicalOperator" value="and">
					AND
					<input type="radio" name="searchBean.logicalOperator" value="or" checked="checked">
					OR
				</div>
			</td>
		</tr>
	</table>
	<br />
	<table width="100%" border="0" align="center" cellpadding="3"
		cellspacing="0">
		<tr>
			<td>
				<span class="formMessage"> <em>Searching without any
						parameters would return all samples.</em> </span>
				<br>
				<table border="0" align="right" cellpadding="4" cellspacing="0">
					<tr>
						<td>
							<div align="right">
								<input type="button" value="Reset"
									onclick="javascript:location.href='advancedSampleSearch.do?dispatch=setup'">
								<input type="hidden" name="dispatch" value="searchSample">
								<input type="hidden" name="page" value="1">
								<input type="submit" value="Search">
							</div>
						</td>
					</tr>
				</table>
				<div align="right"></div>
			</td>
		</tr>
	</table>
</form>
