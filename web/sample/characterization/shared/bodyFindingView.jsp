<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:forEach var="finding" varStatus="findingIndex"
	items="${charBean.findings}">
	<table class="summaryViewLayer4" align="center" width="95%">
		<c:if test="${edit eq 'true'}">
			<tr>
				<th style="text-align: right">
					<a
						href="javascript:setTheFinding(characterizationForm, 'characterization', ${finding.domain.id});">Edit</a>&nbsp;
				</th>
			</tr>
		</c:if>
		<tr>
			<td>
				<b> Data and Conditions</b>
			</td>
		</tr>
		<tr>
			<td>
				<c:choose>
					<c:when test="${! empty finding.rows}">
						<table class="summaryViewLayer4" width="95%" align="center">
							<tr>
								<c:forEach var="col" items="${finding.columnHeaders}">
									<th>
										<strong>${col.displayName}</strong>
									</th>
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
				</br>
			</td>
		</tr>
		<tr>
			<td>
				<b> Files</b>
			</td>
		</tr>
		<tr>
			<td>
				<c:set var="files" value="${finding.files }" />
				<%@include file="../../composition/bodyFileView.jsp"%>
			</td>
		</tr>
	</table>
	<br />
</c:forEach>
<br>