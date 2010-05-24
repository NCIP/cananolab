<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>
<script type="text/javascript" src="javascript/calendar2.js"></script>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="WUSTL Study Efficacy of nanoparticle Characterization" />
	<jsp:param name="topic" value="submit_study_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<table width="100%" align="center" class="submissionView">
	<tr>
		<td class="cellLabel">
			Sample Name
		</td>
		<td>
			<div id="assayTypePrompt">
				<select name="achar.assayType" multiple="true" onchange="javascript:callPrompt('Assay Type', 'assayType', 'assayTypePrompt');" id="assayType">
					<option	value="size" selected="selected">
						MIT_MGH-AWangCMC2008-01
					</option>
					<option	value="size">
						MIT_MGH-AWangCMC2008-02
					</option>
					<option	value="size" selected="selected">
						MIT_MGH-AWangCMC2008-03
					</option>
					<option	value="size">
						MIT_MGH-AWangCMC2008-04
					</option>
				</select>
			</div>
		</td>
		<td class="cellLabel">
			Characterization Name
		</td>
		<td>
			<input type="text" name="achar.dateString" size="12" value="size" id="charName">
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Assay Type
		</td>
		<td>
			<div id="assayTypePrompt">
				<select name="achar.assayType" onchange="javascript:callPrompt('Assay Type', 'assayType', 'assayTypePrompt');" id="assayType"><option value=""></option>
						<option
							value="size" selected="selected">
							size
						</option>
					<option value="other">
						[other]
					</option></select>
			</div>
		</td>
		<td class="cellLabel">

			Protocol Name - Version
		</td>
		<input type="hidden" name="achar.protocolBean.fileBean.domainFile.uri" value="" id="updatedUri">
		<td colspan="3">
				    No protocols available.
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Characterization Source
		</td>

		<td>
			<select name="achar.pocBean.domain.id" id="charSource"><option value=""></option>
				<option value="11796482" selected="selected">WUSTL</option></select>
		</td>
		<td class="cellLabel">
			Characterization Date
		</td>
		<td>
			<input type="text" name="achar.dateString" size="10" value="" id="charDate">
			<a href="javascript:cal1.popup();"><img
					src="images/calendar-icon.gif" width="22" height="18" border="0"
					alt="Click Here to Pick up the date"
					title="Click Here to Pick up the date" align="top"> </a>
		</td>
	</tr>
</table>
<br>
<script language="JavaScript">
	<!-- //
		var cal1 = new calendar2(document.getElementById('charDate'));
	    cal1.year_scroll = true;
		cal1.time_comp = false;
		cal1.context = '/caNanoLab';
  	//-->
</script>
	<div id="characterizationDetail">
	</div>
	<a name="designAndMethod"> 
<table width="100%" align="center" class="submissionView">
	<tr>
		<td class="cellLabel">
			Design and Methods Description
		</td>
		<td>
			<textarea name="achar.description" cols="120" rows="3"></textarea>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="20%">
			Technique and Instrument
		</td>
		<td>
			<a style="display:block" id="addExperimentConfig"
				href="javascript:clearExperimentConfig();openSubmissionForm('ExperimentConfig');"><img
					align="top" src="images/btn_add.gif" border="0" /> </a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
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
			<td>

				dynamic light scattering(DLS)
			</td>
			<td>
				
			</td>
			<td>
				
				Zetasizer 4
				
			</td>
			
				<td align="right">
					<a
						href="javascript:setTheExperimentConfig(22);">Edit</a>&nbsp;

				</td>
			
		</tr>
	
</table>

			
		</td>
	</tr>
	<tr>
		<td colspan="2">
			
			
			<div id="newExperimentConfig" style="display:none">

				



<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<table class="subSubmissionView" width="85%" align="center">
	<tr>
		<th colspan="2">
			Technique and Instrument Info
		</th>
	</tr>
	<tr>
		<td class="cellLabel">

			Technique*
		</td>
		<td>
			<div id="techniqueTypePrompt">
				<select name="achar.theExperimentConfig.domain.technique.type" onchange="javascript:callPrompt('Technique Type', 'techniqueType', 'techniqueTypePrompt');retrieveTechniqueAbbreviation();" id="techniqueType"><option value=""></option>
					<option value="asymmetrical flow field-flow fractionation with multi-angle laser light scattering">asymmetrical flow field-flow fractionation with multi-angle laser light scattering</option>
