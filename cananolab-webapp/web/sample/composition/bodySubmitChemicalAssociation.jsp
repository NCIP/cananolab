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
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<c:choose>
	<c:when test="${compositionForm.map.assoc.type eq 'attachment'}">
		<c:set var="style" value="display:block" />
	</c:when>
	<c:otherwise>
		<c:set var="style" value="display:none" />
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${! empty ceListA }">
		<c:set var="ceStyleA" value="display:block" />
	</c:when>
	<c:otherwise>
		<c:set var="ceStyleA" value="display:none" />
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${! empty ceListB }">
		<c:set var="ceStyleB" value="display:block" />
	</c:when>
	<c:otherwise>
		<c:set var="ceStyleB" value="display:none" />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${!empty compositionForm.map.assoc.domainAssociation.id}">
		<c:set var="chemTitle"
		value="${sampleName} Sample Composition - Chemical Association - ${compositionForm.map.assoc.type}" />
	</c:when>
	<c:otherwise>
		<c:set var="chemTitle"
		value="${sampleName} Sample Composition - Chemical Association" />
	</c:otherwise>
</c:choose>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="${chemTitle}" />
	<jsp:param name="topic" value="chem_association_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="/chemicalAssociation" enctype="multipart/form-data" onsubmit="return validateSavingTheData('newFile', 'file');">
	<jsp:include page="/bodyMessage.jsp?bundle=sample" />
	<table width="100%" align="center" class="submissionView">
		<c:if test="${empty compositionForm.map.assoc.domainAssociation.id}">
			<tr>
				<td class="cellLabel">
					Association Type*
				</td>
				<td>
					<div id="assocTypePrompt">
						<html:select styleId="assoType" property="assoc.type"
							onchange="javascript:callPrompt('Association Type', 'assoType', 'assocTypePrompt');
											displayBondType();">
							<option value=""></option>
							<html:options name="chemicalAssociationTypes" />
							<option value="other">
								[other]
							</option>
						</html:select>
					</div>
				</td>
			</tr>
		</c:if>
		<tr>
			<c:set var="bondTypeStyle" value="display:none" />
			<c:if test="${compositionForm.map.assoc.type eq 'attachment' }">
				<c:set var="bondTypeStyle" value="display:block" />
			</c:if>
			<td class="cellLabel">
				<span id="bondTypeLabel" style="${bondTypeStyle}">Bond Type*</span>
			</td>
			<td>
				<div id="bondTypePrompt" style="${bondTypeStyle}">
					<html:select styleId="bondType"
						property="assoc.attachment.bondType"
						onchange="javascript:callPrompt('Bond Type', 'bondType', 'bondTypePrompt');">
						<option value=""></option>
						<html:options name="bondTypes" />
						<option value="other">
							[other]
						</option>
					</html:select>
				</div>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Description
			</td>
			<td>
				<html:textarea property="assoc.description" rows="3" cols="60" />
			</td>
		</tr>
	</table>
	<br>
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td>
				<c:set var="elementNumber" value="A" />
				<c:set var="entitySelectStyle" value="display:none" />
				<c:set var="composingElementSelectStyle" value="display:none" />
				<c:if test="${!empty entityListA }">
					<c:set var="entityList" value="${entityListA}" />
				</c:if>
				<c:if test="${!empty ceListA }">
					<c:set var="ceList" value="${ceListA}" />
				</c:if>
				<c:if
					test="${! empty compositionForm.map.assoc.associatedElementA.entityId}">
					<c:set var="entitySelectStyle" value="display:block" />
				</c:if>
				<c:if
					test="${compositionForm.map.assoc.associatedElementA.compositionType eq 'nanomaterial entity'}">
					<c:set var="composingElementSelectStyle" value="display:block" />
				</c:if>
				<%@include file="bodySubmitAssociatedElement.jsp"%>
			</td>
			<td>
				<img src="images/arrow_left_right_gray.gif" id="assocImg" />
				<br>
				associated with
			</td>
			<td>
				<c:set var="elementNumber" value="B" />
				<c:set var="entitySelectStyle" value="display:none" />
				<c:set var="composingElementSelectStyle" value="display:none" />
				<c:if test="${!empty entityListB }">
					<c:set var="entityList" value="${entityListB}" />
				</c:if>
				<c:if test="${!empty ceListB }">
					<c:set var="ceList" value="${ceListB}" />
				</c:if>
				<c:if
					test="${! empty compositionForm.map.assoc.associatedElementA.entityId}">
					<c:set var="entitySelectStyle" value="display:block" />
				</c:if>
				<c:if
					test="${compositionForm.map.assoc.associatedElementB.compositionType eq 'nanomaterial entity'}">
					<c:set var="composingElementSelectStyle" value="display:block" />
				</c:if>
				<%@include file="bodySubmitAssociatedElement.jsp"%>
			</td>
		</tr>
	</table>
	<br>
	<%--Chemical Association File Information --%>
	<c:set var="fileParent" value="assoc" />
	<a name="file">
		<table width="100%" align="center" class="submissionView">
			<tr>
				<td class="cellLabel" width="10%">
					File
				</td>
				<td>
					<a style="display:block" id="addFile"
						href="javascript:confirmAddNew('File', 'File', 'clearFile(\'${fileParent}\')'); disableOuterButtons();">
						<img align="top" src="images/btn_add.gif" border="0" />
					</a>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<c:if test="${! empty compositionForm.map.assoc.files }">
						<c:set var="files" value="${compositionForm.map.assoc.files}" />
						<c:set var="editFile" value="true" />
						<c:set var="downloadAction" value="composition"/>
						<%@ include file="../bodyFileEdit.jsp"%>
					</c:if>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<c:set var="newFileStyle" value="display:none"/>
					<c:if test="${openFile eq 'true'}">
					    <c:set var="newFileStyle" value="display:block"/>
					</c:if>
					<div style="${newFileStyle}" id="newFile">
						<c:set var="fileForm" value="compositionForm" />
						<c:set var="theFile" value="${compositionForm.map.assoc.theFile}" />
						<%@include file="../bodySubmitFile.jsp"%>
						&nbsp;
					</div>
				</td>
			</tr>
		</table> </a>
	<br>
    <html:hidden property="sampleId" value="${param.sampleId}" />
	<c:set var="updateId" value="${compositionForm.map.assoc.domainAssociation.id}"/>
	<c:set var="resetOnclick" value="this.form.reset();displayFileRadioButton();"/>
	<c:set var="deleteOnclick" value="deleteData('chemical association', compositionForm, 'chemicalAssociation', 'delete')"/>
	<c:set var="deleteButtonName" value="Delete"/>
	<c:set var="hiddenDispatch" value="create"/>
	<c:set var="hiddenPage" value="2"/>
	<c:set var="showDelete" value="false"/>
	<c:if test="${theSample.userDeletable}">
	   <c:set var="showDelete" value="true"/>
	</c:if>
	<%@include file="../../bodySubmitButtons.jsp"%>
</html:form>
