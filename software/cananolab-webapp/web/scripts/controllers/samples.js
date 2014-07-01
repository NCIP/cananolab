'use strict';
var app = angular.module('angularApp')

  .controller('SampleCtrl', function ($rootScope,$scope,$http) {



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
        $scope.dummy = "asdas";

         });
    });     

    $scope.setCharacterizationOptionsByCharType = function() {
      
      // make rest call with $scope.characterizationType parameter here.
    }
       

  });
