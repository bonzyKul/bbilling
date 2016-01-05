'use strict';

angular.module('bbillingApp')
	.controller('AccountFamilyDeleteController', function($scope, $modalInstance, entity, AccountFamily) {

        $scope.accountFamily = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            AccountFamily.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });