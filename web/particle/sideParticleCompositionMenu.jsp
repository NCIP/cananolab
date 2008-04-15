<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:choose>
	<c:when test="${displaytype == 'Composition' ||
				displaytype == 'Nanoparticle Entity' ||
				displaytype == 'Functionalizing Entity' ||
				displaytype == 'Chemical Association' ||
				displaytype == 'File'}">
		<c:set var="compDisplay" value="display: block;" />
	</c:when>
	<c:otherwise>
		<c:set var="compDisplay" value="display: none;" />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${!empty particleDataTree || canCreateNanoparticle eq 'true'}">
		<li class="controlList">
			<a href="#" class="subMenuSecondary">COMPOSITION</a>
			<ul class="sublist_4" style="${compDisplay}">
				<c:url var="submitUrl" value="composition.do">
					<c:param name="particleId" value="${particleId}" />
					<c:param name="submitType" value="Composition" />
					<c:param name="page" value="0" />
					<c:param name="dispatch" value="setup" />
				</c:url>
				<li>
				
<%--				<jsp:include page="sideParticleCharacterizationMenuButtons.jsp">--%>
<%--					<jsp:param name="charType" value="${subCharType}" />--%>
<%--					<jsp:param name="charTypeStyle" value="sublist_4" />--%>
<%--					<jsp:param name="charTypeLabelStyle" value="titleCell2" />--%>
<%--					<jsp:param name="noDataLabelStyle" value="titleCell2NoData" />--%>
<%--					<jsp:param name="tableStyle" value="charTitle" />--%>
<%--					<jsp:param name="addLinkStyle" value="addCell" />--%>
<%--					<jsp:param name="addAction" value="physicalCharacterization" />--%>
<%--				</jsp:include>--%>
				
				
					<table class="charTitle">
						<tr class="titleRow">
							<td class="titleCell2NoData">
								<li>Nanoparticle Entity</li>
							</td>
							<td>
								&nbsp;
							</td>
							<c:if test="${canCreateNanoparticle eq 'true'}">
								<td class="addCell">
									<a href="${submitUrl}" class="addlink"><img
											src="images/btn_add.gif" border="0" /> </a>
								</td>
							</c:if>
							<c:if
								test="${canUserDeleteChars eq 'true' &&
												!empty allCompositions}">
								<td>
									&nbsp;
								</td>
								<td class="addCell">
									<c:url var="deleteUrl" value="deleteAction.do">
										<c:param name="page" value="0" />
										<c:param name="dispatch" value="setup" />
										<c:param name="particleId" value="${particleId}" />
										<c:param name="submitType" value="Composition" />
									</c:url>
									<a href="${deleteUrl}" class="addlink"><img
											src="images/btn_delete.gif" border="0" /> </a>
								</td>
							</c:if>
						</tr>
					</table>
				<ul>
				<c:set var="compType" value="Nanoparticle Entity"/>
				<c:forEach var="dataLinkBean" items="${particleDataTree[compType]}">
					<c:url var="url" value="composition.do">
						<c:param name="page" value="0" />
						<c:param name="dispatch" value="${dispatchValue}" />
						<c:param name="particleId" value="${particleId}" />
						<c:param name="characterizationId" value="${leafCharBean.id}" />
						<c:param name="submitType" value="${leafCharBean.name}" />
					</c:url>
					<li id="complist">
						<a href=${url } id="complink" class="sublist_5"><span
							class="data_anchar">>&nbsp;</span>${dataLinkBean.dataDisplayType}</a>
					</li>
				</c:forEach>
				</ul>
				</li>
				<li>
				
<%--				<jsp:include page="sideParticleCharacterizationMenuButtons.jsp">--%>
<%--					<jsp:param name="charType" value="${subCharType}" />--%>
<%--					<jsp:param name="charTypeStyle" value="sublist_4" />--%>
<%--					<jsp:param name="charTypeLabelStyle" value="titleCell2" />--%>
<%--					<jsp:param name="noDataLabelStyle" value="titleCell2NoData" />--%>
<%--					<jsp:param name="tableStyle" value="charTitle" />--%>
<%--					<jsp:param name="addLinkStyle" value="addCell" />--%>
<%--					<jsp:param name="addAction" value="physicalCharacterization" />--%>
<%--				</jsp:include>--%>
				
				
					<table class="charTitle">
						<tr class="titleRow">
							<td class="titleCell2NoData">
								<li>Functionalizing Entity</li>
							</td>
							<td>
								&nbsp;
							</td>
							<c:if test="${canCreateNanoparticle eq 'true'}">
								<td class="addCell">
									<a href="${submitUrl}" class="addlink"><img
											src="images/btn_add.gif" border="0" /> </a>
								</td>
							</c:if>
							<c:if
								test="${canUserDeleteChars eq 'true' &&
												!empty allCompositions}">
								<td>
									&nbsp;
								</td>
								<td class="addCell">
									<c:url var="deleteUrl" value="deleteAction.do">
										<c:param name="page" value="0" />
										<c:param name="dispatch" value="setup" />
										<c:param name="particleId" value="${particleId}" />
										<c:param name="submitType" value="Composition" />
									</c:url>
									<a href="${deleteUrl}" class="addlink"><img
											src="images/btn_delete.gif" border="0" /> </a>
								</td>
							</c:if>
						</tr>
					</table>
				<ul>
				<c:forEach var="leafCharBean" items="${allCompositions}">
					<c:url var="url" value="composition.do">
						<c:param name="page" value="0" />
						<c:param name="dispatch" value="${dispatchValue}" />
						<c:param name="particleId" value="${particleId}" />
						<c:param name="characterizationId" value="${leafCharBean.id}" />
						<c:param name="submitType" value="${leafCharBean.name}" />
					</c:url>
					<li id="complist">
						<a href=${url } id="complink" class="sublist_5"><span
							class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
					</li>
				</c:forEach>
				</ul>
				</li>
