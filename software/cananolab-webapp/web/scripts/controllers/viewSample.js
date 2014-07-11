'use strict';
var app = angular.module('angularApp')
  .controller('IndSampleCtrl', function (navigationFactory, groupsFactory, $rootScope,$scope,$http,$filter,$routeParams) {
    $rootScope.tabs = navigationFactory.query();
    $rootScope.groups = groupsFactory.get();   

    // Displays left hand nav for samples section. navTree shows nav and navDetail is page index //
    $rootScope.navTree = true;
    $rootScope.navDetail = 0;

	var sampleId = $routeParams.sampleId; 
	//sampleId = '20917507';
    //$scope.$on('$viewContentLoaded', function(){
     $http({method: 'GET', url: '/caNanoLab/rest/sample/view?sampleId=' + sampleId}).
     success(function(data, status, headers, config) {
      $scope.sampleData = data;
        }).
        error(function(data, status, headers, config) {
          // called asynchronously if an error occurs
          // or server returns response with an error status.
        $scope.message = data;

         });
    //});     

  });
