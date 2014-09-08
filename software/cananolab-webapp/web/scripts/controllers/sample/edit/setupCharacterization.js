'use strict';
var app = angular.module('angularApp')
	.controller('SetupCharacterizationCtrl', function ($scope,$http,$modal,sampleService,$location,$anchorScroll,$filter) {

    // define variables //
    $scope.sampleData = sampleService.sampleData;
    $scope.sampleId = sampleService.sampleId.data;
    $scope.domainFileUri = "";
    $scope.data = {};  
    $scope.PE = {};
    // can remove this after done testing local data
    $scope.dataCopy = angular.copy($scope.data);
    $scope.type = sampleService.type.data;
    $scope.isEdit = sampleService.isEdit.data;
    $scope.charId = sampleService.charId.data;
    $scope.charClassName = sampleService.charClassName.data;
    $scope.techniqueInstrument = {};
    $scope.loader = true;
    $scope.loaderMessage = "Loading";

    // testdata for edit //  
    $scope.data = {"type":"physico-chemical characterization","name":"molecular weight","parentSampleId":20917507,"charId":21867777,"assayType":"molecular weight","protocolId":29949956,"characterizationSourceId":52985856,"characterizationDate":"2014-09-06T16:01:05.583Z","charNamesForCurrentType":["molecular weight","other_pc","physical state","purity","relaxivity","shape","size","solubility","surface","zeta potential","other"],"designMethodsDescription":"Some Design Method Description","techniqueInstruments":{"experiments":[{"id":25570560,"displayName":"size exclusion chromatography with multi-angle laser light scattering(SEC-MALLS)","techniqueType":"size exclusion chromatography with multi-angle laser light scattering","abbreviation":"SEC-MALLS","description":"Some Description","instruments":[{"manufacturer":"Aerotech","modelName":"Some Model","type":"control module"}]},{"id":73990144,"displayName":"asymmetrical flow field-flow fractionation with multi-angle laser light scattering(AFFF-MALLS)","techniqueType":"asymmetrical flow field-flow fractionation with multi-angle laser light scattering","abbreviation":"AFFF-MALLS","description":"Another Description","instruments":[{"manufacturer":"Affymetrix","modelName":"Another Model","type":"photometer"}]}],"techniqueTypeLookup":["acoustic microscopy","asymmetrical flow field-flow fractionation with multi-angle laser light scattering","atomic absorption spectroscopy","atomic force microscopy","biochemical quantitation","capillary electrophoresis","cell counting","centrifugal filtration","coagulation detection","colony counting","confocal laser scanning microscopy","coulter principle","dark field microscopy","deconvolution fluorescence microscopy","differential centrifugal sedimentation","dynamic light scattering","electron microprobe analysis","electron spin resonance","electron spin resonance spectroscopy","electrophoretic light scattering","elemental analysis","energy dispersive spectroscopy","environmental transmission electron microscopy","fast protein liquid chromatography","flow cytometry","fluorescence microscopy","fluorometry","focused ion beam - scanning electron microscopy","fourier transform infrared spectrophotometry","gas chromatography","gas sorption","gel electrophoresis","gel filtration chromatography","gel permeation chromatography","high performance liquid chromatography","high performance liquid chromatography - evaporative light scattering detection","high resolution scanning electron microscopy","high resolution transmission electron microscopy","illumination","imaging","inductively coupled plasma atomic emission spectroscopy","inductively coupled plasma mass spectrometry","infrared imaging","laser diffraction","liight microscopy","liquid chromatography - mass spectrometry","liquid scintillation counting","magnetic property measurement","mass quantitation","matrix assisted laser desorption ionisation - time of flight","microfluidics","multi photon confocal laser scanning microscopy","multiphoton laser scanning microscopy","NNNN","nuclear magnetic resonance","particle quantitation","photoacoustic imaging","photoacoustic spectrometry","polymerase chain reaction","positron emission tomography","powder diffraction","protein quantitation","radioactivity quantiation","Raman spectroscopy","refractometry","scanning auger spectrometry","scanning electron microscopy","scanning probe microscopy","scanning transmission electron microscopy","scanning tunneling microscopy","sfaer927034wqw34","size exclusion chromatography with multi-angle laser light scattering","spectrofluorometry","spectrophotometry","surface plasmon resonance","temperature measurement","thermogravimetric analysis","time-resolved fluorescence microscopy ","transmission electron microscopy","wavelength dispersive spectroscopy","weight","X-ray diffraction","X-ray photoelectron spectroscopy","zeta potential analysis","other"],"manufacturerLookup":["ACT GmbH","Aerotech","Affymetrix","Agilent","Alltech","Amersham","Amersham Pharmacia Biotech","Applied Biosystems","Applied Precision","Asylum Research","B&W Tek","BD Biosciences","Beckman/Coulter","Becton Dickinson","Biacore","BioLogics","Biorad","BioTek","Brookhaven Instruments","Bruker","Budget Sensors","Caliper Life Sciences","Carl Zeiss","ChemoMetec","CPS Instruments","CTI Concorde Microsystems","Dako","Diagnostica Stago","Dynatech","EDAX","Endra","Eppendorf","FEI","FLIR","Gatan","GE Healthcare","Guava Technologies/Millipore","Hamamatsu","Hewlett-Packard","Hitachi","Horiba","Invitrogen","JEOL","Jobin Yvon","Kodak","Kratos Analytical","Labsystems","Lakeshore","LaVision BioTec","LECO","Leica","Luxtron","Malvern","Micromass","Micromeritics","Millipore","Molecular Devices","Molecular Imaging","Nikon","OBB Corp","Ocean Optics","Olympus","Packard","Panametrics","Park Systems","PerkinElmer","Phenomenex","Philips","Photal Otsuka","Photometrics","Photon Technology International","Picoquant","Point Electronic GmBh","Princeton Instruments","PSS","Quantachrome Instruments","Quantum Design","Renishaw","Rigaku","Roche Applied Science","RPMC Lasers","Sartorius","Shimadzu","Siemens Medical","Soft Imaging Systems","TA Instruments","Tecan","Test","Thermo Electron","Thermo Scientific","TosoHaas","Varian","Visualsonics","Waters","Wyatt Technologies","Zeiss","other"]},"finding":[{"findingId":21900545,"numberOfColumns":2,"numberOfRows":1,"rows":[{"cells":[{"value":"24.17","datumOrCondition":"datum","columnOrder":null,"createdDate":null},{"value":"1.003","datumOrCondition":"datum","columnOrder":null,"createdDate":null}]}],"columnHeaders":[{"columnName":"molecular weight","conditionProperty":null,"valueType":"observed","valueUnit":"kDa","columnType":"datum","displayName":"molecular weight<br>(observed,kDa)","constantValue":"","columnOrder":1,"createdDate":1255549581000},{"columnName":"PDI","conditionProperty":null,"valueType":"observed","valueUnit":"","columnType":"datum","displayName":"PDI<br>(observed)","constantValue":"","columnOrder":2,"createdDate":1255549582000}],"files":[],"theFileIndex":0,"errors":[]},{"findingId":21900546,"numberOfColumns":3,"numberOfRows":3,"rows":[{"cells":[{"value":"24.62","datumOrCondition":"datum","columnOrder":null,"createdDate":null},{"value":"1.032","datumOrCondition":"datum","columnOrder":null,"createdDate":null},{"value":"1.9","datumOrCondition":"condition","columnOrder":null,"createdDate":null}]},{"cells":[{"value":"1.2","datumOrCondition":"datum","columnOrder":null,"createdDate":null},{"value":"1.2","datumOrCondition":"datum","columnOrder":null,"createdDate":null},{"value":"1.8","datumOrCondition":"condition","columnOrder":null,"createdDate":null}]},{"cells":[{"value":"1.1","datumOrCondition":"datum","columnOrder":null,"createdDate":null},{"value":"1.1","datumOrCondition":"datum","columnOrder":null,"createdDate":null},{"value":"1.4","datumOrCondition":"condition","columnOrder":null,"createdDate":null}]}],"columnHeaders":[{"columnName":"molecular weight","conditionProperty":null,"valueType":"observed","valueUnit":"kDa","columnType":"datum","displayName":"molecular weight<br>(observed,kDa)","constantValue":"","columnOrder":1,"createdDate":1255549598000},{"columnName":"molecular weight","conditionProperty":null,"valueType":"observed","valueUnit":"","columnType":"datum","displayName":"molecular weight<br>(observed)","constantValue":"","columnOrder":2,"createdDate":1255549599000},{"columnName":"ASODN concentration","conditionProperty":"","valueType":"","valueUnit":"","columnType":"condition","displayName":"ASODN concentration","constantValue":"","columnOrder":3,"createdDate":1410017995000}],"files":[],"theFileIndex":0,"errors":[]}],"analysisConclusion":"The molecular weight determined by SEC-MALLS for NCL23 is very similar to that of NCL22. Since NCL23 is NCL22 with associated Magnevist, the results suggest that Magnevist is no longer associated with the dendrimer after fractionation.","selectedOtherSampleNames":null,"copyToOtherSamples":false,"charTypesLookup":["physico-chemical characterization","in vitro characterization","in vivo characterization","SY-CharType","SY-New-Char-Type","ex vivo","other"],"protocolLookup":[{"domainId":29949960,"domainFileId":29655055,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-13.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-13 (PCC-13), version 1.1"},{"domainId":29949959,"domainFileId":29655054,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-12.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-12 (PCC-12), version 1.1"},{"domainId":29949958,"domainFileId":29655053,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-14.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-14 (PCC-14), version 1.0"},{"domainId":29949957,"domainFileId":29655052,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-11.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-11 (PCC-11), version 1.1"},{"domainId":29949956,"domainFileId":29655050,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-9.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-9 (PCC-9), version 1.1"},{"domainId":29949955,"domainFileId":29655049,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-8.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-8 (PCC-8), version 1.1"},{"domainId":29949954,"domainFileId":29655048,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-10.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-10 (PCC-10), version 1.1"},{"domainId":29949953,"domainFileId":29655047,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-7.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-7 (PCC-7), version 1.1"},{"domainId":29949952,"domainFileId":29655046,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-6.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-6 (PCC-6), version 1.1"},{"domainId":25210112,"domainFileId":25177344,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-1.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-1 (PCC-1), version 1.1"}],"charSourceLookup":[{"id":52985856,"displayName":"C-Sixty (CNI) (John Doe)"},{"id":56328198,"displayName":"DC"},{"id":15695880,"displayName":"CP_UCLA_CalTech"},{"id":0,"displayName":"other"}],"otherSampleNameLookup":["NCL-23-1-SY-CharTest","NCL-23-1-SY-Test-CharDate","SY-NCL-23-1"],"assayTypesByCharNameLookup":["gel permeation chromatography","molecular weight","other"],"errors":[],"messages":null};
    // $scope.data = {"type":"physico-chemical characterization","name":"molecular weight","parentSampleId":20917507,"charId":21867777,"assayType":"molecular weight","protocolId":29949956,"characterizationSourceId":52985856,"characterizationDate":"2014-09-06T13:34:09.849Z","charNamesForCurrentType":["molecular weight","other_pc","physical state","purity","relaxivity","shape","size","solubility","surface","zeta potential","other"],"designMethodsDescription":"Some Design Method Description","techniqueInstruments":{"experiments":[{"id":25570560,"displayName":"size exclusion chromatography with multi-angle laser light scattering(SEC-MALLS)","techniqueType":"size exclusion chromatography with multi-angle laser light scattering","abbreviation":"SEC-MALLS","description":"Some Description","instruments":[{"manufacturer":"Aerotech","modelName":"Some Model","type":"control module"}]},{"id":73990144,"displayName":"asymmetrical flow field-flow fractionation with multi-angle laser light scattering(AFFF-MALLS)","techniqueType":"asymmetrical flow field-flow fractionation with multi-angle laser light scattering","abbreviation":"AFFF-MALLS","description":"Another Description","instruments":[{"manufacturer":"Affymetrix","modelName":"Another Model","type":"photometer"}]}],"techniqueTypeLookup":["acoustic microscopy","asymmetrical flow field-flow fractionation with multi-angle laser light scattering","atomic absorption spectroscopy","atomic force microscopy","biochemical quantitation","capillary electrophoresis","cell counting","centrifugal filtration","coagulation detection","colony counting","confocal laser scanning microscopy","coulter principle","dark field microscopy","deconvolution fluorescence microscopy","differential centrifugal sedimentation","dynamic light scattering","electron microprobe analysis","electron spin resonance","electron spin resonance spectroscopy","electrophoretic light scattering","elemental analysis","energy dispersive spectroscopy","environmental transmission electron microscopy","fast protein liquid chromatography","flow cytometry","fluorescence microscopy","fluorometry","focused ion beam - scanning electron microscopy","fourier transform infrared spectrophotometry","gas chromatography","gas sorption","gel electrophoresis","gel filtration chromatography","gel permeation chromatography","high performance liquid chromatography","high performance liquid chromatography - evaporative light scattering detection","high resolution scanning electron microscopy","high resolution transmission electron microscopy","illumination","imaging","inductively coupled plasma atomic emission spectroscopy","inductively coupled plasma mass spectrometry","infrared imaging","laser diffraction","liight microscopy","liquid chromatography - mass spectrometry","liquid scintillation counting","magnetic property measurement","mass quantitation","matrix assisted laser desorption ionisation - time of flight","microfluidics","multi photon confocal laser scanning microscopy","multiphoton laser scanning microscopy","NNNN","nuclear magnetic resonance","particle quantitation","photoacoustic imaging","photoacoustic spectrometry","polymerase chain reaction","positron emission tomography","powder diffraction","protein quantitation","radioactivity quantiation","Raman spectroscopy","refractometry","scanning auger spectrometry","scanning electron microscopy","scanning probe microscopy","scanning transmission electron microscopy","scanning tunneling microscopy","sfaer927034wqw34","size exclusion chromatography with multi-angle laser light scattering","spectrofluorometry","spectrophotometry","surface plasmon resonance","temperature measurement","thermogravimetric analysis","time-resolved fluorescence microscopy ","transmission electron microscopy","wavelength dispersive spectroscopy","weight","X-ray diffraction","X-ray photoelectron spectroscopy","zeta potential analysis","other"],"manufacturerLookup":["ACT GmbH","Aerotech","Affymetrix","Agilent","Alltech","Amersham","Amersham Pharmacia Biotech","Applied Biosystems","Applied Precision","Asylum Research","B&W Tek","BD Biosciences","Beckman/Coulter","Becton Dickinson","Biacore","BioLogics","Biorad","BioTek","Brookhaven Instruments","Bruker","Budget Sensors","Caliper Life Sciences","Carl Zeiss","ChemoMetec","CPS Instruments","CTI Concorde Microsystems","Dako","Diagnostica Stago","Dynatech","EDAX","Endra","Eppendorf","FEI","FLIR","Gatan","GE Healthcare","Guava Technologies/Millipore","Hamamatsu","Hewlett-Packard","Hitachi","Horiba","Invitrogen","JEOL","Jobin Yvon","Kodak","Kratos Analytical","Labsystems","Lakeshore","LaVision BioTec","LECO","Leica","Luxtron","Malvern","Micromass","Micromeritics","Millipore","Molecular Devices","Molecular Imaging","Nikon","OBB Corp","Ocean Optics","Olympus","Packard","Panametrics","Park Systems","PerkinElmer","Phenomenex","Philips","Photal Otsuka","Photometrics","Photon Technology International","Picoquant","Point Electronic GmBh","Princeton Instruments","PSS","Quantachrome Instruments","Quantum Design","Renishaw","Rigaku","Roche Applied Science","RPMC Lasers","Sartorius","Shimadzu","Siemens Medical","Soft Imaging Systems","TA Instruments","Tecan","Test","Thermo Electron","Thermo Scientific","TosoHaas","Varian","Visualsonics","Waters","Wyatt Technologies","Zeiss","other"]},"finding":[{"findingId":21900545,"numberOfColumns":2,"numberOfRows":1,"rows":[{"cells":[{"value":"24.17","datumOrCondition":"datum","columnOrder":null,"createdDate":null},{"value":"1.003","datumOrCondition":"datum","columnOrder":null,"createdDate":null}]}],"columnHeaders":[{"columnName":"molecular weight","conditionProperty":null,"valueType":"observed","valueUnit":"kDa","columnType":"datum","displayName":"molecular weight<br>(observed,kDa)","constantValue":"","columnOrder":1,"createdDate":1255549581000},{"columnName":"PDI","conditionProperty":null,"valueType":"observed","valueUnit":"","columnType":"datum","displayName":"PDI<br>(observed)","constantValue":"","columnOrder":2,"createdDate":1255549582000}],"files":[],"theFileIndex":0,"errors":[]},{"findingId":21900546,"numberOfColumns":2,"numberOfRows":1,"rows":[{"cells":[{"value":"24.62","datumOrCondition":"datum","columnOrder":null,"createdDate":null},{"value":"1.032","datumOrCondition":"datum","columnOrder":null,"createdDate":null}]}],"columnHeaders":[{"columnName":"molecular weight","conditionProperty":null,"valueType":"observed","valueUnit":"kDa","columnType":"datum","displayName":"molecular weight<br>(observed,kDa)","constantValue":"","columnOrder":1,"createdDate":1255549598000},{"columnName":"PDI","conditionProperty":null,"valueType":"observed","valueUnit":"","columnType":"datum","displayName":"PDI<br>(observed)","constantValue":"","columnOrder":2,"createdDate":1255549599000}],"files":[],"theFileIndex":0,"errors":[]}],"selectedOtherSampleNames":["SY-NCL-23-1"],"copyToOtherSamples":false,"charTypesLookup":["physico-chemical characterization","in vitro characterization","in vivo characterization","SY-CharType","SY-New-Char-Type","ex vivo","other"],"protocolLookup":[{"domainId":29949960,"domainFileId":29655055,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-13.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-13 (PCC-13), version 1.1"},{"domainId":29949959,"domainFileId":29655054,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-12.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-12 (PCC-12), version 1.1"},{"domainId":29949958,"domainFileId":29655053,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-14.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-14 (PCC-14), version 1.0"},{"domainId":29949957,"domainFileId":29655052,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-11.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-11 (PCC-11), version 1.1"},{"domainId":29949956,"domainFileId":29655050,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-9.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-9 (PCC-9), version 1.1"},{"domainId":29949955,"domainFileId":29655049,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-8.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-8 (PCC-8), version 1.1"},{"domainId":29949954,"domainFileId":29655048,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-10.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-10 (PCC-10), version 1.1"},{"domainId":29949953,"domainFileId":29655047,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-7.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-7 (PCC-7), version 1.1"},{"domainId":29949952,"domainFileId":29655046,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-6.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-6 (PCC-6), version 1.1"},{"domainId":25210112,"domainFileId":25177344,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-1.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-1 (PCC-1), version 1.1"}],"charSourceLookup":[{"id":52985856,"displayName":"C-Sixty (CNI) (John Doe)"},{"id":56328198,"displayName":"DC"},{"id":15695880,"displayName":"CP_UCLA_CalTech"},{"id":0,"displayName":"other"}],"otherSampleNameLookup":["NCL-23-1-SY-CharTest","NCL-23-1-SY-Test-CharDate","SY-NCL-23-1"],"assayTypesByCharNameLookup":["gel permeation chromatography","molecular weight","other"],"errors":[],"messages":null};
    // $scope.data.finding = [];
    // testdata for add //
    // $scope.data = {"type":"physico-chemical characterization", "name":null, "parentSampleId":69500928, "charId":69599238, "assayType":"molecular weight", "protocolNameVersion":null, "protocolId":0, "characterizationSource":null, "characterizationDate":null, "charNamesForCurrentType":["molecular weight", "other_pc", "physical state", "purity", "relaxivity", "shape", "size", "solubility", "surface", "zeta potential", "[other]"], "designMethodsDescription":null, "techniqueInstruments":{"experiments":[], "techniqueTypeLookup":["acoustic microscopy", "asymmetrical flow field-flow fractionation with multi-angle laser light scattering", "atomic absorption spectroscopy", "atomic force microscopy", "biochemical quantitation", "capillary electrophoresis", "cell counting", "centrifugal filtration", "coagulation detection", "colony counting", "confocal laser scanning microscopy", "coulter principle", "dark field microscopy", "deconvolution fluorescence microscopy", "differential centrifugal sedimentation", "dynamic light scattering", "electron microprobe analysis", "electron spin resonance", "electron spin resonance spectroscopy", "electrophoretic light scattering", "elemental analysis", "energy dispersive spectroscopy", "environmental transmission electron microscopy", "fast protein liquid chromatography", "flow cytometry", "fluorescence microscopy", "fluorometry", "focused ion beam - scanning electron microscopy", "fourier transform infrared spectrophotometry", "gas chromatography", "gas sorption", "gel electrophoresis", "gel filtration chromatography", "gel permeation chromatography", "high performance liquid chromatography", "high performance liquid chromatography - evaporative light scattering detection", "high resolution scanning electron microscopy", "high resolution transmission electron microscopy", "illumination", "imaging", "inductively coupled plasma atomic emission spectroscopy", "inductively coupled plasma mass spectrometry", "infrared imaging", "laser diffraction", "liight microscopy", "liquid chromatography - mass spectrometry", "liquid scintillation counting", "magnetic property measurement", "mass quantitation", "matrix assisted laser desorption ionisation - time of flight", "microfluidics", "multi photon confocal laser scanning microscopy", "multiphoton laser scanning microscopy", "NNNN", "nuclear magnetic resonance", "particle quantitation", "photoacoustic imaging", "photoacoustic spectrometry", "polymerase chain reaction", "positron emission tomography", "powder diffraction", "protein quantitation", "radioactivity quantiation", "Raman spectroscopy", "refractometry", "scanning auger spectrometry", "scanning electron microscopy", "scanning probe microscopy", "scanning transmission electron microscopy", "scanning tunneling  microscopy", "sfaer927034wqw34", "size exclusion chromatography with multi-angle laser light scattering", "spectrofluorometry", "spectrophotometry", "surface plasmon resonance", "temperature measurement", "thermogravimetric analysis", "time-resolved fluorescence microscopy ", "transmission electron microscopy", "wavelength dispersive spectroscopy", "weight", "X-ray diffraction", "X-ray photoelectron spectroscopy", "zeta potential analysis", "[other]"], "manufacturerLookup":["ACT GmbH", "Aerotech", "Affymetrix", "Agilent", "Alltech", "Amersham", "Amersham Pharmacia Biotech", "Applied Biosystems", "Applied Precision", "Asylum Research", "B&W Tek", "BD Biosciences", "Beckman/Coulter", "Becton Dickinson", "Biacore", "BioLogics", "Biorad", "BioTek", "Brookhaven Instruments", "Bruker", "Budget Sensors", "Caliper Life Sciences", "Carl Zeiss", "ChemoMetec", "CPS Instruments", "CTI Concorde Microsystems", "Dako", "Diagnostica Stago", "Dynatech", "EDAX", "Endra", "Eppendorf", "FEI", "FLIR", "Gatan", "GE Healthcare", "Guava Technologies/Millipore", "Hamamatsu", "Hewlett-Packard", "Hitachi", "Horiba", "Invitrogen", "JEOL", "Jobin Yvon", "Kodak", "Kratos Analytical", "Labsystems", "Lakeshore", "LaVision BioTec", "LECO", "Leica", "Luxtron", "Malvern", "Micromass", "Micromeritics", "Millipore", "Molecular Devices", "Molecular Imaging", "Nikon", "OBB Corp", "Ocean Optics", "Olympus", "Packard", "Panametrics", "Park Systems", "PerkinElmer", "Phenomenex", "Philips", "Photal Otsuka", "Photometrics", "Photon Technology International", "Picoquant", "Point Electronic GmBh", "Princeton Instruments", "PSS", "Quantachrome Instruments", "Quantum Design", "Renishaw", "Rigaku", "Roche Applied Science", "RPMC Lasers", "Sartorius", "Shimadzu", "Siemens Medical", "Soft Imaging Systems", "TA Instruments", "Tecan", "Test", "Thermo Electron", "Thermo Scientific", "TosoHaas", "Varian", "Visualsonics", "Waters", "Wyatt Technologies", "Zeiss", "[other]"] }, "selectedOtherSampleNames":null, "copyToOtherSamples":false, "charTypesLookup":["physico-chemical characterization", "in vitro characterization", "in vivo characterization", "SY-New-Char-Type", "ex vivo", "[other]"], "protocolLookup":[{"domainId":69435392, "domainFileId":69304322, "domainFileUri":"http://www.sciencedirect.com/science/article/pii/S1045105608000572", "displayName":"Physicochemical and biological assays for quality control of biopharmaceuticals: Interferon alfa-2 case study, version v1.0"}, {"domainId":63340544, "domainFileId":63275009, "domainFileUri":"protocols/20140825_10-55-03-531_20110228_05-08-58-717_Apoptosis_Procedure.pdf", "displayName":"Test File Protocol3"}, {"domainId":31817728, "domainFileId":31686657, "domainFileUri":"protocols/20111018_12-08-22-229_protocols_20090113_11-11-33-967_NCL_Method_NIST-NCL_PCC-1.pdf", "displayName":"Demo-PCC, version 1.0"}, {"domainId":29949960, "domainFileId":29655055, "domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-13.pdf", "displayName":"NIST - NCL Joint Assay Protocol, PCC-13 (PCC-13), version 1.1"}, {"domainId":29949959, "domainFileId":29655054, "domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-12.pdf", "displayName":"NIST - NCL Joint Assay Protocol, PCC-12 (PCC-12), version 1.1"}, {"domainId":29949958, "domainFileId":29655053, "domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-14.pdf", "displayName":"NIST - NCL Joint Assay Protocol, PCC-14 (PCC-14), version 1.0"}, {"domainId":29949957, "domainFileId":29655052, "domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-11.pdf", "displayName":"NIST - NCL Joint Assay Protocol, PCC-11 (PCC-11), version 1.1"}, {"domainId":29949956, "domainFileId":29655050, "domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-9.pdf", "displayName":"NIST - NCL Joint Assay Protocol, PCC-9 (PCC-9), version 1.1"}, {"domainId":29949955, "domainFileId":29655049, "domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-8.pdf", "displayName":"NIST - NCL Joint Assay Protocol, PCC-8 (PCC-8), version 1.1"}, {"domainId":29949954, "domainFileId":29655048, "domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-10.pdf", "displayName":"NIST - NCL Joint Assay Protocol, PCC-10 (PCC-10), version 1.1"}, {"domainId":29949953, "domainFileId":29655047, "domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-7.pdf", "displayName":"NIST - NCL Joint Assay Protocol, PCC-7 (PCC-7), version 1.1"}, {"domainId":29949952, "domainFileId":29655046, "domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-6.pdf", "displayName":"NIST - NCL Joint Assay Protocol, PCC-6 (PCC-6), version 1.1"}, {"domainId":25210112, "domainFileId":25177344, "domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-1.pdf", "displayName":"NIST - NCL Joint Assay Protocol, PCC-1 (PCC-1), version 1.1"} ], "charSourceLookup":[{"id":52985856, "displayName":"C-Sixty (CNI) (John Doe)"}, {"id":56328198, "displayName":"DC"}, {"id":15695880, "displayName":"CP_UCLA_CalTech"}, {"id":0, "displayName":"[other]"} ], "otherSampleNameLookup":["Demo-1234", "NCL-23-1", "NCL-23-112312321778", "SY-NCL-23-1", "Test2_Harika", "dasdas343434"], "assayTypesByCharNameLookup":{}, "errors":[], "messages":null };


    // scrolls to section on page with provided id //
    $scope.scroll = function(id) {
        var old = $location.hash();
        $location.hash(id);
        $anchorScroll();
        $location.hash(old);
    };

    // popup calendar functions //
    $scope.setupCalendar = function() {
        $scope.today = function() {
            $scope.data.characterizationDate = new Date();
        };
        $scope.clear = function () {
            $scope.data.characterizationDate = null;
        };
        $scope.toggleMin = function() {
            $scope.minDate = $scope.minDate ? null : new Date();
        };
        $scope.toggleMin();
        $scope.open = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.opened = true;
        };
        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };
        $scope.initDate = new Date('2016-15-20');
        $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
        $scope.format = 'shortDate';
    }


    $scope.setupCalendar();

    // initial rest call to get master data object //
    if ($scope.isEdit) {
        // run initial rest call to setup characterization edit form //
        $scope.title = "Edit";
        $http({method: 'GET', url: '/caNanoLab/rest/characterization/setupUpdate?sampleId='+$scope.sampleId+'&charId='+$scope.charId+'&charClassName='+$scope.charClassName+'&charType='+$scope.type}).
            success(function(data, status, headers, config) {
            $scope.data = data;
            if (!$scope.data.characterizationDate) {
                $scope.today();
            };
            $scope.loader = false;
            $scope.dataCopy = angular.copy($scope.data);   
        }).
            error(function(data, status, headers, config) {
            $scope.loader = false;        
        });     
    }
    else {
        // run initial rest call to setup characterization add form //
        $scope.title = "Add";
        $http({method: 'GET', url: '/caNanoLab/rest/characterization/setupAdd?sampleId='+$scope.sampleId+'&charType='+$scope.type}).
            success(function(data, status, headers, config) {
            $scope.data = data;
            $scope.today();
            $scope.loader = false;
            $scope.dataCopy = angular.copy($scope.data);                            
        }).
            error(function(data, status, headers, config) {
            $scope.loader = false;        
        });         
    };
    if($scope.data.characterizationDate) { 
        $scope.data.characterizationDate = new Date($scope.data.characterizationDate);
    };    

    // gets characterization names when characterization type dropdown is changed //
    $scope.characterizationTypeDropdownChanged = function() {
        $scope.data.assayTypesByCharNameLookup = [];
        delete $scope.data.assayType;
        delete $scope.data.name;
        delete $scope.data.protocolId;
        delete $scope.data.characterizationSourceId;
        delete $scope.domainFileUri;
        if ($scope.data.type=='other') {
            alert("Other");
        }
        else {
            $scope.loader = true;
            $http({method: 'GET', url: '/caNanoLab/rest/characterization/getCharNamesByCharType?charType='+$scope.data.type}).
                success(function(data, status, headers, config) {
                $scope.data.charNamesForCurrentType = data;
                $scope.loader = false;
            }).
                error(function(data, status, headers, config) {
                $scope.loader = false;        
            });  
        };      
    };

    // gets assay types when characterization name dropdown is changed //
    $scope.characterizationNameDropdownChanged = function() {
        delete $scope.data.assayType;

        if ($scope.data.type=='other') {
            alert("Other");
        }
        else {
            $scope.loader = true;
            $http({method: 'GET', url: '/caNanoLab/rest/characterization/getAssayTypesByCharName?charName='+ $scope.data.name}).
                success(function(data, status, headers, config) {
                $scope.data.assayTypesByCharNameLookup = data;
                $scope.loader = false;
            }).
                error(function(data, status, headers, config) {
                $scope.loader = false;        
            });  
        };      
    };

    // looks to see if technique type has abbreviation // 
    $scope.techniqueTypeInstrumentDropdownChanged = function() {
        $http({method: 'GET', url: '/caNanoLab/rest/characterization/getInstrumentTypesByTechniqueType?techniqueType='+$scope.techniqueInstrument.techniqueType}).
            success(function(data, status, headers, config) {
            $scope.instrumentTypesLookup = data;
        }).
            error(function(data, status, headers, config) {
        }); 
    };

    // gets URL for protocol name //
    $scope.getDomainFileUri = function() {
        for (var x = 0; x < $scope.data.protocolLookup.length;x++) {
            if ($scope.data.protocolId==$scope.data.protocolLookup[x].domainId) {
                $scope.domainFileUri = $scope.data.protocolLookup[x].domainFileUri;
            };  
        };
    };

    // open new experiment section //
    $scope.openNewExperimentConfig = function() {
        $scope.updateExperimentConfig = 1;
        $scope.isNewExperimentConfig = 1;
        $scope.techniqueInstrument = {};
        $scope.techniqueInstrument.instruments = [];
        $scope.scroll('editTechniqueInstrumentInfo');
    };

    // open update experiment section //
    $scope.openUpdateExperimentConfig = function(item) {
        $scope.updateExperimentConfig = 1;
        $scope.isNewExperimentConfig = 0;
        $scope.techniqueInstrument = item;
        $http({method: 'GET', url: '/caNanoLab/rest/characterization/getInstrumentTypesByTechniqueType?techniqueType='+$scope.techniqueInstrument.techniqueType}).
            success(function(data, status, headers, config) {
            $scope.instrumentTypesLookup = data;
        }).
            error(function(data, status, headers, config) {
        });         
        // $scope.instrumentTypesLookup = ["control module","guard column","multi angle light scattering detector","photometer","refractometer","separation column","separations module","spectrophotometer","other"];
        $scope.techniqueInstrumentCopy = angular.copy(item);        
        $scope.scroll('editTechniqueInstrumentInfo');
    };  

    // save experiment config and close section //
    $scope.saveExperimentConfig = function() {
        $scope.loader = true;
        $http({method: 'POST', url: '/caNanoLab/rest/characterization/saveExperimentConfig',data: $scope.techniqueInstrument}).
        success(function(data, status, headers, config) {            
            $scope.loader = false;
        }).
        error(function(data, status, headers, config) {
            $scope.loader = false;
        });  
        $scope.updateExperimentConfig = 0; 
    };  

    // removes experiment config and close section //
    $scope.removeExperimentConfig = function() {
        $scope.loader = true;
        $http({method: 'POST', url: '/caNanoLab/rest/characterization/removeExperimentConfig',data: $scope.techniqueInstrument}).
        success(function(data, status, headers, config) {            
            $scope.loader = false;
        }).
        error(function(data, status, headers, config) {
            $scope.loader = false;
        });  
        $scope.updateExperimentConfig = 0;              
    };

    // cancel experiment config and close section //
    $scope.cancelExperimentConfig = function() {
        $scope.updateExperimentConfig = 0;
        angular.copy($scope.techniqueInstrumentCopy,$scope.techniqueInstrument);
        $scope.updateInstrument = 0;

    };        

    // open new instrument section //  
    $scope.openNewInstrument = function() {
        $scope.updateInstrument = 1;
        $scope.instrument = {"manufacturer":null,"modelName":null,"type":null};
        $scope.isNewInstrument = 1;
    };

    // open existing instrument section //  
    $scope.openExistingInstrument = function(instrument) {
        $scope.updateInstrument = 1;
        $scope.instrument = instrument;   
        $scope.isNewInstrument = 0;
        $scope.instrumentCopy = angular.copy(instrument);
        if ($scope.instrumentTypesLookup.indexOf($scope.instrument.type)==-1) {
            $scope.instrument.type="";
        };                
    };    

    // saves instrument. checks if it is new. if it is add it to techniqueInstrument //  
    $scope.saveInstrument = function(instrument) {
        if ($scope.isNewInstrument) {
            $scope.techniqueInstrument.instruments.push($scope.instrument);
        };
        $scope.updateInstrument = 0;

    };     

    // removes instrument from list //
    $scope.removeInstrument = function(instrument) {
        $scope.techniqueInstrument.instruments.splice($scope.techniqueInstrument.instruments.indexOf(instrument),1);
        $scope.updateInstrument = 0;        
    };       

    // closes instrument section, reset instrument if it exists //
    $scope.cancelInstrument = function(instrument) {
        $scope.updateInstrument = 0;
        angular.copy($scope.instrumentCopy, $scope.instrument);
    };

    // opens new finding dialog //
    $scope.addNewFinding = function() {
        var old = $location.hash();
        $scope.currentFinding = {};
        $scope.updateFinding = 1;
        $scope.finding = {};
        $scope.scroll('editFindingInfo');
        $scope.isNewFinding = 1; 
        $scope.currentFindingCopy = angular.copy($scope.currentFinding);

    };

    // open finding dialog with existing finding //
    $scope.updateExistingFinding = function(finding) {
        var old = $location.hash();
        $scope.updateFinding = 1;        
        $scope.currentFinding = finding;
        $scope.scroll('editFindingInfo');
        $scope.isNewFinding = 0;   
        $scope.currentFindingCopy = angular.copy(finding);
    };

    // updates rows and columns and runs rest call update //
    $scope.updateRowsColsForFinding = function() {
        $http({method: 'POST', url: '/caNanoLab/rest/characterization/updateDataConditionTable',data: $scope.currentFinding}).
        success(function(data, status, headers, config) {            
            $scope.loader = false;
            $scope.currentFinding=data;
        }).
        error(function(data, status, headers, config) {
            $scope.loader = false;
        });        
    };

    // opens column form to change properties for column //
    $scope.openColumnForm = function() {
        $scope.findingsColumn = cell;

    };

    // opens column form to change order for columns. Does not actually order columns //
    $scope.openColumnOrderForm = function() {
        $scope.columnOrder = 1;
    }; 

    // 
    $scope.updateColumnOrder = function() {
        $http({method: 'POST', url: '/caNanoLab/rest/characterization/setColumnOrder',data: $scope.currentFinding}).
        success(function(data, status, headers, config) {            
            $scope.loader = false;
            $scope.currentFinding=data;
            $scope.columnOrder = 0;
        }).
        error(function(data, status, headers, config) {
            $scope.loader = false;
            $scope.columnOrder = 0;
        });         
    }; 

    // saves finding info //
    $scope.saveFindingInfo = function() {
        alert("save");
        $scope.updateFinding = 0;        
    };

    // removes finding info //
    $scope.removeFindingInfo = function() {
        alert("remove");
        $scope.updateFinding = 0;        
    };    

    $scope.cancelFindingInfo = function() {
        $scope.updateFinding = 0; 
        angular.copy($scope.currentFindingCopy, $scope.currentFinding);
    };   

    // deletes data and condition row //
    $scope.deleteDataConditionRow = function(row) {
        $scope.currentFinding.rows.splice($scope.currentFinding.rows.indexOf(row),1);
    };

    // save record. //
    $scope.save = function() {
    };

    // reset form //
    $scope.reset = function() {
        $scope.data = angular.copy($scope.dataCopy);
        $scope.domainFileUri = "";
        $scope.updateExperimentConfig = 0;
    };    

 });