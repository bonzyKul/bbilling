'use strict';

angular.module('bbillingApp').controller('AccountsDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'Accounts', 'Products', 'Branch', 'Bank', 'Country', 'Currency', 'Customer', 'AccountFamily',
        function($scope, $stateParams, $modalInstance, $q, entity, Accounts, Products, Branch, Bank, Country, Currency, Customer, AccountFamily) {

        $scope.accounts = entity;
        $scope.productss = Products.query({filter: 'accounts-is-null'});
        $q.all([$scope.accounts.$promise, $scope.productss.$promise]).then(function() {
            if (!$scope.accounts.productsId) {
                return $q.reject();
            }
            return Products.get({id : $scope.accounts.productsId}).$promise;
        }).then(function(products) {
            $scope.productss.push(products);
        });
        $scope.branchs = Branch.query();
        $scope.banks = Bank.query();
        $scope.countrys = Country.query();
        $scope.currencys = Currency.query();
        $scope.customers = Customer.query();
        $scope.accountfamilys = AccountFamily.query();
        $scope.load = function(id) {
            Accounts.get({id : id}, function(result) {
                $scope.accounts = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('bbillingApp:accountsUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.accounts.id != null) {
                Accounts.update($scope.accounts, onSaveSuccess, onSaveError);
            } else {
                Accounts.save($scope.accounts, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
