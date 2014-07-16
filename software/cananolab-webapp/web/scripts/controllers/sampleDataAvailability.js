'use strict';
var app = angular.module('angularApp')
	.controller('SampleDataAvailabilityCtrl', function ($rootScope,$scope,$http,$filter,$routeParams,$modalInstance,sampleId,sampleData) {

	$scope.sampleId = sampleId;
	$scope.sampleData = sampleData;
	$scope.availabilityData = sampleData;

	$scope.ok = function () {
		$modalInstance.close();
	};

    });
