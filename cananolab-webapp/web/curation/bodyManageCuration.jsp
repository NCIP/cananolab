<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Manage Curation" />
	<jsp:param name="topic" value="manage_curation_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<jsp:include page="/bodyMessage.jsp?bundle=curation" />
<div class="welcomeContent">This is the manage curation section.
	In this section, curators can view a list of samples, publications, and
	protocols pending public review, select an item from the pending list,
	review the item, and make the item accessible to public. Curators can
	also generate, regenerate and delete data availability metrics in
	batch.
</div>
<br>
<table summary="" cellpadding="0" cellspacing="0" border="0" width="40%"
	height="100%" class="sidebarSection">
	<tr>
		<th align="left" height="20" class="sidebarTitle">CURATION LINKS
		</th>
	</tr>
	<tr>
		<td class="sidebarContent"><a
			href="reviewData.do?dispatch=setupNew&page=0">Review Data Pending
			Release to Public</a> <br> Review samples, publications and
			protocols submitted by researchers and assign them to be readable by
			Public when appropriate.</td>
	</tr>
	<tr>
		<td class="sidebarContent"><a
			href="generateBatchDataAvailability.do?dispatch=setupNew&page=0">Manage
				Batch Data Availability</a> <br> Manage Data Availability for
			submitted samples in a batch.</td>
	</tr>
</table>

