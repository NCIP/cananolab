<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table class="summaryViewLayer4" align="center" width="95%">
	<tr>
		<th>
			data
		</th>
		<th>
			file
		</th>
		<th>
		</th>
	</tr>
	<c:forEach var="dataSet" varStatus="dataSetIndex"
		items="${charBean.dataSets}">
		<tr>
			<td>
				<table class="summaryViewLayer4" border="1">
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
			</td>
			<td>
				<c:choose>
					<c:when test="${! empty dataSet.file.domainFile.uri}">
						<a href="">${dataSet.file.domainFile.uri }</a>
					</c:when>
					<c:otherwise>N/A
						</c:otherwise>
				</c:choose>
				<a href=""></a>
			</td>
			<td align="right">
				<c:if test="${edit eq 'true'}">
					<a href="javascript:setTheDataSet(${dataSet.domain.id});">Edit</a>&nbsp;
				</c:if>
			</td>
		</tr>
	</c:forEach>
</table>