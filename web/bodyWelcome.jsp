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
			<jsp:include page="/webHelp/helpGlossary.jsp">
				<jsp:param name="topic" value="welcome_workflow" />
				<jsp:param name="glossaryTopic" value="glossary_help" />
			</jsp:include>	
			
		</td>
	</tr>
	<tr>
		<td class="welcomeContent" valign="top">
			Welcome to the cancer Nanotechnology Laboratory (caNanoLab) portal.
			caNanoLab is a data sharing portal designed to facilitate 
			information sharing in the biomedical nanotechnology research community 
			to expedite and validate the use of nanotechnology in biomedicine.  
			caNanoLab allows researchers to share information on nanoparticles 
			including the composition of the particle, the functions (e.g. therapeutic, 
			targeting, diagnostic imaging) of the particle, the characterizations of 
			the particle from physical (e.g. size, molecular weight) and in vitro 
			(e.g. cytotoxicity, immunotoxicity) nanoparticle assays, and the protocols 
			of these characterization. The diagram below illustrates
			the caNanoLab functionality and workflow. Active links are provided
			on the diagram to allow a user to directly navigate to the
			appropriate function based on the authorization level of the user.
			Function navigation is also available through the menus above.
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
					coords="75,235,170,270" alt="rectangle" />
				<area href="searchDocument.do?dispatch=setup" shape="rect"
					coords="75,280,170,320" alt="rectangle" />
				<area href="searchNanoparticle.do?dispatch=setup" shape="rect"
					coords="75,330,170,370" alt="rectangle" />
				
				<c:if test="${canCreateProtocol eq 'true'}">
					<area href="submitProtocol.do?dispatch=setup&page=0" shape="rect"
						coords="275,70,370,105" alt="rectangle" />
				</c:if>
				<c:if test="${canCreateSample eq 'true'}">
					<area href="submitNanoparticleSample.do?dispatch=setup&page=0&location=${location}" shape="rect"
						coords="275,110,370,160" alt="rectangle" />
				</c:if>
				<c:if test="${canCreateReport eq 'true'}">
					<area href="submitReport.do?dispatch=setup&page=0" shape="rect"
						coords="365,450,460,490" alt="rectangle" />
				</c:if>
				<c:if test="${canCreateNanoparticle eq 'true'}">
					<area href="submitNanoparticleSample.do?dispatch=setup&page=0"
						shape="rect" coords="365,190,460,225" alt="rectangle" />
					<area href="submitNanoparticleSample.do?dispatch=setup&page=0"
						shape="rect" coords="365,285,460,320" alt="rectangle" />
					<area href="submitNanoparticleSample.do?dispatch=setup&page=0"
						shape="rect" coords="370,370,465,410" alt="rectangle" />
					<area href="submitNanoparticleSample.do?dispatch=setup&page=0"
						shape="rect" coords="490,170,640,425" alt="rectangle" />
				</c:if>
			</map>
			<img align="middle" src="images/Home_Page_Workflow2.gif"
				alt="caNanoLab flowchart" usemap="#funcMap" border="0">
			<br>
			<br>
		</td>
	</tr>
</table>

