'use strict';

angular.module('bbillingApp').controller('ProductFamilyDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'ProductFamily', 'Products',
        function($scope, $stateParams, $modalInstance, entity, ProductFamily, Products) {

        $scope.productFamily = entity;
        $scope.productss = Products.query();
        $scope.load = function(id) {
            ProductFamily.get({id : id}, function(result) {
                $scope.productFamily = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('bbillingApp:productFamilyUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.productFamily.id != null) {
                ProductFamily.update($scope.productFamily, onSaveSuccess, onSaveError);
            } else {
                ProductFamily.save($scope.productFamily, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
