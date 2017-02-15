(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('CustomerController', CustomerController);

    CustomerController.$inject = ['$scope', '$state', 'Customer'];

    function CustomerController ($scope, $state, Customer) {
        var vm = this;

        vm.customers = [];

        loadAll();

        function loadAll() {
            Customer.query(function(result) {
                vm.customers = result;
                vm.searchQuery = null;
            });
        }
    }
})();
