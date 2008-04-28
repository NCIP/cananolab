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
		<tr>
			<c:choose>
				<c:when test="${!empty param.fileUri }">
					<c:set var="loadFileDisplay" value="display: none;" />
					<c:set var="linkFileDisplay" value="display: inline;" />
				</c:when>
				<c:otherwise>
					<c:set var="loadFileDisplay" value="display: inline;" />
					<c:set var="linkFileDisplay" value="display: none;" />
				</c:otherwise>
			</c:choose>
				
			<td class="label" align="right">
				<strong style="${loadFileDisplay }">Uploaded File</strong>
				<strong style="${linkFileDisplay }">File URL</strong>
			</td>
			<td class="rightLabel" align="left">
				<span style="${loadFileDisplay }"><a
					href="${param.action}.do?dispatch=download&amp;fileId=${param.fileId}">${param.fileDisplayName}</a>
				<html:hidden property="${param.domainFile}.id" />
				<html:hidden property="${param.domainFile}.name" />
				<html:hidden property="${param.domainFile}.uri" />
				</span>

				<span style="${linkFileDisplay }">${param.fileUri }</span>
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>File Title*</strong>
			</td>
			<td class="rightLabel" colspan="2">
				${param.fileTitle}&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftLabel" valign="top">
				<strong>Keywords</strong>
			</td>
			<td class="rightLabel" colspan="2">
				${param.fileKeyword}&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftLabel" valign="top">
				<strong>Visibility</strong>
			</td>
			<td class="rightLabel" colspan="2">
				${param.fileVisibilityGroups}&nbsp;
			</td>
		</tr>
	</tbody>
</table>

