'use strict';

angular.module('bbillingApp')
	.controller('PricingTierDeleteController', function($scope, $modalInstance, entity, PricingTier) {

        $scope.pricingTier = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            PricingTier.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });