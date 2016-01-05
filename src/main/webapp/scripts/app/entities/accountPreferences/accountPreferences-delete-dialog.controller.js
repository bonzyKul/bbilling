'use strict';

angular.module('bbillingApp')
	.controller('AccountPreferencesDeleteController', function($scope, $modalInstance, entity, AccountPreferences) {

        $scope.accountPreferences = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            AccountPreferences.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });