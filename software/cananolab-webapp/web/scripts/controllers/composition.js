'use strict';
var app = angular.module('angularApp')
  .controller('CompositionCtrl', function (sampleService,navigationService, groupService, $rootScope,$scope,$http,$filter,$routeParams) {
    $rootScope.tabs = navigationService.query();
    $rootScope.groups = groupService.get();   
    $scope.sampleId = sampleService.sampleId;

    // Displays left hand nav for samples section. navTree shows nav and navDetail is page index //
    $rootScope.navTree = true;
    $rootScope.navDetail = 1; 
    $scope.sampleData = sampleService.sampleData;

  });
