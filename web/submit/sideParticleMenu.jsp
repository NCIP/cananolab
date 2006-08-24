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

<table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
	<tr>
		<td class="subMenuPrimaryTitle" height="21" width="250">
			PARTICLE TREE
		</td>
	</tr>
	<tr>
		<td class="formMessage" height="100%">
			<table width="100%" height="95%" border="0" cellpadding="2" cellspacing="0">
				<tr>
					<td align="left" valign="top" class="formMessage">
						<ul>
							<c:choose>
								<c:when test="${canUserUpdateParticle eq 'true'}">
									<li>
										<span class="largerText">General Information</span>
										<br>
										<span class="indented"><a href="nanoparticleGeneralInfo.do?dispatch=setupUpdate&particleType=${particleType}&particleName=${particleName}"">${particleName} (${particleType})</a></span>
										<br>
										<br>
									</li>
									<li>
										<span class="largerText">Function</span>
										<br>
										<span class="indented">-Therapeutics &nbsp;&nbsp;</span><a href="submitAction.do?submitType=therapeutics"><em>add</em></a>
										<br>
										<span class="indented">-Targeting &nbsp;&nbsp;</span><a href="submitAction.do?submitType=targeting"><em>add</em></a>
										<br>
										<span class="indented">-Diagnostic Imaging &nbsp;&nbsp;</span><a href="submitAction.do?submitType=imaging"><em>add</em></a>
										<br>
										<span class="indented">-Diagnostic Reporting &nbsp;&nbsp;</span><a href="submitAction.do?submitType=reporting"><em>add</em></a>
										<br>
										<br>
									</li>
									<li>
										<span class="largerText">Characterization</span>
										<br>
										<span class="indented">-Physical Characterization &nbsp;&nbsp;</span><a href="submitAction.do?submitType=physical"><em>add</em></a>
										<br>
										<span class="indented">-In Vitro Characterization</span>
										<br>
										<span class="indented2">-Toxicity &nbsp;&nbsp;</span><a href="submitAction.do?submitType=tox"><em>add</em></a>
										<br>
										<span class="indented3">-Cytotoxicity &nbsp;&nbsp;</span><a href="submitAction.do?submitType=cytoTox"><em>add</em></a>
										<br>
										<span class="indented3">-Immunotoxicity</span>
										<br>
										<span class="indented4">-Blood Contact &nbsp;&nbsp;</span><a href="submitAction.do?submitType=bloodContactTox"><em>add</em></a>
										<br>
										<span class="indented4">-Immune Cell Function &nbsp;&nbsp;</span><a href="submitAction.do?submitType=immuneCellFuncTox"><em>add</em></a>
										<br>
										<span class="indented2">-Metabolic Stability &nbsp;&nbsp;</span><a href="submitAction.do?submitType=metabolic"><em>add</em></a>
										<br>
										<span class="indented">-In Vivo Characterization</span>
										<br>
										<br>
									</li>
									<li>
										<span class="largerText">Assay Results &nbsp;&nbsp;</span><a href="addAssayResult.do?dispatch=setup&particleName=${particleName}"><em>add</em></a>
										<br>
										<br>
									</li>
									<li>
										<span class="largerText">Reports</span>
									</li>
								</c:when>
								<c:otherwise>
									<li>
										<span class="largerText">General Information</span>
										<br>
										<span class="indented"><a href="nanoparticleGeneralInfo.do?dispatch=view&particleType=${particleType}&particleName=${particleName}">${particleName} (${particleType})</a></span>
										<br>
										<br>
									</li>
									<li>
										<span class="largerText">Function</span>
										<br>
										<span class="indented">-Therapeutics &nbsp;&nbsp;</span>
										<br>
										<span class="indented">-Targeting &nbsp;&nbsp;</span>
										<br>
										<span class="indented">-Diagnostic Imaging &nbsp;&nbsp;</span>
										<br>
										<span class="indented">-Diagnostic Reporting &nbsp;&nbsp;</span>
										<br>
										<br>
									</li>
									<li>
										<span class="largerText">Characterization</span>
										<br>
										<span class="indented">-Physical Characterization &nbsp;&nbsp;</span>
										<br>
										<span class="indented">-In Vitro Characterization</span>
										<br>
										<span class="indented2">-Toxicity &nbsp;&nbsp;</span>
										<br>
										<span class="indented3">-Cytotoxicity &nbsp;&nbsp;</span>
										<br>
										<span class="indented3">-Immunotoxicity</span>
										<br>
										<span class="indented4">-Blood Contact &nbsp;&nbsp;</span>
										<br>
										<span class="indented4">-Immune Cell Function &nbsp;&nbsp;</span>
										<br>
										<span class="indented2">-Metabolic Stability &nbsp;&nbsp;</span>
										<br>
										<span class="indented">-In Vivo Characterization</span>
										<br>
										<br>
									</li>
									<li>
										<span class="largerText">Assay Results &nbsp;&nbsp;</span>
										<br>
										<br>
									</li>
									<li>
										<span class="largerText">Reports</span>
									</li>
								</c:otherwise>
							</c:choose>
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
