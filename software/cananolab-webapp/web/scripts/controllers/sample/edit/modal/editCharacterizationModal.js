'use strict';
var app = angular.module('angularApp')
	.controller('EditCharacterizationModalCtrl', function ($scope,$http,$modalInstance,sampleId, sampleData,message,type, isEdit, sampleService) {

    // define variables //
	$scope.sampleId = sampleId;
    $scope.sampleData = sampleData;
    $scope.domainFileUri = "";
    $scope.data = {};    
    $scope.data = {"type":"physico-chemical characterization","name":null,"parentSampleId":0,"charId":0,"assayType":null,"protocolNameVersion":null,"characterizationSource":null,"characterizationDate":null,"charNamesForCurrentType":["blood contact","cytotoxicity","enzyme induction","immune cell function","metabolic stability","other","other_vt","oxidative stress","sterility","targeting","transfection"],"designMethodsDescription":null,"techniqueInstruments":{"simpleExperiments":[],"techniquesLookup":["acoustic microscopy","asymmetrical flow field-flow fractionation with multi-angle laser light scattering","atomic absorption spectroscopy","atomic force microscopy","biochemical quantitation","capillary electrophoresis","cell counting","centrifugal filtration","coagulation detection","colony counting","confocal laser scanning microscopy","coulter principle","dark field microscopy","deconvolution fluorescence microscopy","differential centrifugal sedimentation","dynamic light scattering","electron microprobe analysis","electron spin resonance","electron spin resonance spectroscopy","electrophoretic light scattering","elemental analysis","energy dispersive spectroscopy","environmental transmission electron microscopy","fast protein liquid chromatography","flow cytometry","fluorescence microscopy","fluorometry","focused ion beam - scanning electron microscopy","fourier transform infrared spectrophotometry","gas chromatography","gas sorption","gel electrophoresis","gel filtration chromatography","gel permeation chromatography","high performance liquid chromatography","high performance liquid chromatography - evaporative light scattering detection","high resolution scanning electron microscopy","high resolution transmission electron microscopy","illumination","imaging","inductively coupled plasma atomic emission spectroscopy","inductively coupled plasma mass spectrometry","infrared imaging","laser diffraction","liight microscopy","liquid chromatography - mass spectrometry","liquid scintillation counting","magnetic property measurement","mass quantitation","matrix assisted laser desorption ionisation - time of flight","microfluidics","multi photon confocal laser scanning microscopy","multiphoton laser scanning microscopy","nuclear magnetic resonance","particle quantitation","photoacoustic imaging","photoacoustic spectrometry","polymerase chain reaction","positron emission tomography","powder diffraction","protein quantitation","radioactivity quantiation","Raman spectroscopy","refractometry","scanning auger spectrometry","scanning electron microscopy","scanning probe microscopy","scanning transmission electron microscopy","scanning tunneling microscopy","size exclusion chromatography with multi-angle laser light scattering","spectrofluorometry","spectrophotometry","surface plasmon resonance","temperature measurement","thermogravimetric analysis","time-resolved fluorescence microscopy ","transmission electron microscopy","wavelength dispersive spectroscopy","weight","X-ray diffraction","X-ray photoelectron spectroscopy","zeta potential analysis"],"manufacturerLookup":["ACT GmbH","Aerotech","Affymetrix","Agilent","Alltech","Amersham","Amersham Pharmacia Biotech","Applied Biosystems","Applied Precision","Asylum Research","B&W Tek","BD Biosciences","Beckman/Coulter","Becton Dickinson","Biacore","BioLogics","Biorad","BioTek","Brookhaven Instruments","Bruker","Budget Sensors","Caliper Life Sciences","Carl Zeiss","ChemoMetec","CPS Instruments","CTI Concorde Microsystems","Dako","Diagnostica Stago","Dynatech","EDAX","Endra","Eppendorf","FEI","FLIR","Gatan","GE Healthcare","Guava Technologies/Millipore","Hamamatsu","Hewlett-Packard","Hitachi","Horiba","Invitrogen","JEOL","Jobin Yvon","Kodak","Kratos Analytical","Labsystems","Lakeshore","LaVision BioTec","LECO","Leica","Luxtron","Malvern","Micromass","Micromeritics","Millipore","Molecular Devices","Molecular Imaging","Nikon","OBB Corp","Ocean Optics","Olympus","Packard","Panametrics","Park Systems","PerkinElmer","Phenomenex","Philips","Photal Otsuka","Photometrics","Photon Technology International","Picoquant","Point Electronic GmBh","Princeton Instruments","PSS","Quantachrome Instruments","Quantum Design","Renishaw","Rigaku","Roche Applied Science","RPMC Lasers","Sartorius","Shimadzu","Siemens Medical","Soft Imaging Systems","TA Instruments","Tecan","Test","Thermo Electron","Thermo Scientific","TosoHaas","Varian","Visualsonics","Waters","Wyatt Technologies","Zeiss"]},"charTypesLookup":["physico-chemical characterization","in vitro characterization","in vivo characterization","ex vivo","[other]"],"protocolLookup":[{"domainId":29949960,"domainFileId":29655055,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-13.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-13 (PCC-13), version 1.1"},{"domainId":29949959,"domainFileId":29655054,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-12.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-12 (PCC-12), version 1.1"},{"domainId":29949958,"domainFileId":29655053,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-14.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-14 (PCC-14), version 1.0"},{"domainId":29949957,"domainFileId":29655052,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-11.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-11 (PCC-11), version 1.1"},{"domainId":29949956,"domainFileId":29655050,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-9.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-9 (PCC-9), version 1.1"},{"domainId":29949955,"domainFileId":29655049,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-8.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-8 (PCC-8), version 1.1"},{"domainId":29949954,"domainFileId":29655048,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-10.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-10 (PCC-10), version 1.1"},{"domainId":29949953,"domainFileId":29655047,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-7.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-7 (PCC-7), version 1.1"},{"domainId":29949952,"domainFileId":29655046,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-6.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-6 (PCC-6), version 1.1"},{"domainId":25210112,"domainFileId":25177344,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-1.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-1 (PCC-1), version 1.1"}],"charSourceLookup":[{"id":52985856,"displayName":"C-Sixty (CNI) (John Doe)"},{"id":56328198,"displayName":"DC"},{"id":15695880,"displayName":"CP_UCLA_CalTech"}],"otherSampleNameLookup":["NCL-23-1-SY-CharTest","SY-NCL-23-1"],"assayTypesByCharNameLookup":{},"assayTypes":["coagulation","complement activation","hemolysis","plasma protein binding","platelet aggregation"]};
    $scope.message = message;
    $scope.loader = false;
    $scope.loaderMessage = "Loading";
    $scope.type = type;

    // popup calendar functions //
    $scope.setupCalendar = function() {
        $scope.today = function() {
            $scope.data.characterizationDate = new Date();
        };
        $scope.today();
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
    $scope.loader = true;

    // initial rest call to get master data object //
    if (isEdit) {
        // run initial rest call to setup characterization edit form //
        alert("I AM EDIT");

        $http({method: 'GET', url: '/caNanoLab/rest/characterization/setupEdit?sampleId='+$scope.sampleId+'&charType='+$scope.type}).
            success(function(data, status, headers, config) {
            $scope.data = data;
            $scope.today();
            $scope.data.charTypesLookup.push('[other]');
            $scope.data.charNamesForCurrentType.push('[other]');
            $scope.loader = false;
        }).
            error(function(data, status, headers, config) {
            $scope.loader = false;        
        });     
    }
    else {
        // run initial rest call to setup characterization add form //
        $http({method: 'GET', url: '/caNanoLab/rest/characterization/setupAdd?sampleId='+$scope.sampleId+'&charType='+$scope.type}).
            success(function(data, status, headers, config) {
            $scope.data = data;
            $scope.today();
            $scope.data.charTypesLookup.push('[other]');
            $scope.data.charNamesForCurrentType.push('[other]');
            $scope.loader = false;
        }).
            error(function(data, status, headers, config) {
            $scope.loader = false;        
        });         
    };

    // gets characterization names when characterization type dropdown is changed //
    $scope.characterizationTypeDropdownChanged = function() {
        $scope.data.assayTypes = [];
        delete $scope.data.assayType;
        delete $scope.data.name;
        delete $scope.data.protocolNameVersion;
        delete $scope.data.characterizationSource;
        if ($scope.data.type=='[other]') {
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

        if ($scope.data.type=='[other]') {
            alert("Other");
        }
        else {
            $scope.loader = true;
            $http({method: 'GET', url: '/caNanoLab/rest/characterization/getAssayTypesByCharName?charName='+ $scope.data.name}).
                success(function(data, status, headers, config) {
                $scope.data.assayTypes = data;
                $scope.loader = false;
            }).
                error(function(data, status, headers, config) {
                $scope.loader = false;        
            });  
        };      
    };    

    // gets URL for protocol name //
    $scope.getDomainFileUri = function() {
        for (var x = 0; x < $scope.data.protocolLookup.length;x++) {
            if ($scope.data.protocolNameVersion==$scope.data.protocolLookup[x].domainId) {
                $scope.domainFileUri = $scope.data.protocolLookup[x].domainFileUri;
            };  
        };

    };

    // save record. //
    $scope.save = function() {
    };

    // cancel save //
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

 });