<option value="atomic force microscopy">atomic force microscopy</option>
<option value="capillary electrophoresis">capillary electrophoresis</option>
<option value="cell counting">cell counting</option>

<option value="coagulation detection">coagulation detection</option>
<option value="colony counting">colony counting</option>
<option value="confocal laser scanning microscopy">confocal laser scanning microscopy</option>
<option value="dynamic light scattering">dynamic light scattering</option>
<option value="electron microprobe analysis">electron microprobe analysis</option>
<option value="electrophoretic light scattering">electrophoretic light scattering</option>
<option value="energy dispersive spectroscopy">energy dispersive spectroscopy</option>
<option value="environmental transmission electron microscopy">environmental transmission electron microscopy</option>
<option value="fast protein liquid chromatography">fast protein liquid chromatography</option>

<option value="flow cytometry">flow cytometry</option>
<option value="focused ion beam - scanning electron microscopy">focused ion beam - scanning electron microscopy</option>
<option value="gas sorption">gas sorption</option>
<option value="gel electrophoresis">gel electrophoresis</option>
<option value="gel filtration chromatography">gel filtration chromatography</option>
<option value="high performance liquid chromatography">high performance liquid chromatography</option>
<option value="high resolution scanning electron microscopy">high resolution scanning electron microscopy</option>
<option value="high resolution transmission electron microscopy">high resolution transmission electron microscopy</option>
<option value="imaging">imaging</option>

<option value="mass quantitation">mass quantitation</option>
<option value="matrix assisted laser desorption ionisation - time of flight">matrix assisted laser desorption ionisation - time of flight</option>
<option value="particle quantitation">particle quantitation</option>
<option value="polymerase chain reaction">polymerase chain reaction</option>
<option value="powder diffraction">powder diffraction</option>
<option value="radioactivity quantiation">radioactivity quantiation</option>
<option value="refractometry">refractometry</option>
<option value="scanning auger spectrometry">scanning auger spectrometry</option>
<option value="scanning electron microscopy">scanning electron microscopy</option>

<option value="scanning probe microscopy">scanning probe microscopy</option>
<option value="scanning tunneling  microscopy">scanning tunneling  microscopy</option>
<option value="size exclusion chromatography with multi-angle laser light scattering">size exclusion chromatography with multi-angle laser light scattering</option>
<option value="spectrophotometry">spectrophotometry</option>
<option value="transmission electron microscopy">transmission electron microscopy</option>
<option value="X-ray photoelectron spectroscopy">X-ray photoelectron spectroscopy</option>
<option value="zeta potential analysis">zeta potential analysis</option>

					<option value="other">

						[other]
					</option></select>
			</div>
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Abbreviation
		</td>
		<td>

			<input type="text" name="achar.theExperimentConfig.domain.technique.abbreviation" size="30" value="" id="techniqueAbbr">
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Instrument
		</td>
		<td>
			<div id="instrumentSection" style="position: relative;">

				<a style="display:block" id="addInstrument"
					href="javascript:clearInstrument();openSubmissionForm('Instrument');">Add</a>
				<br>
				<table id="instrumentTable" class="summaryViewLayer4" width="85%"
					style="display: none;">
					<tbody id="instrumentRows">
						<tr id="patternHeader">
							<td width="25%" class="cellLabel">
								Manufacturer
							</td>
							<td width="25%" class="cellLabel">

								Model Name
							</td>
							<td class="cellLabel">
								Type
							</td>
							<td>
							</td>
						</tr>
						<tr id="pattern" style="display: none;">
							<td>

								<span id="instrumentManufacturer">Manufacturer</span>
							</td>
							<td>
								<span id="instrumentModelName">ModelName</span>
							</td>
							<td>
								<span id="instrumentType">Type</span>

							</td>
							<td>
								<input class="noBorderButton" id="edit" type="button"
									value="Edit"
									onclick="editInstrument(this.id);openSubmissionForm('Instrument');" />
							</td>
						</tr>
					</tbody>
				</table>
				<table id="newInstrument" style="display: none;" class="promptbox">
						<tbody>

							<tr>
								<td class="cellLabel">
									Manufacturer
								</td>
								<td>
									<div id="manufacturerPrompt">
										<input type="hidden" name="achar.theInstrument.id" value="" id="id">
										<select name="achar.theInstrument.manufacturer" onchange="javascript:callPrompt('Manufacturer', 'manufacturer', 'manufacturerPrompt');" id="manufacturer"><option value=""></option>
											<option value="Agilent">Agilent</option>

