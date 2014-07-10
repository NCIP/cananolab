var app = angular.module('angularApp')

app.directive('druglist', function($compile) {
  return {
    restrict: 'A',
    compile: function(tElement,tAttrs,transclude)
      {
        return function(scope,element,attrs) 
          {
            var treatment = scope.row.entity.treatment;
            if (treatment != null)
                {
                    scope.drugs = treatment.drugs;
                    if (scope.drugs)
                        {   
                            // var links = '<span ng-repeat="item in drugs"><a ng-click="drugClick(item)" href="/#/cohorts">{{item.name}}</a><span ng-if="$index<drugs.length-1">, </span>';
                        }
                }
            element.html(links)
            $compile(element.contents())(scope);
          }
      }
    }
});
