'use strict';

angular.module('bbillingApp')
	.controller('PricingDeleteController', function($scope, $modalInstance, entity, Pricing) {

        $scope.pricing = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Pricing.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });