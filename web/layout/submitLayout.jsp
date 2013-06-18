<%--L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L--%>

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