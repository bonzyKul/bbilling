'use strict';

angular.module('bbillingApp')
	.controller('BranchDeleteController', function($scope, $modalInstance, entity, Branch) {

        $scope.branch = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Branch.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });