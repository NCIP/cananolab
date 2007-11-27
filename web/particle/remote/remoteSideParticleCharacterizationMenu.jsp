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
	<c:when test="${displaytype == 'In Vitro' ||
			    displaytype == 'Toxicity'}">
		<c:set var="invitroDisplay" value="display: block;" />
	</c:when>
	<c:otherwise>
		<c:set var="invitroDisplay" value="display: none;" />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${displaytype == 'Coagulation' ||
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
	<c:when test="${displaytype == 'Caspase 3 Activation' ||
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
		<style type="text/css" >
			#oxidativeStress {
				display: block;
			}
			#oxidativeStress ul {
				display: block;
			}
		</style>
	</c:when>
	<c:otherwise>
		<style type="text/css" >
			#oxidativeStress {
				display: none;
			}
		</style>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${displaytype == 'Enzyme Induction'}">
		<c:set var="invitroDisplay" value="display: block;" />
		<style type="text/css" >
			#enzymeInduction {
				display: block;
			}
			#enzymeInduction ul {
				display: block;
			}
		</style>
	</c:when>
	<c:otherwise>
		<style type="text/css" >
			#enzymeInduction {
				display: none;
			}
		</style>
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

<c:set var="physicalType" value="Physical" />
		<li class="controlList">
			<a href="#" class="subMenuSecondary">COMPOSITION</a>
			<ul class="sublist_5" style="${compDisplay}">
				<c:forEach var="subCharType"
					items="${remoteSelectedCharacterizations[physicalType]}">
					<c:if test="${subCharType == 'Composition'}">
						<c:forEach var="leafCharBean"
							items="${remoteCharaLeafToCharacterizations[subCharType]}">
							<c:url var="url" value="remoteNanoparticleComposition.do">
								<c:param name="page" value="0" />
								<c:param name="dispatch" value="view" />
								<c:param name="particleName" value="${particleName}" />
								<c:param name="particleType" value="${particleType}" />
								<c:param name="characterizationId" value="${leafCharBean.id}" />
								<c:param name="submitType" value="${subCharType}" />
								<c:param name="actionName" value="${leafCharBean.actionName}" />
								<c:param name="gridNodeHost" value="${gridNodeHost}" />
							</c:url>
							<li>
								<a href=${url } class="sublist_5"><span class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
							</li>
						</c:forEach>
					</c:if>
				</c:forEach>
			</ul>
		</li>
		
<c:url var="url" value="underConstruction.do">
	<c:param name="submitType" value="Physical" />
</c:url>
		<li class="controlList">
			<a href="#" class="subMenuSecondary">PHYSICAL CHARACTERIZATIONS</a>
			<ul class="sublist_4" style="${phyDisplay}">
				<c:forEach var="subCharType"
					items="${remoteSelectedCharacterizations[physicalType]}">
					<c:if test="${subCharType != 'Composition'}">
						<li>
							<a href="#" class="sublist_4">${subCharType}</a>
							<ul class="sublist_5" style="${phyDisplay}">
								<c:forEach var="leafCharBean"
									items="${remoteCharaLeafToCharacterizations[subCharType]}">
									
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
		<li class="controlList">
			<a href="#" class="subMenuSecondary">IN VITRO CHARACTERIZATIONS</a>
			<ul class="sublist_1" style="${invitroDisplay}">
				<c:forEach var="secondLevelChar"
					items="${remoteSelectedCharacterizations[inVitroType]}">
					<li>
						<a href="#" class="sublist_2">${secondLevelChar}</a>
						<ul class="sublist_2" style="${invitroDisplay}">
							<c:forEach var="thirdLevelChar"
								items="${remoteSelectedCharacterizations[secondLevelChar]}">
								<li class="controlList2">
									<a href="#" class="sublist_1">${thirdLevelChar}</a>
									<c:choose>
										<c:when
											test="${!empty remoteCharaLeafToCharacterizations[thirdLevelChar]}">
											<ul class="sublist_5_control" id="${remoteCharaActionName[thirdLevelChar]}">
												<c:forEach var="leafCharBean"
													items="${remoteCharaLeafToCharacterizations[thirdLevelChar]}">
													<li>
														<c:url var="url" value="underConstruction.do">
															<c:param name="submitType" value="${thirdLevelChar}" />
														</c:url>
														<a href="${url}" class="sublist_5"><span
															class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
													</li>
												</c:forEach>
											</ul>
										</c:when>
										<c:otherwise>
											<ul class="sublist_3_control" id="${remoteCharaActionName[thirdLevelChar]}">
												<c:forEach var="fourthLevelChar"
													items="${remoteSelectedCharacterizations[thirdLevelChar]}">
													<li>
														<a href="#" class="sublist_4">${fourthLevelChar}</a>
														<c:choose>
															<c:when
																test="${!empty remoteCharaLeafToCharacterizations[fourthLevelChar]}">
																<ul class="sublist_5">
																	<c:forEach var="leafCharBean"
																		items="${remoteCharaLeafToCharacterizations[fourthLevelChar]}">
																		
																		<li>
																			<c:url var="url" value="underConstruction.do">
																				<c:param name="submitType" value="${fourthLevelChar}" />
																			</c:url>
																			<a href="${url}" class="sublist_5"><span
																				class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
																		</li>
																	</c:forEach>
																</ul>
															</c:when>
															<c:otherwise>
																<ul class="sublist_4" style="${invitroDisplay}">
																	<c:forEach var="fifthLevelChar"
																		items="${remoteSelectedCharacterizations[fourthLevelChar]}">
																		<li>
																			<a href="#" class="sublist_4">${fifthLevelChar}</a>
																			<ul class="sublist_5" style="${invitroDisplay}">
																				<c:forEach var="leafCharBean"
																					items="${remoteCharaLeafToCharacterizations[fifthLevelChar]}">
																					<li>
																						<c:url var="url" value="underConstruction.do">
																							<c:param name="submitType" value="${fifthLevelChar}" />
																						</c:url>
																						<a href="${url}" class="sublist_5"><span
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
		
