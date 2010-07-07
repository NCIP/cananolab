<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table id="summarySection4" width="100%" align="center"
	style="display: block" class="summaryViewNoGrid">
	<tr>
		<th align="left">
			composition file &nbsp;&nbsp;&nbsp;
			<a href="compositionFile.do?dispatch=setupNew&sampleId=${sampleId}"
				class="addlink"><img align="middle" src="images/btn_add.gif"
					border="0" /></a>
		</th>
	</tr>
	<tr>
		<td>
			<c:choose>
				<c:when test="${!empty compositionForm.map.comp.fileTypes}">
					<c:forEach var="fileType"
						items="${compositionForm.map.comp.fileTypes}">
						<a href="#${fileType}"></a>
						<table width="99%" align="center" class="summaryViewNoGrid"
							bgcolor="#dbdbdb">
							<tr>
								<th align="left">
									${fileType}
								</th>
							</tr>
							<tr>
								<td>
									<c:forEach var="file"
										items="${compositionForm.map.comp.type2Files[fileType]}"
										varStatus="fileInd">
										<table class="summaryViewNoGrid" width="99%" align="center"
											bgcolor="#F5F5f5">
											<tr>
												<td width="95%"></td>
												<td align="right"></td>
												<td valign="top" align="right">
													<a
														href="compositionFile.do?dispatch=setupUpdate&sampleId=${sampleId}&dataId=${file.domainFile.id}">Edit</a>
												</td>
											</tr>
											<tr>
												<td class="cellLabel" width="20%">
													Title and Download Link
												</td>
												<td>
													<c:choose>
														<c:when test="${file.domainFile.uriExternal}">
															<a
																href="composition.do?dispatch=download&amp;fileId=${file.domainFile.id}">
																${file.domainFile.uri}</a>
														</c:when>
														<c:otherwise>
															<c:choose>
																<c:when test="${file.image eq 'true'}">
						 				${file.domainFile.title}
										<br>
																	<a href="#"
																		onclick="popImage(event, 'composition.do?dispatch=download&amp;fileId=${file.domainFile.id}', ${file.domainFile.id})"><img
																			src="composition.do?dispatch=download&amp;fileId=${file.domainFile.id}"
																			border="0" width="150"> </a>
																</c:when>
																<c:otherwise>
																	<a
																		href="composition.do?dispatch=download&amp;fileId=${file.domainFile.id}">
																		${file.domainFile.title}</a>
																</c:otherwise>
															</c:choose>
														</c:otherwise>
													</c:choose>
												</td>
												<td></td>
											</tr>
											<tr>
												<td class="cellLabel">
													Keywords
												</td>
												<td>
													<c:choose>
														<c:when test="${!empty fn:trim(file.keywordsStr)}">
															<c:out
																value="${fn:replace(file.keywordsStr, cr, '<br>')}"
																escapeXml="false" />
														</c:when>
														<c:otherwise>N/A
												</c:otherwise>
													</c:choose>
												</td>
												<td></td>
											</tr>
											<tr>
												<td class="cellLabel">
													Description
												</td>
												<td>
													<c:choose>
														<c:when
															test="${!empty fn:trim(file.domainFile.description)}">
															<c:out
																value="${fn:replace(file.domainFile.description, cr, '<br>')}"
																escapeXml="false" />
														</c:when>
														<c:otherwise>N/A
												</c:otherwise>
													</c:choose>
												</td>
												<td></td>
											</tr>
										</table>
										<c:if
											test="${fileInd.count<fn:length(compositionForm.map.comp.type2Files[fileType])}">
											<br />
										</c:if>
									</c:forEach>
								</td>
							</tr>
							<tr>
								<th valign="top" align="left" height="6">
								</th>
							</tr>
						</table>
						<br />
					</c:forEach>
				</c:when>
				<c:otherwise>
					<div class="indented4">
						N/A
					</div>
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
<div id="summarySeparator4">
	<br>
</div>



