<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:url var="printUrl" value="studyPublication.do">
	<c:param name="dispatch" value="summaryPrint" />
	<c:param name="studyId" value="${studyId}" />
</c:url>
<c:url var="exportUrl" value="studyPublication.do">
	<c:param name="dispatch" value="summaryExport" />
	<c:param name="studyId" value="${studyId}" />
</c:url>
<jsp:include page="/bodyTitle.jsp">
		<jsp:param name="pageTitle"
			value="Publications for Study ${studyName}" />
		<jsp:param name="topic" value="publications_all_tab_help" />
		<jsp:param name="glossaryTopic" value="glossary_help" />
		<jsp:param name="printLink" value="${printUrl}" />
		<jsp:param name="exportLink" value="${exportUrl}" />
</jsp:include>

<jsp:include page="/bodyMessage.jsp?bundle=study" />
<c:set var="publicationCategories"
	value="${studyPublicationSummaryView.publicationCategories}" />
<c:if test="${empty printView}">
	<div class="shadetabs" id="summaryTabALL">
		<ul>
			<li class="selected">
				<a
					href="javascript:showSummary('ALL', ${fn:length(publicationCategories)})"
					title="All"><span>All</span> </a>
			</li>
			<li>
				<c:forEach var="type" items="${publicationCategories}"
					varStatus="ind">
					<a
						href="javascript:showSummary('${ind.count}', ${fn:length(publicationCategories)})"
						title="${type}"><span>${type}</span> </a>
					<a href="javascript:printPage('${printUrl}&type=${type}')"
						id="printUrl${ind.count}" style="display: none;"></a>
					<a href="${exportUrl}&type=${type}" id="exportUrl${ind.count}"
						style="display: none;"></a>
				</c:forEach>
			</li>
		</ul>
	</div>
	<c:forEach var="type" items="${publicationCategories}" varStatus="ind">
		<div class="shadetabs" id="summaryTab${ind.count}"
			style="display: none;">
			<ul>
				<li>
					<a
						href="javascript:showSummary('ALL', ${fn:length(publicationCategories)})"
						title="All"><span>All</span> </a>
				</li>
				<c:forEach var="type" items="${publicationCategories}"
					varStatus="ind2">
					<c:choose>
						<c:when test="${ind.count eq ind2.count }">
							<c:set var="selectedClass" value="selected" />
						</c:when>
						<c:otherwise>
							<c:set var="selectedClass" value="" />
						</c:otherwise>
					</c:choose>
					<li class="${selectedClass}">
						<a
							href="javascript:showSummary('${ind2.count}', ${fn:length(publicationCategories)})"
							title="${type}"><span>${type}</span> </a>
					</li>
				</c:forEach>
			</ul>
		</div>
	</c:forEach>
</c:if>
<table class="summaryViewNoTop" width="100%">	
	<tr>
		<td>
			<c:forEach var="type" items="${publicationCategories}"
				varStatus="ind">
				<table id="summarySection${ind.count}" class="summaryViewNoGrid"
					align="center" width="99%">
					<tr>
						<th align="left"">
							<a name="${type}" id="${type}"><span
								class="summaryViewHeading">${type}</span> </a>
						</th>
					</tr>
					<tr>
						<td>
							<table width="99%" align="center" class="summaryViewNoGrid"
								bgcolor="#dbdbdb">
								<tr>
									<th valign="top" align="left" height="6">
									</th>
								</tr>
								<tr>
									<td>
										<c:forEach var="pubBean"
											items="${studyPublicationSummaryView.category2Publications[type]}"
											varStatus="pubBeanInd">
											<c:set var="pubObj" value="${pubBean.domainFile}" />
											<table class="summaryViewNoGrid" width="99%" align="center"
												bgcolor="#F5F5f5">
												<tr>
													<td class="cellLabel" width="10%">
														Bibliography Info
													</td>
													<td>
														<c:out value="${pubBean.displayName}" escapeXml="false" />
														&nbsp;
													</td>
												</tr>
												<c:if test="${!empty pubObj.researchArea}">
													<tr>
														<td class="cellLabel" width="10%">
															Research Category
														</td>
														<td colspan="2">
															<c:out
																value="${fn:replace(pubObj.researchArea, ';', '<br>')}"
																escapeXml="false" />
															&nbsp;
														</td>
													</tr>
												</c:if>
												<c:if test="${!empty pubObj.description}">
													<tr>
														<td class="cellLabel" width="10%">
															Description
														</td>
														<td>
															<c:out value="${pubBean.description}" escapeXml="false" />
															&nbsp;
														</td>
													</tr>
												</c:if>
												<c:if test="${!empty pubBean.keywordsStr}">
													<tr>
														<td class="cellLabel" width="10%">
															Keywords
														</td>
														<td>
															<c:out value="${pubBean.keywordsDisplayName}"
																escapeXml="false" />
															&nbsp;
														</td>
													</tr>
												</c:if>
												<tr>
													<td class="cellLabel" width="10%">
														Publication Status
													</td>
													<td>
														<c:out value="${pubObj.status}" />
														&nbsp;
													</td>
												</tr>
											</table>
											<c:if
												test="${pubBeanInd.count<fn:length(publicationSummaryView.category2Publications[type])}">
												<br />
											</c:if>
										</c:forEach>
									</td>
								</tr>
								<tr>
									<th valign="top" align="left" height="6">
									</th>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<div id="summarySeparator${ind.count}">
					<br>
				</div>
			</c:forEach>
		</td>
	</tr>
