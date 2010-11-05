<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table id="summarySection${index}" width="100%" align="center"
	style="display: block" class="summaryViewNoGrid">
	<tr>
		<th align="left">
			<span class="summaryViewHeading">composition file</span>
		</th>
	</tr>
	<tr>
		<td>
			<c:forEach var="fileType"
				items="${compositionForm.map.comp.fileTypes}">
				<a href="#${fileType}"></a>
				<table width="99%" align="center" class="summaryViewNoGrid"
					bgcolor="#dbdbdb">
					<tr>
						<th align="left">
							<c:out value="${fileType}" />
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
										<td class="cellLabel" width="20%">
											Title and Download Link
										</td>
										<td>
											<c:choose>
												<c:when test="${file.domainFile.uriExternal}">
													<a
														href="composition.do?dispatch=download&amp;fileId=${file.domainFile.id}">
														<c:out value="${file.domainFile.uri}" escapeXml="false" />
													</a>
												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when test="${file.image eq 'true'}">
															<c:out value="${file.domainFile.title}" />
															<br>
															<a href="#"
																onclick="popImage(event, 'composition.do?dispatch=download&amp;fileId=${file.domainFile.id}', ${file.domainFile.id})"><img
																	src="composition.do?dispatch=download&amp;fileId=${file.domainFile.id}"
																	border="0" width="150"> </a>
														</c:when>
														<c:otherwise>
															<a
																href="composition.do?dispatch=download&amp;fileId=${file.domainFile.id}">
																<c:out value="${file.domainFile.title}"
																	escapeXml="false" /> </a>
														</c:otherwise>
													</c:choose>
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<c:if test="${!empty fn:trim(file.keywordsStr)}">
										<tr>
											<td class="cellLabel" width="10%">
												Keywords
											</td>
											<td>
												<c:out value="${fn:replace(file.keywordsStr, cr, '<br>')}"
													escapeXml="false" />
											</td>
										</tr>
									</c:if>
									<c:if test="${!empty fn:trim(file.domainFile.description)}">
										<tr>
											<td class="cellLabel" width="10%">
												Description
											</td>
											<td>
												<c:out value="${file.description}" escapeXml="false" />
											</td>
										</tr>
									</c:if>
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
		</td>
	</tr>
</table>
<div id="summarySeparator${index}">
	<br>
</div>