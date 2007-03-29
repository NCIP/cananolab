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
				<br>
				Nanoparticle Remote Search Results
			</h3>
		</td>
		<td align="right" width="15%">
			<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=remote_nanoparticle_search_results_help')" class="helpText">Help</a>&nbsp;&nbsp; <a href="searchNanoparticle.do?dispatch=setup" class="helpText">back</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyMessage.jsp?bundle=search" />
			<display:table name="remoteParticles" id="particle" requestURI="remoteSearchNanoparticle.do" pagesize="25" class="displaytable" decorator="gov.nih.nci.calab.dto.search.NanoparticleDecorator">
				<display:column title="Particle ID" property="remoteViewURL" sortable="true" />
				<display:column title="Particle Source" property="sampleSource" sortable="true" />
				<display:column title="Particle Classification" property="particleClassification" sortable="true" />
				<display:column title="Particle Type" property="sampleType" sortable="true" />
				<display:column title="Particle Functions" property="functionTypesStr" />
				<display:column title="Particle Characterizations" property="characterizationsStr" />
				<display:column title="Grid Node Host" property="gridNode" sortable="true" />
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

