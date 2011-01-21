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
	<jsp:param name="pageTitle" value="Characterizations in Study MIT_MGH_Kelly" />
	<jsp:param name="topic" value="submit_study_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<jsp:include page="/bodyMessage.jsp?bundle=study" />
<div class="shadetabs" id="summaryTabALL">
	<ul>
		<li class="selected">
			<a
				href="javascript:showSummary('ALL', 4)"
				title="All"><span>&nbsp;All</span>
			</a>
		</li>

			<li>
				<a
					href="javascript:showSummary('1', 4)"
					title="physico-chemical characterization"><span>physico-chemical characterization</span></a>
				<a href="javascript:printPage('characterization.do?dispatch=summaryPrint&sampleId=11337747&type=physico-chemical characterization')"
					id="printUrl1" style="display: none;"></a>
				<a href="characterization.do?dispatch=summaryExport&sampleId=11337747&type=physico-chemical characterization" id="exportUrl1"
					style="display: none;"></a>
			</li>

			<li>
				<a
					href="javascript:showSummary('2', 4)"
					title="in vitro characterization"><span>in vitro characterization</span></a>
				<a href="javascript:printPage('characterization.do?dispatch=summaryPrint&sampleId=11337747&type=in vitro characterization')"
					id="printUrl2" style="display: none;"></a>
				<a href="characterization.do?dispatch=summaryExport&sampleId=11337747&type=in vitro characterization" id="exportUrl2"
					style="display: none;"></a>
			</li>

			<li>
				<a
					href="javascript:showSummary('3', 4)"
					title="in vivo characterization"><span>in vivo characterization</span></a>
				<a href="javascript:printPage('characterization.do?dispatch=summaryPrint&sampleId=11337747&type=in vivo characterization')"
					id="printUrl3" style="display: none;"></a>
				<a href="characterization.do?dispatch=summaryExport&sampleId=11337747&type=in vivo characterization" id="exportUrl3"
					style="display: none;"></a>
			</li>

			<li>
				<a
					href="javascript:showSummary('4', 4)"
					title="ex vivo"><span>ex vivo</span></a>
				<a href="javascript:printPage('characterization.do?dispatch=summaryPrint&sampleId=11337747&type=ex vivo')"
					id="printUrl4" style="display: none;"></a>
				<a href="characterization.do?dispatch=summaryExport&sampleId=11337747&type=ex vivo" id="exportUrl4"
					style="display: none;"></a>
			</li>

		<li>
			<a href="characterization.do?dispatch=setupNew&sampleId=11337747"><span>other</span>
			</a>
		</li>
	</ul>
</div>

	<div class="shadetabs" id="summaryTab1"
		style="display: none;">
		<ul>
			<li>
				<a
					href="javascript:showSummary('ALL',4)"
					title="All"><span>&nbsp;All</span>
				</a>
			</li>







				<li class="selected">
					<a
						href="javascript:showSummary('1', 4)"
						title="physico-chemical characterization"><span>physico-chemical characterization</span> </a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary('2', 4)"
						title="in vitro characterization"><span>in vitro characterization</span> </a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary('3', 4)"
						title="in vivo characterization"><span>in vivo characterization</span> </a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary('4', 4)"
						title="ex vivo"><span>ex vivo</span> </a>
				</li>

			<li>
				<a
					href="characterization.do?dispatch=setupNew&sampleId=11337747"><span>other</span>
				</a>
			</li>
		</ul>
	</div>

	<div class="shadetabs" id="summaryTab2"
		style="display: none;">
		<ul>
			<li>
				<a
					href="javascript:showSummary('ALL',4)"
					title="All"><span>&nbsp;All</span>
				</a>
			</li>







				<li class="">
					<a
						href="javascript:showSummary('1', 4)"
						title="physico-chemical characterization"><span>physico-chemical characterization</span> </a>
				</li>







				<li class="selected">
					<a
						href="javascript:showSummary('2', 4)"
						title="in vitro characterization"><span>in vitro characterization</span> </a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary('3', 4)"
						title="in vivo characterization"><span>in vivo characterization</span> </a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary('4', 4)"
						title="ex vivo"><span>ex vivo</span> </a>
				</li>

			<li>
				<a
					href="characterization.do?dispatch=setupNew&sampleId=11337747"><span>other</span>
				</a>
			</li>
		</ul>
	</div>

	<div class="shadetabs" id="summaryTab3"
		style="display: none;">
		<ul>
			<li>
				<a
					href="javascript:showSummary('ALL',4)"
					title="All"><span>&nbsp;All</span>
				</a>
			</li>







				<li class="">
					<a
						href="javascript:showSummary('1', 4)"
						title="physico-chemical characterization"><span>physico-chemical characterization</span> </a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary('2', 4)"
						title="in vitro characterization"><span>in vitro characterization</span> </a>
				</li>







				<li class="selected">
					<a
						href="javascript:showSummary('3', 4)"
						title="in vivo characterization"><span>in vivo characterization</span> </a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary('4', 4)"
						title="ex vivo"><span>ex vivo</span> </a>
				</li>

			<li>
				<a
					href="characterization.do?dispatch=setupNew&sampleId=11337747"><span>other</span>
				</a>
			</li>
		</ul>
	</div>

	<div class="shadetabs" id="summaryTab4"
		style="display: none;">
		<ul>
			<li>
				<a
					href="javascript:showSummary('ALL',4)"
					title="All"><span>&nbsp;All</span>
				</a>
			</li>







				<li class="">
					<a
						href="javascript:showSummary('1', 4)"
						title="physico-chemical characterization"><span>physico-chemical characterization</span> </a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary('2', 4)"
						title="in vitro characterization"><span>in vitro characterization</span> </a>
				</li>







				<li class="">
					<a
						href="javascript:showSummary('3', 4)"
						title="in vivo characterization"><span>in vivo characterization</span> </a>
				</li>







				<li class="selected">
					<a
						href="javascript:showSummary('4', 4)"
						title="ex vivo"><span>ex vivo</span> </a>
				</li>

			<li>
				<a
					href="characterization.do?dispatch=setupNew&sampleId=11337747"><span>other</span>
				</a>
			</li>
		</ul>
	</div>

