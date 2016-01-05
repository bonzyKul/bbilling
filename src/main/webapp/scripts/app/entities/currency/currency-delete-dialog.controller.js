'use strict';

angular.module('bbillingApp')
	.controller('CurrencyDeleteController', function($scope, $modalInstance, entity, Currency) {

        $scope.currency = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Currency.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });