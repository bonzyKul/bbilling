'use strict';

angular.module('bbillingApp')
	.controller('BankDeleteController', function($scope, $modalInstance, entity, Bank) {

        $scope.bank = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Bank.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });