<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type='text/javascript' src='javascript/addDropDownOptions.js'></script>
<script type='text/javascript' src='javascript/OrganizationManager.js'></script>

<script type="text/javascript"
	src="javascript/ParticleManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/NanoparticleSampleManager.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">

<c:set var="action" value="Submit" scope="request" />
<c:if
	test="${param.dispatch eq 'setupUpdate' || updateDataTree eq 'true'}">
	<c:set var="action" value="Update" scope="request" />
</c:if>
<html:form action="/submitNanoparticleSample">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					${action} Nanoparticle Sample
				</h3>
			</td>
			<td align="right" width="20%">
				<jsp:include page="/helpGlossary.jsp">
					<jsp:param name="topic" value="submit_nano_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>					
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=particle" />
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="2">
								<div align="justify">
									Nanoparticle Sample Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Nanoparticle Sample Name *</strong>
							</td>
							<td class="rightLabel">
								<html:text
									property="particleSampleBean.domainParticleSample.name" size="50"/>
								<c:if
									test="${!empty nanoparticleSampleForm.map.particleSampleBean.domainParticleSample.id}">
									<html:hidden
										property="particleSampleBean.domainParticleSample.id"
										value="${nanoparticleSampleForm.map.particleSampleBean.domainParticleSample.id}" />
								</c:if>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Primary Organization *</strong>
							</td>
							<td class="rightLabel">
								<html:select
									property="particleSampleBean.domainParticleSample.primaryOrganization.name"
									styleId="sampleOrganization"
									onchange="javascript:setupOrganization(nanoparticleSampleForm, 'sampleOrganization');
												removeSourceVisibility();">
									<option />
									<html:options collection="allUserParticleOrganizations"
											labelProperty="name" property="name" />
									<option value="other">
										[Other]
									</option>
								</html:select>&nbsp;
								<a href="#"	onclick="javascript:setupOrgDetailView(nanoparticleSampleForm, 'sampleOrganization');">
									<span class="addLink2">View Detail</span> </a>
							</td>
						</tr>
						<tr>
							<td class="leftLabel" valign="top">
								<strong>Keywords</strong> <i>(one keyword per line)</i>
							</td>
							<td class="rightLabel">
								<html:textarea property="particleSampleBean.keywordsStr"
									rows="6" />
							</td>
						</tr>
						<tr>
							<td class="leftLabel" valign="top">
								<strong>Visibility</strong>
							</td>
							<td class="rightLabel">
								<html:select property="particleSampleBean.visibilityGroups"
									styleId="visibilityGroup" multiple="true" size="6">
									<html:options name="allVisibilityGroupsNoSource" />
								</html:select>
								<br>
								<i>(${applicationOwner}_Researcher and
									${applicationOwner}_DataCurator are always selected by default.)</i>
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
												<input type="reset" value="Reset" onclick="javascript:location.href='submitNanoparticleSample.do?dispatch=setup&page=0'">		
												<input type="hidden" name="dispatch" value="create">
												<input type="hidden" name="page" value="1">
												<html:hidden property="particleSampleBean.createdBy"
													value="${user.loginName }" />
												<html:submit value="${action}" />
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
	</table>
</html:form>

