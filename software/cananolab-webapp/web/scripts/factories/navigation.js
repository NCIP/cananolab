'use strict';

var app = angular.module('angularApp');
app.factory('navigationService', function($resource){
	return $resource('/caNanoLab/rest/core/getTabs');
});
