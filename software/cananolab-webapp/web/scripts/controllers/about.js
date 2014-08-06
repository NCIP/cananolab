'use strict';
var app = angular.module('angularApp')
  .controller('AboutCtrl', function (navigationService,groupService,$rootScope,$scope) {
     $rootScope.tabs = navigationService.get({'homePage':'true'});
	$rootScope.groups = groupService.getGroups.data.get();
	$rootScope.navTree=false;

  });
