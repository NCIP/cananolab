<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type='text/javascript' src='javascript/ParticleManager.js'></script>
<script type='text/javascript' src='dwr/interface/ParticleManager.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<html:form action="/nanoparticleGeneralInfo">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
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
			<c:when
				test="${empty allNewParticleTypes && param.dispatch eq 'setup'}">
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
						<jsp:include page="/bodyMessage.jsp?bundle=particle" />
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
										${nanoparticleGeneralInfoForm.map.particle.sampleType}										
											</c:when>
											<c:otherwise>
												<html:select styleId="particleType"
													property="particle.sampleType"
													onchange="resetParticleNames(); retrieveParticleNames()">
													<option value=""></option>
													<html:options name="allNewParticleTypes" />
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
              							${nanoparticleGeneralInfoForm.map.particle.sampleName}
											</c:when>
											<c:otherwise>
												<html:select styleId="particleName"
													property="particle.sampleName">
													<html:option
														value="${nanoparticleGeneralInfoForm.map.particle.sampleName}">${nanoparticleGeneralInfoForm.map.particle.sampleName}</html:option>
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
												${nanoparticleGeneralInfoForm.map.particle.sampleSource}
											</td>
										</tr>
									</c:when>
								</c:choose>
								<tr>
									<td class="leftLabel" valign="top">
										<strong>Keywords <em>(one word per line)</em> </strong>
									</td>
									<td class="rightLabel">
										<html:textarea property="particle.keywordsStr" rows="4" />
									</td>
								</tr>
								<tr>
									<td class="leftLabel" valign="top">
										<strong>Visibility</strong>
									</td>
									<td class="rightLabel">
										<html:select property="particle.visibilityGroups"
											multiple="true" size="6">
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
														<input type="hidden" name="submitType"
																value="${submitType}"/>
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

