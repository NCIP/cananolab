'use strict';
var app = angular.module('angularApp')

  .controller('TemplateCtrl', function ($route,$scope, $location, $http, keywordService) {
  $scope.keywordData = keywordService.keywordData;    
  $scope.doKeywordSearch = function() {
    $scope.isSearching = true;
    $http({method: 'GET', url: '/caNanoLab/rest/customsearch/search?keyword=' + $scope.keyword_search_text}).

        success(function(data, status, headers, config) {
          $scope.keywordData.data = data;
          $location.path("/keywordSearchResults").replace();          
          $route.reload();
          $scope.isSearching = false;
        }).
        error(function(data, status, headers, config) {
          // called asynchronously if an error occurs
          // or server returns response with an error status.
         });
  };



  });
