'use strict';

angular.module('bbillingApp')
    .controller('PricingTierController', function ($scope, $state, $modal, PricingTier, PricingTierSearch, ParseLinks) {
      
        $scope.pricingTiers = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            PricingTier.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.pricingTiers = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            PricingTierSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.pricingTiers = result;
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
            $scope.pricingTier = {
                pricingTierFrom: null,
                pricingTierTo: null,
                pricingTierValue: null,
                id: null
            };
        };
    });
