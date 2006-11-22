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
<c:choose>
	<c:when test="${canUserUpdateParticle eq 'true'}">
		<c:set var="dispatchValue" value="setupUpdate" />
	</c:when>
	<c:otherwise>
		<c:set var="dispatchValue" value="setupView" />
	</c:otherwise>
</c:choose>
<table summary="" cellpadding="0" cellspacing="0" border="0" height="100%" width="250">
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
								<span class="largerText">General Information</span>
								<br>
								<br>
								<span class="indented"> <c:choose>
										<c:when test="${canUserUpdateParticle eq 'true'}">
											<a href="nanoparticleGeneralInfo.do?dispatch=setupUpdate&particleType=${particleType}&particleName=${particleName}"">${particleName} (${particleType})</a>
										</c:when>
										<c:otherwise>
											<a href="nanoparticleGeneralInfo.do?dispatch=setupView&particleType=${particleType}&particleName=${particleName}"">${particleName} (${particleType})</a>
										</c:otherwise>
									</c:choose> </span>
								<br>
								<br>
							</li>
							<li>
								<span class="largerText">Function</span>
								<br>
								<br>
								<span class="indented">-Therapeutics &nbsp;&nbsp;</span>
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<a href="submitAction.do?submitType=therapeutics"> <em>add</em></a>
									</c:when>
								</c:choose>
								<br>
								<span class="indented">-Targeting &nbsp;&nbsp;</span>
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<a href="submitAction.do?submitType=targeting"> <em>add</em></a>
									</c:when>
								</c:choose>
								<br>
								<span class="indented">-Diagnostic Imaging &nbsp;&nbsp;</span>
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<a href="submitAction.do?submitType=imaging"><em>add</em></a>
									</c:when>
								</c:choose>
								<br>
								<span class="indented">-Diagnostic Reporting &nbsp;&nbsp;</span>
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<a href="submitAction.do?submitType=reporting"> <em>add</em></a>
									</c:when>
								</c:choose>
								<br>
								<br>
							</li>
							<li>
								<span class="largerText">Characterization</span>
								<br>
								<br>
								<span class="indented">-Physical Characterization &nbsp;&nbsp;</span>
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<a href="submitAction.do?submitType=physical"> <em>add</em></a>
									</c:when>
								</c:choose>
								<br>
								<c:forEach var="aChar" items="${charTypeChars['physical']}">
									<%
									java.util.HashMap paramMap = new java.util.HashMap();
									paramMap.put("dispatch", pageContext.getAttribute("dispatchValue"));
									paramMap.put("particleName", session.getAttribute("particleName"));
									paramMap.put("particleType", session.getAttribute("particleType"));
									paramMap.put("characterizationId",((gov.nih.nci.calab.dto.characterization.CharacterizationBean) pageContext.getAttribute("aChar")).getId());
									paramMap.put("submitType", "physical");
									pageContext.setAttribute("paramMap", paramMap);
									%>
									<span class="indented2"> <html:link forward="${aChar.name}" name="paramMap">${aChar.name}: ${aChar.viewTitle}</html:link> </span>
									<br>
								</c:forEach>
								<br>
								<span class="indented">-In Vitro Characterization</span>
								<br>
								<span class="indented2">-Toxicity &nbsp;&nbsp;</span>
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<a href="submitAction.do?submitType=tox"> <em>add</em></a>
									</c:when>
								</c:choose>
								<c:forEach var="aChar" items="${charTypeChars['tox']}">
									<%
									java.util.HashMap toxicityParamMap = new java.util.HashMap();
									toxicityParamMap.put("dispatch", pageContext.getAttribute("dispatchValue"));
									toxicityParamMap.put("particleName", session.getAttribute("particleName"));
									toxicityParamMap.put("particleType", session.getAttribute("particleType"));
									toxicityParamMap.put("characterizationId",((gov.nih.nci.calab.dto.characterization.CharacterizationBean) pageContext.getAttribute("aChar")).getId());
									toxicityParamMap.put("submitType", "tox");
									pageContext.setAttribute("toxicityParamMap", toxicityParamMap);
									%>
									<span class="indented2"> <html:link forward="${aChar.name}" name="toxicityParamMap">${aChar.name}: ${aChar.viewTitle}</html:link> </span>
									<br>
								</c:forEach>
								<br>
								<span class="indented3">-Cytotoxicity &nbsp;&nbsp;</span>
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<a href="submitAction.do?submitType=cytoTox"> <em>add</em></a>
									</c:when>
								</c:choose>
								<c:forEach var="aChar" items="${charTypeChars['cytoTox']}">
									<%
									java.util.HashMap cytotoxicityParamMap = new java.util.HashMap();
									cytotoxicityParamMap.put("dispatch", pageContext.getAttribute("dispatchValue"));
									cytotoxicityParamMap.put("particleName", session.getAttribute("particleName"));
									cytotoxicityParamMap.put("particleType", session.getAttribute("particleType"));
									cytotoxicityParamMap.put("characterizationId",((gov.nih.nci.calab.dto.characterization.CharacterizationBean) pageContext.getAttribute("aChar")).getId());
									pageContext.setAttribute("cytotoxicityParamMap", cytotoxicityParamMap);
									cytotoxicityParamMap.put("submitType", "cytoTox");
									%>
									<span class="indented2"> <html:link forward="${aChar.name}" name="cytotoxicityParamMap">${aChar.name}: ${aChar.viewTitle}</html:link> </span>
									<br>
								</c:forEach>
								<br>
								<span class="indented3">-Immunotoxicity</span>
								<br>
								<span class="indented4">-Blood Contact &nbsp;&nbsp;</span>
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<a href="submitAction.do?submitType=bloodContactTox"> <em>add</em></a>
									</c:when>
								</c:choose>
								<c:forEach var="aChar" items="${charTypeChars['bloodContactTox']}">
									<%
									java.util.HashMap bloodContactParamMap = new java.util.HashMap();
									bloodContactParamMap.put("dispatch", pageContext.getAttribute("dispatchValue"));
									bloodContactParamMap.put("particleName", session.getAttribute("particleName"));
									bloodContactParamMap.put("particleType", session.getAttribute("particleType"));
									bloodContactParamMap.put("characterizationId",((gov.nih.nci.calab.dto.characterization.CharacterizationBean) pageContext.getAttribute("aChar")).getId());
									pageContext.setAttribute("bloodContactParamMap", bloodContactParamMap);
									bloodContactParamMap.put("submitType", "bloodContactTox");
									%>
									<span class="indented2"> <html:link forward="${aChar.name}" name="bloodContactParamMap">${aChar.name}: ${aChar.viewTitle}</html:link> </span>
									<br>
								</c:forEach>
								<br>
								<span class="indented4">-Immune Cell Function &nbsp;&nbsp;</span>
								<c:choose>
									<c:when test="${canUserUpdateParticle eq 'true'}">
										<a href="submitAction.do?submitType=immuneCellFuncTox"> <em>add</em></a>
									</c:when>
								</c:choose>
								<c:forEach var="aChar" items="${charTypeChars['immuneCellFuncTox']}">
									<%
									java.util.HashMap immuneCellParamMap = new java.util.HashMap();
									immuneCellParamMap.put("dispatch", pageContext.getAttribute("dispatchValue"));
									immuneCellParamMap.put("particleName", session.getAttribute("particleName"));
									immuneCellParamMap.put("particleType", session.getAttribute("particleType"));
									immuneCellParamMap.put("characterizationId",((gov.nih.nci.calab.dto.characterization.CharacterizationBean) pageContext.getAttribute("aChar")).getId());
									pageContext.setAttribute("immuneCellParamMap", immuneCellParamMap);
									immuneCellParamMap.put("submitType", "immuneCellFuncTox");
									%>
									<span class="indented2"> <html:link forward="${aChar.name}" name="immuneCellParamMap">${aChar.name}: ${aChar.viewTitle}</html:link> </span>
									<br>
								</c:forEach>
								<br>
								<span class="indented2">-<em>Metabolic Stability</em> &nbsp;&nbsp;</span>
								<br>
								<br>
								<span class="indented">-<em>In Vivo Characterization</em></span>
								<br>
								<br>
							</li>
							<li>
								<span class="largerText">Other Associated Files &nbsp;&nbsp;</span>
								<br>
								<br>
							</li>
							<li>
								<span class="largerText">Reports</span>
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
