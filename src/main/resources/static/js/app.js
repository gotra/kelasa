var kelasaApp = angular.module('kelasaApp', [
    'ngRoute',
]);

kelasaApp.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
        when('/login', {
            templateUrl: 'partials/login.html',
            controller: 'baseCtrl'
        }).
        when('/phones/:phoneId', {
            templateUrl: 'partials/phone-detail.html',
            controller: 'PhoneDetailCtrl'
        }).
        otherwise({
            redirectTo: '/phones'
        });
    }]);
kelasaApp.controller('baseCtrl',['$scope','$http',
    function($scope, $http){
        //do nothing for the moment
    }
]);