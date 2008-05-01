<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html:form action="/chemicalAssociation">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					${particleName} Sample Composition - Chemical Association
				</h4>
			</td>
			<td align="right" width="15%">
				<a
					href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=composition_help')"
					class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<h5 align="center">
					<%--					Entity#1:Dendrimer--%>
				</h5>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=particle" />
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" class="topBorderOnly" summary="" id="summary">
					<tr>
					<tr class="topBorder">
						<td class="formTitle" colspan="4">
							<div align="justify">
								Chemical Association Information
							</div>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Association Type</strong>
						</td>
						<td class="label">
							${chemicalAssociationForm.map.assoc.type}&nbsp;
						</td>
						<td class="label" valign="top">
							&nbsp;
							<Strong style="display:none">Bond Type</Strong>
						</td>
						<td class="rightLabel">
							&nbsp;
							${chemicalAssociationForm.map.assoc.attachment.bondType}
						</td>
					</tr>
					<tr>
						<td class="leftLabel" width="%33">
							<table width="100%" border="0" align="center" cellpadding="3"
								cellspacing="0" >
								<tr>
									<td class="completeLabelNoBottom">
										<strong>Element</strong>
									</td>
								</tr>
								<tr>
									<td class="completeLabelNoTopBottom">
										${chemicalAssociationForm.map.assoc.associatedElementA.compositionType}&nbsp;
									</td>
								</tr>
								<tr>
									<td class="completeLabelNoTopBottom">
										&nbsp;
										${chemicalAssociationForm.map.assoc.associatedElementA.entityId}&nbsp;
									</td>
								</tr>
								<tr>
									<td class=completeLabelNoTop>
										&nbsp;
										${chemicalAssociationForm.map.assoc.associatedElementA.composingElement.type}
									</td>
								</tr>
							</table>
							&nbsp;
						</td>
						<td class="label" colspan="2" align="center">
							<strong style="padding-left:20px">Associated With</strong>
						</td>
						<td class="rightLabel">
							<table width="100%" border="1" align="center" cellpadding="3"
								cellspacing="0" >
								<tr>
									<td class="completeLabelNoBottom">
										<strong>Element</strong>
									</td>
								</tr>
								<tr>
									<td class="completeLabelNoTopBottom">
										${chemicalAssociationForm.map.assoc.associatedElementB.compositionType}&nbsp;
									</td>
								</tr>
								<tr>
									<td class="completeLabelNoTopBottom">
										&nbsp;
										${chemicalAssociationForm.map.assoc.associatedElementB.entityId}
									</td>
								</tr>
								<tr>
									<td class="completeLabelNoTop">
										&nbsp;
										${chemicalAssociationForm.map.assoc.associatedElementB.composingElement.type}
									</td>
								</tr>
							</table>
							&nbsp;
						</td>
					</tr>

					<tr>
						<td class="leftLabel" valign="top">
							<strong>Association Description</strong>
						</td>
						<td class="rightLabel" colspan="3">
							${chemicalAssociationForm.map.assoc.description}&nbsp;
						</td>
					</tr>
				</table>
				<%-- File Information --%>
				<br>
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify" id="peFileTitle">
									Chemical Association File Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<table border="0" width="100%">
									<tr>
										<td id="fileTd">

											<logic:iterate name="chemicalAssociationForm"
												property="assoc.files" id="assocFile" indexId="fileInd">
												<jsp:include page="/particle/bodyLoadFileReadOnly.jsp">
													<jsp:param name="fileInd" value="${fileInd}" />
													<jsp:param name="action" value="chemicalAssociation" />
													<jsp:param name="domainFile"
														value="assoc.files[${fileInd}].domainFile" />
													<jsp:param name="fileId"
														value="${chemicalAssociationForm.map.assoc.files[fileInd].domainFile.id}" />
													<jsp:param name="fileUri"
														value="${chemicalAssociationForm.map.assoc.files[fileInd].domainFile.uri}" />
													<jsp:param name="fileDisplayName"
														value="${chemicalAssociationForm.map.assoc.files[fileInd].displayName}" />
													<jsp:param name="fileType"
														value="${chemicalAssociationForm.map.assoc.files[fileInd].domainFile.type}" />
													<jsp:param name="fileTitle"
														value="${chemicalAssociationForm.map.assoc.files[fileInd].domainFile.title}" />
													<jsp:param name="fileKeyword"
														value="${chemicalAssociationForm.map.assoc.files[fileInd].keywordsStr}" />
													<jsp:param name="fileVisibilityGroups"
														value="${chemicalAssociationForm.map.assoc.files[fileInd].visibilityGroups}" />
												</jsp:include>

												<br>
											</logic:iterate>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</tbody>
				</table>
			</td>
		</tr>
	</table>
</html:form>
