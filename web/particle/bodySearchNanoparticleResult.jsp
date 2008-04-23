<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<link rel="stylesheet" type="text/css" href="css/displaytag.css" />
<table width="100%" align="center">
	<tr>
		<td>
			<h3>
				Nanoparticle Search Results
			</h3>
		</td>
		<td align="right" width="15%">
			<a
				href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=nano_search_results_help')"
				class="helpText">Help</a>&nbsp;&nbsp;
			<a href="searchNanoparticle.do?dispatch=setup" class="helpText">Back</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<c:choose>
				<c:when test="${canCreateNanoparticle eq 'true'}">
					<c:set var="particleURL" value="editParticleURL" />
				</c:when>
				<c:otherwise>
					<c:set var="particleURL" value="viewParticleURL" />
				</c:otherwise>
			</c:choose>
			<jsp:include page="/bodyMessage.jsp?bundle=particle" />
			<display:table name="particles" id="particle"
				requestURI="searchNanoparticle.do" pagesize="25"
				class="displaytable"
				decorator="gov.nih.nci.cananolab.dto.particle.NanoparticleDecorator">
				<display:column title="Particle<br> Sample Name"
					property="${particleURL}" sortable="true" />
				<display:column title="Particle<br> Source"
					property="domainParticleSample.source.organizationName"
					sortable="true" />
				<display:column title="Particle<br> Composition"
					property="compositionStr" sortable="true" />
				<display:column title="Particle<br> Functions"
					property="functionStr" />
				<display:column title="Particle<br> Characterizations"
					property="characterizationStr" />
				<display:column title="Particle<br> Location">Local</display:column>
			</display:table>
			<%--
				<div align="right">
					<input type="hidden" name="dispatch" value="search">
					<input type="hidden" name="page" value="1">
					<html:submit value="View Details" />
				</div>
				--%>
		</td>
	</tr>
</table>

