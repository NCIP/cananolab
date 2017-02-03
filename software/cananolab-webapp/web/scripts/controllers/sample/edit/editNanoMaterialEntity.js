'use strict';
var app = angular.module('angularApp')

    .controller('EditNanoEntityCtrl', function (navigationService,groupService,$rootScope,$scope,$http,$location,$timeout,$routeParams,$upload) {
        $scope.nanoEntityForm = {};
        $rootScope.tabs = navigationService.get();
        $rootScope.groups = groupService.getGroups.data.get();

        $scope.nanoEntityId = '';
        $scope.localForm = {};
        $scope.localForm.otherCategoryText = '';
        $scope.sampleId = $routeParams.sampleId;
        $scope.nanoEntityId = $routeParams.nanoEntityId;
        $scope.sampleName = $routeParams.sampleName;
        //$scope.sampleId = 20917506;
        //$scope.nanoEntityId = 60260353;
        $scope.otherSampleNames = [];
        $scope.localForm.otherSampleNames = [];
        $scope.yesNoOptions = {"" : "", "true" : "Yes", "false" : "No" };


        /* File Variables */
        $scope.usingFlash = FileAPI && FileAPI.upload != null;
        $scope.fileReaderSupported = window.FileReader != null && (window.FileAPI == null || FileAPI.html5 != false);
        $scope.fileForm = {};
        $scope.fileForm.uriExternal = false;
        $scope.externalUrlEnabled = false;
        $scope.addNewFile = false;
        $scope.selectedFileName = '';
       
        var uploadUrl = '/caNanoLab/rest/core/uploadFile';
        $scope.ie9 = false;
        if(navigator.appVersion.indexOf("MSIE 9.")!=-1){
            uploadUrl = '/caNanoLab/uploadFile';
            $scope.ie9 = true;
        }

        /* Composing Element Variables */
        $scope.composingElementForm = {};
        $scope.addNewInherentFunction = false;
        $scope.showInherentFunctionTable = false;
        $scope.composingElementForm.inherentFunction = [];
        $scope.theInherentFunction = {};
        $scope.showModality = false;


        $scope.$on('$viewContentLoaded', function(){
        	 $scope.loader = true;
            $http({method: 'GET', url: '/caNanoLab/rest/nanomaterialEntity/setup?sampleId=' + $scope.sampleId}).
                success(function(data, status, headers, config) {
                    $scope.data = data;
                    //$scope.data = {"pubChemDataSources":["Compound","Substance","BioAssay"],"modalityTypes":["beta radiation","bioluminescence","fluorescence","gamma ray","infrared","MRI","neutron scattering","NMR","PET","photoluminescence","Raman spectroscopy","SPECT","ultrasound","X-ray"],"fileTypes":["document","graph","image","movie","spread sheet"],"otherSampleNames":["Demo123","NCL-20-1","NCL-21-1","NCL-22-1","NCL-24-1","NCL-24-1-Copy","NCL-25-1","QA"],"emulsionComposingElementTypes":["bulk phase","coat","core","dispersed phase","emulsifier","excipient","modifier","coat","core","excipient","Internal buffer","lipid","modifier","monomer","polymer","repeat unit","RNA","shell","terminal group"],"nanomaterialEntityTypes":["biopolymer","carbon","carbon black","carbon nanotube","dendrimer","emulsion","fullerene","liposome","metal oxide","metal particle","metalloid","nanohorn","nanolipogel","nanorod","nanoshell","polymer","quantum dot","silica"],"functionTypes":["endosomolysis","imaging function","other","targeting function","therapeutic function","transfection"],"molecularFormulaTypes":["Hill","SMARTS","SMILES"],"composingElementTypes":["coat","core","excipient","Internal buffer","lipid","modifier","monomer","polymer","repeat unit","RNA","shell","terminal group"],"composingElementUnits":["%","%mole","%vol","%w","%wt","%wt/vol","%wt/wt","Da","g","M","MBq","mCi","mg","mg/mL","mL","mM","mmol","nM","nmol","pmol","uCi/mg","ug","ug/mL","ug/uL","uL","uM","umol","wt%","wt/wt"]};
                    $scope.nanoEntityTypes = $scope.data.nanomaterialEntityTypes;
                    $scope.composingElementTypes = $scope.data.composingElementTypes;
                    $scope.pubChemDataSources = $scope.data.pubChemDataSources;
                    $scope.composingElementUnits = $scope.data.composingElementUnits;
                    $scope.molecularFormulaTypes = $scope.data.molecularFormulaTypes;
                    $scope.functionTypes = $scope.data.functionTypes;
                    $scope.modalityTypes = $scope.data.modalityTypes;
                    $scope.otherSampleNames = $scope.data.otherSampleNames;
                    $scope.fileTypes = $scope.data.fileTypes;
                    $scope.biopolymerTypes = $scope.data.biopolymerTypes;
                    $scope.carbonNanoTubeaverageLengthUnits = $scope.data.composingElementUnits;
                    $scope.carbonNanoTubeDiameterUnits = $scope.data.dimensionUnits;
                    $scope.carbonNanoTubeWallTypes = $scope.data.wallTypes;
                    $scope.fullereneDiameterUnits = $scope.data.dimensionUnits;

                    $scope.loader = false;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $scope.messages = data;
                    $scope.loader = false;
                });
        });

        $scope.showProperties = function() {
            if( $scope.nanoEntityForm.type == 'biopolymer') {
                $scope.nanoEntityForm.withProperties = true;
                $scope.detailsPage = '/caNanoLab/views/sample/composition/nanomaterialEntity/BiopolymerInfoEdit.html';
            }else if( $scope.nanoEntityForm.type == 'polymer') {
                $scope.nanoEntityForm.withProperties = true;
                $scope.detailsPage = '/caNanoLab/views/sample/composition/nanomaterialEntity/PolymerInfoEdit.html';
                if( typeof($scope.nanoEntityForm.domainEntity) != "undefined" ) {
                	if( $scope.nanoEntityForm.domainEntity.crossLinkDegree != null) {
                		$scope.nanoEntityForm.domainEntity.crossLinkDegree = $scope.nanoEntityForm.domainEntity.crossLinkDegree.toString();
                	} 
                	if($scope.nanoEntityForm.domainEntity.isCrossLinked != null){
                    	$scope.nanoEntityForm.domainEntity.isCrossLinked = $scope.nanoEntityForm.domainEntity.isCrossLinked.toString();
                	}
                }                
            }else if( $scope.nanoEntityForm.type == 'carbon nanotube') {
                $scope.nanoEntityForm.withProperties = true;
                $scope.detailsPage = '/caNanoLab/views/sample/composition/nanomaterialEntity/CarbonNanotubeInfoEdit.html';
                if( typeof($scope.nanoEntityForm.domainEntity) != "undefined" ) {
                	if($scope.nanoEntityForm.domainEntity.averageLength != null){
                		$scope.nanoEntityForm.domainEntity.averageLength = $scope.nanoEntityForm.domainEntity.averageLength.toString(); 
                	}
                	if($scope.nanoEntityForm.domainEntity.diameter != null){
                		$scope.nanoEntityForm.domainEntity.diameter = $scope.nanoEntityForm.domainEntity.diameter.toString(); 
                	}
                }
            }else if( $scope.nanoEntityForm.type == 'dendrimer') {
                $scope.nanoEntityForm.withProperties = true;
                $scope.detailsPage = '/caNanoLab/views/sample/composition/nanomaterialEntity/DendrimerInfoEdit.html';
                if( typeof($scope.nanoEntityForm.domainEntity) != "undefined" ) {
                	if($scope.nanoEntityForm.domainEntity.generation != null){
                		$scope.nanoEntityForm.domainEntity.generation = $scope.nanoEntityForm.domainEntity.generation.toString(); //json returns float - so manually convert back to string
                	}
                }
            }else if( $scope.nanoEntityForm.type == 'emulsion') {
                $scope.nanoEntityForm.withProperties = true;
                $scope.detailsPage = '/caNanoLab/views/sample/composition/nanomaterialEntity/EmulsionInfoEdit.html';
                if( typeof($scope.nanoEntityForm.domainEntity) != "undefined" ) {
                	if($scope.nanoEntityForm.domainEntity.isPolymerized != null){
                		$scope.nanoEntityForm.domainEntity.isPolymerized = $scope.nanoEntityForm.domainEntity.isPolymerized.toString(); 
                	}
                }
            }else if( $scope.nanoEntityForm.type == 'fullerene') {
                $scope.nanoEntityForm.withProperties = true;
                $scope.detailsPage = '/caNanoLab/views/sample/composition/nanomaterialEntity/FullereneInfoEdit.html';
                if( typeof($scope.nanoEntityForm.domainEntity) != "undefined" ) {
                	if($scope.nanoEntityForm.domainEntity.averageDiameter != null){
                		$scope.nanoEntityForm.domainEntity.averageDiameter = $scope.nanoEntityForm.domainEntity.averageDiameter.toString(); 
                	}
                	if($scope.nanoEntityForm.domainEntity.numberOfCarbon != null){
                		$scope.nanoEntityForm.domainEntity.numberOfCarbon = $scope.nanoEntityForm.domainEntity.numberOfCarbon.toString(); 
                	}
                }                
            }else if( $scope.nanoEntityForm.type == 'liposome') {
                $scope.nanoEntityForm.withProperties = true;
                $scope.detailsPage = '/caNanoLab/views/sample/composition/nanomaterialEntity/LiposomeInfoEdit.html';
                if( typeof($scope.nanoEntityForm.domainEntity) != "undefined" ) {
                	if($scope.nanoEntityForm.domainEntity.isPolymerized != null){
                		$scope.nanoEntityForm.domainEntity.isPolymerized = $scope.nanoEntityForm.domainEntity.isPolymerized.toString();
                	}
                }
            } else {
                $scope.nanoEntityForm.withProperties = false;
            }
        };

        $scope.loadNanoEntityData = function() {
            if( $scope.nanoEntityId != null ) {
                    $scope.loader = true;
                    $http({method: 'GET', url: '/caNanoLab/rest/nanomaterialEntity/edit?sampleId=' + $scope.sampleId + '&dataId=' + $scope.nanoEntityId}).
                        success(function(data, status, headers, config) {
                            $scope.nanoEntityForm = data;
                            //$scope.nanoEntityForm = {"simpleCompBean":{"type":"core","name":"Test Chem","pubChemDataSourceName":"Compound","pubChemId":100,"value":200.0,"valueUnit":"mg","molecularFormulaType":"Hill","molecularFormula":"Test Formula","description":"Test Desc","id":22064384,"sampleId":"","modality":"","createdBy":"","inherentFunction":[{"id":70090753,"description":"Test endo","modality":null,"type":"endosomolysis"},{"id":70090752,"description":"test image","modality":"bioluminescence","type":"imaging function"}]},"fileBean":{"uriExternal":false,"uri":null,"type":"document","title":"File Doc 1","description":"File Doc Desc","keywordsStr":"FILE DOC KEYWORD","id":69992448},"type":"dendrimer","id":0,"description":"free MagnevistÃ???Ã??Ã?Â®","sampleId":"20917508","userDeletable":false,"userUpdatable":false,"createdBy":"prakasht","createdDate":1165872924000,"domainEntity":{"id":21867783,"sampleComposition":{"id":null,"nanomaterialEntityCollection":null,"sample":null,"chemicalAssociationCollection":null,"fileCollection":null,"functionalizingEntityCollection":null},"createdBy":"prakasht","branch":"1.0","createdDate":"prakasht","generation":2.0},"composingElements":[{"type":"core","name":"Test Chem","pubChemDataSourceName":"Compound","pubChemId":100,"value":200.0,"valueUnit":"mg","molecularFormulaType":"Hill","molecularFormula":"Test Formula","description":"Test Desc","id":22064384,"sampleId":"","modality":"","createdBy":"","inherentFunction":[{"id":70090753,"description":"Test endo","modality":null,"type":"endosomolysis"},{"id":70090752,"description":"test image","modality":"bioluminescence","type":"imaging function"}]}],"files":[{"uriExternal":false,"uri":null,"type":"document","title":"File Doc 1","description":"File Doc Desc","keywordsStr":"FILE DOC KEYWORD","id":69992448}],"withProperties":false,"isPolymerized":null,"isCrossLinked":null,"errors":null};
                            $scope.composingElements = $scope.nanoEntityForm.composingElements;
                            $scope.files = $scope.nanoEntityForm.files;

                            $scope.showProperties();

                            $scope.loader = false;
                        }).
                        error(function(data, status, headers, config) {
                            $scope.messages = data;
                            $scope.loader = false;
                        });
            }
        }

        if( $scope.nanoEntityId != null ) {
            $scope.loadNanoEntityData();
        }
        else {
            $scope.addNewComposingElement=true;
            $scope.composingElements = [];
            $scope.files = [];
        }

        $scope.doSubmit = function() {
            $scope.loader = true;

            if( $scope.sampleId != null ) {
            	$scope.nanoEntityForm.sampleId = $scope.sampleId;
            }
            
            $scope.nanoEntityForm.otherSampleNames = $scope.localForm.otherSampleNames;

            $http({method: 'POST', url: '/caNanoLab/rest/nanomaterialEntity/submit',data: $scope.nanoEntityForm}).
                success(function(data, status, headers, config) {
                    if (data == "success") {
                        //$location.search('message', 'Nanomaterial Entity successfully saved "').path('/message').replace();
                        $location.search('message', 'Nanomaterial Entity successfully saved.').path('/editComposition').replace();
                    }
                    else {
                        $scope.loader = false;
                        $scope.messages = data;
                    }

                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    // $rootScope.sampleData = data;
                    $scope.loader = false;
                    $scope.messages = data;
                });

        };

        $scope.doDelete = function() {
            if (confirm("Are you sure you want to delete the Nanomaterial Entity?")) {
                $scope.loader = true;

                $http({method: 'POST', url: '/caNanoLab/rest/nanomaterialEntity/delete',data: $scope.nanoEntityForm}).
                    success(function(data, status, headers, config) {
                        if (data == "success") {
                        	$location.search('message', 'Nanomaterial Entity successfully deleted.').path('/editComposition').replace();
                        }
                        else {
                            $scope.loader = false;
                            $scope.messages = data;
                        }

                    }).
                    error(function(data, status, headers, config) {
                        // called asynchronously if an error occurs
                        // or server returns response with an error status.
                        // $rootScope.sampleData = data;
                        $scope.loader = false;
                        $scope.messages = data;
                    });
            }
        };

        $scope.resetForm = function() {
            $scope.nanoEntityForm = {};
        };

        $scope.closeAddComposingElement = function() {
            $scope.addNewComposingElement = false;
        };

        var prev = -1;

        $scope.openAddComposingElement = function(i) {
            
            if (prev>-1) {
                $scope.composingElements[prev].expand = false;    
            }

            prev = i;

        	$scope.composingElementForm = {};
        	$scope.composingElementForm.inherentFunction = [];
            $scope.addNewComposingElement = true;
        };

        $scope.getAddComposingElement = function() {
            return $scope.addNewComposingElement;
        };

        $scope.editComposingElement = function(composingElementId) {
            for (var k = 0; k < $scope.composingElements.length; ++k) {
                var element = $scope.composingElements[k];
                if (element.id == composingElementId ) {
                    $scope.composingElementForm.type = element.type;
                    $scope.composingElementForm.name = element.name;
                    $scope.composingElementForm.pubChemDataSourceName = element.pubChemDataSourceName;
                    $scope.composingElementForm.pubChemId = element.pubChemId;
                    $scope.composingElementForm.value = element.value;
                    $scope.composingElementForm.valueUnit = element.valueUnit;
                    $scope.composingElementForm.molecularFormulaType = element.molecularFormulaType;
                    $scope.composingElementForm.molecularFormula = element.molecularFormula;
                    $scope.composingElementForm.description = element.description;
                    $scope.composingElementForm.id = element.id;
                    $scope.composingElementForm.inherentFunction = element.inherentFunction;
                    $scope.composingElementForm.createdBy = element.createdBy;
                    $scope.composingElementForm.createdDate = element.createdDate;

                    if ( $scope.composingElementForm.inherentFunction != null && $scope.composingElementForm.inherentFunction.length > 0)
                        $scope.showInherentFunctionTable = true;
                    else {
                        $scope.showInherentFunctionTable = false;
                        $scope.composingElementForm.inherentFunction = [];
                    }

                    break;
                }
            }
        }

        $scope.removeComposingElement = function(composingElementId) {
            if (confirm("Are you sure you want to delete the Composing Element?")) {
                $scope.loader = true;

                for (var k = 0; k < $scope.composingElements.length; ++k) {
                    var element = $scope.composingElements[k];
                    if (element.id == $scope.composingElementForm.id) {
                        $scope.composingElements.splice(k,1);
                    }
                }
                $scope.nanoEntityForm.composingElements = $scope.composingElements;
                
                if( $scope.nanoEntityForm.simpleCompBean == null ) {
                	$scope.nanoEntityForm.simpleCompBean = {};
                }

                $scope.nanoEntityForm.simpleCompBean.id = $scope.composingElementForm.id;
                $scope.nanoEntityForm.simpleCompBean.type = $scope.composingElementForm.type;
                $scope.nanoEntityForm.simpleCompBean.name = $scope.composingElementForm.name;
                $scope.nanoEntityForm.simpleCompBean.pubChemDataSourceName = $scope.composingElementForm.pubChemDataSourceName;
                $scope.nanoEntityForm.simpleCompBean.pubChemId = $scope.composingElementForm.pubChemId;
                $scope.nanoEntityForm.simpleCompBean.value = $scope.composingElementForm.value;
                $scope.nanoEntityForm.simpleCompBean.valueUnit = $scope.composingElementForm.valueUnit;
                $scope.nanoEntityForm.simpleCompBean.molecularFormulaType = $scope.composingElementForm.molecularFormulaType;
                $scope.nanoEntityForm.simpleCompBean.molecularFormula = $scope.composingElementForm.molecularFormula;
                $scope.nanoEntityForm.simpleCompBean.description = $scope.composingElementForm.description;
                $scope.nanoEntityForm.simpleCompBean.inherentFunction = $scope.composingElementForm.inherentFunction;
                $scope.nanoEntityForm.simpleCompBean.createdBy = $scope.composingElementForm.createdBy;
                $scope.nanoEntityForm.simpleCompBean.createdDate = $scope.composingElementForm.createdDate;
                

                if( $scope.sampleId != null ) {
                	$scope.nanoEntityForm.sampleId = $scope.sampleId;
                }
                

                $http({method: 'POST', url: '/caNanoLab/rest/nanomaterialEntity/removeComposingElement',data: $scope.nanoEntityForm}).
                    success(function(data, status, headers, config) {
                    	$scope.nanoEntityForm = data;
                    	$scope.composingElements = $scope.nanoEntityForm.composingElements;
                        $scope.loader = false;
                        $scope.addNewComposingElement=false;
                        
                        $scope.showProperties();
                    }).
                    error(function(data, status, headers, config) {
                        // called asynchronously if an error occurs
                        // or server returns response with an error status.
                        // $rootScope.sampleData = data;
                        $scope.loader = false;
                        $scope.messages = data;
                    });
            }
        };

        $scope.saveComposingElement = function() {
            $scope.loader = true;

            if ( $scope.composingElementForm.id != null && $scope.composingElementForm.id != '' ) {
                for (var k = 0; k < $scope.composingElements.length; ++k) {
                    var element = $scope.composingElements[k];
                    if (element.id == $scope.composingElementForm.id) {
                        $scope.composingElements[k] = $scope.composingElementForm;
                    }
                }
            }
 /*           else {
                $scope.composingElements.push($scope.composingElementForm);
            }
*/

            $scope.nanoEntityForm.composingElements = $scope.composingElements;

            if( $scope.nanoEntityForm.simpleCompBean == null ) {
            	$scope.nanoEntityForm.simpleCompBean = {};
            }

            $scope.nanoEntityForm.simpleCompBean.id = $scope.composingElementForm.id;
            $scope.nanoEntityForm.simpleCompBean.type = $scope.composingElementForm.type;
            $scope.nanoEntityForm.simpleCompBean.name = $scope.composingElementForm.name;
            $scope.nanoEntityForm.simpleCompBean.pubChemDataSourceName = $scope.composingElementForm.pubChemDataSourceName;
            $scope.nanoEntityForm.simpleCompBean.pubChemId = $scope.composingElementForm.pubChemId;
            $scope.nanoEntityForm.simpleCompBean.value = $scope.composingElementForm.value;
            $scope.nanoEntityForm.simpleCompBean.valueUnit = $scope.composingElementForm.valueUnit;
            $scope.nanoEntityForm.simpleCompBean.molecularFormulaType = $scope.composingElementForm.molecularFormulaType;
            $scope.nanoEntityForm.simpleCompBean.molecularFormula = $scope.composingElementForm.molecularFormula;
            $scope.nanoEntityForm.simpleCompBean.description = $scope.composingElementForm.description;
            $scope.nanoEntityForm.simpleCompBean.inherentFunction = $scope.composingElementForm.inherentFunction;
            $scope.nanoEntityForm.simpleCompBean.createdBy = $scope.composingElementForm.createdBy;
            $scope.nanoEntityForm.simpleCompBean.createdDate = $scope.composingElementForm.createdDate;

            if( $scope.sampleId != null ) {
            	$scope.nanoEntityForm.sampleId = $scope.sampleId;
            }

            $scope.messages = [];
            $http({method: 'POST', url: '/caNanoLab/rest/nanomaterialEntity/saveComposingElement',data: $scope.nanoEntityForm}).
                success(function(data, status, headers, config) {
                	$scope.nanoEntityForm = data;
                	$scope.composingElements = $scope.nanoEntityForm.composingElements;
                    $scope.loader = false;
                    $scope.addNewComposingElement=false;
                    
                    $scope.showProperties();
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    // $rootScope.sampleData = data;
                    $scope.loader = false;
                    $scope.messages = data;
                });
        };


        $scope.checkFunctionType = function() {
            if( $scope.theInherentFunction.type == 'imaging function') {
                $scope.showModality = true;
            }
            else {
                $scope.showModality = false;
            }
        };

        /* Inherent Function Start */
        $scope.addInherentFunction = function() {
            var inherentFunction = {
                id : null,
                type : null,
                modality : null,
                description : null
            };
            var newInherentFunction = false
            inherentFunction.id = $scope.theInherentFunction.id;
            if (inherentFunction.id == null || inherentFunction.id.length == 0) {
                inherentFunction.id = -1000 - $scope.composingElementForm.inherentFunction.length;
                inherentFunction.id = String(inherentFunction.id);
                newInherentFunction = true;
            }
            inherentFunction.type = $scope.theInherentFunction.type;
            inherentFunction.modality = $scope.theInherentFunction.modality;
            inherentFunction.description = $scope.theInherentFunction.description;
            if (inherentFunction.type.length > 0 && inherentFunction.description.length > 0) {
                if (newInherentFunction) {
                    $scope.composingElementForm.inherentFunction.push(inherentFunction);
                }
                else {
                    var k;
                    for (k = 0; k < $scope.composingElementForm.inherentFunction.length; ++k)
                    {
                        var inherentFunctionL = $scope.composingElementForm.inherentFunction[k];
                        if (inherentFunction.id == inherentFunctionL.id ) {
                            $scope.composingElementForm.inherentFunction[k].type = inherentFunction.type;
                            $scope.composingElementForm.inherentFunction[k].modality = inherentFunction.modality;
                            $scope.composingElementForm.inherentFunction[k].description = inherentFunction.description;
                        }
                    }
                }
                $scope.addNewInherentFunction=false;
                $scope.showInherentFunctionTable = true;

                $scope.theInherentFunction.type = '';
                $scope.theInherentFunction.modality = '';
                $scope.theInherentFunction.description = '';
                $scope.theInherentFunction.id = '';
            } else {
                alert("Please fill in values");
            }

        }

        $scope.editInherentFunction = function(id) {
            var k;
            for (k = 0; k < $scope.composingElementForm.inherentFunction.length; ++k)
            {
                var inherentFunction = $scope.composingElementForm.inherentFunction[k];
                if (id == inherentFunction.id ) {
                    $scope.theInherentFunction.type = inherentFunction.type;
                    $scope.theInherentFunction.modality = inherentFunction.modality;
                    $scope.theInherentFunction.description = inherentFunction.description;
                    $scope.theInherentFunction.id = inherentFunction.id;
                    $scope.addNewInherentFunction=true;

                    console.log($scope.addNewInherentFunction);
                }
            }
        }

        $scope.deleteInherentFunction = function() {
            var k;
            for (k = 0; k < $scope.composingElementForm.inherentFunction.length; ++k)
            {
                var inherentFunction = $scope.composingElementForm.inherentFunction[k];
                if ($scope.theInherentFunction.id == inherentFunction.id ) {
                    $scope.composingElementForm.inherentFunction.splice(k,1);
                }
            }
            $scope.addNewInherentFunction=false;
            if( $scope.composingElementForm.inherentFunction.length > 0 ) {
                $scope.showInherentFunctionTable = true;
            }
            else {
                $scope.showInherentFunctionTable = false;
            }

            $scope.theInherentFunction.type = '';
            $scope.theInherentFunction.modality = '';
            $scope.theInherentFunction.description = '';
            $scope.theInherentFunction.id = '';
        }

        $scope.getAddNewInherentFunction = function() {
            return $scope.addNewInherentFunction;
        }

        $scope.closeAddNewInherentFunction = function() {
            $scope.addNewInherentFunction = false;
        }


        $scope.openAddNewInherentFunction = function() {
            $scope.addNewInherentFunction = true;
        }


        /* Inherent Function End */


        /* File Section */
        $scope.onFileSelect = function($files) {
            $scope.selectedFiles = [];
            $scope.selectedFiles = $files;
         
            
            if ($scope.selectedFiles != null && $scope.selectedFiles.length > 0 ) 
            	$scope.selectedFileName = $scope.selectedFiles[0].name;            
            
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
            for (var k = 0; k < $scope.files.length; ++k) {
                var element = $scope.files[k];
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
        };

        $scope.removeFile = function(fileId) {
            if (confirm("Are you sure you want to delete the File?")) {
                $scope.loader = true;

                for (var k = 0; k < $scope.files.length; ++k) {
                    var element = $scope.files[k];
                    if (element.id == $scope.fileForm.id) {
                        $scope.files.splice(k,1);
                    }
                }
                $scope.nanoEntityForm.files = $scope.files;
                
                if( $scope.nanoEntityForm.fileBean == null ) {
                	$scope.nanoEntityForm.fileBean = {};
                }

                $scope.nanoEntityForm.fileBean.externalUrl = $scope.fileForm.externalUrl;
                $scope.nanoEntityForm.fileBean.uri = $scope.fileForm.uri;
                $scope.nanoEntityForm.fileBean.uriExternal = $scope.fileForm.uriExternal;
                $scope.nanoEntityForm.fileBean.type = $scope.fileForm.type;
                $scope.nanoEntityForm.fileBean.title = $scope.fileForm.title;
                $scope.nanoEntityForm.fileBean.keywordsStr = $scope.fileForm.keywordsStr;
                $scope.nanoEntityForm.fileBean.description = $scope.fileForm.description;
                $scope.nanoEntityForm.fileBean.id = $scope.fileForm.id;
                $scope.nanoEntityForm.fileBean.theAccess = $scope.fileForm.theAccess;
                $scope.nanoEntityForm.fileBean.isPublic = $scope.fileForm.isPublic;
                $scope.nanoEntityForm.fileBean.createdBy = $scope.fileForm.createdBy;
                $scope.nanoEntityForm.fileBean.createdDate = $scope.fileForm.createdDate;                  

                if( $scope.sampleId != null ) {
                	$scope.nanoEntityForm.sampleId = $scope.sampleId;
                }
                

                $http({method: 'POST', url: '/caNanoLab/rest/nanomaterialEntity/removeFile',data: $scope.nanoEntityForm}).
                    success(function(data, status, headers, config) {
                    	$scope.nanoEntityForm = data;
                        $scope.files = $scope.nanoEntityForm.files;
                        $scope.addNewFile = false;
                        $scope.showProperties();
                    	$scope.loader = false;
                    }).
                    error(function(data, status, headers, config) {
                        // called asynchronously if an error occurs
                        // or server returns response with an error status.
                        // $rootScope.sampleData = data;
                        $scope.loader = false;
                        $scope.messages = data;
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
                    	$scope.messages = response.status + ': ' + response.data;
                    	$scope.loader = false;
                    }
                }, function(evt) {
                    // Math.min is to fix IE which reports 200% sometimes
                    // $scope.progress[index] = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
                });
                $scope.upload[index].xhr(function(xhr){
//				xhr.upload.addEventListener('abort', function() {console.log('abort complete')}, false);
                });
            }
            else {
                $scope.saveFileData();
            }
        };

        $scope.saveFileData = function() {
            $scope.loader = true;

            if ( $scope.fileForm.id != null && $scope.fileForm.id != '' ) {
                for (var k = 0; k < $scope.files.length; ++k) {
                    var element = $scope.files[k];
                    if (element.id == $scope.fileForm.id) {
                        $scope.files[k] = $scope.fileForm;
                    }
                }
            }
/*            else {
                $scope.files.push($scope.fileForm);
            }
*/
            $scope.nanoEntityForm.files = $scope.files;

            if( $scope.nanoEntityForm.fileBean == null ) {
            	$scope.nanoEntityForm.fileBean = {};
            }


            $scope.nanoEntityForm.fileBean.externalUrl = $scope.fileForm.externalUrl;
            if( $scope.selectedFileName != null && $scope.selectedFileName != '' ) {
            	$scope.nanoEntityForm.fileBean.uri = $scope.selectedFileName;
            } else {
            	$scope.nanoEntityForm.fileBean.uri = $scope.fileForm.uri;
            }            
            $scope.nanoEntityForm.fileBean.uriExternal = $scope.fileForm.uriExternal;
            $scope.nanoEntityForm.fileBean.type = $scope.fileForm.type;
            $scope.nanoEntityForm.fileBean.title = $scope.fileForm.title;
            $scope.nanoEntityForm.fileBean.keywordsStr = $scope.fileForm.keywordsStr;
            $scope.nanoEntityForm.fileBean.description = $scope.fileForm.description;
            $scope.nanoEntityForm.fileBean.id = $scope.fileForm.id;
            $scope.nanoEntityForm.fileBean.theAccess = $scope.fileForm.theAccess;
            $scope.nanoEntityForm.fileBean.isPublic = $scope.fileForm.isPublic;
            $scope.nanoEntityForm.fileBean.createdBy = $scope.fileForm.createdBy;
            $scope.nanoEntityForm.fileBean.createdDate = $scope.fileForm.createdDate;            

            if( $scope.sampleId != null ) {
            	$scope.nanoEntityForm.sampleId = $scope.sampleId;
            }

            $scope.messages = [];
            $http({method: 'POST', url: '/caNanoLab/rest/nanomaterialEntity/saveFile',data: $scope.nanoEntityForm}).
                success(function(data, status, headers, config) {
                	$scope.nanoEntityForm = data;
                    $scope.files = $scope.nanoEntityForm.files;
                    $scope.addNewFile = false;
                    
                    $scope.showProperties();
                    
                    $scope.loader = false;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    // $rootScope.sampleData = data;
                    $scope.loader = false;
                    $scope.messages = data;
                    $scope.addNewFile = true;
                });
        };

        $scope.getAddNewFile = function() {
            return $scope.addNewFile;
        }

        $scope.closeAddNewFile = function() {
            $scope.addNewFile = false;
        }


        $scope.openAddNewFile = function() {
            $scope.addNewFile = true;
            $scope.fileForm = {};
            
            $scope.fileForm.uriExternal = 'false';
            $scope.externalUrlEnabled = false;            
        }

        /* End File Section */

    });


