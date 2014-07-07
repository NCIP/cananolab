'use strict';

var app = angular.module('angularApp');
app.factory('navigationFactory', function($resource){
	return $resource('/caNanoLab/rest/core/getTabs');
});
