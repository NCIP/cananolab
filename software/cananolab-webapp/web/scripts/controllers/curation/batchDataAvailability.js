'use strict';
var app = angular.module('angularApp')

    .controller('BatchDataAvailabilityCtrl', function (navigationService,groupService,$rootScope,$scope,$http,$location) {
        $rootScope.navTree=false;
        $rootScope.tabs = navigationService.get();
        $rootScope.groups = groupService.getGroups.data.get();

        $scope.generateBatchDataAvailabilityForm = {};
        $scope.generateBatchDataAvailabilityForm.option = 'generate all';


        $scope.doSubmit = function() {
            $scope.loader = true;

             $http({method: 'POST', url: '/caNanoLab/rest/curation/generateBatchDataAvailability',data: $scope.generateBatchDataAvailabilityForm}).
             success(function(data, status, headers, config) {
                 $scope.loader = false;
                 $rootScope.tabs = navigationService.get();
                 $scope.messages = data;
             }).
             error(function(data, status, headers, config) {
                 // called asynchronously if an error occurs
                 // or server returns response with an error status.
                 // $rootScope.sampleData = data;
                 $scope.loader = false;
                 $scope.messages = data;
             });
        };

        $scope.resetForm = function() {
            $scope.generateBatchDataAvailabilityForm = {};
        };
    });
