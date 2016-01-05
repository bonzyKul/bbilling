'use strict';

angular.module('bbillingApp')
    .controller('PricingDetailController', function ($scope, $rootScope, $stateParams, entity, Pricing, Products, Currency, PricingTier, ServiceCatalogue) {
        $scope.pricing = entity;
        $scope.load = function (id) {
            Pricing.get({id: id}, function(result) {
                $scope.pricing = result;
            });
        };
        var unsubscribe = $rootScope.$on('bbillingApp:pricingUpdate', function(event, result) {
            $scope.pricing = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
