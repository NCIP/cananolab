<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
					<c:if test="${param.dispatch eq 'setupUpdate'}">
						- ${nanomaterialEntityForm.map.entity.type}
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
					<c:if test="${param.dispatch eq 'setupNew'}">
						<tr>
							<td class="cellLabel">
								Nanomaterial Entity Type*
							</td>
							<td>
								<html:select styleId="peType" property="entity.type"
									onchange="javascript:callPrompt('Particle Entity Type', 'peType');
										setEntityInclude('peType', '/sample/composition/nanomaterialEntity'); getNETypeOptions();">
									<option value=""></option>
									<html:options name="nanomaterialEntityTypes" />
									<option value="other">
										[Other]
									</option>
								</html:select>
							</td>
						</tr>
					</c:if>
					<tr>
						<td class="cellLabel">
							Description
						</td>
						<td>
							<html:textarea property="entity.description" rows="3" cols="100" />
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
							<th colspan="2">
								Composing Elements
							</th>
						</tr>
						<tr>
							<td class="cellLabel" colspan="2">
								Composing Element&nbsp;&nbsp;&nbsp;&nbsp;
								<a style="" id="addTechniqueInstrument"
									href="javascript:resetTheExperimentConfig(true);"><img
										align="top" src="images/btn_add.gif" border="0" /> </a>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<c:if
									test="${! empty nanomaterialEntityForm.map.entity.composingElements}">
									<c:set var="edit" value="true" />
									<c:set var="entity" value="${nanomaterialEntityForm.map.entity}"/>
									<%@ include file="bodyComposingElementView.jsp"%>
								</c:if>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="newComposingElement" style="display: none;">
									<%--<jsp:include page="bodySubmitComposingElement.jsp" />--%>
								</div>
								&nbsp;
							</td>
						</tr>
				</table>
				<br>
				<%--Particle Entity File Information --%>
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify" id="peFileTitle">
									Particle Entity File Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<table border="0" width="100%">
									<tr>
										<td valign="bottom" width="10%">
											<a href="#"
												onclick="javascript:addComponent(nanomaterialEntityForm, 'nanomaterialEntity', 'addFile'); return false;">
												<span class="addLink">Add File</span> </a>
										</td>
										<td id="fileTd">
											<logic:iterate name="nanomaterialEntityForm"
												property="entity.files" id="entityFile" indexId="fileInd">
												<jsp:include page="/sample/bodyLoadFileUpdate.jsp">
													<jsp:param name="fileInd" value="${fileInd}" />
													<jsp:param name="form" value="nanomaterialEntityForm" />
													<jsp:param name="action" value="nanomaterialEntity" />
													<jsp:param name="removeCmd" value="\'removeFile\'" />
													<jsp:param name="fileBean" value="entity.files[${fileInd}]" />
													<jsp:param name="fileId"
														value="${nanomaterialEntityForm.map.entity.files[fileInd].domainFile.id}" />
													<jsp:param name="fileUri"
														value="${nanomaterialEntityForm.map.entity.files[fileInd].domainFile.uri}" />
													<jsp:param name="fileTitle"
														value="${nanomaterialEntityForm.map.entity.files[fileInd].domainFile.title}" />
													<jsp:param name="fileHidden"
														value="${nanomaterialEntityForm.map.entity.files[fileInd].hidden}" />
													<jsp:param name="fileImage"
														value="${nanomaterialEntityForm.map.entity.files[fileInd].image}" />
													<jsp:param name="fileUriExternal"
														value="${nanomaterialEntityForm.map.entity.files[fileInd].domainFile.uriExternal}" />
												</jsp:include>
												<br>
											</logic:iterate>
										</td>
									</tr>
								</table>
							</td>
						</tr>
				</table>
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
