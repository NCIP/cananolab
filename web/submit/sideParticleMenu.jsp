<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!-- submenu begins -->
<c:choose>
	<c:when test="${!empty param.particleName}">
		<c:set var="particleName" value="${param.particleName}"
			scope="session" />
	</c:when>
</c:choose>
<c:choose>
	<c:when test="${!empty param.particleType}">
		<c:set var="particleType" value="${param.particleType}"
			scope="session" />
	</c:when>
</c:choose>
<c:choose>
	<c:when test="${canUserSubmit eq 'true'}">
		<c:set var="dispatchValue" value="setupUpdate" scope="session"/>
	</c:when>
	<c:otherwise>
		<c:set var="dispatchValue" value="setupView" scope="session"/>
	</c:otherwise>
</c:choose>
<table summary="" cellpadding="0" cellspacing="0" border="0"
	height="100%" width="250">
	<tr>
		<td class="subMenuPrimaryTitle" height="21">
			PARTICLE TREE
		</td>
	</tr>
	<tr>
		<td class="formMessage" height="100%">
			<table width="100%" height="95%" border="0" cellpadding="2"
				cellspacing="0">
				<tr>
					<td align="left" valign="top" class="formMessage">
						<ul>
							<li>
								<span class="largerText">General Information</span>
								<br>
								<br>
								<span class="indented"> <c:choose>
										<c:when test="${canUserSubmit eq 'true'}">
											<a
												href="nanoparticleGeneralInfo.do?dispatch=setupUpdate&particleType=${particleType}&particleName=${particleName}"">${particleName}
												(${particleType})</a>
										</c:when>
										<c:otherwise>
											<a
												href="nanoparticleGeneralInfo.do?dispatch=setupView&particleType=${particleType}&particleName=${particleName}"">${particleName}
												(${particleType})</a>
										</c:otherwise>
									</c:choose> </span>
								<br>
								<br>
							</li>
							<li><jsp:include page="sideParticleFunctionMenu.jsp"/></li>
							<li><jsp:include page="sideParticleCharacterizationMenu.jsp"/></li>
							<li>
								<span class="largerText">Other Associated Files
									&nbsp;&nbsp;</span>
								<c:forEach var="aReport" items="${particleAssociatedFiles}">
									<span class="indented"> <a
										href="updateReportForParticle.do?page=0&dispatch=${dispatchValue}&submitType=none&fileId=${aReport.id}&fileType=${aReport.type}"
										title="${aReport.displayName}">${aReport.name}</a> </span>
									<br>
								</c:forEach>
								<br>
								<br>
							</li>
							<li>
								<span class="largerText">Reports</span>
								<br>
								<c:forEach var="aReport" items="${particleReports}">
									<span class="indented"> <a
										href="updateReportForParticle.do?page=0&dispatch=${dispatchValue}&submitType=none&fileId=${aReport.id}&fileType=${aReport.type}"
										title="${aReport.displayName}">${aReport.name}</a> </span>
									<br>
								</c:forEach>
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
