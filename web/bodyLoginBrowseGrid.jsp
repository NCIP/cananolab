<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table class="gridtable" width="100%">
	<tr>
		<th>
			Site
		</th>
		<th>
			Data Type
		</th>
		<th id="results">
			Public Results
		</th>
	</tr>
	<tr align="left">
		<td rowspan="3">
			<select name="searchLocations" id="sites" multiple="true" size="8"
				onchange="getPublicCounts();">
				<option value="${applicationOwner}" selected="selected">
					${applicationOwner}
				</option>
				<c:forEach var="location" items="${allGridNodes}">
					<option value="${location.hostName}">
						${location.hostName}
					</option>
				</c:forEach>
				<option value="all">
					ALL
				</option>
			</select>
		</td>
		<td>
			<table class="gridtableNoBorder">
				<tr>
					<td rowspan="2">
						<a href="#" onclick="gotoProtocols('setup')"> <img
								src="images/icon_protocol_48x.jpg" style="border-style: none;"
								alt="Search Protocals" /> </a>
					</td>
					<td>
						<a href="#" onclick="gotoProtocols('setup')"><b>Search
								Protocols</b> </a>
					</td>
				</tr>
				<tr>
					<td>
						Search for nanotechnology protocols leveraged in performing
						nanoparticle characterization assays.
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
						Search for information on nanoparticle formulations including the
						composition of the particle, results of nanoparticle
						physico-chemical, in vitro, and other characterizations, and
						associated publications. See also
						<a href="${advanceSearchUrl}" id="advanceSearch">Advanced
							Sample Search</a>.
					</td>
				</tr>
			</table>
		</td>
		<td style="padding-left: 22px">
			<img src="images/ajax-loader.gif" border="0" class="counts"
				id="sampleLoaderImg" style="display: block">
			<span id="sampleCount"></span>
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
						<a href="#" onclick=gotoPublications('setup');
><b>Search
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
</table>
