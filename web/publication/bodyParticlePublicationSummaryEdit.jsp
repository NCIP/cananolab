<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table summary="" cellpadding="0" cellspacing="0" border="0"
	width="100%">
	<tr>
		<c:forEach var="type" items="${publicationCategories}">
			<th class="borderlessLabel">
				<a href="#${type}">${type}</a>
			</th>
		</c:forEach>
		<td class="borderlessLabel">
			<a
				href="publication.do?dispatch=setupNew&particleId=${param.particleId }">add
				new </a>
		</td>
	</tr>
</table>
<br>
<c:forEach var="category"
	items="${publicationSummaryView.publicationCategories}">

	<table class="smalltable3" cellpadding="0" cellspacing="0" border="0"
		width="90%">
		<tr>
			<th align="left">
				<a name="${category}" id="${category}">${category}</a>
				&nbsp;&nbsp;&nbsp;
				<a href="#" class="addlink"><img align="absmiddle"
						src="images/btn_add.gif" border="0" /></a>&nbsp;&nbsp;
				<a><img align="absmiddle" src="images/btn_delete.gif" border="0" />
				</a>
			</th>
		</tr>
		<tr>
			<td align="left">
				<jsp:include page="/bodyMessage.jsp?bundle=particle" />
			</td>
		</tr>
		<tr>
			<td>
				<div class="indented4">
					<table class="summarytable" width="90%" border="0" cellpadding="0"
						cellspacing="0" summary="">
						<tr>
							<th width="20%">
								Title
							</th>
							<th width="20%">
								Author(s)
							</th>
							<th width="20%">
								Bibilography Info
							</th>
							<th width="15%">
								Abstract/Full Text
							</th>
							<th >
								Research Category
							</th>
							<th >
								Created Date
							</th>
							<th width="5%">
								&nbsp;
							</th>
						</tr>
						<c:forEach var="pubBean"
							items="${publicationSummaryView.category2Publications[category]}">
							<c:set var="pubObj" value="${pubBean.domainFile}" />
							<tr>
								<td valign="top">
									${pubObj.title}&nbsp;
								</td>
								<td valign="top">
									<c:if test="${!empty pubBean.authors}">
										<c:forEach var="author" items="${pubBean.authors}">
									${author.lastName}, ${author.firstName} ${author.initial}<br>
										</c:forEach>
									</c:if>
									&nbsp;
								</td>
								<td valign="top">
									${pubBean.bibliographyInfo} &nbsp;
								</td>
								<td valign="top">
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
																href="searchPublication.do?dispatch=download&amp;fileId=${pubObj.id}&amp;location=${param.location}"
																target="${pubBean.urlTarget}"> ${pubOjb.uri}</a>&nbsp;
										</c:otherwise>
													</c:choose>
												</c:otherwise>
											</c:choose>
					&nbsp;
					</c:otherwise>
									</c:choose>
								</td>
								<td valign="top">
									${pubObj.researchArea}&nbsp;
								</td>
								<td valign="top">
									${pubBean.createdDateStr}&nbsp;
								</td>
								<td valign="top">
									<c:url var="pubUrl" value="publication.do">
										<c:param name="particleId" value="${particleId}" />
										<c:param name="dispatch" value="setupUpdate" />
										<c:param name="publicationId" value="${pubObj.id}" />
										<c:param name="location" value="${location}" />
									</c:url>
									<a href="${pubUrl}">Edit</a>
								</td>
							</tr>
						</c:forEach>
					</table>
		</div>
		</td>
		</tr>
	</table>
</c:forEach>