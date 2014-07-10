<%--L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="spacer"/>
<table class="contentTitle" width="100%" border="0" summary="layout">
	<tr>
		<th scope="col" align="left">
			<c:out value="${param.pageTitle}"/>
		</th>
		<c:if test="${empty printView}">
			<td align="right" width="30%">
				<jsp:include page="/helpGlossary.jsp">
					<jsp:param name="topic" value="${param.topic}" />
					<jsp:param name="glossaryTopic" value="${param.glossaryTopic}" />
					<jsp:param name="other" value="${param.other}" />
					<jsp:param name="otherLink" value="${param.otherLink}" />
					<jsp:param name="printLink" value="${param.printLink}" />
					<jsp:param name="exportLink" value="${param.exportLink}" />
				</jsp:include>
			</td>
		</c:if>
	</tr>
</table>
<br>