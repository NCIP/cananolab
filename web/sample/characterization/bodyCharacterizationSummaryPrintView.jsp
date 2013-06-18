<%--L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="css/main.css">
		<script type="text/javascript" src="javascript/script.js"></script>
	</head>
	<body onload="window.print();self.close()">
		<c:if test="${not empty theSample}">
			<jsp:include page="/bodyTitle.jsp">
				<jsp:param name="pageTitle" 
					value="${fn:toUpperCase(location)} Sample ${theSample.domain.name} Characterization" />
			</jsp:include>
		</c:if>
		<jsp:include page="shared/bodyCharacterizationSummaryPrintViewTable.jsp" />
	</body>
</html>