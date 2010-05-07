<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table class="contentTitle" align="center" width="100%" border="0">
	<tr height="20">
		<td>
			${param.pageTitle}
		</td>
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