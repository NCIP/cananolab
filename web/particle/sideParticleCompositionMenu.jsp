<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

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
		<li class="controlList">
			<a href="#" class="subMenuSecondary">COMPOSITION</a>
			<ul class="sublist_4" id="compul" style="${compDisplay}">
				<c:url var="submitUrl" value="composition.do">
					<c:param name="particleId" value="${particleId}" />
					<c:param name="submitType" value="Composition" />
					<c:param name="page" value="0" />
					<c:param name="dispatch" value="setup" />
				</c:url>
				<li>
					<table class="charTitle">
						<tr class="titleRow">
							<td class="titleCell_2">
								<a href="#" class="sublist_4">Base Composition</a>
							</td>
							<td>
								&nbsp;
							</td>
							<td class="addCell">
								<a href="${submitUrl}" class="addlink">add</a>
							</td>
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
									<a href="${deleteUrl}" class="addlink">delete</a>
								</td>
							</c:if>
						</tr>
					</table>
				</li>
				<c:forEach var="leafCharBean" items="${allCompositions}">
					<c:url var="url" value="${leafCharBean.actionName}.do">
						<c:param name="page" value="0" />
						<c:param name="dispatch" value="${dispatchValue}" />
						<c:param name="particleId" value="${particleId}" />
						<c:param name="characterizationId" value="${leafCharBean.id}" />
						<c:param name="submitType" value="${compBean.name}" />
					</c:url>
					<li id="complist">
						<a href=${url } id="complink" class="sublist_5"><span
							class="data_anchar">>&nbsp;</span>${leafCharBean.viewTitle}</a>
					</li>
				</c:forEach>
			</ul>
		</li>
	</c:when>
	<c:otherwise>
		<li class="controlList">
			<a href="#" class="subMenuSecondary">COMPOSITION</a>
			<ul class="sublist_5" style="${compDisplay}">
				<c:forEach var="compBean" items="${allCompositions}">
					<c:url var="url" value="${compBean.actionName}.do">
						<c:param name="page" value="0" />
						<c:param name="dispatch" value="${dispatchValue}" />
						<c:param name="particleId" value="${particleId}" />
						<c:param name="characterizationId" value="${compBean.id}" />
						<c:param name="submitType" value="${compBean.name}" />
					</c:url>
					<li>
						<a href=${url } class="sublist_5"><span class="data_anchar">>&nbsp;</span>${compBean.viewTitle}</a>
					</li>
				</c:forEach>
			</ul>
		</li>
	</c:otherwise>
</c:choose>
