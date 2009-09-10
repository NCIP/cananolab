<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${not empty theSample}">
	<jsp:include page="/bodyTitle.jsp">
		<jsp:param name="pageTitle" 
			value="${fn:toUpperCase(location)} Sample ${theSample.domain.name} Characterization" />
		<jsp:param name="topic" value="char_all_tab_help" />
		<jsp:param name="glossaryTopic" value="glossary_help" />
	</jsp:include>
</c:if>
<jsp:include page="/bodyMessage.jsp?bundle=sample" />
<div class="shadetabs" id="summaryTabALL">
	<ul>
		<li class="selected">
			<a
				href="javascript:showSummary('ALL', ${fn:length(characterizationTypes)})"
				title="All"><span>All</span> </a>
			<c:url var="printUrl" value="${actionName}">
				<c:param name="dispatch" value="summaryPrint" />
				<c:param name="sampleId" value="${sampleId}" />
				<c:param name="location" value="${location}" />
			</c:url>
			<c:url var="exportUrl" value="${actionName}">
				<c:param name="dispatch" value="summaryExport" />
				<c:param name="sampleId" value="${sampleId}" />
				<c:param name="location" value="${location}" />
			</c:url>
			<a href="javascript:printPage('${printUrl}')" id="printUrlAll"
				style="display: none;"></a>
			<a href="${exportUrl}" id="exportUrlAll" style="display: none;"></a>
		</li>
		<li>
			<c:forEach var="type" items="${characterizationTypes}"
				varStatus="ind">
				<a
					href="javascript:showSummary('${ind.count}', ${fn:length(characterizationTypes)})"
					title="${type}"><span>${type}</span> </a>
				<a href="javascript:printPage('${printUrl}&type=${type}')"
					id="printUrl${ind.count}" style="display: none;"></a>
				<a href="${exportUrl}&type=${type}" id="exportUrl${ind.count}"
					style="display: none;"></a>
			</c:forEach>
		</li>
		<li>
			<a href="characterization.do?dispatch=setupNew&sampleId=${sampleId }"><span>other</span>
			</a>
		</li>
	</ul>
</div>
<c:forEach var="type" items="${characterizationTypes}" varStatus="ind">
	<div class="shadetabs" id="summaryTab${ind.count}"
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
			<li>
				<a
					href="characterization.do?dispatch=setupNew&sampleId=${sampleId }"><span>other</span>
				</a>
			</li>
		</ul>
	</div>
