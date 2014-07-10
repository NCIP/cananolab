'use strict';
var app = angular.module('angularApp')

  .controller('SampleResultsCtrl', function (navigationFactory,groupsFactory,$rootScope,$scope,$http) {
    $rootScope.tabs = navigationFactory.query();
    $rootScope.groups = groupsFactory.get();




          $scope.filterOptions = {
              filterText: "",
              useExternalFilter: true
          }; 
          $scope.totalServerItems = 0;
          $scope.pagingOptions = {
              pageSizes: [10],
              pageSize: 10,
              currentPage: 1
          };  

          $scope.setPagingData = function(data, page, pageSize){  
            var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
            $scope.myData = pagedData;
            $scope.totalServerItems = data.length;
            if (!$scope.$$phase) {
                $scope.$apply();
            }
          };

          $scope.getPagedDataAsync = function (pageSize, page, searchText) {
            setTimeout(function () {
                var data;
                if (searchText) {
                    var ft = searchText.toLowerCase();
                    $scope.setPagingData($rootScope.sampleData,page,pageSize);
                } else {
                        $scope.setPagingData($rootScope.sampleData,page,pageSize);
                }
            }, 100);
          };
            
          $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);
            
          $scope.$watch('pagingOptions', function (newVal, oldVal) {
            if (newVal !== oldVal && newVal.currentPage !== oldVal.currentPage) {
              $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
            }  
          }, true);

          $scope.$watch('filterOptions', function (newVal, oldVal) {
            if (newVal !== oldVal) {
              $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
            }
          }, true);
            
          $scope.gridOptions = {
            data: 'myData',
            enablePaging: true,
            showFooter: true,
            totalServerItems: 'totalServerItems',
            pagingOptions: $scope.pagingOptions,
            filterOptions: $scope.filterOptions
          };

   
  });
