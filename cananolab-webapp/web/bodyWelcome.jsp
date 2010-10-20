<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="WELCOME TO caNanoLab" />
	<jsp:param name="topic" value="welcome_workflow" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<table>
	<tr>
		<jsp:include page="/bodyMessage.jsp" />
	</tr>
	<tr>
		<td class="welcomeContent" valign="top">
			Welcome to the
			<strong>cancer Nanotechnology Laboratory (caNanoLab)</strong> portal.
			caNanoLab is a data sharing portal designed to facilitate information
			sharing in the biomedical nanotechnology research community to
			expedite and validate the use of nanotechnology in biomedicine.
			caNanoLab allows researchers to share information on nanomaterials
			including the composition of the nanomaterial, the functions (e.g.
			therapeutic, targeting, diagnostic imaging) of the nanomaterial, the
			characterizations of the nanomaterial from physico-chemical (e.g.
			size, molecular weight, surface) and
			<i>in vitro</i> (e.g. cytotoxicity, blood contact) nanomaterial
			assays, and the protocols of these assays. The diagram below
			illustrates the caNanoLab functionality and workflow. Active links
			are provided on the diagram to allow a user to directly navigate to
			the appropriate function based on the authorization level of the
			user. Function navigation is also available through the menus above.
			<br>
			<br>
			<map name="funcMap" id="funcMap">
				<%-- Create User Account --%>
				<c:if test="${!empty user && user.admin }">
					<area href="javascript:openHelpWindow('/upt')" shape="rect"
						coords="60,55,156,93" alt="rectangle" />
				</c:if>

				<%-- Login --%>
				<area href="welcome.do" shape="rect" coords="60,105,156,143"
					alt="rectangle" />

				<%-- Site Preference --%>
				<c:if test="${!empty user && user.admin }">
					<area href="admin.do?dispatch=setupNew" shape="rect"
						coords="60,156,156,195" alt="rectangle" />
				</c:if>

				<%-- Search Protocols --%>
				<area href="searchProtocol.do?dispatch=setup" shape="rect"
					coords="60,259,156,297" alt="rectangle" />

				<%-- Search Sample --%>
				<area href="searchSample.do?dispatch=setup" shape="rect"
					coords="60,311,156,363" alt="rectangle" />

				<%-- Search Publication --%>
				<area href="searchPublication.do?dispatch=setup" shape="rect"
					coords="60,375,156,413" alt="rectangle" />

				<c:if test="${!empty user}">
					<%-- Submit Protocols --%>
					<area href="protocol.do?dispatch=setupNew&page=0" shape="rect"
						coords="272,36,368,74" alt="rectangle" />

					<%-- Submit Samples --%>
					<area href="sample.do?dispatch=setupNew&page=0" shape="rect"
						coords="272,88,368,141" alt="rectangle" />

					<%-- Submit Publication --%>
					<area href="publication.do?dispatch=setupNew&page=0" shape="rect"
						coords="332,412,428,450" alt="rectangle" />

					<%-- Submit Samples (Others) --%>
					<area href="sample.do?dispatch=setupNew&page=0" shape="rect"
						coords="332,152,428,189" alt="rectangle" />
					<area href="sample.do?dispatch=setupNew&page=0" shape="rect"
						coords="332,333,428,371" alt="rectangle" />
					<area href="sample.do?dispatch=setupNew&page=0" shape="rect"
						coords="459,245,554,305" alt="rectangle" />
					<area href="sample.do?dispatch=setupNew&page=0" shape="rect"
						coords="459,368,554,409" alt="rectangle" />
					<area href="sample.do?dispatch=setupNew&page=0" shape="rect"
						coords="459,449,554,490" alt="rectangle" />
				</c:if>
			</map>
			<img align="middle" src="images/Home_Page_Workflow.png"
				alt="caNanoLab flowchart" usemap="#funcMap" border="0">
			<br>
			<br>
		</td>
	</tr>
	<%--to be removed after 1.5.2 --%>
	<c:if
		test="${!empty user && (user.loginName eq 'pansu'||user.loginName eq 'lethai')}">
		<tr>
			<td class="sidebarContent">
				<a href="updateCreatedBy.do?dispatch=setupNew&page=0"> Update
					Created By </a>
				<br>
				Click to update the createdBy field for samples, protocols and
				publications.
			</td>
		</tr>
	</c:if>
</table>

