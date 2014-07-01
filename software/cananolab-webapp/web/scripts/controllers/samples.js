'use strict';
var app = angular.module('angularApp')

  .controller('SampleCtrl', function ($rootScope,$scope,$http) {
    $scope.characterizations = null;


    $scope.$on('$viewContentLoaded', function(){
     $http({method: 'GET', url: '/caNanoLab/rest/sample/setup'}).
     success(function(data, status, headers, config) {
        $scope.functionalizingEntityTypes = data.functionalizingEntityTypes;
        $scope.characterizationTypes = data.characterizationTypes;
        $scope.nanomaterialEntityTypes = data.nanomaterialEntityTypes;
        $scope.functionTypes = data.functionTypes;
        }).
        error(function(data, status, headers, config) {
          // called asynchronously if an error occurs
          // or server returns response with an error status.
        $scope.mesage = data;

         });
    });     

    $scope.setCharacterizationOptionsByCharType = function() {
    
     $http({method: 'GET', url: '/caNanoLab/rest/sample/getCharacterizationByType',params: {"type":$scope.characterizationType}}).
     success(function(data, status, headers, config) {
        $scope.characterizations = data;
        }).
        error(function(data, status, headers, config) {
          // called asynchronously if an error occurs
          // or server returns response with an error status.
          $scope.message = data;
         });        
      // make rest call with $scope.characterizationType parameter here.
    };
       

  });
