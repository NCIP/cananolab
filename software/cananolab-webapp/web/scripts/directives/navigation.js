'use strict';
var app = angular.module('angularApp')

  .directive('navigation',function($location) {



    var link = function($scope, $element, $attrs) {
        $scope.url = $location.path();
        $scope.isEdit = 0;
        $scope.buttons = [];
        // $scope.edit = ['/editSample','/editComposition','/editCharacterization','/updatePublication'];
        $scope.edit = [{'url':'/editSample','name':'GENERAL INFO'},{'url':'/editComposition','name':'COMPOSITION'},{'url':'/editCharacterization','name':'CHARACTERIZATION'},{'url':'/updatePublication','name':'PUBLICATION'}];
        $scope.view = [{'url':'/sample','name':'GENERAL INFO'},{'url':'/composition','name':'COMPOSITION'},{'url':'/characterization','name':'CHARACTERIZATION'},{'url':'/publication','name':'PUBLICATION'}];
        $scope.testClick = function(uri) {
            $location.path(uri).replace();
        }
        $scope.$watch(function() {
            return $location.path();

        },
        function(newValue, oldValue) {
            $scope.buttons = [];
            $scope.navTree = 0;
            for (var x=0;x<$scope.edit.length;x++) {
              if (newValue==$scope.edit[x].url) {
                $scope.isEdit=1;
                $scope.buttons = $scope.edit;
                $scope.url = newValue;
                $scope.navTree = 1;

              }
            };
            for (var x=0;x<$scope.view.length;x++) {
              if (newValue==$scope.view[x].url) {
                $scope.isEdit=0;
                $scope.buttons = $scope.view;  
                $scope.url = newValue;
                $scope.navTree = 1;

              }
            }; 
        });
    };
 
    return {
        restrict: 'E',
        templateUrl:'scripts/directives/navigation.html',      
        link: link
    };
  });
