<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<%--TODO: create online help topic for this page.--%>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="WUSTL Study Efficacy of nanoparticle Publication Summary" />
	<jsp:param name="topic" value="submit_study_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<jsp:include page="/bodyMessage.jsp?bundle=study" />

<div class="shadetabs" id="summaryTabALL">
	<ul>
		<li class="selected">
			<a
				href="javascript:showSummary('ALL', 6)"
				title="All"><span>All</span> </a>
		</li>
		<li>

				<a
					href="javascript:showSummary(1, 6)"
					title="book chapter"><span>book chapter </span> </a>
				<a href="javascript:printPage('publication.do?dispatch=summaryPrint&sampleId=11337747&type=book chapter')"
					id="printUrl1" style="display: none;"></a>
				<a href="publication.do?dispatch=summaryExport&sampleId=11337747&type=book chapter" id="exportUrl1"
					style="display: none;"></a>

				<a
					href="javascript:showSummary(2, 6)"
					title="editorial"><span>editorial </span> </a>
				<a href="javascript:printPage('publication.do?dispatch=summaryPrint&sampleId=11337747&type=editorial')"
					id="printUrl2" style="display: none;"></a>
				<a href="publication.do?dispatch=summaryExport&sampleId=11337747&type=editorial" id="exportUrl2"
					style="display: none;"></a>

				<a
					href="javascript:showSummary(3, 6)"
					title="peer review article"><span>peer review article </span> </a>
				<a href="javascript:printPage('publication.do?dispatch=summaryPrint&sampleId=11337747&type=peer review article')"
					id="printUrl3" style="display: none;"></a>
				<a href="publication.do?dispatch=summaryExport&sampleId=11337747&type=peer review article" id="exportUrl3"
					style="display: none;"></a>

				<a
					href="javascript:showSummary(4, 6)"
					title="proceeding"><span>proceeding </span> </a>
				<a href="javascript:printPage('publication.do?dispatch=summaryPrint&sampleId=11337747&type=proceeding')"
					id="printUrl4" style="display: none;"></a>
				<a href="publication.do?dispatch=summaryExport&sampleId=11337747&type=proceeding" id="exportUrl4"
					style="display: none;"></a>

				<a
					href="javascript:showSummary(5, 6)"
					title="report"><span>report </span> </a>
				<a href="javascript:printPage('publication.do?dispatch=summaryPrint&sampleId=11337747&type=report')"
					id="printUrl5" style="display: none;"></a>
				<a href="publication.do?dispatch=summaryExport&sampleId=11337747&type=report" id="exportUrl5"
					style="display: none;"></a>

				<a
					href="javascript:showSummary(6, 6)"
					title="review"><span>review </span> </a>
				<a href="javascript:printPage('publication.do?dispatch=summaryPrint&sampleId=11337747&type=review')"
					id="printUrl6" style="display: none;"></a>
				<a href="publication.do?dispatch=summaryExport&sampleId=11337747&type=review" id="exportUrl6"
					style="display: none;"></a>

		</li>
		<li>
			<a href="publication.do?dispatch=setupNew&sampleId=11337747"><span>other</span>
			</a>
		</li>
	</ul>
