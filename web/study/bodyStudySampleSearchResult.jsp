<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<link rel="StyleSheet" type="text/css" href="css/displaytag.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<%--TODO: create online help topic for this page.--%>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Study Sample Search Result" />
	<jsp:param name="topic" value="submit_study_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<jsp:include page="/bodyMessage.jsp?bundle=study" />
<table width="100%" align="center">
	<tr>
		<td colspan="2">
			<table class="summaryViewLayer4" width="100%">
				<tr>
					<th style="text-align: center">
						Selected Criteria
					</th>
					<td style="text-align: right">
							<a href="advancedSampleSearch.do?dispatch=validateSetup">Edit</a>
					</td>
				</tr>
				<tr>
					<td>
						(sample name contains MIT<br>and<br>point of contact name contains mit)
					</td>
					<td></td>
				</tr>
			</table>
			<span class="pagebanner">150 items found, displaying 1 to 25.</span><span class="pagelinks">[First/Prev] <strong>1</strong>, <a href="advancedSampleSearch.do?d-446960-p=2&amp;searchBean.theSampleQuery.operand=&amp;searchBean.theCharacterizationQuery.datumName=&amp;searchBean.theCharacterizationQuery.characterizationName=&amp;searchBean.theCharacterizationQuery.datumValueUnit=&amp;searchBean.compositionLogicalOperator=and&amp;searchBean.theCompositionQuery.id=&amp;searchBean.theCharacterizationQuery.datumValue=&amp;searchBean.theCharacterizationQuery.datumValue=&amp;searchBean.theCompositionQuery.compositionType=&amp;searchBean.sampleLogicalOperator=and&amp;searchBean.theCharacterizationQuery.characterizationType=&amp;searchBean.theCharacterizationQuery.operand=&amp;searchBean.logicalOperator=or&amp;dispatch=search&amp;searchBean.theSampleQuery.id=&amp;page=1&amp;searchBean.theSampleQuery.name=&amp;searchBean.theCompositionQuery.entityType=&amp;searchBean.theCharacterizationQuery.id=&amp;searchBean.theCompositionQuery.chemicalName=&amp;searchBean.theSampleQuery.nameType=&amp;searchBean.theCompositionQuery.operand=&amp;searchBean.characterizationLogicalOperator=and" title="Go to page 2">2</a>, <a href="advancedSampleSearch.do?d-446960-p=3&amp;searchBean.theSampleQuery.operand=&amp;searchBean.theCharacterizationQuery.datumName=&amp;searchBean.theCharacterizationQuery.characterizationName=&amp;searchBean.theCharacterizationQuery.datumValueUnit=&amp;searchBean.compositionLogicalOperator=and&amp;searchBean.theCompositionQuery.id=&amp;searchBean.theCharacterizationQuery.datumValue=&amp;searchBean.theCharacterizationQuery.datumValue=&amp;searchBean.theCompositionQuery.compositionType=&amp;searchBean.sampleLogicalOperator=and&amp;searchBean.theCharacterizationQuery.characterizationType=&amp;searchBean.theCharacterizationQuery.operand=&amp;searchBean.logicalOperator=or&amp;dispatch=search&amp;searchBean.theSampleQuery.id=&amp;page=1&amp;searchBean.theSampleQuery.name=&amp;searchBean.theCompositionQuery.entityType=&amp;searchBean.theCharacterizationQuery.id=&amp;searchBean.theCompositionQuery.chemicalName=&amp;searchBean.theSampleQuery.nameType=&amp;searchBean.theCompositionQuery.operand=&amp;searchBean.characterizationLogicalOperator=and" title="Go to page 3">3</a>, <a href="advancedSampleSearch.do?d-446960-p=4&amp;searchBean.theSampleQuery.operand=&amp;searchBean.theCharacterizationQuery.datumName=&amp;searchBean.theCharacterizationQuery.characterizationName=&amp;searchBean.theCharacterizationQuery.datumValueUnit=&amp;searchBean.compositionLogicalOperator=and&amp;searchBean.theCompositionQuery.id=&amp;searchBean.theCharacterizationQuery.datumValue=&amp;searchBean.theCharacterizationQuery.datumValue=&amp;searchBean.theCompositionQuery.compositionType=&amp;searchBean.sampleLogicalOperator=and&amp;searchBean.theCharacterizationQuery.characterizationType=&amp;searchBean.theCharacterizationQuery.operand=&amp;searchBean.logicalOperator=or&amp;dispatch=search&amp;searchBean.theSampleQuery.id=&amp;page=1&amp;searchBean.theSampleQuery.name=&amp;searchBean.theCompositionQuery.entityType=&amp;searchBean.theCharacterizationQuery.id=&amp;searchBean.theCompositionQuery.chemicalName=&amp;searchBean.theSampleQuery.nameType=&amp;searchBean.theCompositionQuery.operand=&amp;searchBean.characterizationLogicalOperator=and" title="Go to page 4">4</a>, <a href="advancedSampleSearch.do?d-446960-p=5&amp;searchBean.theSampleQuery.operand=&amp;searchBean.theCharacterizationQuery.datumName=&amp;searchBean.theCharacterizationQuery.characterizationName=&amp;searchBean.theCharacterizationQuery.datumValueUnit=&amp;searchBean.compositionLogicalOperator=and&amp;searchBean.theCompositionQuery.id=&amp;searchBean.theCharacterizationQuery.datumValue=&amp;searchBean.theCharacterizationQuery.datumValue=&amp;searchBean.theCompositionQuery.compositionType=&amp;searchBean.sampleLogicalOperator=and&amp;searchBean.theCharacterizationQuery.characterizationType=&amp;searchBean.theCharacterizationQuery.operand=&amp;searchBean.logicalOperator=or&amp;dispatch=search&amp;searchBean.theSampleQuery.id=&amp;page=1&amp;searchBean.theSampleQuery.name=&amp;searchBean.theCompositionQuery.entityType=&amp;searchBean.theCharacterizationQuery.id=&amp;searchBean.theCompositionQuery.chemicalName=&amp;searchBean.theSampleQuery.nameType=&amp;searchBean.theCompositionQuery.operand=&amp;searchBean.characterizationLogicalOperator=and" title="Go to page 5">5</a>, <a href="advancedSampleSearch.do?d-446960-p=6&amp;searchBean.theSampleQuery.operand=&amp;searchBean.theCharacterizationQuery.datumName=&amp;searchBean.theCharacterizationQuery.characterizationName=&amp;searchBean.theCharacterizationQuery.datumValueUnit=&amp;searchBean.compositionLogicalOperator=and&amp;searchBean.theCompositionQuery.id=&amp;searchBean.theCharacterizationQuery.datumValue=&amp;searchBean.theCharacterizationQuery.datumValue=&amp;searchBean.theCompositionQuery.compositionType=&amp;searchBean.sampleLogicalOperator=and&amp;searchBean.theCharacterizationQuery.characterizationType=&amp;searchBean.theCharacterizationQuery.operand=&amp;searchBean.logicalOperator=or&amp;dispatch=search&amp;searchBean.theSampleQuery.id=&amp;page=1&amp;searchBean.theSampleQuery.name=&amp;searchBean.theCompositionQuery.entityType=&amp;searchBean.theCharacterizationQuery.id=&amp;searchBean.theCompositionQuery.chemicalName=&amp;searchBean.theSampleQuery.nameType=&amp;searchBean.theCompositionQuery.operand=&amp;searchBean.characterizationLogicalOperator=and" title="Go to page 6">6</a> [<a href="advancedSampleSearch.do?d-446960-p=2&amp;searchBean.theSampleQuery.operand=&amp;searchBean.theCharacterizationQuery.datumName=&amp;searchBean.theCharacterizationQuery.characterizationName=&amp;searchBean.theCharacterizationQuery.datumValueUnit=&amp;searchBean.compositionLogicalOperator=and&amp;searchBean.theCompositionQuery.id=&amp;searchBean.theCharacterizationQuery.datumValue=&amp;searchBean.theCharacterizationQuery.datumValue=&amp;searchBean.theCompositionQuery.compositionType=&amp;searchBean.sampleLogicalOperator=and&amp;searchBean.theCharacterizationQuery.characterizationType=&amp;searchBean.theCharacterizationQuery.operand=&amp;searchBean.logicalOperator=or&amp;dispatch=search&amp;searchBean.theSampleQuery.id=&amp;page=1&amp;searchBean.theSampleQuery.name=&amp;searchBean.theCompositionQuery.entityType=&amp;searchBean.theCharacterizationQuery.id=&amp;searchBean.theCompositionQuery.chemicalName=&amp;searchBean.theSampleQuery.nameType=&amp;searchBean.theCompositionQuery.operand=&amp;searchBean.characterizationLogicalOperator=and">Next</a>/<a href="advancedSampleSearch.do?d-446960-p=6&amp;searchBean.theSampleQuery.operand=&amp;searchBean.theCharacterizationQuery.datumName=&amp;searchBean.theCharacterizationQuery.characterizationName=&amp;searchBean.theCharacterizationQuery.datumValueUnit=&amp;searchBean.compositionLogicalOperator=and&amp;searchBean.theCompositionQuery.id=&amp;searchBean.theCharacterizationQuery.datumValue=&amp;searchBean.theCharacterizationQuery.datumValue=&amp;searchBean.theCompositionQuery.compositionType=&amp;searchBean.sampleLogicalOperator=and&amp;searchBean.theCharacterizationQuery.characterizationType=&amp;searchBean.theCharacterizationQuery.operand=&amp;searchBean.logicalOperator=or&amp;dispatch=search&amp;searchBean.theSampleQuery.id=&amp;page=1&amp;searchBean.theSampleQuery.name=&amp;searchBean.theCompositionQuery.entityType=&amp;searchBean.theCharacterizationQuery.id=&amp;searchBean.theCompositionQuery.chemicalName=&amp;searchBean.theSampleQuery.nameType=&amp;searchBean.theCompositionQuery.operand=&amp;searchBean.characterizationLogicalOperator=and">Last</a>]</span>
