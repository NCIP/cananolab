'use strict';
var app = angular.module('angularApp')

    .controller('EditCompositionFileCtrl', function (navigationService,groupService,$rootScope,$scope,$http,$location,$timeout,$routeParams,$upload) {
        $scope.CompositionFileForm = {};
        $rootScope.navTree=false;
        //$rootScope.tabs = navigationService.query();
        //$rootScope.groups = groupService.getGroups.data.get();

        $scope.compositionFileId = '';
        $scope.localForm = {};
        $scope.localForm.otherCategoryText = '';
        $scope.sampleId = $routeParams.sampleId;
        $scope.compositionFileId = $routeParams.compositionFileId;
        $scope.sampleName = $routeParams.sampleName;
        //$scope.sampleId = 20917506;
        //$scope.sampleName='Test Sample';
        //$scope.compositionFileId = 60260353;


        /* File Variables */
        $scope.usingFlash = FileAPI && FileAPI.upload != null;
        $scope.fileReaderSupported = window.FileReader != null && (window.FileAPI == null || FileAPI.html5 != false);
        $scope.fileForm = {};
        $scope.fileForm.uriExternal = false;
        $scope.externalUrlEnabled = false;
        $scope.addNewFile = false;

        $scope.$on('$viewContentLoaded', function(){
            $http({method: 'GET', url: 'http://localhost:8080/caNanoLab/rest/nanomaterialEntity/setup?sampleId=' + $scope.sampleId}).
                success(function(data, status, headers, config) {
                    $scope.data = data;
                    //$scope.data = {"pubChemDataSources":["Compound","Substance","BioAssay"],"activationMethods":["does not require activation","enzymatic cleavage","infrared","MRI","NMR","pH","ultrasound","ultraviolet"],"modalityTypes":["beta radiation","bioluminescence","fluorescence","gamma ray","infrared","MRI","neutron scattering","NMR","PET","photoluminescence","Raman spectroscopy","SPECT","ultrasound","X-ray"],"antibodyIsotypes":["IgA","IgD","IgE","IgG","IgM"],"otherSampleNames":["Demo123","NCL-20-1","NCL-21-1","NCL-24-1","NCL-24-1-Copy","NCL-25-1","NCL-26-1","QA"],"functionTypes":["endosomolysis","imaging function","other","targeting function","therapeutic function","transfection"],"functionalizingEntityTypes":["Magnetic Particle","Monomer","Polymer","Quantum Dot","antibody","biopolymer","radioisotope","small molecule"],"fileTypes":["document","graph","image","movie","spread sheet"],"biopolymerTypes":["DNA","peptide","protein","RNA","siRNA"],"targetTypes":["antigen","gene","other","receptor"],"antibodyTypes":["Fab","ScFv","whole"],"molecularFormulaTypes":["Hill","SMARTS","SMILES"],"functionalizingEntityUnits":["%","%mole","%vol","%wt/vol","%wt/wt","g","Gy","M","mCi","mg","mg/mL","mL","mM","mmol","nM","nmol","pmol","uCi/mg","ug","ug/uL","uL","uL/mL","uM","wt%"],"speciesTypes":["cat","cattle","dog","goat","guinea pig","hamster","horse","human","mouse","pig","rat","sheep","yeast","zebrafish"]};
                    $scope.fileTypes = $scope.data.fileTypes;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $scope.message = data;
                });
        });

        $scope.loadCompositionFileData = function() {
            if( $scope.funcEntityId != null ) {
                $scope.loader = true;
                $http({method: 'GET', url: 'http://localhost:8080/caNanoLab/rest/compositionFile/edit?sampleId=' + $scope.sampleId + '&dataId=' + $scope.funcEntityId}).
                    success(function(data, status, headers, config) {
                        $scope.CompositionFileForm = data;
                        //$scope.CompositionFileForm = {"simpleCompBean":{"type":"coat","name":"coat","pubChemDataSourceName":"Compound","pubChemId":12,"value":null,"valueUnit":"%vol","molecularFormulaType":"Hill","molecularFormula":"","description":"Test","id":59637770,"functionId":"","functionType":"imaging function","imagingModality":"fluorescence","functionDescription":"Test img function","sampleId":"","modality":""},"fileBean":{"uriExternal":false,"uri":"","type":"movie","title":"movie","description":"","keywordsStr":"","id":null},"type":"dendrimer","id":0,"description":"Test Nano Entity","sampleId":"","userDeletable":false,"userUpdatable":false,"createdBy":"jonnalah","createdDate":1408630200000,"domainEntity":{"id":60260353,"createdBy":"jonnalah","fileCollection":[{"uriExternal":false,"uri":"","type":"movie","title":"movie","description":"","keywordsStr":"","id":null}],"composingElementCollection":[{"type":"coat","name":"coat","pubChemDataSourceName":"Compound","pubChemId":12,"value":null,"valueUnit":"%vol","molecularFormulaType":"Hill","molecularFormula":"","description":"Test","id":59637770,"functionId":"","functionType":"imaging function","imagingModality":"fluorescence","functionDescription":"Test img function","sampleId":"","modality":""}],"branch":null,"createdDate":"jonnalah","generation":null},"withProperties":true,"detailsPage":"/caNanoLab/views/sample/composition/nanomaterialEntity/PolymerInfoEdit.html","isPolymerized":null,"isCrossLinked":null};
                        $scope.files = [];
                        $scope.files.push($scope.CompositionFileForm.domainEntity.file);

                        $scope.editFile($scope.file.id);

                        $scope.loader = false;
                    }).
                    error(function(data, status, headers, config) {
                        $scope.message = data;
                        $scope.loader = false;
                    });
            }
        }

        if( $scope.compositionFileId != null ) {
            $scope.loadCompositionFileData();
        }

        $scope.resetForm = function() {
            $scope.fileForm = {};
        };

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
            if (confirm("Are you sure you want to delete the Composition File?")) {
                $scope.loader = true;

                $http({method: 'POST', url: '/caNanoLab/rest/compositionFile/removeFile',data: $scope.fileForm}).
                    success(function(data, status, headers, config) {
                        $scope.CompositionFileForm = data;
                        $scope.files = $scope.CompositionFileForm.domainEntity.fileCollection;
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
                        //$scope.CompositionFileForm = response.data;
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

            $scope.CompositionFileForm.domainEntity.fileCollection = $scope.files;

            $http({method: 'POST', url: '/caNanoLab/rest/compositionFile/saveFile',data: $scope.CompositionFileForm}).
                success(function(data, status, headers, config) {
                    $scope.CompositionFileForm = data;
                    $scope.files = $scope.CompositionFileForm.domainEntity.fileCollection;
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


