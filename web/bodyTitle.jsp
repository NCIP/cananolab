<tr height="20">
	<td width="100%">
		<table class="contentTitle" width="100%" border="0">
			<tr>
				<td>
					${param.pageTitle}
				</td>
				<td align="right" width="25%">
					<jsp:include page="/helpGlossary.jsp">
						<jsp:param name="topic" value="${param.topic}" />
						<jsp:param name="glossaryTopic" value="${param.glossaryTopic}" />
						<jsp:param name="other" value="${param.other}" />
						<jsp:param name="otherLink" value="${param.otherLink}" />
					</jsp:include>
				</td>
			</tr>
		</table>
		<br>
	</td>
</tr>