</table>
<!-- 	
<table class="contentTitle" align="center" width="100%" border="0">
	<tr height="20">
		<td>
			 Publications for Study ${studyName}
		</td>

			<td align="right" width="30%">





	<a class="helpText" href="javascript:printPage('publication.do?dispatch=summaryPrint&sampleId=11337748')" id="printLink">Print</a>
	&nbsp;


	<a class="helpText" href="publication.do?dispatch=summaryExport&sampleId=11337748" id="exportLink">Export</a>
	&nbsp;


<a	class="helpText"
	href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=publications_all_tab_help')">
Help</a>
&nbsp;


<a	class="helpText"
	href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=glossary_help')">
Glossary</a>
&nbsp;

			</td>

	</tr>
</table>
<br>















	<div class="shadetabs" id="summaryTabALL">
		<ul>
			<li class="selected">
				<a
					href="javascript:showSummary('ALL', 1)"
					title="All"><span>All</span> </a>
			</li>
			<li>

					<a
						href="javascript:showSummary('1', 1)"
						title="peer review article"><span>peer review article</span> </a>
					<a href="javascript:printPage('publication.do?dispatch=summaryPrint&sampleId=11337748&type=peer review article')"
						id="printUrl1" style="display: none;"></a>
					<a href="publication.do?dispatch=summaryExport&sampleId=11337748&type=peer review article" id="exportUrl1"
						style="display: none;"></a>

			</li>
		</ul>
	</div>

		<div class="shadetabs" id="summaryTab1"
			style="display: none;">
			<ul>
				<li>
					<a
						href="javascript:showSummary('ALL', 1)"
						title="All"><span>All</span> </a>
				</li>







					<li class="selected">
						<a
							href="javascript:showSummary('1', 1)"
							title="peer review article"><span>peer review article</span> </a>
					</li>

			</ul>
		</div>


<table class="summaryViewNoTop" width="100%">

	<tr>
		<td>

				<table id="summarySection1" class="summaryViewNoGrid"
					align="center" width="99%">
					<tr>
						<th align="left"">
							<a name="peer review article" id="peer review article"><span
								class="summaryViewHeading">peer review article</span> </a>
						</th>
					</tr>
					<tr>
						<td>
							<table width="99%" align="center" class="summaryViewNoGrid"
								bgcolor="#dbdbdb">
								<tr>
									<th valign="top" align="left" height="6">
									</th>
								</tr>
								<tr>
									<td>


											<table class="summaryViewNoGrid" width="99%" align="center"
												bgcolor="#F5F5f5">
												<tr>
													<td class="cellLabel" width="10%">
														Bibliography Info
													</td>
													<td>
														Kelly, KA, Shaw, SY, Nahrendorf, M, Kristoff, K, Aikawa, E, Schreiber, SL, Clemons, PA, Weissleder, R. Unbiased discovery of in vivo imaging probes through in vitro profiling of nanoparticle libraries. Integrative Biology. 2009; . <a target='_abstract' href=http://dx.doi.org/10.1039/b821775k >DOI: 10.1039/b821775k </a>.
														&nbsp;
													</td>
												</tr>

													<tr>
														<td class="cellLabel" width="10%">
															Research Category
														</td>
														<td colspan="2">
															animal<br>cell line<br>characterization<br>in vitro<br>in vivo<br>synthesis
															&nbsp;
														</td>
													</tr>


													<tr>
														<td class="cellLabel" width="10%">
															Description
														</td>
														<td>
															In vivo imaging reveals how proteins and cells function as part of complex regulatory networks in intact organisms, and thereby contributes to a systems-level understanding of biological processes. However, the development of novel in vivo imaging probes remains challenging. Most probes are directed against a limited number of pre-specified protein targets; cell-based screens for imaging probes have shown promise, but raise concerns over whether in vitro surrogate cell models recapitulate in vivo phenotypes. Here, we rapidly profile the in vitro binding of nanoparticle imaging probes in multiple samples of defined target vs. background cell types, using primary cell isolates. This approach selects for nanoparticles that show desired targeting effects across all tested members of a class of cells, and decreases the likelihood that an idiosyncratic cell line will unduly skew screening results. To adjust for multiple hypothesis testing, we use permutation methods to identify nanoparticles that best differentiate between the target and background cell classes. (This approach is conceptually analogous to one used for high-dimensionality datasets of genome-wide gene expression, e.g. to identify gene expression signatures that discriminate subclasses of cancer.) We apply this approach to the identification of nanoparticle imaging probes that bind endothelial cells, and validate our in vitro findings in human arterial samples, and by in vivo intravital microscopy in mice. Overall, this work presents a generalizable approach to the unbiased discovery of in vivo imaging probes, and may guide the further development of novel endothelial imaging probes.
															&nbsp;
														</td>
													</tr>


													<tr>
														<td class="cellLabel" width="10%">
															Keywords
														</td>
														<td>
															ANHYDRIDES<br>CELLULAR BINDING AFFINITY<br>SCREENING
															&nbsp;
														</td>
													</tr>

												<tr>
													<td class="cellLabel" width="10%">
														Publication Status
													</td>
													<td>
														published
														&nbsp;
													</td>
												</tr>
											</table>


									</td>
								</tr>
								<tr>
									<th valign="top" align="left" height="6">
									</th>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<div id="summarySeparator1">
					<br>
				</div>
		</td>
	</tr>
</table>
-->
