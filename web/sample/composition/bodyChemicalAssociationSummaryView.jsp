<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table id="summarySection${index}" width="100%" align="center"
	style="display: block" class="summaryViewLayer2">
	<tr>
		<th align="left">
			chemical association
		</th>
	</tr>
	<logic:iterate name="compositionForm"
		property="comp.chemicalAssociations" id="assoc" indexId="ind">
		<c:set var="assocType" value="${assoc.type}" />
		<c:if test="${!empty assocType}">
			<tr>
				<td>
					<table class="summaryViewLayer3" width="95%" align="center">
						<tr>
							<th valign="top" align="left" colspan="2">
								${assocType}
							</th>
						</tr>
						<c:if
							test="${! empty assoc.attachment.id && ! empty assoc.attachment.bondType}">
							<tr>
								<td class="cellLabel" width="10%">
									Bond Type
								</td>
								<td>
									${assoc.attachment.bondType}
								</td>
							</tr>
						</c:if>
						<c:if test="${!empty fn:trim(assoc.description)}">
							<tr>
								<td class="cellLabel" width="10%">
									Description
								</td>
								<td>
									<c:out value="${fn:replace(assoc.description, cr, '<br>')}"
										escapeXml="false" />
								</td>
							</tr>
						</c:if>
						<tr>
							<td class="cellLabel" width="10%">
								Associated Elements
							</td>
							<td>
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
											<img src="images/arrow_left_right_gray.gif" id="assocImg" />
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
						<c:if test="${! empty assoc.files}">
							<tr>
								<td class="cellLabel" width="10%">
									Files
								</td>
								<td>
									<c:set var="files" value="${assoc.files }" />
									<c:set var="entityType" value="chemical association" />
									<%@include file="bodyFileView.jsp"%>
								</td>
							</tr>
						</c:if>
					</table>
				</td>
			</tr>
		</c:if>
	</logic:iterate>
</table>
<div id="summarySeparator${index}">
	<br>
</div>


