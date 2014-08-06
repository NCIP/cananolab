/*'use strict';
var app = angular.module('angularApp')
	.controller('PointOfContactModalCtrl', function (sampleService,$rootScope,$scope,$http,$filter,$routeParams,$modalInstance,sampleId,sampleData,poc) {

    $scope.sampleId = sampleId;
    $scope.sampleData = sampleService.sampleData;
    $scope.pocData = poc;

    //$scope.items = items;
    //$scope.selected = {
    //item: $scope.items[0]
    //};

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

	$scope.ok = function () {
		$modalInstance.close();
	};

});
*/
'use strict';
var app = angular.module('angularApp')
	.controller('PointOfContactModalCtrl', function ($scope,$http,$modalInstance,sampleId,poc,sampleService) {

	$scope.sampleId = sampleId;
	$scope.sampleData = sampleService.sampleData;
    $scope.poc = poc;

	$scope.ok = function () {
		$modalInstance.close();
	};
	$scope.save = function () {

		$modalInstance.close();
	};
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

 });