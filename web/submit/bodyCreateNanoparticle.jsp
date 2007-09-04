<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html:form action="/nanoparticleGeneralInfo">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					<br>
					Submit Nanoparticle - General Information
				</h3>
			</td>
			<td align="right" width="15%">
				<a
					href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=nano_general_info_help')"
					class="helpText">Help</a> 
			</td>
		</tr>
		<c:choose>
			<c:when test="${empty allParticleTypes && param.dispatch eq 'setup'}">
				<tr>
					<td colspan="2">
						<font color="blue" size="-1"><b>MESSAGE: </b>There are no
							un-annotated nanoparticles in the database. Please make sure to
							either create a new sample or go to Search Nanoparticle to update
							an existing annotated nanoparticle. </font>
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<tr>
					<td colspan="2">
						<jsp:include page="/bodyMessage.jsp?bundle=submit" />
						<table class="topBorderOnly" cellspacing="0" cellpadding="3"
							width="100%" align="center" summary="" border="0">
							<tbody>
								<tr class="topBorder">
									<td class="formTitle" colspan="2">
										<div align="justify">
											General Information
										</div>
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Particle Type *</strong>
									</td>
									<td class="rightLabel">
										<c:choose>
											<c:when test="${param.dispatch eq 'setupUpdate'}">
										${nanoparticleGeneralInfoForm.map.particleType}
										<html:hidden property="particleType" />
											</c:when>
											<c:otherwise>
												<html:select property="particleType"
													onchange="javascript:doubleDropdown(document.nanoparticleGeneralInfoForm.particleType, document.nanoparticleGeneralInfoForm.particleName, particleTypeParticles)">
													<option value=""></option>
													<html:options name="allParticleTypes" />
												</html:select>
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Particle ID *</strong>
									</td>
									<td class="rightLabel">
										<c:choose>
											<c:when test="${param.dispatch eq 'setupUpdate'}">
              							${nanoparticleGeneralInfoForm.map.particleName}
              							<html:hidden property="particleName" />
											</c:when>
											<c:otherwise>
												<html:select property="particleName">
													<option value=""></option>
													<c:forEach var="name"
														items="${allParticleTypeParticles[nanoparticleGeneralInfoForm.map.particleType]}">
														<html:option value="${name}">${name}</html:option>
													</c:forEach>
												</html:select>
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
								<c:choose>
									<c:when test="${param.dispatch eq 'setupUpdate'}">
										<tr>
											<td class="leftLabel">
												<strong>Particle Source</strong>
											</td>
											<td class="rightLabel">
												${nanoparticleGeneralInfoForm.map.particleSource}
												<html:hidden property="particleSource" />
											</td>
										</tr>
									</c:when>
								</c:choose>
								<tr>
									<td class="leftLabel" valign="top">
										<strong>Keywords <em>(one word per line)</em> </strong>
									</td>
									<td class="rightLabel">
										<html:textarea property="keywords" rows="4" />
									</td>
								</tr>
								<tr>
									<td class="leftLabel" valign="top">
										<strong>Visibility</strong>
									</td>
									<td class="rightLabel">
										<html:select property="visibilities" multiple="true" size="6">
											<html:options name="allVisibilityGroups" />
										</html:select>
										<br>
										<i>(${applicationOwner}_Researcher and
											${applicationOwner}_PI are defaults if none of above is
											selected.)</i>
									</td>
								</tr>
							</tbody>
						</table>
						<br>
						<table width="100%" border="0" align="center" cellpadding="3"
							cellspacing="0" class="topBorderOnly" summary="">
							<tr>
								<td width="30%">
									<span class="formMessage"> </span>
									<br>
									<table width="498" height="32" border="0" align="right"
										cellpadding="4" cellspacing="0">
										<tr>
											<td width="490" height="32">
												<div align="right">
													<div align="right">
														<input type="reset" value="Reset" onclick="">
														<input type="hidden" name="dispatch" value="create">
														<input type="hidden" name="page" value="1">
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
					</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</table>
</html:form>
<script language="JavaScript">
<!--//

  /* populate a hashtable containing particle type particles */
  var particleTypeParticles=new Array();    
  <c:forEach var="item" items="${allParticleTypeParticles}">
    var particleNames=new Array();
    <c:forEach var="particleName" items="${allParticleTypeParticles[item.key]}" varStatus="count">
  		particleNames[${count.index}]='${particleName}';  	
    </c:forEach>
    particleTypeParticles['${item.key}']=particleNames;
  </c:forEach> 
//-->
</script>
