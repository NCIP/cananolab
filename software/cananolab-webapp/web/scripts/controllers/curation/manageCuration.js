'use strict';
var app = angular.module('angularApp')
  .controller('ManageCurationCtrl', function (navigationService,groupService,$scope,$rootScope,$filter,ngTableParams,$http,$location) {
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();
    $rootScope.navTree=false;
  });