<table class="summaryViewNoTop" width="100%">

	<tr>
		<td>

				<table id="summarySectionHeader1" width="100%"
					align="center" style="display: block" class="summaryViewHeader">
					<tr>

							<td align="left">
								<b>physico-chemical characterization</b>
								<br />

									<a href="#relaxivity">relaxivity
										(1)</a> &nbsp;

									<a href="#size">size
										(1)</a> &nbsp;

							</td>

					</tr>
				</table>
				<div id="summaryHeaderSeparator1">
				</div>

				<table id="summarySectionHeader2" width="100%"
					align="center" style="display: block" class="summaryViewHeader">
					<tr>

					</tr>
				</table>
				<div id="summaryHeaderSeparator2">
				</div>

				<table id="summarySectionHeader3" width="100%"
					align="center" style="display: block" class="summaryViewHeader">
					<tr>

					</tr>
				</table>
				<div id="summaryHeaderSeparator3">
				</div>

				<table id="summarySectionHeader4" width="100%"
					align="center" style="display: block" class="summaryViewHeader">
					<tr>

					</tr>
				</table>
				<div id="summaryHeaderSeparator4">
				</div>

			<br />

				<table id="summarySection1" width="100%" align="center"
					style="display: block" class="summaryViewNoGrid">
					<tr>
						<th align="left">
							<span class="summaryViewHeading">physico-chemical characterization</span>&nbsp;&nbsp;
							<a
								href="characterization.do?dispatch=setupNew&fromStudyPage=true&sampleId=11337747&charType=physico-chemical characterization"
								class="addlink"><img align="middle" src="images/btn_add.gif"
									border="0" /></a>

						</th>
					</tr>
					<tr>
						<td>



										<a name="relaxivity"></a>
										<table width="99%" align="center" class="summaryViewNoGrid" bgcolor="#dbdbdb">
											<tr>
												<th align="left">
													relaxivity
												</th>
											</tr>
											<tr>
												<td>










<script type="text/javascript" src="javascript/SampleManager.js"></script>
<script type="text/javascript"
	src="/caNanoLab/dwr/interface/SampleManager.js"></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>






<table class="summaryViewNoGrid" width="99%" align="center"
	bgcolor="#F5F5f5">
	<tr>
		<td></td>
		<td width="95%"></td>
		<td align="right">
			<a
				href="characterization.do?dispatch=setupUpdate&sampleId=11337747&charId=15499607&charClassName=Relaxivity&charType=physico-chemical characterization">Edit</a>
		</td>
	</tr>
			    <tr>
				<td class="cellLabel" width="10%">
					Sample Names
				</td>
				<td>
					MIT_MGH-KKellyIB2009-01, MIT_MGH-KKellyIB2009-02
				</td>
			</tr>

	<tr>
		<td class="cellLabel" width="10%">
			Assay Type
		</td>
		<td colspan="2">


					relaxivity



		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Point of Contact
		</td>
		<td colspan="2">


					MIT_MGH (Stanley Y Shaw)



		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Characterization Date
		</td>
		<td colspan="2">



						N/A


		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Protocol
		</td>
		<td colspan="2">



						N/A



		</td>
	</tr>

	<tr>
		<td class="cellLabel" width="10%">
			Design Description
		</td>
		<td colspan="2">


					37 C  0.5 T



		</td>
	</tr>

	<tr>
		<td class="cellLabel" width="10%">
			Techniques and Instruments
		</td>
		<td colspan="2">


				N/A


		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Characterization Results
		</td>
		<td colspan="2">








	<table align="center" width="100%" class="summaryViewNoGrid">
		<tr>
			<td class="cellLabel">
				Data and Conditions
			</td>
		</tr>
		<tr>
			<td>


						<table class="summaryViewWithGrid" align="left">
																<tr>

																	<td class="cellLabel">
																		R1
																		<br>
																		(1/mM 1/s)
																	</td>

																	<td class="cellLabel">
																		R2
																		<br>
																		(1/mM 1/s)
																	</td>
																	<td class="cellLabel">
																		Sample Name
																	</td>
																</tr>

																<tr>

																	<td>
																		21.0
																	</td>

																	<td>
																		62.0
																	</td>
																	<td>
																		MIT_MGH-KKellyIB2009-01
																	</td>
																</tr>
																<tr>

																	<td>
																		21.0
																	</td>

																	<td>
																		62.0
																	</td>
																	<td>
																		MIT_MGH-KKellyIB2009-02
																	</td>
																</tr>

															</table>



			</td>
		</tr>
		<tr>
			<td class="cellLabel">

			</td>
		</tr>
		<tr>
			<td>



			</td>
		</tr>
	</table>

