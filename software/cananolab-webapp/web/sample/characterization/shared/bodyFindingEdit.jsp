<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:forEach var="finding" varStatus="findingIndex"
	items="${charBean.findings}">
	<table class="editTableWithGrid" align="center" width="95%">
		<c:if test="${edit eq 'true'}">
			<tr>
				<th style="text-align: right">
					<a
						href="javascript:setTheFinding(characterizationForm, 'characterization', ${finding.domain.id});">Edit</a>&nbsp;
				</th>
			</tr>
		</c:if>
		<tr>
			<td class="cellLabel">
				Data and Conditions
			</td>
		</tr>
		<tr>
			<td>
				<c:choose>
					<c:when test="${! empty finding.rows}">
						<table class="summaryViewWithGrid" align="left" width="95%">
							<tr>
								<c:forEach var="col" items="${finding.columnHeaders}">
									<td class="cellLabel">
										<c:out value="${col.displayName}"/>
									</td>
								</c:forEach>
							</tr>
							<c:forEach var="row" items="${finding.rows}">
								<tr>
									<c:forEach var="cell" items="${row.cells}">
										<td>
											<c:out value="${cell.value}"/>
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
				<br />
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Files
			</td>
		</tr>
		<tr>
			<td>
				<c:choose>
					<c:when test="${! empty finding.files}">
						<c:set var="files" value="${finding.files }" />
						<c:set var="downloadAction" value="characterization"/>
						<%@include file="../../bodyFileEdit.jsp"%>
					</c:when>
					<c:otherwise>
						N/A
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</table>
	<br />
</c:forEach>
<br>