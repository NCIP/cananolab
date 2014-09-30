'use strict';
var app = angular.module('angularApp')
    .controller('PocDetailsCtrl', function ($scope,$http,$modalInstance,data) {
        $scope.sampleData = data;
        $scope.cancel = function() {
            $modalInstance.close();
        };
 })
    .controller('CharDetailsCtrl', function ($scope,$http,$modalInstance,data) {
        $scope.sampleData = data;
        $scope.cancel = function() {
            $modalInstance.close();
        };        
 })
 	.controller('NanoMaterialDetailsCtrl', function ($scope,$http,$modalInstance,data) {
        $scope.sampleData = data;
        $scope.cancel = function() {
            $modalInstance.close();
        };
 })
    .controller('FunctionalizingEntityDetailsCtrl', function ($scope,$http,$modalInstance,data) {
        $scope.sampleData = data;
        $scope.cancel = function() {
            $modalInstance.close();
        };
 });