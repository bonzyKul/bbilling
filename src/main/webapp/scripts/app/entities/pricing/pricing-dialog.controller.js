'use strict';

angular.module('bbillingApp').controller('PricingDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Pricing', 'Products', 'Currency', 'PricingTier', 'ServiceCatalogue',
        function($scope, $stateParams, $modalInstance, entity, Pricing, Products, Currency, PricingTier, ServiceCatalogue) {

        $scope.pricing = entity;
        $scope.productss = Products.query();
        $scope.currencys = Currency.query();
        $scope.pricingtiers = PricingTier.query();
        $scope.servicecatalogues = ServiceCatalogue.query();
        $scope.load = function(id) {
            Pricing.get({id : id}, function(result) {
                $scope.pricing = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('bbillingApp:pricingUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.pricing.id != null) {
                Pricing.update($scope.pricing, onSaveSuccess, onSaveError);
            } else {
                Pricing.save($scope.pricing, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