</div>

	<div class="shadetabs" id="summaryTab1"
		style="display: none;">
		<ul>
			<li>
				<a
					href="javascript:showSummary('ALL', 6)"
					title="All"><span>All</span> </a>
			</li>







				<li class="selected">
					<a
						href="javascript:showSummary(1, 6)"
						title="book chapter"><span>book chapter</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(2, 6)"
						title="editorial"><span>editorial</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(3, 6)"
						title="peer review article"><span>peer review article</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(4, 6)"
						title="proceeding"><span>proceeding</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(5, 6)"
						title="report"><span>report</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(6, 6)"
						title="review"><span>review</span>
					</a>
				</li>

			<li>
				<a href="publication.do?dispatch=setupNew&sampleId=11337747"><span>Other</span>
				</a>
			</li>
		</ul>
	</div>

	<div class="shadetabs" id="summaryTab2"
		style="display: none;">
		<ul>
			<li>
				<a
					href="javascript:showSummary('ALL', 6)"
					title="All"><span>All</span> </a>
			</li>







				<li class="">
					<a
						href="javascript:showSummary(1, 6)"
						title="book chapter"><span>book chapter</span>
					</a>
				</li>







				<li class="selected">
					<a
						href="javascript:showSummary(2, 6)"
						title="editorial"><span>editorial</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(3, 6)"
						title="peer review article"><span>peer review article</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(4, 6)"
						title="proceeding"><span>proceeding</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(5, 6)"
						title="report"><span>report</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(6, 6)"
						title="review"><span>review</span>
					</a>
				</li>

			<li>
				<a href="publication.do?dispatch=setupNew&sampleId=11337747"><span>Other</span>
				</a>
			</li>
		</ul>
	</div>

	<div class="shadetabs" id="summaryTab3"
		style="display: none;">
		<ul>
			<li>
				<a
					href="javascript:showSummary('ALL', 6)"
					title="All"><span>All</span> </a>
			</li>







				<li class="">
					<a
						href="javascript:showSummary(1, 6)"
						title="book chapter"><span>book chapter</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(2, 6)"
						title="editorial"><span>editorial</span>
					</a>
				</li>







				<li class="selected">
					<a
						href="javascript:showSummary(3, 6)"
						title="peer review article"><span>peer review article</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(4, 6)"
						title="proceeding"><span>proceeding</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(5, 6)"
						title="report"><span>report</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(6, 6)"
						title="review"><span>review</span>
					</a>
				</li>

			<li>
				<a href="publication.do?dispatch=setupNew&sampleId=11337747"><span>Other</span>
				</a>
			</li>
		</ul>
	</div>

	<div class="shadetabs" id="summaryTab4"
		style="display: none;">
		<ul>
			<li>
				<a
					href="javascript:showSummary('ALL', 6)"
					title="All"><span>All</span> </a>
			</li>







				<li class="">
					<a
						href="javascript:showSummary(1, 6)"
						title="book chapter"><span>book chapter</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(2, 6)"
						title="editorial"><span>editorial</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(3, 6)"
						title="peer review article"><span>peer review article</span>
					</a>
				</li>







				<li class="selected">
					<a
						href="javascript:showSummary(4, 6)"
						title="proceeding"><span>proceeding</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(5, 6)"
						title="report"><span>report</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(6, 6)"
						title="review"><span>review</span>
					</a>
				</li>

			<li>
				<a href="publication.do?dispatch=setupNew&sampleId=11337747"><span>Other</span>
				</a>
			</li>
		</ul>
	</div>

	<div class="shadetabs" id="summaryTab5"
		style="display: none;">
		<ul>
			<li>
				<a
					href="javascript:showSummary('ALL', 6)"
					title="All"><span>All</span> </a>
			</li>







				<li class="">
					<a
						href="javascript:showSummary(1, 6)"
						title="book chapter"><span>book chapter</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(2, 6)"
						title="editorial"><span>editorial</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(3, 6)"
						title="peer review article"><span>peer review article</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(4, 6)"
						title="proceeding"><span>proceeding</span>
					</a>
				</li>







				<li class="selected">
					<a
						href="javascript:showSummary(5, 6)"
						title="report"><span>report</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(6, 6)"
						title="review"><span>review</span>
					</a>
				</li>

			<li>
				<a href="publication.do?dispatch=setupNew&sampleId=11337747"><span>Other</span>
				</a>
			</li>
		</ul>
	</div>

	<div class="shadetabs" id="summaryTab6"
		style="display: none;">
		<ul>
			<li>
				<a
					href="javascript:showSummary('ALL', 6)"
					title="All"><span>All</span> </a>
			</li>







				<li class="">
					<a
						href="javascript:showSummary(1, 6)"
						title="book chapter"><span>book chapter</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(2, 6)"
						title="editorial"><span>editorial</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(3, 6)"
						title="peer review article"><span>peer review article</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(4, 6)"
						title="proceeding"><span>proceeding</span>
					</a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary(5, 6)"
						title="report"><span>report</span>
					</a>
				</li>







				<li class="selected">
					<a
						href="javascript:showSummary(6, 6)"
						title="review"><span>review</span>
					</a>
				</li>

			<li>
				<a href="publication.do?dispatch=setupNew&sampleId=11337747"><span>Other</span>
				</a>
			</li>
		</ul>
	</div>

