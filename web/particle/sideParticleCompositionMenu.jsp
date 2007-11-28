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
		</li>
		<c:forEach var="leafCharBean" items="${allCompositions}">
			<c:url var="url" value="${leafCharBean.actionName}.do">
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
