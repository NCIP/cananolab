'use strict';
var app = angular.module('angularApp')
    .controller('SampleDataAvailabilityCtrl', function ($rootScope,$scope,$http,$filter,$routeParams,$modalInstance,sampleId) {

    $scope.sampleId = sampleId;
    $scope.availabilityData = {"sampleId":0,"sampleName":"GATECH_UCSF-EDickersonCL2008-01","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":"caNanoLab: 23%; MINChar: 22%","createdDate":null,"keywords":null,"pointOfContactInfo":null,"pocBeanDomainId":0,"availableEntityNames":["functionalizing entities","sample function","general sample information","size","sample composition","publications","nanomaterial entities"],"caNanoLabScore":"23.0% (7 out of 30)","mincharScore":"22.0% (2 out of 9)","chemicalAssocs":[null],"physicoChars":[null],"invitroChars":[null],"caNanoMINChar":{"zeta potential":"surface charge","sample composition":"chemical composition","purity":"purity","surface charge":"surface charge","shape":"shape","attachment":"surface chemistry","surface area":"surface area","size":"particle size/size distribution"},"otherPOCBeans":[]};
    $scope.ok = function () {
    $modalInstance.close();
    };

    });