<table class="summaryViewNoTop" width="100%">

	<tr>
		<td>

				<table id="summarySection1" class="summaryViewNoGrid"
					width="100%" align="center">
					<tr>
						<th align="left">
							<a name="book chapter" id="book chapter"><span
								class="summaryViewHeading">book chapter </span>
							</a>&nbsp;&nbsp;
							<a
								href="publication.do?dispatch=setupNew&sampleId=11337747&type=book chapter"
								class="addlink"><img align="middle" src="images/btn_add.gif"
									border="0" /></a>&nbsp;&nbsp;
						</th>
					</tr>
					<tr>
						<td>



									<div class="indented4">
										N/A
									</div>


						</td>
					</tr>
				</table>
				<div id="summarySeparator1">
					<br>
				</div>

				<table id="summarySection2" class="summaryViewNoGrid"
					width="100%" align="center">
					<tr>
						<th align="left">
							<a name="editorial" id="editorial"><span
								class="summaryViewHeading">editorial </span>
							</a>&nbsp;&nbsp;
							<a
								href="publication.do?dispatch=setupNew&sampleId=11337747&type=editorial"
								class="addlink"><img align="middle" src="images/btn_add.gif"
									border="0" /></a>&nbsp;&nbsp;
						</th>
					</tr>
					<tr>
						<td>



									<div class="indented4">
										N/A
									</div>


						</td>
					</tr>
				</table>
				<div id="summarySeparator2">
					<br>
				</div>

				<table id="summarySection3" class="summaryViewNoGrid"
					width="100%" align="center">
					<tr>
						<th align="left">
							<a name="peer review article" id="peer review article"><span
								class="summaryViewHeading">peer review article </span>
							</a>&nbsp;&nbsp;
							<a
								href="publication.do?dispatch=setupNew&sampleId=11337747&type=peer review article"
								class="addlink"><img align="middle" src="images/btn_add.gif"
									border="0" /></a>&nbsp;&nbsp;
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
															<td></td>
															<td width="95%"></td>
															<td align="right">


																	<a href="publication.do?sampleId=11337747&dispatch=setupUpdate&publicationId=11730957">Edit</a>

															</td>
														</tr>
														<tr>
															<td class="cellLabel" width="10%">
																Bibliography Info
															</td>
															<td>
																Kelly, KA, Shaw, SY, Nahrendorf, M, Kristoff, K, Aikawa, E, Schreiber, SL, Clemons, PA, Weissleder, R. Unbiased discovery of in vivo imaging probes through in vitro profiling of nanoparticle libraries. Integrative Biology. 2009; . <a target='_abstract' href=http://dx.doi.org/10.1039/b821775k >DOI: 10.1039/b821775k </a>.
																&nbsp;
															</td>
															<td></td>
														</tr>
														<tr>
															<td class="cellLabel" width="10%">
																Research Category
															</td>
															<td colspan="2">


																	N/A

															</td>
														</tr>
														<tr>
															<td class="cellLabel" width="10%">
																Description
															</td>
															<td>


																	N/A


															</td>
															<td></td>
														</tr>
														<tr>
															<td class="cellLabel" width="10%">
																Keywords
															</td>
															<td>


																	N/A


															</td>
															<td></td>
														</tr>
														<tr>
															<td class="cellLabel" width="10%">
																Publication Status
															</td>
															<td>
																published
																&nbsp;
															</td>
															<td></td>
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
				<div id="summarySeparator3">
					<br>
				</div>

				<table id="summarySection4" class="summaryViewNoGrid"
					width="100%" align="center">
					<tr>
						<th align="left">
							<a name="proceeding" id="proceeding"><span
								class="summaryViewHeading">proceeding </span>
							</a>&nbsp;&nbsp;
							<a
								href="publication.do?dispatch=setupNew&sampleId=11337747&type=proceeding"
								class="addlink"><img align="middle" src="images/btn_add.gif"
									border="0" /></a>&nbsp;&nbsp;
						</th>
					</tr>
					<tr>
						<td>



									<div class="indented4">
										N/A
									</div>


						</td>
					</tr>
				</table>
				<div id="summarySeparator4">
					<br>
				</div>

				<table id="summarySection5" class="summaryViewNoGrid"
					width="100%" align="center">
					<tr>
						<th align="left">
							<a name="report" id="report"><span
								class="summaryViewHeading">report </span>
							</a>&nbsp;&nbsp;
							<a
								href="publication.do?dispatch=setupNew&sampleId=11337747&type=report"
								class="addlink"><img align="middle" src="images/btn_add.gif"
									border="0" /></a>&nbsp;&nbsp;
						</th>
					</tr>
					<tr>
						<td>



									<div class="indented4">
										N/A
									</div>


						</td>
					</tr>
				</table>
				<div id="summarySeparator5">
					<br>
				</div>

				<table id="summarySection6" class="summaryViewNoGrid"
					width="100%" align="center">
					<tr>
						<th align="left">
							<a name="review" id="review"><span
								class="summaryViewHeading">review </span>
							</a>&nbsp;&nbsp;
							<a
								href="publication.do?dispatch=setupNew&sampleId=11337747&type=review"
								class="addlink"><img align="middle" src="images/btn_add.gif"
									border="0" /></a>&nbsp;&nbsp;
						</th>
					</tr>
					<tr>
						<td>



									<div class="indented4">
										N/A
									</div>


						</td>
					</tr>
				</table>
				<div id="summarySeparator6">
					<br>
				</div>

		</td>
	</tr>
</table>
