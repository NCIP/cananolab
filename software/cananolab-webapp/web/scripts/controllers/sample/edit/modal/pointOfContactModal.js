'use strict';
var app = angular.module('angularApp')
	.controller('PointOfContactModalCtrl', function ($scope,$http,$modalInstance,sampleId, originalPoc, sampleService) {

	$scope.sampleId = sampleId;
	$scope.sampleData = sampleService.sampleData;
    $scope.poc = sampleService.pocData;
/* Initialize master for poc */
    $scope.master = angular.copy($scope.poc);

    $scope.createDropDownList = function(list){
        var newList = [];
        newList = list;
        // Add [other] to bottom of newList
        newList.push("[other]");
        return newList;
    };
    $scope.findPocIndex = function(){
    // Return integer index of sampleData for poc
    };
/* Initialize selects with createDropDownList()*/
    $scope.organizations = $scope.createDropDownList($scope.sampleData.organizationNamesForUser);
    $scope.roles = $scope.createDropDownList($scope.sampleData.contactRoles);

/* Exit Codes */
	$scope.save = function () {
		$modalInstance.close(originalPoc);
	};
    $scope.cancel = function () {
        $scope.poc = angular.copy($scope.master);  /* Neither is this.  Capture events in editSample and then do what needs to be done to the model. */
        $modalInstance.dismiss('cancel');
    };

 });