<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@include file="bodyHideSearchDetailView.jsp"%>
<table width="99%" align="center" class="summaryViewNoGrid"
	bgcolor="#F5F5f5">
	<tr>
		<td class="cellLabel" width="20%">
			Sample Name
		</td>
		<td>
			<bean:write name="sampleForm" property="sampleBean.domain.name" />
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Keywords
			<br>
			<i>(one keyword per line)</i>
		</td>
		<td>
			<c:forEach var="keyword"
				items="${sampleForm.map.sampleBean.keywordSet}">
							${keyword}
							<br>
			</c:forEach>
		</td>
	</tr>
	<c:if
		test="${!empty sampleForm.map.sampleBean.primaryPOCBean.domain.id || ! empty sampleForm.map.sampleBean.otherPOCBeans}">
		<tr>
			<td class="cellLabel">
				Point of Contact
			</td>
			<td>
				<c:set var="edit" value="false" />
				<%@ include file="bodyPointOfContactView.jsp"%>
			</td>
		</tr>
	</c:if>
</table>
