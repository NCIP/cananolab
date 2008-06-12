<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html:form action="/chemicalAssociation">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					${fn:toUpperCase(param.location)} ${particleName} Sample Composition - Chemical Association
				</h4>
			</td>
			<td align="right" width="20%">
				<jsp:include page="/webHelp/helpGlossary.jsp">
					<jsp:param name="topic" value="chem_association_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>				
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
							<c:if
								test="${! empty chemicalAssociationForm.map.assoc.attachment.id}">
								<Strong>Bond Type</Strong>
							</c:if>
						</td>
						<td class="rightLabel">
							&nbsp;
							<c:if
								test="${! empty chemicalAssociationForm.map.assoc.attachment.id}">
								${chemicalAssociationForm.map.assoc.attachment.bondType}
							</c:if>
						</td>
					</tr>
					<tr>
						<td class="completeLabel" colspan="4">
							<div id="assocEleBlockA" class="assocEleBlock">
								<ul>
									<li>
										<strong>Element</strong>
									</li>
									<li>
										&nbsp;&nbsp;${chemicalAssociationForm.map.assoc.associatedElementA.compositionType}
									</li>
									<li>
										&nbsp;&nbsp;&nbsp;&nbsp;${chemicalAssociationForm.map.assoc.associatedElementA.entityDisplayName}
									</li>
									<c:if
										test="${! empty chemicalAssociationForm.map.assoc.associatedElementA.composingElement.id }">
										<li>
											&nbsp;&nbsp;&nbsp;&nbsp;${chemicalAssociationForm.map.assoc.associatedElementA.composingElement.type}:${chemicalAssociationForm.map.assoc.associatedElementA.composingElement.name}
										</li>
									</c:if>
								</ul>
							</div>
							<div id="assocEleLinkBlock" class="arrowBlock">
								<img src="images/arrow.small.left.gif" />
								<img src="images/arrow.small.right.gif" />
								<br>
								<strong>associated with</strong>
							</div>
							<div id="assocEleBlockB" class="assocEleBlock">
								<ul>
									<li>
										<strong>Element</strong>
									</li>
									<li>
										&nbsp;&nbsp;${chemicalAssociationForm.map.assoc.associatedElementB.compositionType}
									</li>
									<li>
										&nbsp;&nbsp;&nbsp;&nbsp;${chemicalAssociationForm.map.assoc.associatedElementB.entityDisplayName}
									</li>
									<c:if
										test="${! empty chemicalAssociationForm.map.assoc.associatedElementB.composingElement.id }">
										<li>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${chemicalAssociationForm.map.assoc.associatedElementB.composingElement.type}:${chemicalAssociationForm.map.assoc.associatedElementB.composingElement.name}
										</li>
									</c:if>
								</ul>
							</div>
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
						<c:if test="${!empty chemicalAssociationForm.map.assoc.files}">
							<tr class="topBorder">
								<td class="formTitle" colspan="4">
									<div align="justify" id="peFileTitle">
										Chemical Association File Information
									</div>
								</td>
							</tr>
							<tr>
								<td class="completeLabel" colspan="4">
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
											<jsp:param name="fileType"
												value="${chemicalAssociationForm.map.assoc.files[fileInd].domainFile.type}" />
											<jsp:param name="fileTitle"
												value="${chemicalAssociationForm.map.assoc.files[fileInd].domainFile.title}" />
											<jsp:param name="fileKeywordsStr"
												value="${chemicalAssociationForm.map.assoc.files[fileInd].keywordsStr}" />
											<jsp:param name="visibilityStr"
												value="${chemicalAssociationForm.map.assoc.files[fileInd].visibilityStr}" />
											<jsp:param name="uriExternal"
												value="${chemicalAssociationForm.map.entity.files[fileInd].domainFile.uriExternal}" />
											<jsp:param name="externalUrl"
												value="${chemicalAssociationForm.map.entity.files[fileInd].domainFile.uri}" />
											<jsp:param name="fileImage"
												value="${chemicalAssociationForm.map.assoc.files[fileInd].image}" />
											<jsp:param name="fileHidden"
												value="${chemicalAssociationForm.map.assoc.files[fileInd].hidden}" />
														
												
										</jsp:include>
	
										<br>
									</logic:iterate>
								</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</td>
		</tr>
	</table>
</html:form>
