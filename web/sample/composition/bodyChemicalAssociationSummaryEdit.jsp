<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table id="summarySection3" width="100%" align="center"
	style="display: block" class="summaryViewNoGrid">
	<tr>
		<th align="left">
			<span class="summaryViewHeading">chemical association</span>&nbsp;&nbsp;
			<a
				href="chemicalAssociation.do?dispatch=setupNew&sampleId=${sampleId}"
				class="addlink"><img align="middle" src="images/btn_add.gif"
					border="0" /></a> &nbsp;&nbsp;&nbsp;
			<%--
			<c:if test="${!empty compositionForm.map.comp.chemicalAssociations}">
				<a
					href="/chemicalAssociation.do?dispatch=delete&sampleId=${sampleId}"
					class="addlink"><img align="middle" src="images/btn_delete.gif"
						border="0" /> </a>
			</c:if>
			--%>
		</th>
	</tr>
	<tr>
		<td>
			<c:choose>
				<c:when
					test="${!empty compositionForm.map.comp.chemicalAssociations}">
					<logic:iterate name="compositionForm"
						property="comp.chemicalAssociations" id="assoc" indexId="ind">
						<c:set var="assocType" value="${assoc.type}" />
						<c:if test="${!empty assocType}">
							<table class="summaryViewNoGrid" width="99%" align="center"
								bgcolor="#dbdbdb">
								<tr>
									<th valign="top" align="left" colspan="2" width="90%">
										&nbsp;${assocType}
									</th>
								</tr>
								<tr>
									<td>
										<table class="summaryViewNoGrid" width="99%" align="center"
											bgcolor="#F5F5f5">
											<tr>
												<td></td>
												<td width="95%"></td>
												<td align="right">
													<a
														href="chemicalAssociation.do?dispatch=setupUpdate&sampleId=${sampleId}&dataId=${assoc.domainAssociation.id}">Edit</a>
												</td>
											</tr>
											<c:if test="${! empty assoc.attachment.id}">
												<tr>
													<td class="cellLabel" width="10%">
														Bond Type
													</td>
													<td colspan="2">
														<c:choose>
															<c:when test="${!empty assoc.attachment.bondType}">
												${assoc.attachment.bondType}
											</c:when>
															<c:otherwise>N/A
											</c:otherwise>
														</c:choose>
													</td>
												</tr>
											</c:if>
											<tr>
												<td class="cellLabel" width="10%">
													Description
												</td>
												<td colspan="2">
													<c:choose>
														<c:when test="${!empty fn:trim(assoc.description)}">
															<c:out
																value="${fn:replace(assoc.description, cr, '<br>')}"
																escapeXml="false" />
														</c:when>
														<c:otherwise>N/A
												</c:otherwise>
													</c:choose>
												</td>
											</tr>
											<tr>
												<td class="cellLabel" width="10%">
													Associated Elements
												</td>
												<td colspan="2">
													<table>
														<tr>
															<td>
																${assoc.associatedElementA.compositionType}
																${assoc.associatedElementA.entityDisplayName}
																<c:choose>
																	<c:when
																		test="${! empty assoc.associatedElementA.composingElement.id }">
											composing element of type ${assoc.associatedElementA.composingElement.type} <br>(name: ${assoc.associatedElementA.composingElement.name})
														</c:when>
																	<c:otherwise>
																		<br>(name: ${assoc.associatedElementA.domainElement.name})
															</c:otherwise>
																</c:choose>
															</td>
															<td
																style="border: 0; vertical-align: top; text-align: center;">
																<img src="images/arrow_left_right_gray.gif"
																	id="assocImg" />
																<br>
																<strong>associated with</strong>
															</td>
															<td>
																${assoc.associatedElementB.compositionType}
																${assoc.associatedElementB.entityDisplayName}
																<c:choose>
																	<c:when
																		test="${! empty assoc.associatedElementB.composingElement.id }">

composing element of type ${assoc.associatedElementB.composingElement.type} <br>(name: ${assoc.associatedElementB.composingElement.name})
														</c:when>
																	<c:otherwise>
																		<br> (name: ${assoc.associatedElementB.domainElement.name})
															</c:otherwise>
																</c:choose>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td class="cellLabel" width="10%">
													Files
												</td>
												<td colspan="2">
													<c:choose>
														<c:when test="${! empty assoc.files}">
															<c:set var="files" value="${assoc.files }" />
															<c:set var="entityType" value="chemical association" />
															<%@include file="../bodyFileView.jsp"%>
														</c:when>
														<c:otherwise>
											N/A
											</c:otherwise>
													</c:choose>
												</td>
											</tr>
										</table>
										<br>
									</td>
								</tr>
							</table>
						</c:if>
						<br />
					</logic:iterate>
				</c:when>
				<c:otherwise>
					<div class="indented4">
						N/A
					</div>

				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
<div id="summarySeparator3">
	<br>
</div>



