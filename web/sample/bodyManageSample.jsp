<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table summary="" cellpadding="0" cellspacing="0" border="0"
	width="100%" height="100%">
	<tr>
		<td>
			<h4>
				Manage Sample/Aliquot Home
			</h4>
		</td>
		<td align="right" width="15%">
			<a
				href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=manage_sample_aliquot_help')"
				class="helpText">Help</a>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="welcomeContent">
			This is the manage sample/aliquot section. A sample is the
			nanoparticle received by the laboratory that undergoes further
			characterization.&nbsp; An aliquot is a portion of the total sample
			(nanoparticle) used during the execution of laboratory assays. In
			this section, you may either create a new sample, create a new
			aliquot, or select an existing samples and select existing aliquots.
			<br>
			<br>
			</span>
		</td>
	</tr>
	<tr>
		<td valign="top" width="40%">
			<!-- sidebar begins -->
			<table summary="" cellpadding="0" cellspacing="0" border="0"
				height="100%">
				<tr>
					<td colspan="3">
						<br>
					</td>
				</tr>
				<tr>
					<td colspan="3">
						<br>
					</td>
				</tr>
				<tr>
					<td colspan="3">
						<br>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<table summary="" cellpadding="0" cellspacing="0" border="0"
							width="100%" height="100%" class="sidebarSection">
							<tr>
								<td class="sidebarTitle" height="20">
									SAMPLE LINKS
								</td>
							</tr>

							<tr>
								<td class="sidebarContent">
									<a href="createSample.do?dispatch=setup&amp;page=0"> Create
										a New Sample </a>
									<br>
									Click to add a new sample.
								</td>
							</tr>
							<tr>
								<td class="sidebarContent">
									<a href="searchSample.do?dispatch=setup">Search Existing
										Samples </a>
									<br>
									Enter search criteria to find the sample you wish to operate
									on.
								</td>
							</tr>
						</table>
					</td>
					<td width="10">
						&nbsp;
					</td>
					<td>
						<table summary="" cellpadding="0" cellspacing="0" border="0"
							width="100%" height="100%" class="sidebarSection">
							<tr>
								<td class="sidebarTitle" height="20">
									ALIQUOT LINKS
								</td>
							</tr>

							<tr>
								<td class="sidebarContent">
									<a href="createAliquot.do?dispatch=setup&amp;page=0">
										Create a New Aliquot </a>
									<br>
									Click to add a new aliquot.
								</td>
							</tr>
							<tr>
								<td class="sidebarContent">
									<a href="searchAliquot.do?dispatch=setup">Search Existing
										Aliquots</a>
									<br>
									Enter search criteria to find the aliquot you wish to operate
									on.
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
		<td width="60%"></td>
	</tr>
</table>
<br>

