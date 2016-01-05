'use strict';

angular.module('bbillingApp')
	.controller('CustomerAddressDeleteController', function($scope, $modalInstance, entity, CustomerAddress) {

        $scope.customerAddress = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            CustomerAddress.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });