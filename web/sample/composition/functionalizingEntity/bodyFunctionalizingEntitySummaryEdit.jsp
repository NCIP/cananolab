<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page
	import="gov.nih.nci.cananolab.service.sample.helper.CompositionServiceHelper"%>

<table id="summarySection2" width="100%" align="center"
	style="display: block" class="summaryViewNoGrid">
	<tr>
		<th align="left">
			<span class="summaryViewHeading">functionalizing entity</span>&nbsp;&nbsp;
			<a
				href="functionalizingEntity.do?dispatch=setupNew&sampleId=${sampleId}"
				class="addlink"><img align="middle" src="images/btn_add.gif"
					border="0" /></a>
		</th>
	</tr>
	<tr>
		<td>
			<c:choose>
				<c:when test="${!empty compositionForm.map.comp.funcEntityTypes}">
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
										<%@include
											file="bodySingleFunctionalizingEntitySummaryEdit.jsp"%>
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
						<br/>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<div class="indented4">
						N/A
					</div>
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
<div id="summarySeparator2">
	<br>
</div>
