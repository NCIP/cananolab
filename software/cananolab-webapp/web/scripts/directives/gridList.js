'use strict';
var app = angular.module('angularApp')

.directive('gridList', function($compile) {
  return {
    restrict: 'A',
    compile: function(tElement,tAttrs,transclude)
      {
        return function(scope,element,attrs) 
          {
            var html = '<div ng-repeat="i in row.entity.'+attrs.gridList+'">{{i}}</div>'
            element.html(html)
            $compile(element.contents())(scope);
          }
      }
    }
});

// .directive('gridList', function($compile) {
//   return {
//     restrict: 'A',
//     compile: function(tElement,tAttrs,transclude)
//       {
//         return function(scope,element,attrs) 
//           {
//             var something = scope.row.entity.characterizations;
//             window.something = scope.row.entity;
//             var html = "<div>" + scope.row.entity.characterizations + "</div>";
//             scope.characterizations = [];
//             scope.characterizations = scope.row.entity.characterizations;
//             if (scope.characterizations != null)
//                 { 
//                     var html = '<div ng-repeat="x in characterizations track by $index">{{x}}</div>';
//                 }

//             element.html(html)
//             $compile(element.contents())(scope);
//           }
//       }
//     }
// });


// 'use strict';
// var app = angular.module('angularApp')

// .directive('gridList', function($compile) {
//   return {
//     restrict: 'A',
//     compile: function(tElement,tAttrs,transclude)
//       {
//         return function(scope,element,attrs) 
//           {
//             scope[attrs.gridList] = scope.row.entity[attrs.gridList];
//             if (scope[attrs.gridList] != null)
//                 { 
//                     var html = '<div ng-repeat="x in ' + [attrs.gridList] + '">{{x}}</div>';
//                 }

//             element.html(html)
//             $compile(element.contents())(scope);
//           }
//       }
//     }
// });
