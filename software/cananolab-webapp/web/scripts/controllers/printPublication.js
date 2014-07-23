'use strict';
angular.module('publicationApp',[
    'ngRoute'])
    .controller('PrintSamplePublicationCtrl', function ($rootScope,$scope,$http,$filter,$routeParams) {
    	$scope.sampleId = sampleService.sampleId;
    	$scope.loader = true;
        $http({method: 'GET', url: '/caNanoLab/rest/publication/summaryPrint?sampleId=' + $scope.sampleId.data}).
            success(function(data, status, headers, config) {
                $scope.publicationCategories = data.publicationCategories;
                $scope.publicationBean = data.publicationBean;
                $scope.category2Publications = data.category2Publications;
                $scope.loader = false;
            }).
            error(function(data, status, headers, config) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
                $scope.message = data;
                $scope.loader = false;

            });
        });