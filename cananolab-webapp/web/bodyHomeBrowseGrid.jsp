<table class="gridtable" width="98%" align="center">
	<tr>
		<th width="60%">
			Data Type
		</th>
		<th id="results">
			Public Results
		</th>
	</tr>
	<tr align="left">

		<td>
			<table class="gridtableNoBorder">
				<tr>
					<td rowspan="2">
						<a href="#" onclick="gotoProtocols('setup')"> <img
								src="images/icon_protocol_48x.jpg" style="border-style: none;"
								alt="Search Protocols" /> </a>
					</td>
					<td>
						<a href="#" onclick="gotoProtocols('setup')"><b>Search
								Protocols</b> </a>
					</td>
				</tr>
				<tr>
					<td>
						Search for nanotechnology protocols leveraged in performing
						nanomaterial characterization assays.
					</td>
				</tr>
			</table>
		</td>
		<td style="padding-left: 22px">
			<img src="images/ajax-loader.gif" border="0" class="counts"
				id="protocolLoaderImg" style="display: block">
			<span id="protocolCount"></span>
		</td>
	</tr>
	<tr class="alt">
		<td>
			<table class="gridtableNoBorder">
				<tr>
					<td rowspan="2">
						<a href="#" onclick="gotoSamples('setup')"> <img
								src="images/icon_nanoparticle_48x.jpg"
								style="border-style: none;" alt="Search Samples" /> </a>
					</td>
					<td>
						<a href="#" onclick="gotoSamples('setup')"><b>Search
								Samples</b> </a>
					</td>
				</tr>
				<tr>
					<c:url var="advanceSearchUrl" value="advancedSampleSearch.do">
						<c:param name="dispatch" value="setup" />
					</c:url>
					<td>
						Search for information on nanomaterials including the composition
						of the nanomaterial, results of physico-chemical,
						<i>in vitro</i>, and other characterizations, and associated
						publications.
						<br>
						See also
						<a href="${advanceSearchUrl}" id="advanceSearch">Advanced
							Sample Search</a>.
					</td>
				</tr>
			</table>
		</td>
		<td style="padding-left: 22px">
			<img src="images/ajax-loader.gif" border="0" class="counts"
				id="sampleLoaderImg" style="display: block">
			<div id="sampleCounts">
				<table class="invisibleTable">
					<tr>
						<td>
							<span id="sampleCount"></span>
						</td>
						<td width="30"></td>
						<td>
							<a id="moreStats" onclick="getMoreSamplesStats()"
								onmouseOver="showCursor();" onmouseOut="hideCursor()"><span
								class="moreLink">More Stats</span> </a>
						</td>
					</tr>
				</table>
				<br />
				<img src="images/ajax-loader.gif" border="0" class="counts"
					id="sampleRelatedLoaderImg" style="display: none">
				<div id="sampleRelatedCounts" style="display: none">
					<table class="invisibleTable" style="color: #5F7C7C">
						<tr>
							<th width="10">
								<img src="images/diamond_list_item.gif" />
							</th>
							<td>
								<span id="sampleSourceCount"></span>
							</td>
						</tr>
						<tr>
							<th width="10">
								<img src="images/diamond_list_item.gif" />
							</th>
							<td>
								<span id="CharacterizationCount"></span>
							</td>
						</tr>
						<tr>
							<th></th>
							<td>
								<table class="invisibleTable" style="color: #5F7C7C">
									<tr>
										<th width="10">
											-
										</th>
										<td>
											<span id="PhysicoChemicalCharacterizationCount"></span>
										</td>
									</tr>
									<tr>
										<th width="10">
											-
										</th>
										<td>
											<span id="InvitroCharacterizationCount"></span>
										</td>
									</tr>
									<tr>
										<th width="10">
											-
										</th>
										<td>
											<span id="InvivoCharacterizationCount"></span>
										</td>
									</tr>
									<tr>
										<th width="10">
											-
										</th>
										<td>
											<span id="OtherCharacterizationCount"></span>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<table class="gridtableNoBorder">
				<tr>
					<td rowspan="2">
						<a href="#" onclick="gotoPublications('setup')"> <img
								src="images/icon_report_48x.gif" style="border-style: none;"
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
		<td style="padding-left: 22px">
			<img src="images/ajax-loader.gif" border="0" class="counts"
				id="publicationLoaderImg" style="display: block">
			<span id="publicationCount"></span>
		</td>
	</tr>
	<tr>
		<td>
			<table class="gridtableNoBorder">
				<tr>
					<td rowspan="2">
						<a href="#" onclick="gotoStudies('setup')"> <img
								src="images/icon_report_48x.gif" style="border-style: none;"
								alt="Search Publications" /> </a>
					</td>
					<td>
						<a href="#" onclick=gotoStudies('setup');><b>Search
								Studies</b> </a>
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
		<td style="padding-left: 22px">
			<img src="images/ajax-loader.gif" border="0" class="counts"
				id="studyLoaderImg" style="display: block" >
			<span id="studyCount"></span>
		</td>
	</tr>
</table>