<option value="Amersham">Amersham</option>
<option value="Beckman/Coulter">Beckman/Coulter</option>
<option value="Becton Dickinson">Becton Dickinson</option>
<option value="BioLogics">BioLogics</option>
<option value="Biorad">Biorad</option>
<option value="BioTek">BioTek</option>
<option value="Brookhaven Instruments">Brookhaven Instruments</option>
<option value="Carl Zeiss">Carl Zeiss</option>
<option value="Diagnostica Stago">Diagnostica Stago</option>

<option value="EDAX">EDAX</option>
<option value="Hamamatsu">Hamamatsu</option>
<option value="Hitachi">Hitachi</option>
<option value="JEOL">JEOL</option>
<option value="Kodak">Kodak</option>
<option value="Malvern">Malvern</option>
<option value="Micromeritics">Micromeritics</option>
<option value="Molecular Devices">Molecular Devices</option>
<option value="Molecular Imaging">Molecular Imaging</option>

<option value="Philips">Philips</option>
<option value="Quantachrome Instruments">Quantachrome Instruments</option>
<option value="Shimadzu">Shimadzu</option>
<option value="Tecan">Tecan</option>
<option value="Thermo Electron">Thermo Electron</option>
<option value="Waters">Waters</option>
<option value="Wyatt Technologies">Wyatt Technologies</option>

											<option value="other">

												[other]
											</option></select>
									</div>
								</td>
							</tr>
							<tr>
								<td class="cellLabel">
									Model Name
								</td>
								<td>

									<input type="text" name="achar.theInstrument.modelName" size="30" value="" id="modelName">
								</td>
							</tr>
							<tr>
								<td class="cellLabel">
									Type
								</td>
								<td>
									<div id="instrumentTypePrompt">

										<select name="achar.theInstrument.type" onchange="javascript:callPrompt('Instrment Type', 'type', 'instrumentTypePrompt');" id="type"><option value=""></option>
											<option value="other">
												[other]
											</option></select>
									</div>
								</td>
							</tr>
							<tr>
								<td>

									<input style="display: none;" class="promptButton" id="deleteInstrument"
										type="button" value="Remove" onclick="deleteTheInstrument()" />
								</td>
								<td>
									<div align="right">
										<input class="promptButton" type="button" value="Add"
											onclick="addInstrument();" />
										<input class="promptButton" type="button" value="Cancel"
											onclick="clearInstrument();" />
									</div>
								</td>
							</tr>

						</tbody>
					</table>
				</div>
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Description
		</td>

		<td>
			<textarea name="achar.theExperimentConfig.domain.description" cols="70" rows="5" id="configDescription"></textarea>
		</td>
	</tr>
	<tr>
		<td>
			
				<input style="display: none;" id="deleteExperimentConfig"
					type="button" value="Remove"
					onclick="javascript:deleteTheExperimentConfig()">
			
		</td>

		<td align="right">
			<div align="right">
				<input type="button" value="Save"
					onclick="javascript:validateSaveConfig('characterization');">
				<input type="reset" value="Cancel"
					onclick="javascript:clearExperimentConfig();closeSubmissionForm('ExperimentConfig')">
			</div>
		</td>
	</tr>
	<input type="hidden" name="achar.theExperimentConfig.domain.id" value="" id="configId">
</table>

			</div>
		</td>
	</tr>
</table>
<br></a>
	<a name="result"> 
<table width="100%" align="center" class="submissionView">
	<tr>
		<td class="cellLabel" width="20%">
			Result	 
		</td>
		<td>
			<a style="display:block" id="addFinding"
				href="javascript:resetTheFinding(characterizationForm);openSubmissionForm('Finding');"><img
					align="top" src="images/btn_add.gif" border="0" /> </a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
	<table class="summaryViewLayer4" align="center" width="95%">
			<tr>

				<th style="text-align: right">
					<a
						href="javascript:setTheFinding(characterizationForm, 'characterization', 8224778);">Edit</a>&nbsp;
				</th>
			</tr>
		<tr>
			<td>
				<b> Data and Conditions</b>

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
				<br />

			</td>
		</tr>
		<tr>
			<td>
				<b> Files</b>
			</td>
		</tr>
		<tr>

			<td>
				
					
					
						N/A
					
				
			</td>
		</tr>
	</table>
	<br />

