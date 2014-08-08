'use strict';
var app = angular.module('angularApp')
	.controller('PointOfContactModalCtrl', function ($scope,$http,$modalInstance,sampleId, originalPoc, sampleData,sampleService) {

	$scope.sampleId = sampleId;
    $scope.sampleData = sampleData;
    $scope.poc = sampleService.pocData;
    $scope.other = {'organization':'','role':''};
    $scope.organizations = $scope.sampleData.organizationNamesForUser;$scope.organizations.push('[other]');
    $scope.roles = $scope.sampleData.contactRoles;$scope.roles.push('[other]');
    /* Initialize master for poc */
    $scope.master = angular.copy($scope.poc);

    // Fired when organization or role are changed, used when user selects other //
    $scope.organizationDropdownChanged = function() {
    if ($scope.poc.organization.name=='[other]'|| $scope.poc.role=='[other]') {
        //check if organization or role are other. if so set other text to show //
        $scope.otherRow = true;
        if ($scope.poc.organization.name=='[other]') { $scope.organizationOther = true; }
        else { $scope.organizationOther = false; }

        if ($scope.poc.role=='[other]') { $scope.roleOther = true; }
        else { $scope.roleOther = false; };            
    }
    else { $scope.other = false; }
    };

    $scope.savePoc = function() {

    };

    // save the point of contact //
	$scope.save = function () {
        // check if organization and role are other. if so set name to other instead of dropdown values //
        if ($scope.other.organization) { 
            $scope.poc.organization.name = $scope.other.organization;
            alert($scope.other.organization);
        }
        if ($scope.other.role) {
            $scope.poc.role = $scope.other.role;
        } 

        alert("DO REST CALL HERE  in another function");
        $modalInstance.close();
	};

    // cancel save //
    $scope.cancel = function () {
        $scope.poc = angular.copy($scope.master);  /* Neither is this.  Capture events in editSample and then do what needs to be done to the model. */
        $modalInstance.dismiss('cancel');
    };

 });