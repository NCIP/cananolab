'use strict';
var app = angular.module('angularApp')
    .controller('UserResultsCtrl', function (userService,navigationService,groupService,utilsService,$scope,$rootScope,$filter,ngTableParams,$http,$location) {
        $rootScope.tabs = navigationService.get();
        $rootScope.groups = groupService.getGroups.data.get();
        $scope.userData = userService.userData;
        $scope.utilsService = utilsService;
        $scope.loggedInUser = groupService.getUserName();
        $scope.goBack = function() {
            $location.path("/searchUser").replace();
        };


        var data = $scope.userData.data;
        
        if (data==null) {
            $scope.userData.data = [];
            $location.path("/searchUser").replace();
        }

        $scope.tableParams = new ngTableParams(
            {
                page: 1,            // show first page
                count: 10           // count per page
            },
            {
                counts: [], // hide page counts control

                total: data.length, // length of data
                getData: function($defer, params) {
                    // use build-in angular filter
                    var orderedData = params.sorting() ?
                        $filter('orderBy')(data, params.orderBy()) : data;
                    $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                }
            });

    });