<br>
			
		</td>
	</tr>

	<tr>
		<td colspan="2">
			
			
			<div id="newFinding" style="display:none">
				<a name="submitFinding">




<table class="subSubmissionView" width="85%" align="center">
	<tr>
		<th colspan="2">

			Finding Info
		</th>
	</tr>
	<tr>
		<td colspan="2">
			<span class="cellLabel">Data and Conditions</span> &nbsp;&nbsp;
			<input type="text" name="achar.theFinding.numberOfColumns" size="1" value="0" onkeydown="return filterInteger(event)" id="colNum">
			columns
			<input type="text" name="achar.theFinding.numberOfRows" size="1" value="0" onkeydown="return filterInteger(event)" id="rowNum">

			rows &nbsp;&nbsp;
			<a href="javascript:updateMatrix(characterizationForm)">Update</a>
		</td>
	</tr>
	<tr>
		<td valign="top" colspan="2">
			<div id="newMatrix">
				





<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<table class="promptbox" width="85%" align="center" id="matrix">
	<tr>
		
		<td></td>
	</tr>
	
</table>

<input type="hidden" name="numberOfRows" value="0" id="matrixRowNum">
<input type="hidden" name="numberOfColumns" value="0" id="matrixColNum">

			</div>

			<br/>
			<br/>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="15%">
			File 
		</td>
		<td>

			
			
			<a style="display:block" id="addFile"
				href="javascript:clearFile();openSubmissionForm('File');">Add</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<table class="submissionView" width="85%" align="center">
				
			</table>
		</td>

	</tr>
	<tr>
		<td colspan="2">
			
			
			<div style="display:none" id="newFile">
				
				
				
				
				










	
	
	


<table class="promptbox" width="85%"
	align="center">
	
		
		
			
			
		
	
	<tr>
		<td class="cellLabel">
			<input type="radio" name="achar.theFinding.theFile.domainFile.uriExternal" value="false" checked="checked" onclick="displayFileRadioButton();" id="external0">
			Upload
		</td>

		<td class="cellLabel">
			<input type="radio" name="achar.theFinding.theFile.domainFile.uriExternal" value="true" onclick="displayFileRadioButton();" id="external1">
			Enter File URL
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<span id="load" style="display: block"> <input type="file" name="achar.theFinding.theFile.uploadedFile" size="60" value="" id="uploadedFile"> &nbsp;&nbsp;</span>

			
			
			<span id="uploadedUri" style="display:none"></span>
			<span id="link" style="display: none"><input type="text" name="achar.theFinding.theFile.externalUrl" size="60" value="" id="externalUrl"> </span>&nbsp;
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			File Type*
		</td>
		<td>

			<div id="fileTypePrompt">
				<select name="achar.theFinding.theFile.domainFile.type" onchange="javascript:callPrompt('File Type', 'fileType', 'fileTypePrompt');" id="fileType"><option value="" />
						<option value="document">document</option>
<option value="graph">graph</option>
<option value="image">image</option>
<option value="spread sheet">spread sheet</option>

					<option value="other">
						[other]
					</option></select>

			</div>
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			File Title*
		</td>
		<td>
			<input type="text" name="achar.theFinding.theFile.domainFile.title" size="60" value="" id="fileTitle">

		</td>
	</tr>
	<tr>
		<td class="cellLabel" valign="top">
			Keywords
		</td>
		<td>
			<textarea name="achar.theFinding.theFile.keywordsStr" cols="60" rows="3" id="fileKeywords"></textarea>
			<br>

			<em>(one word per line)</em>
		</td>
	</tr>
	<tr>
			<td class="cellLabel" valign="top">
				Description
			</td>
			<td>
				<textarea name="achar.theFinding.theFile.domainFile.description" cols="60" rows="3" id="fileDescription"></textarea>

			</td>
		</tr>
		<tr>
			<td class="cellLabel" valign="top">
				<strong>Visibility</strong>
			</td>
			<td>
				<select name="achar.theFinding.theFile.visibilityGroups" multiple="multiple" size="6" id="fileVisibility"><option value="Public">Public</option>

