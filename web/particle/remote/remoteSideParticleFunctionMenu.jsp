<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:choose>
	<c:when
		test="${displaytype == 'Therapeutic' ||
				displaytype == 'Targeting' ||
				displaytype == 'Diagnostic Imaging' ||
				displaytype == 'Diagnostic Reporting'}">
		<c:set var="funcDisplay" value="display: block;" />
	</c:when>
	<c:otherwise>
		<c:set var="funcDisplay" value="display: none;" />
	</c:otherwise>
</c:choose>
<li class="toplist">
	<a href="#" class="subMenuSecondary">FUNCTION</a>
	<ul class="sublist_4" style="${funcDisplay}">
		<c:forEach var="funcType" items="${allFunctionTypes}">
			<c:if test="${!empty remoteFuncTypeFuncs[funcType]}">
				<li>
					<a href="#" class="sublist_4">${funcType}</a>
					<ul class="sublist_5" style="${funcDisplay}">
						<c:forEach var="aFunc" items="${remoteFuncTypeFuncs[funcType]}">
							<c:url var="url" value="underConstruction.do">
								<c:param name="page" value="0" />
								<c:param name="dispatch" value="setupView" />
								<c:param name="particleName" value="${particleName}" />
								<c:param name="particleType" value="${particleType}" />
								<c:param name="functionId" value="${aFunc.viewTitle}" />
								<c:param name="submitType" value="${funcType}" />
							</c:url>
							<li>
								<a href=${url}><span class="data_anchar">>&nbsp;</span>${aFunc.viewTitle}</a>
							</li>
						</c:forEach>
					</ul>
				</li>
			</c:if>
		</c:forEach>
	</ul>
</li>
