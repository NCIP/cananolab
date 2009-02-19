<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table>
	<tr>
		<c:forEach var="category"
			items="${publicationSummaryView.publicationCategories}">
			<th>
				<a href="#${category}">${category}</a>
			</th>
		</c:forEach>
	</tr>
</table>

<c:forEach var="category"
	items="${publicationSummaryView.publicationCategories}">

	<table width="100%" border="0" align="center" cellpadding="3"
		cellspacing="0" class="topBorderOnly" summary="">
		<tr>
			<td class="formTitle" colspan="7">
				<a name="${category}" id="${category}">${category}</a>
			</td>
		</tr>
		<tr>
			<th class="leftLabel" width="20%">
				Title
			</th>
			<th class="label" width="20%">
				Author(s)
			</th>
			<th class="label" width="20%">
				Bibilography Info
			</th>
			<th class="label" width="15%">
				Abstract/Full Text
			</th>
			<th class="label">
				Research Category
			</th>
			<th class="rightLabel">
				Created Date
			</th>
		</tr>
		<c:forEach var="pubBean"
			items="${publicationSummaryView.category2Publications[category]}">
			<c:set var="pubObj" value="${pubBean.domainFile}" />
			<tr>
				<td class="leftLabel" valign="top">
					${pubObj.title}&nbsp;
				</td>
				<td class="label" valign="top">
					<c:if test="${!empty pubBean.authors}">
						<c:forEach var="author" items="${pubBean.authors}">
									${author.lastName}, ${author.firstName} ${author.initial}<br>
						</c:forEach>
					</c:if>
					&nbsp;
				</td>
				<td class="label" valign="top">
					${pubBean.bibliographyInfo} &nbsp;
				</td>
				<td class="label" valign="top">
					<c:choose>
						<c:when test="${! empty pubObj.abstractText}">
							${pubObj.abstractText}
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${! empty pubObj.pubMedId}">
									<a target="_abstract"
										href="http://www.ncbi.nlm.nih.gov/pubmed/${pubObj.pubMedId}">PMID:
										${pubObj.pubMedId }</a>&nbsp;
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${! empty pubObj.digitalObjectId}">
											<a target="_abstract"
												href="http://dx.doi.org/${pubObj.digitalObjectId}">PMID:
												${pubObj.digitalObjectId }</a>&nbsp;
										</c:when>
										<c:otherwise>
											<a
												href="publication.do?dispatch=download&amp;fileId=${pubObj.id}&amp;location=${param.location}"
												target="${pubBean.urlTarget}"> ${pubOjb.uri}</a>&nbsp;
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
					&nbsp;
					</c:otherwise>
					</c:choose>
				</td>
				<td class="label" valign="top">
					${pubObj.researchArea}&nbsp;
				</td>
				<td class="rightLabel" valign="top">
					${pubBean.createdDateStr}&nbsp;
				</td>
			</tr>
		</c:forEach>
	</table>
</c:forEach>