'use strict';
var app = angular.module('angularApp');
app.factory("keywordService", function(){
    // Service keeps keyword search results in memory to pass between controllers //
    return {
        keywordData: {data: null }
    }

});