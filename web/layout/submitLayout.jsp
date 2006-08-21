<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<tiles:importAttribute scope="session"/>

<table cellspacing="0" cellpadding="0" width="100%" summary="" border="0">
	<tr>
		<td valign="top">
			<tiles:insert attribute="submitActionMenu" />
		</td>
	</tr>
	<tr>
		<td valign="top">
			<tiles:insert attribute="submitContent" />
		</td>
	</tr>

</table>