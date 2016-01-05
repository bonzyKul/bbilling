'use strict';

angular.module('bbillingApp')
	.controller('ServiceCatalogueDeleteController', function($scope, $modalInstance, entity, ServiceCatalogue) {

        $scope.serviceCatalogue = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ServiceCatalogue.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });