<%@ page import="java.util.ArrayList"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<%//tmp code to be replaced 
			java.util.List actions = new ArrayList();
			actions.add("Use Aliquot");
			actions.add("Upload Files");
			actions.add("Download All Files (Zipped)");
			actions.add("Download All Files (Unzipped)");
			pageContext.setAttribute("actions", actions);
%>

<table>
	<tr>
		<logic:iterate name="actions" id="action">
			<td class="mainMenuItemOver">
				<bean:write name="action" />
				&nbsp; &nbsp;
			</td>
		</logic:iterate>
    </tr>
</table>
