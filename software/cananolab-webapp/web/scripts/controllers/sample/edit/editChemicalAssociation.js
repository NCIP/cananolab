'use strict';
var app = angular.module('angularApp')

    .controller('EditChemAssociationCtrl', function (navigationService,groupService,$rootScope,$scope,$http,$location,$timeout,$routeParams,$upload) {
        $scope.chemAssociationForm = {};
        $rootScope.navTree=false;
        //$rootScope.tabs = navigationService.query();
        //$rootScope.groups = groupService.getGroups.data.get();

        $scope.chemAssociationId = '';
        $scope.localForm = {};
        $scope.localForm.otherCategoryText = '';
        $scope.sampleId = $routeParams.sampleId;
        $scope.chemAssociationId = $routeParams.chemAssociationId;
        $scope.sampleName = $routeParams.sampleName;
        //$scope.sampleId = 20917506;
        //$scope.sampleName='Test Sample';
        //$scope.chemAssociationId = 60260353;


        /* File Variables */
        $scope.usingFlash = FileAPI && FileAPI.upload != null;
        $scope.fileReaderSupported = window.FileReader != null && (window.FileAPI == null || FileAPI.html5 != false);
        $scope.fileForm = {};
        $scope.fileForm.uriExternal = false;
        $scope.externalUrlEnabled = false;
        $scope.addNewFile = false;
        $scope.selectedFileName = '';
        

        $scope.$on('$viewContentLoaded', function(){
            $scope.loader = true;
            $http({method: 'GET', url: '/caNanoLab/rest/chemicalAssociation/setup?sampleId=' + $scope.sampleId}).
                success(function(data, status, headers, config) {
                    $scope.data = data;
                    //$scope.data = {"pubChemDataSources":["Compound","Substance","BioAssay"],"activationMethods":["does not require activation","enzymatic cleavage","infrared","MRI","NMR","pH","ultrasound","ultraviolet"],"modalityTypes":["beta radiation","bioluminescence","fluorescence","gamma ray","infrared","MRI","neutron scattering","NMR","PET","photoluminescence","Raman spectroscopy","SPECT","ultrasound","X-ray"],"antibodyIsotypes":["IgA","IgD","IgE","IgG","IgM"],"otherSampleNames":["Demo123","NCL-20-1","NCL-21-1","NCL-24-1","NCL-24-1-Copy","NCL-25-1","NCL-26-1","QA"],"functionTypes":["endosomolysis","imaging function","other","targeting function","therapeutic function","transfection"],"functionalizingEntityTypes":["Magnetic Particle","Monomer","Polymer","Quantum Dot","antibody","biopolymer","radioisotope","small molecule"],"fileTypes":["document","graph","image","movie","spread sheet"],"biopolymerTypes":["DNA","peptide","protein","RNA","siRNA"],"targetTypes":["antigen","gene","other","receptor"],"antibodyTypes":["Fab","ScFv","whole"],"molecularFormulaTypes":["Hill","SMARTS","SMILES"],"functionalizingEntityUnits":["%","%mole","%vol","%wt/vol","%wt/wt","g","Gy","M","mCi","mg","mg/mL","mL","mM","mmol","nM","nmol","pmol","uCi/mg","ug","ug/uL","uL","uL/mL","uM","wt%"],"speciesTypes":["cat","cattle","dog","goat","guinea pig","hamster","horse","human","mouse","pig","rat","sheep","yeast","zebrafish"]};
                    $scope.bondTypes = $scope.data.bondTypes;
                    $scope.fileTypes = $scope.data.fileTypes;
                    $scope.chemicalAssociationTypes = $scope.data.chemicalAssociationTypes;
                    $scope.associationCompositionTypes = $scope.data.associationCompositionTypes;
                    $scope.loader = false;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $scope.messages = data;
                    $scope.loader = false;
                });
        });

        $scope.loadChemAssociationData = function() {
            if( $scope.chemAssociationId != null ) {
                $scope.loader = true;
                
                 $http({method: 'GET', url: '/caNanoLab/rest/chemicalAssociation/edit?sampleId=' + $scope.sampleId + '&dataId=' + $scope.chemAssociationId}).
                 success(function(data, status, headers, config) {
	                 $scope.chemAssociationForm = data;
	                 //$scope.chemAssociationForm = {"simpleFile":null,"type":"attachment","bondType":"covalent","description":"Test","associatedElementA":{"compositionType":"nanomaterial entity","entityId":"72056840","entityDisplayName":"fullerene","className":"ComposingElement","composingElement":{"type":"repeat unit","name":"test","pubChemDataSourceName":"","pubChemId":null,"value":null,"valueUnit":null,"molecularFormulaType":null,"molecularFormula":null,"description":"","id":72089608,"sampleId":"","modality":"","createdBy":"prakasht","createdDate":null,"inherentFunction":null}},"associatedElementB":{"compositionType":"functionalizing entity","entityId":"22719746","entityDisplayName":"small molecule","className":"SmallMolecule","composingElement":{"type":null,"name":null,"pubChemDataSourceName":null,"pubChemId":null,"value":null,"valueUnit":null,"molecularFormulaType":null,"molecularFormula":null,"description":null,"id":null,"sampleId":"","modality":"","createdBy":null,"createdDate":null,"inherentFunction":null}},"errors":null};
	                 $scope.files = $scope.chemAssociationForm.files;
	                 
	                 if( $scope.files == null ) {
	                	 $scope.files = [];
	                 }
	                 
	                 $scope.loadAssociatedElements('A', $scope.chemAssociationForm.associatedElementA.compositionType);
	                 $scope.loadComposingElements('A', $scope.chemAssociationForm.associatedElementA.compositionType, $scope.chemAssociationForm.associatedElementA.entityId);
	                 
	                 $scope.loadAssociatedElements('B', $scope.chemAssociationForm.associatedElementB.compositionType);
	                 $scope.loadComposingElements('B', $scope.chemAssociationForm.associatedElementB.compositionType, $scope.chemAssociationForm.associatedElementB.entityId);
	                 
	
	                 $scope.loader = false;
                 }).
                 error(function(data, status, headers, config) {
	                 $scope.messages = data;
	                 $scope.loader = false;
                 });
                 

                //$scope.chemAssociationForm = {"simpleFile":null,"type":"attachment","bondType":"covalent","description":"Test","associatedElementA":{"compositionType":"nanomaterial entity","domainId":"72056840","entityId":"72056840","entityDisplayName":"fullerene","className":"ComposingElement","composingElement":{"type":"repeat unit","name":"test","pubChemDataSourceName":"","pubChemId":null,"value":null,"valueUnit":null,"molecularFormulaType":null,"molecularFormula":null,"description":"","id":72089608,"sampleId":"","modality":"","createdBy":"prakasht","createdDate":null,"inherentFunction":null}},"associatedElementB":{"compositionType":"functionalizing entity","entityId":"22719746","entityDisplayName":"small molecule","className":"SmallMolecule","composingElement":{"type":null,"name":null,"pubChemDataSourceName":null,"pubChemId":null,"value":null,"valueUnit":null,"molecularFormulaType":null,"molecularFormula":null,"description":null,"id":null,"sampleId":"","modality":"","createdBy":null,"createdDate":null,"inherentFunction":null}},"errors":null};
            }
        }


        if( $scope.chemAssociationId != null ) {
            $scope.loadChemAssociationData();
        }
        else {
            $scope.files = [];
        }

        $scope.loadAssociatedElements = function(type, compositionType) {
            if (compositionType != null) {
                $scope.loader = true;

                $http({method: 'POST', url: '/caNanoLab/rest/chemicalAssociation/getAssociatedElementOptions?compositionType=' + compositionType}).
                    success(function (data, status, headers, config) {
                        if( type == 'A') {
                            $scope.associatedElementsA = data;
                        }
                        else {
                            $scope.associatedElementsB = data;
                        }

                        $scope.loader = false;
                    }).
                    error(function (data, status, headers, config) {
                        $scope.loader = false;
                        $scope.messages = data;
                    });

                //$scope.associatedElementsA = [{"type":"dendrimer","description":"G4.5 COONa terminated PAMAM dendrimer","theFile":{"uriExternal":false,"uri":null,"type":null,"title":null,"description":null,"keywordsStr":"","id":null,"createdBy":null,"createdDate":null},"className":"Dendrimer","files":null,"domainId":"21376268","displayName":null},{"type":"dendrimer","description":"Test Nano Entity","theFile":{"uriExternal":false,"uri":null,"type":null,"title":null,"description":null,"keywordsStr":"","id":null,"createdBy":null,"createdDate":null},"className":"Dendrimer","files":null,"domainId":"60260353","displayName":null},{"type":"fullerene","description":"tets","theFile":{"uriExternal":false,"uri":null,"type":null,"title":null,"description":null,"keywordsStr":"","id":null,"createdBy":null,"createdDate":null},"className":"Fullerene","files":null,"domainId":"68780032","displayName":null},{"type":"liposome","description":"Test Nano Material Entity","theFile":{"uriExternal":false,"uri":null,"type":null,"title":null,"description":null,"keywordsStr":"","id":null,"createdBy":null,"createdDate":null},"className":"Liposome","files":null,"domainId":"60456960","displayName":null},{"type":"metal oxide","description":"","theFile":{"uriExternal":false,"uri":null,"type":null,"title":null,"description":null,"keywordsStr":"","id":null,"createdBy":null,"createdDate":null},"className":"OtherNanomaterialEntity","files":null,"domainId":"60555264","displayName":null}];
            }

        };

        $scope.loadComposingElements = function(type, compositionType, associatedElementId) {
            if (associatedElementId != null) {
            	
                var assocList = [];
                if( type == 'A') {
                    assocList = $scope.associatedElementsA;
                } else {
                    assocList = $scope.associatedElementsB;
                }
                
                if( assocList != null ) {
	                for (var k = 0; k < assocList.length; ++k) {
	                    var element = assocList[k];
	                    if (element.domainId == associatedElementId ) {
	                        $scope.entityDisplayName = element.type;
	                        break;
	                    }
	                }
	                
	                if ( type == 'A') {
	                    $scope.chemAssociationForm.associatedElementA.entityDisplayName = $scope.entityDisplayName;
	                } else {
	                    $scope.chemAssociationForm.associatedElementB.entityDisplayName = $scope.entityDisplayName;
	                }
                }
               
                if (compositionType == 'nanomaterial entity') {
                    $scope.loader = true;

	                $http({method: 'POST', url: '/caNanoLab/rest/chemicalAssociation/getComposingElementsByNanomaterialEntityId?id=' + associatedElementId}).
	                    success(function (data, status, headers, config) {
	                        if( type == 'A') {
	                            $scope.composingElementsA = data;
	                        } else {
	                            $scope.composingElementsB = data;
	                        }
	                        $scope.loader = false;
	                    }).
	                    error(function (data, status, headers, config) {
	                        $scope.loader = false;
	                        $scope.messages = data;
	                    });
	
	                //$scope.composingElementsA = [{"type":"modifier","name":"chem Test","pubChemDataSourceName":"Compound","pubChemId":2312,"value":null,"valueUnit":"","molecularFormulaType":"","molecularFormula":"","description":"Test Compsing Element","id":0,"sampleId":"","modality":"","createdBy":"jonnalah","createdDate":1410006671000,"inherentFunction":[]},{"type":"coat","name":"Test Coat","pubChemDataSourceName":"Substance","pubChemId":345,"value":null,"valueUnit":"","molecularFormulaType":"","molecularFormula":"","description":"Test","id":0,"sampleId":"","modality":"","createdBy":"jonnalah","createdDate":1410007056000,"inherentFunction":[{"createdBy":"jonnalah","description":"Test","modality":null,"type":"targeting function","createdDate":1410007056000},{"createdBy":"jonnalah","description":"Test2","modality":null,"type":"therapeutic function","createdDate":1410007057000}]}];
                }	
            }

        };


        $scope.doSubmit = function() {
            $scope.loader = true;
            
            if( $scope.sampleId != null ) {
            	$scope.chemAssociationForm.sampleId = $scope.sampleId;
            }  
            
            if( $scope.chemAssociationId != null ) {
            	$scope.chemAssociationForm.associationId = $scope.chemAssociationId;
            }             

            $http({method: 'POST', url: '/caNanoLab/rest/chemicalAssociation/submit',data: $scope.chemAssociationForm}).
                success(function(data, status, headers, config) {
                    if (data == "success") {
                        //$location.search('message', 'Nanomaterial Entity successfully saved "').path('/message').replace();
                        $location.search('message', 'Chemical Association successfully saved.').path('/editComposition').replace();
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
            if (confirm("Are you sure you want to delete the Chemical Association?")) {
                $scope.loader = true;

                $http({method: 'POST', url: '/caNanoLab/rest/chemicalAssociation/delete',data: $scope.chemAssociationForm}).
                    success(function(data, status, headers, config) {
                        if (data == "success") {
                            $location.search('message', 'Chemical Association successfully deleted.').path('/editComposition').replace();
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
            $scope.chemAssociationForm = {};
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
                
                for (var k = 0; k < $scope.files.length; ++k) {
                    var element = $scope.files[k];
                    if (element.id == $scope.fileForm.id) {
                        $scope.files.splice(k,1);
                    }
                }
                $scope.chemAssociationForm.files = $scope.files;
                
                if( $scope.chemAssociationForm.simpleFile == null ) {
                	$scope.chemAssociationForm.simpleFile = {};
                }

                $scope.chemAssociationForm.simpleFile.externalUrl = $scope.fileForm.externalUrl;
                $scope.chemAssociationForm.simpleFile.uri = $scope.fileForm.uri;
                $scope.chemAssociationForm.simpleFile.uriExternal = $scope.fileForm.uriExternal;
                $scope.chemAssociationForm.simpleFile.type = $scope.fileForm.type;
                $scope.chemAssociationForm.simpleFile.title = $scope.fileForm.title;
                $scope.chemAssociationForm.simpleFile.keywordsStr = $scope.fileForm.keywordsStr;
                $scope.chemAssociationForm.simpleFile.description = $scope.fileForm.description;
                $scope.chemAssociationForm.simpleFile.id = $scope.fileForm.id;
                $scope.chemAssociationForm.simpleFile.theAccess = $scope.fileForm.theAccess;
                $scope.chemAssociationForm.simpleFile.isPublic = $scope.fileForm.isPublic;
                $scope.chemAssociationForm.simpleFile.createdBy = $scope.fileForm.createdBy;
                $scope.chemAssociationForm.simpleFile.createdDate = $scope.fileForm.createdDate;                  

                if( $scope.sampleId != null ) {
                	$scope.chemAssociationForm.sampleId = $scope.sampleId;
                }       
                
                if( $scope.chemAssociationId != null ) {
                	$scope.chemAssociationForm.associationId = $scope.chemAssociationId;
                }                

                $http({method: 'POST', url: '/caNanoLab/rest/chemicalAssociation/removeFile',data: $scope.chemAssociationForm}).
                    success(function(data, status, headers, config) {
                        $scope.chemAssociationForm = data;
                        $scope.files = $scope.chemAssociationForm.files;
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
                    data : $scope.protocolForm,
                    file: $scope.selectedFiles[index],
                    fileFormDataName: 'myFile'
                });
                $scope.upload[index].then(function(response) {
                    $timeout(function() {
                        //$scope.uploadResult.push(response.data);
                        //alert(response.data);
                        //$scope.chemAssociationForm = response.data;
                        $scope.saveFileData();
                        //$scope.loader = false;
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
//            else {
//                $scope.files.push($scope.fileForm);
//            }

            $scope.chemAssociationForm.files = $scope.files;
            
            if( $scope.chemAssociationForm.simpleFile == null ) {
            	$scope.chemAssociationForm.simpleFile = {};
            }


            $scope.chemAssociationForm.simpleFile.externalUrl = $scope.fileForm.externalUrl;
            if( $scope.selectedFileName != null && $scope.selectedFileName != '' ) {
            	$scope.chemAssociationForm.simpleFile.uri = $scope.selectedFileName;
            } else {
            	$scope.chemAssociationForm.simpleFile.uri = $scope.fileForm.uri;
            }
            $scope.chemAssociationForm.simpleFile.uriExternal = $scope.fileForm.uriExternal;
            $scope.chemAssociationForm.simpleFile.type = $scope.fileForm.type;
            $scope.chemAssociationForm.simpleFile.title = $scope.fileForm.title;
            $scope.chemAssociationForm.simpleFile.keywordsStr = $scope.fileForm.keywordsStr;
            $scope.chemAssociationForm.simpleFile.description = $scope.fileForm.description;
            $scope.chemAssociationForm.simpleFile.id = $scope.fileForm.id;
            $scope.chemAssociationForm.simpleFile.theAccess = $scope.fileForm.theAccess;
            $scope.chemAssociationForm.simpleFile.isPublic = $scope.fileForm.isPublic;
            $scope.chemAssociationForm.simpleFile.createdBy = $scope.fileForm.createdBy;
            $scope.chemAssociationForm.simpleFile.createdDate = $scope.fileForm.createdDate;             
            

            if( $scope.sampleId != null ) {
            	$scope.chemAssociationForm.sampleId = $scope.sampleId;
            }
            
            if( $scope.chemAssociationId != null ) {
            	$scope.chemAssociationForm.associationId = $scope.chemAssociationId;
            }            
            
            $scope.messages = [];
            $http({method: 'POST', url: '/caNanoLab/rest/chemicalAssociation/saveFile',data: $scope.chemAssociationForm}).
                success(function(data, status, headers, config) {
                    $scope.chemAssociationForm = data;
                    $scope.files = $scope.chemAssociationForm.files;
                    $scope.addNewFile = false;
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


