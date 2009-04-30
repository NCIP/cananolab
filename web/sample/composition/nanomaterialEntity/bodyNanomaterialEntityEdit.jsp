<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table id="summarySection1" width="95%" align="center"
	style="display: block" class="summaryViewLayer2">
	<tr>
		<th align="left">
			Nanomaterial Entity &nbsp;&nbsp;&nbsp;
			<a
				href="nanomaterialEntity.do?dispatch=setupNew&sampleId=${sampleId}"
				class="addlink"><img align="middle" src="images/btn_add.gif"
					border="0" /> </a> &nbsp;&nbsp;&nbsp;
			<c:if test="${!empty compositionForm.map.comp.nanomaterialEntities}">
				<a
					href="/nanopmaterialEntity.do?dispatch=delete&sampleId=${sampleId}"
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
		<c:when test="${!empty compositionForm.map.comp.nanomaterialEntities}">
			<logic:iterate name="compositionForm"
				property="comp.nanomaterialEntities" id="entity" indexId="ind">
				<c:set var="entityType" value="${entity.type}" />
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
												href="nanomaterialEntity.do?dispatch=setupUpdate&sampleId=${sampleId}&dataId=${entity.domainEntity.id}">Edit</a>
										</th>
									</tr>
									<tr>
										<td class="cellLabel" width="20%">
											Description
										</td>
										<td>
											<c:choose>
												<c:when test="${!empty fn:trim(entity.description)}">
													<c:out
														value="${fn:replace(entity.description, cr, '<br>')}"
														escapeXml="false" />
												</c:when>
												<c:otherwise>N/A
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<c:if test="${entity.withProperties }">
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
												<c:set var="entity" value="${entity}" scope="session" />
												<jsp:include page="${detailPage}">
													<jsp:param name="summary" value="true" />
												</jsp:include>
											</td>
										</tr>
									</c:if>
									<tr>
										<td class="cellLabel">
											Composing Elements
										</td>
										<td>
											<%@include file="bodyComposingElementView.jsp"%>
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
<div id="summarySeparator1">
	<br>
</div>



