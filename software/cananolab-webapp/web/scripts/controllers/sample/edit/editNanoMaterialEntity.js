'use strict';
var app = angular.module('angularApp')

    .controller('EditNanoEntityCtrl', function (navigationService,groupService,$rootScope,$scope,$http,$location,$timeout,$routeParams,$upload) {
        $scope.nanoEntityForm = {};
        $rootScope.navTree=false;
        //$rootScope.tabs = navigationService.query();
        //$rootScope.groups = groupService.getGroups.data.get();

        $scope.nanoEntityId = '';
        $scope.localForm = {};
        $scope.localForm.otherCategoryText = '';
        $scope.sampleId = $routeParams.sampleId;
        $scope.nanoEntityId = $routeParams.nanoEntityId;
        $scope.sampleName = $routeParams.sampleName;
        //$scope.sampleId = 20917506;
        //$scope.nanoEntityId = 60260353;
        $scope.nanoEntityForm.otherSampleNames = [];


        /* File Variables */
        $scope.usingFlash = FileAPI && FileAPI.upload != null;
        $scope.fileReaderSupported = window.FileReader != null && (window.FileAPI == null || FileAPI.html5 != false);
        $scope.fileForm = {};
        $scope.fileForm.uriExternal = false;
        $scope.externalUrlEnabled = false;
        $scope.addNewFile = false;

        /** Composing Element Variables */
        $scope.composingElementForm = {};
        $scope.addNewInherentFunction = false;
        $scope.showInherentFunctionTable = false;
        $scope.composingElementForm.inherentFunctions = [];
        $scope.theInherentFunction = {};
        $scope.showModality = false;


        $scope.$on('$viewContentLoaded', function(){
            $http({method: 'GET', url: 'http://localhost:8080/caNanoLab/rest/nanomaterialEntity/setup?sampleId=' + $scope.sampleId}).
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
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $scope.message = data;
                });
        });

        $scope.loadNanoEntityData = function() {
            if( $scope.nanoEntityId != null ) {
                    $scope.loader = true;
                    $http({method: 'GET', url: 'http://localhost:8080/caNanoLab/rest/nanomaterialEntity/edit?sampleId=' + $scope.sampleId + '&dataId=' + $scope.nanoEntityId}).
                        success(function(data, status, headers, config) {
                            $scope.nanoEntityForm = data;
                            //$scope.nanoEntityForm = {"simpleCompBean":{"type":"coat","name":"coat","pubChemDataSourceName":"Compound","pubChemId":12,"value":null,"valueUnit":"%vol","molecularFormulaType":"Hill","molecularFormula":"","description":"Test","id":59637770,"functionId":"","functionType":"imaging function","imagingModality":"fluorescence","functionDescription":"Test img function","sampleId":"","modality":""},"fileBean":{"uriExternal":false,"uri":"","type":"movie","title":"movie","description":"","keywordsStr":"","id":null},"type":"dendrimer","id":0,"description":"Test Nano Entity","sampleId":"","userDeletable":false,"userUpdatable":false,"createdBy":"jonnalah","createdDate":1408630200000,"domainEntity":{"id":60260353,"createdBy":"jonnalah","fileCollection":[{"uriExternal":false,"uri":"","type":"movie","title":"movie","description":"","keywordsStr":"","id":null}],"composingElementCollection":[{"type":"coat","name":"coat","pubChemDataSourceName":"Compound","pubChemId":12,"value":null,"valueUnit":"%vol","molecularFormulaType":"Hill","molecularFormula":"","description":"Test","id":59637770,"functionId":"","functionType":"imaging function","imagingModality":"fluorescence","functionDescription":"Test img function","sampleId":"","modality":""}],"branch":null,"createdDate":"jonnalah","generation":null},"withProperties":true,"detailsPage":"/caNanoLab/views/sample/composition/nanomaterialEntity/PolymerInfoEdit.html","isPolymerized":null,"isCrossLinked":null};
                            $scope.composingElements = $scope.nanoEntityForm.domainEntity.composingElementCollection;
                            $scope.files = $scope.nanoEntityForm.files;
                            $scope.loader = false;
                        }).
                        error(function(data, status, headers, config) {
                            $scope.message = data;
                            $scope.loader = false;
                        }); 
            }
        }

        if( $scope.nanoEntityId != null ) {
            $scope.loadNanoEntityData();
        }
        else {
            $scope.addNewComposingElement=true;
        }

        $scope.doSubmitData = function() {
            $scope.loader = true;

            if (typeof $scope.protocolForm.fileId == 'undefined' || $scope.protocolForm.fileId == null) {
                $scope.protocolForm.fileId = '0';
            }

            $http({method: 'POST', url: '/caNanoLab/rest/protocol/submitProtocol',data: $scope.protocolForm}).
                success(function(data, status, headers, config) {
                    if (data == "success") {
                        $location.search('message', 'Protocol successfully saved with title "' + $scope.protocolForm.name + '"').path('/message').replace();
                    }
                    else if (data == "retract success") {
                        $location.search('message', 'Protocol successfully saved with title "' + $scope.protocolForm.name + '" and retracted from public access.').path('/message').replace();
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
            if (confirm("Delete the Protocol?")) {
                $scope.loader = true;

                $http({method: 'POST', url: '/caNanoLab/rest/protocol/deleteProtocol',data: $scope.protocolForm}).
                    success(function(data, status, headers, config) {
                        if (data == "success") {
                            $location.search('message','Protocol successfully removed with title "' + $scope.protocolForm.name + '"').path('/message').replace();
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

        $scope.showProperties = function() {
            if( $scope.nanoEntityForm.type == 'biopolymer') {
                $scope.nanoEntityForm.withProperties = true;
                $scope.nanoEntityForm.detailsPage = '/caNanoLab/views/sample/composition/nanomaterialEntity/BiopolymerInfoEdit.html';
            }else if( $scope.nanoEntityForm.type == 'polymer') {
                $scope.nanoEntityForm.withProperties = true;
                $scope.nanoEntityForm.detailsPage = '/caNanoLab/views/sample/composition/nanomaterialEntity/PolymerInfoEdit.html';
            }else if( $scope.nanoEntityForm.type == 'carbon nanotube') {
                $scope.nanoEntityForm.withProperties = true;
                $scope.nanoEntityForm.detailsPage = '/caNanoLab/views/sample/composition/nanomaterialEntity/CarbonNanotubeInfoEdit.html';
            }else if( $scope.nanoEntityForm.type == 'dendrimer') {
                $scope.nanoEntityForm.withProperties = true;
                $scope.nanoEntityForm.detailsPage = '/caNanoLab/views/sample/composition/nanomaterialEntity/DendrimerInfoEdit.html';
            }else if( $scope.nanoEntityForm.type == 'emulsion') {
                $scope.nanoEntityForm.withProperties = true;
                $scope.nanoEntityForm.detailsPage = '/caNanoLab/views/sample/composition/nanomaterialEntity/EmulsionInfoEdit.html';
            }else if( $scope.nanoEntityForm.type == 'fullerene') {
                $scope.nanoEntityForm.withProperties = true;
                $scope.nanoEntityForm.detailsPage = '/caNanoLab/views/sample/composition/nanomaterialEntity/FullereneInfoEdit.html';
            }else if( $scope.nanoEntityForm.type == 'liposome') {
                $scope.nanoEntityForm.withProperties = true;
                $scope.nanoEntityForm.detailsPage = '/caNanoLab/views/sample/composition/nanomaterialEntity/LiposomeInfoEdit.html';
            } else {
                $scope.nanoEntityForm.withProperties = false;
            }
        };

        $scope.closeAddComposingElement = function() {
            $scope.addNewComposingElement=false;
        };

        $scope.openAddComposingElement = function() {
            $scope.addNewComposingElement=true;
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

                    break;
                }
            }
        }

        $scope.removeComposingElement = function(composingElementId) {
            if (confirm("Are you sure you want to delete the Composing Element?")) {
                $scope.loader = true;

                $http({method: 'POST', url: '/caNanoLab/rest/nanomaterialEntity/removeComposingElement',data: $scope.composingElementForm}).
                    success(function(data, status, headers, config) {
                        //$location.search('message','Publication Association successfully removed with title "' + $scope.publicationForm.title + '"').path('/publication').replace();
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
                inherentFunction.id = -1000 - $scope.composingElementForm.inherentFunctions.length;
                newInherentFunction = true;
            }
            inherentFunction.type = $scope.theInherentFunction.type;
            inherentFunction.modality = $scope.theInherentFunction.modality;
            inherentFunction.description = $scope.theInherentFunction.description;
            if (inherentFunction.type.length > 0 && inherentFunction.description.length > 0) {
                if (newInherentFunction) {
                    $scope.composingElementForm.inherentFunctions.push(inherentFunction);
                }
                else {
                    var k;
                    for (k = 0; k < $scope.composingElementForm.inherentFunctions.length; ++k)
                    {
                        var inherentFunctionL = $scope.composingElementForm.inherentFunctions[k];
                        if (inherentFunction.id == inherentFunctionL.id ) {
                            $scope.composingElementForm.inherentFunctions[k].type = inherentFunction.type;
                            $scope.composingElementForm.inherentFunctions[k].modality = inherentFunction.modality;
                            $scope.composingElementForm.inherentFunctions[k].description = inherentFunction.description;
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
            for (k = 0; k < $scope.composingElementForm.inherentFunctions.length; ++k)
            {
                var inherentFunction = $scope.composingElementForm.inherentFunctions[k];
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
            for (k = 0; k < $scope.composingElementForm.inherentFunctions.length; ++k)
            {
                var inherentFunction = $scope.composingElementForm.inherentFunctions[k];
                if ($scope.theInherentFunction.id == inherentFunction.id ) {
                    $scope.composingElementForm.inherentFunctions.splice(k,1);
                }
            }
            $scope.addNewInherentFunction=false;
            if( $scope.composingElementForm.inherentFunctions.length > 0 ) {
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


        /* Inherent Function End */


        /* File Section */
        $scope.onFileSelect = function($files) {
            $scope.selectedFiles = [];
            $scope.selectedFiles = $files;
        };

        $scope.doSubmit = function() {
            if( $scope.protocolForm.isPublic && ! $scope.isCurator.curator) {
                if (confirm("The data has been assigned to Public.  Updating the data would retract it from Public.  You will need to resubmit the data to the curator for review before the curator reassigns it to Public.  Are you sure you want to continue?")) {
                    // continue
                } else {
                    return false;
                }
            }
            var index = 0;
            $scope.upload = [];
            if ($scope.selectedFiles != null && $scope.selectedFiles.length > 0 ) {
                $scope.upload[index] = $upload.upload({
                    url: '/caNanoLab/rest/core/uploadFile',
                    method: 'POST',
                    headers: {'my-header': 'my-header-value'},
                    data : $scope.protocolForm,
                    file: $scope.selectedFiles[index],
                    fileFormDataName: 'myFile'
                });
                $scope.upload[index].then(function(response) {
                    $timeout(function() {
                        //$scope.uploadResult.push(response.data);
                        //alert(response.data);
                        $scope.protocolForm.uri = response.data;
                        $scope.doSubmitData();
                    });
                }, function(response) {
                    if (response.status > 0) $scope.messages = response.status + ': ' + response.data;
                }, function(evt) {
                    // Math.min is to fix IE which reports 200% sometimes
                    // $scope.progress[index] = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
                });
                $scope.upload[index].xhr(function(xhr){
//				xhr.upload.addEventListener('abort', function() {console.log('abort complete')}, false);
                });
            }
            else {
                $scope.doSubmitData();
            }
        };
    });


