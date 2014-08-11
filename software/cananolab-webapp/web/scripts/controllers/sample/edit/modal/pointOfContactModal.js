'use strict';
var app = angular.module('angularApp')
	.controller('PointOfContactModalCtrl', function ($scope,$http,$modalInstance,sampleId, originalPoc, sampleData,master,sampleService) {

    // define variables //
    	$scope.sampleId = sampleId;
        $scope.sampleData = sampleData;
        $scope.poc = sampleService.pocData;
        $scope.other = {'organization':'','role':''};
        $scope.organizations = $scope.sampleData.organizationNamesForUser; $scope.organizations.push('[other]');
        $scope.roles = $scope.sampleData.contactRoles; $scope.roles.push('[other]');
        $scope.message = "";
        $scope.loader = false;
        $scope.master = master;


    /* Initialize master for poc */
    $scope.master = angular.copy($scope.poc);

    // Fired when organization or role are changed, used when user selects other //
    $scope.organizationDropdownChanged = function() {
        if ($scope.poc.organization.name=='[other]' || $scope.poc.role=='[other]') {
            //check if organization or role are other. if so set other text to show //
            $scope.otherRow = true;
            if ($scope.poc.organization.name=='[other]') { $scope.organizationOther = true; }
            else { $scope.organizationOther = false; }

            if ($scope.poc.role=='[other]') { $scope.roleOther = true; }
            else { $scope.roleOther = false; };            
        }
        else { $scope.otherRow = false; }
    };

    $scope.savePoc = function() {
        $scope.loader = true;

        $scope.message = "";
        sampleService.pocData.dirty = true;
        if(parseInt(sampleService.pocData.id) > 0) {
            originalPoc = sampleService.pocData;
            var index = sampleService.sampleData.pointOfContacts.map(function(x) {return x.id; }).indexOf(sampleService.pocData.id);
            $scope.sampleData.pointOfContacts[index] = originalPoc;
        } 
        else {
            $scope.sampleData.pointOfContacts.push(sampleService.pocData);
        };
        
        $http({method: 'POST', url: '/caNanoLab/rest/sample/savePOC',data: $scope.sampleData}).
            success(function(data, status, headers, config) {
                $scope.sampleData.pointOfContacts = data.pointOfContacts;
                $scope.master = angular.copy($scope.sampleData);
                $scope.loader = false;
                $modalInstance.close();

            }).
            error(function(data, status, headers, config) {
                $scope.loader = false;
            $scope.message = data;
            });
            alert("Adding contact again on edit")
    };

    // save the point of contact //
	$scope.save = function () {
        $scope.loader = true;
        // check if organization and role are other. if so set name to other instead of dropdown values //
        if ($scope.other.organization && $scope.poc.organization.name=='[other]') { 
            // $scope.organizationOther = false;
            $scope.poc.organization.name = $scope.other.organization;
        }
        if ($scope.other.role && $scope.poc.role=='[other]') {
            $scope.poc.role = $scope.other.role;
        } 
        $scope.savePoc();
	};

    // cancel save //
    $scope.cancel = function () {
        $scope.poc = angular.copy($scope.master);  /* Neither is this.  Capture events in editSample and then do what needs to be done to the model. */
        $modalInstance.dismiss('cancel');
    };

 });