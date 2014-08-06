'use strict';
angular.module('angularApp')
    .controller('MessageCtrl', function (navigationService, groupService, $rootScope,$scope,$http,$filter,$routeParams) {
        $rootScope.tabs = navigationService.get();
        $rootScope.groups = groupService.get();   

        $scope.message = $routeParams.message;
    });