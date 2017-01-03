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
        
        // Role variables
        $scope.defineRoleVariables = function() {
            $scope.userForm.theRole = {};
            $scope.roleForm = {};
            $scope.dataType = 'User';
            $scope.parentFormName = 'userForm';
            $scope.roleForm.theAcccess = {}; //FIX_ME
            $scope.isCurator = groupService.isCurator();
            $scope.adminRoles = [];
            $scope.curatorRoles = [];
            $scope.addRole = false;
            $scope.showAddRoleButton = true;
            $scope.showCollaborationGroup = true;
            $scope.showRolecurator = false;
            $scope.showRoleSelection = false;
            $scope.roleForm.theRole = {};
            $scope.roleForm.theRole.recipient = '';
            $scope.roleForm.theRole.recipientDisplayName = '';
            $scope.role = {};
            $scope.role.recipient = '';
            $scope.role.recipientDisplayName = '';
            $scope.userForm.isAdmin = false;
            $scope.roleForm.theRole.roleBy = 'admin';
            $scope.roleExists = false;
        };
        $scope.defineRoleVariables();
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

                        $scope.adminRoles = $scope.userForm.adminRoles;
                        $scope.curatorRoles = $scope.userForm.curatorRoles;

                        if( $scope.curatorRoles != null && $scope.curatorRoles.length > 0 ) {
                            $scope.roleExists = true;
                        }

                        if( $scope.adminRoles != null && $scope.adminRoles.length > 1 ) {
                            $scope.roleExists = true;
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
            $scope.defineRoleVariables();
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

                    if (confirm("A database record with the same username already exists.  Load it and update?")) {
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
        	// if( $scope.userForm.isAdmin && ! $scope.isCurator.curator) {
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

        /** Start - Role functions **/

        $scope.getCollabGroups = function() {
            if ($scope.roleForm.theRole.recipient === undefined || $scope.roleForm.theRole.recipient === null) {
                $scope.roleForm.theRole.recipient = '';
                $scope.roleForm.theRole.recipientDisplayName = '';
            }

            $scope.loader = true;
            $http({method: 'GET', url: '/caNanoLab/rest/core/getCollaborationGroup?searchStr=' + $scope.roleForm.theRole.recipient}).
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

            $scope.showRoleSelection=true;

        };

        $scope.getRolePublic = function() {
            if ($scope.roleForm.theRole.recipient === undefined || $scope.roleForm.theRole.recipient === null) {
                $scope.roleForm.theRole.recipient = '';
            }

            $scope.loader = true;
            $http({method: 'GET', url: '/caNanoLab/rest/core/getUsers?searchStr=' + $scope.roleForm.theRole.recipient}).
                success(function(data, status, headers, config) {
                    $scope.roleCurators = data;
                    $scope.loader = false;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $scope.message = data;
                    $scope.loader = false;
                });

            $scope.showRoleSelection=true;

        };

        $scope.hideRoleSection = function() {
            $scope.addRole=false;
            $scope.showAddRoleButton=true;
        }

        $scope.saveRoleSection = function() {
            $scope.loader = true;
            $scope.userForm.theRole = $scope.roleForm.theRole;
            $scope.addRole=false;
            $scope.showAddRoleButton=true;

            if( $scope.roleForm.theRole.roleBy == 'admin') {
                $scope.userForm.isAdmin = true;
            }
            
            if (typeof $scope.userForm.fileId == 'undefined' || $scope.userForm.fileId == null) {
            	$scope.userForm.fileId = '0';
            }

            $http({method: 'POST', url: '/caNanoLab/rest/user/saveRole',data: $scope.userForm}).
                success(function(data, status, headers, config) {
                    $scope.userForm = data;
                    $scope.adminRoles = $scope.userForm.adminRoles;
                    $scope.curatorRoles = $scope.userForm.curatorRoles;
                    $scope.loader = false;
                    $scope.roleExists = true;
                }).
                error(function(data, status, headers, config) {
                    $scope.loader = false;
                    $scope.messages = data;
                    $scope.showAddRoleButton=false;
                    $scope.addRole=true;
                    $scope.showRoleSelection=false;
                });

        };

        $scope.editCuratorRoleSection = function(loginName, curatorRole) {
            $scope.addRole=true;
            $scope.roleForm.theRole.roleBy='curator';
            $scope.roleForm.theRole.recipient=loginName;
            $scope.showCollaborationGroup=false;
            $scope.showRolecurator=true;
            $scope.showRoleSelection=false;

            for(var key in $scope.csmRoleNames){
                if($scope.csmRoleNames[key] == curatorRole){
                    $scope.roleForm.theRole.roleName = key;
                }
            }
        }

        $scope.editPublicRoleSection = function(groupName, publicRole) {
            $scope.addRole=true;
            $scope.roleForm.theRole.roleBy='public';
            $scope.roleForm.theRole.recipient=groupName;
            $scope.roleForm.theRole.recipientDisplayName=groupName;
            $scope.showCollaborationGroup=true;
            $scope.showRolecurator=false;
            $scope.showRoleSelection=false;

            for(var key in $scope.csmRoleNames){
                if($scope.csmRoleNames[key] == publicRole){
                    $scope.roleForm.theRole.roleName = key;
                }
            }

            if($scope.roleForm.theRole.recipient == 'ROLE_ANONYMOUS') {
                $scope.roleForm.theRole.roleBy='admin';
            }
        }


        $scope.removeRoleSection = function() {
            $scope.userForm.theRole = $scope.roleForm.theRole;
            $scope.addRole=false;
            $scope.showAddRoleButton=true;

            if (confirm("Are you sure you want to delete?")) {
                $scope.loader = true;
                
                if (typeof $scope.userForm.fileId == 'undefined' || $scope.userForm.fileId == null) {
                	$scope.userForm.fileId = '0';
                }

                $http({method: 'POST', url: '/caNanoLab/rest/user/deleteRole',data: $scope.userForm}).
                    success(function(data, status, headers, config) {
                        $scope.userForm = data;
                        $scope.adminRoles = $scope.userForm.adminRoles;
                        $scope.curatorRoles = $scope.userForm.curatorRoles;
                        if( $scope.curatorRoles != null && $scope.curatorRoles.length > 0 ) {
                            $scope.roleExists = true;
                        }
                        if( $scope.adminRoles != null && $scope.adminRoles.length > 1 ) {
                            $scope.roleExists = true;
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
        
        $scope.getCuratorRoleSection = function() {
            return $scope.showRolecurator && $scope.showRoleSelection;
        };

        $scope.getPublicRoleSection = function() {
            return $scope.showCollaborationGroup && $scope.showRoleSelection;
        };

        $scope.selectPublicRole = function() {
            $scope.showCollaborationGroup=true;
            $scope.showRolecurator=false;
            $scope.showRoleSelection=false;
        };

        $scope.selectCuratorRole = function() {
            $scope.showCollaborationGroup=false;
            $scope.showRolecurator=true;
            $scope.showRoleSelection=false;
        };

        $scope.selectAdminRole = function() {
            $scope.roleForm.theRole.recipient = 'ROLE_ANONYMOUS';
            $scope.roleForm.theRole.recipientDisplayName = 'admin';
            $scope.roleForm.theRole.roleName = 'R';
            $scope.showCollaborationGroup=true;
            $scope.showRolecurator=false;
            $scope.showRoleSelection=false;
        };       

        /** End - Role Section **/
    });


