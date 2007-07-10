<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html:form action="/${reportActionName}">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					<br>
					Report File
				</h3>
			</td>
			<td align="right" width="15%">
	<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=nano_report_help')" class="helpText">Help</a>
				<logic:equal name="reportActionName" value="updateReport">
				&nbsp;&nbsp;<a href="reportResults.do" class="helpText">Back</a>
				</logic:equal>
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
								<bean:write name="publishReportForm" property="file.type" />
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>File Name</strong>
							</td>
							<td class="rightLabel" colspan="3">
								<a href="searchReport.do?dispatch=download&amp;fileId=${publishReportForm.map.file.id}"> <bean:write name="publishReportForm" property="file.displayName" /></a>
							</td>
						</tr>
						<c:choose>
							<c:when test="${canUserSubmit eq 'true'}">
								<tr>
									<td class="leftLabel">
										<strong>File Title*</strong>
									</td>
									<td class="rightLabel"">
										<html:text property="file.title" size="80" />
										<html:hidden property="file.id" />
										<html:hidden property="file.type" />
										<html:hidden property="file.uri" />
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>File Description</strong>
									</td>
									<td class="rightLabel"">
										<html:textarea property="file.description" rows="3" cols="80" />
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Comments</strong>
									</td>
									<td class="rightLabel"">
										<html:textarea property="file.comments" rows="3" cols="80" />
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Visibility</strong>
									</td>
									<td class="rightLabel">
										<html:select property="file.visibilityGroups" multiple="true" size="6">
											<html:options name="allVisibilityGroups" />
										</html:select>
										<br>
										<i>(${applicationOwner}_Researcher and ${applicationOwner}_PI are defaults if none of above is selected.)</i>
									</td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<td class="leftLabel">
										<strong>Title</strong>
									</td>
									<td class="rightLabel" colspan="3">
										<bean:write name="publishReportForm" property="file.title" />
										&nbsp;
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Description</strong>
									</td>
									<td class="rightLabel" colspan="3">
										<bean:write name="publishReportForm" property="file.description" />
										&nbsp;
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Comments</strong>
									</td>
									<td class="rightLabel" colspan="3">
										<bean:write name="publishReportForm" property="file.comments" />
										&nbsp;
									</td>
								</tr>
								<tr>
									<td class="leftLabel" valign="top">
										<strong>Visibility</strong>
									</td>
									<td class="rightLabel" colspan="3">
										<bean:write name="publishReportForm" property="file.visibilityStr" filter="false" />
										&nbsp;
									</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
				<br>
			</td>
		</tr>
	</table>
	<c:choose>
		<c:when test="${canUserSubmit eq 'true'}">
			<br>
			<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
				<tr>
					<td width="30%">
						<span class="formMessage"> </span>
						<br>
						<table width="498" height="32" border="0" align="right" cellpadding="4" cellspacing="0">
							<tr>
								<td width="490" height="32">
									<div align="right">
										<div align="right">
											<input type="reset" value="Reset">
											<input type="hidden" name="dispatch" value="update">
											<input type="hidden" name="page" value="1">
											<html:submit />
										</div>
									</div>
								</td>
							</tr>
						</table>
						<div align="right"></div>
					</td>
				</tr>
			</table>
		</c:when>
	</c:choose>
</html:form>
