<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table id="summarySection2" width="95%" align="center"
	style="display: block" class="summaryViewLayer2">
	<tr>
		<th align="left">
			Functionalizing Entity &nbsp;&nbsp;&nbsp;
			<a
				href="functionalizingEntity.do?dispatch=setupNew&sampleId=${sampleId}"
				class="addlink"><img align="middle" src="images/btn_add.gif"
					border="0" /> </a> &nbsp;&nbsp;&nbsp;
			<c:if
				test="${!empty compositionForm.map.comp.functionalizingEntities}">
				<a
					href="/functionalizingEntity.do?dispatch=delete&sampleId=${sampleId}"
					class="addlink"><img align="middle" src="images/btn_delete.gif"
						border="0" /> </a>
			</c:if>
		</th>
	</tr>
	<tr>
		<td align="left">
			<jsp:include page="/bodyMessage.jsp?bundle=particle" />
		</td>
	</tr>
	<c:choose>
		<c:when
			test="${!empty compositionForm.map.comp.functionalizingEntities}">
			<logic:iterate name="compositionForm"
				property="comp.functionalizingEntities" id="functionalizingEntity"
				indexId="ind">
				<c:set var="entityType" value="${functionalizingEntity.type}" />
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
											<a
												href="functionalizingEntity.do?dispatch=setupUpdate&sampleId=${sampleId}&dataId=${functionalizingEntity.domainEntity.id}">Edit</a>
										</th>
									</tr>
									<tr>
										<td class="cellLabel">
											Name
										</td>
										<td>
											<c:choose>
												<c:when test="${!empty functionalizingEntity.name}">
													${functionalizingEntity.name}
													</c:when>
												<c:otherwise>
														N/A
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<tr>
										<td class="cellLabel">
											Amount
										</td>
										<td>
											<c:choose>
												<c:when test="${!empty functionalizingEntity.value}">
													${functionalizingEntity.value} ${functionalizingEntity.valueUnit}
													</c:when>
												<c:otherwise>
														N/A
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<tr>
										<td class="cellLabel">
											Molecular Formula
										</td>
										<td>
											<c:choose>
												<c:when
													test="${!empty functionalizingEntity.molecularFormulaDisplayName}">
													${functionalizingEntity.molecularFormulaDisplayName}
													</c:when>
												<c:otherwise>
														N/A
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<c:if test="${functionalizingEntity.withProperties }">
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
																								"functionalizingEntity");
																				pageContext.setAttribute("detailPage",
																						detailPage);
												%>
												<c:set var="functionalizingEntity"
													value="${functionalizingEntity}" scope="session" />
												<jsp:include page="${detailPage}">
													<jsp:param name="summary" value="true" />
												</jsp:include>
											</td>
										</tr>
									</c:if>
									<tr>
										<td class="cellLabel">
											Function(s)
										</td>
										<td>
											<c:choose>
												<c:when
													test="${!empty functionalizingEntity.functionDisplayNames}">
													<c:forEach var="function"
														items="${functionalizingEntity.functionDisplayNames}">
													${function}<br>
													</c:forEach>
												</c:when>
												<c:otherwise>
													N/A
											</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<tr>
										<td class="cellLabel">
											Activation Method
										</td>
										<td>
											<c:choose>
												<c:when
													test="${!empty functionalizingEntity.activationMethodDisplayName}">
													${functionalizingEntity.activationMethodDisplayName}
													</c:when>
												<c:otherwise>
														N/A
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<tr>
										<td class="cellLabel" width="20%">
											Description
										</td>
										<td>
											<c:choose>
												<c:when
													test="${!empty fn:trim(functionalizingEntity.description)}">
													<c:out
														value="${fn:replace(functionalizingEntity.description, cr, '<br>')}"
														escapeXml="false" />
												</c:when>
												<c:otherwise>N/A
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<tr>
										<td class="cellLabel">
											Files
										</td>
										<td>
											<c:choose>
												<c:when test="${! empty functionalizingEntity.files}">
													<c:forEach var="file" items="${functionalizingEntity.files}">
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
												</c:when>
												<c:otherwise>
					N/A
					</c:otherwise>
											</c:choose>
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
				</c:if>
			</logic:iterate>
		</c:when>
		<c:otherwise>
			<tr>
				<td>
					<div class="indented4">
						N/A
					</div>
				</td>
			</tr>
		</c:otherwise>
	</c:choose>
</table>
<div id="summarySeparator2">
	<br>
</div>
