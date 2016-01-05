'use strict';

angular.module('bbillingApp').controller('BankDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Bank',
        function($scope, $stateParams, $modalInstance, entity, Bank) {

        $scope.bank = entity;
        $scope.load = function(id) {
            Bank.get({id : id}, function(result) {
                $scope.bank = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('bbillingApp:bankUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.bank.id != null) {
                Bank.update($scope.bank, onSaveSuccess, onSaveError);
            } else {
                Bank.save($scope.bank, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
