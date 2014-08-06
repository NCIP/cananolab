'use strict';
var app = angular.module('angularApp')

  .controller('SampleSearchCtrl', function (sampleService,navigationService,groupService,$rootScope,$scope,$http,$location) {
    $scope.searchSampleForm = {};
    $scope.sampleData = sampleService.sampleData;
    $rootScope.navTree=false;
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();   
    
    $scope.$on('$viewContentLoaded', function(){
      $http({method: 'GET', url: '/caNanoLab/rest/sample/setup'}).
      success(function(data, status, headers, config) {
        $scope.functionalizingEntityTypesList = data.functionalizingEntityTypes;
        $scope.characterizationTypesList = data.characterizationTypes;
        $scope.nanomaterialEntityTypesList = data.nanomaterialEntityTypes;
        $scope.functionTypesList = data.functionTypes;
      }).
      error(function(data, status, headers, config) {
          // called asynchronously if an error occurs
          // or server returns response with an error status.
      $scope.message = data;
      });
    });     

    $scope.setCharacterizationOptionsByCharType = function() {
      $http({method: 'GET', url: '/caNanoLab/rest/sample/getCharacterizationByType',params: {"type":$scope.searchSampleForm.characterizationType}}).
      success(function(data, status, headers, config) {
        $scope.characterizationsList = data;
      }).
      error(function(data, status, headers, config) {
        // called asynchronously if an error occurs
        // or server returns response with an error status.
        $scope.message = data;
      });        
    };
       

    $scope.doSearch = function() {
      $scope.loader = true;

      $http({method: 'POST', url: '/caNanoLab/rest/sample/searchSample',data: $scope.searchSampleForm}).
      success(function(data, status, headers, config) {
        // $rootScope.sampleData = data;
        $scope.sampleData.data = data;
        $location.path("/sampleResults").replace();

      }).
      error(function(data, status, headers, config) {
        // called asynchronously if an error occurs
        // or server returns response with an error status.
        // $rootScope.sampleData = data;
        $scope.loader = false;
        $scope.message = data;
      }); 
    };

    $scope.resetForm = function() {
      $scope.searchSampleForm = {};
      $scope.characterizationsList = [];  
    };

  });