<option value="BROWN_STANFORD">BROWN_STANFORD</option>
<option value="CAS_VT_VCU">CAS_VT_VCU</option>
<option value="CLM_UHA_CDS_INSERM">CLM_UHA_CDS_INSERM</option>
<option value="CP_UCLA_CalTech">CP_UCLA_CalTech</option>
<option value="CTRAIN">CTRAIN</option>
<option value="CWRU">CWRU</option>
<option value="GATECH">GATECH</option>
<option value="GATECH_EMORY">GATECH_EMORY</option>
<option value="GATECH_UCSF">GATECH_UCSF</option>

<option value="JSTA_FHU_NEC_MU_AIST_JAPAN">JSTA_FHU_NEC_MU_AIST_JAPAN</option>
<option value="JST_AIST_FHU_TU_NEC_MU_JAPAN">JST_AIST_FHU_TU_NEC_MU_JAPAN</option>
<option value="KU_JSTC_JAPAN">KU_JSTC_JAPAN</option>
<option value="MIT_MGH">MIT_MGH</option>
<option value="MIT_UCBBIC_HST_CU">MIT_UCBBIC_HST_CU</option>
<option value="MIT_UC_BBIC_HST_CU">MIT_UC_BBIC_HST_CU</option>
<option value="MSKCC_CU_UA">MSKCC_CU_UA</option>
<option value="NCL">NCL</option>
<option value="NCL_DataCurator">NCL_DataCurator</option>

<option value="NCL_Researcher">NCL_Researcher</option>
<option value="NEU">NEU</option>
<option value="NEU_MGH_UP_FCCC">NEU_MGH_UP_FCCC</option>
<option value="NEU_MIT_MGH">NEU_MIT_MGH</option>
<option value="NIOSH">NIOSH</option>
<option value="NWU">NWU</option>
<option value="PURDUE">PURDUE</option>
<option value="SNU_CHINA">SNU_CHINA</option>
<option value="STANFORD">STANFORD</option>

<option value="TTU">TTU</option>
<option value="UAM-CSIC-IMDEA">UAM-CSIC-IMDEA</option>
<option value="UAM_CSIC_IMDEA">UAM_CSIC_IMDEA</option>
<option value="UCSD_MIT_MGH">UCSD_MIT_MGH</option>
<option value="UC_HU_UEN_GERMANY">UC_HU_UEN_GERMANY</option>
<option value="UI">UI</option>
<option value="UM">UM</option>
<option value="UNC">UNC</option>
<option value="VCU_VT_EHC">VCU_VT_EHC</option>

<option value="WSU">WSU</option>
<option value="WUSTL">WUSTL</option>
<option value="WU_SU_CHINA">WU_SU_CHINA</option></select>
				<br>
				<i>(WUSTL_Researcher and
					WUSTL_DataCurator are defaults if none of above is
					selected)</i>
			</td>
		</tr>
		
			<input type="hidden" name="achar.theFinding.theFile.domainFile.id" value="" id="hiddenFileId">

			<input type="hidden" name="achar.theFinding.theFile.domainFile.uri" value="" id="hiddenFileUri">
			<input type="hidden" name="achar.theFinding.theFileIndex" value="-1" id="hiddenFileIndex">
		
		
			<tr>
				<td>
					
						<input class="promptButton" type="button" value="Remove"
							onclick="removeFile('characterization', characterizationForm)"
							id="deleteFile" style="display: none;" />
					
				</td>
				<td>
					<div align="right">
						<input class="promptButton" type="button" value="Add"
							onclick="addFile('characterization', characterizationForm);" />

						<input class="promptButton" type="button" value="Cancel"
							onclick="clearFile('achar.theFinding');closeSubmissionForm('File');" />
					</div>
				</td>
			</tr>
		
</table>

				&nbsp;
			</div>
		</td>

	</tr>
	<tr>
		<td>
			
		</td>
		<td>
			<div align="right">
				
				<input type="button" value="Save"
					onclick="javascript:saveFinding('characterization');">
				<input type="button" value="Cancel"	onclick="javascript:closeSubmissionForm('Finding');">
			</div>

		</td>
	</tr>
</table>

</a>
			</div>
		</td>
	</tr>
</table>
<br>
</a>
	





