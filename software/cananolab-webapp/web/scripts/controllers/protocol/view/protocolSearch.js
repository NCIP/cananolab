'use strict';
var app = angular.module('angularApp')

    .controller('ProtocolSearchCtrl', function (protocolService,navigationService,groupService,$rootScope,$scope,$http,$location) {
        $scope.searchProtocolForm = {};
        $rootScope.navTree=false;
        $scope.protocolData = protocolService.protocolData;
        $rootScope.tabs = navigationService.get();
        $rootScope.groups = groupService.getGroups.data.get();

        $scope.$on('$viewContentLoaded', function(){
            $http({method: 'GET', url: '/caNanoLab/rest/protocol/setup'}).
                success(function(data, status, headers, config) {
                    $scope.data = data;
                    $scope.protocolTypes = data.protocolTypes;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $scope.message = data;
                });
        });


        $scope.doSearch = function() {
            $scope.loader = true;
            for (var key in $scope.researchArea) {
                if ($scope.researchArea[key]) {
                    $scope.searchPublicationForm.researchArea.push(key)
                }

            }

            $http({method: 'POST', url: '/caNanoLab/rest/protocol/searchProtocol',data: $scope.searchProtocolForm}).
                success(function(data, status, headers, config) {
                    $scope.protocolData.data = data;
                    $location.path("/protocolResults").replace();
                    //$scope.message = data;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    // $rootScope.sampleData = data;
                    $scope.loader = false;
                    $scope.message = data;

                    //$scope.protocolData.data = [{"type":"sterility","viewName":"STE-2","abbreviation":"STE-2","version":"1.0","createdDate":null,"fileInfo":"Title:<br>Detection of Microbial Contamination Assay<br><br>Description:<br>Protocol describes a procedure for quantitative determination of microbial<br>contamination in a nanoparticle preparation.<br><br><a href=rest/protocol/download?fileId=24521990 target='new'>View</a>","id":24489222,"editable":false},{"type":"sterility","viewName":"STE-1","abbreviation":"STE-1","version":"1.0","createdDate":null,"fileInfo":"Title:<br>Detection of Endotoxin Contamination by End Point Chromogenic LAL Assay<br><br>Description:<br>Protocol for a quantitative detection of Gram negative<br>bacterial endotoxin in nanoparticle preparations using an end-point Limulus Amebocyte Lysate (LAL) assay.<br><br><a href=rest/protocol/download?fileId=24521989 target='new'>View</a>","id":24489221,"editable":false}];
                    //$location.path("/protocolResults").replace();
                });
        };

        $scope.resetForm = function() {
            $scope.searchProtocolForm = {};
        };

    });
