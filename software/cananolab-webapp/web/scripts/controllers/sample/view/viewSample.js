'use strict';
var app = angular.module('angularApp')
  .controller('IndSampleCtrl', function (sampleService,navigationService, groupService, $rootScope,$scope,$http,$filter,$location,$routeParams) {
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();   
    $scope.sampleData = sampleService.sampleData;
    // $scope.sampleData = {"sampleId":27131906,"sampleName":"UMC_HSTVAMC_NCL_NB-NChandaNNBM2010-01","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":1275683278000,"keywords":"BIOCOMPATIBILITY<br>GOLD<br>GUM ARABIC","pointOfContactMap":{"organizationDisplayName":["UMC_RadiolD<br>Department of Radiology<br>University of Missouri-Columbia<br>Columbia MO 65212 USA","UMC_RadiolD<br>Department of Radiology<br>University of Missouri-Columbia<br>Columbia MO 65212 USA"],"primaryContact":["true","false"],"role":["investigator","investigator"],"contactPerson":["Raghuraman Kannan<br>kannanr@health.missouri.edu","Kattesh V Katti<br>kattik@health.missouri.edu"]},"pocBeanDomainId":27066372,"availableEntityNames":null,"caNanoLabScore":null,"mincharScore":null,"chemicalAssocs":null,"physicoChars":null,"invitroChars":null,"invivoChars":null,"caNano2MINChar":null,"caNanoMINChar":null};
    $scope.sampleId = sampleService.sampleId;
    $scope.goBack = function() {
      $location.path("/sampleResults").replace(); 
      $location.search('sampleId', null);     
    };

    if ($routeParams.sampleId) {
      $scope.sampleId.data = $routeParams.sampleId;
    };    

    $scope.returnUserReadableBoolean = function(val) {
      if (val=='true') {
        return "Yes";
      }
      return "No";
    }


    $scope.$on('$viewContentLoaded', function(){
      $scope.loader = true;      
     $http({method: 'GET', url: '/caNanoLab/rest/sample/view?sampleId=' + $scope.sampleId.data}).
     success(function(data, status, headers, config) {
      $scope.sampleData = data;
      $scope.loader=false;
          $scope.sampleName = sampleService.sampleName($scope.sampleId.data);

        }).
        error(function(data, status, headers, config) {
          // called asynchronously if an error occurs
          // or server returns response with an error status.
        $scope.message = data;
      $scope.loader=false;

         });
    });



  });