</c:forEach>
<table class="summaryViewLayer1" width="100%">
	<c:if test="${! empty characterizationTypes}">
		<tr>
			<td>
				<a href="javascript:printPage('${printUrl}')" id="printLink">Print</a>&nbsp;&nbsp;
				<a href="${exportUrl}" id="exportLink">Export</a>
			</td>
		</tr>
	</c:if>
	<tr>
		<td>
			<c:forEach var="type" items="${characterizationTypes}"
				varStatus="ind">
				<table id="summarySection${ind.count}" width="95%" align="center"
					style="display: block" class="summaryViewLayer2">
					<tr>
						<th align="left" width="1000px">
							${type} &nbsp;&nbsp;&nbsp;
							<a
								href="characterization.do?dispatch=setupNew&sampleId=${sampleId}&charType=${type}"
								class="addlink"><img align="middle" src="images/btn_add.gif"
									border="0" /></a>&nbsp;&nbsp;
							<%--
							<c:if
								test="${!empty characterizationSummaryView.type2Characterizations[type]}">
								<a><img align="middle" src="images/btn_delete.gif"
										border="0" /> </a>
							</c:if>
							--%>
						</th>
					</tr>
					<tr>
						<td>
							<c:choose>
								<c:when
									test="${!empty characterizationSummaryView.type2Characterizations[type] }">
									<c:forEach var="charBean"
										items="${characterizationSummaryView.type2Characterizations[type]}">
										<c:set var="charObj" value="${charBean.domainChar}" />
										<c:set var="charName" value="${charBean.characterizationName}" />
										<c:set var="charType" value="${charBean.characterizationType}" />
											<a name="${charBean.domainChar.id}">
											<table class="summaryViewLayer3" width="95%" align="center">
												<tr>
													<th align="left" width="20%">
														${charName}
													</th>
													<th align="right">
														<a
															href="characterization.do?dispatch=setupUpdate&sampleId=${sampleId}&charId=${charBean.domainChar.id}&charClassName=${charBean.className}&charType=${charBean.characterizationType}">Edit</a>
													</th>
												</tr>
												<tr>
													<td class="cellLabel">
														Assay Type
													</td>
													<td>
														<c:choose>
															<c:when test="${!empty charObj.assayType}">
																${charObj.assayType}
															</c:when>
															<c:otherwise>
																<c:choose>
																	<c:when
																		test="${charBean.characterizationType eq 'physico chemical characterization'}">
																	${charName}
																</c:when>
																	<c:otherwise>N/A</c:otherwise>
																</c:choose>
															</c:otherwise>
														</c:choose>
													</td>
												</tr>
												<tr>
													<td class="cellLabel">
														Point of Contact
													</td>
													<td>
														<c:choose>
															<c:when test="${!empty charBean.pocBean.displayName}">
																${charBean.pocBean.displayName}
															</c:when>
															<c:otherwise>
															N/A
															</c:otherwise>
														</c:choose>
													</td>
												</tr>
												<tr>
													<td class="cellLabel">
														Characterization Date
													</td>
													<td>
														<c:choose>
															<c:when test="${!empty charBean.dateString}">
																${charBean.dateString}
															</c:when>
															<c:otherwise>
															N/A
															</c:otherwise>
														</c:choose>
													</td>
												</tr>
												<tr>
													<td class="cellLabel">
														Protocol
													</td>
													<td>

														<c:choose>
															<c:when
																test="${!empty charBean.protocolBean.displayName}">
																${charBean.protocolBean.displayName}
																</c:when>
															<c:otherwise>
																N/A
																</c:otherwise>
														</c:choose>

													</td>
												</tr>
												<c:if test="${charBean.withProperties }">
													<tr>
														<td class="cellLabel">
															Properties
														</td>
														<td>
															<%
																String detailPage=gov.nih.nci.cananolab.ui.sample.InitCharacterizationSetup.getInstance().getDetailPage((String)pageContext.getAttribute("charType"), (String)pageContext.getAttribute("charName"));
																pageContext.setAttribute("detailPage", detailPage);
															%>
															<c:set var="charBean" value="${charBean}" scope="session" />
															<jsp:include page="${detailPage}">
																<jsp:param name="summary" value="true" />
															</jsp:include>
														</td>
													</tr>
												</c:if>
												<tr>
													<td class="cellLabel">
														Design Description
													</td>
													<td>
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

												<tr>
													<td class="cellLabel">
														Techniques and Instruments
													</td>
													<td>
														<c:choose>
															<c:when test="${!empty charBean.experimentConfigs}">
																<%@ include file="shared/bodyExperimentConfigView.jsp"%>
															</c:when>
															<c:otherwise>N/A
																</c:otherwise>
														</c:choose>
													</td>
												</tr>
												<tr>
													<td class="cellLabel">
														Characterization Results
													</td>
													<td>
														<c:choose>
															<c:when test="${!empty charBean.findings}">
																<%@ include file="shared/bodyFindingView.jsp"%>
															</c:when>
															<c:otherwise>
															N/A</c:otherwise>
														</c:choose>
													</td>
												</tr>
												<tr>
													<td class="cellLabel">
														Analysis and Conclusion
													</td>
													<td>
														<c:choose>
															<c:when test="${!empty charBean.conclusion}">
													${charBean.conclusion}
												</c:when>
															<c:otherwise>
															N/A</c:otherwise>
														</c:choose>
													</td>
												</tr>
											</table>
											</a>
										<br>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<div class="indented4">
										N/A
									</div>
								</c:otherwise>
							</c:choose>
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