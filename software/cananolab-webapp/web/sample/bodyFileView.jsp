<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<table class="summaryViewNoGrid" align="left">
	<c:forEach var="file" items="${files}">
		<tr>
			<td>
				<c:choose>
					<c:when test="${file.domainFile.uriExternal}">
						<a style="white-space: normal;"
							href="${downloadAction}.do?dispatch=download&amp;fileId=${file.domainFile.id}">
							<c:out value="${file.domainFile.uri}" escapeXml="false"/></a>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${file.image eq 'true'}">
						 				<c:out value="${file.domainFile.title}"/>
										<br>
								<a href="#"
									onclick="popImage(event, '${downloadAction}.do?dispatch=download&amp;fileId=${file.domainFile.id}', ${file.domainFile.id})"><img
										src="${downloadAction}.do?dispatch=download&amp;fileId=${file.domainFile.id}"
										border="0" width="150"> </a>
							</c:when>
							<c:otherwise>
								<a style="white-space: normal;"
									href="${downloadAction}.do?dispatch=download&amp;fileId=${file.domainFile.id}">
									<c:out value="${file.domainFile.title}"/></a>
							</c:otherwise>
						</c:choose>
					</c:otherwise>
				</c:choose>
				(<c:out value="${file.domainFile.type}"/>)
			</td>
			<td>
				<c:choose>
					<c:when test="${!empty fn:trim(file.keywordsStr)}">
						<c:set var="keywordsStr" value="${fn:replace(file.keywordsStr, '<', '&lt;')}" />
						<c:out
							value="${fn:replace(keywordsStr, cr, '<br>')}"
							escapeXml="false" />
					</c:when>
				</c:choose>
			</td>
			<td>
				<c:choose>
					<c:when test="${!empty fn:trim(file.domainFile.description)}">
						<c:set var="desc" value="${fn:replace(file.domainFile.description, '<', '&lt;')}" />
						<c:out
							value="${fn:replace(desc, cr, '<br>')}"
							escapeXml="false" />
					</c:when>
				</c:choose>
			</td>
		</tr>
	</c:forEach>
</table>
