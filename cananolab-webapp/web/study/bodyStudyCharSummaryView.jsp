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
	<jsp:param name="pageTitle" value="WUSTL Study Efficacy of nanoparticle Characterization" />
	<jsp:param name="topic" value="submit_study_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<jsp:include page="/bodyMessage.jsp?bundle=study" />
<div class="shadetabs" id="summaryTabALL">
	<ul>
		<li class="selected">
			<a	href="javascript:showSummary('ALL', 1)"
				title="All"><span>All</span></a>
		</li>

			<li>
				<a
					href="javascript:showSummary('1', 1)"
					title="physico-chemical characterization"><span>physico-chemical characterization</span>
				</a>
				<a href="javascript:printPage('characterization.do?dispatch=summaryPrint&sampleId=11337747&type=physico-chemical characterization')"
					id="printUrl1" style="display: none;"></a>
				<a href="characterization.do?dispatch=summaryExport&sampleId=11337747&type=physico-chemical characterization" id="exportUrl1"
					style="display: none;"></a>
			</li>

	</ul>
</div>

	<div class="shadetabs" id="summaryTab1"
		style="display: none;">
		<ul>
			<li>
				<a	href="javascript:showSummary('ALL',1)"
					title="All"><span>All</span></a>
			</li>







				<li class="selected">
					<a	href="javascript:showSummary('1', 1)"
						title="physico-chemical characterization"><span>physico-chemical characterization</span></a>
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

			<br/>







	<table id="summarySection1" width="100%" align="center"
		style="display: block" class="summaryViewNoGrid">
		<tr>
			<th align="left">
				<span class="summaryViewHeading">physico-chemical characterization</span>
			</th>
		</tr>
		<tr>
			<td>

					<a name="relaxivity"></a>
					<table width="99%" align="center" class="summaryViewNoGrid"
						bgcolor="#dbdbdb">
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
				<td class="cellLabel" width="10%">
					Assay Type
				</td>
				<td>
					relaxivity
				</td>
			</tr>




		<tr>
			<td class="cellLabel" width="10%">
				Point of Contact
			</td>
			<td>
				MIT_MGH (Stanley Y Shaw)
			</td>
		</tr>





		<tr>
			<td class="cellLabel" width="10%">
				Design Description
			</td>
			<td>


						37 C  0.5 T



			</td>
		</tr>



		<tr>
			<td class="cellLabel" width="10%">
				Characterization Results
			</td>
			<td>






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
										R1<br>(1/mM 1/s)
									</td>

									<td class="cellLabel">
										R2<br>(1/mM 1/s)
									</td>

							</tr>

								<tr>

										<td>
											21.0
										</td>

										<td>
											62.0
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
					<table width="99%" align="center" class="summaryViewNoGrid"
						bgcolor="#dbdbdb">
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
			<td class="cellLabel" width="10%">
				Point of Contact
			</td>
			<td>
				MIT_MGH
			</td>
		</tr>





		<tr>
			<td class="cellLabel" width="10%">
				Design Description
			</td>
			<td>


						Overall size (volume weighted) in aqueous solution of dextran coated iron oxide particle



			</td>
		</tr>



		<tr>
			<td class="cellLabel" width="10%">
				Characterization Results
			</td>
			<td>






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
										Diameter<br>(nm)
									</td>

							</tr>

								<tr>

										<td>
											38.0
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
		<tr>
			<th valign="top" align="left" height="6">
			</th>
		</tr>
	</table>
	<div id="summarySeparator1">
		<br>
	</div>

		</td>
	</tr>
</table>
