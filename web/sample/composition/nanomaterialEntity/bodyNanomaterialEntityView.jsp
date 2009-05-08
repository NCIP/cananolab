<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table id="summarySection1" width="95%" align="center"
	style="display: block" class="summaryViewLayer2">
	<tr>
		<th align="left">
			Nanomaterial Entity
		</th>
	</tr>
	<logic:iterate name="compositionForm"
		property="comp.nanomaterialEntities" id="nanomaterialEntity"
		indexId="ind">
		<c:set var="entityType" value="${nanomaterialEntity.type}" />
		<c:if test="${!empty entityType}">
			<tr>
				<td>
					<div class="indented4">
						<table class="summaryViewLayer3" width="95%" align="center">
							<tr>
								<th valign="top" align="left">
									${entityType}
								</th>
								<th valign="top" align="right">
								</th>
							</tr>
							<c:if
								test="${!empty fn:trim(nanomaterialEntity.emulsion.description)}">
								<tr>
									<td class="cellLabel">
										Description
									</td>
									<td>
										<c:out
											value="${fn:replace(nanomaterialEntity.emulsion.description, cr, '<br>')}"
											escapeXml="false" />
									</td>
								</tr>
							</c:if>
							<c:if test="${nanomaterialEntity.withProperties }">
								<tr>
									<td class="cellLabel">
										Properties
									</td>
									<td>
										<%
											String detailPage = gov.nih.nci.cananolab.ui.sample.InitCompositionSetup
																		.getInstance()
																		.getDetailPage(
																				application,
																				(String) pageContext
																						.getAttribute("entityType"),
																				"nanomaterialEntity");
																pageContext.setAttribute("detailPage",
																		detailPage);
										%>
										<c:set var="nanomaterialEntity" value="${nanomaterialEntity}"
											scope="session" />
										<jsp:include page="${detailPage}">
											<jsp:param name="summary" value="true" />
										</jsp:include>
									</td>
								</tr>
							</c:if>
							<c:if test="${! empty nanomaterialEntity.composingElements }">
								<tr>
									<td class="cellLabel">
										Composing Elements
									</td>
									<td>
										<%@include file="bodyComposingElementView.jsp"%>
									</td>
								</tr>
							</c:if>
							<c:if test="${! empty nanomaterialEntity.files}">
								<tr>
									<td class="cellLabel">
										Files
									</td>
									<td>
										<c:forEach var="file" items="${nanomaterialEntity.files}">
											<c:choose>
												<c:when test="${file.image eq 'true'}">
						 				${file.domainFile.title}
										<br>
													<a href="#"
														onclick="popImage(event, 'composition.do?dispatch=download&amp;fileId=${file.domainFile.id}&amp;location=${location}', ${file.domainFile.id}, 100, 100)"><img
															src="composition.do?dispatch=download&amp;fileId=${file.domainFile.id}&amp;location=${location}"
															border="0" width="150"> </a>
												</c:when>
												<c:otherwise>
													<a
														href="composition.do?dispatch=download&amp;fileId=${file.domainFile.id}&amp;location=${location}">
														${file.domainFile.title}</a>
												</c:otherwise>
											</c:choose>
											<br>
										</c:forEach>
									</td>
								</tr>
							</c:if>
						</table>
					</div>
				</td>
			</tr>
		</c:if>
	</logic:iterate>
</table>
<div id="summarySeparator1">
	<br>
</div>