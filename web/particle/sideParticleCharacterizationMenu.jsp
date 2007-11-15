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
	<c:when test="${displaytype == 'Composition'}">
		<c:set var="compDisplay" value="display: block;" />
	</c:when>
	<c:otherwise>
		<c:set var="compDisplay" value="display: none;" />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${canCreateNanoparticle eq 'true'}">
		<c:set var="physicalType" value="Physical" />
		<li class="toplist">
			<a href="#" class="subMenuSecondary">COMPOSITION</a>
			<ul class="sublist_5" style="${compDisplay}">
				<c:forEach var="subCharType"
					items="${allCharacterizations[physicalType]}">
					<c:if test="${subCharType == 'Composition'}">
						<c:forEach var="leafCharBean"
							items="${nameToCharacterizations[subCharType]}">
							<c:url var="url" value="${leafCharBean.actionName}.do">
								<c:param name="page" value="0" />
								<c:param name="dispatch" value="${dispatchValue}" />
								<c:param name="particleName" value="${particleName}" />
								<c:param name="particleType" value="${particleType}" />
								<c:param name="particleSource" value="${particleSource}" />
								<c:param name="characterizationId" value="${leafCharBean.id}" />
								<c:param name="submitType" value="${subCharType}" />
								<c:param name="actionName" value="${leafCharBean.actionName}" />
								<c:param name="charName" value="${leafCharBean.name}" />
							</c:url>
							<li>
								<a href=${url } class="sublist_5"><span class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
							</li>
						</c:forEach>
						<c:url var="submitUrl" value="composition.do">
							<c:param name="particleName" value="${particleName}" />
							<c:param name="particleType" value="${particleType}" />
							<c:param name="particleSource" value="${particleSource}" />
							<c:param name="submitType" value="${physicalType}" />
							<c:param name="page" value="0" />
							<c:param name="dispatch" value="setup" />
							<c:param name="actionName" value="${leafCharBean.actionName}" />
							<c:param name="charName" value="${leafCharBean.name}" />
						</c:url>
						<li>
							<a href="${submitUrl}">Enter ${subCharType}</a>
						</li>
					</c:if>
				</c:forEach>
			</ul>
		</li>

		<li class="toplist">
			<a href="#" class="subMenuSecondary">PHYSICAL CHARACTERIZATIONS</a>
			<ul class="sublist_4" style="${phyDisplay}">
				<c:forEach var="subCharType"
					items="${allCharacterizations[physicalType]}">
					<c:if test="${subCharType != 'Composition'}">
						<li>
							<a href="#" class="sublist_4">${subCharType}</a>
							<ul class="sublist_5" style="${phyDisplay}">
								<c:forEach var="leafCharBean"
									items="${nameToCharacterizations[subCharType]}">
									<c:url var="url" value="${leafCharBean.actionName}.do">
										<c:param name="page" value="0" />
										<c:param name="dispatch" value="${dispatchValue}" />
										<c:param name="particleName" value="${particleName}" />
										<c:param name="particleType" value="${particleType}" />
										<c:param name="particleSource" value="${particleSource}" />
										<c:param name="characterizationId" value="${leafCharBean.id}" />
										<c:param name="submitType" value="${subCharType}" />
										<c:param name="actionName" value="${leafCharBean.actionName}" />
										<c:param name="charName" value="${leafCharBean.name}" />
									</c:url>
									<li>
										<a href=${url } class="sublist_5"><span
											class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
									</li>
									<c:set var="actionname" value="${leafCharBean.actionName}" />
									<c:set var="charname" value="${leafCharBean.name}" />
								</c:forEach>
								<c:url var="submitUrl" value="${charaLeafActionName[subCharType]}.do">
									<c:param name="particleName" value="${particleName}" />
									<c:param name="particleType" value="${particleType}" />
									<c:param name="particleSource" value="${particleSource}" />
									<c:param name="submitType" value="${physicalType}" />
									<c:param name="page" value="0" />
									<c:param name="dispatch" value="setup" />
									<c:param name="actionName" value="${charaLeafActionName[subCharType]}" />
									<c:param name="charName" value="${subCharType}" />
								</c:url>
								<li>
									<a href="${submitUrl}">Enter ${subCharType}</a>
								</li>
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
					items="${allCharacterizations[inVitroType]}">
					<li>
						<a href="#" class="sublist_1">${secondLevelChar}</a>
						<ul class="sublist_2" style="${invitroDisplay}">
							<c:forEach var="thirdLevelChar"
								items="${allCharacterizations[secondLevelChar]}">
								<li>
									<a href="#" class="sublist_2">${thirdLevelChar}</a>
									<c:choose>
										<c:when test="${!empty nameToCharacterizations[thirdLevelChar] ||
														empty allCharacterizations[thirdLevelChar] }">
											<ul class="sublist_5" style="${invitroDisplay}">
												<c:forEach var="leafCharBean"
													items="${nameToCharacterizations[thirdLevelChar]}">
													<c:url var="url3" value="${leafCharBean.actionName}.do">
														<c:param name="page" value="0" />
														<c:param name="dispatch" value="${dispatchValue}" />
														<c:param name="particleName" value="${particleName}" />
														<c:param name="particleType" value="${particleType}" />
														<c:param name="particleSource" value="${particleSource}" />
														<c:param name="characterizationId"
															value="${leafCharBean.id}" />
														<c:param name="submitType" value="${thirdLevelChar}" />
														<c:param name="actionName"
															value="${leafCharBean.actionName}" />
														<c:param name="charName" value="${leafCharBean.name}" />
													</c:url>
													<li>
														<a href="${url3}" class="sublist_5"><span
															class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
													</li>
												</c:forEach>
												<c:if test="${empty allCharacterizations[thirdLevelChar]}">
													<c:url var="submitUrl" value="${charaLeafActionName[thirdLevelChar]}.do">
														<c:param name="particleName" value="${particleName}" />
														<c:param name="particleType" value="${particleType}" />
														<c:param name="particleSource" value="${particleSource}" />
														<c:param name="submitType" value="${secondLevelChar}" />
														<c:param name="page" value="0" />
														<c:param name="dispatch" value="setup" />
														<c:param name="actionName" value="${charaLeafActionName[thirdLevelChar]}" />
														<c:param name="charName" value="${thirdLevelChar}" />
													</c:url>
													<li>
														<a href="${submitUrl}">Enter ${thirdLevelChar}</a>
													</li>
												</c:if>
											</ul>
										</c:when>
										<c:otherwise>
											<ul class="sublist_3" style="${invitroDisplay}">
												<c:forEach var="fourthLevelChar"
													items="${allCharacterizations[thirdLevelChar]}">
													<li>
														<a href="#" class="sublist_4">${fourthLevelChar}</a>
														<c:choose>
															<c:when test="${!empty nameToCharacterizations[fourthLevelChar] ||
																			empty allCharacterizations[fourthLevelChar]}">
																<ul class="sublist_5" style="${invitroDisplay}">
																	<c:forEach var="leafCharBean"
																		items="${nameToCharacterizations[fourthLevelChar]}">
																		<c:url var="url4"
																			value="${leafCharBean.actionName}.do">
																			<c:param name="page" value="0" />
																			<c:param name="dispatch" value="${dispatchValue}" />
																			<c:param name="particleName" value="${particleName}" />
																			<c:param name="particleType" value="${particleType}" />
																			<c:param name="particleSource"
																				value="${particleSource}" />
																			<c:param name="characterizationId"
																				value="${leafCharBean.id}" />
																			<c:param name="submitType" value="${thirdLevelChar}" />
																			<c:param name="actionName"
																				value="${leafCharBean.actionName}" />
																			<c:param name="charName" value="${leafCharBean.name}" />
																		</c:url>
																		<li>
																			<a href="${url4}" class="sublist_5"><span
																				class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
																		</li>
																	</c:forEach>
																	<c:if test="${empty allCharacterizations[fourthLevelChar]}">
																		<c:url var="submitUrl" value="${charaLeafActionName[fourthLevelChar]}.do">
																			<c:param name="particleName" value="${particleName}" />
																			<c:param name="particleType" value="${particleType}" />
																			<c:param name="particleSource" value="${particleSource}" />
																			<c:param name="submitType" value="${secondLevelChar}" />
																			<c:param name="page" value="0" />
																			<c:param name="dispatch" value="setup" />
																			<c:param name="actionName" value="${charaLeafActionName[fourthLevelChar]}" />
																			<c:param name="charName" value="${fourthLevelChar}" />
																		</c:url>
																		<li>
																			<a href="${submitUrl}">Enter ${fourthLevelChar}</a>
																		</li>
																	</c:if>
																</ul>
															</c:when>
															<c:otherwise>
																<ul class="sublist_4" style="${invitroDisplay}">
																	<c:forEach var="fifthLevelChar"
																		items="${allCharacterizations[fourthLevelChar]}">
																		<li>
																			<a href="#" class="sublist_4">${fifthLevelChar}</a>
																			<ul class="sublist_5" style="${invitroDisplay}">
																				<c:forEach var="leafCharBean"
																					items="${nameToCharacterizations[fifthLevelChar]}">
																					<c:url var="url5"
																						value="${leafCharBean.actionName}.do">
																						<c:param name="page" value="0" />
																						<c:param name="dispatch" value="${dispatchValue}" />
																						<c:param name="particleName"
																							value="${particleName}" />
																						<c:param name="particleType"
																							value="${particleType}" />
																						<c:param name="particleSource"
																							value="${particleSource}" />
																						<c:param name="characterizationId"
																							value="${leafCharBean.id}" />
																						<c:param name="submitType"
																							value="${thirdLevelChar}" />
																						<c:param name="actionName"
																							value="${leafCharBean.actionName}" />
																						<c:param name="charName"
																							value="${leafCharBean.name}" />
																					</c:url>
																					<li>
																						<a href=${url5} class="sublist_5"><span
																							class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
																					</li>
																				</c:forEach>
																				<c:url var="submitUrl" value="${charaLeafActionName[fifthLevelChar]}.do">
																					<c:param name="particleName" value="${particleName}" />
																					<c:param name="particleType" value="${particleType}" />
																					<c:param name="particleSource" value="${particleSource}" />
																					<c:param name="submitType" value="${secondLevelChar}" />
																					<c:param name="page" value="0" />
																					<c:param name="dispatch" value="setup" />
																					<c:param name="actionName" value="${charaLeafActionName[fifthLevelChar]}" />
																					<c:param name="charName" value="${fifthLevelChar}" />
																				</c:url>
																				<li>
																					<a href="${submitUrl}">Enter ${fifthLevelChar}</a>
																				</li>
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

	</c:when>
	<c:otherwise>
		<c:set var="physicalType" value="Physical" />
		<li class="toplist">
			<a href="#" class="subMenuSecondary">COMPOSITION</a>
			<ul class="sublist_5" style="${compDisplay}">
				<c:forEach var="subCharType"
					items="${selectedCharacterizations[physicalType]}">
					<c:if test="${subCharType == 'Composition'}">
						<c:forEach var="leafCharBean"
							items="${nameToCharacterizations[subCharType]}">
							<c:url var="url" value="${leafCharBean.actionName}.do">
								<c:param name="page" value="0" />
								<c:param name="dispatch" value="${dispatchValue}" />
								<c:param name="particleName" value="${particleName}" />
								<c:param name="particleType" value="${particleType}" />
								<c:param name="particleSource" value="${particleSource}" />
								<c:param name="characterizationId" value="${leafCharBean.id}" />
								<c:param name="submitType" value="${subCharType}" />
								<c:param name="actionName" value="${leafCharBean.actionName}" />
								<c:param name="charName" value="${leafCharBean.name}" />
							</c:url>
							<li>
								<a href=${url } class="sublist_5"><span class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
							</li>
						</c:forEach>
					</c:if>
				</c:forEach>
			</ul>
		</li>

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
									items="${nameToCharacterizations[subCharType]}">
									<c:url var="url" value="${leafCharBean.actionName}.do">
										<c:param name="page" value="0" />
										<c:param name="dispatch" value="${dispatchValue}" />
										<c:param name="particleName" value="${particleName}" />
										<c:param name="particleType" value="${particleType}" />
										<c:param name="particleSource" value="${particleSource}" />
										<c:param name="characterizationId" value="${leafCharBean.id}" />
										<c:param name="submitType" value="${subCharType}" />
										<c:param name="actionName" value="${leafCharBean.actionName}" />
										<c:param name="charName" value="${leafCharBean.name}" />
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
											test="${!empty nameToCharacterizations[thirdLevelChar]}">
											<ul class="sublist_5" style="${invitroDisplay}">
												<c:forEach var="leafCharBean"
													items="${nameToCharacterizations[thirdLevelChar]}">
													<c:url var="url3" value="${leafCharBean.actionName}.do">
														<c:param name="page" value="0" />
														<c:param name="dispatch" value="${dispatchValue}" />
														<c:param name="particleName" value="${particleName}" />
														<c:param name="particleType" value="${particleType}" />
														<c:param name="particleSource" value="${particleSource}" />
														<c:param name="characterizationId"
															value="${leafCharBean.id}" />
														<c:param name="submitType" value="${thirdLevelChar}" />
														<c:param name="actionName"
															value="${leafCharBean.actionName}" />
														<c:param name="charName" value="${leafCharBean.name}" />
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
																test="${!empty nameToCharacterizations[fourthLevelChar]}">
																<ul class="sublist_5" style="${invitroDisplay}">
																	<c:forEach var="leafCharBean"
																		items="${nameToCharacterizations[fourthLevelChar]}">
																		<c:url var="url4"
																			value="${leafCharBean.actionName}.do">
																			<c:param name="page" value="0" />
																			<c:param name="dispatch" value="${dispatchValue}" />
																			<c:param name="particleName" value="${particleName}" />
																			<c:param name="particleType" value="${particleType}" />
																			<c:param name="particleSource"
																				value="${particleSource}" />
																			<c:param name="characterizationId"
																				value="${leafCharBean.id}" />
																			<c:param name="submitType" value="${thirdLevelChar}" />
																			<c:param name="actionName"
																				value="${leafCharBean.actionName}" />
																			<c:param name="charName" value="${leafCharBean.name}" />
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
																					items="${nameToCharacterizations[fifthLevelChar]}">
																					<c:url var="url5"
																						value="${leafCharBean.actionName}.do">
																						<c:param name="page" value="0" />
																						<c:param name="dispatch" value="${dispatchValue}" />
																						<c:param name="particleName"
																							value="${particleName}" />
																						<c:param name="particleType"
																							value="${particleType}" />
																						<c:param name="particleSource"
																							value="${particleSource}" />
																						<c:param name="characterizationId"
																							value="${leafCharBean.id}" />
																						<c:param name="submitType"
																							value="${thirdLevelChar}" />
																						<c:param name="actionName"
																							value="${leafCharBean.actionName}" />
																						<c:param name="charName"
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

