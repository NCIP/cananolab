<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type="text/javascript" src="javascript/CompositionManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/CompositionManager.js'></script>
<script type="text/javascript"
	src="javascript/FunctionalizingEntityManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/FunctionalizingEntityManager.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>
<c:set var="validate" value="false" />
<c:if test="${!user.curator && theSample.publicStatus}">
	<c:set var="validate" value="true" />
</c:if>
<c:choose>
	<c:when
		test="${!empty compositionForm.map.functionalizingEntity.domainEntity.id}">
		<c:set var="funcTitle"
			value="${sampleName} Sample Composition - Functionalizing Entity - ${compositionForm.map.functionalizingEntity.type}" />
	</c:when>
	<c:otherwise>
		<c:set var="funcTitle"
			value="${sampleName} Sample Composition - Functionalizing Entity" />
	</c:otherwise>
</c:choose>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="${funcTitle}" />
	<jsp:param name="topic" value="function_entity_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="/functionalizingEntity" enctype="multipart/form-data"
	onsubmit="return validateAmountValue() &&
	validateSavingTheData('newFunction', 'function') && validateSavingTheData('newFile', 'file');">
	<jsp:include page="/bodyMessage.jsp?bundle=sample" />
	<table width="100%" align="center" class="submissionView">
		<c:if
			test="${empty compositionForm.map.functionalizingEntity.domainEntity.id}">
			<tr>
				<td class="cellLabel">
					Functionalizing Entity Type*
				</td>
				<td colspan="3">
					<div id="feTypePrompt">
						<html:select styleId="feType"
							property="functionalizingEntity.type"
							onchange="javascript:callPrompt('Functionalizing Entity Type', 'feType', 'feTypePrompt'); setEntityInclude('feType', 'functionalizingEntity');">
							<option value=""></option>
							<html:options name="functionalizingEntityTypes" />
							<option value="other">
								[other]
							</option>
						</html:select>
					</div>
				</td>
			</tr>
		</c:if>
		<tr>
			<td class="cellLabel">
				Chemical Name*
			</td>
			<td colspan="4">
				<html:text property="functionalizingEntity.name" size="70" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				PubChem DataSource
			</td>
			<td>
				<div id="pubChemDataSourcePrompt">
					<html:select styleId="pubChemDataSource"
						property="functionalizingEntity.pubChemDataSourceName"
						onchange="javascript:callPrompt('PubChem DataSource', 'pubChemDataSource', 'pubChemDataSourcePrompt');">
						<option value="" />
							<html:options name="pubChemDataSources" />
					</html:select>
				</div>
			</td>
			<td class="cellLabel">
				PubChem Id
			</td>
			<td>
				<html:text property="functionalizingEntity.pubChemId"
					onkeydown="return filterInteger(event)" size="30"
					styleId="pubChemId" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Amount
			</td>
			<td>
				<html:text property="functionalizingEntity.value"
					styleId="amountValue" />
					<%-- onkeydown="return filterFloatNumber(event)" /--%>
			</td>
			<td class="cellLabel">
				Amount Unit
			</td>
			<td>
				<div id="feUnitPrompt">
					<html:select styleId="feUnit"
						property="functionalizingEntity.valueUnit"
						onchange="javascript:callPrompt('Amount Unit', 'feUnit', 'feUnitPrompt');">
						<option value=""></option>
						<html:options name="functionalizingEntityUnits" />
						<option value="other">
							[other]
						</option>
					</html:select>
				</div>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Molecular Formula Type
			</td>
			<td>
				<div id="mfTypePrompt">
					<html:select styleId="mfType"
						property="functionalizingEntity.molecularFormulaType"
						onchange="javascript:callPrompt('Molecular Formula Type', 'mfType', 'mfTypePrompt'); ">
						<option value=""></option>
						<html:options name="molecularFormulaTypes" />
						<option value="other">
							[other]
						</option>
					</html:select>
				</div>
				&nbsp;
			</td>
			<td class="cellLabel">
				Molecular Formula
			</td>
			<td>
				<html:textarea property="functionalizingEntity.molecularFormula"
					rows="2" cols="80" />
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Activation Method
			</td>
			<td>
				<div id="feaMethodPrompt">
					<html:select styleId="feaMethod"
						property="functionalizingEntity.activationMethod.type"
						onchange="javascript:callPrompt('Activation Method', 'feaMethod', 'feaMethodPrompt');">
						<option value=""></option>
						<html:options name="activationMethods" />
						<option value="other">
							[other]
						</option>
					</html:select>
				</div>
			</td>
			<td class="cellLabel">
				Activation Effect
			</td>
			<td>
				<html:text
					property="functionalizingEntity.activationMethod.activationEffect"
					size="70" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Description
			</td>
			<td colspan="3">
				<html:textarea property="functionalizingEntity.description" rows="2"
					cols="80" />
			</td>
		</tr>
	</table>
	<br>

	<div id="entityInclude">
	<c:if test="${!empty entityDetailPage}">
		<jsp:include page="${entityDetailPage}" />
	</c:if>
	</div>
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td class="cellLabel" width="15%">
				Function
			</td>
			<td>
				<c:set var="newaddFuncButtonStyle" value="display:block" />
				<c:set var="disableOuterButtons" value="false"/>
				<c:if
					test="${openFunction eq 'true'}">
					<c:set var="newaddFuncButtonStyle" value="display:none" />
					<c:set var="disableOuterButtons" value="true"/>
				</c:if>
				<a style="${newaddFuncButtonStyle}" id="addFunction"
					href="javascript:clearFunction();openSubmissionForm('Function');disableOuterButtons();"><img
						align="top" src="images/btn_add.gif" border="0" /> </a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<c:if
					test="${! empty compositionForm.map.functionalizingEntity.functions}">
					<c:set var="edit" value="true" />
					<c:set var="entity"
						value="${compositionForm.map.functionalizingEntity}" />
					<%@ include file="bodyFunctionEdit.jsp"%>
				</c:if>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<c:set var="newFuncStyle" value="display:none" />
				<c:if
					test="${openFunction eq 'true'}">
					<c:set var="newFuncStyle" value="display:block" />
				</c:if>
				<div id="newFunction" style="${newFuncStyle}">
					<%@ include file="bodySubmitFunction.jsp"%>
				</div>
				&nbsp;
			</td>
		</tr>
	</table>
	<br>
	<%--Functionalizing Entity File Information --%>
	<c:set var="fileParent" value="functionalizingEntity" />
	<a name="file">
		<table width="100%" align="center" class="submissionView">
			<tr>
				<td class="cellLabel" width="15%">
					File
				</td>
				<td>
					<c:set var="addFileButtonStyle" value="display:block"/>
					<c:if test="${openFile eq 'true' }">
						<c:set var="addFileButtonStyle" value="display:none"/>
					</c:if>
					<a style="${addFileButtonStyle }" id="addFile"
						href="javascript:clearFile('${fileParent }'); openSubmissionForm('File');disableOuterButtons();"><img
							align="top" src="images/btn_add.gif" border="0" /> </a>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<c:if
						test="${! empty compositionForm.map.functionalizingEntity.files }">
						<c:set var="files"
							value="${compositionForm.map.functionalizingEntity.files}" />
						<c:set var="editFile" value="true" />
						<c:set var="downloadAction" value="composition"/>
						<%@ include file="../../bodyFileEdit.jsp"%>
					</c:if>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<c:set var="newFileStyle" value="display:none"/>
					<c:if test="${openFile eq 'true' }">
						<c:set var="newFileStyle" value="display:block"/>
					</c:if>
					<div style="${newFileStyle}" id="newFile">
						<c:set var="fileForm" value="compositionForm" />
						<c:set var="theFile"
							value="${compositionForm.map.functionalizingEntity.theFile}" />
						<c:set var="actionName" value="functionalizingEntity" />
						<%@include file="../../bodySubmitFile.jsp"%>
						&nbsp;
					</div>
				</td>
			</tr>
		</table> </a>
	<br>
	<jsp:include page="/sample/bodyAnnotationCopy.jsp" />
	<br>
	<html:hidden property="sampleId" value="${param.sampleId}" />
	<c:set var="updateId"
		value="${compositionForm.map.functionalizingEntity.domainEntity.id}" />
	<c:set var="resetOnclick" value="this.form.reset();displayFileRadioButton();"/>
	<c:set var="deleteOnclick" value="deleteData('functionalizing entity', compositionForm, 'functionalizingEntity', 'delete')"/>
	<c:set var="deleteButtonName" value="Delete"/>
	<c:set var="hiddenDispatch" value="create"/>
	<c:set var="hiddenPage" value="2"/>
	<c:set var="showDelete" value="false"/>
	<c:if test="${theSample.userDeletable && !empty updateId}">
	   <c:set var="showDelete" value="true"/>
	</c:if>
	<%@include file="../../../bodySubmitButtons.jsp"%>
</html:form>