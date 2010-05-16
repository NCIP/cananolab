<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table id="summarySection${index}" width="100%" align="center"
	style="display: block" class="summaryViewNoGrid">
	<tr>
		<th align="left">
			<span class="summaryViewHeading">functionalizing entity</span>
		</th>
	</tr>
	<tr>
		<td>
			<c:forEach var="entityType"
				items="${compositionForm.map.comp.funcEntityTypes}">
				<a href="#${entityType}"></a>
				<table width="99%" align="center" class="summaryViewNoGrid"
					bgcolor="#dbdbdb">
					<tr>
						<th align="left">
							${entityType}
						</th>
					</tr>
					<tr>
						<td>
							<c:forEach var="functionalizingEntity"
								items="${compositionForm.map.comp.type2FuncEntities[entityType]}"
								varStatus="entityInd">
								<%@include file="bodySingleFunctionalizingEntitySummaryView.jsp"%>
								<c:if
									test="${entityInd.count<fn:length(compositionForm.map.comp.type2FuncEntities[entityType])}">
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
				<br />
			</c:forEach>
		</td>
	</tr>
</table>
<div id="summarySeparator${index}">
</div>
