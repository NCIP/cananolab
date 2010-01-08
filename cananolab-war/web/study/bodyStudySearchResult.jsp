<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<link rel="stylesheet" type="text/css" href="css/displaytag.css" />
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Study Search Results" />
	<jsp:param name="topic" value="sample_search_results_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
	<jsp:param name="other" value="Back" />
	<jsp:param name="otherLink" value="searchSample.do?dispatch=setup" />
</jsp:include>
<table width="100%" align="center">
	<tr>
		<td colspan="2">
			<span class="pagebanner">16 items found.</span><span
				class="pagelinks"></span>
			<table class="displaytable" id="sample">
				<thead>
					<tr>
						<th class="sortable">
							<a
								href="searchSample.do?text=test&amp;d-446960-o=2&amp;d-446960-s=0&amp;dispatch=search&amp;page=1&amp;searchLocations=WUSTL&amp;samplePointOfContact=&amp;characterizationType=&amp;characterizationType=">
								Study Name</a>
						</th>
						<th class="sortable">
							<a
								href="searchSample.do?text=test&amp;d-446960-o=2&amp;d-446960-s=0&amp;dispatch=search&amp;page=1&amp;searchLocations=WUSTL&amp;samplePointOfContact=&amp;characterizationType=&amp;characterizationType=">
								Sample Name</a>
						</th>
						<th class="sortable">
							<a
								href="searchSample.do?text=test&amp;d-446960-o=2&amp;d-446960-s=1&amp;dispatch=search&amp;page=1&amp;searchLocations=WUSTL&amp;samplePointOfContact=&amp;characterizationType=&amp;characterizationType=">
								Primary<br>Point Of Contact</a>
						</th>
						<th class="sortable">
							<a
								href="searchSample.do?text=test&amp;d-446960-o=2&amp;d-446960-s=2&amp;dispatch=search&amp;page=1&amp;searchLocations=WUSTL&amp;samplePointOfContact=&amp;characterizationType=&amp;characterizationType=">
								Disease</a>
						</th>
						<th class="sortable">
							<a
								href="searchSample.do?text=test&amp;d-446960-o=2&amp;d-446960-s=5&amp;dispatch=search&amp;page=1&amp;searchLocations=WUSTL&amp;samplePointOfContact=&amp;characterizationType=&amp;characterizationType=">
								Site</a>
						</th>
					</tr>
				</thead>
				<tbody>
					<tr class="odd">
						<td>
							<a
								href="study.do?dispatch=studyEdit&amp;location=&amp;page=0amp;&tab=ALL&amp;sampleId=13631489&amp;location=WUSTL">Efficacy of nanoparticle 1</a>
						</td>
						<td>
							<a
								href="study.do?dispatch=studyEdit&amp;location=&amp;page=0amp;&tab=ALL&amp;sampleId=13631489&amp;location=WUSTL">CLM_UHA_CDS_INSERM-TDaouLGMR2009-02</a>
						</td>
						<td>
							CLM_UHA_CDS_INSERM (T Jean Daou)
						</td>
						<td>
							Cancer
						</td>
						<td>
							WUSTL
						</td>
					</tr>
					<tr class="even">
						<td>
							<a
								href="study.do?dispatch=studyEdit&amp;location=&amp;page=0amp;&tab=ALL&amp;sampleId=13631489&amp;location=WUSTL">Efficacy of nanoparticle 2</a>
						</td>
						<td>
							<a
								href="study.do?dispatch=studyEdit&amp;location=&amp;page=0amp;&tab=ALL&amp;sampleId=6979618&amp;location=WUSTL">CLM_UHA_CDS_INSERM-TDaouLGMR2009-03</a>
						</td>
						<td>
							CLM_UHA_CDS_INSERM (T Jean Daou)
						</td>
						<td>
							Cancer
						</td>
						<td>
							WUSTL
						</td>
					</tr>
					<tr class="odd">
						<td>
							<a
								href="study.do?dispatch=studyEdit&amp;location=&amp;page=0amp;&tab=ALL&amp;sampleId=13631489&amp;location=WUSTL">Efficacy of nanoparticle 3</a>
						</td>
						<td>
							<a
								href="study.do?dispatch=studyEdit&amp;location=&amp;page=0amp;&tab=ALL&amp;sampleId=9994265&amp;location=WUSTL">CLM_UHA_CDS_INSERM-TDaouLGMR2009-04</a>
						</td>
						<td>
							UC_HU_UEN_GERMANY
						</td>
						<td>
							Cancer
						</td>
						<td>
							WUSTL
						</td>
					</tr>
				</tbody>
			</table>
		</td>
	</tr>
</table>

