'use strict';
var app = angular.module('angularApp')
	.controller('EditCharacterizationModalCtrl', function ($scope,$http,$modalInstance,sampleId, sampleData,message,type, sampleService) {

    // define variables //
    	$scope.sampleId = sampleId;
        $scope.sampleData = sampleData;
        $scope.message = message;
        $scope.loader = false;
        $scope.type = type;

      $http({method: 'GET', url: '/caNanoLab/rest/characterization/setupAdd?sampleId='+$scope.sampleId+'&charType='+$scope.type}).
        success(function(data, status, headers, config) {
            alert("success");
        }).
        error(function(data, status, headers, config) {
          alert("Fail");
        });

    // cancel save //
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

 });