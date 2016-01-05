'use strict';

angular.module('bbillingApp')
	.controller('AccountAppHLDDeleteController', function($scope, $modalInstance, entity, AccountAppHLD) {

        $scope.accountAppHLD = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            AccountAppHLD.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });