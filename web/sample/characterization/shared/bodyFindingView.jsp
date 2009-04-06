<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table class="summaryViewLayer4" align="center" width="95%">
	<tr>
		<th>
			Data
		</th>
		<th>
			File
		</th>
		<th>
		</th>
	</tr>
	<c:forEach var="finding" varStatus="findingIndex"
		items="${charBean.findings}">
		<tr>
			<td>
				<table class="summaryViewLayer4" border="1">
					<tr>
						<c:forEach var="col" items="${finding.columnBeans}">
							<td>
								<strong>${col.columnLabel}</strong>
							</td>
						</c:forEach>
					</tr>
					<c:forEach var="row" items="${finding.rows}">
						<tr>
							<c:forEach var="condition" items="${row.conditions}">
								<td>
									${condition.value}
								</td>
							</c:forEach>
							<c:forEach var="datum" items="${row.data}">
								<td>
									${datum.value}
								</td>
							</c:forEach>
						</tr>
					</c:forEach>
				</table>
			</td>
			<td>
				<c:choose>
					<c:when test="${! empty finding.files}">
						<c:forEach var="file" items="${finding.files}">
							<a href="">${file.domainFile.uri }</a><br>
						</c:forEach>
					</c:when>
					<c:otherwise>N/A
						</c:otherwise>
				</c:choose>
				<a href=""></a>
			</td>
			<td align="right">
				<c:if test="${edit eq 'true'}">
					<a href="javascript:setTheFinding(${finding.domain.id});">Edit</a>&nbsp;
				</c:if>
			</td>
		</tr>
	</c:forEach>
</table>