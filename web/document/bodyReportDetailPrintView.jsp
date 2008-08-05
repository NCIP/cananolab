<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="css/caLab.css">
		<script type="text/javascript" src="javascript/script.js"></script>
	</head>
	<body onload="window.print();self.close()">
		<table width="100%" align="center">
			<tr>
				<td colspan="">
					<h4>
						<br>
						Report
					</h4>
				</td>				
			</tr>			
			<tr>
				<td colspan="2">
					<table width="100%" border="0" align="center" cellpadding="3"
						cellspacing="0" class="topBorderOnly" summary="">
						<tr>
							<td class="formTitle" align="center" colspan="2">
								${fn:toUpperCase(param.location)} ${particleName}
							</td>
						</tr>
					<tr>
					<th class="leftLabel" valign="top">
						Title
					</th>
					<td class="rightLabel">
						<bean:write name="submitReportForm"
							property="file.domainFile.title" />
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Report URL
					</th>
					<td class="rightLabel">
						${submitReportForm.map.file.domainFile.uri}
					</td>
				</tr>
				<tr>
					<th class="leftLabel" valign="top">
						Description
					</th>
					<td class="rightLabel">
						<bean:write name="submitReportForm"
							property="file.domainFile.description" />&nbsp;
					</td>
				</tr>

					</table>
				</td>
			</tr>
		</table>
</body>
</html>
