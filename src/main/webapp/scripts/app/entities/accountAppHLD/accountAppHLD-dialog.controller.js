'use strict';

angular.module('bbillingApp').controller('AccountAppHLDDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'AccountAppHLD', 'Accounts', 'Products',
        function($scope, $stateParams, $modalInstance, entity, AccountAppHLD, Accounts, Products) {

        $scope.accountAppHLD = entity;
        $scope.accountss = Accounts.query();
        $scope.productss = Products.query();
        $scope.load = function(id) {
            AccountAppHLD.get({id : id}, function(result) {
                $scope.accountAppHLD = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('bbillingApp:accountAppHLDUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.accountAppHLD.id != null) {
                AccountAppHLD.update($scope.accountAppHLD, onSaveSuccess, onSaveError);
            } else {
                AccountAppHLD.save($scope.accountAppHLD, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
