<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/cananolab.tld" prefix="cananolab" %>
<html:form action="/login" styleId="loginForm">
	<table summary="layout" cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td valign="top" class="mainContentHomePage">
			    <img src="images/bannerhome.jpg" border="0" width="600px" alt="caNanoLab banner image">			    
				<div class="welcomeTitle">Welcome to caNanoLab</div> <br>
				<div class="welcomeContent">
					Welcome to the cancer Nanotechnology Laboratory (caNanoLab) portal.
					caNanoLab is a data sharing portal designed to facilitate
					information sharing in the biomedical nanotechnology research
					community to expedite and validate the use of nanotechnology in
					biomedicine. caNanoLab provides support for the annotation of
					nanomaterials with characterizations resulting from
					physico-chemical and <i>in vitro</i> assays and the sharing of
					these characterizations and associated nanotechnology protocols in
					a secure fashion.
				</div> <br> <jsp:include page="/bodyMessage.jsp" />
				<div class="welcomeTitle">Browse caNanoLab</div> <br> 
				<%@include file="bodyHomeBrowseGrid.jsp"%>
			</td>
			<td valign="top" class="rightSideBar">			
				<!-- right sidebar begins --> <%@include file="bodyHomeRightSideBar.jsp"%>
			</td>
		</tr>
	</table>
</html:form>


