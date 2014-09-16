'use strict';


var app = angular.module('angularApp')

app.controller('CharacterizationCtrl', function (sampleService,utilsService,navigationService, groupService, $rootScope,$scope,$http,$location,$filter,$routeParams) {
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();   
    $scope.sampleData = sampleService.sampleData;
    $scope.sampleId = sampleService.sampleId;

    $scope.goBack = function() {
      $location.path("/sampleResults").replace(); 
      $location.search('sampleId', null);
    };  

    if ($routeParams.sampleId) {
      $scope.sampleId.data = $routeParams.sampleId;
    };   

    $scope.select = function(tab) {
        var size = 0, key;
        for (key in $scope.data) {
          size+=1
        };

        for (var x=0; x<size;x++) {
            if (tab>=0) {
                if (x==tab){
                    $scope['show'+x]=false;                
                }
                else {
                    $scope['show'+x]=true;
                } 
            }    
            else {
                $scope['show'+x]=false;
            }      
        }
    };    
    
    $scope.loader = true;
    $http({method: 'GET', url: '/caNanoLab/rest/sample/characterizationView?sampleId='+$scope.sampleId.data}).
    success(function(data, status, headers, config) {
      $scope.data = data;
      $scope.sampleName = sampleService.sampleName($scope.sampleId.data);
      
	 $scope.loader = false;      
    }).
    error(function(data, status, headers, config) {
        // called asynchronously if an error occurs
        // or server returns response with an error status.
        $scope.message = data;
      $scope.loader = false;
    });

    $scope.print = function() {
    	window.open('views/sample/view/printCharacterization.html?sampleId='+$scope.sampleId.data+'&sampleName='+$scope.sampleData.data[0].sampleName)
    }
    $scope.popImage = function(imgSrc, imgId) {
    	utilsService.popImage(imgSrc, imgId);
    }

  });

