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
	answer = confirm("Are you sure you want to delete the nanomaterial entity?")
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
<html:form action="/nanomaterialEntity" enctype="multipart/form-data">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					${sampleName} Sample Composition - Nanomaterial Entity
					<c:if
						test="${!empty compositionForm.map.nanomaterialEntity.domainEntity.id}">
						- ${compositionForm.map.nanomaterialEntity.type}
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
						<th colspan="2">
							Summary
						</th>
					</tr>
					<c:if
						test="${empty compositionForm.map.nanomaterialEntity.domainEntity.id}">
						<tr>
							<td class="cellLabel">
								Nanomaterial Entity Type*
							</td>
							<td>
								<div id="peTypePrompt">
									<html:select styleId="peType"
										property="nanomaterialEntity.type"
										onchange="javascript:callPrompt('Nanomaterial Entity Type', 'peType', 'peTypePrompt');
										setEntityInclude('peType', 'nanomaterialEntity');">
										<option value=""></option>
										<html:options name="nanomaterialEntityTypes" />
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
						<td>
							<html:textarea property="nanomaterialEntity.description" rows="3"
								cols="100" />
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
								Composing Elements
							</th>
						</tr>
						<tr>
							<td class="cellLabel">
								Composing Element&nbsp;&nbsp;&nbsp;&nbsp;
								<a style="" id="addTechniqueInstrument"
									href="javascript:resetTheExperimentConfig(true);"><img
										align="top" src="images/btn_add.gif" border="0" /> </a>
							</td>
						</tr>
						<tr>
							<td>
								<c:if
									test="${! empty compositionForm.map.nanomaterialEntity.composingElements}">
									<c:set var="edit" value="true" />
									<c:set var="entity"
										value="${compositionForm.map.nanomaterialEntity}" />
									<%@ include file="bodyComposingElementView.jsp"%>
								</c:if>
							</td>
						</tr>
						<tr>
							<td>
								<div id="newComposingElement" style="display: none;">
									<%--<jsp:include page="bodySubmitComposingElement.jsp" />--%>
								</div>
								&nbsp;
							</td>
						</tr>
				</table>
				<br>
				<%--Nanomaterial Entity File Information --%>
				<a name="file">
					<table width="100%" align="center" class="submissionView">
						<tbody>
							<tr>
								<th>
									Nanomaterial Entity File
								</th>
							</tr>
							<tr>
								<td class="cellLabel">
									File&nbsp;&nbsp;&nbsp;&nbsp;
									<a href="javascript:clearFile(); show('newFile');"><img
											align="top" src="images/btn_add.gif" border="0" /> </a>
								</td>
							</tr>
							<tr>
								<td>
									<c:if
										test="${! empty compositionForm.map.nanomaterialEntity.files }">
										<c:set var="files"
											value="${compositionForm.map.nanomaterialEntity.files}" />
										<c:set var="edit" value="true" />
										<c:set var="entityType" value="nanomaterial entity" />
										<%@ include file="../bodyFileView.jsp"%>
									</c:if>
								</td>
							</tr>
							<tr>
								<td>
									<c:set var="newFileStyle" value="display:block" />
									<c:if
										test="${param.dispatch eq 'addFile' || fn:length(compositionForm.map.nanomaterialEntity.files)>0}">
										<c:set var="newFileStyle" value="display:none" />
									</c:if>
									<div style="${newFileStyle}" id="newFile">
										<c:set var="fileParent" value="nanomaterialEntity" />
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
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" class="topBorderOnly" summary="">
					<tr>
						<td width="30%">
							<span class="formMessage"> </span>
							<br>
							<c:choose>
								<c:when
									test="${param.dispatch eq 'setupUpdate'&& canDelete eq 'true'}">
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
														<c:set var="dataId" value="${param.dataId}"
															scope="session" />
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
												<c:choose>
													<c:when test="${!empty param.sampleId }">
														<html:hidden property="sampleId"
															value="${param.sampleId }" />
													</c:when>
													<c:otherwise>
														<html:hidden property="sampleId" />
													</c:otherwise>
												</c:choose>
												<input type="hidden" name="submitType"
													value="${param.submitType}" />
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
