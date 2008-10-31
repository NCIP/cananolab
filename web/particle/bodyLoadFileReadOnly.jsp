<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr>
			<td class="formSubTitleNoRight" colspan="2">
				File #${param.fileInd+1}
			</td>
		</tr>
		<c:choose>
			<c:when test="${param.fileHidden eq 'false' }">

				<tr>
					<c:choose>
						<c:when test="${param.uriExternal eq 'true' }">
							<td class="leftLabel">
								<strong>File URL</strong>
							</td>
							<td class="rightLabel" >
								<a
									href="${param.action}.do?dispatch=download&amp;fileId=${param.fileId}&amp;location=${location}">
									${param.fileUri}</a>
							</td>
						</c:when>
						<c:otherwise>
							<td class="leftLabel">
								<strong>Uploaded File</strong>
							</td>
							<td class="rightLabel">
								<c:choose>
									<c:when test="${param.fileImage eq 'true'}">
						 				${param.fileTitle}
										<br>
										<a href="#"
											onclick="popImage(event, '${param.action}.do?dispatch=download&amp;fileId=${param.fileId}&amp;location=${location}', ${param.fileId}, 100, 100)"><img
												src="${param.action}.do?dispatch=download&amp;fileId=${param.fileId}&amp;location=${location}"
												border="0" width="150"> </a>
									</c:when>
									<c:otherwise>
										<a
											href="${param.action}.do?dispatch=download&amp;fileId=${param.fileId}&amp;location=${location}">
											<%--			target="${submitReportForm.map.file.urlTarget}">--%>
											${param.fileUri}</a>
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
					<td class="rightLabel" >
						${param.fileType }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="leftLabel">
						<strong>File Title</strong>
					</td>
					<td class="rightLabel" >
						${param.fileTitle }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="leftLabel" valign="top">
						<strong>Keywords <em>(one word per line)</em> </strong>
					</td>
					<td class="rightLabel" >
						${param.fileKeywordsStr }&nbsp;
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<tr>
					<td class="leftLabel">
						The file is private.
					</td>
					<td class="rightLabel" >
						&nbsp;
					</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>

