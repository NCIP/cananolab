'use strict';
var app = angular.module('angularApp')
  .controller('CharacterizationCtrl', function (sampleService,navigationService, groupService, $rootScope,$scope,$http,$location,$filter,$routeParams) {
    $rootScope.tabs = navigationService.query();
    $rootScope.groups = groupService.get();   
    $scope.sampleData = sampleService.sampleData;
    $scope.sampleId = sampleService.sampleId;

    // Displays left hand nav for samples section. navTree shows nav and navDetail is page index //
    $rootScope.navTree = true;
    $rootScope.navDetail = 2;  

    $scope.goBack = function() {
      $location.path("/sampleResults").replace();      
    };

    $http({method: 'GET', url: 'http://localhost:8080/caNanoLab/rest/sample/characterizationView?sampleId=20917507'}).
    // $http({method: 'GET', url: '/caNanoLab/rest/sample/characterizationView?sampleId=' + $scope.sampleId.data}).
    success(function(data, status, headers, config) {
      $scope.data = data;
    }).
    error(function(data, status, headers, config) {
        // called asynchronously if an error occurs
        // or server returns response with an error status.
        $scope.message = data;

    });


  });

