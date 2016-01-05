'use strict';

angular.module('bbillingApp')
    .controller('PricingController', function ($scope, $state, $modal, Pricing, PricingSearch, ParseLinks) {
      
        $scope.pricings = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Pricing.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.pricings = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            PricingSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.pricings = result;
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
            $scope.pricing = {
                pricingType: null,
                pricingChargeAmount: null,
                pricingRateType: null,
                pricingUnit: null,
                pricingStartDate: null,
                pricingEndDate: null,
                pricingUnitType: null,
                pricingAmountType: null,
                pricingForStaff: false,
                pricingTaxIndicator: false,
                id: null
            };
        };
    });
