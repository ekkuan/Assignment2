(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('SalesPersonDeleteController',SalesPersonDeleteController);

    SalesPersonDeleteController.$inject = ['$uibModalInstance', 'entity', 'SalesPerson'];

    function SalesPersonDeleteController($uibModalInstance, entity, SalesPerson) {
        var vm = this;

        vm.salesPerson = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SalesPerson.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
