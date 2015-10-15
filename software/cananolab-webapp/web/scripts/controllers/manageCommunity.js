'use strict';
var app = angular.module('angularApp')

  .controller('ManageCommunityCtrl', function (navigationService,groupService,$rootScope,$scope,$http) {
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();     

  });
