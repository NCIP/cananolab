'use strict';
var app = angular.module('angularApp')

.directive('gridList', function($compile) {
  return {
    restrict: 'A',
    compile: function(tElement,tAttrs,transclude)
      {
        return function(scope,element,attrs) 
          {
            var html = '<div ng-repeat="i in row.entity.'+attrs.gridList+' track by $index">{{i}}</div>'
            element.html(html)
            $compile(element.contents())(scope);
          }
      }
    }
});
