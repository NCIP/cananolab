'use strict';
angular.module('angularApp')
    .controller('MessageCtrl', function ($rootScope,$scope,$http,$filter,$routeParams) {
        $scope.message = $routeParams.message;
    });