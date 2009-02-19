<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table summary="" cellpadding="0" cellspacing="0" border="0"
	width="100%">
	<tr>
		<c:forEach var="type" items="${characterizationTypes}">
			<th class="borderlessLabel">
				<a href="#${type}">${type}</a>
			</th>
		</c:forEach>
		<td class="borderlessLabel">
			<a
				href="characterization.do?dispatch=setupNew&particleId=${param.particleId }">add
				new </a>
		</td>
	</tr>
</table>
<br>

<c:forEach var="type" items="${characterizationTypes}">
	<table class="smalltable3" cellpadding="0" cellspacing="0" border="0"
		width="90%">
		<tr>
			<th colspan="4" align="left">
				${type} &nbsp;&nbsp;&nbsp;
				<a href="#" class="addlink"><img align="absmiddle"
						src="images/btn_add.gif" border="0" /> </a>&nbsp;&nbsp;
				<a><img align="absmiddle" src="images/btn_delete.gif" border="0" />
				</a>
			</th>
		</tr>
		<tr>
			<td colspan="4" align="left">
				<jsp:include page="/bodyMessage.jsp?bundle=particle" />
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<%--
				<c:if
					test="${!empty characterizationSummaryView.type2Characterizations[type] }">
					<c:forEach var="charBean"
						items="${characterizationSummaryView.type2Characterizations[type]}">
						<c:set var="charObj" value="${charBean.domainChar}" />
						<table class="smalltable2" width="100%" border="0" align="center"
							cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
							<tr>
								<td class="formTitle">
									${charBean.characterizationName} ${charBean.viewTitle}
									${charBean.dateString }
								</td>

							</tr>
							<tr>
								<td>
									<table>

										<tr>
											<td>
												Design Methods Description
											</td>
											<td>
												${charObj.designMethodsDescription}
											</td>
										</tr>
										<tr>
											<td>
												Assay Endpoint
											</td>
											<td>
												${charObj.assayType}
											</td>
										</tr>

										<tr>
											<td>
												Protocol
											</td>
											<td>
												${charBean.protocolFileBean.displayName}
											</td>
										</tr>

										<tr>
											<td>
												Instruments/Techniques
											</td>
											<td>
												<c:forEach var="experimentConfig"
													items="charBean.experimentConfigs">
									${experimentConfig.displayName}<br>
												</c:forEach>
											</td>
										</tr>
										<tr>
											<td valign="top">
												Data/Files
											</td>
											<td valign="top">

												<c:forEach var="dataSetBean" items="charBean.dataSets">
													<table>
														<tr>

															<td valign="top">
																<table>
																	<tr>
																		<c:forEach var="col" items="dataSetBean.columns">
																			<th>
																				${col}
																			</th>
																		</c:forEach>
																	</tr>
																	<c:forEach var="dataRow" items="dataSetBean.dataRows">
																		<tr>
																			<c:forEach var="datum" items="dataRow.data">
																				<td>
																					${datum.value}
																				</td>
																			</c:forEach>
																			<c:forEach var="condition" items="dataRow.conditions">
																				<td>
																					${condition.value}
																				</td>
																			</c:forEach>
																		</tr>
																	</c:forEach>
																</table>
															</td>

															<td>
																<c:choose>
																	<c:when test="${dataSetBean.file.image eq 'true' }">
																		<a href="#"
																			onclick="popImage(event, 'characterization.do?dispatch=download&amp;fileId=${dataSetBean.file.domainFile.id}&amp;location=${location}', ${dataSetBean.file.domainFile.id}, 100, 100)"><img
																				src="characterization.do?dispatch=download&amp;fileId=${dataSetBean.file.domainFile.id}&amp;location=${location}"
																				border="0" width="150"> </a>
																	</c:when>
																	<c:otherwise>
																		<a
																			href="characterization.do?dispatch=download&amp;fileId=${dataSetBean.file.domainFile.id}&amp;location=${location}"
																			target="${dataSetBean.file.urlTarget}">${dataSetBean.file.domainFile.title}</a>
																	</c:otherwise>
																</c:choose>
															</td>
														</tr>
													</table>
													<br>
												</c:forEach>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<br>
					</c:forEach>
				</c:if>
				--%>
				<br>
			</td>
		</tr>
	</table>
	<br>
</c:forEach>