<br>



		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Analysis and Conclusion
		</td>
		<td colspan="2">


				N/A

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
										<br/>

										<a name="size"></a>
										<table width="99%" align="center" class="summaryViewNoGrid" bgcolor="#dbdbdb">
											<tr>
												<th align="left">
													size
												</th>
											</tr>
											<tr>
												<td>










<script type="text/javascript" src="javascript/SampleManager.js"></script>
<script type="text/javascript"
	src="/caNanoLab/dwr/interface/SampleManager.js"></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>






<table class="summaryViewNoGrid" width="99%" align="center"
	bgcolor="#F5F5f5">
	<tr>
		<td></td>
		<td width="95%"></td>
		<td align="right">
			<a
				href="characterization.do?dispatch=setupUpdate&sampleId=11337747&charId=11567108&charClassName=Size&charType=physico-chemical characterization">Edit</a>
		</td>
	</tr>
			    <tr>
				<td class="cellLabel" width="10%">
					Sample Names
				</td>
				<td>
					MIT_MGH-KKellyIB2009-01, MIT_MGH-KKellyIB2009-02
				</td>
			</tr>

	<tr>
		<td class="cellLabel" width="10%">
			Assay Type
		</td>
		<td colspan="2">





						N/A



		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Point of Contact
		</td>
		<td colspan="2">


					MIT_MGH



		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Characterization Date
		</td>
		<td colspan="2">



						N/A


		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Protocol
		</td>
		<td colspan="2">



						N/A



		</td>
	</tr>

	<tr>
		<td class="cellLabel" width="10%">
			Design Description
		</td>
		<td colspan="2">


					Overall size (volume weighted) in aqueous solution of dextran coated iron oxide particle



		</td>
	</tr>

	<tr>
		<td class="cellLabel" width="10%">
			Techniques and Instruments
		</td>
		<td colspan="2">


				N/A


		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Characterization Results
		</td>
		<td colspan="2">








	<table align="center" width="100%" class="summaryViewNoGrid">
		<tr>
			<td class="cellLabel">
				Data and Conditions
			</td>
		</tr>
		<tr>
			<td>


					<table class="summaryViewWithGrid" align="left">
																<tr>

																	<td class="cellLabel">
																		Diameter
																		<br>
																		(nm)
																	</td>
																	<td class="cellLabel">
																		Sample Name
																	</td>
																</tr>

																<tr>

																	<td>
																		38.0
																	</td>
																	<td>
																		MIT_MGH-KKellyIB2009-01
																	</td>
																</tr>
																<tr>

																	<td>
																		38.0
																	</td>
																	<td>
																		MIT_MGH-KKellyIB2009-02
																	</td>
																</tr>

															</table>


			</td>
		</tr>
		<tr>
			<td class="cellLabel">

			</td>
		</tr>
		<tr>
			<td>



			</td>
		</tr>
	</table>

<br>



		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Analysis and Conclusion
		</td>
		<td colspan="2">


				N/A

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
										<br/>




						</td>
					</tr>
				</table>
				<div id="summarySeparator1">
					<br>
				</div>

				<table id="summarySection2" width="100%" align="center"
					style="display: block" class="summaryViewNoGrid">
					<tr>
						<th align="left">
							<span class="summaryViewHeading">in vitro characterization</span>&nbsp;&nbsp;
							<a
								href="characterization.do?dispatch=setupNew&sampleId=11337747&charType=in vitro characterization"
								class="addlink"><img align="middle" src="images/btn_add.gif"
									border="0" /></a>

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

				<table id="summarySection3" width="100%" align="center"
					style="display: block" class="summaryViewNoGrid">
					<tr>
						<th align="left">
							<span class="summaryViewHeading">in vivo characterization</span>&nbsp;&nbsp;
							<a
								href="characterization.do?dispatch=setupNew&sampleId=11337747&charType=in vivo characterization"
								class="addlink"><img align="middle" src="images/btn_add.gif"
									border="0" /></a>

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
				<div id="summarySeparator3">
					<br>
				</div>

				<table id="summarySection4" width="100%" align="center"
					style="display: block" class="summaryViewNoGrid">
					<tr>
						<th align="left">
							<span class="summaryViewHeading">ex vivo</span>&nbsp;&nbsp;
							<a
								href="characterization.do?dispatch=setupNew&sampleId=11337747&charType=ex vivo"
								class="addlink"><img align="middle" src="images/btn_add.gif"
									border="0" /></a>

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

		</td>
	</tr>
</table>