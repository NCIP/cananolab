<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:forEach var="type" items="${characterizationTypes}" varStatus="ind">
	<table id="summarySectionHeader${ind.count}" width="100%" align="center"
		style="display: block" class="summaryViewHeader">
		<tr>
			<td align="left">
				<b>${type}</b>
				<br />
				<c:forEach var="charName"
					items="${characterizationSummaryView.type2CharacterizationNames[type]}">
					<a href="#${charName}">${charName}
						(${characterizationSummaryView.charName2Counts[charName]})</a> &nbsp;				
	            </c:forEach>
			</th>
		</tr>
	</table>
	<div id="summaryHeaderSeparator${ind.count}">		
	</div>
</c:forEach>
<br />
<c:forEach var="type" items="${characterizationTypes}" varStatus="ind">
	<table id="summarySection${ind.count}" width="100%" align="center"
		style="display: block">		
		<tr>
			<th align="left" style="font-size: 0.75em">
				${type} &nbsp;&nbsp;&nbsp;
			</th>
		</tr>		
		<tr>
			<td>
				<c:forEach var="charName"
					items="${characterizationSummaryView.type2CharacterizationNames[type]}">
					<a name="${charName}"></a>
					<table width="100%" align="center" class="summaryViewLayer2">
						<tr>
							<th align="left" style="font-size: 0.70em">
								${charName}
							</th>
						</tr>
						<tr>
							<td>
								<c:forEach var="charBean"
									items="${characterizationSummaryView.charName2Characterizations[charName]}">
									<%@ include file="bodySingleCharacterizationSummaryView.jsp"%>
									<br />
								</c:forEach>
							</td>
						</tr>
					</table>
					<br>
				</c:forEach>
			</td>
		</tr>
	</table>
	<div id="summarySeparator${ind.count}">
		<br>
	</div>
</c:forEach>