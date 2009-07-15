<tr class="contentTitle" height="25">
	<td>
		${param.pageTitle}
	</td>
	<td align="right" width="15%">
		<jsp:include page="/helpGlossary.jsp">
			<jsp:param name="topic" value="${param.topic}" />
			<jsp:param name="glossaryTopic" value="${param.glossaryTopic}" />
			<jsp:param name="other" value="${param.other}" />
			<jsp:param name="otherLink" value="${param.otherLink}" />
		</jsp:include>
		<br><br>
	</td>
</tr>