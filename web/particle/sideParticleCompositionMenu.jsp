<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:choose>
	<c:when
		test="${displaytype == 'Composition' ||
				displaytype == 'Nanoparticle Entity' ||
				displaytype == 'Functionalizing Entity' ||
				displaytype == 'Chemical Association' ||
				displaytype == 'Composition File'}">
		<c:set var="compDisplay" value="display: block;" />
	</c:when>
	<c:otherwise>
		<c:set var="compDisplay" value="display: none;" />
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when
		test="${hasCompositionData eq 'true' || canCreateNanoparticle eq 'true'}">
		<c:set var="compositionType" value="Composition" />
		<li class="controlList">
			<a href="#" class="subMenuSecondary">SAMPLE COMPOSITION</a>

			<ul class="sublist_4" style="${compDisplay }">
				<c:forEach var="compoDataBean"
					items="${compositionTypes[compositionType]}">
					<li>
						<jsp:include page="sideParticleCharacterizationMenuButtons.jsp">
							<jsp:param name="charType"
								value="${compoDataBean.dataDisplayType}" />
							<jsp:param name="charTypeStyle" value="sublist_4" />
							<jsp:param name="charTypeLabelStyle" value="titleCell2" />
							<jsp:param name="noDataLabelStyle" value="titleCell2NoData" />
							<jsp:param name="tableStyle" value="charTitle" />
							<jsp:param name="addLinkStyle" value="addCell" />
							<jsp:param name="addAction" value="${compoDataBean.dataLink}" />
							<jsp:param name="location" value="${location}" />
						</jsp:include>
						<c:if
							test="${!empty particleDataTree[compoDataBean.dataDisplayType] }">
							<ul class="sublist_5" style="${compDisplay }">
								<c:forEach var="dataLinkBean"
									items="${particleDataTree[compoDataBean.dataDisplayType]}">
									<c:url var="url" value="${dataLinkBean.dataLink}.do">
										<c:param name="page" value="0" />
										<c:param name="dispatch" value="${dispatchValue}" />
										<c:param name="particleId" value="${particleId}" />
										<c:param name="dataId" value="${dataLinkBean.dataId}" />
										<c:param name="dataClassName"
											value="${dataLinkBean.dataClassName}" />
										<c:param name="submitType"
											value="${compoDataBean.dataDisplayType}" />
										<c:param name="location" value="${location}" />
									</c:url>
									<li id="complist">
										<c:choose>
											<c:when test="${dataLinkBean.viewColor != null}">
												<c:set var="viewTitleDisplay"
													value="color: ${dataLinkBean.viewColor};" />
												<a href="${url}" class="sublist_5" style=""> <span
													class="data_anchar">>&nbsp;</span>${dataLinkBean.viewTitle}</a>
											</c:when>
											<c:otherwise>
												<a href="${url}" class="sublist_5"><span
													class="data_anchar">>&nbsp;</span>${dataLinkBean.viewTitle}</a>
											</c:otherwise>
										</c:choose>
									</li>
								</c:forEach>
							</ul>
						</c:if>
					</li>
				</c:forEach>
			</ul>
		</li>
	</c:when>
	<c:otherwise>
		<li class="nodatali">
			SAMPLE COMPOSITION
		</li>
	</c:otherwise>
</c:choose>
