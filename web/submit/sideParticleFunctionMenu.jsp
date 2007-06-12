<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<span class="largerText">Function</span>
<br>
<br>
<c:forEach var="funcType" items="${allFunctionTypes}">
	<span class="indented"><strong>-${funcType}</strong></span>
	<c:choose>
		<c:when test="${canUserSubmit eq 'true'}">
			<a
				href="nanoparticleFunction.do?dispatch=setup&page=0&particleType=${particleType}&particleName=${particleName}&submitType=${funcType}">
				<em>add</em> </a>
		</c:when>
	</c:choose>
	<br>
	<c:forEach var="aFunc" items="${allFuncTypeFuncs[funcType]}">
		<span class="indented2"><a
			href="nanoparticleFunction.do?dispatch=${dispatchValue}&page=0&particleType=${particleType}&particleName=${particleName}&functionId=${aFunc.id}&submitType=${funcType}">${aFunc.viewTitle}</a>
		</span>
		<br>
	</c:forEach>
	<br>
</c:forEach>
<br>