<table class="displaytable" id="sample">
<thead>
<tr>
<th class="sortable">
<a href="advancedSampleSearch.do?d-446960-o=2&amp;searchBean.theCharacterizationQuery.datumName=&amp;searchBean.theSampleQuery.operand=&amp;searchBean.theCharacterizationQuery.datumValueUnit=&amp;searchBean.theCharacterizationQuery.characterizationName=&amp;searchBean.theCompositionQuery.id=&amp;searchBean.compositionLogicalOperator=and&amp;searchBean.theCharacterizationQuery.datumValue=&amp;searchBean.theCharacterizationQuery.datumValue=&amp;searchBean.sampleLogicalOperator=and&amp;searchBean.theCompositionQuery.compositionType=&amp;searchBean.theCharacterizationQuery.characterizationType=&amp;d-446960-s=0&amp;searchBean.theSampleQuery.id=&amp;dispatch=search&amp;searchBean.logicalOperator=or&amp;searchBean.theCharacterizationQuery.operand=&amp;page=1&amp;searchBean.theCharacterizationQuery.id=&amp;searchBean.theCompositionQuery.entityType=&amp;searchBean.theSampleQuery.name=&amp;searchBean.theCompositionQuery.chemicalName=&amp;searchBean.theCompositionQuery.operand=&amp;searchBean.theSampleQuery.nameType=&amp;searchBean.characterizationLogicalOperator=and">Sample Name</a></th>
<th class="sortable">
<a href="advancedSampleSearch.do?d-446960-o=2&amp;searchBean.theCharacterizationQuery.datumName=&amp;searchBean.theSampleQuery.operand=&amp;searchBean.theCharacterizationQuery.datumValueUnit=&amp;searchBean.theCharacterizationQuery.characterizationName=&amp;searchBean.theCompositionQuery.id=&amp;searchBean.compositionLogicalOperator=and&amp;searchBean.theCharacterizationQuery.datumValue=&amp;searchBean.theCharacterizationQuery.datumValue=&amp;searchBean.sampleLogicalOperator=and&amp;searchBean.theCompositionQuery.compositionType=&amp;searchBean.theCharacterizationQuery.characterizationType=&amp;d-446960-s=1&amp;searchBean.theSampleQuery.id=&amp;dispatch=search&amp;searchBean.logicalOperator=or&amp;searchBean.theCharacterizationQuery.operand=&amp;page=1&amp;searchBean.theCharacterizationQuery.id=&amp;searchBean.theCompositionQuery.entityType=&amp;searchBean.theSampleQuery.name=&amp;searchBean.theCompositionQuery.chemicalName=&amp;searchBean.theCompositionQuery.operand=&amp;searchBean.theSampleQuery.nameType=&amp;searchBean.characterizationLogicalOperator=and">point of contact name</a></th>
<th class="sortable">
<a href="advancedSampleSearch.do?d-446960-o=2&amp;searchBean.theCharacterizationQuery.datumName=&amp;searchBean.theSampleQuery.operand=&amp;searchBean.theCharacterizationQuery.datumValueUnit=&amp;searchBean.theCharacterizationQuery.characterizationName=&amp;searchBean.theCompositionQuery.id=&amp;searchBean.compositionLogicalOperator=and&amp;searchBean.theCharacterizationQuery.datumValue=&amp;searchBean.theCharacterizationQuery.datumValue=&amp;searchBean.sampleLogicalOperator=and&amp;searchBean.theCompositionQuery.compositionType=&amp;searchBean.theCharacterizationQuery.characterizationType=&amp;d-446960-s=2&amp;searchBean.theSampleQuery.id=&amp;dispatch=search&amp;searchBean.logicalOperator=or&amp;searchBean.theCharacterizationQuery.operand=&amp;page=1&amp;searchBean.theCharacterizationQuery.id=&amp;searchBean.theCompositionQuery.entityType=&amp;searchBean.theSampleQuery.name=&amp;searchBean.theCompositionQuery.chemicalName=&amp;searchBean.theCompositionQuery.operand=&amp;searchBean.theSampleQuery.nameType=&amp;searchBean.characterizationLogicalOperator=and">Site</a></th></tr></thead>
<tbody>
<tr class="odd">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=10780672&location=WUSTL>MIT_MGH-AWangCMC2008-01</a></td>
<td>

						
						    MIT_MGH&nbsp; 						    
						 	<div id="details10780672:1:1"
								style="position: relative">
								
								<a id="detailLink10780672:1:1" href="#"
									onclick="showDetailView('10780672:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=10780672&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView10780672:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content10780672:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="even">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=10780673&location=WUSTL>MIT_MGH-AWangCMC2008-02</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details10780673:1:1"
								style="position: relative">
								
								<a id="detailLink10780673:1:1" href="#"
									onclick="showDetailView('10780673:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=10780673&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView10780673:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content10780673:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="odd">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=10780674&location=WUSTL>MIT_MGH-AWangCMC2008-03</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details10780674:1:1"
								style="position: relative">
								
								<a id="detailLink10780674:1:1" href="#"
									onclick="showDetailView('10780674:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=10780674&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView10780674:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content10780674:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="even">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=6782976&location=WUSTL>MIT_MGH-FGuPNAS2008-01</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details6782976:1:1"
								style="position: relative">
								
								<a id="detailLink6782976:1:1" href="#"
									onclick="showDetailView('6782976:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=6782976&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView6782976:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content6782976:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="odd">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=6782977&location=WUSTL>MIT_MGH-FGuPNAS2008-02</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details6782977:1:1"
								style="position: relative">
								
								<a id="detailLink6782977:1:1" href="#"
									onclick="showDetailView('6782977:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=6782977&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView6782977:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content6782977:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="even">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=5603329&location=WUSTL>MIT_MGH-FGuPNAS2008-03</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details5603329:1:1"
								style="position: relative">
								
								<a id="detailLink5603329:1:1" href="#"
									onclick="showDetailView('5603329:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=5603329&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView5603329:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content5603329:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="odd">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=6782978&location=WUSTL>MIT_MGH-FGuPNAS2008-04</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details6782978:1:1"
								style="position: relative">
								
								<a id="detailLink6782978:1:1" href="#"
									onclick="showDetailView('6782978:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=6782978&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView6782978:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content6782978:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="even">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=6782979&location=WUSTL>MIT_MGH-FGuPNAS2008-05</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details6782979:1:1"
								style="position: relative">
								
								<a id="detailLink6782979:1:1" href="#"
									onclick="showDetailView('6782979:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=6782979&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView6782979:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content6782979:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="odd">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=6782980&location=WUSTL>MIT_MGH-FGuPNAS2008-06</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details6782980:1:1"
								style="position: relative">
								
								<a id="detailLink6782980:1:1" href="#"
									onclick="showDetailView('6782980:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=6782980&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView6782980:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content6782980:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="even">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=9142272&location=WUSTL>MIT_MGH-FGuPNAS2008-07</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details9142272:1:1"
								style="position: relative">
								
								<a id="detailLink9142272:1:1" href="#"
									onclick="showDetailView('9142272:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=9142272&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView9142272:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content9142272:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="odd">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=5341184&location=WUSTL>MIT_MGH-JMcCarthyNL2005-01</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details5341184:1:1"
								style="position: relative">
								
								<a id="detailLink5341184:1:1" href="#"
									onclick="showDetailView('5341184:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=5341184&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView5341184:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content5341184:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="even">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=5341185&location=WUSTL>MIT_MGH-JMcCarthyNL2005-02</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details5341185:1:1"
								style="position: relative">
								
								<a id="detailLink5341185:1:1" href="#"
									onclick="showDetailView('5341185:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=5341185&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView5341185:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content5341185:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="odd">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=5341186&location=WUSTL>MIT_MGH-JMcCarthySmall2006-01</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details5341186:1:1"
								style="position: relative">
								
								<a id="detailLink5341186:1:1" href="#"
									onclick="showDetailView('5341186:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=5341186&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView5341186:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content5341186:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="even">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=11337747&location=WUSTL>MIT_MGH-KKellyIB2009-01</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details11337747:1:1"
								style="position: relative">
								
								<a id="detailLink11337747:1:1" href="#"
									onclick="showDetailView('11337747:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=11337747&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView11337747:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content11337747:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="odd">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=11337748&location=WUSTL>MIT_MGH-KKellyIB2009-02</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details11337748:1:1"
								style="position: relative">
								
								<a id="detailLink11337748:1:1" href="#"
									onclick="showDetailView('11337748:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=11337748&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView11337748:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content11337748:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="even">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=11337749&location=WUSTL>MIT_MGH-KKellyIB2009-03</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details11337749:1:1"
								style="position: relative">
								
								<a id="detailLink11337749:1:1" href="#"
									onclick="showDetailView('11337749:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=11337749&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView11337749:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content11337749:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="odd">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=11337750&location=WUSTL>MIT_MGH-KKellyIB2009-04</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details11337750:1:1"
								style="position: relative">
								
								<a id="detailLink11337750:1:1" href="#"
									onclick="showDetailView('11337750:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=11337750&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView11337750:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content11337750:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="even">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=11337751&location=WUSTL>MIT_MGH-KKellyIB2009-05</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details11337751:1:1"
								style="position: relative">
								
								<a id="detailLink11337751:1:1" href="#"
									onclick="showDetailView('11337751:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=11337751&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView11337751:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content11337751:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="odd">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=11337752&location=WUSTL>MIT_MGH-KKellyIB2009-06</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details11337752:1:1"
								style="position: relative">
								
								<a id="detailLink11337752:1:1" href="#"
									onclick="showDetailView('11337752:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=11337752&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView11337752:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content11337752:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="even">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=11337753&location=WUSTL>MIT_MGH-KKellyIB2009-07</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details11337753:1:1"
								style="position: relative">
								
								<a id="detailLink11337753:1:1" href="#"
									onclick="showDetailView('11337753:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=11337753&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView11337753:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content11337753:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="odd">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=11337754&location=WUSTL>MIT_MGH-KKellyIB2009-08</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details11337754:1:1"
								style="position: relative">
								
								<a id="detailLink11337754:1:1" href="#"
									onclick="showDetailView('11337754:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=11337754&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView11337754:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content11337754:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="even">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=11337755&location=WUSTL>MIT_MGH-KKellyIB2009-09</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details11337755:1:1"
								style="position: relative">
								
								<a id="detailLink11337755:1:1" href="#"
									onclick="showDetailView('11337755:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=11337755&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView11337755:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content11337755:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="odd">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=11337756&location=WUSTL>MIT_MGH-KKellyIB2009-10</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details11337756:1:1"
								style="position: relative">
								
								<a id="detailLink11337756:1:1" href="#"
									onclick="showDetailView('11337756:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=11337756&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView11337756:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content11337756:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="even">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=11337757&location=WUSTL>MIT_MGH-KKellyIB2009-11</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details11337757:1:1"
								style="position: relative">
								
								<a id="detailLink11337757:1:1" href="#"
									onclick="showDetailView('11337757:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=11337757&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView11337757:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content11337757:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr>
<tr class="odd">
<td><a href=sample.do?dispatch=summaryEdit&page=0&sampleId=11337758&location=WUSTL>MIT_MGH-KKellyIB2009-12</a></td>

<td>
						
						    MIT_MGH&nbsp; 						    
						 	<div id="details11337758:1:1"
								style="position: relative">
								
								<a id="detailLink11337758:1:1" href="#"
									onclick="showDetailView('11337758:1:1', 'sample.do?page=0&dispatch=setupView&sampleId=11337758&location=WUSTL'); return false;">Details</a>
								<table
									id="detailView11337758:1:1"
									style="display: none; position: absolute; left: -250px; top: 20px; z-index: 5; width:500px; font-size: 10px; background-color: #FFFFFF"
									class="promptbox">
									<tr>
										<td>
											<div
												id="content11337758:1:1"></div>
										</td>

									</tr>
								</table>
							</div>
							<br />
							<br />
						
					</td>
<td>WUSTL</td></tr></tbody></table>
		</td>
	</tr>
</table>