<%--			End of Functionalizing Entity--%>

			<%--			Chemical Association --%>
			
				
				<li>
				
<%--				<jsp:include page="sideParticleCharacterizationMenuButtons.jsp">--%>
<%--					<jsp:param name="charType" value="${subCharType}" />--%>
<%--					<jsp:param name="charTypeStyle" value="sublist_4" />--%>
<%--					<jsp:param name="charTypeLabelStyle" value="titleCell2" />--%>
<%--					<jsp:param name="noDataLabelStyle" value="titleCell2NoData" />--%>
<%--					<jsp:param name="tableStyle" value="charTitle" />--%>
<%--					<jsp:param name="addLinkStyle" value="addCell" />--%>
<%--					<jsp:param name="addAction" value="physicalCharacterization" />--%>
<%--				</jsp:include>--%>
				
				
					<table class="charTitle">
						<tr class="titleRow">
							<td class="titleCell2NoData">
								<li>Chemical Association</li>
							</td>
							<td>
								&nbsp;
							</td>
							<c:if test="${canCreateNanoparticle eq 'true'}">
								<td class="addCell">
									<a href="${submitUrl}" class="addlink"><img
											src="images/btn_add.gif" border="0" /> </a>
								</td>
							</c:if>
							<c:if
								test="${canUserDeleteChars eq 'true' &&
												!empty allCompositions}">
								<td>
									&nbsp;
								</td>
								<td class="addCell">
									<c:url var="deleteUrl" value="deleteAction.do">
										<c:param name="page" value="0" />
										<c:param name="dispatch" value="setup" />
										<c:param name="particleId" value="${particleId}" />
										<c:param name="submitType" value="Composition" />
									</c:url>
									<a href="${deleteUrl}" class="addlink"><img
											src="images/btn_delete.gif" border="0" /> </a>
								</td>
							</c:if>
						</tr>
					</table>
				<ul>
				<c:forEach var="leafCharBean" items="${allCompositions}">
					<c:url var="url" value="composition.do">
						<c:param name="page" value="0" />
						<c:param name="dispatch" value="${dispatchValue}" />
						<c:param name="particleId" value="${particleId}" />
						<c:param name="characterizationId" value="${leafCharBean.id}" />
						<c:param name="submitType" value="${leafCharBean.name}" />
					</c:url>
					<li id="complist">
						<a href=${url } id="complink" class="sublist_5"><span
							class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
					</li>
				</c:forEach>
				</ul>
				</li>
<%--			End of Entity Association--%>

			<%--			File --%>
			
				<li>
				
<%--				<jsp:include page="sideParticleCharacterizationMenuButtons.jsp">--%>
<%--					<jsp:param name="charType" value="${subCharType}" />--%>
<%--					<jsp:param name="charTypeStyle" value="sublist_4" />--%>
<%--					<jsp:param name="charTypeLabelStyle" value="titleCell2" />--%>
<%--					<jsp:param name="noDataLabelStyle" value="titleCell2NoData" />--%>
<%--					<jsp:param name="tableStyle" value="charTitle" />--%>
<%--					<jsp:param name="addLinkStyle" value="addCell" />--%>
<%--					<jsp:param name="addAction" value="physicalCharacterization" />--%>
<%--				</jsp:include>--%>
				
				
					<table class="charTitle">
						<tr class="titleRow">
							<td class="titleCell2NoData">
								<li>Composition File</li>
							</td>
							<td>
								&nbsp;
							</td>
							<c:if test="${canCreateNanoparticle eq 'true'}">
								<td class="addCell">
									<a href="${submitUrl}" class="addlink"><img
											src="images/btn_add.gif" border="0" /> </a>
								</td>
							</c:if>
							<c:if
								test="${canUserDeleteChars eq 'true' &&
												!empty allCompositions}">
								<td>
									&nbsp;
								</td>
								<td class="addCell">
									<c:url var="deleteUrl" value="deleteAction.do">
										<c:param name="page" value="0" />
										<c:param name="dispatch" value="setup" />
										<c:param name="particleId" value="${particleId}" />
										<c:param name="submitType" value="Composition" />
									</c:url>
									<a href="${deleteUrl}" class="addlink"><img
											src="images/btn_delete.gif" border="0" /> </a>
								</td>
							</c:if>
						</tr>
					</table>
				<ul>
				<c:forEach var="leafCharBean" items="${allCompositions}">
					<c:url var="url" value="composition.do">
						<c:param name="page" value="0" />
						<c:param name="dispatch" value="${dispatchValue}" />
						<c:param name="particleId" value="${particleId}" />
						<c:param name="characterizationId" value="${leafCharBean.id}" />
						<c:param name="submitType" value="${leafCharBean.name}" />
					</c:url>
					<li id="complist">
						<a href=${url } id="complink" class="sublist_5"><span
							class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
					</li>
				</c:forEach>
				</ul>
				</li>
<%--			End of File--%>
			</ul>
		</li>

	</c:when>
	<c:otherwise>
		<li class="nodatali">
			COMPOSITION
		</li>
	</c:otherwise>
</c:choose>
