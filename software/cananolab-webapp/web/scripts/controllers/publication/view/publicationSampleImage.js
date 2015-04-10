'use strict';
angular.module('angularApp',['ngRoute','ngSanitize','ngRoute'])
  .controller('PublicationSampleImageCtrl', function (utilsService, $rootScope,$scope,$http,$filter,$location,$routeParams) {
    $scope.id = utilsService.getParameterFromURL('id');  
    $scope.type = utilsService.getParameterFromURL('type'); 

        $scope.loader = true;      
        $http({method: 'GET', url: '/caNanoLab/rest/publication/searchByIdImage?type='+$scope.type+'&id='+$scope.id}).
        success(function(data, status, headers, config) {
        	$scope.fileInfo = data;
        	$scope.loader = false;
        }).
        error(function(data, status, headers, config) {
          // called asynchronously if an error occurs
          // or server returns response with an error status.
        	$scope.fileInfo = data;
    		$scope.loader = false;

         });

  });