<table width="100%" align="center" class="submissionView">
	<tr>
		<td class="cellLabel">
			Analysis and Conclusion
		</td>
		<td>
			<textarea name="achar.conclusion" cols="120" rows="3"></textarea>
			&nbsp;
		</td>

	</tr>
</table>
<br>
	



<table width="100%" align="center" class="submissionView">
	<tbody>
		
			
				<tr>
					<td width="20%">
						<strong>Copy to other samples with the same primary point
							of contact</strong>
					</td>

					<td>
						<select name="otherSamples" multiple="multiple" size="10"><option value="WUSTL-AMorawskiMRM2004-01">WUSTL-AMorawskiMRM2004-01</option>
<option value="WUSTL-AMorawskiMRM2004-02">WUSTL-AMorawskiMRM2004-02</option>
<option value="WUSTL-AMorawskiMRM2004-03">WUSTL-AMorawskiMRM2004-03</option>
<option value="WUSTL-AMorawskiMRM2004-04">WUSTL-AMorawskiMRM2004-04</option>
<option value="WUSTL-ANystromCC2008-01">WUSTL-ANystromCC2008-01</option>
<option value="WUSTL-ANystromCC2008-02">WUSTL-ANystromCC2008-02</option>
<option value="WUSTL-ANystromCC2008-03">WUSTL-ANystromCC2008-03</option>

<option value="WUSTL-ANystromCC2008-04">WUSTL-ANystromCC2008-04</option>
<option value="WUSTL-ANystromCC2008-05">WUSTL-ANystromCC2008-05</option>
<option value="WUSTL-ANystromCC2008-06">WUSTL-ANystromCC2008-06</option>
<option value="WUSTL-ASchmiederMRM2005-01">WUSTL-ASchmiederMRM2005-01</option>
<option value="WUSTL-ASchmiederMRM2005-02">WUSTL-ASchmiederMRM2005-02</option>
<option value="WUSTL-ASchmiederMRM2005-03">WUSTL-ASchmiederMRM2005-03</option>
<option value="WUSTL-ASchmiederMRM2005-04">WUSTL-ASchmiederMRM2005-04</option>
<option value="WUSTL-GHuIJC2007-01">WUSTL-GHuIJC2007-01</option>
<option value="WUSTL-GHuIJC2007-02">WUSTL-GHuIJC2007-02</option>

<option value="WUSTL-GHuIJC2007-03">WUSTL-GHuIJC2007-03</option>
<option value="WUSTL-GHuIJC2007-04">WUSTL-GHuIJC2007-04</option>
<option value="WUSTL-GHuIJC2007-05">WUSTL-GHuIJC2007-05</option>
<option value="WUSTL-GHuIJC2007-06">WUSTL-GHuIJC2007-06</option>
<option value="WUSTL-GHuIJC2007-07">WUSTL-GHuIJC2007-07</option>
<option value="WUSTL-GSunBM2008-01">WUSTL-GSunBM2008-01</option>
<option value="WUSTL-GSunBM2008-02">WUSTL-GSunBM2008-02</option>
<option value="WUSTL-GSunBM2008-03">WUSTL-GSunBM2008-03</option>
<option value="WUSTL-GSunBM2008-04">WUSTL-GSunBM2008-04</option>

<option value="WUSTL-GSunBM2008-05">WUSTL-GSunBM2008-05</option>
<option value="WUSTL-GSunBM2008-06">WUSTL-GSunBM2008-06</option>
<option value="WUSTL-GSunBM2008-07">WUSTL-GSunBM2008-07</option>
<option value="WUSTL-GSunBM2008-08">WUSTL-GSunBM2008-08</option>
<option value="WUSTL-GSunBM2008-09">WUSTL-GSunBM2008-09</option>
<option value="WUSTL-GSunBM2008-10">WUSTL-GSunBM2008-10</option>
<option value="WUSTL-GSunBM2008-11">WUSTL-GSunBM2008-11</option>
<option value="WUSTL-GSunBM2008-12">WUSTL-GSunBM2008-12</option>
<option value="WUSTL-GSunBM2008-13">WUSTL-GSunBM2008-13</option>

