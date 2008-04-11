<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type='text/javascript' src='javascript/addDropDownOptions.js'></script>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">

<c:set var="action" value="Submit" scope="request" />
<c:if test="${param.dispatch eq 'setupUpdate'}">
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
			<td align="right" width="15%">
				<a
					href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=annotate_nano_help')"
					class="helpText">Help</a>
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
								<html:text property="particleSampleBean.particleSample.name" />
								<c:if
									test="${!empty nanoparticleSampleForm.map.particleSampleBean.particleSample.id}">
									<html:hidden property="particleSampleBean.particleSample.id"
										value="${nanoparticleSampleForm.map.particleSampleBean.particleSample.id}" />
								</c:if>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Nanoparticle Sample Source *</strong>
							</td>
							<td class="rightLabel">
								<html:select
									property="particleSampleBean.particleSample.source.organizationName"
									styleId="sampleSource"
									onchange="javascript:callPrompt('Nanoparticle Sample Source', 'sampleSource');">
									<option />
										<html:options collection="allParticleSources"
											labelProperty="organizationName" property="organizationName" />
									<option value="other">
										[Other]
									</option>
								</html:select>
							</td>
						</tr>
						<tr>
							<td class="leftLabel" valign="top">
								<strong>Keywords <em>(one word per line)</em> </strong>
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

