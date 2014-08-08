'use strict';
angular.module('angularApp')
    .controller('CloneSampleCtrl', function (navigationService,groupService,$rootScope,$scope,$http,$filter,$routeParams,$location) {
        $scope.cloneForm = {};
        $scope.showSearchSampleTable = false;
        $rootScope.tabs = navigationService.get();
        $rootScope.groups = groupService.getGroups.data.get();

        var sampleName = $routeParams.sampleName;
        $scope.cloneForm.sampleName = sampleName;

        $scope.searchSamples = function() {
           /* $http({method: 'GET', url: '/caNanoLab/rest/publication/getSamples?searchStr='}).
                success(function(data, status, headers, config) {
                    $scope.sampleResults = data;
                }).
                error(function(data, status, headers, config) {
                    $scope.message = data;
                }); */

            $scope.sampleResults = ["GATECH_UCSF-EDickersonCL2008-01","NCL-16","NCL-17","NCL-19","NCL-20-1","NCL-21-1","NCL-22-1","NCL-23-1","NCL-24-1","NCL-25-1","NCL-26-1","NCL-42","NCL-45","NCL-48","NCL-48-4","NCL-49","NCL-49-2","NCL-50-1","NCL-51-3","NCL-MGelderman-IJN2008-01","NCL-MGelderman-IJN2008-02","UMC_HSTVAMC_NCL_NB-NChandaNNBM2010-01","UMC_HSTVAMC_NCL_NB-NChandaNNBM2010-02"];

        };

        $scope.updateCloningSample = function() {
            $scope.cloneForm.sampleName = $scope.matchedSampleSelect;
            $scope.showSearchSampleTable = false;
        };

        $scope.doSubmit = function() {
            $scope.loader = true;

            $http({method: 'POST', url: '/caNanoLab/rest/sample/copySample',data: $scope.cloneForm}).
                success(function(data, status, headers, config) {
                     $location.search('sampleId',data.sampleId).path('/editSample').replace();
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    // $rootScope.sampleData = data;
                    $scope.loader = false;
                    $scope.messages = data;
                });

        };

    });
