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
			<a
				href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=welcome_workflow')"
				class="helpText">Help</a>&nbsp;&nbsp;&nbsp;
				<a
				href="javascript:openHelpWindow('webHelp/wwhelp/wwhimpl/js/html/wwglossary.htm')"
				class="helpText">Glossary</a>&nbsp;
		</td>
	</tr>
	<tr>
		<td class="welcomeContent" valign="top">
			Welcome to the cancer Nanotechnology Laboratory (caNanoLab) portal.
			caNanoLab is a data sharing portal designed for laboratories
			performing nanoparticle assays. caNanoLab provides support for the
			annotation of nanoparticles with characterizations resulting from
			physical and in vitro nanoparticle assays and the sharing of these
			characterizations in a secure fashion. The diagram below illustrates
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
						coords="275,70,370,105" alt="rectangle" />
				</c:if>
				<c:if test="${canCreateSample eq 'true'}">
					<area href="submitNanoparticleSample.do?dispatch=setup&page=0" shape="rect"
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
			<img align="middle" src="images/Home_Page_Workflow.gif"
				alt="caNanoLab flowchart" usemap="#funcMap" border="0">
			<br>
			<br>
		</td>
	</tr>
</table>

