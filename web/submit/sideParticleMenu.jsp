<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!-- submenu begins -->
<c:choose>
	<c:when test="${!empty param.particleName}">
		<c:set var="particleName" value="${param.particleName}" scope="session" />
	</c:when>
</c:choose>
<c:choose>
	<c:when test="${!empty param.particleType}">
		<c:set var="particleType" value="${param.particleType}" scope="session" />
	</c:when>
</c:choose>
<table summary="" cellpadding="0" cellspacing="0" border="0" width="220" height="100%">
	<tr>
		<td class="subMenuPrimaryTitle" height="21">
			PARTICLE TREE
		</td>
	</tr>
	<tr>
		<td class="formMessage" height="100%">
			<table width="100%" height="95%" border="0" cellpadding="2" cellspacing="0">
				<tr>
					<td align="left" valign="top" class="formMessage">
						<ul>
							<li>
								General Information
								<dl class="indented">
									-
									<a href="#">${particleName}</a>
								</dl>
							</li>
							<li>
								Nanoparticle Type
								<dl class="indented">
									-
									<a href="nanoparticleProperties.do?dispatch=setup&particleType=${particleType}&particleName=${particleName}">${particleType}</a>
								</dl>
							</li>
							<li>
								Nanoparticle Function
								<logic:iterate name="allParticleFunctions" id="function">
									<dl class="indented">
										-${function}
									</dl>
								</logic:iterate>
							</li>
							<li>
								Nanoparticle Characterization

								<logic:iterate name="allParticleCharacterizationTypeCharacterizations" id="charTypeChar">
									<dl class="indented">
										-${charTypeChar.key}
										<br>
										<logic:iterate name="charTypeChar" property="value" id="characterization">
											<span class="indented"> -<a href="#">Add ${characterization}</a><br> </span>
										</logic:iterate>
									</dl>
								</logic:iterate>

							</li>
							<li>
								Assay Results
								<dl class="indented">
									-
									<a href="addAssayResult.do?dispatch=setup&particleName=${particleName}">Add Assay Results</a>
								</dl>
							</li>
							<li>
								Reports
							</li>
						</ul>
						<p>
							&nbsp;
						</p>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="subMenuFooter" height="22">
			&nbsp;
		</td>
	</tr>
</table>
