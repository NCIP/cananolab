<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<tiles:importAttribute scope="session"/>

<table height="100%" cellspacing="0" cellpadding="0" width="100%" summary="" border="0">
	<tr>
		<td height="20">
			<tiles:insert attribute="workflowActionMenu" />
		</td>
	</tr>
	<tr>
		<td valign="top">
			<tiles:insert attribute="workflowContent" />
		</td>
	</tr>

</table>