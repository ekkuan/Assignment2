(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('SalesPersonController', SalesPersonController);

    SalesPersonController.$inject = ['$scope', '$state', 'SalesPerson'];

    function SalesPersonController ($scope, $state, SalesPerson) {
        var vm = this;

        vm.salesPeople = [];

        loadAll();

        function loadAll() {
            SalesPerson.query(function(result) {
                vm.salesPeople = result;
                vm.searchQuery = null;
            });
        }
    }
})();
