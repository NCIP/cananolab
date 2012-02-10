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
			<img src="images/ajax-loader.gif" border="0" class="counts"
				id="protocolLoaderImg" alt="protocol count">
			<span class="counts" id="protocolCount"></span>
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
			<img src="images/ajax-loader.gif" border="0" class="counts"
				id="sampleLoaderImg" alt="sample count">
			<div id="sampleCounts" class="counts" >
			    <span id="sampleCount"></span>&nbsp; &nbsp; &nbsp; &nbsp;<a id="moreStats" onclick="getMoreSamplesStats()"><span
								class="moreLink">More Stats</span></a>
				<br />
				<img src="images/ajax-loader.gif" border="0" id="sampleRelatedLoaderImg" alt="sample related info count" style="display:none">
				<div id="sampleRelatedCounts" class="indented1">
					<span id="sampleSourceCount"></span><br> <span
						id="CharacterizationCount"></span><br>
					<div class="indented2">
						<span id="PhysicoChemicalCharacterizationCount"></span><br> <span
							id="InvitroCharacterizationCount"></span><br> <span
							id="InvivoCharacterizationCount"></span><br> <span
							id="OtherCharacterizationCount"></span><br>
					</div>
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
			<img src="images/ajax-loader.gif" border="0" class="counts"
				id="publicationLoaderImg" alt="publication count">
			<span id="publicationCount" class="counts"></span>
		</td>
	</tr>
</table>