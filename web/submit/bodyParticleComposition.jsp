<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html:form action="/nanoparticleComposition">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					<br>
					Physical Characterization - Composition
				</h4>
			</td>
			<td align="right" width="15%">
				<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=create_nanoparticle')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<h5 align="center">
					${nanoparticleCompositionForm.map.particleName} (${nanoparticleCompositionForm.map.particleType})
				</h5>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=submit" />
				<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
					<tr>
					<tr class="topBorder">
						<td class="formTitle" colspan="4">
							<div align="justify">
								Summary
							</div>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Characterization Source* </strong>
						</td>
						<td class="label">
							<c:choose>
								<c:when test="${canUserUpdateParticle eq 'true'}">
									<html:select property="characterizationSource">
										<html:options name="characterizationSources"/>
									</html:select>
								</c:when>
								<c:otherwise>
						${nanoparticleCompositionForm.map.characterizationSource}&nbsp;
					</c:otherwise>
							</c:choose>
						</td>
						<td class="label">
							<strong>View Title*</strong><br>
							<em>(text will be truncated after 25 characters)</em>
						</td>
						<td class="rightLabel">
							<c:choose>
								<c:when test="${canUserUpdateParticle eq 'true'}">
									<html:text property="viewTitle" />
								</c:when>
								<c:otherwise>
						${nanoparticleCompositionForm.map.viewTitle}&nbsp;
					</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Description</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<c:choose>
								<c:when test="${canUserUpdateParticle eq 'true'}">
									<html:textarea property="description" rows="3" />
								</c:when>
								<c:otherwise>
						${nanoparticleCompositionForm.map.description}&nbsp;
					</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</table>
				<br>
				<jsp:include page="${nanoparticleCompositionForm.map.particlePage}" />
				<c:choose>
					<c:when test="${canUserUpdateParticle eq 'true'}">
						<br>
						<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
							<tr>
								<td width="30%">
									<span class="formMessage"> </span>
									<br>
									<table width="498" height="32" border="0" align="right" cellpadding="4" cellspacing="0">
										<tr>
											<td width="490" height="32">
												<div align="right">
													<div align="right">
														<input type="reset" value="Reset" onclick="">
														<input type="hidden" name="dispatch" value="create">
														<input type="hidden" name="page" value="1">
														<c:choose>
															<c:when test="${canUserUpdateParticle eq 'true'}">
																<html:hidden property="particleType" />
															</c:when>
														</c:choose>
														<html:submit />
													</div>
												</div>
											</td>
										</tr>
									</table>
									<div align="right"></div>
								</td>
							</tr>
						</table>
					</c:when>
				</c:choose>
			</td>
		</tr>
	</table>
</html:form>
<script language="JavaScript">
<!--//

  /* populate a hashtable containing particle type particles */
  var particleTypeParticles=new Array();    
  <c:forEach var="item" items="${allParticleTypeParticles}">
    var particleNames=new Array();
    <c:forEach var="particleName" items="${allParticleTypeParticles[item.key]}" varStatus="count">
  		particleNames[${count.index}]=new Option('${particleName}', '${particleName}');  	
    </c:forEach>
    particleTypeParticles['${item.key}']=particleNames;
  </c:forEach> 
//-->
</script>
