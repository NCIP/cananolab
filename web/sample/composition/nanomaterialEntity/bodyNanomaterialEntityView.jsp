<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:url var="entityAddUrl" value="nanomaterialEntity.do">
	<c:param name="page" value="0" />
	<c:param name="dispatch" value="setup" />
	<c:param name="location" value="local" />
	<c:param name="sampleId" value="${sampleId}" />
	<c:param name="submitType" value="Nanomaterial Entity" />
</c:url>
<table id="summarySection1" width="95%" align="center"
	style="display: block" class="summaryViewLayer2">
	<tr>
		<th align="left">
			Nanomaterial Entity
		</th>
	</tr>
	<tr>
		<td align="left">
			<jsp:include page="/bodyMessage.jsp?bundle=particle" />
		</td>
	</tr>
	<logic:iterate name="compositionForm"
		property="comp.nanomaterialEntities" id="entity" indexId="ind">
		<c:if test="${!empty entity.className}">
			<tr>
				<td>
					<div class="indented4">
						<table class="summaryViewLayer3" width="95%" align="center">
							<tr>
								<th valign="top" align="left">
									${entity.className}
								</th>
								<th valign="top" align="right">
								</th>
							</tr>
							<c:if test="${!empty fn:trim(entity.emulsion.description)}">
								<tr>
									<td class="cellLabel">
										Description
									</td>
									<td>
										<c:out
											value="${fn:replace(entity.emulsion.description, cr, '<br>')}"
											escapeXml="false" />
									</td>
								</tr>
							</c:if>
							<%--
									<tr>
										<td valign="top" colspan="2">
											PROPERTIES
											<div class="indented4">

												<jsp:include
													page="/sample/composition/nanomaterialEntity/body${entity.className}Info.jsp">
													<jsp:param name="entityIndex" value="${ind}" />
												</jsp:include>

											</div>
										</td>
									</tr>
--%>
							<c:if test="${! empty entity.composingElements }">
								<tr>
									<td class="cellLabel">
										Composing Elements
									</td>
									<td>
										<jsp:include page="bodyComposingElementView.jsp">
											<jsp:param name="entityIndex" value="${ind}" />
										</jsp:include>
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