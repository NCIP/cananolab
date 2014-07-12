'use strict';
var app = angular.module('angularApp');
app.factory("sampleService", function(){

  return {
  	sampleData: {data: null },
  	sampleId: {data: null }  	
   }

});