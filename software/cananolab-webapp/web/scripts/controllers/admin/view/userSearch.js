'use strict';
var app = angular.module('angularApp')

    .controller('UserSearchCtrl', function (userService,navigationService,groupService,$rootScope,$scope,$http,$location) {
        $scope.searchUserForm = {};
        $scope.userData = userService.userData;
        $rootScope.tabs = navigationService.get();
        $rootScope.groups = groupService.getGroups.data.get();

        $scope.$on('$viewContentLoaded', function(){
            $http({method: 'GET', url: '/caNanoLab/rest/user/setup'}).
                success(function(data, status, headers, config) {
                    $scope.data = data;
                    $scope.userTypes = data.userTypes;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $scope.message = data;
                });
        });


        $scope.doSearch = function() {
            $scope.loader = true;
            for (var key in $scope.researchArea) {
                if ($scope.researchArea[key]) {
                    $scope.searchPublicationForm.researchArea.push(key)
                }

            }

            $http({method: 'POST', url: '/caNanoLab/rest/user/searchUser',data: $scope.searchUserForm}).
                success(function(data, status, headers, config) {
                    $scope.userData.data = data;
                    $location.path("/userResults").replace();
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $scope.loader = false;
                    $scope.message = data;
                });
        };

        $scope.resetForm = function() {
            $scope.searchUserForm = {};
        };

    });
