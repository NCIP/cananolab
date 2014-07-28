'use strict';
var app = angular.module('angularApp')

  .controller('PublicationSearchCtrl', function (sampleService,navigationService,groupService,$rootScope,$scope,$http,$location) {
    $scope.searchPublicationForm = {};
    $rootScope.navTree=false;
    $rootScope.tabs = navigationService.query();
    $rootScope.groups = groupService.get();   
    
    $scope.$on('$viewContentLoaded', function(){
      $http({method: 'GET', url: 'http://localhost:8080/caNanoLab/rest/publication/setup'}).
      success(function(data, status, headers, config) {
        $scope.data = data;
      }).
      error(function(data, status, headers, config) {
          // called asynchronously if an error occurs
          // or server returns response with an error status.
      $scope.message = data;
      });
    });     

    // $scope.setCharacterizationOptionsByCharType = function() {
    //   $http({method: 'GET', url: '/caNanoLab/rest/sample/getCharacterizationByType',params: {"type":$scope.searchSampleForm.characterizationType}}).
    //   success(function(data, status, headers, config) {
    //     $scope.characterizationsList = data;
    //   }).
    //   error(function(data, status, headers, config) {
    //     // called asynchronously if an error occurs
    //     // or server returns response with an error status.
    //     $scope.message = data;
    //   });        
    // };
       

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
