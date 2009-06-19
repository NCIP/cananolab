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
			nanoparticles including the composition of the particle,the
			functions (e.g. therapeutic,targeting,diagnostic imaging) of the
			particle,the characterizations of the particle from
			physico-chemical (e.g. size,molecular weight,surface) and in
			vitro (e.g. cytotoxicity,blood contact) nanoparticle assays,and
			the protocols of these characterization. The diagram
			below illustrates the caNanoLab functionality and workflow. Active
			links are provided on the diagram to allow a user to directly
			navigate to the appropriate function based on the authorization level
			of the user. Function navigation is also available through the menus
			above.
			<br>
			<br>
			<map name="funcMap" id="funcMap">
				<%-- Create User Account --%>
				<c:if test="${!empty user && user.admin }">
					<area href="javascript:openHelpWindow('/upt')" shape="rect"
						coords="80,73,208,121" alt="rectangle" />
				</c:if>

				<%-- Login --%>
				<area href="login.jsp" shape="rect" coords="80,140,208,190"
					alt="rectangle" />

				<%-- Site Preference --%>
				<c:if test="${!empty user && user.admin }">
					<area href="admin.do?dispatch=sitePreference" shape="rect"
						coords="80,207,208,258" alt="rectangle" />
				</c:if>
				
				<%-- Search Protocols --%>
				<area href="searchProtocol.do?dispatch=setup" shape="rect"
					coords="80,343,208,394" alt="rectangle" />
					
				<%-- Search Sample --%>
				<area href="searchSample.do?dispatch=setup" shape="rect"
					coords="80,411,208,482" alt="rectangle" />
					
				<%-- Search Publication --%>
				<area href="searchPublication.do?dispatch=setup" shape="rect"
					coords="80,498,208,550" alt="rectangle" />
					
				<c:if test="${!empty user && user.curator}">
					<%-- Submit Protocols --%>
					<area href="submitProtocol.do?dispatch=setup&page=0" shape="rect"
						coords="360,47,490,100" alt="rectangle" />
						
					<%-- Submit Samples --%>
					<area href="sample.do?dispatch=setupNew&page=0"
						shape="rect" coords="360,115,490,186" alt="rectangle" />
						
					<%-- Submit Publication --%>
					<area href="publication.do?dispatch=setupNew&page=0" shape="rect"
						coords="440,546,570,597" alt="rectangle" />
						
					<%-- Submit Samples (Others) --%>
					<area href="sample.do?dispatch=setupNew&page=0"
						shape="rect" coords="440,201,570,253" alt="rectangle" />
					<area href="sample.do?dispatch=setupNew&page=0"
						shape="rect" coords="440,441,570,493" alt="rectangle" />
					<area href="sample.do?dispatch=setupNew&page=0"
						shape="rect" coords="440,546,570,597" alt="rectangle" />
					<area href="sample.do?dispatch=setupNew&page=0"
						shape="rect" coords="609,323,735,403" alt="rectangle" />
					<area href="sample.do?dispatch=setupNew&page=0"
						shape="rect" coords="609,487,735,540" alt="rectangle" />
					<area href="sample.do?dispatch=setupNew&page=0"
						shape="rect" coords="609,597,735,650" alt="rectangle" />
				</c:if>
			</map>
			<img align="middle" src="images/Home_Page_Workflow.png"
				alt="caNanoLab flowchart" usemap="#funcMap" border="0">
			<br>
			<br>
		</td>
	</tr>
</table>

