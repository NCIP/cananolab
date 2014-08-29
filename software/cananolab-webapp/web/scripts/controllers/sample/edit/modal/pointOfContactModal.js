'use strict';
var app = angular.module('angularApp')
	.controller('PointOfContactModalCtrl', function ($scope,$http,$modalInstance,sampleId, originalPoc, sampleData,master, message, type, sampleService) {

    // define variables //
    	$scope.sampleId = sampleId;
        $scope.sampleData = sampleData;
        $scope.poc = sampleService.pocData;
        $scope.other = {'organization':'','role':''};
        $scope.organizations = $scope.sampleData.organizationNamesForUser; $scope.organizations.push('[other]');
        $scope.roles = $scope.sampleData.contactRoles; $scope.roles.push('[other]');
        $scope.message = message;
        $scope.loader = false;
        $scope.master = master;
        $scope.type = type;
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

    // delete the point of contact //
    // opens delete buttons if block is false, otherwise deletes or closes block //
    $scope.delete = function(val,block) {
        $scope.deleteBlock = true;
        // checks if it is a yes or no button or main delete button that calls method //
        if (block) {
            // called if yes button is pressed on delete block //
            if (val) {
                $scope.poc.dirty = true;
                $scope.loader = true;
                $scope.loaderMessage = "Deleting";
                $http({method: 'POST', url: '/caNanoLab/rest/sample/deletePOC',data: $scope.poc}).
                    success(function(data, status, headers, config) {
                        $scope.sampleData = data;
                        $scope.sampleData.message = "Point of contact deleted";

                        $modalInstance.close($scope.sampleData);
                    }).
                    error(function(data, status, headers, config) {
                        $scope.loader = false;
                        $modalInstance.close($scope.sampleData);

                });
            }
            // this is no button. close the block //
            else {
                $scope.deleteBlock = false;
            };
        };
    };

    $scope.savePoc = function() {
        $scope.loader = true;
        $scope.loaderMessage = "Saving";
        sampleService.pocData.dirty = true;
        if(parseInt(sampleService.pocData.id) > 0) {
            originalPoc = sampleService.pocData;
            var index = sampleService.sampleData.pointOfContacts.map(function(x) {return x.id; }).indexOf(sampleService.pocData.id);
            $scope.sampleData.pointOfContacts[index] = originalPoc;
        } 
        else {
            if ($scope.sampleData.pointOfContacts == null) {
                $scope.sampleData.pointOfContacts = [];
            }
            $scope.sampleData.pointOfContacts.push(sampleService.pocData);
        };
        
        $http({method: 'POST', url: '/caNanoLab/rest/sample/savePOC',data: $scope.sampleData}).
            success(function(data, status, headers, config) {
                $scope.sampleData.pointOfContacts = data.pointOfContacts;
                $scope.master = angular.copy($scope.sampleData);
                $scope.loader = false;
                $scope.sampleData = data;
                $scope.sampleData.message = "Point of contact saved";
                $modalInstance.close($scope.sampleData);

            }).
            error(function(data, status, headers, config) {
                $scope.loader = false;
                $scope.sampleData.errors = data;
                // remove if add and there is an error //
                if ($scope.type=='add') {
                    $scope.sampleData.pointOfContacts.pop();
                };
                // alert("fail");
                // $scope.sampleData = data;
                
                // $modalInstance.close($scope.sampleData);

            });
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