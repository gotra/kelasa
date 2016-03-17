var kelasaApp = angular.module('kelasaApp', [
    'ngRoute'
]);

kelasaApp.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
        when('/home', {
            templateUrl: 'partials/home.html',
            controller: 'baseCtrl'
        }).
        when('/services', {
            templateUrl: 'partials/services.html',
            controller: 'baseCtrl'
        }).
        otherwise({
            redirectTo: '/home'
        });
    }]);

kelasaApp.controller('baseCtrl',['$scope','$http',
    function($scope, $http){
        //do nothing for the moment
    }
]);

kelasaApp.factory('SearchService',["$location","$http",function($location, $http){
    var SearchService;
    SearchService = {};

    // The array that will contain search results
    SearchService.arrSearchResults = [];

    // The search term (for decoration)
    SearchService.searchTerm = "";

    // Control if user searched recently
    SearchService.userSearched = false;



    // Clear the search
    SearchService.clearSearch = function() {
        SearchService.searchTerm = "";
        SearchService.arrSearchResults = [];
        SearchService.userSearched = false;
    };

    // Search function
    SearchService.submitSearch = function(aSearchTerm) {

        // Make sure aSearchTerm has content (always good to double check)
        if(aSearchTerm !== "") {

            // Alter URL to show new request
            $location.search('q', aSearchTerm);
            SearchService.searchTerm = aSearchTerm;

            // Determine URL to request based on search type/state
            requestUrl = "";
            if (SearchService.typeOfSearch == "web") {
                requestUrl = "results-web.json";
            }
            else if (SearchService.typeOfSearch == "image") {
                requestUrl = "results-image.json";
            }

            console.log("Making request to ", requestUrl);

            // Make a GET request to your URL that will
            // return data for you to populate
            $http.get(requestUrl).
            success(function(data, status, headers, config) {


                SearchService.userSearched = true;
                console.log(data, status, headers, config);

                // this callback will be called asynchronously
                // when the response is available

                // Assuming the data returned is a list of items
                // or object items
                // (i.e. [ "Search Result1", "Search Result2", ... ]
                SearchService.arrSearchResults = data;

            }).
            error(function(data, status, headers, config) {

                SearchService.userSearched = true;
                console.log(data, status, headers, config);

                // called asynchronously if an error occurs
                // or server returns response with an error status.

                // Empty the array of search results
                // to show no results
                SearchService.arrSearchResults = [];
            });
        }
    }

    return SearchService;
}]);