'use strict';
angular.module('publicationApp',[
    'ngRoute'])
    .controller('PrintSamplePublicationCtrl', function ($rootScope,$scope,$http,$filter,$routeParams) {
        var sampleId = $routeParams.sampleId;
        $http({method: 'GET', url: '/caNanoLab/rest/publication/summaryView?sampleId=27131907'}).
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
        });