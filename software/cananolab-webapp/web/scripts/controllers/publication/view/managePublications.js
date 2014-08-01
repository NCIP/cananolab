'use strict';
var app = angular.module('angularApp')
  .controller('ManagePublicationCtrl', function (publicationService,navigationService,groupService,$scope,$rootScope,$filter,ngTableParams,$http,$location,$modal) {
    $rootScope.tabs = navigationService.query();
    $rootScope.groups = groupService.get();
    $scope.publicationData = publicationService.publicationData;
    $rootScope.navTree=false;
  });
