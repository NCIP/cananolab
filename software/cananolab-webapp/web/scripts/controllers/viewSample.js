'use strict';
var app = angular.module('angularApp')
  .controller('IndSampleCtrl', function (sampleService,navigationService, groupService, $rootScope,$scope,$http,$filter,$location,$routeParams) {
    $rootScope.tabs = navigationService.query();
    $rootScope.groups = groupService.get();   
    $scope.sampleData = sampleService.sampleData;
    // $scope.sampleData = {"sampleId":20917507,"sampleName":"NCL-23-1","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":1152504000000,"keywords":"MAGNEVIST<br>MRI<br>NCL-23","pointOfContactMap":{"organizationDisplayName":["DNT"],"primaryContact":["true"],"role":[""],"contactPerson":[""]},"pocBeanDomainId":20884736,"availableEntityNames":null,"caNanoLabScore":null,"mincharScore":null,"chemicalAssocs":null,"physicoChars":null,"invitroChars":null,"invivoChars":null,"caNano2MINChar":null,"caNanoMINChar":null};
    $scope.sampleId = sampleService.sampleId;
    // Displays left hand nav for samples section. navTree shows nav and navDetail is page index //
    $rootScope.navTree = true;
    $rootScope.navDetail = 0;

    $scope.goBack = function() {
      $location.path("/sampleResults").replace();      
    };

    if ($routeParams.sampleId) {
      $scope.sampleId.data = $routeParams.sampleId;
    }

    $scope.returnUserReadableBoolean = function(val) {
      if (val=='true') {
        return "Yes";
      }
      return "No";
    }



    $scope.$on('$viewContentLoaded', function(){
     $http({method: 'GET', url: '/caNanoLab/rest/sample/view?sampleId=' + $scope.sampleId.data}).
     success(function(data, status, headers, config) {
      $scope.sampleData = data;
        }).
        error(function(data, status, headers, config) {
          // called asynchronously if an error occurs
          // or server returns response with an error status.
        $scope.message = data;

         });
    });     

  });
