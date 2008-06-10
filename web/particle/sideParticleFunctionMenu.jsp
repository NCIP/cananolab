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

<c:choose>
	<c:when test="${!empty allFuncTypeFuncs || canCreateNanoparticle eq 'true' && location eq 'local'}">
	<li class="controlList">
	<a href="#" class="subMenuSecondary">FUNCTION</a>
	<ul class="sublist_4" style="${funcDisplay}">
		<c:forEach var="funcType" items="${allFunctionTypes}">
			<c:choose>
				<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local' && location eq 'local'}">
					<c:url var="addUrl" value="nanoparticleFunction.do">
						<c:param name="page" value="0" />
						<c:param name="dispatch" value="setup" />
						<c:param name="particleId" value="${particleId}" />
						<c:param name="submitType" value="${funcType}" />
					</c:url>
					<li>
					<table class="charTitle">
						<tr class="titleRow">
							<td class="titleCell2NoData">
								<c:out value="${funcType}" />
							</td>
							<td>
								&nbsp;
							</td>
							<td class="addCell">
								<a href="${addUrl}" class="addlink"><img src="images/btn_add.gif" border="0"/></a>
							</td>
						</tr>
					</table>
					</li>
				</c:when>
				<c:otherwise>
					<c:if test="${!empty allFuncTypeFuncs[funcType]}">
						<li class="sublist_4_func">${funcType}</li>
					</c:if>
				</c:otherwise>
			</c:choose>
			<c:if test="${!empty allFuncTypeFuncs[funcType]}">
				<ul class="sublist_5" style="${funcDisplay}">
					<c:forEach var="aFunc" items="${allFuncTypeFuncs[funcType]}">
						<c:url var="url" value="nanoparticleFunction.do">
							<c:param name="page" value="0" />
							<c:param name="dispatch" value="${dispatchValue}" />
							<c:param name="particleId" value="${particleId}" />
							<c:param name="functionId" value="${aFunc.id}" />
							<c:param name="submitType" value="${funcType}" />
						</c:url>
						<li>
							<a href=${url}><span class="data_anchar">>&nbsp;</span>${aFunc.viewTitle}</a>
						</li>
					</c:forEach>
				</ul>
			</c:if>
		</c:forEach>
	</ul>
</li>
</c:when>
	<c:otherwise>
		<li class="nodatali">
			FUNCTION
		</li>
	</c:otherwise>
</c:choose>
