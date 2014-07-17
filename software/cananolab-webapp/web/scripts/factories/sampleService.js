'use strict';
var app = angular.module('angularApp');
app.factory("sampleService", function(){
	// Service keeps data search results and current sample in memory to pass between controllers //
	return {
		sampleData: {data: null },
		sampleId: {data: null }  	
	}

});

app.factory("sortService", function(){
	// Service unsorts hashmap. Angular auto sorts //

	return {
		getUnsorted: function(hash) {
	        if (!hash) {
	            return [];
	        }
	        return Object.keys(hash);			
		}
	}

});