'use strict';

angular.module('bbillingApp')
	.controller('ProductsDeleteController', function($scope, $modalInstance, entity, Products) {

        $scope.products = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Products.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });