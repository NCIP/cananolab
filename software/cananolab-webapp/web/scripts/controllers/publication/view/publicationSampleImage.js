'use strict';
angular.module('angularApp',['ngRoute','ngSanitize','ngRoute'])
  .controller('PublicationSampleImageCtrl', function (utilsService, $rootScope,$scope,$http,$filter,$location,$routeParams) {
    $scope.id = utilsService.getParameterFromURL('id');  
    $scope.type = utilsService.getParameterFromURL('type'); 
    $scope.url = '/caNanoLab/rest/publication/searchByIdImage?type='+$scope.type+'&id='+$scope.id;
  });
