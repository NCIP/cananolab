<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="invisibleTable" summary="layout">
	<c:choose>
		<c:when test="${theFile.domainFile.uriExternal eq 'true' }">
			<c:set var="linkStyle" value="display: block" />
			<c:set var="loadStyle" value="display: none" />
		</c:when>
		<c:otherwise>
			<c:set var="linkStyle" value="display: none" />
			<c:set var="loadStyle" value="display: block" />
		</c:otherwise>
	</c:choose>
	<tr>
		<td class="cellLabel" width="40%"><html:radio styleId="external0"
				property="${fileBeanProperty}.domainFile.uriExternal" value="false"
				onclick="displayFileRadioButton();" /> <label for="external0">Upload</label>
		</td>
		<td class="cellLabel"><html:radio styleId="external1"
				property="${fileBeanProperty}.domainFile.uriExternal" value="true"
				onclick="displayFileRadioButton();" /> <label for="external1">Enter
				File URL</label>
		</td>
	</tr>
	<tr>
		<td colspan="2"><span id="load" style="${loadStyle}"> <c:choose>
					<c:when test="${!empty fileJavascript}">
						<html:file property="${fileBeanProperty}.uploadedFile" size="60"
							styleId="uploadedFile" onchange="javascript:${fileJavascript};" />
					</c:when>
					<c:otherwise>
						<html:file property="${fileBeanProperty}.uploadedFile" size="60"
							styleId="uploadedFile" />
					</c:otherwise>
				</c:choose> </span><label for="uploadedFile">&nbsp;&nbsp;</label> <c:set
				var="uploadedUriStyle" value="display:none" /> <c:if
				test="${! empty theFile.domainFile.uri && theFile.domainFile.uriExternal eq false}">
				<c:set var="uploadedUriStyle" value="display:block" />
			</c:if> <span id="uploadedUri" style="${uploadedUriStyle}">
			<a href="${actionName}.do?dispatch=download&amp;fileId=${fileId}">
			<c:out value="${theFile.domainFile.uri }"/></a></span>
			<span id="link"	style="${linkStyle}"><html:text
					property="${fileBeanProperty}.externalUrl" size="60"
					styleId="externalUrl" />&nbsp;&nbsp;[<a class="disclaimerLink" href="html/cananoDisclaimer.html" target="new" id="pubExternalLink">Disclaimer</a>]
					</span><label for="externalUrl">&nbsp;</label>
			</td>
	</tr>
</table>
