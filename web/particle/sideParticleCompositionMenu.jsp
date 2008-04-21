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
			<a href="#" class="subMenuSecondary">COMPOSITION</a>

			<ul class="sublist_4" style="">
				<c:forEach var="compoDataBean"
					items="${compositionTypes[compositionType]}">
					<%--				<c:url var="submitUrl" value="nanoparticleEntity.do">--%>
					<%--					<c:param name="particleId" value="${particleId}" />--%>
					<%--					<c:param name="page" value="0" />--%>
					<%--					<c:param name="dispatch" value="setup" />--%>
					<%--				</c:url>--%>

					<li>
						<jsp:include page="sideParticleCharacterizationMenuButtons.jsp">
							<jsp:param name="charType" value="${compoDataBean.dataDisplayType}" />
							<jsp:param name="charTypeStyle" value="sublist_4" />
							<jsp:param name="charTypeLabelStyle" value="titleCell2" />
							<jsp:param name="noDataLabelStyle" value="titleCell2NoData" />
							<jsp:param name="tableStyle" value="charTitle" />
							<jsp:param name="addLinkStyle" value="addCell" />
							<jsp:param name="addAction" value="${compoDataBean.dataLink}" />
						</jsp:include>
						<ul>
							
							<c:set var="pindex" value="1" />
							<c:forEach var="dataLinkBean"
								items="${particleDataTree[compoDataBean.dataDisplayType]}">
								<c:url var="url" value="${dataLinkBean.dataLink}.do">
									<c:param name="page" value="0" />
									<c:param name="dispatch" value="${dispatchValue}" />
									<c:param name="particleId" value="${particleId}" />
									<c:param name="dataId" value="${dataLinkBean.dataId}" />
									<c:param name="submitType" value="${compoDataBean.dataDisplayType}" />
								</c:url>
								<li id="complist">
									<a href=${url } id="complink" class="sublist_5"><span
										class="data_anchar">>&nbsp;</span>#${pindex}:&nbsp;${dataLinkBean.dataDisplayType}</a>
								</li>
								<c:set var="pindex" value="${pindex + 1}" />
							</c:forEach>
						</ul>
					</li>
				</c:forEach>
			</ul>
		</li>
	</c:when>
	<c:otherwise>
		<li class="nodatali">
			COMPOSITION
		</li>
	</c:otherwise>
</c:choose>
