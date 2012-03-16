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
		<td class="cellLabel" width="30%"><html:radio styleId="external0"
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
		<td colspan="2"><span id="load" style="${loadStyle}"> <html:file
					property="${fileBeanProperty}.uploadedFile" size="60"
					styleId="uploadedFile" onchange="javascript:writeLink(null, null);" />
		</span><label for="uploadedFile">&nbsp;&nbsp;</label> <c:set
				var="uploadedUriStyle" value="display:none" /> <c:if
				test="${! empty theFile.domainFile.uri && theFile.domainFile.uriExternal eq false}">
				<c:set var="uploadedUriStyle" value="display:block" />
			</c:if> <span id="uploadedUri" style="${uploadedUriStyle}"><c:out
					value="${theFile.domainFile.uri}" /> </span> <span id="link"
			style="${linkStyle }"><html:text
					property="${fileBeanProperty}.externalUrl" size="60"
					styleId="externalUrl" /> </span><label for="externalUrl">&nbsp;</label></td>
	</tr>
</table>


<table width="100%" align="center" class="submissionView" summary="layout">
			<c:choose>
				<c:when
					test="${publicationForm.map.publication.domainFile.uriExternal eq 'true' }">
					<c:set var="linkStyle" value="display: block" />
					<c:set var="loadStyle" value="display: none" />
				</c:when>
				<c:otherwise>
					<c:set var="linkStyle" value="display: none" />
					<c:set var="loadStyle" value="display: block" />
				</c:otherwise>
			</c:choose>
			<tr>
				<td class="cellLabel">
					<label for="external0" style="display:none">File</label>
					<html:radio styleId="external0"
						property="publication.domainFile.uriExternal" value="false"
						onclick="displayFileRadioButton()" />
					<label for="uploadedFileField">Upload File</label>
					<br>
					&nbsp;&nbsp;or
					<br>
					<label for="external1" style="display:none">URL</label>
					<html:radio styleId="external1"
						property="publication.domainFile.uriExternal" value="true"
						onclick="displayFileRadioButton()" />
					<label for="externalUrlField">Enter File URL</label>
				</td>
				<td colspan="2">
					<span id="load"> <html:file
							property="publication.uploadedFile" styleId="uploadedFileField"
							size="60"
							onchange="javascript:updateWithExistingNonPubMedDOIPublication('${publicationForm.map.publication.domainFile.uri}');" />
						&nbsp;&nbsp; </span>
					<br>
					<br>
					<span id="link" style=""><html:text
							property="publication.externalUrl" styleId="externalUrlField"
							size="60"
							onchange="javascript:updateWithExistingNonPubMedDOIPublication('${publicationForm.map.publication.domainFile.uri}');" />
					</span>&nbsp;&nbsp;(<a class="disclaimerLink" href="html/cananoDisclaimer.html" target="new" id="pubExternalLink">Disclaimer</a>)
				</td>
			</tr>
			<tr>
				<td colspan="3">
					<span id="existingFileInfo" style="display: none"></span>
				</td>
			</tr>
			<c:if
				test="${!empty publicationForm.map.publication.domainFile.uri }">
				<tr>
					<td colspan="3">
						<div id="existingFileInfoFromUpdate" style="display: block">
							<c:choose>
								<c:when
									test="${publicationForm.map.publication.image eq 'true'}">
						 				<c:out value="${publicationForm.map.publication.domainFile.title}"/><br>
									<br>
									<a href="#"
										onclick="popImage(event, 'publication.do?dispatch=download&amp;fileId=${publicationForm.map.publication.domainFile.id}',
														${publicationForm.map.publication.domainFile.id})"><img
											src="publication.do?dispatch=download&amp;fileId=${publicationForm.map.publication.domainFile.id}"
											border="0" width="150"> </a>
								</c:when>
								<c:otherwise>
											Submitted Publication &nbsp;&nbsp;
										<a
										href="publication.do?dispatch=download&amp;fileId=${publicationForm.map.publication.domainFile.id}"
										target="${publicationForm.map.publication.urlTarget}">
										<c:out value="${publicationForm.map.publication.domainFile.uri}"/></a>
									<br>
								</c:otherwise>
							</c:choose>
						</div>
					</td>
				</tr>
			</c:if>
		</table>