'use strict';
var app = angular.module('angularApp')
	.controller('PointOfContactModalCtrl', function ($rootScope,$scope,$http,$filter,$routeParams,$modalInstance,sampleId,sampleData,poc) {

    $scope.sampleId = sampleId;
    $scope.sampleData = sampleData;
    $scope.pocData = poc;

    $scope.items = items;
    $scope.selected = {
    item: $scope.items[0]
    };

    $scope.ok = function () {
        $modalInstance.close($scope.selected.item);
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

	$scope.ok = function () {
		$modalInstance.close();
	};

});