'use strict';
var app = angular.module('angularApp')
	.controller('SetupCharacterizationCtrl', function ($scope,$http,$modal,sampleService,$location,$anchorScroll,$filter,$upload,$timeout) {

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
    
    /* File Variables */
    $scope.usingFlash = FileAPI && FileAPI.upload != null;
    $scope.fileReaderSupported = window.FileReader != null && (window.FileAPI == null || FileAPI.html5 != false);
    $scope.fileForm = {};
    $scope.fileForm.uriExternal = false;
    $scope.externalUrlEnabled = false;
    $scope.addNewFile = false;
    $scope.selectedFileName = '';  
    var uploadUrl = '/caNanoLab/rest/core/uploadFile';
    if(navigator.appVersion.indexOf("MSIE 9.")!=-1)
        uploadUrl = '/caNanoLab/uploadFile';    

    // $scope.data =  {"type":"physico-chemical characterization","name":"physical state","parentSampleId":67698688,"charId":100237312,"assayType":"physical state","protocolId":0,"characterizationSourceId":0,"characterizationDate":null,"charNamesForCurrentType":["molecular weight","other_pc","physical state","purity","relaxivity","shape","size","solubility","surface","zeta potential","other"],"property":{"@type":"SimplePhysicalState","propertyName":"PhysicalState","propertyDisplayName":"PhysicalState","type":"","typeOptions":["3D-cube","3D-cylinder","colloid-emulsion","colloid-gel","colloid-sol","fluid-gas","fluid-liquid","fluid-vapor","solid-crystal","solid-fibril","solid-glass","solid-granule","solid-powder","other"],"propertyViewTitles":["Type"],"propertyViewValues":[""]},"designMethodsDescription":null,"techniqueInstruments":{"experiments":[{"id":102760448,"displayName":"atomic force microscopy","techniqueType":"atomic force microscopy","abbreviation":null,"description":"","dirty":false,"instruments":[],"parentCharType":"","parentCharName":""}],"techniqueTypeLookup":["acoustic microscopy","asymmetrical flow field-flow fractionation with multi-angle laser light scattering","atomic absorption spectroscopy","atomic force microscopy","biochemical quantitation","capillary electrophoresis","cell counting","centrifugal filtration","coagulation detection","colony counting","confocal laser scanning microscopy","coulter principle","dark field microscopy","deconvolution fluorescence microscopy","differential centrifugal sedimentation","dynamic light scattering","electron microprobe analysis","electron spin resonance","electron spin resonance spectroscopy","electrophoretic light scattering","elemental analysis","energy dispersive spectroscopy","environmental transmission electron microscopy","fast protein liquid chromatography","flow cytometry","fluorescence microscopy","fluorometry","focused ion beam - scanning electron microscopy","fourier transform infrared spectrophotometry","gas chromatography","gas sorption","gel electrophoresis","gel filtration chromatography","gel permeation chromatography","high performance liquid chromatography","high performance liquid chromatography - evaporative light scattering detection","high resolution scanning electron microscopy","high resolution transmission electron microscopy","illumination","imaging","inductively coupled plasma atomic emission spectroscopy","inductively coupled plasma mass spectrometry","infrared imaging","laser diffraction","liight microscopy","liquid chromatography - mass spectrometry","liquid scintillation counting","magnetic property measurement","mass quantitation","matrix assisted laser desorption ionisation - time of flight","microfluidics","multi photon confocal laser scanning microscopy","multiphoton laser scanning microscopy","NNNN","nuclear magnetic resonance","particle quantitation","photoacoustic imaging","photoacoustic spectrometry","polymerase chain reaction","positron emission tomography","powder diffraction","protein quantitation","radioactivity quantiation","Raman spectroscopy","refractometry","scanning auger spectrometry","scanning electron microscopy","scanning probe microscopy","scanning transmission electron microscopy","scanning tunneling microscopy","sfaer927034wqw34","size exclusion chromatography with multi-angle laser light scattering","spectrofluorometry","spectrophotometry","surface plasmon resonance","SY-New-Technique-Type","sy-thurs-techtype-5","sy-thursday-techtype","temperature measurement","thermogravimetric analysis","time-resolved fluorescence microscopy ","transmission electron microscopy","wavelength dispersive spectroscopy","weight","X-ray diffraction","X-ray photoelectron spectroscopy","zeta potential analysis","other"],"manufacturerLookup":["ACT GmbH","Aerotech","Affymetrix","Agilent","Alltech","Amersham","Amersham Pharmacia Biotech","Applied Biosystems","Applied Precision","Asylum Research","B&W Tek","BD Biosciences","Beckman/Coulter","Becton Dickinson","Biacore","BioLogics","Biorad","BioTek","Brookhaven Instruments","Bruker","Budget Sensors","Caliper Life Sciences","Carl Zeiss","ChemoMetec","CPS Instruments","CTI Concorde Microsystems","Dako","Diagnostica Stago","Dynatech","EDAX","Endra","Eppendorf","FEI","FLIR","Gatan","GE Healthcare","Guava Technologies/Millipore","Hamamatsu","Hewlett-Packard","Hitachi","Horiba","Invitrogen","JEOL","Jobin Yvon","Kodak","Kratos Analytical","Labsystems","Lakeshore","LaVision BioTec","LECO","Leica","Luxtron","Malvern","Micromass","Micromeritics","Millipore","Molecular Devices","Molecular Imaging","Nikon","OBB Corp","Ocean Optics","Olympus","Packard","Panametrics","Park Systems","PerkinElmer","Phenomenex","Philips","Photal Otsuka","Photometrics","Photon Technology International","Picoquant","Point Electronic GmBh","Princeton Instruments","PSS","Quantachrome Instruments","Quantum Design","Renishaw","Rigaku","Roche Applied Science","RPMC Lasers","Sartorius","Shimadzu","Siemens Medical","Soft Imaging Systems","SY factory","sy-thurs-manuf-5","sy-thursday-inst-type","TA Instruments","Tecan","Test","Thermo Electron","Thermo Scientific","TosoHaas","Varian","Visualsonics","Waters","Wyatt Technologies","Zeiss","other"]},"finding":[{"findingId":100171777,"numberOfColumns":1,"numberOfRows":1,"rows":[{"cells":[{"value":"","datumOrCondition":"datum","columnOrder":null,"createdDate":null}]}],"columnHeaders":[{"columnName":"asd","conditionProperty":null,"valueType":null,"valueUnit":null,"columnType":"datum","displayName":"asd","constantValue":"","columnOrder":1,"createdDate":1411062444000}],"files":[],"theFile":{"uriExternal":false,"uri":"","type":"","title":"","description":"","keywordsStr":"","id":null,"createdBy":"","createdDate":null,"sampleId":"","errors":null,"externalUrl":"","theAccess":null,"isPublic":false},"theFileIndex":-1,"dirty":false,"errors":[],"parentCharType":"","parentCharName":""}],"analysisConclusion":null,"selectedOtherSampleNames":[],"copyToOtherSamples":false,"submitNewChar":false,"charTypesLookup":["physico-chemical characterization","in vitro characterization","in vivo characterization","Canano_char_type","SY-New-Char-Type","TEST111","ex vivo","other"],"protocolLookup":[{"domainId":69435392,"domainFileId":69304322,"domainFileUri":"http://www.sciencedirect.com/science/article/pii/S1045105608000572","displayName":"Physicochemical and biological assays for quality control of biopharmaceuticals: Interferon alfa-2 case study, version v1.0"},{"domainId":63340544,"domainFileId":63275009,"domainFileUri":"protocols/20140825_10-55-03-531_20110228_05-08-58-717_Apoptosis_Procedure.pdf","displayName":"Test File Protocol3"},{"domainId":31817728,"domainFileId":31686657,"domainFileUri":"protocols/20111018_12-08-22-229_protocols_20090113_11-11-33-967_NCL_Method_NIST-NCL_PCC-1.pdf","displayName":"Demo-PCC, version 1.0"},{"domainId":29949960,"domainFileId":29655055,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-13.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-13 (PCC-13), version 1.1"},{"domainId":29949959,"domainFileId":29655054,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-12.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-12 (PCC-12), version 1.1"},{"domainId":29949958,"domainFileId":29655053,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-14.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-14 (PCC-14), version 1.0"},{"domainId":29949957,"domainFileId":29655052,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-11.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-11 (PCC-11), version 1.1"},{"domainId":29949956,"domainFileId":29655050,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-9.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-9 (PCC-9), version 1.1"},{"domainId":29949955,"domainFileId":29655049,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-8.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-8 (PCC-8), version 1.1"},{"domainId":29949954,"domainFileId":29655048,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-10.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-10 (PCC-10), version 1.1"},{"domainId":29949953,"domainFileId":29655047,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-7.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-7 (PCC-7), version 1.1"},{"domainId":29949952,"domainFileId":29655046,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-6.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-6 (PCC-6), version 1.1"},{"domainId":25210112,"domainFileId":25177344,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-1.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-1 (PCC-1), version 1.1"}],"charSourceLookup":[{"id":64847872,"displayName":"BROWN_STANFORD"}],"otherSampleNameLookup":["BROWN_STANFORD-HLeeJNM2008-01","BROWN_STANFORD-HLeeJNM2008-01-copy","BROWN_STANFORD-HLeeJNM2008-02","BROWN_STANFORD-HLeeJNM2008-03","BROWN_STANFORD-HLeeJNM2008-04","BROWN_STANFORD-HLeeJNM2008-05","BROWN_STANFORD-JXieJACS2008-01","NCL-23-1123123217784","NCL-23-1123321312321321321321312","NCL-23-123232322232","NCL-23-1232323232323232323232","NCL-23-1232323299181881","NCL-23-1983989238981298219321","NCL-23-1983989238981298219321232","NCL-23-199288292","NCL-49-421323","NCL-49-43343434343443","SY-CANANOLAB-161","gsaeteateara","ncl-24-1-Copy-sy","slsldhafasd","sy-sprint-4-2","sy-sprint-4-3","testDSADJASKDSAJKJSDKASD"],"datumConditionValueTypeLookup":["boolean","mean","median","mode","number averaged","number of replicates","number of samples","observed","RMS","standard deviation","standard error of mean","SY-Test-Column-valuetype","sy-thurs-colvaltype-5","sy-thurs-colvaluetype-6","Z-average","Z-score","other"],"assayTypesByCharNameLookup":["physical state","other"],"errors":[],"messages":[],"dirtyFindingBean":null,"dirtyExperimentBean":null};
    // $scope.columnNameLookup = ["asd","other"];
    $scope.fileTypes = ["document","graph","image","movie","spread sheet"];


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
        // $scope.toggleMin();
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
        $scope.saveButton = "Update";
        $http({method: 'GET', url: '/caNanoLab/rest/characterization/setupUpdate?sampleId='+$scope.sampleId+'&charId='+$scope.charId+'&charClassName='+$scope.charClassName+'&charType='+$scope.type}).
            success(function(data, status, headers, config) {
            $scope.data = data;
            $scope.sampleName = sampleService.sampleName($scope.sampleId);

            if (!$scope.data.characterizationDate) {
                $scope.data.characterizationDate = null;
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
        $scope.saveButton = "Submit";        
        $http({method: 'GET', url: '/caNanoLab/rest/characterization/setupAdd?sampleId='+$scope.sampleId+'&charType='+$scope.type}).
            success(function(data, status, headers, config) {
            $scope.data = data;
            $scope.sampleName = sampleService.sampleName($scope.sampleId);

            // $scope.today();
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

    // gets assay types when characterization name dropdown is changed //
    $scope.characterizationNameDropdownChanged = function() {
        delete $scope.data.assayType;

        $scope.loader = true;
        $http({method: 'GET', url: '/caNanoLab/rest/characterization/getAssayTypesByCharName?charName='+ $scope.data.name}).
            success(function(data, status, headers, config) {
            $scope.data.assayTypesByCharNameLookup = data;
            $scope.loader = false;
        }).
            error(function(data, status, headers, config) {
            $scope.loader = false;        
        }); 

        // gets property block //
        $http({method: 'GET', url: '/caNanoLab/rest/characterization/getCharProperties?charName='+ $scope.data.name}).
            success(function(data, status, headers, config) {
            $scope.data.property = data;
        }).
            error(function(data, status, headers, config) {
            $scope.loader = false;        
        });         
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
        if ($scope.isNewExperimentConfig) {
            $scope.data.techniqueInstruments.experiments.push($scope.techniqueInstrument);
        };
        $scope.techniqueInstrument.parentCharType = $scope.data.type;
        $scope.techniqueInstrument.parentCharName = $scope.data.name;
        $scope.techniqueInstrument.dirty = 1;
        $http({method: 'POST', url: '/caNanoLab/rest/characterization/saveExperimentConfig',data: $scope.data}).
        success(function(data, status, headers, config) {   
            $scope.saveButton = "Update";        
            $scope.loader = false;
            $scope.data=data;
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
            $scope.data=data;
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
        $scope.currentFinding.dirty = 1;
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
        $scope.currentFinding.dirty = 1;
        $scope.currentFindingCopy = angular.copy(finding);
    };

    // updates rows and columns and runs rest call update //
    $scope.updateRowsColsForFinding = function() {
        $scope.loader = true;        
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
    $scope.openColumnForm = function(cell) {
        $scope.findingsColumn = cell;
        $scope.columnForm = 1;
        $scope.findingsColumnCopy = angular.copy($scope.findingsColumn);
        if ($scope.findingsColumn.columnType) {
            $scope.onColumnTypeDropdownChange(1);
        }
    };

    // close column form without saving //
    $scope.closeColumnForm = function() {
        angular.copy($scope.findingsColumnCopy, $scope.findingsColumn);
        $scope.columnForm = 0;
    };

    // close column form with saving //
    $scope.saveColumnForm = function() {
        $scope.columnForm = 0;
        
        var columnIndex = 0;
        if( $scope.findingsColumn.columnOrder != null ) {
            columnIndex = parseInt($scope.findingsColumn.columnOrder) - 1;
            var headerName = $scope.findingsColumn.columnName;

            if( $scope.findingsColumn.valueType != null ) {
                headerName = headerName + ' (' + $scope.findingsColumn.valueType + ')';
            }
            $scope.currentFinding.columnHeaders[columnIndex].displayName = headerName;

        }
    };    

    // remove column data //
    $scope.removeColumnForm = function() {
        angular.copy($scope.findingsColumnCopy, $scope.findingsColumn);
        $scope.columnForm = 0;

    };    

    // opens column form to change order for columns. Does not actually order columns //
    $scope.openColumnOrderForm = function() {
        $scope.columnOrder = 1;
    }; 

    // called when columnType dropdown is changed on column form //
    $scope.onColumnTypeDropdownChange = function(newOpen) {
        $http({method: 'GET', url: '/caNanoLab/rest/characterization/getColumnNameOptionsByType?columnType='+$scope.findingsColumn.columnType+'&charName='+$scope.data.name+'&assayType='+$scope.data.assayType}).
            success(function(data, status, headers, config) {
            $scope.columnNameLookup = data;
            $scope.loader = false;
        }).
            error(function(data, status, headers, config) {
            $scope.loader = false;        
        });  

        if (newOpen) {
            $scope.getColumnValueUnitOptions();
        };        
    };

    // gets column value unit options based on  name and condition //
    $scope.getColumnValueUnitOptions = function() {
       $http({method: 'GET', url: '/caNanoLab/rest/characterization/getColumnValueUnitOptions?columnName='+$scope.findingsColumn.columnName+'&conditionProperty='+$scope.findingsColumn.conditionProperty}).
            success(function(data, status, headers, config) {
            $scope.valueUnitsLookup = data;
            $scope.loader = false;
        }).
            error(function(data, status, headers, config) {
            $scope.loader = false;        
        }); 
    };

    // called when columnType dropdown is changed on column form //
    $scope.onColumnNameDropdownChange = function() {
        $scope.getColumnValueUnitOptions();
        $http({method: 'GET', url: '/caNanoLab/rest/characterization/getConditionPropertyOptions?columnName='+$scope.findingsColumn.columnName}).
            success(function(data, status, headers, config) {
            $scope.conditionTypeLookup = data;
            $scope.loader = false;
        }).
            error(function(data, status, headers, config) {
            $scope.loader = false;        
        });        
    };    

    // sets the column order //
    $scope.updateColumnOrder = function() {
        $scope.loader = true;        
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
        $scope.loader = true;
        if ($scope.isNewFinding) {
            $scope.data.finding.push($scope.currentFinding);            
        };

        for (var x=0; x<$scope.data.finding.length;x++) {
            if ($scope.data.finding[x].findingId==$scope.currentFinding.findingId) {
                $scope.data.finding.splice(x,1);
                $scope.data.finding.push($scope.currentFinding);
                break;
            }
        };

        $http({method: 'POST', url: '/caNanoLab/rest/characterization/saveFinding',data: $scope.data}).
        success(function(data, status, headers, config) { 
            $scope.saveButton = "Update";                           
            $scope.loader = false;
            $scope.data=data;
        }).
        error(function(data, status, headers, config) {
            $scope.loader = false;
        }); 
        $scope.updateFinding = 0;        
    };

    // removes finding info //
    $scope.removeFindingInfo = function() {
    	if (confirm("Delete the Finding?")) {
    		$scope.loader = true;
            $http({method: 'POST', url: '/caNanoLab/rest/characterization/removeFinding',data: $scope.currentFinding}).
            success(function(data, status, headers, config) {            
                $scope.loader = false;
                $scope.data=data;
            }).
            error(function(data, status, headers, config) {
                $scope.loader = false;
            }); 
    		$scope.updateFinding = 0;        
    	}
    };    

    $scope.cancelFindingInfo = function() {
        $scope.updateFinding = 0; 
        angular.copy($scope.currentFindingCopy, $scope.currentFinding);
    };   

    // deletes data and condition row //
    $scope.deleteDataConditionRow = function(row) {
        $scope.currentFinding.rows.splice($scope.currentFinding.rows.indexOf(row),1);
    };

    // save characterization record. //
    $scope.save = function() {
        $scope.loader = true;    
        $http({method: 'POST', url: '/caNanoLab/rest/characterization/saveCharacterization',data: $scope.data}).
        success(function(data, status, headers, config) {  
            $location.path("/editCharacterization").search({'sampleId':$scope.sampleId}).replace();
            $scope.loader = false;
        }).
        error(function(data, status, headers, config) {
            $scope.loader = false;
        }); 
    };

    // remove characterization record. //
    $scope.remove = function() {
        $scope.loader = true;
        $http({method: 'POST', url: '/caNanoLab/rest/characterization/removeCharacterization',data: $scope.data}).
        success(function(data, status, headers, config) {            
            $scope.loader = false;
            $location.path("/editCharacterization").search({'sampleId':$scope.sampleId}).replace();
        }).
        error(function(data, status, headers, config) {
            $scope.loader = false;
        }); 
    };    

    // reset form //
    $scope.reset = function() {
        $scope.data = angular.copy($scope.dataCopy);
        $scope.domainFileUri = "";
        $scope.updateExperimentConfig = 0;
    };    
    
    /* File Section */
    $scope.onFileSelect = function($files) {
        $scope.selectedFiles = [];
        $scope.selectedFiles = $files;
        
        $scope.dataUrls = [];
		for ( var i = 0; i < $files.length; i++) {
			var $file = $files[i];
			if ($scope.fileReaderSupported && $file.type.indexOf('image') > -1) {
				var fileReader = new FileReader();
				fileReader.readAsDataURL($files[i]);
				var loadFile = function(fileReader, index) {
					fileReader.onload = function(e) {
						$timeout(function() {
							$scope.dataUrls[index] = e.target.result;
						});
					}
				}(fileReader, i);
			}
		}        
    };

    $scope.editFile = function(fileId) {
    	$scope.selectedFileName = ''; 
        for (var k = 0; k < $scope.currentFinding.files.length; ++k) {
            var element = $scope.currentFinding.files[k];
            if (element.id == fileId ) {
                $scope.fileForm.externalUrl = element.externalUrl;
                $scope.fileForm.uri = element.uri;
                $scope.fileForm.uriExternal = element.uriExternal;
                $scope.fileForm.type = element.type;
                $scope.fileForm.title = element.title;
                $scope.fileForm.keywordsStr = element.keywordsStr;
                $scope.fileForm.description = element.description;
                $scope.fileForm.id = element.id;
                $scope.fileForm.createdBy = element.createdBy;
                $scope.fileForm.createdDate = element.createdDate;

                $scope.addNewFile = true;
                
                
                if( $scope.fileForm.externalUrl != null && $scope.fileForm.externalUrl != '') {
                    $scope.externalUrlEnabled = true;
                    $scope.fileForm.uriExternal = 'true';
                }
                else {
                    $scope.externalUrlEnabled  = false;
                    $scope.fileForm.uriExternal = 'false';
                }

                break;
            }
        }
    }

    $scope.removeFile = function(fileId) {
        if (confirm("Are you sure you want to delete the File?")) {
            $scope.loader = true;

            for (var k = 0; k < $scope.currentFinding.files.length; ++k) {
                var element = $scope.currentFinding.files[k];
                if (element.id == $scope.fileForm.id) {
                    $scope.currentFinding.files.splice(k,1);
                }
            }
            //$scope.currentFinding.files = $scope.files;

            if( $scope.currentFinding.theFile == null ) {
                $scope.currentFinding.theFile = {};
            }

            $scope.currentFinding.theFile.externalUrl = $scope.fileForm.externalUrl;
            $scope.currentFinding.theFile.uri = $scope.fileForm.uri;
            $scope.currentFinding.theFile.uriExternal = $scope.fileForm.uriExternal;
            $scope.currentFinding.theFile.type = $scope.fileForm.type;
            $scope.currentFinding.theFile.title = $scope.fileForm.title;
            $scope.currentFinding.theFile.keywordsStr = $scope.fileForm.keywordsStr;
            $scope.currentFinding.theFile.description = $scope.fileForm.description;
            $scope.currentFinding.theFile.id = $scope.fileForm.id;
            $scope.currentFinding.theFile.theAccess = $scope.fileForm.theAccess;
            $scope.currentFinding.theFile.isPublic = $scope.fileForm.isPublic;
            $scope.currentFinding.theFile.createdBy = $scope.fileForm.createdBy;
            $scope.currentFinding.theFile.createdDate = $scope.fileForm.createdDate;

            if( $scope.sampleId != null ) {
                $scope.currentFinding.theFile.sampleId = $scope.sampleId;
            }


            $http({method: 'POST', url: '/caNanoLab/rest/characterization/removeFile',data: $scope.currentFinding}).
                success(function(data, status, headers, config) {
                	$scope.currentFinding = data;
                    $scope.addNewFile = false;
                    $scope.loader = false;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    // $rootScope.sampleData = data;
                    $scope.loader = false;
                    $scope.data.errors = data;
                });
        }
    };

    $scope.saveFile = function() {
        $scope.loader = true;
        var index = 0;
        $scope.upload = [];
        if ($scope.selectedFiles != null && $scope.selectedFiles.length > 0 ) {
            $scope.selectedFileName = $scope.selectedFiles[0].name;
            $scope.upload[index] = $upload.upload({
                url: uploadUrl,
                method: 'POST',
                headers: {'my-header': 'my-header-value'},
                data : $scope.fileForm,
                file: $scope.selectedFiles[index],
                fileFormDataName: 'myFile'
            });
            $scope.upload[index].then(function(response) {
                $timeout(function() {
                    //$scope.uploadResult.push(response.data);
                    //alert(response.data);
                    //$scope.nanoEntityForm = response.data;
                    $scope.saveFileData();
                    //$scope.loader = false;
                });
            }, function(response) {
                $timeout(function() {
                	//only for IE 9
                    if(navigator.appVersion.indexOf("MSIE 9.")!=-1) {
                        $scope.saveFileData();
                    }
                });            	
                if (response.status > 0) {
                	$scope.data.errors = response.status + ': ' + response.data;
                    $scope.loader = false;
                }
            }, function(evt) {
                // Math.min is to fix IE which reports 200% sometimes
                // $scope.progress[index] = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
            });
            $scope.upload[index].xhr(function(xhr){
//			xhr.upload.addEventListener('abort', function() {console.log('abort complete')}, false);
            });
        }
        else {
            $scope.saveFileData();
        }
    };

    $scope.saveFileData = function() {
        $scope.loader = true;

        var k=0;
        if ( $scope.fileForm.id != null && $scope.fileForm.id != '' ) {
            for (k = 0; k < $scope.currentFinding.files.length; ++k) {
                var element = $scope.currentFinding.files[k];
                if (element.id == $scope.fileForm.id) {
                    $scope.currentFinding.files[k] = $scope.fileForm;
                }
            }
        }
        /*            else {
         $scope.files.push($scope.fileForm);
         }
         */
        //$scope.currentFinding.files = $scope.files;

        if( $scope.currentFinding.theFile == null ) {
            $scope.currentFinding.theFile = {};
        }

        $scope.currentFinding.theFile.externalUrl = $scope.fileForm.externalUrl;
        if( $scope.selectedFileName != null && $scope.selectedFileName != '' ) {
        	$scope.currentFinding.theFile.uri = $scope.selectedFileName;
        } else {
        	$scope.currentFinding.theFile.uri = $scope.fileForm.uri;
        }        
        $scope.currentFinding.theFile.uriExternal = $scope.fileForm.uriExternal;
        $scope.currentFinding.theFile.type = $scope.fileForm.type;
        $scope.currentFinding.theFile.title = $scope.fileForm.title;
        $scope.currentFinding.theFile.keywordsStr = $scope.fileForm.keywordsStr;
        $scope.currentFinding.theFile.description = $scope.fileForm.description;
        $scope.currentFinding.theFile.id = $scope.fileForm.id;
        $scope.currentFinding.theFile.theAccess = $scope.fileForm.theAccess;
        $scope.currentFinding.theFile.isPublic = $scope.fileForm.isPublic;
        $scope.currentFinding.theFile.createdBy = $scope.fileForm.createdBy;
        $scope.currentFinding.theFile.createdDate = $scope.fileForm.createdDate;

        if( $scope.fileForm.id == null || $scope.fileForm.id == '') {
            $scope.currentFinding.theFileIndex = -1;
        } else {
            $scope.currentFinding.theFileIndex = k-1;
        }

        if( $scope.sampleId != null ) {
            $scope.currentFinding.theFile.sampleId = $scope.sampleId;
        }

        $scope.messages = [];
        $http({method: 'POST', url: '/caNanoLab/rest/characterization/saveFile',data: $scope.currentFinding}).
            success(function(data, status, headers, config) {
                $scope.currentFinding = data;
                $scope.addNewFile = false;
                $scope.loader = false;
            }).
            error(function(data, status, headers, config) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
                // $rootScope.sampleData = data;
                $scope.loader = false;
                $scope.data.errors = data;
                $scope.addNewFile = true;
            });
    };

    $scope.getAddNewFile = function() {
        return $scope.addNewFile;
    };

    $scope.closeAddNewFile = function() {
        $scope.addNewFile = false;
    };

    $scope.columnInvalid = function() {
        for (var x = 0; x < $scope.currentFinding.columnHeaders.length;x++) {
            var col = $scope.currentFinding.columnHeaders[x];
            if (!col.columnType || col.columnType=='') {
                return 1
                break
            };            
        };
    };

    $scope.disableColumn = function(option) {
        for (var x = 0; x < $scope.currentFinding.columnHeaders.length;x++) {
            var col = $scope.currentFinding.columnHeaders[x];
            if (col.columnName==option) {
                return 1;
                break;
            };

        };        
    };

    $scope.openAddNewFile = function() {
        $scope.addNewFile = true;
        $scope.fileForm = {};
        
        $scope.fileForm.uriExternal = 'false';
        $scope.externalUrlEnabled = false;          
    };

    /* End File Section */

 });