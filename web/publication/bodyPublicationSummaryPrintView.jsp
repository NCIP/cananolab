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
<table width="100%" align="center">
	<tr height="25">
		<td class="contentTitle" colspan="2">
			<br>
			Publications
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<table width="100%" border="0" align="center" cellpadding="3"
				cellspacing="0" class="topBorderOnly" summary="">
				<tr>
					<td class="formTitle" colspan="4" align="center">
						${fn:toUpperCase(location)} ${sampleName} - Publications
					</td>
				</tr>
				<tr>
					<th class="leftLabel">
						Identifier
					</th>
					<th class="label">
						Title
					</th>
					<th class="label">
						Authors
					</th>
					<th class="rightLabel">
						Year
					</th>
				</tr>
				<c:forEach var="pubObj" items="${sampleBean.domain.publicationCollection}">
				<tr>
					<td class="leftLabel">

						<c:choose>
							<c:when test="${pubObj.pubMedId != null && pubObj.pubMedId != 0}">
								PMID: ${pubObj.pubMedId }
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${pubObj.digitalObjectId != null &&
										pubObj.digitalObjectId ne ''}">
										DOI: ${pubObj.digitalObjectId }
									</c:when>
									<c:otherwise>
										${pubObj.category}: ${pubObj.title }
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					</td>
					<td class="label">
					${pubObj.title}&nbsp;
					</td>
					<td class="label">
						<c:if test="${!empty pubObj.documentAuthorCollection}">
							<c:forEach var="author"
								items="${pubObj.documentAuthorCollection}">
									${author.lastName};
							</c:forEach>
						</c:if>

						&nbsp;
					</td>

					<td class="rightLabel">
						<c:if test="${pubObj.year!=0}">
							${pubObj.year}
						</c:if>
						&nbsp;
					</td>
				</tr>
				</c:forEach>
			</table>
		</td>
	</tr>
</table>
</body></html>