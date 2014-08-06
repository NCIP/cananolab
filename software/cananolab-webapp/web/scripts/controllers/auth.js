'use strict';
var app = angular.module('angularApp')

  .controller('AuthCtrl', function (navigationService,groupService,$rootScope,$scope,$http) {
    $scope.auth_cookie="";
    $rootScope.navTree=false;
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();     

  });
