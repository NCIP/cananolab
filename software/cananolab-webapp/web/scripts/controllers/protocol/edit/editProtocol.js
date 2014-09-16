'use strict';
var app = angular.module('angularApp')

    .controller('EditProtocolCtrl', function (navigationService,groupService,$rootScope,$scope,$http,$location,$timeout,$routeParams,$upload) {
        $scope.protocolForm = {};
        //$rootScope.tabs = navigationService.query();
        //$rootScope.groups = groupService.getGroups.data.get();

        $scope.protocolId = '';
        $scope.protocolForm.uriExternal = false;
        $scope.protocolForm.review = false;
        $scope.reviewData = {
        		dataId : null,
        		dataName : null,
        		dataType : 'protocol'
            };
        $scope.localForm = {};        
        $scope.localForm.otherCategoryText = '';
        $scope.externalUrlEnabled  = false; 
        
        // Access variables
        $scope.protocolForm.theAccess = {};
        $scope.accessForm = {};
        $scope.dataType = 'Protocol';
        $scope.parentFormName = 'protocolForm';
        $scope.accessForm.theAcccess = {};
        $scope.accessForm.theAcccess.userBean = {};
        $scope.isCurator = groupService.isCurator();
        $scope.groupAccesses = [];
        $scope.userAccesses = [];
        $scope.addAccess = false;
        $scope.showAddAccessButton = true;
        $scope.showCollaborationGroup = true;
        $scope.showAccessuser = false;
        $scope.showAccessSelection = false;
        $scope.accessForm.theAccess = {};
        $scope.accessForm.theAccess.groupName = '';
        $scope.accessForm.theAccess.userBean = {};
        $scope.accessForm.theAccess.userBean.loginName = '';
        $scope.access = {};
        $scope.access.groupName = '';
        $scope.access.loginName = '';
        $scope.protocolForm.isPublic = false;
        $scope.accessForm.theAccess.accessBy = 'group';
        $scope.accessExists = false;

        /* File Variables */
        $scope.usingFlash = FileAPI && FileAPI.upload != null;
        $scope.fileReaderSupported = window.FileReader != null && (window.FileAPI == null || FileAPI.html5 != false);

        $scope.loader = true;
        $scope.$on('$viewContentLoaded', function(){
            $http({method: 'GET', url: '/caNanoLab/rest/protocol/setup'}).
                success(function(data, status, headers, config) {
                    $scope.data = data;
                    $scope.protocolTypes = data.protocolTypes;
                    $scope.csmRoleNames = data.csmRoleNames;
                    $scope.loader = false;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $scope.messages = data;
                    $scope.loader = false;
                });
        });

        $scope.loadProtocolData = function() {
            if( $scope.protocolId != null ) {
                $scope.loader = true;
                $http({method: 'GET', url: '/caNanoLab/rest/protocol/edit?protocolId=' + $scope.protocolId}).
                    success(function(data, status, headers, config) {
                        $scope.protocolForm = data;
                        $scope.loader = false;

                        $scope.groupAccesses = $scope.protocolForm.groupAccesses;
                        $scope.userAccesses = $scope.protocolForm.userAccesses;

                        if( $scope.userAccesses != null && $scope.userAccesses.length > 0 ) {
                            $scope.accessExists = true;
                        }

                        if( $scope.groupAccesses != null && $scope.groupAccesses.length > 1 ) {
                            $scope.accessExists = true;
                        }
                        
		                if( $scope.protocolForm.externalUrl != null && $scope.protocolForm.externalUrl != '') {
		                    $scope.externalUrlEnabled = true;
		                    $scope.protocolForm.uriExternal = 'true';

		                    $timeout(function() {
		                        var el = document.getElementById('external1');
		                        angular.element(el).triggerHandler('click');
		                    }, 0);
		                }
		                else {
		                    $scope.externalUrlEnabled  = false;
		                    $scope.protocolForm.uriExternal = 'false';

		                    $timeout(function() {
		                        var el = document.getElementById('external0');
		                        angular.element(el).triggerHandler('click');
		                    }, 0);
		                }
                        
                    }).
                    error(function(data, status, headers, config) {
                        $scope.messages = data;
                        $scope.loader = false;
                    });
            }
        }

        $scope.protocolId = $routeParams.protocolId;

        if( $scope.protocolId != null ) {
            $scope.loadProtocolData();
        }
        else {
            $scope.$on('$viewContentLoaded', function() {
                $scope.protocolForm.uriExternal = 'false';
                $scope.externalUrlEnabled = false;
            });
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
            $scope.protocolForm = {};
        };
        
        $scope.fillProtocolInfo = function() {
            $scope.loader = true;
            
            if (typeof $scope.protocolForm.version == 'undefined') {
            	$scope.protocolForm.version = '';
            }

            $http({method: 'GET', url: '/caNanoLab/rest/protocol/getProtocol?protocolType=' + $scope.protocolForm.type + '&protocolName=' + $scope.protocolForm.name + '&protocolVersion=' + $scope.protocolForm.version }).
                success(function(data, status, headers, config) {
                    $scope.protocol = data;
                    $scope.protocolId = data.id;

                    if (confirm("A database record with the same protocol type and protocol name already exists.  Load it and update?")) {
                    	if ($scope.protocol.userUpdatable) {
                    		$scope.loadProtocolData();
                    	}
                    	else {
                    		alert("The protocol already exists and you don't have update and delete privilege on this protocol");
                    	}
                    }

                    $scope.loader = false;
                }).
                error(function(data, status, headers, config) {
                    $scope.loader = false;
                });
        };

       
        $scope.goBack = function() {
            $location.path("/protocolResults").replace();
            $location.search('protocolId', null);
        };
        
        $scope.submitForReview = function() {
            $scope.loader = true;
            
            $scope.reviewData.dataId = $scope.protocolId;
            $scope.reviewData.dataName = 	$scope.protocolForm.name;

            $http({method: 'POST', url: '/caNanoLab/rest/protocol/submitForReview',data: $scope.reviewData}).
                success(function(data, status, headers, config) {
                    if (data == "success") {
                        $location.search('message', 'Protocol successfully submitted to the curator for review and release to public.').path('/message').replace();
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

        /** Start - Access functions **/

        $scope.getCollabGroups = function() {
            if ($scope.accessForm.theAccess.groupName === undefined || $scope.accessForm.theAccess.groupName === null) {
                $scope.accessForm.theAccess.groupName = '';
            }

            $scope.loader = true;
            $http({method: 'GET', url: '/caNanoLab/rest/core/getCollaborationGroup?searchStr=' + $scope.accessForm.theAccess.groupName}).
                success(function(data, status, headers, config) {
                    $scope.collabGroups = data;
                    $scope.loader = false;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $scope.message = data;
                    $scope.loader = false;
                });

            //$scope.collabGroups = ["curator group", "NCI", "NCIP"];
            $scope.showAccessSelection=true;

        };

        $scope.getAccessUsers = function() {
            if ($scope.accessForm.theAccess.userBean.loginName === undefined || $scope.accessForm.theAccess.userBean.loginName === null) {
                $scope.accessForm.theAccess.userBean.loginName = '';
            }

            $scope.loader = true;
            $http({method: 'GET', url: '/caNanoLab/rest/core/getUsers?searchStr=' + $scope.accessForm.theAccess.userBean.loginName}).
                success(function(data, status, headers, config) {
                    $scope.accessUsers = data;
                    $scope.loader = false;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $scope.message = data;
                    $scope.loader = false;
                });

            //$scope.accessUsers = {"lethai":"Le Thai","Omelchen":"Omelchenko Marina","burnskd":"Burns Kevin","canano_guest":"Guest Guest","grodzinp":"Grodzinski Piotr","swand":"Swan Don","skoczens":"Skoczen Sarah","sternstephan":"Stern Stephan","zolnik":"Zolnik Banu","hunseckerk":"Hunsecker Kelly","lipkeyfg":"Lipkey Foster","marina":"Dobrovolskaia Marina","pottert":"Potter Tim","uckunf":"Uckun Fatih","michal":"Lijowski Michal","mcneils":"Mcneil Scott","neunb":"Neun Barry","cristr":"Crist Rachael","zhengji":"Zheng Jiwen","frittsmj":"Fritts Martin","SchaeferH":"Schaefer Henry","benhamm":"Benham Mick","masoods":"Masood Sana","mclelandc":"McLeland Chris","torresdh":"Torres David","KlemmJ":"Klemm Juli","patria":"Patri Anil","hughesbr":"Hughes Brian","clogstonj":"Clogston Jeff","hinkalgw":"Hinkal George","MorrisS2":"Morris Stephanie","sharon":"Gaheen Sharon"};
            $scope.showAccessSelection=true;

        };

        $scope.hideAccessSection = function() {
            $scope.addAccess=false;
            $scope.showAddAccessButton=true;
        }

        $scope.saveAccessSection = function() {
            $scope.loader = true;
            $scope.protocolForm.theAccess = $scope.accessForm.theAccess;
            $scope.addAccess=false;
            $scope.showAddAccessButton=true;

            if( $scope.accessForm.theAccess.accessBy == 'public') {
                $scope.protocolForm.isPublic = true;
            }
            
            if (typeof $scope.protocolForm.fileId == 'undefined' || $scope.protocolForm.fileId == null) {
            	$scope.protocolForm.fileId = '0';
            }

            $http({method: 'POST', url: '/caNanoLab/rest/protocol/saveAccess',data: $scope.protocolForm}).
                success(function(data, status, headers, config) {
                    // $rootScope.sampleData = data;
                    //$scope.sampleData.data = data;
                    //$location.path("/sampleResults").replace();

                    $scope.protocolForm = data;

                    $scope.groupAccesses = $scope.protocolForm.groupAccesses;
                    $scope.userAccesses = $scope.protocolForm.userAccesses;

                    //console.log($scope.groupAccesses);
                    //console.log($scope.userAccesses);

                    $scope.loader = false;
                    $scope.accessExists = true;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    // $rootScope.sampleData = data;
                    $scope.loader = false;
                    $scope.messages = data;
                    $scope.showAddAccessButton=false;
                    $scope.addAccess=true;
                    $scope.showAccessSelection=false;
                });

        };

        $scope.editUserAccessSection = function(loginName, userAccess) {
            $scope.addAccess=true;
            $scope.accessForm.theAccess.accessBy='user';
            $scope.accessForm.theAccess.userBean.loginName=loginName;
            $scope.showCollaborationGroup=false;
            $scope.showAccessuser=true;
            $scope.showAccessSelection=false;

            for(var key in $scope.csmRoleNames){
                if($scope.csmRoleNames[key] == userAccess){
                    $scope.accessForm.theAccess.roleName = key;
                }
            }
        }

        $scope.editGroupAccessSection = function(groupName, groupAccess) {
            $scope.addAccess=true;
            $scope.accessForm.theAccess.accessBy='group';
            $scope.accessForm.theAccess.groupName=groupName;
            $scope.showCollaborationGroup=true;
            $scope.showAccessuser=false;
            $scope.showAccessSelection=false;

            for(var key in $scope.csmRoleNames){
                if($scope.csmRoleNames[key] == groupAccess){
                    $scope.accessForm.theAccess.roleName = key;
                }
            }

            if($scope.accessForm.theAccess.groupName == 'Public') {
                $scope.accessForm.theAccess.accessBy='public';
            }
        }


        $scope.removeAccessSection = function() {
            $scope.protocolForm.theAccess = $scope.accessForm.theAccess;
            $scope.addAccess=false;
            $scope.showAddAccessButton=true;

            if (confirm("Are you sure you want to delete?")) {
                $scope.loader = true;
                
                if (typeof $scope.protocolForm.fileId == 'undefined' || $scope.protocolForm.fileId == null) {
                	$scope.protocolForm.fileId = '0';
                }

                $http({method: 'POST', url: '/caNanoLab/rest/protocol/deleteAccess',data: $scope.protocolForm}).
                    success(function(data, status, headers, config) {
                        // $rootScope.sampleData = data;
                        //$scope.sampleData.data = data;
                        //$location.path("/sampleResults").replace();

                        $scope.protocolForm = data;

                        $scope.groupAccesses = $scope.protocolForm.groupAccesses;
                        $scope.userAccesses = $scope.protocolForm.userAccesses;

                        if( $scope.userAccesses != null && $scope.userAccesses.length > 0 ) {
                            $scope.accessExists = true;
                        }

                        if( $scope.groupAccesses != null && $scope.groupAccesses.length > 1 ) {
                            $scope.accessExists = true;
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

        };
        
        $scope.getUserAccessSection = function() {
            return $scope.showAccessuser && $scope.showAccessSelection;
        };

        $scope.getCollabAccessSection = function() {
            return $scope.showCollaborationGroup && $scope.showAccessSelection;
        };

        $scope.selectCgAccess = function() {
            $scope.showCollaborationGroup=true;
            $scope.showAccessuser=false;
            $scope.showAccessSelection=false;
        };

        $scope.selectUserAccess = function() {
            $scope.showCollaborationGroup=false;
            $scope.showAccessuser=true;
            $scope.showAccessSelection=false;
        };

        $scope.selectPublicAccess = function() {
            $scope.accessForm.theAccess.groupName = 'Public';
            $scope.accessForm.theAccess.roleName = 'R';
            $scope.showCollaborationGroup=true;
            $scope.showAccessuser=false;
            $scope.showAccessSelection=false;
        };       

        /** End - Access Section **/
    });


