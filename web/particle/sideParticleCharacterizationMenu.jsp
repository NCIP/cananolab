<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:choose>
	<c:when
		test="${displaytype == 'Physical' ||
			  displaytype == 'Molecular Weight' ||
			  displaytype == 'Physical State' ||
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
			    displaytype == 'Toxicity'}">
		<c:set var="invitroDisplay" value="display: block;" />
	</c:when>
	<c:otherwise>
		<c:set var="invitroDisplay" value="display: none;" />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when
		test="${displaytype == 'Coagulation' ||
				displaytype == 'Hemolysis' ||
				displaytype == 'Plasma Protein Binding' ||
				displaytype == 'CFU GM' ||
				displaytype == 'Chemotaxis' ||
				displaytype == 'Complement Activation' ||
				displaytype == 'Cytokine Induction' ||
				displaytype == 'Leukocyte Proliferation' ||
				displaytype == 'NK Cell Cytotoxic Activity' ||
				displaytype == 'Oxidative Burst' ||
				displaytype == 'Phagocytosis' ||
				displaytype == 'Platelet Aggregation'}">
		<c:set var="invitroDisplay" value="display: block;" />
		<style type="text/css">
#immunotoxicity {
	display: block;
}

#immunotoxicity ul {
	display: block;
}
</style>
	</c:when>
	<c:otherwise>
		<style type="text/css">
#immunotoxicity {
	display: none;
}
</style>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when
		test="${displaytype == 'Caspase 3 Activation' ||
				displaytype == 'Cell Viability'}">
		<c:set var="invitroDisplay" value="display: block;" />
		<style type="text/css">
#Cytotoxicity {
	display: block;
}

#Cytotoxicity ul {
	display: block;
}
</style>
	</c:when>
	<c:otherwise>
		<style type="text/css">
#Cytotoxicity {
	display: none;
}
</style>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${displaytype == 'Oxidative Stress'}">
		<c:set var="invitroDisplay" value="display: block;" />
		<style type="text/css">
#OxidativeStress {
	display: block;
}

#OxidativeStress ul {
	display: block;
}
</style>
	</c:when>
	<c:otherwise>
		<style type="text/css">
#OxidativeStress {
	display: none;
}
</style>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${displaytype == 'Enzyme Induction'}">
		<c:set var="invitroDisplay" value="display: block;" />
		<style type="text/css">
#EnzymeInduction {
	display: block;
}

#EnzymeInduction ul {
	display: block;
}
</style>
	</c:when>
	<c:otherwise>
		<style type="text/css">
#EnzymeInduction {
	display: none;
}
</style>
	</c:otherwise>
</c:choose>
<c:set var="physicalType" value="Physical Characterization" />
<c:choose>
	<c:when
		test="${hasPhysicalData eq 'true' || (canCreateNanoparticle eq 'true' && location eq 'local')}">

		<li class="controlList">
			<a href="#" class="subMenuSecondary">PHYSICAL CHARACTERIZATIONS</a>
			<ul class="sublist_4" style="${phyDisplay }">
				<c:forEach var="physicalChara"
					items="${physicalTypes[physicalType]}">
					<%--			<c:if test="${canCreateNanoparticle eq 'true' && location eq 'local' || !empty particleDataTree[physicalChara]}" >--%>
					<li>
						<jsp:include page="sideParticleCharacterizationMenuButtons.jsp">
							<jsp:param name="charType" value="${physicalChara}" />
							<jsp:param name="charTypeStyle" value="sublist_4" />
							<jsp:param name="charTypeLabelStyle" value="titleCell2" />
							<jsp:param name="noDataLabelStyle" value="titleCell2NoData" />
							<jsp:param name="tableStyle" value="charTitle" />
							<jsp:param name="addLinkStyle" value="addCell" />
							<jsp:param name="addAction" value="physicalCharacterization" />
							<jsp:param name="location" value="${location}" />
						</jsp:include>
						<c:if test="${!empty particleDataTree[physicalChara]}">
							<ul class="sublist_5" style="${phyDisplay }">
								<c:forEach var="leafCharBean"
									items="${particleDataTree[physicalChara]}">
									<c:url var="url" value="${leafCharBean.dataLink}.do">
										<c:param name="page" value="0" />
										<c:param name="dispatch" value="detailView" />
										<c:param name="particleId" value="${particleId}" />
										<c:param name="dataId" value="${leafCharBean.dataId}" />
										<c:param name="dataClassName"
											value="${leafCharBean.dataClassName}" />
										<c:param name="submitType"
											value="${leafCharBean.dataDisplayType}" />
										<c:param name="location" value="${location}" />
									</c:url>
									<li id="${leafCharBean.dataId}">
										<c:choose>
											<c:when test="${leafCharBean.viewColor != null}">
												<c:set var="viewTitleDisplay"
													value="color: ${leafCharBean.viewColor};" />
												<a href="${url}" class="sublist_5" style=""><span
													class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
											</c:when>
											<c:otherwise>
												<a href="${url}" class="sublist_5"><span
													class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
											</c:otherwise>
										</c:choose>
									</li>
								</c:forEach>
							</ul>
						</c:if>
					</li>
					<%--			</c:if>--%>
				</c:forEach>
			</ul>
		</li>
	</c:when>
	<c:otherwise>
		<li class="nodatali">
			PHYSICAL CHARACTERIZATIONS
		</li>
	</c:otherwise>
