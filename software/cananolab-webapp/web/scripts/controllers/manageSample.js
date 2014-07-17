'use strict';
var app = angular.module('angularApp')

  .controller('ManageSampleCtrl', function (navigationService,groupService,$rootScope,$scope,$http) {
    $rootScope.navTree=false;
    $rootScope.tabs = navigationService.query();
    $rootScope.groups = groupService.get();     

  });
