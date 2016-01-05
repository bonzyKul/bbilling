'use strict';

angular.module('bbillingApp').controller('CustomerDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Customer', 'CustomerAddress', 'Country',
        function($scope, $stateParams, $modalInstance, entity, Customer, CustomerAddress, Country) {

        $scope.customer = entity;
        $scope.customeraddresss = CustomerAddress.query();
        $scope.countrys = Country.query();
        $scope.load = function(id) {
            Customer.get({id : id}, function(result) {
                $scope.customer = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('bbillingApp:customerUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.customer.id != null) {
                Customer.update($scope.customer, onSaveSuccess, onSaveError);
            } else {
                Customer.save($scope.customer, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
