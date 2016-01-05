'use strict';

angular.module('bbillingApp').controller('HolidayDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Holiday', 'Country',
        function($scope, $stateParams, $modalInstance, entity, Holiday, Country) {

        $scope.holiday = entity;
        $scope.countrys = Country.query();
        $scope.load = function(id) {
            Holiday.get({id : id}, function(result) {
                $scope.holiday = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('bbillingApp:holidayUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.holiday.id != null) {
                Holiday.update($scope.holiday, onSaveSuccess, onSaveError);
            } else {
                Holiday.save($scope.holiday, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
