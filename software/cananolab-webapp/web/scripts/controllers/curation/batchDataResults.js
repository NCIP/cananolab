'use strict';
var app = angular.module('angularApp')
    .controller('BatchDataResultsCtrl', function (navigationService, groupService, $rootScope,$scope,$http,$filter,$location,$routeParams) {
        $rootScope.tabs = navigationService.get();
        $rootScope.groups = groupService.getGroups.data.get();
        $scope.dataExists = false;

        $scope.$on('$viewContentLoaded', function(){
            $scope.loader = true;
            $http({method: 'GET', url: '/caNanoLab/rest/curation/manageResult'}).
                success(function(data, status, headers, config) {
                    $scope.data = data;
                    $scope.loader = false;
                    
                    if( data != null && data != '' && data.length > 0 ) {
                    	$scope.dataExists = true;
                    }

                }).
                error(function(data, status, headers, config) {
                    $scope.message = data;
                    $scope.loader = false;

                });
        }); 
        
        
        //$scope.data = [{'type':'asdsadsadsad', 'status':'sdafefrerwerwe'}];
        //$scope.dataExists = true;

    });