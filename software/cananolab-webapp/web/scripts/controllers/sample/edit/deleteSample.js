'use strict';

var app = angular.module('angularApp')
.controller('DelSampleCtrl', function ($scope,$rootScope,sampleService,navigationService,groupService) {
    $rootScope.tabs = navigationService.get();
    $scope.sampleMessage = sampleService.message.data;
    $rootScope.groups = groupService.getGroups.data.get();

});