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
				displaytype == 'CFU_GM' ||
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
			#cytotoxicity {
				display: block;
			}
			#cytotoxicity ul {
				display: block;
			}
		</style>
	</c:when>
	<c:otherwise>
		<style type="text/css">
			#cytotoxicity {
				display: none;
			}
		</style>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${displaytype == 'Oxidative Stress'}">
		<c:set var="invitroDisplay" value="display: block;" />
		<style type="text/css">
			#oxidativeStress {
				display: block;
			}
			#oxidativeStress ul {
				display: block;
			}
		</style>
	</c:when>
	<c:otherwise>
		<style type="text/css">
			#oxidativeStress {
				display: none;
			}
		</style>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${displaytype == 'Enzyme Induction'}">
		<c:set var="invitroDisplay" value="display: block;" />
		<style type="text/css">
			#enzymeInduction {
				display: block;
			}
			#enzymeInduction ul {
				display: block;
			}
		</style>
	</c:when>
	<c:otherwise>
		<style type="text/css">
			#enzymeInduction {
				display: none;
			}
		</style>
	</c:otherwise>
</c:choose>

<c:set var="physicalType" value="Physical" />
<li class="controlList">
	<a href="#" class="subMenuSecondary">PHYSICAL CHARACTERIZATIONS</a>
	<ul class="sublist_4" style="${phyDisplay}">
		<c:forEach var="subCharType"
			items="${allCharacterizations[physicalType]}">
			<li>
				<jsp:include page="sideParticleCharacterizationMenuButtons.jsp">
					<jsp:param name="charType" value="${subCharType}" />
					<jsp:param name="charTypeStyle" value="sublist_4" />
					<jsp:param name="charTypeLabelStyle" value="titleCell2" />
					<jsp:param name="noDataLabelStyle" value="titleCell2NoData" />
					<jsp:param name="tableStyle" value="charTitle" />
					<jsp:param name="addLinkStyle" value="addCell" />
					<jsp:param name="addAction" value="physicalCharacterization" />
				</jsp:include>
				<c:if test="${!empty charaLeafToCharacterizations[subCharType]}">
					<ul class="sublist_5" style="${phyDisplay}">
						<c:forEach var="leafCharBean"
							items="${charaLeafToCharacterizations[subCharType]}">
							<c:url var="url" value="physicalCharacterization.do">
								<c:param name="page" value="0" />
								<c:param name="dispatch" value="detailView" />
								<c:param name="particleId" value="${particleId}" />
								<c:param name="characterizationId" value="${leafCharBean.id}" />
								<c:param name="submitType" value="${leafCharBean.name}" />
							</c:url>
							<li id="${leafCharBean.id}">
								<a href=${url } class="sublist_5"><span class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
							</li>
						</c:forEach>
					</ul>
				</c:if>
			</li>
		</c:forEach>
	</ul>
</li>
<c:set var="inVitroType" value="In Vitro" />
<li class="controlList">
	<a href="#" class="subMenuSecondary">IN VITRO CHARACTERIZATIONS</a>
	<ul class="sublist_1" style="${invitroDisplay}">
		<c:forEach var="secondLevelChar"
			items="${allCharacterizations[inVitroType]}">
			<li>
				<a href="#" class="sublist_1">${secondLevelChar}</a>
				<ul class="sublist_2" style="${invitroDisplay}">
					<c:forEach var="thirdLevelChar"
						items="${allCharacterizations[secondLevelChar]}">
						<li class="controlList2">
							<c:choose>
								<c:when test="${!empty allCharacterizations[thirdLevelChar]}">
									<table class="subTitleTable">
										<tr class="titleRowVitro">
											<td class="titleCell2Vitro">
												<a href="#" class="sublist_2">${thirdLevelChar}</a>
											</td>
										</tr>
									</table>
								</c:when>
								<c:otherwise>
									<jsp:include page="sideParticleCharacterizationMenuButtons.jsp">
										<jsp:param name="charType" value="${thirdLevelChar}" />
										<jsp:param name="charTypeStyle" value="sublist_2" />
										<jsp:param name="charTypeLabelStyle" value="titleCell2Vitro" />
										<jsp:param name="noDataLabelStyle"
											value="titleCell2VitroNoData" />
										<jsp:param name="tableStyle" value="subTitleTable" />
										<jsp:param name="addLinkStyle" value="addCellVitro" />
										<jsp:param name="addAction" value="inVitroCharacterization" />
									</jsp:include>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when
									test="${!empty charaLeafToCharacterizations[thirdLevelChar]}">
									<ul class="sublist_5_control"
										id="${charaLeafActionName[thirdLevelChar]}">
										<c:forEach var="leafCharBean"
											items="${charaLeafToCharacterizations[thirdLevelChar]}">
											<c:url var="url3" value="inVitroCharacterization.do">
												<c:param name="page" value="0" />
												<c:param name="dispatch" value="detailView" />
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
										<ul class="sublist_3_control"
											id="${charaLeafActionName[thirdLevelChar]}">
											<c:forEach var="fourthLevelChar"
												items="${allCharacterizations[thirdLevelChar]}">
												<li>
													<c:choose>
														<c:when
															test="${!empty allCharacterizations[fourthLevelChar]}">
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
																<jsp:param name="charTypeLabelStyle" value="titleCell3" />
																<jsp:param name="noDataLabelStyle"
																	value="titleCell3NoData" />
																<jsp:param name="tableStyle" value="charTitle" />
																<jsp:param name="addLinkStyle" value="addCell" />
																<jsp:param name="addAction"
																	value="inVitroCharacterization" />
															</jsp:include>
														</c:otherwise>
													</c:choose>
													<c:choose>
														<c:when
															test="${!empty charaLeafToCharacterizations[fourthLevelChar]}">
															<ul class="sublist_5">
																<c:forEach var="leafCharBean"
																	items="${charaLeafToCharacterizations[fourthLevelChar]}">
																	<c:url var="url4" value="inVitroCharacterization.do">
																		<c:param name="page" value="0" />
																		<c:param name="dispatch" value="detailView" />
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
																				value="inVitroCharacterization.do">
																				<c:param name="particleId" value="${particleId}" />
																				<c:param name="submitType" value="${fifthLevelChar}" />
																				<c:param name="page" value="0" />
																				<c:param name="dispatch" value="setup" />
																			</c:url>
																			<jsp:include
																				page="sideParticleCharacterizationMenuButtons.jsp">
																				<jsp:param name="charType" value="${fifthLevelChar}" />
																				<jsp:param name="charTypeStyle" value="sublist_4" />
																				<jsp:param name="charTypeLabelStyle"
																					value="titleCell4" />
																				<jsp:param name="noDataLabelStyle"
																					value="titleCell4NoData" />
																				<jsp:param name="tableStyle" value="charTitle" />
																				<jsp:param name="addLinkStyle" value="addCellVitro" />
																				<jsp:param name="addAction"
																					value="inVitroCharacterization" />
																			</jsp:include>
																			<c:if
																				test="${!empty charaLeafToCharacterizations[fifthLevelChar]}">
																				<ul class="sublist_5" style="${invitroDisplay}">
																					<c:forEach var="leafCharBean"
																						items="${charaLeafToCharacterizations[fifthLevelChar]}">
																						<c:url var="url5"
																							value="inVitroCharacterization.do">
																							<c:param name="page" value="0" />
																							<c:param name="dispatch" value="detailView" />
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