<option value="WUSTL-GSunBM2008-14">WUSTL-GSunBM2008-14</option>
<option value="WUSTL-JMarshIEEEUS1999-02">WUSTL-JMarshIEEEUS1999-02</option>
<option value="WUSTL-JMarshIEEEUS1999-03">WUSTL-JMarshIEEEUS1999-03</option>
<option value="WUSTL-JMarshIEEEUS1999-04">WUSTL-JMarshIEEEUS1999-04</option>
<option value="WUSTL-JMarshIEEEUS1999-05">WUSTL-JMarshIEEEUS1999-05</option>
<option value="WUSTL-JMarshIEEEUS1999-06">WUSTL-JMarshIEEEUS1999-06</option>
<option value="WUSTL-JMarshIEEEUS1999-07">WUSTL-JMarshIEEEUS1999-07</option>
<option value="WUSTL-JMarshIEEEUS2000-01">WUSTL-JMarshIEEEUS2000-01</option>
<option value="WUSTL-JMarshIEEEUS2000-02">WUSTL-JMarshIEEEUS2000-02</option>

<option value="WUSTL-JMarshUMB2007-01">WUSTL-JMarshUMB2007-01</option>
<option value="WUSTL-JMarshUMB2007-02">WUSTL-JMarshUMB2007-02</option>
<option value="WUSTL-JMarshUMB2007-03">WUSTL-JMarshUMB2007-03</option>
<option value="WUSTL-JMarshUMB2007-04">WUSTL-JMarshUMB2007-04</option>
<option value="WUSTL-KPartlowBM2008-01">WUSTL-KPartlowBM2008-01</option>
<option value="WUSTL-KPartlowBM2008-02">WUSTL-KPartlowBM2008-02</option>
<option value="WUSTL-KPartlowBM2008-03">WUSTL-KPartlowBM2008-03</option>
<option value="WUSTL-KPartlowBM2008-04">WUSTL-KPartlowBM2008-04</option>
<option value="WUSTL-KPartlowBM2008-05">WUSTL-KPartlowBM2008-05</option>

<option value="WUSTL-KPartlowBM2008-06">WUSTL-KPartlowBM2008-06</option>
<option value="WUSTL-KPartlowBM2008-07">WUSTL-KPartlowBM2008-07</option>
<option value="WUSTL-KPartlowBM2008-08">WUSTL-KPartlowBM2008-08</option>
<option value="WUSTL-KZhangBM2009-01">WUSTL-KZhangBM2009-01</option>
<option value="WUSTL-KZhangBM2009-02">WUSTL-KZhangBM2009-02</option>
<option value="WUSTL-KZhangBM2009-03">WUSTL-KZhangBM2009-03</option>
<option value="WUSTL-KZhangBM2009-04">WUSTL-KZhangBM2009-04</option>
<option value="WUSTL-KZhangBM2009-05">WUSTL-KZhangBM2009-05</option>
<option value="WUSTL-KZhangBM2009-06">WUSTL-KZhangBM2009-06</option>

<option value="WUSTL-KZhangBM2009-07">WUSTL-KZhangBM2009-07</option>
<option value="WUSTL-KZhangBM2009-08">WUSTL-KZhangBM2009-08</option>
<option value="WUSTL-KZhangBM2009-09">WUSTL-KZhangBM2009-09</option>
<option value="WUSTL-KZhangBM2009-10">WUSTL-KZhangBM2009-10</option>
<option value="WUSTL-KZhangBM2009-11">WUSTL-KZhangBM2009-11</option>
<option value="WUSTL-KZhangBM2009-12">WUSTL-KZhangBM2009-12</option>
<option value="WUSTL-KZhangBM2009-13">WUSTL-KZhangBM2009-13</option>
<option value="WUSTL-KZhangBM2009-14">WUSTL-KZhangBM2009-14</option>
<option value="WUSTL-KZhangBM2009-15">WUSTL-KZhangBM2009-15</option>

<option value="WUSTL-KZhangBM2009-16">WUSTL-KZhangBM2009-16</option>
<option value="WUSTL-KZhangBM2009-17">WUSTL-KZhangBM2009-17</option>
<option value="WUSTL-KZhangBM2009-18">WUSTL-KZhangBM2009-18</option>
<option value="WUSTL-KZhangBM2009-19">WUSTL-KZhangBM2009-19</option>
<option value="WUSTL-KZhangBM2009-20">WUSTL-KZhangBM2009-20</option>
<option value="WUSTL-KZhangBM2009-21">WUSTL-KZhangBM2009-21</option>
<option value="WUSTL-KZhangBM2009-22">WUSTL-KZhangBM2009-22</option>
<option value="WUSTL-KZhangBM2009-23">WUSTL-KZhangBM2009-23</option>
<option value="WUSTL-KZhangBM2009-24">WUSTL-KZhangBM2009-24</option>

