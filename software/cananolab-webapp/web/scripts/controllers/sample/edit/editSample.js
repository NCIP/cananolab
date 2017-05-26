'use strict';

var app = angular.module('angularApp')
.controller('editSampleCtrl', function (sampleService,navigationService,groupService,$scope,$rootScope,$filter,ngTableParams,$http,$location,$modal,$routeParams) {
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();
    $scope.sampleResultData =sampleService.sampleData;

    $scope.sampleData = {};
    $scope.reviewBean = {}; 
    $scope.sampleMessage = sampleService.message;
    $scope.submitForReviewButton=1;
    $scope.sampleId = sampleService.sampleId;
    $scope.pocData = sampleService.pocData;
    $scope.scratchPad = sampleService.scratchPad;
    $scope.master = {};
    $scope.newKeyword = "";
    $scope.message = "";
    // Access variables
    $scope.csmRoleNames = {"R":"READ","RWD":"READ WRITE DELETE"};
    $scope.sampleData.theAccess = {};
    $scope.accessForm = {};
    $scope.accessForm.theAcccess = {};
    $scope.isCurator = groupService.isCurator();
    $scope.groupAccesses = [];
    $scope.userAccesses = [];
    $scope.addAccess = false;
    $scope.showAddAccessButton = true;
    $scope.showCollaborationGroup = true;
    $scope.showAccessuser = false;
    $scope.showAccessSelection = false;
    $scope.accessForm.theAccess = {};
    $scope.accessForm.theAccess.recipient = '';
    $scope.accessForm.theAccess.recipientDisplayName = '';
    $scope.access = {};
    $scope.access.recipient = '';
    $scope.access.recipientDisplayName = '';
    $scope.sampleData.isPublic = false;
    $scope.accessForm.theAccess.accessType = 'group';        
    $scope.accessExists = false;    

    var editSampleData = {"editSampleData":{"dirty": false}};
    $scope.scratchPad = editSampleData;

    $scope.editSampleForm = false;


    if ($routeParams.isAdvancedSearch) {
      $scope.isAdvancedSearch = 1;
    }; 

    //goBack
    //Change location if user hits the Back button
    $scope.goBack = function() {
        if ($rootScope.fromCollab!=undefined && $rootScope.fromCollab) {
            $rootScope.fromCollab = false;
            $location.path("/collaborationGroup").replace();
            return;
        }
                    console.log($routeParams);

        if ($scope.updateButton=='Save') {
            console.log("Save");
            $location.path("/manageSamples").replace();
            $location.search('sampleId', null);
        }
        else if($scope.updateButton=='Update' && $routeParams.fromMyWorkspace=='true') {
            console.log("update");
            $location.path("/myWorkspace").replace();
            $location.search('sampleId', null);
            $location.search('fromMyWorkspace', null);
        }
        else {
            if ($scope.isAdvancedSearch) {
            	console.log("isAdvanced");
            	$location.path("/advancedSampleResults").replace();           
            }
            else if ($routeParams.fromFavorites=='true') {
            	console.log("myFavorites");
            	$location.path("/myFavorites").replace();           
            }        
            else if ($routeParams.fromKeyword=='true') {
            	console.log("keywordSearch");
            	$location.path("/keywordSearchResults").replace();           
            }         
            else {
            	console.log("sample Results");
            	$location.path("/sampleResults").replace();           
            }
        }
        $location.search('sampleId', null);

    };

    //Get URL params
    if ($routeParams.sampleId) {
      $scope.sampleId.data = $routeParams.sampleId;

    } else {
        $scope.sampleId.data = null;
    }
     

    $scope.returnUserReadableBoolean = function(val) {
        if (val== true) {
            return "Yes";
        }
        return "No";
    }

    // if sampleid exists do initial loading of rest data for sample, else this is new sample //
    
    if ($scope.sampleId.data != null) {
        $scope.updateButton = "Update";
        $scope.loader = true;
        $scope.loaderText = "Loading";

        $http({method: 'GET', url: '/caNanoLab/rest/sample/edit?sampleId='+$scope.sampleId.data}).
            success(function(data, status, headers, config, statusText) {
                $scope.sampleData = data;
                $scope.sampleName = sampleService.sampleName($scope.sampleId.data);

                $scope.loader = false;
                $scope.editSampleForm = true;
                
                $scope.groupAccesses = $scope.sampleData.groupAccesses;
                $scope.userAccesses = $scope.sampleData.userAccesses;
                
                if( $scope.userAccesses != null && $scope.userAccesses.length > 0 ) {
                	$scope.accessExists = true;
                }  
                
                if( $scope.groupAccesses != null && $scope.groupAccesses.length > 1 ) {
                	$scope.accessExists = true;
                }
            }).
            error(function(data, status, headers, config, statusText) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
                if(status != 200){
                    $scope.message = "Response code " + status.toString() + ":  " + statusText;
                } else {
                    $scope.message = data;
                }
                $scope.sampleName = sampleService.sampleName($scope.sampleId.data);
                
                $scope.loader = false;
                $scope.submissionView = false;
        });
    }
    else {
        $scope.editSampleForm = true;
        $scope.loaderText = "Loading";
        $scope.updateButton = "Save";
        $scope.loader = true;
        $http({method: 'GET', url: '/caNanoLab/rest/sample/submissionSetup'}).
            success(function(data, status, headers, config, statusText) {
                $scope.sampleData = data;
                $scope.loader = false;
            }).
            error(function(data, status, headers, config, statusText) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
                var path = $location.path();
                $location.path("/login").search({'came_from':path}).replace();
                $scope.loader = false;
        });

    };

    // * Page Change Events *
    //Add keyword
    $scope.addKeyword=function(){
        if($scope.newKeyword.length > 1) {
            $scope.sampleData.keywords.push($scope.newKeyword.toUpperCase());
            $scope.newKeyword = "";
            $scope.scratchPad.editSampleData.dirty = true;
        }
    };

    $scope.removeKeyword = function(item) {
        var index = $scope.sampleData.keywords.indexOf(item)
        $scope.sampleData.keywords.splice(index, 1);
        $scope.scratchPad.editSampleData.dirty = true;
    }

    $scope.changedSampleName = function() {
        $scope.scratchPad.editSampleData.dirty = true;
    };

    $scope.copy = function() {
        //Submit a copy of sampleName to cloneSample url //
        $location.path("/cloneSample").search({'sampleName':$scope.sampleData.sampleName}).replace();
    };

    $scope.delete = function() {
            // Delete sample, send back to results //        

            var modalHtml = '<div class="modal-header">Are you sure you wish to delete this sample?</div>';
            modalHtml+= '<div class="modal-body"><button ng-disabled="loader" style="margin-right:50px !important;" ng-click="closeModal(1)" class="btn btn-primary btn-sm">Yes</button>';
            modalHtml+= '<button ng-disabled="loader" ng-click="closeModal(0)" class="btn btn-primary  btn-sm">No</button></div>';
            modalHtml+='<div id="loader" style="top:0px;left:30px;" ng-show="loader"><div id="loaderText">Deleting sample</div><div id="loaderGraphic"></div></div>';        
            var modalInstance = $modal.open({
            template:modalHtml,
            size:"sm",
            controller:"modalCtrl",
            resolve: {
                id: function() {
                    return $scope.sampleId.data;
                }
            }
        });

        modalInstance.result.then(function(selection) {
            if (selection) {
                $scope.loader = true;
                $scope.loaderText = "Deleting Sample";

                $http({method: 'GET', url: '/caNanoLab/rest/sample/deleteSample',params: {"sampleId":$scope.sampleData.sampleId}}).
                success(function(data, status, headers, config) {
                    $scope.sampleMessage.data = data;
                    var b = $scope.sampleResultData['data'];
                    if (b) {
                        for (var g = 0;g<b.length;g++) {
                          if (b[g].sampleId==$scope.sampleId.data) {
                            $scope.sampleResultsData = b.splice(g,1);
                          };
                        };
                     }  
                    if($routeParams.fromMyWorkspace=='true') {
                        $location.path("/myWorkspace").replace();
                        $location.search('sampleId', null);
                        $location.search('fromMyWorkspace', null); 
                        }
                	else {
                		$location.path("/sampleDelete").replace();
                	}
                }).
                error(function(data, status, headers, config) {
                    $scope.loader = false;
                    $scope.message = data;
                }); 
            };
        });       
    };    

    $scope.reset = function() {
        $scope.loader = true;
         $http({method: 'GET', url: '/caNanoLab/rest/sample/submissionSetup'}).
             success(function(data, status, headers, config, statusText) {
                 $scope.sampleData = data;
                 $scope.loader = false;
             }).
             error(function(data, status, headers, config, statusText) {
                 // called asynchronously if an error occurs
                 // or server returns response with an error status.
                 var path = $location.path();
                 $location.path("/login").search({'came_from':path}).replace();
                 $scope.loader = false;
         });
 
    };

    $scope.submitSample = function() {
        $scope.loader = true;
        $scope.loaderText = "Saving Sample";
        $http({method: 'POST', url: '/caNanoLab/rest/sample/submitSample',data: $scope.sampleData}).
        success(function(data, status, headers, config) {            
            $scope.sampleData.pointOfContacts = data.pointOfContacts;
            $scope.master = angular.copy($scope.sampleData);
            $scope.scratchPad.editSampleData.dirty = false;
            $scope.loader = false;
            $location.path("/editSample").search({'sampleId':data.sampleId}).replace();
        }).
        error(function(data, status, headers, config) {
            $scope.loader = false;
            $scope.sampleData.errors = data;
        });


    };

    $scope.submitForReview = function() {
        $scope.reviewBean.dataId=$scope.sampleData.sampleId;
        $scope.reviewBean.dataName=$scope.sampleData.sampleName;
        $scope.reviewBean.dataType='sample';
        $scope.submitForReviewButton = 0;
        $scope.loader = true;
        $scope.loaderText = "Submitting sample for review";
        $http({method: 'POST', url: '/caNanoLab/rest/sample/submitForReview',data: $scope.reviewBean}).
        success(function(data, status, headers, config) {            
            $scope.message = data;
            $scope.loader = false;
        }).
        error(function(data, status, headers, config) {
            $scope.loader = false;
            $scope.sampleData.errors = data;
        });        
    };    

    $scope.update = function() {
        $scope.loader = true;
        $scope.loaderText = "Saving Sample";
        $http({method: 'POST', url: '/caNanoLab/rest/sample/updateSample',data: $scope.sampleData}).
        success(function(data, status, headers, config) {            
            $scope.sampleData.pointOfContacts = data.pointOfContacts;
            $scope.master = angular.copy($scope.sampleData);
            $scope.scratchPad.editSampleData.dirty = false;
            $scope.loader = false;
        }).
        error(function(data, status, headers, config) {
            $scope.loader = false;
            $scope.sampleData.errors = data;
        });


    };

    // Modal for Access To Sample (1)
    $scope.openPointOfContactModal = function(sampleId, poc,type) {
        sampleService.sampleData = angular.copy($scope.sampleData);
        sampleService.pocData = angular.copy(poc);

        $scope.pocData = poc.data;
        var modalInstance = $modal.open({
          templateUrl: 'views/sample/edit/modal/pointOfContactModal.html',
          controller: 'PointOfContactModalCtrl',
          windowClass: 'pointOfContact-modal-window',
          resolve: {
            sampleId: function () {
              return sampleId;
            },
            sampleData: function () {
              return $scope.sampleData;
            },            
            originalPoc: function () {
              return poc;
            },
            type: function () {
              return type;
            },            
            master: function() {
                return $scope.master;
            },
            message: function() {
                return $scope.message;
            }    
          }
        });

        modalInstance.result.then(function (sampleData) {
            $scope.sampleData = sampleData;
            // $scope.message = $scope.sampleData.message;
            $scope.groupAccesses = $scope.sampleData.groupAccesses;
            $scope.userAccesses = $scope.sampleData.userAccesses;
            
            if( $scope.userAccesses != null && $scope.userAccesses.length > 0 ) {
            	$scope.accessExists = true;
            }  
            
            if( $scope.groupAccesses != null && $scope.groupAccesses.length > 1 ) {
            	$scope.accessExists = true;
            }            
        });
    };

    // generates data availability //
    $scope.generateDataAvailability = function(sampleId) {
        $scope.generateLoader = true;

          $http({method: 'GET', url: '/caNanoLab/rest/sample/regenerateDataAvailability',params: {"sampleId":sampleId}}).
            success(function(data, status, headers, config) {
                // $scope.accessUsers = data;
                $scope.sampleData = data;
                $scope.generateLoader = false;
            }).
            error(function(data, status, headers, config) {
                $scope.message = data;
                $scope.generateLoader = false;                
            });        

    };

    // Modal for Data Availability //
    $scope.openDataAvailability = function(sampleId) {
          $http({method: 'GET', url: '/caNanoLab/rest/sample/viewDataAvailability',params: {"sampleId":sampleId}}).
          success(function(data, status, headers, config) {
            var modalInstance = $modal.open({
              templateUrl: 'views/sample/view/sampleDataAvailability.html',
              controller: 'SampleDataAvailabilityCtrl',
              size: 'sm',
              resolve: {
                sampleId: function () {
                  return sampleId;
                },
                availabilityData: function() {
                  return data;
                },
                sampleData: function() {
                  return $scope.sampleData;
                },                
                edit: function() {
                  return 1;
                }                
              }
            });
            modalInstance.result.then(function(data) {
                $scope.sampleData = data;
            });
          }).
          error(function(data, status, headers, config) {
            $scope.message = data;
          });


    };
    
    
    /** Start - Access functions **/
    
    $scope.getCollabGroups = function() {
        if ($scope.accessForm.theAccess.recipient === undefined || $scope.accessForm.theAccess.recipient === null) {
            $scope.accessForm.theAccess.recipient = '';
            $scope.accessForm.theAccess.recipientDisplayName = '';
        }

        $scope.loader = true;
        $http({method: 'GET', url: '/caNanoLab/rest/core/getCollaborationGroup?searchStr=' + $scope.accessForm.theAccess.recipient}).
            success(function(data, status, headers, config) {
                $scope.collabGroups = data;
                $scope.loader = false;
            }).
            error(function(data, status, headers, config) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
                $scope.message = data;
                $scope.loader = false;
            });
        $scope.showAccessSelection=true;

    };

    $scope.getAccessUsers = function() {
        if ($scope.accessForm.theAccess.recipient === undefined || $scope.accessForm.theAccess.recipient === null) {
            $scope.accessForm.theAccess.recipient = '';
            $scope.accessForm.theAccess.recipientDisplayName = '';
        }
        
        $scope.loader = true;
        $http({method: 'GET', url: '/caNanoLab/rest/core/getUsers?searchStr=' + $scope.accessForm.theAccess.recipient}).
            success(function(data, status, headers, config) {
                $scope.accessUsers = data;
                $scope.loader = false;
            }).
            error(function(data, status, headers, config) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
                $scope.message = data;
                $scope.loader = false;
            });
        $scope.showAccessSelection=true;

    };

    $scope.hideAccessSection = function() {
        $scope.addAccess=false;
        $scope.showAddAccessButton=true;
    }
    
    $scope.saveAccessSection = function() {
        $scope.loader = true;
        $scope.sampleData.theAccess = $scope.accessForm.theAccess;
        $scope.addAccess=false;
        $scope.showAddAccessButton=true;
        
        if( $scope.accessForm.theAccess.accessType == 'public') {
            $scope.sampleData.isPublic = true;
        }

        $http({method: 'POST', url: '/caNanoLab/rest/sample/saveAccess',data: $scope.sampleData}).
            success(function(data, status, headers, config) {            	
            	$scope.sampleData = data;
            	
            	$scope.groupAccesses = $scope.sampleData.groupAccesses;
                $scope.userAccesses = $scope.sampleData.userAccesses;
                
                $scope.loader = false;
                $scope.accessExists = true;
            }).
            error(function(data, status, headers, config) {
                $scope.loader = false;
                $scope.messages = data;
                $scope.showAddAccessButton=false;
                $scope.addAccess=true;
                $scope.showAccessSelection=false;                
            });
        
    }; 
    
    $scope.editUserAccessSection = function(loginName, userAccess) {
        $scope.addAccess=true;
        $scope.accessForm.theAccess.accessType='user';
        $scope.accessForm.theAccess.recipient=loginName;
        $scope.accessForm.theAccess.recipientDisplayName=loginName;
        $scope.showCollaborationGroup=false;
        $scope.showAccessuser=true;
        $scope.showAccessSelection=false;

        for(var key in $scope.csmRoleNames){
            if($scope.csmRoleNames[key] == userAccess){
                $scope.accessForm.theAccess.roleName = key;
            }
        }
    }

    $scope.editGroupAccessSection = function(groupName, groupAccess) {
        $scope.addAccess=true;
        $scope.accessForm.theAccess.accessType='group';
        $scope.accessForm.theAccess.recipient=groupName;
        $scope.accessForm.theAccess.recipientDisplayName=groupName;
        $scope.showCollaborationGroup=true;
        $scope.showAccessuser=false;
        $scope.showAccessSelection=false;

        for(var key in $scope.csmRoleNames){
            if($scope.csmRoleNames[key] == groupAccess){
                $scope.accessForm.theAccess.roleName = key;
            }
        }
        
        if($scope.accessForm.theAccess.recipient == 'ROLE_ANONYMOUS') {
            $scope.accessForm.theAccess.accessType='public';
        }
    }

    
    $scope.removeAccessSection = function() {
        $scope.sampleData.theAccess = $scope.accessForm.theAccess;
        $scope.addAccess=false;
        $scope.showAddAccessButton=true;
        
        console.log($scope.sampleData.theAccess);
        
        if (confirm("Are you sure you want to delete?")) {
            $scope.loader = true;
        
            $http({method: 'POST', url: '/caNanoLab/rest/sample/deleteAccess',data: $scope.sampleData}).
                success(function(data, status, headers, config) {            	
                	$scope.sampleData = data;
                	
                	$scope.groupAccesses = $scope.sampleData.groupAccesses;
                    $scope.userAccesses = $scope.sampleData.userAccesses;
                    
                    if( $scope.userAccesses != null && $scope.userAccesses.length > 0 ) {
	                	$scope.accessExists = true;
	                }
                    
	                if( $scope.groupAccesses != null && $scope.groupAccesses.length > 1 ) {
	                	$scope.accessExists = true;
	                }
                    
                    $scope.loader = false;
                }).
                error(function(data, status, headers, config) {
                    $scope.loader = false;
                    $scope.messages = data;
                });
        }
        
    };   
    
    $scope.getUserAccessSection = function() {
        return $scope.showAccessuser && $scope.showAccessSelection;
    };

    $scope.getCollabAccessSection = function() {
        return $scope.showCollaborationGroup && $scope.showAccessSelection;
    };

    $scope.selectCgAccess = function() {
        $scope.showCollaborationGroup=true;
        $scope.showAccessuser=false;
        $scope.showAccessSelection=false;
    };

    $scope.selectUserAccess = function() {
        $scope.showCollaborationGroup=false;
        $scope.showAccessuser=true;
        $scope.showAccessSelection=false;
    };

    $scope.selectPublicAccess = function() {
        $scope.accessForm.theAccess.recipient = 'ROLE_ANONYMOUS';
        $scope.accessForm.theAccess.recipientDisplayName = 'Public';
        $scope.accessForm.theAccess.roleName = 'R';
        $scope.showCollaborationGroup=true;
        $scope.showAccessuser=false;
        $scope.showAccessSelection=false;
    }; 
    
    /** End - Access Section **/        

    // $scope.master = angular.copy($scope.sampleData);
    // $scope.reset();
})

.controller('modalCtrl', function ($scope, $modalInstance,id) {
    $scope.closeModal = function(val) {
        if (val) {
            $modalInstance.close(val);            
        }
        else {
            $modalInstance.dismiss();
        }

    }
});