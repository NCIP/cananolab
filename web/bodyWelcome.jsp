<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table>
	<tr>
		<jsp:include page="/bodyMessage.jsp" />
	</tr>
	<tr>
		<td class="welcomeTitle" height="20">
			WELCOME TO caNanoLab
		</td>
	</tr>
	<tr>
		<td align="right">
			<jsp:include page="/helpGlossary.jsp">
				<jsp:param name="topic" value="welcome_workflow" />
				<jsp:param name="glossaryTopic" value="glossary_help" />
			</jsp:include>

		</td>
	</tr>
	<tr>
		<td class="welcomeContent" valign="top">
			Welcome to the
			<strong>cancer Nanotechnology Laboratory (caNanoLab)</strong>
			portal. caNanoLab is a data sharing portal designed to facilitate
			information sharing in the biomedical nanotechnology research
			community to expedite and validate the use of nanotechnology in
			biomedicine. caNanoLab allows researchers to share information on
			nanoparticles including the composition of the particle, the
			functions (e.g. therapeutic, targeting, diagnostic imaging) of the
			particle, the characterizations of the particle from
			physico-chemical (e.g. size, molecular weight, surface) and in
			vitro (e.g. cytotoxicity, blood contact) nanoparticle assays, and
			the protocols of these characterization. The diagram
			below illustrates the caNanoLab functionality and workflow. Active
			links are provided on the diagram to allow a user to directly
			navigate to the appropriate function based on the authorization level
			of the user. Function navigation is also available through the menus
			above.
			<br>
			<br>
			<map name="funcMap" id="funcMap">
				<c:if test="${isAdmin eq 'true' }">
					<area href="admin.do?dispatch=sitePreference" shape="rect"
						coords="70,70,170,110" alt="rectangle" />
				</c:if>
				<area href="login.jsp" shape="rect" coords="75,130,170,170"
					alt="rectangle" />
				<area href="searchProtocol.do?dispatch=setup" shape="rect"
					coords="70,230,165,265" alt="rectangle" />
				<area href="searchSample.do?dispatch=setup" shape="rect"
					coords="70,275,165,315" alt="rectangle" />
				<area href="searchPublication.do?dispatch=setup" shape="rect"
					coords="70,325,165,365" alt="rectangle" />

				<c:if test="${canCreateProtocol eq 'true'}">
					<area href="submitProtocol.do?dispatch=setup&page=0" shape="rect"
						coords="265,60,360,95" alt="rectangle" />
				</c:if>
				<c:if test="${canCreateSample eq 'true'}">
					<area
						href="sample.do?dispatch=setupNew&page=0&location=${location}"
						shape="rect" coords="265,100,360,160" alt="rectangle" />
				</c:if>
				<c:if test="${canCreatePublication eq 'true'}">
					<area href="managePublication.do" shape="rect"
						coords="360,445,445,480" alt="rectangle" />
				</c:if>
				<c:if test="${canCreateSample eq 'true'}">
					<area href="sample.do?dispatch=setupNew&page=0"
						shape="rect" coords="355,175,450,220" alt="rectangle" />
					<area href="sample.do?dispatch=setupNew&page=0"
						shape="rect" coords="355,280,450,315" alt="rectangle" />
					<area href="sample.do?dispatch=setupNew&page=0"
						shape="rect" coords="360,370,455,420" alt="rectangle" />
					<area href="sample.do?dispatch=setupNew&page=0"
						shape="rect" coords="475,155,620,420" alt="rectangle" />
				</c:if>
			</map>
			<img align="middle" src="images/Home_Page_Workflow.gif"
				alt="caNanoLab flowchart" usemap="#funcMap" border="0">
			<br>
			<br>
		</td>
	</tr>
</table>

