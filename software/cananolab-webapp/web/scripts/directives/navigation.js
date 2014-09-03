'use strict';
var app = angular.module('angularApp')

  .directive('navigation',function($location) {
    var url = $location.path();
    var isEdit = 0;
    var edit = ['/editSample','/editComposition','/editCharacterization','/updatePublication'];
    var view = ['/sample','/composition','/characterization','/publication'];

    for (var x=0;x<edit.length;x++) {
      if (url==edit[x]) {
        isEdit=1;
      }
    };
    for (var x=0;x<view.length;x++) {
      if (url==view[x]) {
        isEdit=0;
      }
    }; 
var link = function() { 
sc   
scope.$watch
    return {
      restrict: 'E',      
      template: isEdit
    };
  });
