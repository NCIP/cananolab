<%@ page import="java.util.HashMap"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<%//tmp code to be replaced 
			String type = request.getParameter("type");
			
			System.out.println("request parameter type = " + type);
			
			java.util.Map actions = new HashMap();
			if (type != null){
				if (type.equals("in")) {			
					actions.put("Use Aliquot", "initSession.do?forwardPage=useAliquot&runId=4");
					actions.put("Upload Files", "useAliquot.jsp");
					actions.put("Download All Files (Zipped)", "/useAliquot.jsp");
					actions.put("Download All Files (Unzipped)", "/useAliquot.jsp");
				}
				else if (type.equals("out")){
					actions.put("Upload Files", "/useAliquot.jsp");
					actions.put("Download All Files (Zipped)", "/useAliquot.jsp");
					actions.put("Download All Files (Unzipped)", "/useAliquot.jsp");
				}
				else if (type.equals("assay")) {
					actions.put("Create Run", "createRun.jsp");
				}
			}				
			else {
			actions.put("","");
			}
			pageContext.setAttribute("actions", actions);
%>

<table>
	<tr>
		<logic:iterate name="actions" id="action">
			<td class="formLabelGrey">
				<a href="<bean:write name="action" property="value"/>"><bean:write name="action" property="key"/></a>
				&nbsp; &nbsp;
			</td>
		</logic:iterate>
    </tr> 
</table>