<option value="WUSTL-KZhangBM2009-25">WUSTL-KZhangBM2009-25</option>
<option value="WUSTL-KZhangBM2009-26">WUSTL-KZhangBM2009-26</option>
<option value="WUSTL-NSomanNL2008-01">WUSTL-NSomanNL2008-01</option>
<option value="WUSTL-NSomanNL2008-02">WUSTL-NSomanNL2008-02</option>
<option value="WUSTL-NSomanNL2008-03">WUSTL-NSomanNL2008-03</option>
<option value="WUSTL-NSomanNL2008-04">WUSTL-NSomanNL2008-04</option>
<option value="WUSTL-NSomanNL2008-05">WUSTL-NSomanNL2008-05</option>
<option value="WUSTL-NSomanNL2008-06">WUSTL-NSomanNL2008-06</option>
<option value="WUSTL-NSomanNL2008-07">WUSTL-NSomanNL2008-07</option>

<option value="WUSTL-NSomanNL2008-08">WUSTL-NSomanNL2008-08</option>
<option value="WUSTL-PWInterATVB2006-01">WUSTL-PWInterATVB2006-01</option>
<option value="WUSTL-PWinterATVB2006-02">WUSTL-PWinterATVB2006-02</option>
<option value="WUSTL-PWinterATVB2006-03">WUSTL-PWinterATVB2006-03</option>
<option value="WUSTL-PWinterATVB2006-04">WUSTL-PWinterATVB2006-04</option>
<option value="WUSTL-PWinterATVB2006-05">WUSTL-PWinterATVB2006-05</option>
<option value="WUSTL-PWinterATVB2006-06">WUSTL-PWinterATVB2006-06</option>
<option value="WUSTL-PWinterATVB2006-07">WUSTL-PWinterATVB2006-07</option>
<option value="WUSTL-PWinterATVB2006-08">WUSTL-PWinterATVB2006-08</option>

<option value="WUSTL-PWinterFJ2008-01">WUSTL-PWinterFJ2008-01</option>
<option value="WUSTL-PWinterFJ2008-02">WUSTL-PWinterFJ2008-02</option>
<option value="WUSTL-PWinterFJ2008-03">WUSTL-PWinterFJ2008-03</option>
<option value="WUSTL-PWinterFJ2008-04">WUSTL-PWinterFJ2008-04</option>
<option value="WUSTL-SCaruthers-IR2006-01">WUSTL-SCaruthers-IR2006-01</option>
<option value="WUSTL-SCaruthers-IR2006-02">WUSTL-SCaruthers-IR2006-02</option>
<option value="WUSTL-SCaruthers-IR2006-03">WUSTL-SCaruthers-IR2006-03</option></select>
					</td>
					<td>

						
							<input type="checkbox" name="copyData" value="on">
							<strong>Also copy finding data and conditions ?</strong>
						
						&nbsp;
					</td>
				</tr>
			
			
		
	</tbody>
</table>

	
	
	
	
	





<table width="100%" border="0" align="center" cellpadding="3"
	cellspacing="0">

	<tr>
		<td width="30%">
			
				
					<table height="32" border="0" align="left" cellpadding="4"
						cellspacing="0">
						<tr>
							<td height="32">
								<div align="left">
									<input type="button" value="Delete"
										onclick="deleteData('characterization', characterizationForm, 'characterization')">
								</div>
							</td>

						</tr>
					</table>
				
			
			<table width="498" height="32" border="0" align="right"
				cellpadding="4" cellspacing="0">
				<tr>
					<td width="490" height="32">
						<div align="right">
							<div align="right">
								<input type="reset" value="Reset">
								<input type="hidden" name="dispatch" value="create">

								<input type="hidden" name="page" value="1">
								<input type="hidden" name="sampleId" value="3735562">
								<input type="hidden" name="location" value="WUSTL">
								<input type="submit" value="Submit">
							</div>
						</div>
					</td>
				</tr>
			</table>

		</td>
	</tr>
</table>
