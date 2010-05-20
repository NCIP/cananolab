<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:forEach var="type" items="${characterizationTypes}" varStatus="ind">
	<table id="summarySection${ind.count}" width="100%" align="center"
		style="display: block" class="summaryViewNoGrid">
		<tr>
			<th align="left">
				<span class="summaryViewHeading">${type}</span>
			</th>
		</tr>
		<tr>
			<td>
				<c:forEach var="charName"
					items="${characterizationSummaryView.type2CharacterizationNames[type]}">
					<a name="${charName}"></a>
					<table width="99%" align="center" class="summaryViewNoGrid"
						bgcolor="#dbdbdb">
						<tr>
							<th align="left">
								${charName}
							</th>
						</tr>
						<tr>
							<td>
								<c:forEach var="charBean"
									items="${characterizationSummaryView.charName2Characterizations[charName]}"
									varStatus="charBeanInd">
									<%@ include file="bodySingleCharacterizationSummaryView.jsp"%>
									<c:if
										test="${charBeanInd.count<fn:length(characterizationSummaryView.charName2Characterizations[charName])}">
										<br />
									</c:if>
								</c:forEach>
							</td>
						</tr>
						<tr>
							<th valign="top" align="left" height="6">
							</th>
						</tr>
					</table>
					<br/>					
				</c:forEach>
			</td>
		</tr>
		<tr>
			<th valign="top" align="left" height="6">
			</th>
		</tr>
	</table>
	<div id="summarySeparator${ind.count}">
		<br>
	</div>
</c:forEach>