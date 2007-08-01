<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html:form action="/nanoparticleFunction">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					<br>
					Particle ${submitType} Function
					<html:hidden property="function.type" value="${submitType}" />
				</h4>
			</td>
			<td align="right" width="15%">
				<a
					href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=particle_function_help')"
					class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<h5 align="center">
					${nanoparticleFunctionForm.map.particleName}
					(${nanoparticleFunctionForm.map.particleType})
				</h5>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=submit" />
				<jsp:include page="bodyFunctionSummary.jsp" />

				<%-- Function Linkage Agent --%>

				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify">
									Linkage Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<table border="0" width="100%">
									<tr>
										<c:choose>
											<c:when test="${canUserSubmit eq 'true'}">
												<td valign="bottom">
													<a href="#"
														onclick="javascript:addLinkage(nanoparticleFunctionForm)"><span
														class="addLink">Add Linkage</span> </a>
												</td>
											</c:when>
											<c:otherwise>
												<td></td>
											</c:otherwise>
										</c:choose>
										<td>
											<logic:iterate id="linkage" name="nanoparticleFunctionForm"
												property="function.linkages" indexId="linkageInd">
												<jsp:include
													page="/submit/function/bodyParticleFunctionLinkage.jsp">
													<jsp:param name="linkageInd" value="${linkageInd}" />
												</jsp:include>
												<br>
											</logic:iterate>
										</td>
									</tr>
								</table>
							</td>
						</tr>
				</table>
			</td>
		</tr>
	</table>
	<br>
	<table width="100%" border="0" align="center" cellpadding="3"
		cellspacing="0" class="topBorderOnly" summary="">
		<tr>
			<td width="30%">
				<span class="formMessage"> </span>
				<br>
				<c:choose>
					<c:when test="${canUserSubmit eq 'true'}">
						<table height="32" border="0" align="right" cellpadding="4"
							cellspacing="0">
							<tr>
								<td width="490" height="32">
									<div align="right">
										<input type="reset" value="Reset" onclick="">
										<input type="hidden" name="dispatch" value="create">
										<input type="hidden" name="page" value="2">
										<html:submit />
									</div>
								</td>
							</tr>
						</table>
					</c:when>
				</c:choose>
				<div align="right"></div>
			</td>
		</tr>
	</table>
</html:form>
