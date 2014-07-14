'use strict';
var app = angular.module('angularApp')
    .controller('SampleDataAvailabilityCtrl', function ($rootScope,$scope,$http,$filter,$routeParams,$modalInstance,sampleId) {

    $scope.sampleId = sampleId;
    $scope.availabilityData = {"sampleId":0,"sampleName":"NCL-23-1","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":"caNanoLab: 50%; MINChar: 33%","createdDate":null,"keywords":null,"pointOfContactMap":{},"pocBeanDomainId":0,"availableEntityNames":["sample composition","solubility","molecular weight","blood contact","size","physical state","nanomaterial entities","purity","publications","functionalizing entities","cytotoxicity","immune cell function","general sample information","sample function","association"],"caNanoLabScore":"50.0% (15 out of 30)","mincharScore":"33.0% (3 out of 9)","chemicalAssocs":[null],"physicoChars":["molecular weight","physical state","purity","relaxivity","shape","size","solubility","surface"],"invitroChars":["blood contact","cytotoxicity","enzyme induction","immune cell function","metabolic stability","oxidative stress","sterility","targeting","transfection"],"caNanoMINChar":{"zeta potential":"surface charge","sample composition":"chemical composition","purity":"purity","surface charge":"surface charge","shape":"shape","attachment":"surface chemistry","surface area":"surface area","size":"particle size/size distribution"}};
    $scope.ok = function () {
    $modalInstance.close();
    };

    });
