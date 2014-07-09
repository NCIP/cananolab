'use strict';
var app = angular.module('angularApp')
  .controller('IndSampleCtrl', function ($rootScope,$scope,$http,$filter,$routeParams) {
	var sampleId = $routeParams.sampleId; 
	sampleId = '20917507';
    $scope.$on('$viewContentLoaded', function(){
     $http({method: 'GET', url: '/caNanoLab/rest/sample/view?sampleId=' + sampleId}).
     success(function(data, status, headers, config) {
        $scope.sampleName = data.sampleName;
        $scope.createdDate = $filter('date')(data.createdDate, 'shortDate');
        $scope.keywords = data.keywords;
        $scope.pointOfContactInfo = data.pointOfContactInfo;
        $scope.otherPOCBeans = data.otherPOCBeans;
        }).
        error(function(data, status, headers, config) {
          // called asynchronously if an error occurs
          // or server returns response with an error status.
        $scope.message = data;

         });
    });     

  });
