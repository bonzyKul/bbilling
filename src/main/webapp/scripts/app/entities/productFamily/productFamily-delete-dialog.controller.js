'use strict';

angular.module('bbillingApp')
	.controller('ProductFamilyDeleteController', function($scope, $modalInstance, entity, ProductFamily) {

        $scope.productFamily = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ProductFamily.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });