<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<link rel="stylesheet" type="text/css" href="css/displaytag.css" />
<html:form action="/selectNanoparticle">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					<br>
					Particle Search Results
				</h3>
			</td>
			<td align="right" width="15%">
				<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=sample_search_results')" class="helpText">Help</a>&nbsp;&nbsp; <a href="searchSample.do?dispatch=setup&page=0&rememberSearch=true"
					class="helpText">back</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">

				<jsp:include page="/bodyMessage.jsp?bundle=search" />
				<display:table name="particles" id="particle" requestURI="searchNanoparticle.do" pagesize="25" class="displaytable">
					<display:column title="Particle ID" property="particle.sortableName" sortable="true" />
					<display:column title="Particle Source" property="particle.particleSource" sortable="true" />
					<display:column title="Particle Type" property="particle.particleType" sortable="true" />
					<display:column title="Particle Functions" property="particle.particleFunctions" />
					<display:column title="Particle Characterization Categroies" property="particle.characterizationTypes" />
					<display:column title="Particle Keywords" property="particle.keywords" />
				</display:table>
				<div align="right">
					<input type="hidden" name="dispatch" value="search">
					<input type="hidden" name="page" value="1">
					<html:submit value="View Details" />
				</div>
			</td>
		</tr>
	</table>
	</form>