</c:choose>

<c:set var="inVitroType" value="In Vitro Characterization" />
<c:choose>
	<c:when
		test="${hasInVitroData eq 'true' || (canCreateNanoparticle eq 'true' && location eq 'local')}">
		<li class="controlList">
			<a href="#" class="subMenuSecondary">IN VITRO CHARACTERIZATIONS</a>
			<ul class="sublist_1" style="${invitroDisplay }">
				<c:forEach var="secondLevelChar"
					items="${invitroTypes[inVitroType]}">
					<li>
						<a href="#" class="sublist_1">${secondLevelChar}</a>
						<ul class="sublist_2" style="${invitroDisplay }">
							<c:forEach var="thirdLevelChar"
								items="${invitroTypes[secondLevelChar]}">
								<li class="controlList2">
									<c:choose>
										<c:when test="${!empty invitroTypes[thirdLevelChar]}">
											<table class="subTitleTable">
												<tr class="titleRowVitro">
													<td class="titleCell2Vitro">
														<a href="#" class="sublist_2"><img
																src="images/subMenuArrow.gif" border="0" />&nbsp;${thirdLevelChar}</a>
													</td>
													<td width="100%" class="titleCell2Vitro">
														&nbsp;
													</td>
												</tr>
											</table>
										</c:when>
										<c:otherwise>
											<jsp:include
												page="sideParticleCharacterizationMenuButtons.jsp">
												<jsp:param name="charType" value="${thirdLevelChar}" />
												<jsp:param name="charTypeStyle" value="sublist_2" />
												<jsp:param name="charTypeLabelStyle"
													value="titleCell2VitroButton" />
												<jsp:param name="noDataLabelStyle"
													value="titleCell2VitroNoData" />
												<jsp:param name="tableStyle" value="subTitleTable" />
												<jsp:param name="addLinkStyle" value="addCellVitro" />
												<jsp:param name="addAction" value="invitroCharacterization" />
												<jsp:param name="location" value="${location}" />
											</jsp:include>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${!empty particleDataTree[thirdLevelChar]}">
											<ul class="sublist_5_control"
												id="${charaClassNameMap[thirdLevelChar]}">
												<c:forEach var="leafCharBean"
													items="${particleDataTree[thirdLevelChar]}">
													<c:url var="url3" value="${leafCharBean.dataLink}.do">
														<c:param name="page" value="0" />
														<c:param name="dispatch" value="detailView" />
														<c:param name="particleId" value="${particleId}" />
														<c:param name="dataId" value="${leafCharBean.dataId}" />
														<c:param name="dataClassName"
															value="${leafCharBean.dataClassName}" />
														<c:param name="submitType"
															value="${leafCharBean.dataDisplayType}" />
														<c:param name="location" value="${location}" />
													</c:url>
													<li>
														<c:choose>
															<c:when test="${leafCharBean.viewColor != null}">
																<c:set var="viewTitleDisplay"
																	value="color: ${leafCharBean.viewColor};" />
																<a href="${url3}" class="sublist_5" style=""> <span
																	class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
															</c:when>
															<c:otherwise>
																<a href="${url3}" class="sublist_5"><span
																	class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
															</c:otherwise>
														</c:choose>
													</li>
												</c:forEach>
											</ul>
										</c:when>
										<c:otherwise>
											<c:if test="${!empty invitroTypes[thirdLevelChar]}">
												<ul class="sublist_3_control"
													id="${charaClassNameMap[thirdLevelChar]}">
													<c:forEach var="fourthLevelChar"
														items="${invitroTypes[thirdLevelChar]}">
														<li>
															<c:choose>
																<c:when test="${!empty invitroTypes[fourthLevelChar]}">
																	<table class="charTitle">
																		<tr class="titleRow">
																			<td class="titleCell3">
																				<a href="#" class="sublist_4">${fourthLevelChar}</a>
																			</td>
																		</tr>
																	</table>
																</c:when>
																<c:otherwise>
																	<jsp:include
																		page="sideParticleCharacterizationMenuButtons.jsp">
																		<jsp:param name="charType" value="${fourthLevelChar}" />
																		<jsp:param name="charTypeStyle" value="sublist_4" />
																		<jsp:param name="charTypeLabelStyle"
																			value="titleCell3" />
																		<jsp:param name="noDataLabelStyle"
																			value="titleCell3NoData" />
																		<jsp:param name="tableStyle" value="charTitle" />
																		<jsp:param name="addLinkStyle" value="addCell" />
																		<jsp:param name="addAction"
																			value="invitroCharacterization" />
																		<jsp:param name="location" value="${location}" />
																	</jsp:include>
																</c:otherwise>
															</c:choose>
															<c:choose>
																<c:when
																	test="${!empty particleDataTree[fourthLevelChar]}">
																	<ul class="sublist_5">
																		<c:forEach var="leafCharBean"
																			items="${particleDataTree[fourthLevelChar]}">
																			<c:url var="url4" value="${leafCharBean.dataLink}.do">
																				<c:param name="page" value="0" />
																				<c:param name="dispatch" value="detailView" />
																				<c:param name="particleId" value="${particleId}" />
																				<c:param name="dataId"
																					value="${leafCharBean.dataId}" />
																				<c:param name="dataClassName"
																					value="${leafCharBean.dataClassName}" />
																				<c:param name="submitType"
																					value="${leafCharBean.dataDisplayType}" />
																				<c:param name="location" value="${location}" />
																			</c:url>
																			<li>
																				<c:choose>
																					<c:when test="${leafCharBean.viewColor != null}">
																						<c:set var="viewTitleDisplay"
																							value="color: ${leafCharBean.viewColor};" />
																						<a href="${url4}" class="sublist_5" style="">
																							<span class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
																					</c:when>
																					<c:otherwise>
																						<a href="${url4}" class="sublist_5"><span
																							class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
																					</c:otherwise>
																				</c:choose>
																			</li>
																		</c:forEach>
																	</ul>
																</c:when>
																<c:otherwise>
																	<c:if test="${!empty invitroTypes[fourthLevelChar]}">
																		<ul class="sublist_4" style="">
																			<c:forEach var="fifthLevelChar"
																				items="${invitroTypes[fourthLevelChar]}">
																				<li>
																					<c:url var="submitUrl"
																						value="${leafCharBean.dataLink}.do">
																						<c:param name="particleId" value="${particleId}" />
																						<c:param name="submitType"
																							value="${fifthLevelChar}" />
																						<c:param name="page" value="0" />
																						<c:param name="dispatch" value="setup" />
																						<c:param name="location" value="${location}" />
																					</c:url>
																					<jsp:include
																						page="sideParticleCharacterizationMenuButtons.jsp">
																						<jsp:param name="charType"
																							value="${fifthLevelChar}" />
																						<jsp:param name="charTypeStyle" value="sublist_4" />
																						<jsp:param name="charTypeLabelStyle"
																							value="titleCell4" />
																						<jsp:param name="noDataLabelStyle"
																							value="titleCell4NoData" />
																						<jsp:param name="tableStyle" value="charTitle" />
																						<jsp:param name="addLinkStyle"
																							value="addCellVitro" />
																						<jsp:param name="addAction"
																							value="invitroCharacterization" />
																						<jsp:param name="location" value="${location}" />
																					</jsp:include>
																					<c:if
																						test="${!empty particleDataTree[fifthLevelChar]}">
																						<ul class="sublist_5" style="${invitroDisplay }">
																							<c:forEach var="leafCharBean"
																								items="${particleDataTree[fifthLevelChar]}">
																								<c:url var="url5"
																									value="${leafCharBean.dataLink}.do">
																									<c:param name="page" value="0" />
																									<c:param name="dispatch" value="detailView" />
																									<c:param name="particleId"
																										value="${particleId}" />
																									<c:param name="dataId"
																										value="${leafCharBean.dataId}" />
																									<c:param name="dataClassName"
																										value="${leafCharBean.dataClassName}" />
																									<c:param name="submitType"
																										value="${leafCharBean.dataDisplayType}" />
																									<c:param name="location" value="${location}" />
																								</c:url>
																								<li>
																									<c:choose>
																										<c:when
																											test="${leafCharBean.viewColor != null}">
																											<c:set var="viewTitleDisplay"
																												value="color: ${leafCharBean.viewColor};" />
																											<a href="${url5}" class="sublist_5" style="${invitroDisplay }">
																												<span class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
																										</c:when>
																										<c:otherwise>
																											<a href="${url5}" class="sublist_5"><span
																												class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
																										</c:otherwise>
																									</c:choose>
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
		<li class="nodatali">
			IN VITRO CHARACTERIZATIONS
		</li>
	</c:otherwise>
</c:choose>
