<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:choose>
	<c:when
		test="${displaytype == 'Physical' ||
			  displaytype == 'Molecular Weight' ||
			  displaytype == 'Morphology' ||
			  displaytype == 'Purity' ||
			  displaytype == 'Size' ||
			  displaytype == 'Surface' ||
			  displaytype == 'Solubility' ||
			  displaytype == 'Shape'}">
		<c:set var="phyDisplay" value="display: block;" />
	</c:when>
	<c:otherwise>
		<c:set var="phyDisplay" value="display: none;" />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when
		test="${displaytype == 'In Vitro' ||
			    displaytype == 'Toxicity' ||
				displaytype == 'Cytotoxicity' ||
				displaytype == 'Immunotoxicity' ||
				displaytype == 'Oxidative Stress' ||
				displaytype == 'Enzyme Induction'}">
		<c:set var="invitroDisplay" value="display: block;" />
	</c:when>
	<c:otherwise>
		<c:set var="invitroDisplay" value="display: none;" />
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${canCreateNanoparticle eq 'true'}">
		<c:set var="physicalType" value="Physical" />
		<li class="toplist">
			<a href="#" class="subMenuSecondary">PHYSICAL CHARACTERIZATIONS</a>
			<ul class="sublist_4" style="${phyDisplay}">
				<c:forEach var="subCharType"
					items="${allCharacterizations[physicalType]}">
					<li>
						<c:url var="submitUrl"
							value="${charaLeafActionName[subCharType]}.do">
							<c:param name="particleId" value="${particleId}" />
							<c:param name="submitType" value="${physicalType}" />
							<c:param name="page" value="0" />
							<c:param name="dispatch" value="setup" />
						</c:url>
						<table class="charTitle">
							<tr class="titleRow">
								<td class="titleCell_2">
									<a href="#" class="sublist_4">${subCharType}</a>
								</td>
								<td>
									&nbsp;
								</td>
								<td class="addCell">
									<a href="${submitUrl}" class="addlink">add</a>
								</td>
								<c:if
									test="${canUserDeleteChars eq 'true' &&
												!empty charaLeafToCharacterizations[subCharType]}">
									<td>
										&nbsp;
									</td>
									<td class="addCell">
										<c:url var="deleteUrl" value="deleteAction.do">
											<c:param name="particleId" value="${particleId}" />
											<c:param name="submitType" value="${physicalType}" />
											<c:param name="page" value="0" />
											<c:param name="dispatch" value="setup" />
										</c:url>
										<a href="${deleteUrl}" class="addlink">delete</a>
									</td>
								</c:if>
							</tr>
						</table>
						<c:if test="${!empty charaLeafToCharacterizations[subCharType]}">
							<ul class="sublist_5" style="${phyDisplay}">
								<c:forEach var="leafCharBean"
									items="${charaLeafToCharacterizations[subCharType]}">
									<c:url var="url" value="${leafCharBean.actionName}.do">
										<c:param name="page" value="0" />
										<c:param name="dispatch" value="summaryView" />
										<c:param name="particleId" value="${particleId}" />
										<c:param name="characterizationId" value="${leafCharBean.id}" />
										<c:param name="submitType" value="${leafCharBean.name}" />
									</c:url>
									<li>
										<a href=${url } class="sublist_5"><span
											class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
									</li>
								</c:forEach>
							</ul>
						</c:if>
					</li>
				</c:forEach>
			</ul>
		</li>
		<c:set var="inVitroType" value="In Vitro" />
		<li class="toplist">
			<a href="#" class="subMenuSecondary">IN VITRO CHARACTERIZATIONS</a>
			<ul class="sublist_1" style="${invitroDisplay}">
				<c:forEach var="secondLevelChar"
					items="${allCharacterizations[inVitroType]}">
					<li>
						<a href="#" class="sublist_1">${secondLevelChar}</a>
						<ul class="sublist_2" style="${invitroDisplay}">
							<c:forEach var="thirdLevelChar"
								items="${allCharacterizations[secondLevelChar]}">
								<li>
									<c:choose>
										<c:when test="${!empty allCharacterizations[thirdLevelChar]}">
											<table class="charTitle">
												<tr class="titleRow">
													<td class="titleCell_2">
														<a href="#" class="sublist_2">${thirdLevelChar}</a>
													</td>
												</tr>
											</table>
										</c:when>
										<c:otherwise>
											<c:url var="submitUrl"
												value="${charaLeafActionName[thirdLevelChar]}.do">
												<c:param name="particleId" value="${particleId}" />
												<c:param name="submitType" value="${secondLevelChar}" />
												<c:param name="page" value="0" />
												<c:param name="dispatch" value="setup" />
											</c:url>
											<table class="charTitle">
												<tr class="titleRow">
													<td class="titleCell_2">
														<a href="#" class="sublist_2">${thirdLevelChar}</a>
													</td>
													<td>
														&nbsp;
													</td>
													<td class="addCell">
														<a href="${submitUrl}" class="addlink">add</a>
													</td>
													<c:if
														test="${canUserDeleteChars eq 'true' &&
																!empty charaLeafToCharacterizations[fourthLevelChar]}">
														<td>
															&nbsp;
														</td>
														<td class="addCell">
															<c:url var="deleteUrl" value="deleteAction.do">
																<c:param name="particleId" value="${particleId}" />
																<c:param name="submitType" value="${secondLevelChar}" />
																<c:param name="page" value="0" />
																<c:param name="dispatch" value="setup" />
															</c:url>
															<a href="${deleteUrl}" class="addlink">delete</a>
														</td>
													</c:if>
												</tr>
											</table>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when
											test="${!empty charaLeafToCharacterizations[thirdLevelChar]}">
											<ul class="sublist_5" style="${invitroDisplay}">
												<c:forEach var="leafCharBean"
													items="${charaLeafToCharacterizations[thirdLevelChar]}">
													<c:url var="url3" value="${leafCharBean.actionName}.do">
														<c:param name="page" value="0" />
														<c:param name="dispatch" value="summaryView" />
														<c:param name="particleId" value="${particleId}" />
														<c:param name="characterizationId"
															value="${leafCharBean.id}" />
														<c:param name="submitType" value="${leafCharBean.name}" />
													</c:url>
													<li>
														<a href="${url3}" class="sublist_5"><span
															class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
													</li>
												</c:forEach>
											</ul>
										</c:when>
										<c:otherwise>
											<c:if test="${!empty allCharacterizations[thirdLevelChar]}">
												<ul class="sublist_3" style="${invitroDisplay}">
													<c:forEach var="fourthLevelChar"
														items="${allCharacterizations[thirdLevelChar]}">
														<li>
															<c:choose>
																<c:when
																	test="${!empty allCharacterizations[fourthLevelChar]}">
																	<table class="charTitle">
																		<tr class="titleRow">
																			<td class="titleCell_3">
																				<a href="#" class="sublist_4">${fourthLevelChar}</a>
																			</td>
																		</tr>
																	</table>
																</c:when>
																<c:otherwise>
																	<c:url var="submitUrl"
																		value="${charaLeafActionName[fourthLevelChar]}.do">
																		<c:param name="particleId" value="${particleId}" />
																		<c:param name="submitType" value="${secondLevelChar}" />
																		<c:param name="page" value="0" />
																		<c:param name="dispatch" value="setup" />
																	</c:url>
																	<table class="charTitle">
																		<tr class="titleRow">
																			<td class="titleCell_3">
																				<a href="#" class="sublist_4">${fourthLevelChar}</a>
																			</td>
																			<td>
																				&nbsp;
																			</td>
																			<td class="addCell">
																				<a href="${submitUrl}" class="addlink">add</a>
																			</td>
																			<c:if
																				test="${canUserDeleteChars eq 'true' &&
																				!empty charaLeafToCharacterizations[fourthLevelChar]}">
																				<td>
																					&nbsp;
																				</td>
																				<td class="addCell">
																					<c:url var="deleteUrl" value="deleteAction.do">
																						<c:param name="particleId" value="${particleId}" />
																						<c:param name="submitType"
																							value="${secondLevelChar}" />
																						<c:param name="page" value="0" />
																						<c:param name="dispatch" value="setup" />
																					</c:url>
																					<a href="${deleteUrl}" class="addlink">delete</a>
																				</td>
																			</c:if>
																		</tr>
																	</table>
																</c:otherwise>
															</c:choose>
															<c:choose>
																<c:when
																	test="${!empty charaLeafToCharacterizations[fourthLevelChar]}">
																	<ul class="sublist_5" style="${invitroDisplay}">
																		<c:forEach var="leafCharBean"
																			items="${charaLeafToCharacterizations[fourthLevelChar]}">
																			<c:url var="url4"
																				value="${leafCharBean.actionName}.do">
																				<c:param name="page" value="0" />
																				<c:param name="dispatch" value="summaryView" />
																				<c:param name="particleId" value="${particleId}" />
																				<c:param name="characterizationId"
																					value="${leafCharBean.id}" />
																				<c:param name="submitType"
																					value="${leafCharBean.name}" />
																			</c:url>
																			<li>
																				<a href="${url4}" class="sublist_5"><span
																					class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
																			</li>
																		</c:forEach>
																	</ul>
																</c:when>
																<c:otherwise>
																	<c:if
																		test="${!empty allCharacterizations[fourthLevelChar]}">
																		<ul class="sublist_4" style="${invitroDisplay}">
																			<c:forEach var="fifthLevelChar"
																				items="${allCharacterizations[fourthLevelChar]}">
																				<li>

																					<c:url var="submitUrl"
																						value="${charaLeafActionName[fifthLevelChar]}.do">
																						<c:param name="particleId" value="${particleId}" />
																						<c:param name="submitType"
																							value="${secondLevelChar}" />
																						<c:param name="page" value="0" />
																						<c:param name="dispatch" value="setup" />
																					</c:url>
																					<table class="charTitle">
																						<tr class="titleRow">
																							<td class="titleCell_4">
																								<a href="#" class="sublist_4">${fifthLevelChar}</a>
																							</td>
																							<td>
																								&nbsp;
																							</td>
																							<td class="addCell">
																								<a href="${submitUrl}" class="addlink">add</a>
																							</td>
																							<c:if
																								test="${canUserDeleteChars eq 'true' &&
																							!empty charaLeafToCharacterizations[fifthLevelChar]}">
																								<td>
																									&nbsp;
																								</td>
																								<td class="addCell">
																									<c:url var="deleteUrl" value="deleteAction.do">
																										<c:param name="particleId"
																											value="${particleId}" />
																										<c:param name="submitType"
																											value="${secondLevelChar}" />
																										<c:param name="page" value="0" />
																										<c:param name="dispatch" value="setup" />
																									</c:url>
																									<a href="${deleteUrl}" class="addlink">delete</a>
																								</td>
																							</c:if>
																						</tr>
																					</table>
																					<c:if
																						test="${!empty charaLeafToCharacterizations[fifthLevelChar]}">
																						<ul class="sublist_5" style="${invitroDisplay}">
																							<c:forEach var="leafCharBean"
																								items="${charaLeafToCharacterizations[fifthLevelChar]}">
																								<c:url var="url5"
																									value="${leafCharBean.actionName}.do">
																									<c:param name="page" value="0" />
																									<c:param name="dispatch"
																										value="summaryView" />
																									<c:param name="particleId"
																										value="${particleId}" />
																									<c:param name="characterizationId"
																										value="${leafCharBean.id}" />
																									<c:param name="submitType"
																										value="${leafCharBean.name}" />
																								</c:url>
																								<li>
																									<a href=${url5 } class="sublist_5"><span
																										class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
																								</li>
																							</c:forEach>
																						</ul>
																					</c:if>
																				</li>
																			</c:forEach>
																		</ul>
																	</c:if>
																</c:otherwise>
															</c:choose>
														</li>
													</c:forEach>
												</ul>
											</c:if>
										</c:otherwise>
									</c:choose>
								</li>
							</c:forEach>
						</ul>
					</li>
				</c:forEach>
			</ul>
		</li>
	</c:when>
	<c:otherwise>
		<c:set var="physicalType" value="Physical" />
		<li class="toplist">
			<a href="#" class="subMenuSecondary">PHYSICAL CHARACTERIZATIONS</a>
			<ul class="sublist_4" style="${phyDisplay}">
				<c:forEach var="subCharType"
					items="${selectedCharacterizations[physicalType]}">
					<c:if test="${subCharType != 'Composition'}">
						<li>
							<a href="#" class="sublist_4">${subCharType}</a>
							<ul class="sublist_5" style="${phyDisplay}">
								<c:forEach var="leafCharBean"
									items="${charaLeafToCharacterizations[subCharType]}">
									<c:url var="url" value="${leafCharBean.actionName}.do">
										<c:param name="page" value="0" />
										<c:param name="dispatch" value="summaryView" />
										<c:param name="particleId" value="${particleId}" />
										<c:param name="characterizationId" value="${leafCharBean.id}" />
										<c:param name="submitType" value="${leafCharBean.name}" />
									</c:url>
									<li>
										<a href=${url } class="sublist_5"><span
											class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
									</li>
								</c:forEach>
							</ul>
						</li>
					</c:if>
				</c:forEach>
			</ul>
		</li>

		<c:set var="inVitroType" value="In Vitro" />
		<li class="toplist">
			<a href="#" class="subMenuSecondary">IN VITRO CHARACTERIZATIONS</a>
			<ul class="sublist_1" style="${invitroDisplay}">
				<c:forEach var="secondLevelChar"
					items="${selectedCharacterizations[inVitroType]}">
					<li>
						<a href="#" class="sublist_2">${secondLevelChar}</a>
						<ul class="sublist_2" style="${invitroDisplay}">
							<c:forEach var="thirdLevelChar"
								items="${selectedCharacterizations[secondLevelChar]}">
								<li>
									<a href="#" class="sublist_1">${thirdLevelChar}</a>
									<c:choose>
										<c:when
											test="${!empty charaLeafToCharacterizations[thirdLevelChar]}">
											<ul class="sublist_5" style="${invitroDisplay}">
												<c:forEach var="leafCharBean"
													items="${charaLeafToCharacterizations[thirdLevelChar]}">
													<c:url var="url3" value="${leafCharBean.actionName}.do">
														<c:param name="page" value="0" />
														<c:param name="dispatch" value="summaryView" />
														<c:param name="particleId" value="${particleId}" />
														<c:param name="characterizationId"
															value="${leafCharBean.id}" />
														<c:param name="submitType" value="${leafCharBean.name}" />
													</c:url>
													<li>
														<a href="${url3}" class="sublist_5"><span
															class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
													</li>
												</c:forEach>
											</ul>
										</c:when>
										<c:otherwise>
											<ul class="sublist_3" style="${invitroDisplay}">
												<c:forEach var="fourthLevelChar"
													items="${selectedCharacterizations[thirdLevelChar]}">
													<li>
														<a href="#" class="sublist_4">${fourthLevelChar}</a>
														<c:choose>
															<c:when
																test="${!empty charaLeafToCharacterizations[fourthLevelChar]}">
																<ul class="sublist_5" style="${invitroDisplay}">
																	<c:forEach var="leafCharBean"
																		items="${charaLeafToCharacterizations[fourthLevelChar]}">
																		<c:url var="url4"
																			value="${leafCharBean.actionName}.do">
																			<c:param name="page" value="0" />
																			<c:param name="dispatch" value="summaryView" />
																			<c:param name="particleId" value="${particleId}" />
																			<c:param name="characterizationId"
																				value="${leafCharBean.id}" />
																			<c:param name="submitType" value="${leafCharBean.name}" />
																		</c:url>
																		<li>
																			<a href="${url4}" class="sublist_5"><span
																				class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
																		</li>
																	</c:forEach>
																</ul>
															</c:when>
															<c:otherwise>
																<ul class="sublist_4" style="${invitroDisplay}">
																	<c:forEach var="fifthLevelChar"
																		items="${selectedCharacterizations[fourthLevelChar]}">
																		<li>
																			<a href="#" class="sublist_4">${fifthLevelChar}</a>
																			<ul class="sublist_5" style="${invitroDisplay}">
																				<c:forEach var="leafCharBean"
																					items="${charaLeafToCharacterizations[fifthLevelChar]}">
																					<c:url var="url5"
																						value="${leafCharBean.actionName}.do">
																						<c:param name="page" value="0" />
																						<c:param name="dispatch" value="summaryView" />
																						<c:param name="particleId" value="${particleId}" />
																						<c:param name="characterizationId"
																							value="${leafCharBean.id}" />
																						<c:param name="submitType"
																							value="${leafCharBean.name}" />
																					</c:url>
																					<li>
																						<a href=${url5 } class="sublist_5"><span
																							class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
																					</li>
																				</c:forEach>

																			</ul>
																		</li>
																	</c:forEach>
																</ul>
															</c:otherwise>
														</c:choose>
													</li>
												</c:forEach>
											</ul>
										</c:otherwise>
									</c:choose>
								</li>
							</c:forEach>
						</ul>
					</li>
				</c:forEach>
			</ul>
		</li>
	</c:otherwise>
</c:choose>

