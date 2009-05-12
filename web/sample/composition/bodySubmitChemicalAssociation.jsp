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

<script type="text/javascript">
<!--//
function confirmDeletion()
{
	answer = confirm("Are you sure you want to delete the chemical association?")
	if (answer !=0)
	{
		this.document.forms[0].dispatch.value="delete";
		this.document.forms[0].page.value="1";
		this.document.forms[0].submit();
		return true;
	}
}
//-->
</script>
<c:choose>
	<c:when
		test="${chemicalAssociationForm.map.assoc.type eq 'attachment'}">
		<c:set var="style" value="display:inline" />
	</c:when>
	<c:otherwise>
		<c:set var="style" value="display:none" />
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${! empty ceListA }">
		<c:set var="ceStyleA" value="display:inline" />
	</c:when>
	<c:otherwise>
		<c:set var="ceStyleA" value="display:none" />

	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${! empty ceListB }">
		<c:set var="ceStyleB" value="display:inline" />
	</c:when>
	<c:otherwise>
		<c:set var="ceStyleB" value="display:none" />
	</c:otherwise>
</c:choose>
<html:form action="/chemicalAssociation" enctype="multipart/form-data">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					${sampleName} Sample Composition - Chemical Association
					<c:if
						test="${!empty compositionForm.map.assoc.domainAssociation.id}">
						- ${compositionForm.map.assoc.domainAssociation.type}
				</c:if>
				</h4>
			</td>
			<td align="right" width="20%">
				<jsp:include page="/helpGlossary.jsp">
					<jsp:param name="topic" value="nano_entity_help" />
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
						test="${empty compositionForm.map.assoc.domainAssociation.id}">
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
											[Other]
										</option>
									</html:select>
								</div>
							</td>
							<td class="cellLabel">
								<span id="bondTypeLabel" style="display: none">Bond Type*</span>
							</td>
							<td>
								<div id="bondTypePrompt" style="display: none;">
									<html:select styleId="bondType"
										property="assoc.attachment.bondType"
										onchange="javascript:callPrompt('Bond Type', 'bondType', 'bondTypePrompt');">
										<option value=""></option>
										<html:options name="bondTypes" />
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
							Description
						</td>
						<td colspan="3">
							<html:textarea property="assoc.description" rows="3" cols="60" />
						</td>
					</tr>
				</table>
				<br>
				<table width="100%" align="center" class="submissionView">
					<tr>
						<th colspan="4">
							Associated Elements Information
						</th>
					</tr>
					<tr>
						<td>
							<table>
								<tr>
									<td class="cellLabel" width="40%">
										Element*
									</td>
								</tr>
								<tr>
									<td>
										<html:select styleId="compositionTypeA"
											property="assoc.associatedElementA.compositionType"
											onchange="getEntityDisplayNameOptions('A');">
											<option value=""></option>
											<html:options name="associationCompositionTypes" />
										</html:select>
										<div id="materialEntitySelectA" style="display: none">
											<br>
											<html:select styleId="compositionTypeA"
												property="assoc.associatedElementA.entityDisplayName"
												onchange="">
												<option value=""></option>
												<html:options collection="sampleMaterialEntities"
													property="type" labelProperty="type" />
											</html:select>
										</div>
										<div id="functionalizingEntitySelectA" style="display: none">
											<br>
											<html:select styleId="compositionTypeA"
												property="assoc.associatedElementA.entityDisplayName"
												onchange="">
												<option value=""></option>
												<html:options collection="sampleFunctionalizingEntities"
													property="displayName" labelProperty="displayName" />
											</html:select>
										</div>
									</td>
								</tr>
							</table>
						</td>
						<td width="20%">
							<img src="images/arrow_left_right_gray.gif" id="assocImg" />
							<br>
							associated with
						</td>
						<td width="40%">
							<table>
								<tr>
									<td class="cellLabel">
										Element*
									</td>
								</tr>
								<tr>
									<td>
										<html:select styleId="compositionTypeB"
											property="assoc.associatedElementB.compositionType"
											onchange="getEntityDisplayNameOptions('B');">
											<option value=""></option>
											<html:options name="associationCompositionTypes" />
										</html:select>
										<div id="materialEntitySelectB" style="display: none">
											<br>
											<html:select styleId="compositionTypeB"
												property="assoc.associatedElementB.entityDisplayName"
												onchange="">
												<option value=""></option>
												<html:options collection="sampleMaterialEntities"
													property="type" labelProperty="type" />
											</html:select>
										</div>
										<div id="functionalizingEntitySelectB" style="display: none">
											<br>
											<html:select styleId="compositionTypeB"
												property="assoc.associatedElementB.entityDisplayName"
												onchange="">
												<option value=""></option>
												<html:options collection="sampleFunctionalizingEntities"
													property="displayName" labelProperty="displayName" />
											</html:select>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<br>
				<%--Chemical Association File Information --%>
				<a name="file">
					<table width="100%" align="center" class="submissionView">
						<tbody>
							<tr>
								<th colspan="2">
									Chemical Association File
								</th>
							</tr>
							<tr>
								<td class="cellLabel" colspan="2">
									File&nbsp;&nbsp;&nbsp;&nbsp;
									<a href="javascript:clearFile(); show('newFile');"><img
											align="top" src="images/btn_add.gif" border="0" /> </a>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<c:set var="newFileStyle" value="display:block" />
									<c:if
										test="${param.dispatch eq 'addFile' || fn:length(compositionForm.map.assoc.files)>0}">
										<c:set var="newFileStyle" value="display:none" />
									</c:if>
									<div style="" id="newFile">
										<c:set var="fileParent" value="assoc" />
										<c:set var="fileForm" value="compositionForm" />
										<c:set var="theFile"
											value="${compositionForm.map.assoc.theFile}" />
										<%@include file="../bodySubmitFile.jsp"%>
										&nbsp;
									</div>
								</td>
							</tr>
					</table> </a>
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
									<c:set var="origUrl"
										value="chemicalAssociation.do?sampleId=${sampleId}&page=0&dispatch=setup&location=${location}" />
									<c:if test="${!empty dataId}">
										<c:set var="origUrl"
											value="chemicalAssociation.do?sampleId=${sampleId}&page=0&dispatch=setupUpdate&location=${location}&dataId=${dataId}" />
									</c:if>
									<input type="reset" value="Reset"
										onclick="javascript:window.location.href='${origUrl}'">
									<input type="hidden" name="dispatch" value="create">
									<input type="hidden" name="page" value="2">
									<c:choose>
										<c:when test="${!empty param.sampleId }">
											<html:hidden property="sampleId" value="${param.sampleId }" />
										</c:when>
										<c:otherwise>
											<html:hidden property="sampleId" />
										</c:otherwise>
									</c:choose>
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
</html:form>
