<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:forEach var="finding" varStatus="findingIndex"
	items="${charBean.findings}">
	<table align="center" width="100%" class="summaryViewNoGrid">
		<tr>
			<td class="cellLabel">
				<c:if test="${! empty finding.rows}">Data and Conditions</c:if>
			</td>
		</tr>
		<tr>
			<td>
				<c:choose>
					<c:when test="${! empty finding.rows}">
						<table class="summaryViewWithGrid" align="left">
							<tr>
								<c:forEach var="col" items="${finding.columnHeaders}">
									<td class="cellLabel">
										${col.displayName}
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
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				<c:if test="${! empty finding.files}">
				<br>Files
			</c:if>
			</td>
		</tr>
		<tr>
			<td>
				<c:choose>
					<c:when test="${! empty finding.files}">
						<c:set var="files" value="${finding.files }" />
						<c:set var="downloadAction" value="characterization"/>
						<%@include file="../../bodyFileView.jsp"%>
					</c:when>
				</c:choose>
			</td>
		</tr>
	</table>
</c:forEach>
<br>