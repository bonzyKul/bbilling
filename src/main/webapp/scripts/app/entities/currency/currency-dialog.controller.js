'use strict';

angular.module('bbillingApp').controller('CurrencyDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Currency', 'Country',
        function($scope, $stateParams, $modalInstance, entity, Currency, Country) {

        $scope.currency = entity;
        $scope.countrys = Country.query();
        $scope.load = function(id) {
            Currency.get({id : id}, function(result) {
                $scope.currency = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('bbillingApp:currencyUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.currency.id != null) {
                Currency.update($scope.currency, onSaveSuccess, onSaveError);
            } else {
                Currency.save($scope.currency, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
