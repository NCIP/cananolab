'use strict';
var app = angular.module('angularApp')
	.controller('PointOfContactModalCtrl', function ($scope,$http,$modalInstance,sampleId,poc,sampleService) {

	$scope.sampleId = sampleId;
	$scope.sampleData = sampleService.sampleData;
    $scope.poc = poc;
/* Initialize master for poc */
    $scope.master = angular.copy(poc);

    $scope.createDropDownList = function(list){
        var newList = [];
        newList = list;
        // Add [other] to bottom of newList
        newList.push("[other]");
        return newList;
    };
    $scope.findPocIndex = function(poc){
    // Return integer index of sampleData for poc
    };
/* Initialize selects with createDropDownList()*/
    $scope.organizations = $scope.createDropDownList($scope.sampleData.organizationNamesForUser);
    $scope.roles = $scope.createDropDownList($scope.sampleData.contactRoles);

/* Exit Codes */
	$scope.ok = function () {
        //var pocIndex = $scope.findPocIndex(poc);
        $scope.poc = angular.copy($scope.master); /* This isn't working */
		$modalInstance.close();
	};
	$scope.save = function () {

		$modalInstance.close();
	};
    $scope.cancel = function () {
        $scope.poc = angular.copy($scope.master);  /* Neither is this.  Capture events in editSample and then do what needs to be done to the model. */
        $modalInstance.dismiss('cancel');
    };

 });