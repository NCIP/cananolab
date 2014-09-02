'use strict';
var app = angular.module('angularApp')
	.controller('EditCharacterizationModalCtrl', function ($scope,$http,$modalInstance,sampleId, sampleData,message,type, sampleService) {

    // define variables //
	$scope.sampleId = sampleId;
    $scope.sampleData = sampleData;
    $scope.characterizationData = {};
    // $scope.data = {"type":"physico-chemical characterization","name":null,"parentSampleId":0,"charId":0,"assayType":null,"protocolNameVersion":null,"characterizationSource":null,"characterizationDate":null,"charNamesForCurrentType":["molecular weight","other_pc","physical state","purity","relaxivity","shape","size","solubility","surface","zeta potential"],"designMethodsDescription":null,"charTypesLookup":["physico-chemical characterization","in vitro characterization","in vivo characterization","ex vivo","[other]"],"protocolLookup":[{"domainId":29949960,"domainFileId":29655055,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-13.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-13 (PCC-13), version 1.1"},{"domainId":29949959,"domainFileId":29655054,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-12.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-12 (PCC-12), version 1.1"},{"domainId":29949958,"domainFileId":29655053,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-14.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-14 (PCC-14), version 1.0"},{"domainId":29949957,"domainFileId":29655052,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-11.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-11 (PCC-11), version 1.1"},{"domainId":29949956,"domainFileId":29655050,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-9.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-9 (PCC-9), version 1.1"},{"domainId":29949955,"domainFileId":29655049,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-8.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-8 (PCC-8), version 1.1"},{"domainId":29949954,"domainFileId":29655048,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-10.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-10 (PCC-10), version 1.1"},{"domainId":29949953,"domainFileId":29655047,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-7.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-7 (PCC-7), version 1.1"},{"domainId":29949952,"domainFileId":29655046,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-6.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-6 (PCC-6), version 1.1"},{"domainId":25210112,"domainFileId":25177344,"domainFileUri":"http://ncl.cancer.gov/NCL_Method_PCC-1.pdf","displayName":"NIST - NCL Joint Assay Protocol, PCC-1 (PCC-1), version 1.1"}],"charSourceLookup":[{"id":52985856,"displayName":"C-Sixty (CNI) (John Doe)"},{"id":56328198,"displayName":"DC"},{"id":15695880,"displayName":"CP_UCLA_CalTech"}],"otherSampleNameLookup":["NCL-23-1-SY-CharTest","SY-NCL-23-1"],"assayTypesByCharNameLookup":{}}    
    $scope.message = message;
    $scope.loader = false;
    $scope.loaderMessage = "Loading";
    $scope.type = type;

    $scope.loader = true;
    $http({method: 'GET', url: '/caNanoLab/rest/characterization/setupAdd?sampleId='+$scope.sampleId+'&charType='+$scope.type}).
        success(function(data, status, headers, config) {
        $scope.data = data;
        $scope.data.charTypesLookup.push('[other]');
        $scope.data.charNamesForCurrentType.push('[other]');
        $scope.loader = false;
    }).
        error(function(data, status, headers, config) {
        $scope.loader = false;        
    });



    // cancel save //
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

 });