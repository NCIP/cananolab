'use strict';
var app = angular.module('angularApp')
  .controller('CompositionCtrl', function (utilsService,sampleService,navigationService, groupService, $rootScope,$scope,$http,$location,$filter,$routeParams) {
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();
    $scope.sampleData = sampleService.sampleData;
    $scope.sampleId = sampleService.sampleId;

    $scope.sampleData = sampleService.sampleData;

     $scope.goBack = function() {
      $location.path("/sampleResults").replace();
      $location.search('sampleId', null);      };
      
    if ($routeParams.sampleId) {
      $scope.sampleId.data = $routeParams.sampleId;
    };
    
      $scope.select = function(tab) {
          var size = 0, key;
          for (key in $scope.compositionSections) {
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
	$http({method: 'GET', url: '/caNanoLab/rest/composition/summaryView?sampleId=' + $scope.sampleId.data}).
            success(function(data, status, headers, config) {
                $scope.compositionSections = data.compositionSections;
                $scope.nanomaterialentity = data.nanomaterialentity;
                $scope.functionalizingentity = data.functionalizingentity;
                $scope.chemicalassociation = data.chemicalassociation;
                $scope.compositionfile = data.compositionfile;
                $scope.sampleName = sampleService.sampleName($scope.sampleId.data);
                
                $scope.loader = false;     
                
                $scope.nanomaterialentityEmpty = utilsService.isHashEmpty(data.nanomaterialentity);
                $scope.functionalizingentityEmpty = utilsService.isHashEmpty(data.functionalizingentity);
                $scope.chemicalassociationEmpty = utilsService.isHashEmpty($scope.chemicalassociation);
                $scope.compositionfileEmpty = utilsService.isHashEmpty(data.compositionfile);
            }).
            error(function(data, status, headers, config) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
                $scope.message = data;
                $scope.loader = false;

            });
	
    $scope.print = function() {
    	window.open('views/sample/view/printComposition.html?sampleId='+$scope.sampleId.data)
    }
    $scope.popImage = function(imgSrc, imgId) {
    	utilsService.popImage(imgSrc, imgId);
    }

  });

