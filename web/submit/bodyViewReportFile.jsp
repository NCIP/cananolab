<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:choose>
	<c:when test="${param.type eq 'NCL Report'}">
		<c:forEach var="aReport" items="${charTypeReports}" varStatus="count">
			<logic:equal parameter="fileInd" value="${count.index}">
				<bean:define id="theFile" name="aReport" />
			</logic:equal>
		</c:forEach>
	</c:when>
	<c:otherwise>
		<c:forEach var="aReport" items="${charTypeAssociatedFiles}" varStatus="count">
			<logic:equal parameter="fileInd" value="${count.index}">
				<bean:define id="theFile" name="aReport" />
			</logic:equal>
		</c:forEach>
	</c:otherwise>
</c:choose>

<table width="100%" align="center">
	<tr>
		<td>
			<h3>
				<br>
				Report File
			</h3>
		</td>
		<td align="right" width="15%">
			<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=create_nanoparticle')" class="helpText">Help</a>&nbsp;&nbsp; <a href="javascript:history.go(-1)" class="helpText">back</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyMessage.jsp?bundle=submit" />
			<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
				<tbody>
					<tr class="topBorder">
						<td class="formTitle" colspan="4">
							<div align="justify">
								File Information
							</div>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Report File Type</strong>
						</td>
						<td class="rightLabel" colspan="3">
							${param.type}&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>File Name</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<a href="searchReport.do?dispatch=download&amp;fileId=${theFile.id}"><bean:write name="theFile" property="displayName" /></a>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>File Title</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<bean:write name="theFile" property="title" />
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>File Description</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<bean:write name="theFile" property="description" />
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>File Comments</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<bean:write name="theFile" property="comments" />
							&nbsp;
						</td>
					</tr>					
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Visibility</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<bean:write name="theFile" property="visibilityStr" filter="false"/>
							&nbsp;
						</td>
					</tr>
				</tbody>
			</table>
			<br>
		</td>
	</tr>
</table>
