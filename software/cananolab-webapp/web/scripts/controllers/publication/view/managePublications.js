'use strict';
var app = angular.module('angularApp')
  .controller('ManagePublicationCtrl', function (publicationService,navigationService,groupService,$scope,$rootScope,$filter,ngTableParams,$http,$location,$modal) {
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();
    $scope.publicationData = publicationService.publicationData;
  });
