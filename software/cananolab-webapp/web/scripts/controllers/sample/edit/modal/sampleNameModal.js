'use strict';
var app = angular.module('angularApp')
	.controller('sampleNameModalCtrl', function ($rootScope,$scope,$http,$filter,$routeParams,$modalInstance,sampleId,sampleData) {

	$scope.sampleId = sampleId;
	$scope.sampleData = sampleData;

	$scope.ok = function () {
		$modalInstance.close();
	};
});
