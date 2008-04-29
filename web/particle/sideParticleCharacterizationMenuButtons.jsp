<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<table class="${param.tableStyle}">
	<tr class="titleRow">
		<td class="${param.charTypeLabelStyle}">
			<c:choose>
				<c:when test="${!empty particleDataTree[param.charType]}">
					<c:choose>
						<c:when
							test="${param.charType eq 'Nanoparticle Entity' ||
									param.charType eq 'Functionalizing Entity' ||
									param.charType eq 'Chemical Association' ||
									param.charType eq 'Composition File' }">

							<a href="#" class="${param.charTypeStyle}">${param.charType}</a>

						</c:when>
						<c:otherwise>
							<c:url var="url" value="${param.addAction}.do">
								<c:param name="particleId" value="${particleId}" />
								<c:param name="submitType" value="${param.charType}" />
								<c:param name="page" value="0" />
								<c:param name="dispatch" value="summaryView" />
							</c:url>
							<a href="${url}" class="${param.charTypeStyle}">${param.charType}</a>

						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					<td class="${param.noDataLabelStyle}">
						<c:out value="${param.charType}" />
					</td>
				</c:otherwise>
			</c:choose>
		</td>
		<c:choose>
			<c:when test="${canCreateNanoparticle eq 'true'}">
				<c:url var="submitUrl" value="${param.addAction}.do">
					<c:param name="particleId" value="${particleId}" />
					<c:param name="submitType" value="${param.charType}" />
					<c:param name="page" value="0" />
					<c:param name="dispatch" value="setup" />
				</c:url>
				<td>
					&nbsp;
				</td>
				<td class="${param.addLinkStyle }">
					<a href="${submitUrl}" class="addlink"><img
							src="images/btn_add.gif" border="0" /> </a>
				</td>
			</c:when>
		</c:choose>
		<c:if
			test="${canUserDelete eq 'true' &&
				!empty particleDataTree[param.charType]}">
			<c:url var="deleteUrl" value="${param.addAction}.do">
				<c:param name="particleId" value="${particleId}" />
				<c:param name="submitType" value="${param.charType}" />
				<c:param name="page" value="0" />
				<c:param name="dispatch" value="setupDeleteAll" />
			</c:url>
			<td>
				&nbsp;
			</td>
			<td class="${param.addLinkStyle}">
				<a href="${deleteUrl}" class="addlink"><img
						src="images/btn_delete.gif" border="0" /> </a>
			</td>
		</c:if>
		<td class="tdfill">
			&nbsp;
		</td>
	</tr>
</table>
