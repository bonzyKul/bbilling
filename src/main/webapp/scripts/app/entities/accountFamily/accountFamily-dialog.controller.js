'use strict';

angular.module('bbillingApp').controller('AccountFamilyDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'AccountFamily', 'Accounts',
        function($scope, $stateParams, $modalInstance, entity, AccountFamily, Accounts) {

        $scope.accountFamily = entity;
        $scope.accountss = Accounts.query();
        $scope.load = function(id) {
            AccountFamily.get({id : id}, function(result) {
                $scope.accountFamily = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('bbillingApp:accountFamilyUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.accountFamily.id != null) {
                AccountFamily.update($scope.accountFamily, onSaveSuccess, onSaveError);
            } else {
                AccountFamily.save($scope.accountFamily, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
