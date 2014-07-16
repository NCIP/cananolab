'use strict';
var app = angular.module('angularApp')
  .controller('AboutCtrl', function (navigationService,groupService,$rootScope,$scope) {
	$rootScope.tabs = navigationService.query({'homePage':'true'});
	$rootScope.groups = groupService.get();
	$rootScope.navTree=false;

  });
