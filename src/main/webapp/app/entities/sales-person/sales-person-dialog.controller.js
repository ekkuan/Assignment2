(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('SalesPersonDialogController', SalesPersonDialogController);

    SalesPersonDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SalesPerson', 'Car'];

    function SalesPersonDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SalesPerson, Car) {
        var vm = this;

        vm.salesPerson = entity;
        vm.clear = clear;
        vm.save = save;
        vm.cars = Car.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.salesPerson.id !== null) {
                SalesPerson.update(vm.salesPerson, onSaveSuccess, onSaveError);
            } else {
                SalesPerson.save(vm.salesPerson, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('assignment2App:salesPersonUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
