<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<center>
	<table width="100%" align="center">
		<tr>
			<td>
				<br>
				<h3>
					Mask Files
				</h3>
			</td>
			<td align="right" width="10%">
				<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caLAB_1.0_OH&amp;topic=mask_files')" class="helpText">Help</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/workflow/bodyWorkflowInfo.jsp" />
				<logic:notPresent name="filesToMask">
					<font color="blue">There are no files to mask.</font>
				</logic:notPresent>
				<logic:present name="filesToMask">
					<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
						<tr>
							<td colspan="3" class="formTitle" align="center">
								Uploaded ${inout} Files
							</td>
						<tr>
						<tr>
							<td class="formTitle">
								File Name
							</td>
							<td class="formTitle">
								Uploaded Date
							</td>
							<td class="formTitle" align="right">
								Mask Action
							</td>
						</tr>
						<c:set var="rowNum" value="-1" />
						<logic:iterate id="file" name="filesToMask">
							<c:set var="rowNum" value="${rowNum+1}" />
							<c:choose>
								<c:when test="${rowNum % 2 == 0}">
									<c:set var="style" value="formFieldGrey" />
									<c:set var="style0" value="leftBorderedFormFieldGrey" />
								</c:when>
								<c:otherwise>
									<c:set var="style" value="formFieldWhite" />
									<c:set var="style0" value="leftBorderedFormFieldWhite" />
								</c:otherwise>
							</c:choose>
							<tr>
								<td class="${style0}">
									<bean:write name="file" property="shortFilename" />
								</td>
								<td class="${style}">
									<bean:write name="file" property="createdDate" />
								</td>
								<c:url var="maskFileURL" value="/maskFile.do">
									<c:param name="dispatch" value="setup" />									
									<c:param name="itemId" value="${file.id}" />
									<c:param name="itemName" value="${file.filename}" />
								</c:url>
								<td align="right" class="${style}">
									<b><a href="${maskFileURL}">Mask </a> </b>
								</td>
							</tr>
						</logic:iterate>
					</table>
				</logic:present>
			</td>
		</tr>
	</table>