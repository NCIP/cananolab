<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table border="0" align="center" cellpadding="3" cellspacing="0"
	width="100%" class="topBorderOnly" summary="">
	<tr>
	<tr class="topBorder">
		<td class="formTitle" colspan="4">
			<div align="justify">
				Results
			</div>
		</td>
	</tr>
	<tr>
		<td class="completeLabelNoBottom" valign="top" colspan="4">
			<strong>DataSet</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<c:if
				test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
				<a style="" id="addDataSet"
					href="javascript:resetTheDataSet(true);"> <span
					class="addLink2">Add</span> </a>
			</c:if>
		</td>
	</tr>
	<tr>
		<td class="leftLabel" valign="top" colspan="1">			
			<c:forEach var="dataSet" varStatus="dataSetIndex"
				items="${characterizationForm.map.achar.dataSets}">	
				DataSet #${dataSetIndex.index+1}&nbsp;
				<a
					href="javascript:setTheDataSet(${dataSet.domain.id});">
					edit</a>
					<br>
				<table>			
					<c:forEach var="dataRow" varStatus="dataRowIndex"
						items="${dataSet.dataRows}">
						<c:if test="${dataRowIndex.index eq 0}">
							<tr>
								<c:forEach var="datum"
									items="${dataRow.data}">
									<td>${datum.name}&nbsp;${datum.valueType}&nbsp;${datum.valueUnit}</td>						
								</c:forEach>
							</tr>
						</c:if> 
						<tr>
						<c:forEach var="datum"
							items="${dataRow.data}">
							<td>${datum.value}</td>						
						</c:forEach>
						</tr>
					</c:forEach>
				</table>
				<br>
			</c:forEach>
			&nbsp;
		</td>
		<td class="rightLabel" valign="top" colspan="3">
			<div id="newDataSet" style="display: none;">
				<jsp:include page="/particle/characterization/shared/bodySubmitDataSet.jsp" />
			</div>
			&nbsp;
		</td>
	</tr>
</table>
<br>
