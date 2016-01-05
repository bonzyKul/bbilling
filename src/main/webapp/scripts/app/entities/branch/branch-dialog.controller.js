'use strict';

angular.module('bbillingApp').controller('BranchDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Branch',
        function($scope, $stateParams, $modalInstance, entity, Branch) {

        $scope.branch = entity;
        $scope.load = function(id) {
            Branch.get({id : id}, function(result) {
                $scope.branch = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('bbillingApp:branchUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.branch.id != null) {
                Branch.update($scope.branch, onSaveSuccess, onSaveError);
            } else {
                Branch.save($scope.branch, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
