<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html:form action="/login">
<table summary="" cellpadding="0" cellspacing="0" border="0"  height="100%">
	<tr>
		<td valign="top" height="100%">
			<img src="images/bannerhome.jpg">
			<table summary="" cellpadding="0" cellspacing="0" border="0"
				width="600">
				<tr>
					<td class="welcomeTitle" height="20">
						Welcome to caNanoLab
					</td>
				</tr>
				<tr>
					<td class="welcomeContent" valign="top">
						Welcome to the cancer Nanotechnology Laboratory (caNanoLab)
						portal. caNanoLab is a data sharing portal designed to facilitate
						information sharing in the biomedical nanotechnology research
						community to expedite and validate the use of nanotechnology in
						biomedicine. caNanoLab provides support for the annotation of
						nanomaterials with characterizations resulting from
						physico-chemical and
						<i>in vitro</i> assays and the sharing of these characterizations
						and associated nanotechnology protocols in a secure fashion.
					</td>
				</tr>
			</table>
			<br/>
			<table summary="" cellpadding="0" cellspacing="0" border="0"
				width="600">
				<tr>
					<td><jsp:include page="/bodyMessage.jsp" /></td>
				</tr>
				<tr>
					<td class="welcomeTitle" height="20">
						Browse caNanoLab
					</td>
				</tr>
				<tr>
					<td>
						<br />
						<%@include file="bodyHomeBrowseGrid.jsp"%>
					</td>
				</tr>
			</table>
		</td>
		<td valign="top" height="100%">
			<!-- right sidebar begins -->
			<%@include file="bodyHomeRightSideBar.jsp"%>
		</td>
	</tr>
</table>
</html:form>


