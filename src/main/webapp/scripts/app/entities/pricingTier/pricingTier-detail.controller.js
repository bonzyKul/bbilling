'use strict';

angular.module('bbillingApp')
    .controller('PricingTierDetailController', function ($scope, $rootScope, $stateParams, entity, PricingTier, Pricing) {
        $scope.pricingTier = entity;
        $scope.load = function (id) {
            PricingTier.get({id: id}, function(result) {
                $scope.pricingTier = result;
            });
        };
        var unsubscribe = $rootScope.$on('bbillingApp:pricingTierUpdate', function(event, result) {
            $scope.pricingTier = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
