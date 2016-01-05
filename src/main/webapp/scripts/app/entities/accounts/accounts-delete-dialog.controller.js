'use strict';

angular.module('bbillingApp')
	.controller('AccountsDeleteController', function($scope, $modalInstance, entity, Accounts) {

        $scope.accounts = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Accounts.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });