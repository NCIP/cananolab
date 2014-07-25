'use strict';

/**
 * @ngdoc overview
 * @name angularApp
 * @description
 * # angularApp
 *
 * Main module of the application.
 */

var app = angular.module('angularApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngTable',
    'ngSanitize',
    'ngTouch','ngGrid','ui.bootstrap']);


app.config(function ($routeProvider, $httpProvider) {
  $httpProvider.defaults.useXDomain = true;  
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
     .when('/register', {
       templateUrl: 'views/register.html',
       controller: 'RegisterCtrl'
     })       
      .when('/home', {
        templateUrl: 'views/loggedIn.html',
        controller: 'AuthCtrl'
      }) 
      .when('/searchSample', {
        templateUrl: 'views/sampleSearch.html',
        controller: 'SampleSearchCtrl'
      })   
      .when('/sampleResults', {
        templateUrl: 'views/sampleResults.html',
        controller: 'SampleResultsCtrl'
      })                  
      .when('/editSample', {
        templateUrl: 'views/sample/editSample.html',
        controller: 'editSampleCtrl'
      })                  
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl'
      })
      .when('/login', {
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl'
      })  
      .when('/logout', {
        templateUrl: 'views/logout.html',
        controller: 'LogoutCtrl'
      })  
      .when('/manageSample', {
       templateUrl: 'views/manageSample.html',
       controller: 'ManageSampleCtrl'
      }) 
      .when('/sample', {
       templateUrl: 'views/viewSample.html',
       controller: 'IndSampleCtrl'
      })       
      .when('/editSample', {
       templateUrl: 'views/sample/edit/editSample.html',
       controller: 'editSampleCtrl'
      })       
      .when('/composition', {
       templateUrl: 'views/composition.html',
       controller: 'CompositionCtrl'
      }) 
      .when('/characterization', {
       templateUrl: 'views/characterization.html',
       controller: 'CharacterizationCtrl'
      }) 
      .when('/publication', {
       templateUrl: 'views/publication.html',
       controller: 'PublicationCtrl'
      })    

      .when('/searchPublication', {
        templateUrl: 'views/publication/view/publicationSearch.html',
        controller: 'PublicationCtrl'
      })   
      .when('/publicationResults', {
        templateUrl: 'views/publication/view/publicationResults.html',
        controller: 'PublicationResultsCtrl'
      })                                    
      .otherwise({
        redirectTo: '/'
      });
  });
