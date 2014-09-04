'use strict';
var app = angular.module('angularApp')
	.controller('SetupCharacterizationCtrl', function ($scope,$http, sampleService) {

    // define variables //
    $scope.sampleData = sampleService.sampleData;
    $scope.sampleId = sampleService.sampleId.data;
    $scope.domainFileUri = "";
    $scope.data = {};    
    // $scope.data = {"type":"physico-chemical characterization","name":null,"parentSampleId":69500928,"charId":69599238,"assayType":"molecular weight","protocolNameVersion":null,"protocolId":0,"characterizationSource":null,"characterizationDate":null,"charNamesForCurrentType":["molecular weight","other_pc","physical state","purity","relaxivity","shape","size","solubility","surface","zeta potential","[other]"],"designMethodsDescription":null,"techniqueInstruments":{"experiments":[{"id":69632002,"displayName":"sfaer927034wqw34(SEC-MALLS)","abbreviation":null,"description":"SY Testing","instruments":[{"manufacturer":"Agilent","modelName":"","type":""},{"manufacturer":"ACT GmbH","modelName":"","type":""}]},{"id":70057984,"displayName":"electron microprobe analysis(EMPA)","abbreviation":null,"description":"","instruments":[{"manufacturer":"Affymetrix","modelName":"","type":""}]},{"id":70385664,"displayName":"asymmetrical flow field-flow fractionation with multi-angle laser light scattering(AFFF-MALLS)","abbreviation":null,"description":"SY Testing","instruments":[{"manufacturer":"Aerotech","modelName":"SY-Model","type":""},{"manufacturer":"Aerotech","modelName":"SY-Model2","type":"photometer"}]},{"id":70549504,"displayName":"electron microprobe analysis(EMPA)","abbreviation":null,"description":"","instruments":[{"manufacturer":"MyFactory","modelName":"SY-Best2","type":"SYType2"},{"manufacturer":"MyFactory","modelName":"SY-Best2","type":"SYType2"}]},{"id":70680576,"displayName":"electron microprobe analysis(EMPA)","abbreviation":null,"description":"","instruments":[{"manufacturer":"MyFactory","modelName":"SY-Best2","type":"SYType2"}]}],"techniqueTypeLookup":["acoustic microscopy","asymmetrical flow field-flow fractionation with multi-angle laser light scattering","atomic absorption spectroscopy","atomic force microscopy","biochemical quantitation","capillary electrophoresis","cell counting","centrifugal filtration","coagulation detection","colony counting","confocal laser scanning microscopy","coulter principle","dark field microscopy","deconvolution fluorescence microscopy","differential centrifugal sedimentation","dynamic light scattering","electron microprobe analysis","electron spin resonance","electron spin resonance spectroscopy","electrophoretic light scattering","elemental analysis","energy dispersive spectroscopy","environmental transmission electron microscopy","fast protein liquid chromatography","flow cytometry","fluorescence microscopy","fluorometry","focused ion beam - scanning electron microscopy","fourier transform infrared spectrophotometry","gas chromatography","gas sorption","gel electrophoresis","gel filtration chromatography","gel permeation chromatography","high performance liquid chromatography","high performance liquid chromatography - evaporative light scattering detection","high resolution scanning electron microscopy","high resolution transmission electron microscopy","illumination","imaging","inductively coupled plasma atomic emission spectroscopy","inductively coupled plasma mass spectrometry","infrared imaging","laser diffraction","liight microscopy","liquid chromatography - mass spectrometry","liquid scintillation counting","magnetic property measurement","mass quantitation","matrix assisted laser desorption ionisation - time of flight","microfluidics","multi photon confocal laser scanning microscopy","multiphoton laser scanning microscopy","NNNN","nuclear magnetic resonance","particle quantitation","photoacoustic imaging","photoacoustic spectrometry","polymerase chain reaction","positron emission tomography","powder diffraction","protein quantitation","radioactivity quantiation","Raman spectroscopy","refractometry","scanning auger spectrometry","scanning electron microscopy","scanning probe microscopy","scanning transmission electron microscopy","scanning tunneling  microscopy","sfaer927034wqw34","size exclusion chromatography with multi-angle laser light scattering","spectrofluorometry","spectrophotometry","surface plasmon resonance","temperature measurement","thermogravimetric analysis","time-resolved fluorescence microscopy ","transmission electron microscopy","wavelength dispersive spectroscopy","weight","X-ray diffraction","X-ray photoelectron spectroscopy","zeta potential analysis","[other]"],"manufacturerLookup":["ACT GmbH","Aerotech","Affymetrix","Agilent","Alltech","Amersham","Amersham Pharmacia Biotech","Applied Biosystems","Applied Precision","Asylum Research","B&W Tek","BD Biosciences","Beckman/Coulter","Becton Dickinson","Biacore","BioLogics","Biorad","BioTek","Brookhaven Instruments","Bruker","Budget Sensors","Caliper Life Sciences","Carl Zeiss","ChemoMetec","CPS Instruments","CTI Concorde Microsystems","Dako","Diagnostica Stago","Dynatech","EDAX","Endra","Eppendorf","FEI","FLIR","Gatan","GE Healthcare","Guava Technologies/Millipore","Hamamatsu","Hewlett-Packard","Hitachi","Horiba","Invitrogen","JEOL","Jobin Yvon","Kodak","Kratos Analytical","Labsystems","Lakeshore","LaVision BioTec","LECO","Leica","Luxtron","Malvern","Micromass","Micromeritics","Millipore","Molecular Devices","Molecular Imaging","Nikon","OBB Corp","Ocean Optics","Olympus","Packard","Panametrics","Park Systems","PerkinElmer","Phenomenex","Philips","Photal Otsuka","Photometrics","Photon Technology International","Picoquant","Point Electronic GmBh","Princeton Instruments","PSS","Quantachrome Instruments","Quantum Design","Renishaw","Rigaku","Roche Applied Science","RPMC Lasers","Sartorius","Shimadzu","Siemens Medical","Soft Imaging Systems","TA Instruments","Tecan","Test","Thermo Electron","Thermo Scientific","TosoHaas","Varian","Visualsonics","Waters","Wyatt Technologies","Zeiss","[other]"]},"selectedOtherSampleNames":null,"copyToOtherSamples":false,"charTypesLookup":["physico-chemical characterization","in vitro characterization","in vivo characterization","SY-New-Char-Type","ex vivo","[other]"],"protocolLookup":[{"domainId":69435392,"domainFileId":69304322,"domainFileUri":"http://www.sciencedirect.com/science/article/pii/S1045105608000572","displayName":"Physicochemical and biological assays for quality control of biopharmaceuticals: Interferon alfa-2 case study, version v1.0"},{"domainId":63340544,"domainFileId":63275009,"domainFileUri":"protocols/20140825_10-55-03-531_20110228_05-08-58-717_Apoptosis_Procedure.pdf","displayName":"Test File Protocol3"},{"domainId":31817728,"domainFileId":31686657,"domainFileUri":"protocols/20111018_12-08-22-229_protocols_20090113_11-11-33-967_NCL_Method_NIST-NCL_PCC-1.pdf","displayName":"Demo-PCC, version 1.0"},{"domainId":29949960,"domainFileId":29655055,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-13.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-13 (PCC-13), version 1.1"},{"domainId":29949959,"domainFileId":29655054,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-12.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-12 (PCC-12), version 1.1"},{"domainId":29949958,"domainFileId":29655053,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-14.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-14 (PCC-14), version 1.0"},{"domainId":29949957,"domainFileId":29655052,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-11.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-11 (PCC-11), version 1.1"},{"domainId":29949956,"domainFileId":29655050,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-9.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-9 (PCC-9), version 1.1"},{"domainId":29949955,"domainFileId":29655049,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-8.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-8 (PCC-8), version 1.1"},{"domainId":29949954,"domainFileId":29655048,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-10.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-10 (PCC-10), version 1.1"},{"domainId":29949953,"domainFileId":29655047,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-7.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-7 (PCC-7), version 1.1"},{"domainId":29949952,"domainFileId":29655046,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-6.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-6 (PCC-6), version 1.1"},{"domainId":25210112,"domainFileId":25177344,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-1.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-1 (PCC-1), version 1.1"}],"charSourceLookup":[{"id":52985856,"displayName":"C-Sixty (CNI) (John Doe)"},{"id":56328198,"displayName":"DC"},{"id":15695880,"displayName":"CP_UCLA_CalTech"},{"id":0,"displayName":"[other]"}],"otherSampleNameLookup":["Demo-1234","NCL-23-1","NCL-23-112312321778","SY-NCL-23-1","Test2_Harika","dasdas343434"],"assayTypesByCharNameLookup":{},"errors":[],"messages":null};
    $scope.dataCopy = angular.copy($scope.data);
    $scope.loader = false;
    $scope.loaderMessage = "Loading";
    $scope.type = sampleService.type.data;
    $scope.isEdit = sampleService.isEdit.data;
    $scope.techniqueInstrument = {};

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
    if ($scope.isEdit) {
        // run initial rest call to setup characterization edit form //
        alert("I AM EDIT");

        $http({method: 'GET', url: '/caNanoLab/rest/characterization/setupEdit?sampleId='+$scope.sampleId+'&charType='+$scope.type}).
            success(function(data, status, headers, config) {
            $scope.data = data;
            $scope.today();
            $scope.loader = false;
            $scope.dataCopy = angular.copy($scope.data);                
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
            $scope.loader = false;
            $scope.dataCopy = angular.copy($scope.data);                            
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
        delete $scope.domainFileUri;
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

    // looks to see if technique type has abbreviation // 
    $scope.techniqueTypeInstrumentDropdownChanged = function() {
        $http({method: 'GET', url: '/caNanoLab/rest/characterization/getAbbreviationByTechniqueType?techniqueType='+ $scope.techniqueInstrument.displayName}).
            success(function(data, status, headers, config) {
            $scope.techniqueInstrument.abbreviation = data;
        }).
            error(function(data, status, headers, config) {
        });         
        // alert("rest call"+$scope.techniqueInstrument.displayName)
    };

    // gets URL for protocol name //
    $scope.getDomainFileUri = function() {
        for (var x = 0; x < $scope.data.protocolLookup.length;x++) {
            if ($scope.data.protocolNameVersion==$scope.data.protocolLookup[x].domainId) {
                $scope.domainFileUri = $scope.data.protocolLookup[x].domainFileUri;
            };  
        };
    };

    $scope.openNewExperimentConfig = function() {
        $scope.newExperimentConfig = 1;
    };

    // save experiment config and close section //
    $scope.saveExperimentConfig = function() {
        $scope.loader = true;
        $scope.loader = false;
        $scope.newExperimentConfig = 0;
    };

    // save record. //
    $scope.save = function() {
    };

    // reset form //
    $scope.reset = function() {
        $scope.data = angular.copy($scope.dataCopy);
        $scope.domainFileUri = "";
        $scope.newExperimentConfig = 0;

    };    

    // cancel save //
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

 });