'use strict';
var app = angular.module('angularApp')
    .controller('MyWorkspaceCtrl', function (navigationService, groupService, $rootScope,$scope,$http,$filter,$location,$routeParams) {
        $rootScope.tabs = navigationService.get();
        $rootScope.groups = groupService.getGroups.data.get();

        $scope.$on('$viewContentLoaded', function(){
            $scope.loader = true;
            $http({method: 'GET', url: '/caNanoLab/rest/core/getWorkspaceItems'}).
                success(function(data, status, headers, config) {
                    $scope.data = data;
                    $scope.samples = $scope.data.samples;
                    $scope.protocols = $scope.data.protocols;
                    $scope.publications = $scope.data.publications;                    
                    $scope.loader = false;

                }).
                error(function(data, status, headers, config) {
                    $scope.message = data;
                    $scope.loader = false;

                });
        }); 

        $scope.mySamples = true;
        $scope.myProtocols = true;
        $scope.myPublications = true;
/*
        $scope.data = {"samples":[{"actions":["View","Edit","Delete"],"name":"Sample 0","id":2030493,"submisstionStatus":"In Progress","createdDate":1407961286181,"comments":null,"pubMedId":null,"access":"Read Write Delete (Owner)"},{"actions":["View","Edit","Delete"],"name":"Sample 1","id":2030494,"submisstionStatus":"In Progress","createdDate":1407961286181,"comments":null,"pubMedId":null,"access":"Read Write Delete (Owner)"},{"actions":["View","Edit","Delete"],"name":"Sample 2","id":2030495,"submisstionStatus":"In Progress","createdDate":1407961286181,"comments":null,"pubMedId":null,"access":"Read Write Delete (Owner)"},{"actions":["View","Edit","Delete"],"name":"Sample 3","id":2030496,"submisstionStatus":"In Progress","createdDate":1407961286181,"comments":null,"pubMedId":null,"access":"Read Write Delete (Owner)"},{"actions":["View","Edit","Delete"],"name":"Sample 4","id":2030497,"submisstionStatus":"In Progress","createdDate":1407961286181,"comments":null,"pubMedId":null,"access":"Read Write Delete (Owner)"},{"actions":["View","Edit","Delete"],"name":"Sample 5","id":2030498,"submisstionStatus":"In Progress","createdDate":1407961286181,"comments":null,"pubMedId":null,"access":"Read Write Delete (Owner)"}],"protocols":[{"actions":["View","Edit","Delete"],"name":"Protocol 0","id":495959,"submisstionStatus":"Submitted for Public Access","createdDate":1407961286181,"comments":null,"pubMedId":null,"access":"Read Write Delete (Owner)"},{"actions":["View","Edit","Delete"],"name":"Protocol 1","id":495960,"submisstionStatus":"Submitted for Public Access","createdDate":1407961286181,"comments":null,"pubMedId":null,"access":"Read Write Delete (Owner)"},{"actions":["View","Edit","Delete"],"name":"Protocol 2","id":495961,"submisstionStatus":"Submitted for Public Access","createdDate":1407961286181,"comments":null,"pubMedId":null,"access":"Read Write Delete (Owner)"},{"actions":["View","Edit","Delete"],"name":"Protocol 3","id":495962,"submisstionStatus":"Submitted for Public Access","createdDate":1407961286181,"comments":null,"pubMedId":null,"access":"Read Write Delete (Owner)"},{"actions":["View","Edit","Delete"],"name":"Protocol 4","id":495963,"submisstionStatus":"Submitted for Public Access","createdDate":1407961286181,"comments":null,"pubMedId":null,"access":"Read Write Delete (Owner)"}],"publications":[{"actions":["View","Edit","Delete"],"name":"Publication 0","id":495959,"submisstionStatus":"In Review","createdDate":1407961286181,"comments":null,"pubMedId":"18686770","access":"Read Write Delete (Owner)"},{"actions":["View","Edit","Delete"],"name":"Publication 1","id":495960,"submisstionStatus":"In Review","createdDate":1407961286181,"comments":null,"pubMedId":"18686771","access":"Read Write Delete (Owner)"},{"actions":["View","Edit","Delete"],"name":"Publication 2","id":495961,"submisstionStatus":"In Review","createdDate":1407961286181,"comments":null,"pubMedId":"18686772","access":"Read Write Delete (Owner)"},{"actions":["View","Edit","Delete"],"name":"Publication 3","id":495962,"submisstionStatus":"In Review","createdDate":1407961286181,"comments":null,"pubMedId":"18686773","access":"Read Write Delete (Owner)"}]};
        $scope.samples = $scope.data.samples;
        $scope.protocols = $scope.data.protocols;
        $scope.publications = $scope.data.publications;
*/
        //$scope.samples = [{'sampleName':'NCL-25-1', 'status':'in preparation', 'createdDate':'01/01/2014', 'user' : []}];
        //$scope.protocols = [{'protocolName':'Protocol 1', 'status':'in preparation', 'createdDate':'01/01/2014', 'user' : []}];
        //$scope.publications = [{'id':'1','publicationTitle':'Pub 1', 'status':'in preparation', 'createdDate':'01/01/2014', 'user' : []}];
        
        $scope.doDeleteSample = function(sampleId) {
            if (confirm("Are you sure you want to delete the Sample?")) {
                $scope.loader = true;

                $http({method: 'GET', url: '/caNanoLab/rest/sample/deleteSampleFromWorkspace',params: {"sampleId":sampleId}}).
                success(function(data, status, headers, config) {
                	//var sampleRow = document.getElementById('sample' + sampleId);
                    //sampleRow.parentNode.removeChild(sampleRow);
                    for (var k = 0; k < $scope.samples.length; ++k)
                    {
                        var sample = $scope.samples[k];
                        if (sampleId == sample.id ) {
                            $scope.samples.splice(k,1);
                        }
                    }
                    $scope.loader = false;
                }).
                error(function(data, status, headers, config) {
                    $scope.loader = false;
                    $scope.message = data;
                }); 
            }
        };
        
        $scope.doDeleteProtocol = function(protocolId) {
            if (confirm("Are you sure you want to delete the Protocol?")) {
                $scope.loader = true;

                $http({method: 'GET', url: '/caNanoLab/rest/protocol/deleteProtocolById',params: {"protocolId":protocolId}}).
                    success(function(data, status, headers, config) {
                        if (data == "success") {
                        	//var protocolRow = document.getElementById('protocol' + protocolId);
                        	//protocolRow.parentNode.removeChild(protocolRow);
                        	for (var k = 0; k < $scope.protocols.length; ++k)
                            {
                                var protocol = $scope.protocols[k];
                                if (protocolId == protocol.id ) {
                                    $scope.protocols.splice(k,1);
                                }
                            }
                        	$scope.loader = false;
                        }
                        else {
                            $scope.loader = false;
                            $scope.messages = data;
                        }

                    }).
                    error(function(data, status, headers, config) {
                        // called asynchronously if an error occurs
                        // or server returns response with an error status.
                        // $rootScope.sampleData = data;
                        $scope.loader = false;
                        $scope.messages = data;
                    });
            }
        };        
        
        $scope.doDeletePublication = function(publicationId) {
            if (confirm("Are you sure you want to delete  the Publication?")) {
            	$scope.loader = true;

            	$http({method: 'GET', url: '/caNanoLab/rest/publication/deletePublicationById',params: {"publicationId":publicationId}}).
	                success(function(data, status, headers, config) {
	                	if (data == "success") {
	                		//var publicationRow = document.getElementById('publication' + publicationId);
	                		//publicationRow.parentNode.removeChild(publicationRow);
	                		for (var k = 0; k < $scope.publications.length; ++k)
                            {
                                var publication = $scope.publications[k];
                                if (publicationId == publication.id ) {
                                    $scope.publications.splice(k,1);
                                }
                            }
	                		$scope.loader = false;
	                	}
	                	else {
	                		$scope.loader = false;
	                		$scope.messages = data;
	                	}
	
	                }).
	                error(function(data, status, headers, config) {
	                    // called asynchronously if an error occurs
	                    // or server returns response with an error status.
	                    // $rootScope.sampleData = data;
	                    $scope.loader = false;
	                    $scope.messages = data;
	                });
            }
        };         

    });