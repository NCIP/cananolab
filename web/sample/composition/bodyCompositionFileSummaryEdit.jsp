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
					border="0" /></a> &nbsp;&nbsp;&nbsp;
			<%--
			<c:if test="${!empty compositionForm.map.comp.files}">
				<a href="/compositionFile.do?dispatch=delete&sampleId=${sampleId}"
					class="addlink"><img align="middle" src="images/btn_delete.gif"
						border="0" /> </a>
			</c:if>
			--%>
		</th>
	</tr>
	<tr>
		<td>
			<c:choose>
				<c:when test="${!empty compositionForm.map.comp.files}">
					<logic:iterate name="compositionForm" property="comp.files"
						id="file" indexId="ind">
						<c:set var="fileType" value="${file.domainFile.type}" />
						<c:if test="${!empty fileType}">
							<table class="summaryViewNoGrid" width="99%" align="center">
								<tr>
									<th valign="top" align="left" colspan="2" width="90%">
										${fileType}
									</th>
									<th valign="top" align="right">
										<a
											href="compositionFile.do?dispatch=setupUpdate&sampleId=${sampleId}&dataId=${file.domainFile.id}">Edit</a>
									</th>
								</tr>
								<tr>
									<td class="cellLabel" width="10%">
										Title and Download Link
									</td>
									<td colspan="2">
										<c:choose>
											<c:when test="${file.domainFile.uriExternal}">
												<a
													href="composition.do?dispatch=download&amp;fileId=${file.domainFile.id}&amp;location=${location}">
													${file.domainFile.uri}</a>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${file.image eq 'true'}">
						 				${file.domainFile.title}
										<br>
														<a href="#"
															onclick="popImage(event, 'composition.do?dispatch=download&amp;fileId=${file.domainFile.id}&amp;location=${location}', ${file.domainFile.id})"><img
																src="composition.do?dispatch=download&amp;fileId=${file.domainFile.id}&amp;location=${location}"
																border="0" width="150"> </a>
													</c:when>
													<c:otherwise>
														<a
															href="composition.do?dispatch=download&amp;fileId=${file.domainFile.id}&amp;location=${location}">
															${file.domainFile.title}</a>
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
								<tr>
									<td class="cellLabel" width="10%">
										Keywords
									</td>
									<td colspan="2">
										<c:choose>
											<c:when test="${!empty fn:trim(file.keywordsStr)}">
												<c:out value="${fn:replace(file.keywordsStr, cr, '<br>')}"
													escapeXml="false" />
											</c:when>
											<c:otherwise>N/A
												</c:otherwise>
										</c:choose>
									</td>
								</tr>
								<tr>
									<td class="cellLabel" width="10%">
										Description
									</td>
									<td colspan="2">
										<c:choose>
											<c:when test="${!empty fn:trim(file.domainFile.description)}">
												<c:out
													value="${fn:replace(file.domainFile.description, cr, '<br>')}"
													escapeXml="false" />
											</c:when>
											<c:otherwise>N/A
												</c:otherwise>
										</c:choose>
									</td>
								</tr>
							</table>
						</c:if>
						<br/>
					</logic:iterate>
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



