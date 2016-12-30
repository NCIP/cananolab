'use strict';
var app = angular.module('angularApp')

    .controller('EditUserCtrl', function (navigationService,groupService,$rootScope,$scope,$http,$location,$timeout,$routeParams,$upload) {
        $scope.userForm = {};
        $scope.userTag = '';
        $scope.userForm.uriExternal = false;
        $scope.userForm.review = false;
        $scope.reviewData = {
        		dataId : null,
        		dataName : null,
        		dataType : 'user'
            };
        $scope.localForm = {};        
        $scope.localForm.otherCategoryText = '';
        $scope.externalUrlEnabled  = false; 
        
        // Access variables
        $scope.defineAccessVariables = function() {
            $scope.userForm.theAccess = {};
            $scope.accessForm = {};
            $scope.dataType = 'User';
            $scope.parentFormName = 'userForm';
            $scope.accessForm.theAcccess = {};
            $scope.isCurator = groupService.isCurator();
            $scope.groupAccesses = [];
            $scope.userAccesses = [];
            $scope.addAccess = false;
            $scope.showAddAccessButton = true;
            $scope.showCollaborationGroup = true;
            $scope.showAccessuser = false;
            $scope.showAccessSelection = false;
            $scope.accessForm.theAccess = {};
            $scope.accessForm.theAccess.recipient = '';
            $scope.accessForm.theAccess.recipientDisplayName = '';
            $scope.access = {};
            $scope.access.recipient = '';
            $scope.access.recipientDisplayName = '';
            $scope.userForm.isPublic = false;
            $scope.accessForm.theAccess.accessBy = 'group';
            $scope.accessExists = false;
        };
        $scope.defineAccessVariables();
        /* File Variables */
        $scope.usingFlash = FileAPI && FileAPI.upload != null;
        $scope.fileReaderSupported = window.FileReader != null && (window.FileAPI == null || FileAPI.html5 != false);
        $scope.selectedFileName = '';
        
        var uploadUrl = '/caNanoLab/rest/core/uploadFile';
        $scope.ie9 = false;
        if(navigator.appVersion.indexOf("MSIE 9.")!=-1){
            uploadUrl = '/caNanoLab/uploadFile';
            $scope.ie9 = true;
        }

        $scope.loader = true;
        $scope.$on('$viewContentLoaded', function(){
            $http({method: 'GET', url: '/caNanoLab/rest/user/setup'}).
                success(function(data, status, headers, config) {
                    $scope.data = data;
                    $scope.userTypes = data.userTypes;
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

        $scope.loadUserData = function() {
            if( $scope.userTag != null ) {
                $scope.loader = true;
                $http({method: 'GET', url: '/caNanoLab/rest/user/edit?userTag=' + $scope.userTag}).
                    success(function(data, status, headers, config) {
                        $scope.userForm = data;
                        $scope.loader = false;

                        $scope.groupAccesses = $scope.userForm.groupAccesses;
                        $scope.userAccesses = $scope.userForm.userAccesses;

                        if( $scope.userAccesses != null && $scope.userAccesses.length > 0 ) {
                            $scope.accessExists = true;
                        }

                        if( $scope.groupAccesses != null && $scope.groupAccesses.length > 1 ) {
                            $scope.accessExists = true;
                        }
                        
		                if( $scope.userForm.externalUrl != null && $scope.userForm.externalUrl != '') {
		                    $scope.externalUrlEnabled = true;
		                    $scope.userForm.uriExternal = 'true';

		                    $timeout(function() {
		                        var el = document.getElementById('external1');
		                        angular.element(el).triggerHandler('click');
		                    }, 0);
		                }
		                else {
		                    $scope.externalUrlEnabled  = false;
		                    $scope.userForm.uriExternal = 'false';

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

        $scope.userTag = $routeParams.userTag;

        if( $scope.userTag != null ) {
            $scope.loadUserData();
        }
        else {
            $scope.$on('$viewContentLoaded', function() {
                $scope.userForm.uriExternal = 'false';
                $scope.externalUrlEnabled = false;
            });
        }        

        $scope.doSubmitData = function() {
            $scope.loader = true;
            
            if (typeof $scope.userForm.fileId == 'undefined' || $scope.userForm.fileId == null) {
            	$scope.userForm.fileId = '0';
            }

            $http({method: 'POST', url: '/caNanoLab/rest/user/saveUser',data: $scope.userForm}).
                success(function(data, status, headers, config) {
                    if (data[0] == "success") {
                    	var msg = 'User successfully saved with username "' + $scope.userForm.userName + '"';
                    	$scope.messages = [msg];
                    	$scope.userTag = data[1];
                    	$scope.loadUserData();
                    }
                    else {
                        $scope.loader = false;
                        $scope.messages = data;
                    }
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $scope.loader = false;
                    $scope.messages = data;
                });
        };

        $scope.resetForm = function() {
            $scope.userForm = {};
            $scope.defineAccessVariables();
        };
        
        $scope.fillUserInfo = function() {
            $scope.loader = true;
            
            // if (typeof $scope.userForm.version == 'undefined') {
            // 	$scope.userForm.version = '';
            // }

            $http({method: 'GET', url: '/caNanoLab/rest/user/getUser?firstName=' + $scope.userForm.firstName + '&userTag=' + $scope.userForm.userName + '&lastName=' + $scope.userForm.lastName }).
                success(function(data, status, headers, config) {
                    $scope.user = data;
                    $scope.userTag = data.id;

                    if (confirm("A database record with the same user name already exists.  Load it and update?")) {
                    	if ($scope.user.userUpdatable) {
                    		$scope.loadUserData();
                    	}
                    	// else {
                    	// 	alert("The user already exists and you don't have update and delete privilege on this user");
                    	// }
                    }

                    $scope.loader = false;
                }).
                error(function(data, status, headers, config) {
                    $scope.loader = false;
                });
        };

       
        $scope.goBack = function() {
            $location.path("/userResults").replace();
            $location.search('userTag', null);
        };
                
        $scope.doSubmit = function() {
        	// if( $scope.userForm.isPublic && ! $scope.isCurator.curator) {
        	// 	if (confirm("The data has been assigned to Public.  Updating the data would retract it from Public.  You will need to resubmit the data to the curator for review before the curator reassigns it to Public.  Are you sure you want to continue?")) {
        	// 		// continue
        	// 	} else {
        	// 		return false;
        	// 	}
        	// }
            var index = 0;
            $scope.upload = [];
            if ($scope.selectedFiles != null && $scope.selectedFiles.length > 0 ) {
                $scope.upload[index] = $upload.upload({
                    url: uploadUrl,
                    method: 'POST',
                    headers: {'my-header': 'my-header-value'},
                    data : $scope.userForm,
                    file: $scope.selectedFiles[index],
                    fileFormDataName: 'myFile'
                });
                $scope.upload[index].then(function(response) {
                    $timeout(function() {
                    	$scope.userForm.uri = response.data;
                    	$scope.doSubmitData();
                    });
                }, function(response) {
                	$timeout(function() {
                    	//only for IE 9
                        if(navigator.appVersion.indexOf("MSIE 9.")!=-1) {
                        	$scope.userForm.uri = response.data;
                        	$scope.doSubmitData();
                        }
                    });
                    if (response.status > 0) { 
                    	$scope.messages = response.status + ': ' + response.data;
                    	$scope.loader = false;
                    }
                }, function(evt) {
                    // Math.min is to fix IE which reports 200% sometimes
                });
                $scope.upload[index].xhr(function(xhr){
                });
        	}
        	else {
        		$scope.doSubmitData();
        	}
        };        

        /** Start - Access functions **/

        $scope.getCollabGroups = function() {
            if ($scope.accessForm.theAccess.recipient === undefined || $scope.accessForm.theAccess.recipient === null) {
                $scope.accessForm.theAccess.recipient = '';
                $scope.accessForm.theAccess.recipientDisplayName = '';
            }

            $scope.loader = true;
            $http({method: 'GET', url: '/caNanoLab/rest/core/getCollaborationGroup?searchStr=' + $scope.accessForm.theAccess.recipient}).
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

            $scope.showAccessSelection=true;

        };

        $scope.getAccessUsers = function() {
            if ($scope.accessForm.theAccess.recipient === undefined || $scope.accessForm.theAccess.recipient === null) {
                $scope.accessForm.theAccess.recipient = '';
            }

            $scope.loader = true;
            $http({method: 'GET', url: '/caNanoLab/rest/core/getUsers?searchStr=' + $scope.accessForm.theAccess.recipient}).
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

            $scope.showAccessSelection=true;

        };

        $scope.hideAccessSection = function() {
            $scope.addAccess=false;
            $scope.showAddAccessButton=true;
        }

        $scope.saveAccessSection = function() {
            $scope.loader = true;
            $scope.userForm.theAccess = $scope.accessForm.theAccess;
            $scope.addAccess=false;
            $scope.showAddAccessButton=true;

            if( $scope.accessForm.theAccess.accessBy == 'public') {
                $scope.userForm.isPublic = true;
            }
            
            if (typeof $scope.userForm.fileId == 'undefined' || $scope.userForm.fileId == null) {
            	$scope.userForm.fileId = '0';
            }

            $http({method: 'POST', url: '/caNanoLab/rest/user/saveAccess',data: $scope.userForm}).
                success(function(data, status, headers, config) {
                    $scope.userForm = data;
                    $scope.groupAccesses = $scope.userForm.groupAccesses;
                    $scope.userAccesses = $scope.userForm.userAccesses;
                    $scope.loader = false;
                    $scope.accessExists = true;
                }).
                error(function(data, status, headers, config) {
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
            $scope.accessForm.theAccess.recipient=loginName;
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
            $scope.accessForm.theAccess.recipient=groupName;
            $scope.accessForm.theAccess.recipientDisplayName=groupName;
            $scope.showCollaborationGroup=true;
            $scope.showAccessuser=false;
            $scope.showAccessSelection=false;

            for(var key in $scope.csmRoleNames){
                if($scope.csmRoleNames[key] == groupAccess){
                    $scope.accessForm.theAccess.roleName = key;
                }
            }

            if($scope.accessForm.theAccess.recipient == 'ROLE_ANONYMOUS') {
                $scope.accessForm.theAccess.accessBy='public';
            }
        }


        $scope.removeAccessSection = function() {
            $scope.userForm.theAccess = $scope.accessForm.theAccess;
            $scope.addAccess=false;
            $scope.showAddAccessButton=true;

            if (confirm("Are you sure you want to delete?")) {
                $scope.loader = true;
                
                if (typeof $scope.userForm.fileId == 'undefined' || $scope.userForm.fileId == null) {
                	$scope.userForm.fileId = '0';
                }

                $http({method: 'POST', url: '/caNanoLab/rest/user/deleteAccess',data: $scope.userForm}).
                    success(function(data, status, headers, config) {
                        $scope.userForm = data;
                        $scope.groupAccesses = $scope.userForm.groupAccesses;
                        $scope.userAccesses = $scope.userForm.userAccesses;
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
            $scope.accessForm.theAccess.recipient = 'ROLE_ANONYMOUS';
            $scope.accessForm.theAccess.recipientDisplayName = 'Public';
            $scope.accessForm.theAccess.roleName = 'R';
            $scope.showCollaborationGroup=true;
            $scope.showAccessuser=false;
            $scope.showAccessSelection=false;
        };       

        /** End - Access Section **/
    });


