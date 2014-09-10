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
        //$scope.sampleId = 20917506;
        //$scope.sampleName='Test Sample';
        //$scope.funcEntityId = 60260353;
        $scope.otherSampleNames = [];


        /* File Variables */
        $scope.usingFlash = FileAPI && FileAPI.upload != null;
        $scope.fileReaderSupported = window.FileReader != null && (window.FileAPI == null || FileAPI.html5 != false);
        $scope.fileForm = {};
        $scope.fileForm.uriExternal = false;
        $scope.externalUrlEnabled = false;
        $scope.addNewFile = false;
        $scope.selectedFileName = '';

        /* Composing Element Variables */
        $scope.composingElementForm = {};
        $scope.addNewInherentFunction = false;
        $scope.showInherentFunctionTable = false;
        $scope.composingElementForm.inherentFunction = [];
        $scope.theInherentFunction = {};
        $scope.showModality = false;
        $scope.theInherentFunction.targets = [];
        $scope.theTargetFunction = {};
        $scope.showTargetFunction = false;
        $scope.showSpecies = false;
        $scope.showTargetFunctionTable = false;
        $scope.addNewTargetFunction = false;


        $scope.$on('$viewContentLoaded', function(){
            $scope.loader = true;

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
                    $scope.loader = false;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $scope.message = data;
                    $scope.loader = false;
                });
        });

        $scope.showProperties = function() {
            if( $scope.funcEntityForm.type == 'biopolymer') {
                $scope.withProperties = true;
                $scope.detailsPage = '/caNanoLab/views/sample/composition/functionalizingentity/BiopolymerInfoEdit.html';
            }else if( $scope.funcEntityForm.type == 'antibody') {
                $scope.withProperties = true;
                $scope.detailsPage = '/caNanoLab/views/sample/composition/functionalizingentity/AntibodyInfoEdit.html';
            }else if( $scope.funcEntityForm.type == 'small molecule') {
                $scope.withProperties = true;
                $scope.detailsPage = '/caNanoLab/views/sample/composition/functionalizingentity/SmallMoleculeInfoEdit.html';
            } else {
                $scope.withProperties = false;
            }
        };

        $scope.loadFuncEntityData = function() {
            if( $scope.funcEntityId != null ) {
                $scope.loader = true;

                $http({method: 'GET', url: '/caNanoLab/rest/functionalizingEntity/edit?sampleId=' + $scope.sampleId + '&dataId=' + $scope.funcEntityId}).
                    success(function(data, status, headers, config) {
                        $scope.funcEntityForm = data;
                        //$scope.funcEntityForm = {"fileBean":null,"simpleFunctionBean":{"type":"targeting function","modality":null,"description":"Test","id":"72712192","targetId":null,"targetType":null,"speciesType":"","targetName":null,"targetDescription":null,"createdBy":"","createdDate":null},"type":"small molecule","name":"Magnevist","pubChemDataSourceName":"","pubChemId":"","value":"","valueUnit":"","molecularFormulaType":"SMARTS","molecularFormula":"we","activationMethodType":"","activationEffect":"","description":"Test","sampleId":"","domainEntity":{},"errors":null,"functionList":[{"type":"imaging function","modality":null,"description":null,"id":"22031616","targetId":null,"targetType":null,"speciesType":"","targetName":null,"targetDescription":null,"createdBy":"","createdDate":null},{"type":"endosomolysis","modality":null,"description":"tesgt","id":"69730309","targetId":null,"targetType":null,"speciesType":"","targetName":null,"targetDescription":null,"createdBy":"","createdDate":null},{"type":"targeting function","modality":null,"description":"Test","id":"72712192","targetId":null,"targetType":null,"speciesType":"","targetName":null,"targetDescription":null,"createdBy":"","createdDate":null}],"fileList":[]};
                        $scope.composingElementForm.inherentFunction = $scope.funcEntityForm.functionList;
                        $scope.files = $scope.funcEntityForm.fileList;

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
            
            if( $scope.sampleId != null ) {
            	$scope.funcEntityForm.sampleId = $scope.sampleId;
            }

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

            if( $scope.theInherentFunction.type == 'targeting function') {
                $scope.showTargetFunction = true;
            }
            else {
                $scope.showTargetFunction = false;
            }
        };

        $scope.addInherentFunction = function() {
        	if ($scope.theInherentFunction.type != null && $scope.theInherentFunction.description != null) {
	            if ( $scope.theInherentFunction.id != null && $scope.theInherentFunction.id != '' ) {
	                for (var k = 0; k < $scope.composingElementForm.inherentFunction.length; ++k) {
	                    var inherentFunctionL = $scope.composingElementForm.inherentFunction[k];
	                    if ($scope.theInherentFunction.id == inherentFunctionL.id ) {
	                        $scope.composingElementForm.inherentFunction[k] = $scope.theInherentFunction;
	                    }
	                }
	            }
/*	            else {
	                $scope.composingElementForm.inherentFunction.push($scope.theInherentFunction);
	            }
*/	            
	            $scope.funcEntityForm.functionList = $scope.composingElementForm.inherentFunction;
	            
	            if( $scope.funcEntityForm.simpleFunctionBean == null ) {
	            	$scope.funcEntityForm.simpleFunctionBean = {};
	            }
	            
            	$scope.funcEntityForm.simpleFunctionBean.type = $scope.theInherentFunction.type;
            	$scope.funcEntityForm.simpleFunctionBean.modality = $scope.theInherentFunction.modality;
            	$scope.funcEntityForm.simpleFunctionBean.description = $scope.theInherentFunction.description;
            	$scope.funcEntityForm.simpleFunctionBean.id = $scope.theInherentFunction.id;
            	$scope.funcEntityForm.simpleFunctionBean.createdBy = $scope.theInherentFunction.createdBy;
            	$scope.funcEntityForm.simpleFunctionBean.createdDate = $scope.theInherentFunction.createdDate;
            	$scope.funcEntityForm.simpleFunctionBean.targets = $scope.theInherentFunction.targets;
            	
                if( $scope.sampleId != null ) {
                	$scope.funcEntityForm.sampleId = $scope.sampleId;
                }

                $scope.loader = true;
	            $http({method: 'POST', url: '/caNanoLab/rest/functionalizingEntity/saveFunction',data: $scope.funcEntityForm}).
                success(function(data, status, headers, config) {
                	$scope.funcEntityForm = data;
                    $scope.composingElementForm.inherentFunction = $scope.funcEntityForm.functionList;
                    $scope.loader = false;
                    $scope.addNewInherentFunction=false;
                    
                    $scope.showProperties();
                    
                    if( $scope.composingElementForm.inherentFunction != null && $scope.composingElementForm.inherentFunction.length > 0 ) {
                        $scope.showInherentFunctionTable = true;
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
        	else {
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
                    $scope.theInherentFunction.createdBy = inherentFunction.createdBy;
                    $scope.theInherentFunction.createdDate = inherentFunction.createdDate;
                    $scope.theInherentFunction.targets = inherentFunction.targets;
                    $scope.addNewInherentFunction=true;

                    if( $scope.theInherentFunction.targets != null && $scope.theInherentFunction.targets.length > 0 ) {
                        $scope.showTargetFunctionTable = true;
                    }
                    else {
                        $scope.showTargetFunctionTable = false;
                    }
                }
            }

            $scope.checkFunctionType();
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
            $scope.funcEntityForm.functionList = $scope.composingElementForm.inherentFunction;
            
            if( $scope.funcEntityForm.simpleFunctionBean == null ) {
            	$scope.funcEntityForm.simpleFunctionBean = {};
            }
            
        	$scope.funcEntityForm.simpleFunctionBean.type = $scope.theInherentFunction.type;
        	$scope.funcEntityForm.simpleFunctionBean.modality = $scope.theInherentFunction.modality;
        	$scope.funcEntityForm.simpleFunctionBean.description = $scope.theInherentFunction.description;
        	$scope.funcEntityForm.simpleFunctionBean.id = $scope.theInherentFunction.id;
        	$scope.funcEntityForm.simpleFunctionBean.createdBy = $scope.theInherentFunction.createdBy;
        	$scope.funcEntityForm.simpleFunctionBean.createdDate = $scope.theInherentFunction.createdDate;
        	$scope.funcEntityForm.simpleFunctionBean.targets = $scope.theInherentFunction.targets;
        	
            if( $scope.sampleId != null ) {
            	$scope.funcEntityForm.sampleId = $scope.sampleId;
            }
            
            
            $http({method: 'POST', url: '/caNanoLab/rest/functionalizingEntity/removeFunction',data: $scope.funcEntityForm}).
            success(function(data, status, headers, config) {
            	$scope.funcEntityForm = data;
            	$scope.composingElementForm.inherentFunction = $scope.funcEntityForm.functionList;
            	
            	$scope.addNewInherentFunction=false;
                $scope.theInherentFunction = {};
            	if( $scope.composingElementForm.inherentFunction.length > 0 ) {
                    $scope.showInherentFunctionTable = true;
                }
                else {
                    $scope.showInherentFunctionTable = false;
                }
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

        /* Target Function start */
        $scope.checkTargetFunctionType = function() {
            if( $scope.theTargetFunction.type == 'antigen') {
                $scope.showSpecies = true;
            }
            else {
                $scope.showSpecies = false;
            }
        };

        $scope.getAddNewTargetFunction = function() {
            return $scope.addNewTargetFunction;
        }

        $scope.closeAddNewTargetFunction = function() {
            $scope.addNewTargetFunction = false;
        }


        $scope.openAddNewTargetFunction = function() {
            $scope.addNewTargetFunction = true;
        }

        $scope.getShowTargetFunctionTable = function() {
            return $scope.showTargetFunctionTable;
        }

        $scope.addTargetFunction = function() {
            var targetFunction = {
                id : null,
                type : null,
                species : null,
                name : null,
                description : null
            };
            var newTargetFunction = false
            targetFunction.id = $scope.theTargetFunction.id;
            if (targetFunction.id == null || targetFunction.id.length == 0) {
                targetFunction.id = -1000 - $scope.theInherentFunction.targets.length;
                newTargetFunction = true;
            }
            targetFunction.type = $scope.theTargetFunction.type;
            targetFunction.species = $scope.theTargetFunction.species;
            targetFunction.name = $scope.theTargetFunction.name;
            targetFunction.description = $scope.theTargetFunction.description;
            if (targetFunction.type != null ) {
                if (newTargetFunction) {
                    $scope.theInherentFunction.targets.push(targetFunction);
                }
                else {
                    var k;
                    for (k = 0; k < $scope.theInherentFunction.targets.length; ++k)
                    {
                        var targetFunctionL = $scope.theInherentFunction.targets[k];
                        if (targetFunction.id == targetFunctionL.id ) {
                            $scope.theInherentFunction.targets[k].type = targetFunction.type;
                            $scope.theInherentFunction.targets[k].species = targetFunction.species;
                            $scope.theInherentFunction.targets[k].name = targetFunction.name;
                            $scope.theInherentFunction.targets[k].description = targetFunction.description;
                        }
                    }
                }
                $scope.addNewTargetFunction=false;
                $scope.showTargetFunctionTable = true;

                $scope.theTargetFunction.type = '';
                $scope.theTargetFunction.species = '';
                $scope.theTargetFunction.name = '';
                $scope.theTargetFunction.description = '';
                $scope.theTargetFunction.id = '';
            } else {
                alert("Please fill in values");
            }

        }

        $scope.editTargetFunction = function(id) {
            var k;
            for (k = 0; k < $scope.theInherentFunction.targets.length; ++k)
            {
                var targetFunction = $scope.theInherentFunction.targets[k];
                if (id == targetFunction.id ) {
                    $scope.theTargetFunction.type = targetFunction.type;
                    $scope.theTargetFunction.species = targetFunction.species;
                    $scope.theTargetFunction.name = targetFunction.name;
                    $scope.theTargetFunction.description = targetFunction.description;
                    $scope.theTargetFunction.id = targetFunction.id;
                    $scope.addNewTargetFunction=true;

                    console.log($scope.addNewTargetFunction);
                }
            }
            console.log($scope.addNewTargetFunction);
            if( $scope.theTargetFunction.type == 'antigen') {
                $scope.showSpecies = true;
            }
            else {
                $scope.showSpecies = false;
            }
        }

        $scope.deleteTargetFunction = function() {
            var k;
            for (k = 0; k < $scope.theInherentFunction.targets.length; ++k)
            {
                var targetFunction = $scope.theInherentFunction.targets[k];
                if ($scope.theTargetFunction.id == targetFunction.id ) {
                    $scope.theInherentFunction.targets.splice(k,1);
                }
            }
            $scope.addNewTargetFunction=false;
            if( $scope.theInherentFunction.targets.length > 0 ) {
                $scope.showTargetFunctionTable = true;
            }
            else {
                $scope.showTargetFunctionTable = false;
            }

            $scope.theTargetFunction.type = '';
            $scope.theTargetFunction.species = '';
            $scope.theTargetFunction.name = '';
            $scope.theTargetFunction.description = '';
            $scope.theTargetFunction.id = '';
        }

        /* Target Function End */

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
                    $scope.fileForm.createdBy = element.createdBy;
                    $scope.fileForm.createdDate = element.createdDate;

                    $scope.addNewFile = true;

                    break;
                }
            }
        }

        $scope.removeFile = function(fileId) {
            if (confirm("Are you sure you want to delete the File?")) {
                $scope.loader = true;
                
                for (var k = 0; k < $scope.files.length; ++k) {
                    var element = $scope.files[k];
                    if (element.id == $scope.fileForm.id) {
                        $scope.files.splice(k,1);
                    }
                }
                $scope.funcEntityForm.fileList = $scope.files;          
                
                if( $scope.funcEntityForm.fileBean == null ) {
                	$scope.funcEntityForm.fileBean = {};
                }
                
                $scope.funcEntityForm.fileBean.externalUrl = $scope.fileForm.externalUrl;
                $scope.funcEntityForm.fileBean.uri = $scope.fileForm.uri;
                $scope.funcEntityForm.fileBean.uriExternal = $scope.fileForm.uriExternal;
                $scope.funcEntityForm.fileBean.type = $scope.fileForm.type;
                $scope.funcEntityForm.fileBean.title = $scope.fileForm.title;
                $scope.funcEntityForm.fileBean.keywordsStr = $scope.fileForm.keywordsStr;
                $scope.funcEntityForm.fileBean.description = $scope.fileForm.description;
                $scope.funcEntityForm.fileBean.id = $scope.fileForm.id;
                $scope.funcEntityForm.fileBean.theAccess = $scope.fileForm.theAccess;
                $scope.funcEntityForm.fileBean.isPublic = $scope.fileForm.isPublic;
                $scope.funcEntityForm.fileBean.createdBy = $scope.fileForm.createdBy;
                $scope.funcEntityForm.fileBean.createdDate = $scope.fileForm.createdDate;                  
                
                if( $scope.sampleId != null ) {
                	$scope.funcEntityForm.sampleId = $scope.sampleId;
                }
                

                $http({method: 'POST', url: '/caNanoLab/rest/functionalizingEntity/removeFile',data: $scope.funcEntityForm}).
                    success(function(data, status, headers, config) {
                        $scope.funcEntityForm = data;
                        $scope.files = $scope.funcEntityForm.fileList;
                        $scope.addNewFile = false;
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
                    url: '/caNanoLab/rest/core/uploadFile',
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
/*            else {
                $scope.files.push($scope.fileForm);
            }
*/            

            $scope.funcEntityForm.fileList = $scope.files;
            
            if( $scope.funcEntityForm.fileBean == null ) {
            	$scope.funcEntityForm.fileBean = {};
            }
            
            
            $scope.funcEntityForm.fileBean.externalUrl = $scope.fileForm.externalUrl;
            $scope.funcEntityForm.fileBean.uri = $scope.selectedFileName;
            $scope.funcEntityForm.fileBean.uriExternal = $scope.fileForm.uriExternal;
            $scope.funcEntityForm.fileBean.type = $scope.fileForm.type;
            $scope.funcEntityForm.fileBean.title = $scope.fileForm.title;
            $scope.funcEntityForm.fileBean.keywordsStr = $scope.fileForm.keywordsStr;
            $scope.funcEntityForm.fileBean.description = $scope.fileForm.description;
            $scope.funcEntityForm.fileBean.id = $scope.fileForm.id;
            $scope.funcEntityForm.fileBean.theAccess = $scope.fileForm.theAccess;
            $scope.funcEntityForm.fileBean.isPublic = $scope.fileForm.isPublic;
            $scope.funcEntityForm.fileBean.createdBy = $scope.fileForm.createdBy;
            $scope.funcEntityForm.fileBean.createdDate = $scope.fileForm.createdDate;              
            
            
            if( $scope.sampleId != null ) {
            	$scope.funcEntityForm.sampleId = $scope.sampleId;
            }

            $http({method: 'POST', url: '/caNanoLab/rest/functionalizingEntity/saveFile',data: $scope.funcEntityForm}).
                success(function(data, status, headers, config) {
                    $scope.funcEntityForm = data;
                    $scope.files = $scope.funcEntityForm.fileList;
                    $scope.addNewFile = false;
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


