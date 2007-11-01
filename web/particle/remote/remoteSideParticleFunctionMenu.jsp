<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<span class="largerText">Function</span>
<br>
<br>
<c:forEach var="funcType" items="${allFunctionTypes}">
	<span class="indented0"><strong>-${funcType}</strong></span>
	<br>
	<c:forEach var="aFunc" items="${remoteFuncTypeFuncs[funcType]}">
		<span class="indented1"><a
			href="underConstruction.do?dispatch=setupView&page=0&particleType=${particleType}&particleName=${particleName}&functionId=${aFunc.id}&submitType=${funcType}">${aFunc.viewTitle}</a>
		</span>
		<br>
	</c:forEach>
	<br>
</c:forEach>
<br>
