'use strict';
var app = angular.module('angularApp')

  .controller('WorkflowCtrl', function (navigationService,groupService,$rootScope,$scope,$http) {
    $scope.auth_cookie="";
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();     

  });
