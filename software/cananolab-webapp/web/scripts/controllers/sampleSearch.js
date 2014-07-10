'use strict';
var app = angular.module('angularApp')

  .controller('SampleCtrl', function (navigationFactory,groupsFactory,$rootScope,$scope,$http,$location) {
    $scope.searchSampleForm = {};
    $rootScope.tabs = navigationFactory.query();
    $rootScope.groups = groupsFactory.get();

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
      $scope.mesage = data;
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
        $rootScope.sampleData = data;
        $location.path("/sampleResults").replace();

      }).
      error(function(data, status, headers, config) {
        // called asynchronously if an error occurs
        // or server returns response with an error status.
        $rootScope.sampleData = data;
        $scope.loader = false;
      }); 
    };

    $scope.resetForm = function() {
      $scope.searchSampleForm = {};
      $scope.characterizationsList = [];  
    };

  });
