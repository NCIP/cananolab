<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html:form action="/loadFile" enctype="multipart/form-data">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					<br>
					Load/Update Characterization File
				</h3>
			</td>
			<td align="right" width="15%">
				<a
					href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=load_characterization_file_help')"
					class="helpText">Help</a> &nbsp;&nbsp;
				<a href="javascript:history.go(-1)" class="helpText">back</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=submit" />
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify">
									File Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<html:radio property="fileSource" value="new" />
										<strong> Upload New File</strong>&nbsp;
										<c:choose>
											<c:when
												test="${!empty file.uri && empty file.id && loadDerivedBioAssayDataForm.map.fileSource=='new'}">
									    	${file.displayName} 
									    	<html:hidden property="file.name" />
												<html:hidden property="file.uri" />
											</c:when>
										</c:choose>
										<c:choose>
											<c:when
												test="${!empty file.id && loadDerivedBioAssayDataForm.map.fileSource=='new'}">
												<a
													href="${actionName}.do?dispatch=download&amp;fileId=${file.id}">${file.displayName}</a>
												<html:hidden property="file.id" />
												<html:hidden property="file.name" />
												<html:hidden property="file.uri" />
											</c:when>
										</c:choose>
										<table cellspacing="0" cellpadding="3" width="100%"
											align="center" summary="" border="0">
											<tr>
												<td class="borderlessLabel" width="50">
													&nbsp;
												</td>
												<td class="borderlessLabel" valign="top">
													<html:file property="uploadedFile" />
												</td>
											</tr>
										</table>
										<br>
										<html:radio property="fileSource" value="chooseExisting" />
										<strong> Choose File from Workflow</strong>&nbsp;
										<c:choose>
											<c:when
												test="${!empty file.uri && empty file.id && loadDerivedBioAssayDataForm.map.fileSource=='chooseExisting'}">
									    	${file.displayName}
									    	<html:hidden property="file.name" />
												<html:hidden property="file.uri" />
											</c:when>
										</c:choose>
										<c:choose>
											<c:when
												test="${!empty file.id && loadDerivedBioAssayDataForm.map.fileSource=='chooseExisting'}">
												<a
													href="${actionName}.do?dispatch=download&amp;fileId=${file.id}">${file.displayName}</a>
												<html:hidden property="file.id" />
												<html:hidden property="file.name" />
												<html:hidden property="file.uri" />
											</c:when>
										</c:choose>
										<table cellspacing="0" cellpadding="3" width="100%"
											align="center" summary="" border="0">
											<tr>
												<td class="borderlessLabel" width="50">
													&nbsp;
												</td>
												<td class="borderlessLabel">
													<html:select property="runFileId">
														<option value=""></option>
														<html:options collection="allRunFiles" property="id"
															labelProperty="displayName" />
													</html:select>
												</td>
											</tr>
										</table>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when
												test="${!empty file.uri && empty file.id && loadDerivedBioAssayDataForm.map.fileSource=='new'}">
									    	${file.displayName}
									    	<html:hidden property="file.name" />
												<html:hidden property="file.uri" />
											</c:when>
										</c:choose>
										<c:choose>
											<c:when
												test="${!empty file.id && loadDerivedBioAssayDataForm.map.fileSource=='new'}">
												<a
													href="${actionName}.do?dispatch=download&amp;fileId=${file.id}">${file.displayName}</a>
												<html:hidden property="file.id" />
												<html:hidden property="file.name" />
												<html:hidden property="file.uri" />
											</c:when>
										</c:choose>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>File Title*</strong>
							</td>
							<td class="rightLabel">
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<html:text property="file.title" size="60" />
									</c:when>
									<c:otherwise>
								${file.title}&nbsp;
								</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td class="leftLabel" valign="top">
								<strong>Keywords <em>(one per line)</em> </strong>
							</td>
							<td class="rightLabel">
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">
										<html:textarea property="file.keywordsStr" rows="3" />
									</c:when>
									<c:otherwise>
										<c:forEach var="keyword"
											items="${loadDerivedBioAssayDataForm.map.file.keywords}">
											<c:out value="${keyword}" />
											<br>
										</c:forEach>&nbsp;	
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td class="leftLabel" valign="top">
								<strong>Visibility</strong>
							</td>
							<td class="rightLabel">
								<c:choose>
									<c:when test="${canUserSubmit eq 'true'}">

										<html:select property="file.visibilityGroups" multiple="true"
											size="6">
											<html:options name="allVisibilityGroups" />
										</html:select>
										<br>
										<i>(${applicationOwner}_Researcher and
											${applicationOwner}_PI are defaults if none of above is
											selected.)</i>
									</c:when>
									<c:otherwise>
										<bean:write name="loadDerivedBioAssayDataForm"
											property="file.visibilityStr" filter="false" />
										&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</tbody>
				</table>
				<br>
				<c:choose>
					<c:when test="${canUserSubmit eq 'true'}">
						<table width="100%" border="0" align="center" cellpadding="3"
							cellspacing="0" class="topBorderOnly" summary="">
							<tr>
								<td width="30%">
									<span class="formMessage"> </span>
									<br>
									<table width="498" height="32" border="0" align="right"
										cellpadding="4" cellspacing="0">
										<tr>
											<td width="490" height="32">
												<div align="right">
													<div align="right">
														<input type="button" value="Cancel"
															onclick="javascript:history.go(-1);">
														<input type="reset" value="Reset">
														<input type="hidden" name="dispatch" value="submit">
														<input type="hidden" name="page" value="2">
														<html:hidden property="forwardPage" />
														<html:hidden property="fileNumber" />
														<html:hidden property="file.particleName" />
														<html:hidden property="file.characterizationName" />
														<html:submit value="Load" />
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
			</td>
		</tr>
	</table>
</html:form>
