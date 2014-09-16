'use strict';
angular.module('angularApp',[
     'ngRoute','ngSanitize','ngRoute'])
    .controller('PrintSamplePublicationCtrl', function (utilsService,$rootScope,$scope,$http,$filter,$routeParams) {
        $scope.sampleId = utilsService.getParameterFromURL('sampleId');

    	$scope.loader = true;
        $http({method: 'GET', url: '/caNanoLab/rest/publication/summaryPrint?sampleId=' + $scope.sampleId}).
            success(function(data, status, headers, config) {
                $scope.data = data;
                $scope.publicationCategories = data.publicationCategories;
                $scope.publicationBean = data.publicationBean;
                $scope.category2Publications = data.category2Publications;
                $scope.sampleName = sampleService.sampleName($scope.sampleId);

                $scope.loader = false;
            }).
            error(function(data, status, headers, config) {

                // called asynchronously if an error occurs
                // or server returns response with an error status.
                $scope.message = data;
                $scope.loader = false;

            });
        });