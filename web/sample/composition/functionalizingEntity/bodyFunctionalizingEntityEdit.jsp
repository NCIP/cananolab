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
				property="comp.functionalizingEntities" id="entity" indexId="ind">
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
											<a
												href="functionalizingEntity.do?dispatch=setupUpdate&sampleId=${sampleId}&dataId=${entity.domainEntity.id}">Edit</a>
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
									<tr>
										<td class="cellLabel">
											Properties
										</td>
										<td>
											<%--<%@include file="bodyFunctionalizingEntityPropertiesView.jsp"%>--%>
										</td>
									</tr>

									<tr>
										<td class="cellLabel">
											Molecular Formula
										</td>
										<td>
											<c:choose>
												<c:when test="${!empty entity.molecularFormulaDisplayName}">
													${entity.molecularFormulaDisplayName}
													</c:when>
												<c:otherwise>
														N/A
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<tr>
										<td class="cellLabel">
											Function(s)
										</td>
										<td>
											<c:choose>
												<c:when test="${!empty entity.functionDisplayNames}">
													<c:forEach var="function"
														items="${entity.functionDisplayNames}">
													${function}<br>
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
