'use strict';
angular.module('characterizationApp',[
    'ngRoute','ngSanitize'])

.controller('PrintCharacterizationCtrl', function ($rootScope,$scope,$http,$location,$filter,$routeParams) {

    // Displays left hand nav for samples section. navTree shows nav and navDetail is page index //
    $scope.loader = true;
    $http({method: 'GET', url: 'http://localhost:8080/caNanoLab/rest/sample/characterizationView?sampleId=20917507'}).
    // $http({method: 'GET', url: '/caNanoLab/rest/sample/characterizationView?sampleId=' + $scope.sampleId.data}).
    success(function(data, status, headers, config) {
      $scope.data = data;
	  $scope.loader = false;      
    }).
    error(function(data, status, headers, config) {
        // called asynchronously if an error occurs
        // or server returns response with an error status.
        $scope.message = data;
        $scope.loader = false;
    });

  });