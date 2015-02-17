'use strict';
var app = angular.module('angularApp')

    .controller('EditCompositionFileCtrl', function (navigationService,groupService,$rootScope,$scope,$http,$location,$timeout,$routeParams,$upload) {
        $scope.fileForm = {};
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
        $scope.selectedFileName = '';
         
        var uploadUrl = '/caNanoLab/rest/core/uploadFile';
        $scope.ie9 = false;
        if(navigator.appVersion.indexOf("MSIE 9.")!=-1){
            uploadUrl = '/caNanoLab/uploadFile';
            $scope.ie9 = true;
        }

        $scope.$on('$viewContentLoaded', function(){
            $http({method: 'GET', url: '/caNanoLab/rest/compositionFile/setup?sampleId=' + $scope.sampleId}).
                success(function(data, status, headers, config) {
                    $scope.data = data;
                    //$scope.data = {"pubChemDataSources":["Compound","Substance","BioAssay"],"activationMethods":["does not require activation","enzymatic cleavage","infrared","MRI","NMR","pH","ultrasound","ultraviolet"],"modalityTypes":["beta radiation","bioluminescence","fluorescence","gamma ray","infrared","MRI","neutron scattering","NMR","PET","photoluminescence","Raman spectroscopy","SPECT","ultrasound","X-ray"],"antibodyIsotypes":["IgA","IgD","IgE","IgG","IgM"],"otherSampleNames":["Demo123","NCL-20-1","NCL-21-1","NCL-24-1","NCL-24-1-Copy","NCL-25-1","NCL-26-1","QA"],"functionTypes":["endosomolysis","imaging function","other","targeting function","therapeutic function","transfection"],"functionalizingEntityTypes":["Magnetic Particle","Monomer","Polymer","Quantum Dot","antibody","biopolymer","radioisotope","small molecule"],"fileTypes":["document","graph","image","movie","spread sheet"],"biopolymerTypes":["DNA","peptide","protein","RNA","siRNA"],"targetTypes":["antigen","gene","other","receptor"],"antibodyTypes":["Fab","ScFv","whole"],"molecularFormulaTypes":["Hill","SMARTS","SMILES"],"functionalizingEntityUnits":["%","%mole","%vol","%wt/vol","%wt/wt","g","Gy","M","mCi","mg","mg/mL","mL","mM","mmol","nM","nmol","pmol","uCi/mg","ug","ug/uL","uL","uL/mL","uM","wt%"],"speciesTypes":["cat","cattle","dog","goat","guinea pig","hamster","horse","human","mouse","pig","rat","sheep","yeast","zebrafish"]};
                    $scope.fileTypes = $scope.data.fileTypes;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $scope.messages = data;
                });
        });

        $scope.loadCompositionFileData = function() {
            if( $scope.compositionFileId != null ) {
                $scope.loader = true;
                $http({method: 'GET', url: '/caNanoLab/rest/compositionFile/edit?sampleId=' + $scope.sampleId + '&dataId=' + $scope.compositionFileId}).
                    success(function(data, status, headers, config) {
                        $scope.fileForm = data;
                        //$scope.fileForm = {"uriExternal":true,"uri":"http://www.cancer.gov/","type":"graph","title":"graph","description":"","keywordsStr":"","id":73433125,"createdBy":"jonnalah","createdDate":1410117656000,"sampleId":"57835522","errors":null,"externalUrl":"http://www.cancer.gov/"};
                        
		                if( $scope.fileForm.externalUrl != null && $scope.fileForm.externalUrl != '') {
		                    $scope.externalUrlEnabled = true;
		                    $scope.fileForm.uriExternal = 'true';
/*
		                    $timeout(function() {
		                        var el = document.getElementById('external1');
		                        angular.element(el).triggerHandler('click');
		                    }, 0);  */
		                }
		                else {
		                    $scope.externalUrlEnabled  = false;
		                    $scope.fileForm.uriExternal = 'false';
/*
		                    $timeout(function() {
		                        var el = document.getElementById('external0');
		                        angular.element(el).triggerHandler('click');
		                    }, 0);  */
		                }
		                
                        $scope.loader = false;
                    }).
                    error(function(data, status, headers, config) {
                        $scope.messages = data;
                        $scope.loader = false;
                    });

            }
        }

        if( $scope.compositionFileId != null ) {
            $scope.loadCompositionFileData();
        }
        else {
            $scope.$on('$viewContentLoaded', function() {
                $scope.fileForm.uriExternal = 'false';
                $scope.externalUrlEnabled = false;
            });
        }  

        $scope.resetForm = function() {
            $scope.fileForm = {};
        };

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
            if (confirm("Are you sure you want to delete the Composition File?")) {
                $scope.loader = true;
                
                if( $scope.sampleId != null ) {
                	$scope.fileForm.sampleId = $scope.sampleId;
                }                

                $http({method: 'POST', url: '/caNanoLab/rest/compositionFile/delete',data: $scope.fileForm}).
                    success(function(data, status, headers, config) {
                        $location.search('message', 'Composition File successfully deleted.').path('/editComposition').replace();
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
                        //$scope.CompositionFileForm = response.data;
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
            
            if( $scope.sampleId != null ) {
            	$scope.fileForm.sampleId = $scope.sampleId;
            }      
            
            if( $scope.selectedFileName != null && $scope.selectedFileName != '' ) {
            	$scope.fileForm.uri = $scope.selectedFileName;
            }
            
            $http({method: 'POST', url: '/caNanoLab/rest/compositionFile/submit',data: $scope.fileForm}).
                success(function(data, status, headers, config) {
                	if (data == "success") {
                		$location.search('message', 'Composition File successfully saved.').path('/editComposition').replace();
                	}
                	else {
                        $scope.messages = data;
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
        };

        /* End File Section */

    });


