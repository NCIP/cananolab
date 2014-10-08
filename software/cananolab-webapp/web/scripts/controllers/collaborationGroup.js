'use strict';
var app = angular.module('angularApp')
  .controller('CollaborationGroupCtrl', function (navigationService,groupService,$scope,$rootScope,$http,$location) {
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();
  });
