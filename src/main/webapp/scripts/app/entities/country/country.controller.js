'use strict';

angular.module('bbillingApp')
    .controller('CountryController', function ($scope, $state, $modal, Country, CountrySearch, ParseLinks) {
      
        $scope.countrys = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Country.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.countrys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            CountrySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.countrys = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.country = {
                countryCode: null,
                countryName: null,
                id: null
            };
        };
    });
