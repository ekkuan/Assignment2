(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('CustomerDeleteController',CustomerDeleteController);

    CustomerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Customer'];

    function CustomerDeleteController($uibModalInstance, entity, Customer) {
        var vm = this;

        vm.customer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Customer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
