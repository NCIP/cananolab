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
<script type="text/javascript" src="javascript/FunctionalizingEntityManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/FunctionalizingEntityManager.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>
<html:form action="/functionalizingEntity" enctype="multipart/form-data">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					${sampleName} Sample Composition - Functionalizing Entity
					<c:if
						test="${!empty compositionForm.map.functionalizingEntity.domainEntity.id}">
						- ${compositionForm.map.functionalizingEntity.type}
						</c:if>
				</h4>
			</td>
			<td align="right" width="15%">
				<jsp:include page="/helpGlossary.jsp">
					<jsp:param name="topic" value="function_entity_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=particle" />
				<table width="100%" align="center" class="submissionView">
					<tr>
						<th colspan="4">
							Summary
						</th>
					</tr>
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
											[Other]
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
							Amount
						</td>
						<td>
							<html:text property="functionalizingEntity.value"
								onkeydown="return filterFloatNumber(event)" />
						</td>
						<td class="cellLabel">
							Amount Unit
						</td>
						<td>
							<html:select styleId="feUnit"
								property="functionalizingEntity.valueUnit"
								onchange="javascript:callPrompt('Amount Unit', 'feUnit');">
								<option value=""></option>
								<html:options name="functionalizingEntityUnits" />
								<option value="other">
									[Other]
								</option>
							</html:select>
						</td>
					</tr>
					<tr>
						<td class="cellLabel">
							Molecular Formula Type
						</td>
						<td>
							<html:select styleId="mfType"
								property="functionalizingEntity.molecularFormulaType"
								onchange="javascript:callPrompt('Molecular Formula Type', 'mfType'); ">
								<option value=""></option>
								<html:options name="feMolecularFormulaTypes" />
								<option value="other">
									[Other]
								</option>
							</html:select>
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
							<html:select styleId="feaMethod"
								property="functionalizingEntity.activationMethod.type"
								onchange="javascript:callPrompt('Activation Method', 'feaMethod');">
								<option value=""></option>
								<html:options name="activationMethods" />
								<option value="other">
									[Other]
								</option>
							</html:select>
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
							<html:textarea property="functionalizingEntity.description"
								rows="2" cols="80" />
						</td>
					</tr>
				</table>
				<br>
				<c:if test="${!empty entityDetailPage}">
					<jsp:include page="${entityDetailPage}" />
				</c:if>
				<div id="entityInclude"></div>
				<table width="100%" align="center" class="submissionView">
					<tbody>
						<tr>
							<th>
								Function Information
							</th>
						</tr>
						<tr>
							<td class="cellLabel">
								Function&nbsp;&nbsp;&nbsp;&nbsp;
								<a style="" id="addFunction"
									href="javascript:clearFunction();show('newFunction');"><img align="top"
										src="images/btn_add.gif" border="0" /> </a>
							</td>
						</tr>
						<tr>
							<td>
								<c:if
									test="${! empty compositionForm.map.functionalizingEntity.functions}">
									<c:set var="edit" value="true" />
									<c:set var="entity"
										value="${compositionForm.map.functionalizingEntity}" />
									<%@ include file="bodyFunctionView.jsp"%>
								</c:if>
							</td>
						</tr>
						<tr>
							<td>
								<div id="newFunction" style="display: none;">
									<%@ include file="bodySubmitFunction.jsp" %>
								</div>
								&nbsp;
							</td>
						</tr>
					</tbody>
				</table>
			</td>
		</tr>
	</table>
	<br>
	<%--Functionalizing Entity File Information --%>
	<c:set var="fileParent" value="functionalizingEntity" />
	<a name="file">
		<table width="100%" align="center" class="submissionView">
			<tbody>
				<tr>
					<th>
						Functionalizing Entity File
					</th>
				</tr>
				<tr>
					<td class="cellLabel">
						File&nbsp;&nbsp;&nbsp;&nbsp;
						<a href="javascript:clearFile('${fileParent }'); show('newFile');"><img
								align="top" src="images/btn_add.gif" border="0" /> </a>
					</td>
				</tr>
				<tr>
					<td>
						<c:if
							test="${! empty compositionForm.map.functionalizingEntity.files }">
							<c:set var="files"
								value="${compositionForm.map.functionalizingEntity.files}" />
							<c:set var="edit" value="true" />
							<c:set var="entityType" value="functionalizing entity" />
							<%@ include file="../bodyFileView.jsp"%>
						</c:if>
					</td>
				</tr>
				<tr>
					<td>
						<c:set var="newFileStyle" value="display:block" />
						<c:if
							test="${param.dispatch eq 'setupNew' || param.dispatch eq 'setupUpdate'||param.dispatch eq 'addFile' || fn:length(compositionForm.map.functionalizingEntity.files)>0}">
							<c:set var="newFileStyle" value="display:none" />
						</c:if>
						<div style="${newFileStyle}" id="newFile">

							<c:set var="fileForm" value="compositionForm" />
							<c:set var="theFile"
								value="${compositionForm.map.functionalizingEntity.theFile}" />
							<%@include file="../../bodySubmitFile.jsp"%>
							&nbsp;
						</div>
					</td>
				</tr>
		</table> </a>
	<br>
	<jsp:include page="/sample/bodyAnnotationCopy.jsp" />

	<br>
	<table width="100%" border="0" align="center" cellpadding="3"
		cellspacing="0" class="topBorderOnly" summary="">
		<tr>
			<td width="30%">
				<span class="formMessage"> </span>
				<br>
				<c:choose>
					<c:when
						test="${param.dispatch eq 'setupUpdate'&& canUserDelete eq 'true'}">
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
									<c:choose>
										<c:when test="${'setup' eq param.dispatch }">
											<c:remove var="dataId" scope="session" />
										</c:when>
										<c:when test="${'setupUpdate' eq param.dispatch }">
											<c:set var="dataId" value="${param.dataId}" scope="session" />
										</c:when>
									</c:choose>
									<c:set var="origUrl"
										value="${actionName}.do?sampleId=${sampleId}&submitType=${submitType}&page=0&dispatch=setup&location=${location}" />
									<c:if test="${!empty dataId}">
										<c:set var="origUrl"
											value="${actionName}.do?sampleId=${sampleId}&submitType=${submitType}&page=0&dispatch=setupUpdate&location=${location}&dataId=${dataId}" />
									</c:if>
									<input type="reset" value="Reset"
										onclick="javascript:window.location.href='${origUrl}'">
									<input type="hidden" name="dispatch" value="create">
									<input type="hidden" name="page" value="2">
									<input type="hidden" name="submitType"
										value="${param.submitType}" />
									<html:submit />
									<c:choose>
										<c:when test="${!empty param.sampleId }">
											<html:hidden property="sampleId" value="${param.sampleId }" />
										</c:when>
										<c:otherwise>
											<html:hidden property="sampleId" />
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</td>
					</tr>
				</table>
				<div align="right"></div>
			</td>
		</tr>
	</table>
</html:form>