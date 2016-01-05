'use strict';

angular.module('bbillingApp').controller('ProductsDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Products', 'ProductFamily', 'ServiceCatalogue',
        function($scope, $stateParams, $modalInstance, entity, Products, ProductFamily, ServiceCatalogue) {

        $scope.products = entity;
        $scope.productfamilys = ProductFamily.query();
        $scope.servicecatalogues = ServiceCatalogue.query();
        $scope.load = function(id) {
            Products.get({id : id}, function(result) {
                $scope.products = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('bbillingApp:productsUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.products.id != null) {
                Products.update($scope.products, onSaveSuccess, onSaveError);
            } else {
                Products.save($scope.products, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
