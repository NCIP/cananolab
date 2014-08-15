'use strict';
var app = angular.module('angularApp')
  .controller('ManageProtocolCtrl', function (protocolService,navigationService,groupService,$scope,$rootScope,$filter,ngTableParams,$http,$location,$modal) {
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();
    $scope.protocolData = protocolService.protocolData;
    $rootScope.navTree=false;
  });
