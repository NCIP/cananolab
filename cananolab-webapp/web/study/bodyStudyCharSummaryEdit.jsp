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
			<a
				href="javascript:showSummary('ALL', 4})"
				title="All"><span>&nbsp;All&nbsp;&nbsp;</span></a>
		</li>
		<li>
			<a
				href="javascript:showSummary('physico chemical characterization', 4)"
				title="physico chemical characterization"><span>physico chemical characterization</span></a>
		</li>
		<li>
			<a
				href="javascript:showSummary('invitro characterization', 4)"
				title="invitro characterization"><span>invitro characterization</span></a>
		</li>
		<li>
			<a
				href="javascript:showSummary('invivo characterization', 4)"
				title="invivo characterization"><span>invivo characterization</span></a>
		</li>
		<li>
			<a href="characterization.do?dispatch=setupNew&sampleId=${sampleId }"><span>other</span>
			</a>
		</li>
	</ul>
</div>
<table class="summaryViewNoGrid" width="100%">
	<tr>
		<td>
		<table id="summarySection${ind.count}" width="100%" align="center"
			style="display: block" class="summaryViewLayer2">
			<tr>
				<th align="left">
					physico chemical characterization&nbsp;&nbsp;&nbsp;
					<a
						href="study.do?dispatch=charEdit"
						class="addlink"><img align="middle" src="images/btn_add.gif"
							border="0" /></a>&nbsp;&nbsp;
				</th>
			</tr>
			<tr>
				<td>
					<table class="summaryViewLayer3" width="95%" align="center">
					<tr>
						<th align="left" colspan="2" width="90%">
							size
						</th>
						<th align="right">
							<a href="study.do?dispatch=charEdit">Edit</a>
						</th>
					</tr>
					<tr>
						<td class="cellLabel" width="10%">
							Assay Type
						</td>
						<td colspan="2">size</td>
					</tr>
					<tr>
						<td class="cellLabel" width="10%">
							Sample Name
						</td>
						<td colspan="2">MIT_MGH-AWangCMC2008-01<br>MIT_MGH-AWangCMC2008-03</td>
					</tr>
					<tr>
						<td class="cellLabel" width="10%">
							Point of Contact
						</td>
						<td colspan="2">WUSTL</td>
					</tr>
					<tr>
						<td class="cellLabel" width="10%">
							Characterization Date
						</td>
						<td colspan="2">N/A</td>
					</tr>
					<tr>
						<td class="cellLabel" width="10%">
							Design Description
						</td>
						<td colspan="2">N/A</td>
					</tr>
					<tr>
						<td class="cellLabel" width="10%">
							Techniques and Instruments
						</td>
						<td colspan="2">
							<table class="summaryViewLayer4" align="center" width="95%">
								<tr>
									<th width="33%">
										Technique
									</th>
									<th width="33%">
										Instruments
									</th>
									<th>
										Description
									</th>
									<th>
									</th>
								</tr>
								<tr>
									<td>dynamic light scattering(DLS)</td>
									<td></td>
									<td>Zetasizer 4</td>
									<td align="right"></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="cellLabel" width="10%">
							Characterization Results
						</td>
						<td colspan="2">
						<table class="summaryViewLayer4" align="center" width="95%">
								<tr>
									<td>
										<b>Data and Conditions</b>
									</td>
								</tr>
								<tr>
									<td>
										<table class="summaryViewLayer4" width="95%" align="center">
											<tr>
												<th>
													<strong>size (RMS,nm)</strong>
												</th>
												<th>
													<strong>Source Name</strong>
												</th>
											</tr>
											<tr>
												<td>179.0</td>
												<td>MIT_MGH-AWangCMC2008-01</td>
											</tr>
											<tr>
												<td>129.0</td>
												<td>MIT_MGH-AWangCMC2008-03</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<b>Files</b>
									</td>
								</tr>
								<tr>
									<td>
										N/A
									</td>
								</tr>
							</table>
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
					<br>
				</td>
			</tr>
		</table>
		<div id="summarySeparator3">
			<br>
		</div>
		<table id="summarySection${ind.count}" width="100%" align="center"
			style="display: block" class="summaryViewLayer2">
			<tr>
				<th align="left">
					invitro characterization&nbsp;&nbsp;&nbsp;
					<a
						href="study.do?dispatch=charEdit"
						class="addlink"><img align="middle" src="images/btn_add.gif"
							border="0" /></a>&nbsp;&nbsp;
				</th>
			</tr>
			<tr>
				<td>
					<table class="summaryViewLayer3" width="95%" align="center">
					<tr>
						<th align="left" colspan="2" width="90%">
							targeting
						</th>
						<th align="right">
							<a href="study.do?dispatch=charEdit">Edit</a>
						</th>
					</tr>
					<tr>
						<td class="cellLabel" width="10%">
							Assay Type
						</td>
						<td colspan="2">immunoassay</td>
					</tr>
					<tr>
						<td class="cellLabel" width="10%">
							Sample Name
						</td>
						<td colspan="2">MIT_MGH-AWangCMC2008-01<br>MIT_MGH-AWangCMC2008-03</td>
					</tr>
					<tr>
						<td class="cellLabel" width="10%">
							Point of Contact
						</td>
						<td colspan="2">WUSTL</td>
					</tr>
					<tr>
						<td class="cellLabel" width="10%">
							Characterization Date
						</td>
						<td colspan="2">N/A</td>
					</tr>
					<tr>
						<td class="cellLabel" width="10%">
							Design Description
						</td>
						<td colspan="2">N/A</td>
					</tr>
					<tr>
						<td class="cellLabel" width="10%">
							Techniques and Instruments
						</td>
						<td colspan="2">
							<table class="summaryViewLayer4" align="center" width="95%">
								<tr>
									<th width="33%">
										Technique
									</th>
									<th width="33%">
										Instruments
									</th>
									<th>
										Description
									</th>
									<th>
									</th>
								</tr>
								<tr>
									<td>dynamic light scattering(DLS)</td>
									<td></td>
									<td>Zetasizer 4</td>
									<td align="right"></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="cellLabel" width="10%">
							Characterization Results
						</td>
						<td colspan="2">
						<table class="summaryViewLayer4" align="center" width="95%">
								<tr>
									<td>
										<b>Data and Conditions</b>
									</td>
								</tr>
								<tr>
									<td>
										<table class="summaryViewLayer4" width="95%" align="center">
											<tr>
												<th>
													<strong>sample concentration (nM)</strong>
												</th>
												<th>
													<strong>sample concentration after treatment (mg/mL) (Mean (M),pM)</strong>
												</th>
											</tr>
											<tr>
												<td>12</td>
												<td>1122.235</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<b>Files</b>
									</td>
								</tr>
								<tr>
									<td>
										N/A
									</td>
								</tr>
							</table>
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
					<br>
				</td>
			</tr>
		</table>
		<div id="summarySeparator4">
			<br>
		</div>
		<table id="summarySection3" style="display: block;" class="summaryViewLayer2" align="center" width="100%">
			<tbody><tr>
				<th align="left">
					invivo characterization &nbsp;&nbsp;&nbsp;
					<a href="characterization.do?dispatch=setupNew&amp;sampleId=3735562&amp;charType=invivo%20characterization" class="addlink"><img src="images/btn_add.gif" align="middle" border="0"></a>&nbsp;&nbsp;
				</th>
			</tr>
			<tr>
				<td>
							<div class="indented4">
								N/A
							</div>
					<br>
				</td>
			</tr>
		</tbody>
		</table>
		<div id="summarySeparator3">
			<br>
		</div>
		<table id="summarySection4" style="display: block;" class="summaryViewLayer2" align="center" width="100%">
			<tbody>
			<tr>
				<th align="left">
					other &nbsp;&nbsp;&nbsp;
					<a href="characterization.do?dispatch=setupNew&amp;sampleId=3735562&amp;charType=ex%20vivo" class="addlink"><img src="images/btn_add.gif" align="middle" border="0"></a>&nbsp;&nbsp;
				</th>
			</tr>
			<tr>
				<td>
							<div class="indented4">
								N/A
							</div>
					<br>
				</td>
			</tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
