'use strict';
var app = angular.module('angularApp')
	.controller('SampleDataAvailabilityCtrl', function ($rootScope,$scope,$http,$filter,$routeParams,$modalInstance,sampleId) {

	$scope.sampleId = sampleId;
	// $scope.availabilityData = {"sampleId":0,"sampleName":"NCL-23-1","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":"caNanoLab: 50%; MINChar: 33%","createdDate":null,"keywords":null,"pointOfContactMap":{},"pocBeanDomainId":0,"availableEntityNames":["general sample information","blood contact","immune cell function","physical state","functionalizing entities","cytotoxicity","sample function","size","nanomaterial entities","molecular weight","solubility","association","publications","purity","sample composition"],"caNanoLabScore":"50.0% (15 out of 30)","mincharScore":"33.0% (3 out of 9)","chemicalAssocs":["attachment","encapsulation","entrapment"],"physicoChars":["molecular weight","physical state","purity","relaxivity","shape","size","solubility","surface"],"invitroChars":["blood contact","cytotoxicity","enzyme induction","immune cell function","metabolic stability","oxidative stress","sterility","targeting","transfection"],"invivoChars":["pharmacokinetics","toxicology"],"caNano2MINChar":{"zeta potential":"surface charge","sample composition":"chemical composition","purity":"purity","surface charge":"surface charge","shape":"shape","attachment":"surface chemistry","surface area":"surface area","size":"particle size/size distribution"},"caNanoMINChar":{"zeta potential":"surface charge","sample composition":"chemical composition","purity":"purity","surface charge":"surface charge","shape":"shape","attachment":"surface chemistry","surface area":"surface area","size":"particle size/size distribution"}};
	$scope.$on('$viewContentLoaded', function(){
	$http({method: 'GET', url: '/caNanoLab/rest/sample/viewDataAvailability?sampleId='+$scope.sampleId }).
		success(function(data, status, headers, config) {
			$scope.availabilityData = data;
		}).
		error(function(data, status, headers, config) {
		// called asynchronously if an error occurs
		// or server returns response with an error status.
		//alert(data);
			$scope.authErrors=data;
		});
	});    

	$scope.ok = function () {
		$modalInstance.close();
	};

    });
