<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<table width="100%" align="center" class="submissionView">
	<tr>
		<th>
			Results
		</th>
	</tr>
	<tr>
		<td class="cellLabel">
			Finding&nbsp;&nbsp;
			<a id="addFinding"
				href="javascript:resetTheFinding(characterizationForm, true);"><img
					align="top" src="images/btn_add.gif" border="0" /></a>
		</td>
	</tr>
	<tr>
		<td>
			<c:if test="${! empty characterizationForm.map.achar.findings }">
				<c:set var="charBean" value="${characterizationForm.map.achar}" />
				<c:set var="edit" value="true" />
				<%@ include file="bodyFindingView.jsp"%>
			</c:if>
		</td>
	</tr>
	<tr>
		<td>
			<c:set var="findingInfoStyle" value="display:none" />
			<c:if
				test="${param.dispatch eq 'addFile' || param.dispatch eq 'removeFile' || param.dispatch eq 'drawMatrix' || param.dispatch eq 'getFinding' ||param.dispatch eq 'resetFinding'}">
				<c:set var="findingInfoStyle" value="display:block" />
			</c:if>
			<div id="newFinding" style="${findingInfoStyle}">
				<a name="submitFinding"><%@ include file="bodySubmitFinding.jsp"%></a>
			</div>
		</td>
	</tr>
</table>
<br>
