<%--L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L--%>

<table class="gridtableHomePage">
	<tr>
		<th class="dataTypeHomePage" scope="col">
			Data Type
		</th>
		<th id="results" scope="col">
			Public Results
		</th>
	</tr>
	<tr align="left">
		<td>
			<table class="gridtableNoBorder" summary="layout">
				<tr>
					<td rowspan="2">
						<a href="#" onclick="gotoProtocols('setup')"> <img
								src="images/icon_protocol_48x.jpg" class="imgNoBorder" alt="Search Protocols" /></a>
					</td>
					<td>
						<a href="#" onclick="gotoProtocols('setup')"><b>Search
								Protocols</b></a>
					</td>
				</tr>
				<tr>
					<td>
						Search for nanotechnology protocols leveraged in performing	nanomaterial characterization assays.
					</td>
				</tr>
			</table>
		</td>
		<td>
			<span class="counts" id="protocolCount"><a href="javascript:gotoProtocols('search')">
				<c:out value="${publicCounts.numOfPublicProtocols}"/></a></span>
		</td>
	</tr>
	<tr class="alt">
		<td>
			<table class="gridtableNoBorder" summary="layout">
				<tr>
					<td rowspan="2">
						<a href="#" onclick="gotoSamples('setup')"> <img
								src="images/icon_nanoparticle_48x.jpg"
								 class="imgNoBorder" alt="Search Samples" /></a>
					</td>
					<td>
						<a href="#" onclick="gotoSamples('setup')"><b>Search
								Samples</b> </a>
					</td>
				</tr>
				<tr>					
					<td>
						Search for information on nanomaterials including the composition
						of the nanomaterial, results of physico-chemical,
						<i>in vitro</i>, and other characterizations, and associated
						publications.
						<br>
						See also
						<a href="advancedSampleSearch.do?dispatch=setup&page=0" id="advanceSearch">Advanced
							Sample Search</a>.
					</td>
				</tr>
			</table>
		</td>
		<td>			
			<div id="sampleCounts" class="counts" >
			    <span id="sampleCount"><a href="javascript:gotoSamples('search')"><c:out value="${publicCounts.numOfPublicSamples}"/></a></span>
					<br>
				<div id="sampleRelatedCounts" class="indented1">
					<c:if test="${publicCounts.numOfPublicSources>0}"> 
					    <span id="sampleSourceCount"><c:out value="${publicCounts.numOfPublicSources}"/> Sample Sources</span><br>
					</c:if> 
					<c:if test="${publicCounts.numOfPublicCharacterizations>0}"> 
					   <span id="CharacterizationCount"><c:out value="${publicCounts.numOfPublicCharacterizations}"/> Characterizations</span><br>					
					   <div class="indented2">
					      <c:if test="${publicCounts.numOfPublicPhysicoChemicalCharacterizations>0}"> 
						     <span id="PhysicoChemicalCharacterizationCount"><c:out value="${publicCounts.numOfPublicPhysicoChemicalCharacterizations}"/> Physico-chemical</span><br> 
						  </c:if>
						  <c:if test="${publicCounts.numOfPublicInvitroCharacterizations>0}"> 
						     <span id="InvitroCharacterizationCount"><c:out value="${publicCounts.numOfPublicInvitroCharacterizations}"/> In Vitro</span><br>
						  </c:if>
						  <c:if test="${publicCounts.numOfPublicInvivoCharacterizations>0}"> 
						     <span id="InvivoCharacterizationCount"><c:out value="${publicCounts.numOfPublicInvivoCharacterizations}"/> In Vivo</span><br>
						  </c:if> 
						  <c:if test="${publicCounts.numOfPublicOtherCharacterizations>0}">
						     <span id="OtherCharacterizationCount"><c:out value="${publicCounts.numOfPublicOtherCharacterizations}"/> Other</span><br>
						  </c:if>
					   </div>
					</c:if>
				</div>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<table class="gridtableNoBorder" summary="layout">
				<tr>
					<td rowspan="2">
						<a href="#" onclick="gotoPublications('setup')"> <img
								src="images/icon_report_48x.gif" class="imgNoBorder"
								alt="Search Publications" /> </a>
					</td>
					<td>
						<a href="#" onclick=gotoPublications('setup');><b>Search
								Publications</b> </a>
					</td>
				</tr>
				<tr>
					<td>
						Search for information on nanotechnology publications including
						peer reviewed articles, reviews, and other types of reports
						related to the use of nanotechnology in biomedicine.
					</td>
				</tr>
			</table>
		</td>
		<td>
			<span id="publicationCount" class="counts"><a href="javascript:gotoPublications('search')"><c:out value="${publicCounts.numOfPublicPublications}"/></a></span>
		</td>
	</tr>
</table>
<div class="formMessage" align="center">Last updated on <c:out value="${publicCounts.countDateString}"/></div>