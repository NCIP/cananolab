<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<table width="100%" align="center" class="submissionView">
	<tr>
		<td class="cellLabel" width="20%">
			Finding	 
		</td>
		<td>
			<c:set var="addFindingButtonStyle" value="display:block" />
			<c:if
				test="${openFinding eq 'true'}">
				<c:set var="addFindingButtonStyle" value="display:none" />
			</c:if>
			<a style="${addFindingButtonStyle}" id="addFinding"
				href="javascript:resetTheFinding(characterizationForm);openSubmissionForm('Finding');"><img
					align="top" src="images/btn_add.gif" border="0" /> </a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<c:if test="${! empty characterizationForm.map.achar.findings }">
				<c:set var="charBean" value="${characterizationForm.map.achar}" />
				<c:set var="edit" value="true" />
				<%@ include file="bodyFindingEdit.jsp"%>
			</c:if>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<c:set var="newFindingStyle" value="display:none" />
			<c:if
				test="${openFinding eq 'true'}">
				<c:set var="newFindingStyle" value="display:block" />
			</c:if>
			<div id="newFinding" style="${newFindingStyle}">
				<a name="submitFinding"><%@ include file="bodySubmitFinding.jsp"%></a>
			</div>
		</td>
	</tr>
</table>
<br>
