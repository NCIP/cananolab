'use strict';
var app = angular.module('angularApp')

    .controller('EditUserCtrl', function (navigationService,groupService,$rootScope,$scope,$http,$location,$timeout,$routeParams,$upload) {
        $scope.userForm = {};
        $scope.userForm.roles = [];
        $scope.userRoles = [];
        $scope.userTag = $routeParams.username;
        $scope.messages = '';

        $scope.loadUserData = function() {
            if( $scope.userTag != null ) {
                $scope.loader = true;
                $http({method: 'GET', url: '/caNanoLab/rest/useraccount/read?username=' + $scope.userTag}).
                success(function(data, status, headers, config) {
                    var update = data;
                    delete update.accountNonExpired;
                    delete update.accountNonLocked;
                    delete update.admin;
                    delete update.authorities;
                    delete update.credentialsNonExpired;
                    delete update.curator;
                    delete update.displayName;
                    delete update.groups;
                    delete update.password;
                    delete update.public;
                    delete update.researcher;

                    if ($rootScope.msgDisplay!=undefined) {
                        if (!$rootScope.msgDisplay.fromCreate) {
                            delete $rootScope.msgDisplay;
                        }
                        else {
                            $rootScope.msgDisplay.fromCreate = false;
                        }
                    }

                    $scope.userForm = data;

                    if (data.roles.indexOf("ROLE_CURATOR")!=-1) {
                        $scope.userRoles[0] = "ROLE_CURATOR";
                    }

                    if (data.roles.indexOf("ROLE_RESEARCHER")!=-1) {
                        $scope.userRoles[1] = "ROLE_RESEARCHER";
                    }

                    if (data.roles.indexOf("ROLE_ADMIN")!=-1) {
                        $scope.userRoles[2] = "ROLE_ADMIN";
                    }

                    $scope.loader = false;

                }).
                error(function(data, status, headers, config) {
                });
            };
        };

        $scope.loader = false;     

        $scope.doSubmitData = function() {
            $scope.loader = true;

            var roles = [];
            angular.forEach($scope.userRoles,function(item) {
                if (item) {
                    roles.push(item);
                }
            });
            $scope.userForm.roles = roles;

            var enabled = true;
            $scope.userForm.enabled = enabled;

            $http({method: 'POST', url: '/caNanoLab/rest/useraccount/create',data: $scope.userForm}).
                success(function(data, status, headers, config) {
                    $scope.loader = false;
                    	var msg = 'User successfully saved with username "' + $scope.userForm.username + '"';
                        $rootScope.msgDisplay = {msg: msg, fromCreate: true};
                        $location.path("/editUser").search({username: $scope.userForm.username}).replace();
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $scope.loader = false;
                    $scope.messages = 'Error creating new user with username "' + $scope.userForm.username + '"';
                });
        };

        $scope.doUpdateData = function() {
            $scope.loader = true;
            var roles = [];
            angular.forEach($scope.userRoles,function(item) {
                if (item) {
                    roles.push(item);
                }
            });
            $scope.userForm.roles = roles;

            var ndx = roles.indexOf("ROLE_ANONYMOUS");
                if (ndx > -1) {
                    roles.splice(ndx, 1);
                }

            $http({method: 'POST', url: '/caNanoLab/rest/useraccount/update',data: $scope.userForm}).
                success(function(data, status, headers, config) {
                    $scope.loader = false;
                        var msg = 'User successfully updated with username "' + $scope.userForm.username + '"';
                        $scope.messages = msg;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $scope.loader = false;
                    $scope.messages = 'Error updating user with username "' + $scope.userForm.username + '"';
                });
        };

        $scope.doResetPwd = function() {
            $scope.loader = true;
            $scope.resetPwd.username = $scope.userForm.username;
            $http({
                    method: 'POST',
                    url: '/caNanoLab/rest/useraccount/resetpwd',
                    data: $scope.resetPwd,
                    transformRequest: function(obj) {
                        var str = [];
                        for(var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    },
                    headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                    }
                    }).
                    success(function(data, status, headers, config) {
                        $scope.loader = false;
                            $location.path("/userResults").replace();
                    }).
                    error(function(data, status, headers, config) {
                        // called asynchronously if an error occurs
                        // or server returns response with an error status.
                        $scope.loader = false;
                        $scope.messages = 'Error resetting password for user with username "' + $scope.userForm.username + '"';
                    });
        };

        $scope.cancelResetPwd = function() {
            $location.path("/userResults").replace();
        };

        $scope.resetForm = function() {
            $scope.userForm = {};
            $scope.defineRoleVariables();
        };
       
        $scope.goBack = function() {
            $location.path("/userResults").replace();
            $location.search('userTag', null);
        };

        $scope.loadUserData();
    });
