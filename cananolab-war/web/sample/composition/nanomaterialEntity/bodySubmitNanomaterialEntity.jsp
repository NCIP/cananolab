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
	src="javascript/NanomaterialEntityManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/NanomaterialEntityManager.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<c:choose>
	<c:when test="${!empty compositionForm.map.nanomaterialEntity.domainEntity.id}">
		<c:set var="nanoTitle"
		value="${sampleName} Sample Composition - Nanomaterial Entity - ${compositionForm.map.nanomaterialEntity.type}"/>
	</c:when>
	<c:otherwise>
		<c:set var="nanoTitle"
		value="${sampleName} Sample Composition - Nanomaterial Entity"/>
	</c:otherwise>
</c:choose>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="${nanoTitle}" />
	<jsp:param name="topic" value="nanomaterial_entity_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="/nanomaterialEntity" enctype="multipart/form-data"
	onsubmit="return validateTubeInfo() && validateFullereneInfo() && validatePolymerInfo() &&
	validateSavingTheData('newComposingElement', 'Composing Element') && validateSavingTheData('newFile', 'file');">
	<jsp:include page="/bodyMessage.jsp?bundle=sample" />
	<table width="100%" align="center" class="submissionView">
		<c:if
			test="${empty compositionForm.map.nanomaterialEntity.domainEntity.id}">
			<tr>
				<td class="cellLabel">
					Nanomaterial Entity Type*
				</td>
				<td>
					<div id="peTypePrompt">
						<html:select styleId="peType" property="nanomaterialEntity.type"
							onchange="javascript:callPrompt('Nanomaterial Entity Type', 'peType', 'peTypePrompt');
										setEntityInclude('peType', 'nanomaterialEntity');">
							<option value=""></option>
							<html:options name="nanomaterialEntityTypes" />
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
				Description
			</td>
			<td>
				<html:textarea property="nanomaterialEntity.description" rows="3"
					cols="100" />
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
				Composing Element
			</td>
			<td>
				<c:set var="newAddCEButtonStyle" value="display:block" />
				<c:if test="${openComposingElement eq 'true'}">
					<c:set var="newAddCEButtonStyle" value="display:none" />
				</c:if>
				<a style="${newAddCEButtonStyle}" id="addComposingElement"
					href="#submitComposingElement"
					onclick="javascript:clearComposingElement(); openSubmissionForm('ComposingElement');"><img
						align="top" src="images/btn_add.gif" border="0" /> </a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<c:if
					test="${! empty compositionForm.map.nanomaterialEntity.composingElements}">
					<c:set var="edit" value="true" />
					<c:set var="entity"
						value="${compositionForm.map.nanomaterialEntity}" />
					<%@ include file="bodyComposingElementEdit.jsp"%>
				</c:if>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<c:set var="newCEStyle" value="display:none" />
				<c:if test="${openComposingElement eq 'true'}">
					<c:set var="newCEStyle" value="display:block" />
				</c:if>
				<div style="${newCEStyle }" id="newComposingElement">
					<c:set var="theComposingElement"
						value="${compositionForm.map.nanomaterialEntity.theComposingElement}" />
					<c:set var="actionName" value="nanomaterialEntity" />
					<a name="submitComposingElement"><%@ include
							file="bodySubmitComposingElement.jsp"%></a>
				</div>
			</td>
		</tr>
	</table>
	<br>
	<%--Nanomaterial Entity File Information --%>
	<c:set var="fileParent" value="nanomaterialEntity" />
	<a name="file">
		<table width="100%" align="center" class="submissionView">
			<tr>
				<td class="cellLabel" width="15%">
					File
				</td>
				<td>
					<c:set var="addFileButtonStyle" value="display:block" />
					<c:if test="${openFile eq 'true'}">
						<c:set var="addFileButtonStyle" value="display:none" />
					</c:if>
					<a style="${addFileButtonStyle}" id="addFile"
						href="javascript:clearFile('${fileParent }'); openSubmissionForm('File');"><img
							align="top" src="images/btn_add.gif" border="0" /> </a>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<c:if
						test="${! empty compositionForm.map.nanomaterialEntity.files }">
						<c:set var="files"
							value="${compositionForm.map.nanomaterialEntity.files}" />
						<c:set var="editFile" value="true" />
						<%@ include file="../../bodyFileEdit.jsp"%>
					</c:if>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<c:set var="newFileStyle" value="display:none" />
					<c:if test="${openFile eq 'true'}">
						<c:set var="newFileStyle" value="display:block" />
					</c:if>
					<div style="${newFileStyle}" id="newFile">
						<c:set var="fileForm" value="compositionForm" />
						<c:set var="theFile"
							value="${compositionForm.map.nanomaterialEntity.theFile}" />
						<c:set var="actionName" value="nanomaterialEntity" />
						<%@include file="../../bodySubmitFile.jsp"%>
					</div>
				</td>
			</tr>
		</table> </a>
	<br>
	<jsp:include page="/sample/bodyAnnotationCopy.jsp" />
	<br>
	<html:hidden property="sampleId" value="${param.sampleId}" />
	<c:set var="updateId"
		value="${compositionForm.map.nanomaterialEntity.domainEntity.id}" />
	<c:set var="resetOnclick" value="this.form.reset();displayFileRadioButton();"/>
	<c:set var="deleteOnclick" value="deleteData('nanomaterial entity', compositionForm, 'nanomaterialEntity', 'delete')"/>
	<c:set var="deleteButtonName" value="Delete"/>
	<c:set var="hiddenDispatch" value="create"/>
	<c:set var="hiddenPage" value="2"/>
	<%@include file="../../../bodySubmitButtons.jsp"%>
</html:form>
