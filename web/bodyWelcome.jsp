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
			the caNanoLab functionality:
			<br>
			<br>
			<map name="funcMap" id="funcMap">
				<c:if test="${isAdmin eq 'true' }">
					<area href="javascript:openHelpWindow('/upt')" shape="rect"
						coords="100,108,218,148" alt="rectangle" />
				</c:if>
				<area href="login.jsp" shape="rect" coords="100,188,218,222"
					alt="rectangle" />
				<area href="searchProtocol.do?dispatch=setup" shape="rect"
					coords="100,315,218,355" alt="rectangle" />
				<area href="searchReport.do?dispatch=setup" shape="rect"
					coords="100,370,218,415" alt="rectangle" />
				<area href="searchNanoparticle.do?dispatch=setup" shape="rect"
					coords="100,425,218,470" alt="rectangle" />
				<c:if test="${canCreateSample eq 'true'}">
					<area href="searchSample.do?dispatch=setup" shape="rect"
						coords="100,480,218,525" alt="rectangle" />
					<area href="searchAliquot.do?dispatch=setup" shape="rect"
						coords="100,535,218,575" alt="rectangle" />
				</c:if>
				<c:if test="${canCreateProtocol eq 'true'}">
					<area href="submitProtocol.do?dispatch=setup&page=0" shape="rect"
						coords="358,86,442,128" alt="rectangle" />
				</c:if>
				<c:if test="${canCreateSample eq 'true'}">
					<area href="createSample.do?dispatch=setup&page=0" shape="rect"
						coords="358,162,442,200" alt="rectangle" />
					<area href="createAliquot.do?dispatch=setup&page=0" shape="rect"
						coords="491,162,576,200" alt="rectangle" />
				</c:if>
				<c:if test="${canCreateReport eq 'true'}">
					<area href="submitReport.do?dispatch=setup&page=0" shape="rect"
						coords="440,227,530,267" alt="rectangle" />
				</c:if>
				<c:if test="${canCreateNanoparticle eq 'true'}">
					<area href="nanoparticleGeneralInfo.do?dispatch=setup&page=0" shape="rect"
						coords="440,411,530,459" alt="rectangle" />
				</c:if>
			</map>
			<img align="middle" src="images/functions_flowchart.gif"
				alt="caNanoLab flowchart" usemap="#funcMap" border="0">
			<br>
			<br>
		</td>
	</tr>
</table>

