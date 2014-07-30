'use strict';
var app = angular.module('angularApp');
app.factory("publicationService", function(){
	// Service keeps data search results in memory to pass between controllers //
	return {
		publicationData: {data: null }
		}

});