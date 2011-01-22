<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<table width="99%" align="center" class="summaryViewNoGrid"
	bgcolor="#F5F5f5">
	<c:if test="${edit eq 'true'}">
		<tr>
			<td></td>
			<td width="95%"></td>
			<td align="right">
				<a href="study.do?dispatch=summaryEdit">Edit</a>
			</td>
		</tr>
	</c:if>
	<c:if test="${viewDetail eq 'true'}">
		<tr>
			<td></td>
			<td width="95%"></td>
			<td align="right">
				<a href="study.do?dispatch=summaryView">View</a>
			</td>
		</tr>
	</c:if>
	<tr>
		<td class="cellLabel">
			Study Title
		</td>
		<td colspan="3">
			<bean:write name="studyForm" property="studyBean.title" />
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Type
		</td>
		<td colspan="3">
			<c:out value="${theStudy.type}" />			
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Design Types
		</td>
		<td colspan="3">
			<c:out value="${theStudy.designTypes}" />
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Diseases
		</td>
		<td colspan="3">
			<c:out value="${theStudy.diseaseNames}" />
		</td>
	</tr>
	<!-- <tr>
		<td class="cellLabel">
			Factors
		</td>
		<td colspan="3">
			temperature, pH
		</td>
	</tr>
	-->
	<tr>
		<td class="cellLabel">
			Description
		</td>
		<td colspan="3">
			<c:out value="${theStudy.description}" />
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Outcome
		</td>
		<td colspan="3">
			<c:out value="${theStudy.outcome}" />
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Date Range
		</td>
		<td colspan="3">
			${theStudy.startDateStr} - ${theStudy.endDateStr}
		</td>
	</tr>
	<c:if
		test="${!empty studyForm.map.studyBean.primaryPOCBean.domain.id || ! empty studyForm.map.studyBean.otherPOCBeans}">
		<tr>
			<td class="cellLabel">
				Point of Contact
			</td>
			<td colspan="3">
				<c:set var="edit" value="false" />
				<%@ include file="bodyStudyPointOfContactView.jsp"%>
			</td>
		</tr>
	</c:if>	
</table>
