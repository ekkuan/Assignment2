(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('SalesPersonDetailController', SalesPersonDetailController);

    SalesPersonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SalesPerson', 'Car'];

    function SalesPersonDetailController($scope, $rootScope, $stateParams, previousState, entity, SalesPerson, Car) {
        var vm = this;

        vm.salesPerson = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('assignment2App:salesPersonUpdate', function(event, result) {
            vm.salesPerson = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
