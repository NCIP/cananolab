<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:forEach var="type" items="${characterizationTypes}" varStatus="ind">
	<table id="summarySection${ind.count}" width="100%" align="center"
		style="display: block" class="summaryViewLayer2">
		<tr>
			<th align="left">
				${type} &nbsp;&nbsp;&nbsp;
			</th>
		</tr>
		<tr>
			<th align="left">
				<c:forEach var="charName"
					items="${characterizationSummaryView.type2CharacterizationNames[type]}">
					<a href="#${charName}">${charName}</a> &nbsp;
			</c:forEach>
				<br>
			</th>
		</tr>
		<tr>
			<td>
				<c:forEach var="charBean"
					items="${characterizationSummaryView.type2Characterizations[type]}">
					<%@ include file="bodySingleCharacterizationSummaryView.jsp"%>
					<br />
				</c:forEach>
				<br>
			</td>
		</tr>
		<tr>
			<th align="left">
				<c:forEach var="charName"
					items="${characterizationSummaryView.type2CharacterizationNames[type]}">
					<a href="#${charName}">${charName}</a> &nbsp;
			</c:forEach>
				<br>
			</th>
		</tr>
	</table>
	<div id="summarySeparator${ind.count}">
		<br>
	</div>
</c:forEach>