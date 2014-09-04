'use strict';
var app = angular.module('angularApp')

    .controller('EditFuncEntityCtrl', function (navigationService,groupService,$rootScope,$scope,$http,$location,$timeout,$routeParams,$upload) {
        $scope.funcEntityForm = {};
        //$rootScope.tabs = navigationService.query();
        //$rootScope.groups = groupService.getGroups.data.get();

        $scope.funcEntityId = '';
        $scope.localForm = {};
        $scope.localForm.otherCategoryText = '';
        $scope.sampleId = $routeParams.sampleId;
        $scope.funcEntityId = $routeParams.funcEntityId;
        $scope.sampleName = $routeParams.sampleName;
        $scope.sampleId = 20917506;
        $scope.sampleName='Test Sample';
        //$scope.funcEntityId = 60260353;
        $scope.funcEntityForm.otherSampleNames = [];


        /* File Variables */
        $scope.usingFlash = FileAPI && FileAPI.upload != null;
        $scope.fileReaderSupported = window.FileReader != null && (window.FileAPI == null || FileAPI.html5 != false);
        $scope.fileForm = {};
        $scope.fileForm.uriExternal = false;
        $scope.externalUrlEnabled = false;
        $scope.addNewFile = false;

        /* Composing Element Variables */
        $scope.composingElementForm = {};
        $scope.addNewInherentFunction = false;
        $scope.showInherentFunctionTable = false;
        $scope.composingElementForm.inherentFunction = [];
        $scope.theInherentFunction = {};
        $scope.showModality = false;


        $scope.$on('$viewContentLoaded', function(){
            $http({method: 'GET', url: '/caNanoLab/rest/functionalizingEntity/setup?sampleId=' + $scope.sampleId}).
                success(function(data, status, headers, config) {
                    $scope.data = data;
                    //$scope.data = {"pubChemDataSources":["Compound","Substance","BioAssay"],"activationMethods":["does not require activation","enzymatic cleavage","infrared","MRI","NMR","pH","ultrasound","ultraviolet"],"modalityTypes":["beta radiation","bioluminescence","fluorescence","gamma ray","infrared","MRI","neutron scattering","NMR","PET","photoluminescence","Raman spectroscopy","SPECT","ultrasound","X-ray"],"antibodyIsotypes":["IgA","IgD","IgE","IgG","IgM"],"otherSampleNames":["Demo123","NCL-20-1","NCL-21-1","NCL-24-1","NCL-24-1-Copy","NCL-25-1","NCL-26-1","QA"],"functionTypes":["endosomolysis","imaging function","other","targeting function","therapeutic function","transfection"],"functionalizingEntityTypes":["Magnetic Particle","Monomer","Polymer","Quantum Dot","antibody","biopolymer","radioisotope","small molecule"],"fileTypes":["document","graph","image","movie","spread sheet"],"biopolymerTypes":["DNA","peptide","protein","RNA","siRNA"],"targetTypes":["antigen","gene","other","receptor"],"antibodyTypes":["Fab","ScFv","whole"],"molecularFormulaTypes":["Hill","SMARTS","SMILES"],"functionalizingEntityUnits":["%","%mole","%vol","%wt/vol","%wt/wt","g","Gy","M","mCi","mg","mg/mL","mL","mM","mmol","nM","nmol","pmol","uCi/mg","ug","ug/uL","uL","uL/mL","uM","wt%"],"speciesTypes":["cat","cattle","dog","goat","guinea pig","hamster","horse","human","mouse","pig","rat","sheep","yeast","zebrafish"]};
                    $scope.funcEntityTypes = $scope.data.functionalizingEntityTypes;
                    $scope.activationMethods = $scope.data.activationMethods;
                    $scope.pubChemDataSources = $scope.data.pubChemDataSources;
                    $scope.antibodyIsotypes = $scope.data.antibodyIsotypes;
                    $scope.molecularFormulaTypes = $scope.data.molecularFormulaTypes;
                    $scope.functionTypes = $scope.data.functionTypes;
                    $scope.functionalizingEntityUnits = $scope.data.functionalizingEntityUnits;
                    $scope.modalityTypes = $scope.data.modalityTypes;
                    $scope.otherSampleNames = $scope.data.otherSampleNames;
                    $scope.fileTypes = $scope.data.fileTypes;
                    $scope.biopolymerTypes = $scope.data.biopolymerTypes;
                    $scope.targetTypes = $scope.data.targetTypes;
                    $scope.antibodyTypes = $scope.data.antibodyTypes;
                    $scope.speciesTypes = $scope.data.speciesTypes;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $scope.message = data;
                });
        });

        $scope.showProperties = function() {
            if( $scope.funcEntityForm.type == 'biopolymer') {
                $scope.funcEntityForm.withProperties = true;
                $scope.detailsPage = '/caNanoLab/views/sample/composition/functionalizingentity/BiopolymerInfoEdit.html';
            }else if( $scope.funcEntityForm.type == 'antibody') {
                $scope.funcEntityForm.withProperties = true;
                $scope.detailsPage = '/caNanoLab/views/sample/composition/functionalizingentity/AntibodyInfoEdit.html';
            }else if( $scope.funcEntityForm.type == 'small molecule') {
                $scope.funcEntityForm.withProperties = true;
                $scope.detailsPage = '/caNanoLab/views/sample/composition/functionalizingentity/SmallMoleculeInfoEdit.html';
            } else {
                $scope.funcEntityForm.withProperties = false;
            }
        };

        $scope.loadFuncEntityData = function() {
            if( $scope.funcEntityId != null ) {
                $scope.loader = true;
                $http({method: 'GET', url: '/caNanoLab/rest/functionalizingEntity/edit?sampleId=' + $scope.sampleId + '&dataId=' + $scope.funcEntityId}).
                    success(function(data, status, headers, config) {
                        $scope.funcEntityForm = data;
                        //$scope.funcEntityForm = {"fileBean":null,"simpleFunctionBean":{"type":null,"modality":null,"description":null,"id":null,"targetId":null,"targetType":null,"speciesType":"","targetName":null,"targetDescription":null},"type":"small molecule","name":"Magnevist","pubChemDataSourceName":"","pubChemId":"","value":"","valueUnit":"","molecularFormulaType":"","molecularFormula":"","activationMethodType":"","activationEffect":"","description":"","domainEntity":{"functionCollection":[{"type":null,"modality":null,"description":null,"id":null,"targetId":null,"targetType":null,"speciesType":"","targetName":null,"targetDescription":null},{"type":null,"modality":null,"description":null,"id":null,"targetId":null,"targetType":null,"speciesType":"","targetName":null,"targetDescription":null}],"fileCollection":[]},"errors":null};
                        $scope.composingElementForm.inherentFunction = $scope.funcEntityForm.domainEntity.functionCollection;
                        $scope.files = $scope.funcEntityForm.files;

                        $scope.showProperties();
                        
                        if( $scope.composingElementForm.inherentFunction != null && $scope.composingElementForm.inherentFunction.length > 0 ) {
                        	$scope.showInherentFunctionTable = true;
                        }

                        $scope.loader = false;
                    }).
                    error(function(data, status, headers, config) {
                        $scope.message = data;
                        $scope.loader = false;
                    });
            }
        }

        if( $scope.funcEntityId != null ) {
            $scope.loadFuncEntityData();
        }
        else {
            $scope.addNewInherentFunction=true;
        }

        $scope.doSubmit = function() {
            $scope.loader = true;

            $http({method: 'POST', url: '/caNanoLab/rest/functionalizingEntity/submit',data: $scope.funcEntityForm}).
                success(function(data, status, headers, config) {
                    if (data == "success") {
                        //$location.search('message', 'Nanomaterial Entity successfully saved "').path('/message').replace();
                        $location.search('message', 'FunctionalizingEntity Entity successfully saved."').path('/editComposition').replace();
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
            if (confirm("Are you sure you want to delete the FunctionalizingEntity Entity?")) {
                $scope.loader = true;

                $http({method: 'POST', url: '/caNanoLab/rest/functionalizingEntity/delete',data: $scope.funcEntityForm}).
                    success(function(data, status, headers, config) {
                        if (data == "success") {
                            $location.search('message', 'FunctionalizingEntity Entity successfully deleted."').path('/editComposition').replace();
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
            $scope.funcEntityForm = {};
        };

        /* Inherent Function Start */

        $scope.checkFunctionType = function() {
            if( $scope.theInherentFunction.type == 'imaging function') {
                $scope.showModality = true;
            }
            else {
                $scope.showModality = false;
            }
        };

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
        
        $scope.getShowInherentFunctionTable = function() {
            return $scope.showInherentFunctionTable;
        }        


        /* Inherent Function End */


        /* File Section */
        $scope.onFileSelect = function($files) {
            $scope.selectedFiles = [];
            $scope.selectedFiles = $files;
        };

        $scope.editFile = function(fileId) {
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

                    break;
                }
            }
        }

        $scope.removeFile = function(fileId) {
            if (confirm("Are you sure you want to delete the File?")) {
                $scope.loader = true;

                $http({method: 'POST', url: '/caNanoLab/rest/nanomaterialEntity/removeFile',data: $scope.fileForm}).
                    success(function(data, status, headers, config) {
                        $scope.funcEntityForm = data;
                        $scope.files = $scope.funcEntityForm.domainEntity.fileCollection;
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
                        //$scope.funcEntityForm = response.data;
                        $scope.saveFileData();
                        $scope.loader = false;
                    });
                }, function(response) {
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
            else {
                $scope.files.push($scope.fileForm);
            }

            $scope.funcEntityForm.domainEntity.fileCollection = $scope.files;

            $http({method: 'POST', url: '/caNanoLab/rest/nanomaterialEntity/saveFile',data: $scope.funcEntityForm}).
                success(function(data, status, headers, config) {
                    $scope.funcEntityForm = data;
                    $scope.files = $scope.funcEntityForm.domainEntity.fileCollection;
                    $scope.loader = false;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    // $rootScope.sampleData = data;
                    $scope.loader = false;
                    $scope.messages = data;
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
        }

        /* End File Section */

    });


