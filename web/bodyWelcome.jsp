<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table>
	<tr>
		<td class="welcomeTitle" height="20">
			WELCOME TO caNanoLab
		</td>
	</tr>
	<tr>
		<td align="right">
			<a
				href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=welcome_workflow')"
				class="helpText">Help</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
	</tr>
	<tr>
		<td class="welcomeContent" valign="top">
			Welcome to the
			<strong>cancer Nanotechnology Laboratory (caNanoLab)</strong> portal.
			caNanoLab is a data sharing portal designed for laboratories
			performing nanoparticle assays. caNanoLab provides support for the
			annotation of nanoparticles with characterizations resulting from
			physical and in vitro nanoparticle assays and the sharing of these
			characterizations in a secure fashion. The diagram below describes
			the caNanoLab functionality. Hotspots are defined on the diagram to
			allow users to directly navigate to the appropriate functions visible
			to the users.
			<br>
			<br>
			<map name="funcMap" id="funcMap">
				<c:if test="${isAdmin eq 'true' }">
					<area href="javascript:openHelpWindow('/upt')" shape="rect"
						coords="80,80,160,110" alt="rectangle" />
				</c:if>
				<area href="login.jsp" shape="rect" coords="80,140,160,170"
					alt="rectangle" />
				<area href="searchProtocol.do?dispatch=setup" shape="rect"
					coords="80,240,160,270" alt="rectangle" />
				<area href="searchReport.do?dispatch=setup" shape="rect"
					coords="80,280,160,310" alt="rectangle" />
				<area href="searchNanoparticle.do?dispatch=setup" shape="rect"
					coords="80,320,160,350" alt="rectangle" />
				<c:if test="${canCreateSample eq 'true'}">
					<area href="searchSample.do?dispatch=setup" shape="rect"
						coords="80,360,160,390" alt="rectangle" />
					<area href="searchAliquot.do?dispatch=setup" shape="rect"
						coords="80,400,160,430" alt="rectangle" />
				</c:if>
				<c:if test="${canCreateProtocol eq 'true'}">
					<area href="submitProtocol.do?dispatch=setup&page=0" shape="rect"
						coords="270,65,330,100" alt="rectangle" />
				</c:if>
				<c:if test="${canCreateSample eq 'true'}">
					<area href="createSample.do?dispatch=setup&page=0" shape="rect"
						coords="270,120,330,150" alt="rectangle" />
					<area href="createAliquot.do?dispatch=setup&page=0" shape="rect"
						coords="370,120,430,150" alt="rectangle" />
				</c:if>
				<c:if test="${canCreateReport eq 'true'}">
					<area href="submitReport.do?dispatch=setup&page=0" shape="rect"
						coords="330,170,400,200" alt="rectangle" />
				</c:if>
				<c:if test="${canCreateNanoparticle eq 'true'}">
					<area href="nanoparticleGeneralInfo.do?dispatch=setup&page=0"
						shape="rect" coords="330,310,400,350" alt="rectangle" />
					<area href="nanoparticleGeneralInfo.do?dispatch=setup&page=0"
						shape="rect" coords="455,243,545,277" alt="rectangle" />
					<area href="nanoparticleGeneralInfo.do?dispatch=setup&page=0"
						shape="rect" coords="455,287,545,321" alt="rectangle" />
					<area href="nanoparticleGeneralInfo.do?dispatch=setup&page=0"
						shape="rect" coords="455,332,545,368" alt="rectangle" />
					<area href="nanoparticleGeneralInfo.do?dispatch=setup&page=0"
						shape="rect" coords="455,381,545,417" alt="rectangle" />
				</c:if>
			</map>
			<img align="middle" src="images/functions_flowchart.png"
				alt="caNanoLab flowchart" usemap="#funcMap" border="0">
			<br>
			<br>
		</td>
	</tr>
</table>

