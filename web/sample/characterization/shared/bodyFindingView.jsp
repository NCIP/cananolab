<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table class="summaryViewLayer4" align="center" width="95%">
	<tr>
		<th>
			Data and Conditions
		</th>
		<th>
			File(s)
		</th>
		<th>
		</th>
	</tr>
	<c:forEach var="finding" varStatus="findingIndex"
		items="${charBean.findings}">
		<tr>
			<td>
				<c:choose>
					<c:when test="${! empty finding.rows}">
						<table class="summaryViewLayer4" width="100%">
							<tr>
								<c:forEach var="col" items="${finding.columnHeaders}">
									<td>
										<strong>${col.displayName}</strong>
									</td>
								</c:forEach>
							</tr>
							<c:forEach var="row" items="${finding.rows}">
								<tr>
									<c:forEach var="cell" items="${row.cells}">
										<td>
											${cell.value}
										</td>
									</c:forEach>
								</tr>
							</c:forEach>
						</table>
					</c:when>
					<c:otherwise>
						N/A
					</c:otherwise>
				</c:choose>
			</td>
			<td>
				<c:choose>
					<c:when test="${! empty finding.files}">
						<c:forEach var="file" items="${finding.files}">
							<c:choose>
								<c:when test="${file.domainFile.uriExternal eq 'true'}">
									<a
										href="characterization.do?dispatch=download&amp;fileId=${file.domainFile.id}&amp;location=${location}"
										target="external">${file.domainFile.uri}</a>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${file.image eq 'true'}">
						 					${file.domainFile.title}
											<br>
											<a href="#"
												onclick="popImage(event, 'characterization.do?dispatch=download&amp;fileId=${file.domainFile.id}&amp;location=${location}', ${file.domainFile.id})"><img
													src="characterization.do?dispatch=download&amp;fileId=${file.domainFile.id}&amp;location=${location}"
													border="0" width="150"> </a>
										</c:when>
										<c:otherwise>
											<a
												href="characterization.do?dispatch=download&amp;fileId=${file.domainFile.id}&amp;location=${location}">
												${file.domainFile.title} </a>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
							<br>
							<br>
						</c:forEach>
					</c:when>
					<c:otherwise>
						N/A
					</c:otherwise>
				</c:choose>
			</td>
			<td align="right">
				<c:if test="${edit eq 'true'}">
					<a
						href="javascript:setTheFinding(characterizationForm, 'characterization', ${finding.domain.id});">Edit</a>&nbsp;
				</c:if>
			</td>
		</tr>
	</c:forEach>
</table>
<br>