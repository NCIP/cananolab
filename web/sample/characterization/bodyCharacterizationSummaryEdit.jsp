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
		<li>
			<a href="characterization.do?dispatch=setupNew&sampleId=${sampleId }"><span>New</span>
			</a>
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
			<li>
				<a
					href="characterization.do?dispatch=setupNew&sampleId=${sampleId }"><span>New</span>
				</a>
			</li>
		</ul>
	</div>
</c:forEach>
<c:forEach var="type" items="${characterizationTypes}" varStatus="ind">
	<table id="summarySection${ind.count}" class="smalltable3"
		cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr>
			<th colspan="4" align="left">
				${type} &nbsp;&nbsp;&nbsp;
				<a
					href="characterization.do?dispatch=setupNew&sampleId=${sampleId}&charType=${type}"
					class="addlink"><img align="middle" src="images/btn_add.gif"
						border="0" /></a>&nbsp;&nbsp;
				<a><img align="middle" src="images/btn_delete.gif" border="0" />
				</a>
			</th>
		</tr>
		<tr>
			<td colspan="4">
				<c:choose>
					<c:when
						test="${!empty characterizationSummaryView.type2Characterizations[type] }">
						<c:forEach var="charBean"
							items="${characterizationSummaryView.type2Characterizations[type]}">
							<c:set var="charObj" value="${charBean.domainChar}" />
							<div class="indented4">
								<table class="summarytable" cellpadding="0" cellspacing="0"
									border="0" width="90%">
									<tr>
										<th align="left">
											${charBean.characterizationName}
											(${charBean.pocBean.displayName} ${charBean.dateString })
										</th>
										<th align="right">
											<a
												href="characterization.do?dispatch=setupUpdate&sampleId=${sampleId}&charId=${charBean.domainChar.id}&charClassName=${charBean.className}&charType=${charBean.characterizationType}">Edit</a>
										</th>
									</tr>
									<tr>
										<td colspan="2">
											<table border="0">
												<tr>
													<td>
														ASSAY TYPE
														<div class="indented5">
															<c:choose>
																<c:when test="${!empty charObj.assayType}">
																${charObj.assayType}
															</c:when>
																<c:otherwise>
															N/A
															</c:otherwise>
															</c:choose>
														</div>
													</td>
												</tr>
												<tr>
													<td>
														PROTOCOL
														<div class="indented5">
															<c:choose>
																<c:when
																	test="${!empty charBean.protocolFileBean.displayName}">
																${charBean.protocolFileBean.displayName}
																</c:when>
																<c:otherwise>
																N/A
																</c:otherwise>
															</c:choose>
														</div>
													</td>
												</tr>
												<tr>
													<td>
														DESIGN DESCRIPTION
														<div class="indented5">
															<c:choose>
																<c:when
																	test="${!empty fn:trim(charObj.designMethodsDescription)}">
																	${charObj.designMethodsDescription}
															</c:when>
																<c:otherwise>N/A
															</c:otherwise>
															</c:choose>
														</div>
													</td>
												</tr>

												<tr>
													<td>
														TECHNIQUES AND INSTRUMENTS
														<div class="indented5">
															<c:choose>
																<c:when test="${!empty charBean.experimentConfigs}">
																	<%@ include file="shared/bodyExperimentConfigView.jsp"%>
																</c:when>
																<c:otherwise>N/A
																</c:otherwise>
															</c:choose>
														</div>
													</td>
												</tr>

												<tr>
													<td valign="top">
														DATA AND FILES
														<div class="indented5">
															<c:choose>
																<c:when test="${!empty charBean.dataSets}">
																	<%@ include file="shared/bodyDataSetView.jsp"%>
																</c:when>
																<c:otherwise>
																N/A</c:otherwise>
															</c:choose>
														</div>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</div>
							<br>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<div class="indented4">
							<table class="summarytable" width="90%">
								<tr>
									<td valign="top" class="borderlessLabel">
										N/A
									</td>
								</tr>
							</table>
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