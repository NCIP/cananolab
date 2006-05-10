<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<br>
<center>
	
	<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
			<td align="right" colspan="2">
				<c:choose>
       				 <c:when test='${param.menuType == "in"}'>
 						<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=in_folder')" class="helpText">Help</a>
			         </c:when>
			         <c:when test='${param.menuType == "out"}'>
  						<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=out_folder')" class="helpText">Help</a> 
			         </c:when>
			         <c:when test='${param.menuType == "upload"}'>
   						<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=file_upload')" class="helpText">Help</a>
			         </c:when>
			    </c:choose><br>
			</td>
		</tr>
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td class="formTitle" colspan="2" align="center">
				General Information for the workflow
			</td>
		</tr>
		<tr>
			<td class="leftBorderedFormFieldGrey">
				<b>Assay Type</b>
			</td>
			<td class="formFieldGrey">
				<c:out value="${param.assayType}" />
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftBorderedFormFieldWhite">
				<b>Assay Name</b>
			</td>
			<td class="formFieldWhite">
				<c:out value="${param.assayName}" />
				&nbsp;
			</td>
		</tr>
		<logic:present parameter="runName">
			<tr>
				<td class="leftBorderedFormFieldGrey">
					<b>Run Name</b>
				</td>
				<td class="formFieldGrey">
					<c:out value="${param.runName}" />
					&nbsp;
				</td>
			</tr>
		</logic:present>
		<tr>
			<td colspan="2">
				<br>
				<jsp:include page="/bodyMessage.jsp?bundle=workflow" />
			</td>
	</table>
	<br>
</center>
