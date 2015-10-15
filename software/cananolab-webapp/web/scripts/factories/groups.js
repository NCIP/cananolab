'use strict';

var app = angular.module('angularApp');
app.factory('groupService', function($resource,$http){
	var that = this;
	return {
		getGroups: {data:$resource('/caNanoLab/rest/security/getUserGroups') },
		isCurator: function(groups) {
			that.curator = 0;
			$http({method: 'GET', url: '/caNanoLab/rest/security/getUserGroups' }).
			success(function(data, status, headers, config) {

			var x = data;
			for (var key in x) {
			  for (var y=0;y<x[key].length;y++) {
			    if(x[key][y]=='Curator') {
			    		that.curator = 1;
			    };
			  };
			}
			}).
			error(function(data, status, headers, config) {
				that.curator = "0";
			});
			return that;

		},
		getUserName: function() {
			that.name = '';
			$http({method: 'GET', url: '/caNanoLab/rest/security/getUserGroups' }).
				success(function(data, status, headers, config) {
				var x = data;
				for (var key in x) {
				  that.name = key;
				}
			}).
			error(function(data, status, headers, config) {
				that.name = '';
			});
			return that;

		}
	}	
});
