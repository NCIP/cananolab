'use strict';
var app = angular.module('angularApp')
    .controller('MyWorkspaceCtrl', function ($rootScope,$scope,$http,$filter,$location,$routeParams) {
/*    .controller('MyWorkspaceCtrl', function (navigationService, groupService, $rootScope,$scope,$http,$filter,$location,$routeParams) {
        $rootScope.tabs = navigationService.get();
        $rootScope.groups = groupService.getGroups.data.get();
        $rootScope.navTree = false;

        $scope.$on('$viewContentLoaded', function(){
            $scope.loader = true;
            $http({method: 'GET', url: '/caNanoLab/rest/myworkspace'}).
                success(function(data, status, headers, config) {
                    $scope.data = data;
                    $scope.loader = false;

                }).
                error(function(data, status, headers, config) {
                    $scope.message = data;
                    $scope.loader = false;

                });
        }); */

        $scope.mySamples = true;
        $scope.myProtocols = true;
        $scope.myPublications = true;


        $scope.samples = [{'sampleName':'NCL-25-1', 'status':'in preparation', 'createdDate':'01/01/2014', 'user' : []}];
        $scope.protocols = [{'protocolName':'Protocol 1', 'status':'in preparation', 'createdDate':'01/01/2014', 'user' : []}];
        $scope.publications = [{'id':'1','publicationTitle':'Pub 1', 'status':'in preparation', 'createdDate':'01/01/2014', 'user' : []}];

    });