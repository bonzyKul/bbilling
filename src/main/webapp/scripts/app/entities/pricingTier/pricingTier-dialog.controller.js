'use strict';

angular.module('bbillingApp').controller('PricingTierDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'PricingTier', 'Pricing',
        function($scope, $stateParams, $modalInstance, entity, PricingTier, Pricing) {

        $scope.pricingTier = entity;
        $scope.pricings = Pricing.query();
        $scope.load = function(id) {
            PricingTier.get({id : id}, function(result) {
                $scope.pricingTier = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('bbillingApp:pricingTierUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.pricingTier.id != null) {
                PricingTier.update($scope.pricingTier, onSaveSuccess, onSaveError);
            } else {
                PricingTier.save($scope.pricingTier, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
