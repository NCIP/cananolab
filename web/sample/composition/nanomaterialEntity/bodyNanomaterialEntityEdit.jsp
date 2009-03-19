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
<table id="summarySection1" style="display: block;" class="smalltable3"
	cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<th colspan="4" align="left">
			Nanomaterial Entity &nbsp;&nbsp;&nbsp;
			<a href="${entityAddUrl}" class="addlink"><img align="absmiddle"
					src="images/btn_add.gif" border="0" />
			</a> &nbsp;&nbsp;&nbsp;
			<c:if test="${!empty compositionForm.map.comp.nanomaterialEntities}">
				<a href="${entityAddUrl}" class="addlink"><img align="absmiddle"
						src="images/btn_delete.gif" border="0" /> </a>
			</c:if>
		</th>
	</tr>
	<tr>
		<td colspan="4" align="left">
			<jsp:include page="/bodyMessage.jsp?bundle=particle" />
		</td>
	</tr>
	<c:choose>
		<c:when test="${!empty compositionForm.map.comp.nanomaterialEntities}">
			<logic:iterate name="compositionForm"
				property="comp.nanomaterialEntities" id="entity" indexId="ind">
				<c:if test="${!empty entity.className}">
					<tr>
						<td>
							<div class="indented4">
								<table class="summarytable" cellpadding="0" cellspacing="0"
									border="0" width="90%">
									<tr>
										<th valign="top" align="left">
											${entity.className}
										</th>
										<th valign="top" align="right">
											<a href="#">Edit</a>
										</th>
									</tr>
									<tr>
										<td valign="top" colspan="2">
											${fn:toUpperCase('Description')}
											<div class="indented5">
												${entity.emulsion.description}
											</div>
										</td>
									</tr>

									<tr>
										<td valign="top" colspan="2">
											PROPERTIES
											<div class="indented4"><%--
												<jsp:include
													page="/sample/composition/nanomaterialEntity/body${entity.className}Info.jsp">
													<jsp:param name="entityIndex" value="${ind}" />
												</jsp:include>--%>
											</div>
										</td>
									</tr>


									<tr>
										<td valign="top" colspan="2" align="left">
											COMPOSING ELEMENTS:
											<div class="indented4">
												<jsp:include page="bodyComposingElementView.jsp">
													<jsp:param name="entityIndex" value="${ind}" />
												</jsp:include>
											</div>

										</td>
									</tr>
								</table>
							</div>
							&nbsp;&nbsp;
						</td>
					</tr>
				</c:if>
			</logic:iterate>
		</c:when>
		<c:otherwise>
			<tr>
				<td>
					<div class="indented4">
						<table class="summarytable" border="0" width="90%">
							<tr>
								<td>
									N/A
								</td>
							</tr>
						</table>
						&nbsp;
					</div>
					&nbsp;
				</td>
			</tr>
		</c:otherwise>
	</c:choose>
</table>
<div id="summarySeparator1">
	<br>
</div>



