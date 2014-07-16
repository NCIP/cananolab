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
    $scope.sampleData = sampleService.sampleData;

    $scope.goBack = function() {
      $location.path("/sampleResults").replace();      
    };

  });

