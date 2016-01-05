'use strict';

angular.module('bbillingApp').controller('AccountPreferencesDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'AccountPreferences', 'Accounts',
        function($scope, $stateParams, $modalInstance, entity, AccountPreferences, Accounts) {

        $scope.accountPreferences = entity;
        $scope.accountss = Accounts.query();
        $scope.load = function(id) {
            AccountPreferences.get({id : id}, function(result) {
                $scope.accountPreferences = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('bbillingApp:accountPreferencesUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.accountPreferences.id != null) {
                AccountPreferences.update($scope.accountPreferences, onSaveSuccess, onSaveError);
            } else {
                AccountPreferences.save($scope.accountPreferences, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
