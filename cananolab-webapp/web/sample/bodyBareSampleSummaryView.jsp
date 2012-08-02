<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@include file="bodyHideSearchDetailView.jsp"%>
<table width="99%" align="center" class="summaryViewNoGrid"
	bgcolor="#F5F5f5">
	<tr>
		<th scope="row" class="cellLabel" width="20%">
			Sample Name
		</th>
		<td>
			<bean:write name="sampleForm" property="sampleBean.domain.name" />
		</td>
	</tr>
	<tr>
		<th scope="row" class="cellLabel" width="20%">
			Created Date
		</th>
		<td>
			<bean:write name="sampleForm" property="sampleBean.domain.createdDate" format="MM-dd-yyyy"/>
		</td>
	</tr>
	
	<tr>
		<th scope="row" class="cellLabel">
			Keywords		
		</th>
		<td>
			<c:out value="${sampleForm.map.sampleBean.keywordsDisplayName}"
				escapeXml="false" />
		</td>
	</tr>
	<c:if
		test="${!empty sampleForm.map.sampleBean.primaryPOCBean.domain.id || ! empty sampleForm.map.sampleBean.otherPOCBeans}">
		<tr>
			<th scope="row" class="cellLabel">
				Point of Contact
			</th>
			<td>
				<c:set var="edit" value="false" />
				<%@ include file="bodyPointOfContactView.jsp"%>
			</td>
		</tr>
	</c:if>
</table>
