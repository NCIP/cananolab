'use strict';
var app = angular.module('angularApp')
  .controller('PublicationCtrl', function (sampleService, navigationService, groupService, $rootScope,$scope,$http,$filter,$routeParams) {
    $rootScope.tabs = navigationService.query();
    $rootScope.groups = groupService.get();   
    $scope.sampleData = sampleService.sampleData;
    $scope.sampleId = sampleService.sampleId;

    // Displays left hand nav for samples section. navTree shows nav and navDetail is page index //
    $rootScope.navTree = true;
    $rootScope.navDetail = 3;
    

    //$scope.$on('$viewContentLoaded', function(){
        $http({method: 'GET', url: 'http://localhost:8080/caNanoLab/rest/publication/summaryView?sampleId=' + $scope.sampleId.data}).
            success(function(data, status, headers, config) {
                $scope.publicationCategories = data.publicationCategories;
                $scope.publicationBean = data.publicationBean;
                $scope.category2Publications = data.category2Publications;
            }).
            error(function(data, status, headers, config) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
                $scope.message = data;

            });
    //});
    

  });
