<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/SampleManager.js"></script>
<script type="text/javascript"
	src="/caNanoLab/dwr/interface/SampleManager.js"></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>
<script type="text/javascript"
	src="javascript/CharacterizationManager.js"></script>
<script type="text/javascript"
	src="/caNanoLab/dwr/interface/CharacterizationManager.js"></script>
<script type="text/javascript" src="javascript/FindingManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/FindingManager.js'></script>

<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle"
		value="${applicationOwner} Advanced Sample Search" />
	<jsp:param name="topic" value="search_sample_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
	<jsp:param name="other" value="Basic Search" />
	<jsp:param name="otherLink"
		value="searchSample.do?dispatch=setup" />
</jsp:include>
<html:form action="/advancedSampleSearch" enctype="multipart/form-data"
	onsubmit="return validateDatumValue()">
	<jsp:include page="/bodyMessage.jsp?bundle=sample" />
	<table width="100%" align="center" class="submissionView">
		<tr>
			<th>
				Sample Criteria
			</th>
		</tr>
		<tr>
			<td>
				<table id="sampleQueryTable" class="editTableWithGrid" width="85%"
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
							<html:hidden property="searchBean.theSampleQuery.id"
								styleId="sampleQueryId" />
							<html:select property="searchBean.theSampleQuery.nameType"
								styleId="nameType">
								<option value="">
									-- Please Select --
								</option>
								<option value="sample name">
									sample name
								</option>
								<option value="point of contact name">
									point of contact name
								</option>
							</html:select>
						</td>
						<td>
							<html:select property="searchBean.theSampleQuery.operand"
								styleId="sampleOperand">
								<option value="">
									-- Please Select --
								</option>
								<html:options collection="stringOperands" property="value"
									labelProperty="label" />
							</html:select>
						</td>
						<td>
							<html:text property="searchBean.theSampleQuery.name"
								styleId="name" size="50" />
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
					<html:radio property="searchBean.sampleLogicalOperator" value="and" />
					AND
					<html:radio property="searchBean.sampleLogicalOperator" value="or" />
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
				<table id="compositionQueryTable" class="editTableWithGrid"
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
							<html:hidden property="searchBean.theCompositionQuery.id"
								styleId="compQueryId" />
							<html:select
								property="searchBean.theCompositionQuery.compositionType"
								styleId="compType"
								onchange="javascript:setCompositionEntityOptions();">
								<option value="">
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
								</option>
							</html:select>
						</td>
						<td>
							<html:select property="searchBean.theCompositionQuery.entityType"
								styleId="entityType">
								<option value="">
									-- Please Select --
								</option>
							</html:select>
						</td>
						<td class="cellLabel">
							<span id="compChemicalNameLabel">with chemical name</span>
						</td>
						<td>
							<html:select property="searchBean.theCompositionQuery.operand"
								styleId="compOperand">
								<option value="">
									-- Please Select --
								</option>
								<html:options collection="stringOperands" property="value"
									labelProperty="label" />
							</html:select>
						</td>
						<td>
							<html:text property="searchBean.theCompositionQuery.chemicalName"
								styleId="chemicalName" size="50" />
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
					<html:radio property="searchBean.compositionLogicalOperator"
						value="and" />
					AND
					<html:radio property="searchBean.compositionLogicalOperator"
						value="or" />
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
				<table id="characterizationQueryTable" class="editTableWithGrid"
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
							<html:hidden property="searchBean.theCharacterizationQuery.id"
								styleId="charQueryId" />
							<html:select
								property="searchBean.theCharacterizationQuery.characterizationType"
								styleId="charType"
								onchange="javascript:setCharacterizationOptions();">
								<option value="">
									-- Please Select --
								</option>
								<html:options collection="decoratedCharacterizationTypes"
									labelProperty="label" property="value" />
							</html:select>
						</td>
						<td>
							<html:select
								property="searchBean.theCharacterizationQuery.characterizationName"
								styleId="charName"
								onchange="javascript:setDatumNameOptionsByCharName();">
								<option value="">
									-- Please Select --
								</option>
							</html:select>
						</td>
						<td>
							<html:select
								property="searchBean.theCharacterizationQuery.datumName"
								styleId="datumName"
								onchange="javascript:setDatumValueUnitOptions();setCharacterizationOperandOptions();setDatumValueOptions()">
								<option value="">
									-- Please Select --
								</option>
							</html:select>
						</td>
						<td>
							<html:select
								property="searchBean.theCharacterizationQuery.operand"
								styleId="charOperand">
								<option value="">
									-- Please Select --
								</option>
								<html:options collection="numberOperands" labelProperty="label"
									property="value" />
							</html:select>
						</td>
						<td>
							<div id="datumValueTextBlock" style="display:none">
								<html:text
									property="searchBean.theCharacterizationQuery.datumValue"
									styleId="datumValue" size="10" />
								<%--onkeydown="return filterFloatNumber(event)" /--%>
							</div>
							<div id="datumValueSelectBlock" style="display:none">
								<html:select
									property="searchBean.theCharacterizationQuery.datumValue"
									styleId="datumValueSelect">
									<option value="">
										-- Please Select --
									</option>
									<html:options collection="booleanOptions" property="value"
										labelProperty="label" />
								</html:select>
							</div>
						</td>
						<td>
							<div id="datumValueUnitBlock" style="display: none">
								<html:select
									property="searchBean.theCharacterizationQuery.datumValueUnit"
									styleId="datumValueUnit">
									<option value="">
									</option>
								</html:select>
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
					<html:radio property="searchBean.characterizationLogicalOperator"
						value="and" />
					AND
					<html:radio property="searchBean.characterizationLogicalOperator"
						value="or" />
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
					<html:radio property="searchBean.logicalOperator" value="and" />
					AND
					<html:radio property="searchBean.logicalOperator" value="or" />
					OR
				</div>
			</td>
		</tr>
	</table>
	<br />
	<c:set var="dataType" value="sample" />
	<c:set var="resetOnclick"
		value="javascript: location.href = 'advancedSampleSearch.do?dispatch=setup&page=0'" />
	<c:set var="hiddenDispatch" value="search" />
	<c:set var="hiddenPage" value="1" />
	<%@include file="../bodySearchButtons.jsp"%>
</html:form>
