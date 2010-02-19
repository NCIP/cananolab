<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table summary="" cellpadding="0" cellspacing="0" border="0"
	height="100%">
	<tr>
		<td valign="top" width="600">
			<img src="images/bannerhome.jpg" width="600">
			<table summary="" cellpadding="0" cellspacing="0" border="0"
				width="100%">
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
						physico-chemical and in vitro assays and the sharing of these
						characterizations and associated nanotechnology protocols in a
						secure fashion.
						<br />
						<br />
						caNanoLab is installed at a variety of sites performing
						nanotechnology characterizations and/or literature curation. To
						view publically available data from a specific caNanoLab site,
						select the Site of interest when performing a search. Multiple
						sites or All sites can be selected for querying across multiple
						caNanoLab sites.
					</td>
				</tr>
			</table>
			<br />
			<table summary="" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="welcomeTitle" height="20">
						Browse caNanoLab
					</td>
				</tr>
				<tr>
					<td><br/>
						<jsp:include page="/bodyLoginBrowseGrid.jsp" />
					</td>
				</tr>
			</table>
		</td>
		<td valign="top">
			<!-- right sidebar begins -->
			<jsp:include page="/bodyLoginRightSideBar.jsp" />
		</td>
	</tr>
</table>


