'use strict';
var app = angular.module('angularApp')
	.controller('keywordModalCtrl', function ($rootScope,$scope,$http,$filter,$routeParams,$modalInstance,sampleId,sampleData) {

	$scope.sampleId = sampleId;
	$scope.sampleData = sampleData;

	$scope.ok = function () {
		$modalInstance.close();
	};
});
