<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:forEach var="dataSet" varStatus="dataSetIndex"
	items="${charBean.dataSets}">
	<table class="smalltable2" border="0" width="90%" align="center">
		<tr>
			<td class="subformTitle" align="right">
				<c:if test="${edit eq 'true'}">
					<a href="javascript:setTheDataSet(${dataSet.domain.id});">edit</a>&nbsp;
				</c:if>
			</td>
		</tr>
		<tr>
			<td>
				<b>Data</b>
			</td>
		</tr>
		<tr>
			<td>
				<div class="indented4">
					<table class="smalltable3" border="1">
						<tr>
							<c:forEach var="col" items="${dataSet.columnBeans}">
								<td>
									<strong>${col.displayName}</strong>
								</td>
							</c:forEach>
						</tr>
						<c:forEach var="dataRow" items="${dataSet.dataRows}">
							<tr>
								<c:forEach var="condition" items="${dataRow.conditions}">
									<td>
										${condition.value}
									</td>
								</c:forEach>
								<c:forEach var="datum" items="${dataRow.data}">
									<td>
										${datum.value}
									</td>
								</c:forEach>
							</tr>
						</c:forEach>
					</table>
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<b>File</b>
			</td>
		</tr>
		<tr>
			<td>
				<div class="indented4">
					<c:choose>
						<c:when test="${! empty dataSet.file.domainFile.uri}">
							<a href="">${dataSet.file.domainFile.uri }</a>
						</c:when>
						<c:otherwise>N/A
						</c:otherwise>
					</c:choose>
					<a href=""></a>
				</div>
			</td>
		</tr>
	</table>
	<br>
</c:forEach>