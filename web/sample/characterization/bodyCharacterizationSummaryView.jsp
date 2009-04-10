<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="/bodyMessage.jsp?bundle=particle" />
<div class="animatedtabs" id="summaryTabALL">
	<ul>
		<li class="selected">
			<a
				href="javascript:showSummary('ALL', ${fn:length(characterizationTypes)})"
				title="All"><span>All</span> </a>
		</li>
		<li>
			<c:forEach var="type" items="${characterizationTypes}"
				varStatus="ind">
				<a
					href="javascript:showSummary('${ind.count}', ${fn:length(characterizationTypes)})"
					title="${type}"><span>${type}</span> </a>
			</c:forEach>
		</li>
	</ul>
</div>
<c:forEach var="type" items="${characterizationTypes}" varStatus="ind">
	<div class="animatedtabs" id="summaryTab${ind.count}"
		style="display: none;">
		<ul>
			<li>
				<a
					href="javascript:showSummary('ALL',${fn:length(characterizationTypes)})"
					title="All"><span>All</span> </a>
			</li>
			<c:forEach var="type" items="${characterizationTypes}"
				varStatus="ind2">
				<c:choose>
					<c:when test="${ind.count eq ind2.count }">
						<c:set var="selectedClass" value="selected" />
					</c:when>
					<c:otherwise>
						<c:set var="selectedClass" value="" />
					</c:otherwise>
				</c:choose>
				<li class="${selectedClass}">
					<a
						href="javascript:showSummary('${ind2.count}', ${fn:length(characterizationTypes)})"
						title="${type}"><span>${type}</span> </a>
				</li>
			</c:forEach>
		</ul>
	</div>
</c:forEach>
<table class="summaryViewLayer1" width="100%">
	<tr>
		<td>
			<a href="print">Print</a>&nbsp;&nbsp;
			<a href="export">Export</a>
		</td>
	</tr>
	<tr>
		<td>
			<c:forEach var="type" items="${characterizationTypes}"
				varStatus="ind">
				<table id="summarySection${ind.count}" width="95%" align="center"
					style="display: block" class="summaryViewLayer2">
					<tr>
						<th align="left">
							${type} &nbsp;&nbsp;&nbsp;
						</th>
					</tr>
					<tr>
						<td>
							<c:forEach var="charBean"
								items="${characterizationSummaryView.type2Characterizations[type]}">
								<c:set var="charObj" value="${charBean.domainChar}" />
								<div class="indented4">
									<table class="summaryViewLayer3" width="95%" align="center">
										<tr>
											<th align="left" width="20%">
												${charBean.characterizationName}
											</th>
											<th align="right" colspan="2">
											</th>
										</tr>
										<c:if test="${!empty charObj.assayType}">
											<tr>
												<td class="cellLabel">
													Assay Type
												</td>
												<td colspan="2">
													${charObj.assayType}
											</tr>
										</c:if>
										<c:if test="${!empty charBean.pocBean.displayName}">
											<tr>
												<td class="cellLabel">
													Point of Contact
												</td>
												<td colspan="2">
													${charBean.pocBean.displayName}
											</tr>
										</c:if>
										<c:if test="${!empty charBean.dateString}">
											<tr>
												<td class="cellLabel">
													Characterization Date
												</td>
												<td colspan="2">
													${charBean.dateString}
											</tr>
										</c:if>
										<c:if test="${!empty charBean.protocolBean.displayName}">>
													<tr>
												<td class="cellLabel">
													Protocol
												</td>
												<td colspan="2">
													${charBean.protocolBean.displayName}
												</td>
											</tr>
										</c:if>
										<c:if test="${charBean.withProperties }">
											<tr>
												<td class="cellLabel">
													Properties
												</td>
												<td colspan="2">
													<%@include file="bodyCharacterizationPropertiesView.jsp"%>
												</td>
											</tr>
										</c:if>
										<c:if
											test="${!empty fn:trim(charObj.designMethodsDescription)}">
											<tr>
												<td class="cellLabel">
													Design Description
												</td>
												<td colspan="2">
													<c:choose>
														<c:when
															test="${!empty fn:trim(charObj.designMethodsDescription)}">
															<c:out
																value="${fn:replace(charObj.designMethodsDescription, cr, '<br>')}"
																escapeXml="false" />
														</c:when>
														<c:otherwise>N/A
															</c:otherwise>
													</c:choose>
												</td>
											</tr>
										</c:if>
										<c:if test="${!empty charBean.experimentConfigs}">
											<tr>
												<td class="cellLabel">
													Techniques and Instruments
												</td>
												<td colspan="2">
													<%@ include file="shared/bodyExperimentConfigView.jsp"%>
												</td>
											</tr>
										</c:if>
										<c:if test="${!empty charBean.findings}">
											<tr>
												<td class="cellLabel">
													Characterizaiton Results
												</td>
												<td colspan="2">
													<%@ include file="shared/bodyFindingView.jsp"%>
												</td>
											</tr>
										</c:if>
										<c:if test="${!empty charBean.conclusion}">
											<tr>
												<td class="cellLabel">
													Analysis and Conclusion
												</td>
												<td colspan="2">
													${charBean.conclusion}
												</td>
											</tr>
										</c:if>
									</table>
								</div>
								<br>
							</c:forEach>
							<br>
						</td>
					</tr>
				</table>
				<div id="summarySeparator${ind.count}">
					<br>
				</div>
			</c:forEach>
		</td>
	</tr>
</table>