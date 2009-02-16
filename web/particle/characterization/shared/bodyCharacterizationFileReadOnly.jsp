<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html:form action="/characterization">
	<table class="topBorderOnly" cellspacing="0" cellpadding="3"
		width="100%" align="center" summary="" border="0">
		<tbody>
			<tr class="topBorder">
				<td class="formTitle" colspan="3">
					<div align="justify">
						${fn:toUpperCase(param.location)} ${particleName} Composition File Information
					</div>
				</td>
			</tr>
			<c:choose>
				<c:when
					test="${compositionFileForm.map.compFile.hidden eq 'false' }">
					<tr>
						<c:choose>
							<c:when
								test="${compositionFileForm.map.compFile.domainFile.uriExternal eq 'true' }">
								<td class="leftLabel">
									<strong>File URL</strong>
								</td>
								<td class="rightLabel" colspan="2">
									${compositionFileForm.map.compFile.externalUrl}&nbsp;
								</td>
							</c:when>
							<c:otherwise>
								<td class="leftLabel">
									<strong>Uploaded File</strong>
								</td>
								<td class="rightLabel" colspan="2">
									<c:choose>
										<c:when
											test="${compositionFileForm.map.compFile.image eq 'true'}">
						 				${compositionFileForm.map.compFile.domainFile.uri}<br>
											<br>
											<a href="#"
												onclick="popImage(event, 'compositionFile.do?dispatch=download&amp;fileId=${compositionFileForm.map.compFile.domainFile.id}&amp;location=${location}',
														${compositionFileForm.map.compFile.domainFile.id}, 100, 100)"><img
													src="compositionFile.do?dispatch=download&amp;fileId=${compositionFileForm.map.compFile.domainFile.id}&amp;location=${location}"
													border="0" width="150"> </a>
										</c:when>
										<c:otherwise>
										<a
												href="compositionFile.do?dispatch=download&amp;fileId=${compositionFileForm.map.compFile.domainFile.id}&amp;location=${location}"
												target="${compositionFileForm.map.file.urlTarget}">
												${compositionFileForm.map.compFile.domainFile.uri}</a>
										</c:otherwise>
									</c:choose>
								</td>
							</c:otherwise>
						</c:choose>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>File Type</strong>
						</td>
						<td class="rightLabel" colspan="2">
							${compositionFileForm.map.compFile.domainFile.type }&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>File Title</strong>
						</td>
						<td class="rightLabel" colspan="2">
							${compositionFileForm.map.compFile.domainFile.title }&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Keywords <em>(one word per line)</em> </strong>
						</td>
						<td class="rightLabel" colspan="2">
							${compositionFileForm.map.compFile.keywordsStr }&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Visibility</strong>
						</td>
						<td class="rightLabel" colspan="2">
							${compositionFileForm.map.compFile.visibilityStr }&nbsp;
						</td>
					</tr>
				</c:when>
				<c:otherwise>
					<tr>
						<td class="leftLabel">
							The file is private.
						</td>
						<td class="rightLabel" colspan="2">
							&nbsp;
						</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
</html:form>
