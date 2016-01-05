'use strict';

angular.module('bbillingApp')
    .controller('CurrencyController', function ($scope, $state, $modal, Currency, CurrencySearch, ParseLinks) {
      
        $scope.currencys = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Currency.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.currencys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            CurrencySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.currencys = result;
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
            $scope.currency = {
                currencyCode: null,
                currencyDescription: null,
                id: null
            };
        };
    });
