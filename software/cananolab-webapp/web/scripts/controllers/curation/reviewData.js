'use strict';
var app = angular.module('angularApp')

    .controller('ReviewDataCtrl', function (navigationService,groupService,$rootScope,$scope,$http,$location) {
        $rootScope.tabs = navigationService.get();
        $rootScope.groups = groupService.getGroups.data.get();

        $scope.$on('$viewContentLoaded', function(){
             $http({method: 'GET', url: '/caNanoLab/rest/curation/reviewData'}).
                success(function(data, status, headers, config) {
                    $scope.reviewData = data;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $scope.message = data;
                });

            //$scope.reviewData = [{"dataId":"31358981","dataName":"NCNHIR_20nm_colloidal_silver_citrate_coated","dataType":"sample","submittedDate":"2011-10-18","submittedBy":"cristr","reviewStatus":"pending","reviewLink":"rest/sample/edit?sampleId=31358981"},{"dataId":"44335104","dataName":"SY-Test","dataType":"sample","submittedDate":"2014-07-29","submittedBy":"canano_res","reviewStatus":"pending","reviewLink":"rest/sample/edit?sampleId=44335104"}];

        });
    });
