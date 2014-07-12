'use strict';
var app = angular.module('angularApp')
  .controller('IndSampleCtrl', function (sampleService, navigationService, groupService, $rootScope,$scope,$http,$filter,$routeParams) {
    $rootScope.tabs = navigationService.query();
    $rootScope.groups = groupService.get();   
    $scope.sampleData = sampleService.sampleData;
    $scope.sampleId = sampleService.sampleId;
    // Displays left hand nav for samples section. navTree shows nav and navDetail is page index //
    $rootScope.navTree = true;
    $rootScope.navDetail = 0;

    if ($routeParams.sampleId) {
      $scope.sampleId.data = $routeParams.sampleId;
    }




    $scope.$on('$viewContentLoaded', function(){
     $http({method: 'GET', url: '/caNanoLab/rest/sample/view?sampleId=' + $scope.sampleId.data}).
     success(function(data, status, headers, config) {
      $scope.sampleData = data;
        }).
        error(function(data, status, headers, config) {
          // called asynchronously if an error occurs
          // or server returns response with an error status.
        $scope.message = data;

         });
    });     

  });
