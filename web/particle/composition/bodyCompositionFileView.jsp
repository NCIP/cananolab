<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<a name="Composition File"></a>
<c:url var="entityAddUrl" value="nanoparticleEntity.do">
	<c:param name="page" value="0" />
	<c:param name="dispatch" value="setup" />
	<c:param name="location" value="local" />
	<c:param name="particleId" value="${particleId}" />
	<c:param name="submitType" value="Nanoparticle Entity" />
</c:url>
<table class="smalltable3" cellpadding="0" cellspacing="0" border="0"
	width="90%">
	<tr>
		<th colspan="4" align="left">
			Composition File&nbsp;&nbsp;&nbsp;
			<a href="${entityAddUrl}" class="addlink"><img align="absmiddle"
					src="images/btn_add.gif" border="0" /> </a>
		</th>
	</tr>
	<tr>
		<td colspan="4" align="left">
			<jsp:include page="/bodyMessage.jsp?bundle=particle" />
		</td>
	</tr>
	<c:if test="${!empty compositionForm.map.comp.nanoparticleEntities}">
		<logic:iterate name="compositionForm"
			property="comp.nanoparticleEntities" id="entity" indexId="ind">
			<c:if test="${!empty entity.className}">
				<tr>
					<td>
						<div class="indented4">
						<table class="smalltable2" cellpadding="0" cellspacing="0"
							border="0">
							<tr>
								<td valign="top" colspan="2"><a href="#">${entity.className}</a></td>
							</tr>
							<tr>
								<td valign="top" align="left">&nbsp;</td>
								<td valign="top" align="left">
									<jsp:include
										page="/particle/composition/nanoparticleEntity/body${entity.className}Info.jsp">
										<jsp:param name="entityIndex"
											value="${ind}" />
									</jsp:include>
								</td>
							</tr>
						</table>
						</div>
					</td>
				</tr>
			</c:if>
		</logic:iterate>

	</c:if>


</table>



