'use strict';
var app = angular.module('angularApp')
	.controller('PointOfContactModalCtrl', function ($rootScope,$scope,$http,$filter,$routeParams,$modalInstance,sampleId,sampleData,poc) {

	$scope.sampleId = sampleId;
//	$scope.sampleData = sampleData;
    $scope.sampleData = sampleService.sampleData;

	$scope.pocData = poc;

	$scope.ok = function () {
		$modalInstance.close();
	};

});