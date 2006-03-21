<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<table height="100%" cellspacing="0" cellpadding="0" width="100%" summary="" border="0">
	<tr>
		<td height="20">
			<tiles:insert attribute="workflowActionMenu" />
		</td>
	</tr>
	<tr>
		<td height="80%" valign="top">
			<tiles:insert attribute="workflowContent" />
		</td>
	</tr>

</table>