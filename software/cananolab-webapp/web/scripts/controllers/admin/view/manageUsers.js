'use strict';
var app = angular.module('angularApp')
  .controller('ManageUserCtrl', function (userService,navigationService,groupService,$scope,$rootScope,$filter,ngTableParams,$http,$location,$modal) {
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();
    $scope.userData = userService.userData;
  });
