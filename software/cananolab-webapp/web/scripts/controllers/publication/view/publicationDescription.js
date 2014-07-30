'use strict';
var app = angular.module('angularApp')
	.controller('PublicationDescriptionCtrl', function ($rootScope,$scope,$http,$filter,$routeParams,$modalInstance,description) {
	$scope.description=description;
	$scope.ok = function () {
		$modalInstance.close();
	};

    });
