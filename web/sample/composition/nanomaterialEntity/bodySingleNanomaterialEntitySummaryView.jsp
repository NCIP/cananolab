<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/sample/bodyHideAdvancedSearchDetailView.jsp" %>
<c:set var="entityType" value="${nanomaterialEntity.type}" />
<c:if test="${!empty entityType}">
	<tr>
		<td>
				<table class="summaryViewLayer3" width="95%" align="center">
					<tr>
						<th valign="top" align="left" colspan="2" width="1000px">
							${entityType}
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
								<c:set var="entity" value="${nanomaterialEntity}" />
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
								<c:set var="files" value="${nanomaterialEntity.files }" />
								<c:set var="entityType" value="nanomaterial entity" />
								<%@include file="../bodyFileView.jsp"%>
							</td>
						</tr>
					</c:if>
				</table>
		</td>
	</tr>
</c:if>
