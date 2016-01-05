'use strict';

angular.module('bbillingApp').controller('CustomerAddressDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'CustomerAddress', 'Country', 'Customer',
        function($scope, $stateParams, $modalInstance, entity, CustomerAddress, Country, Customer) {

        $scope.customerAddress = entity;
        $scope.countrys = Country.query();
        $scope.customers = Customer.query();
        $scope.load = function(id) {
            CustomerAddress.get({id : id}, function(result) {
                $scope.customerAddress = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('bbillingApp:customerAddressUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.customerAddress.id != null) {
                CustomerAddress.update($scope.customerAddress, onSaveSuccess, onSaveError);
            } else {
                CustomerAddress.save($scope.customerAddress, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
