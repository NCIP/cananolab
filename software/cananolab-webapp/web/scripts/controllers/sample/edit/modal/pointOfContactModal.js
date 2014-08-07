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

    // Fired when organization or role are chnaged, used when user selects other //
    $scope.organizationDropdownChanged = function() {
        if ($scope.poc.organization.name=='[other]'|| $scope.poc.role=='[other]') {
            $scope.other = true;
            if ($scope.poc.organization.name=='[other]') {
                $scope.organizationOther = true;
            }
            else {
                $scope.organizationOther = false;
            }
            if ($scope.poc.role=='[other]') {
                $scope.roleOther = true;
            }
            else {
                $scope.roleOther = false;
            }            
        }
        else {
            $scope.other = false;
        }
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