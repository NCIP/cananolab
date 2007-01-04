<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html:form action="/updateAssayFile">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					<br>
					Assay Result File
				</h3>
			</td>
			<td align="right" width="15%">
				<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caLAB_1.0_OH&amp;topic=load_characterization_file_help')" class="helpText">Help</a>&nbsp;&nbsp; <a href="javascript:history.go(-1)" class="helpText">back</a> 
			&nbsp;
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
								<strong>Assay Result File Name</strong>
							</td>
							<td class="rightLabel" colspan="3">
								<a href="${actionName}.do?dispatch=download&amp;fileId=<bean:write name="loadDerivedBioAssayDataForm" property="file.id"/>"><bean:write name="loadDerivedBioAssayDataForm" property="file.displayName" /></a>
							</td>
						</tr>
						<c:choose>
							<c:when test="${canUserSubmit eq 'true'}">
								<tr>
									<td class="leftLabel">
										<strong>Assay Result File Title*</strong>
									</td>
									<td class="rightLabel" colspan="3">
										<html:text property="file.title" size="80" />
										<html:hidden property="file.id" />
										<html:hidden property="file.path" />
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Assay Result File Description</strong>
									</td>
									<td class="rightLabel" colspan="3">
										<html:textarea property="file.description" rows="3" cols="80" />
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Keywords <em>(one per line)</em></strong>
									</td>
									<td class="rightLabel" colspan="3">
										<html:textarea property="file.keywordsStr" rows="3" />
									</td>
								</tr>
								<tr>
									<td class="leftLabel" valign="top">
										<strong>Visibility</strong>
									</td>
									<td class="rightLabel" colspan="3">
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
										<strong>Assay Result File Title</strong>
									</td>
									<td class="rightLabel" colspan="3">
										<bean:write name="loadDerivedBioAssayDataForm" property="file.title" />
										&nbsp;
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Assay Result File Description</strong>
									</td>
									<td class="rightLabel" colspan="3">
										<bean:write name="loadDerivedBioAssayDataForm" property="file.description" />
										&nbsp;
									</td>
								</tr>
								<tr>
									<td class="leftLabel" valign="top">						
										<strong>Keywords <em>(one per line)</em></strong>
									</td>
									<td class="rightLabel" colspan="3">
										<c:forEach var="keyword" items="${loadDerivedBioAssayDataForm.map.file.keywords}">
											<c:out value="${keyword}" />
											<br>
										</c:forEach>&nbsp;										
									</td>
								</tr>
								<tr>
									<td class="leftLabel" valign="top">
										<strong>Visibility</strong>
									</td>
									<td class="rightLabel" colspan="3">
										<bean:write name="loadDerivedBioAssayDataForm" property="file.visibilityStr" filter="false" />
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
											<input type="button" value="Cancel" onclick="javascript:history.go(-1);">
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
