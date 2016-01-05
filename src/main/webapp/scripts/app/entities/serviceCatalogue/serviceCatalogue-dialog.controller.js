'use strict';

angular.module('bbillingApp').controller('ServiceCatalogueDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'ServiceCatalogue', 'Products', 'Pricing',
        function($scope, $stateParams, $modalInstance, entity, ServiceCatalogue, Products, Pricing) {

        $scope.serviceCatalogue = entity;
        $scope.productss = Products.query();
        $scope.pricings = Pricing.query();
        $scope.load = function(id) {
            ServiceCatalogue.get({id : id}, function(result) {
                $scope.serviceCatalogue = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('bbillingApp:serviceCatalogueUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.serviceCatalogue.id != null) {
                ServiceCatalogue.update($scope.serviceCatalogue, onSaveSuccess, onSaveError);
            } else {
                ServiceCatalogue.save($scope.serviceCatalogue, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
