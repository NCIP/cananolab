'use strict';
var app = angular.module('angularApp')
  .controller('PublicationSampleInformationCtrl', function (navigationService, groupService, $rootScope,$scope,$http,$filter,$location,$routeParams) {
    $rootScope.tabs = navigationService.query();
    $rootScope.groups = groupService.get();   
    $rootScope.navTree = false;

    $scope.data = {"id":"10.1002/ijc.22581","type":"DOI","authors":"John Doe, Mary Smith and al.","title":"Latest and greatest","journal":"International journal of cancer. Journal international du cancer","samples":{"20917508":"SampleName_20917508","20917509":"SampleName_20917509","20917515":"SampleName_20917515","20917516":"SampleName_20917516","20917507":"SampleName_20917507","20917510":"SampleName_20917510","20917513":"SampleName_20917513","20917514":"SampleName_20917514","20917511":"SampleName_20917511","20917512":"SampleName_20917512"},"year":2000,"volumn":"120:340-430","errors":[]};
    // $scope.goBack = function() {
    //   $location.path("/sampleResults").replace(); 
    //   $location.search('sampleId', null);     
    // };

    // if ($routeParams.sampleId) {
    //   $scope.sampleId.data = $routeParams.sampleId;
    // };

    // $scope.returnUserReadableBoolean = function(val) {
    //   if (val=='true') {
    //     return "Yes";
    //   }
    //   return "No";
    // }


    // $scope.$on('$viewContentLoaded', function(){
    //   $scope.loader = true;      
    //  $http({method: 'GET', url: '/caNanoLab/rest/sample/view?sampleId=' + $scope.sampleId.data}).
    //  success(function(data, status, headers, config) {
    //   alert("uere")
    //   $scope.sampleData = data;
    //   $scope.loader=false;
      
    //     }).
    //     error(function(data, status, headers, config) {
    //       // called asynchronously if an error occurs
    //       // or server returns response with an error status.
    //     $scope.message = data;
    //   $scope.loader=false;

    //      });
    // });     

  });
