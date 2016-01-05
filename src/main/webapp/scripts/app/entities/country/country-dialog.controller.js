'use strict';

angular.module('bbillingApp').controller('CountryDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Country', 'Currency',
        function($scope, $stateParams, $modalInstance, entity, Country, Currency) {

        $scope.country = entity;
        $scope.currencys = Currency.query();
        $scope.load = function(id) {
            Country.get({id : id}, function(result) {
                $scope.country = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('bbillingApp:countryUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.country.id != null) {
                Country.update($scope.country, onSaveSuccess, onSaveError);
            } else {
                Country.save($scope.country, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
