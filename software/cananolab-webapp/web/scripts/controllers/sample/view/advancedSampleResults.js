'use strict';
var app = angular.module('angularApp')
  .controller('AdvancedSampleResultsCtrl', function (sampleService,navigationService,groupService,utilsService,$scope,$rootScope,$filter,ngTableParams,$http,$location,$modal) {
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();
    $scope.sampleData = sampleService.sampleData;
    $scope.utilsService = utilsService;
    
    $scope.goBack = function() {
      $location.path("/advancedSampleSearch").replace();      
    }; 

    var data = $scope.sampleData.data;
    $scope.sampleData.data  = data;
    if (data==null) {
      $scope.sampleData.data = [];
      data = [];
      $location.path("/advancedSampleSearch").search({}).replace();
    }  

    $scope.tableParams = new ngTableParams(
        {
            page: 1,            // show first page
            count: 20           // count per page     
        },
        {
            counts: [], // hide page counts control

            total: data.length, // length of data
            getData: function($defer, params) {
            // use build-in angular filter
            var orderedData = params.sorting() ? 
            $filter('orderBy')(data, params.orderBy()) : data;
            $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            } 
        }); 

  
  });
