'use strict';
var app = angular.module('angularApp')
  .controller('PublicationSampleInformationCtrl', function (navigationService, groupService, $rootScope,$scope,$http,$filter,$location,$routeParams) {
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();   
    $rootScope.navTree = false;

    $scope.id = $routeParams.id;
    $scope.type = $routeParams.type;
    $scope.goBack = function() {
        $location.path("/searchSamplesByPublication").search({}).replace();
    };


    $scope.$on('$viewContentLoaded', function(){
        $scope.loader = true;      
        $http({method: 'GET', url: '/caNanoLab/rest/publication/searchById?id='+$scope.id+'&type='+$scope.type}).
        success(function(data, status, headers, config) {
        $scope.data = data;
        $scope.loader = false;
    
        }).
        error(function(data, status, headers, config) {
          // called asynchronously if an error occurs
          // or server returns response with an error status.
        $scope.message = data;
        $scope.loader = false;

         });
    });     

  });
