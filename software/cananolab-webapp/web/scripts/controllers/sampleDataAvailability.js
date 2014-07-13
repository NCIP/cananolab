'use strict';
var app = angular.module('angularApp')
    .controller('SampleDataAvailabilityCtrl', function ($rootScope,$scope,$http,$filter,$routeParams,$log,$modalInstance,sampleId) {

    $scope.sampleId = sampleId;

    $scope.ok = function () {
    $modalInstance.close();
    };

    });
