'use strict';
var app = angular.module('angularApp')
    .controller('PocDetailsCtrl', function ($scope,$http,$modalInstance,data) {
        $scope.sampleData = data;
        $scope.cancel = function() {
            $modalInstance.close();
        };
 })
    .controller('anotherCtrl', function ($scope,$http,$modalInstance,data) {
        $scope.sampleData = data;
        $scope.cancel = function() {
            $modalInstance.close();
        };        
 })
    .controller('anotherCtrl2', function ($scope,$http,$modalInstance,data) {
        $scope.sampleData = data;
        $scope.cancel = function() {
            $modalInstance.close();
        };
 });