(function() {
    'use strict';
    angular
        .module('assignment2App')
        .factory('SalesPerson', SalesPerson);

    SalesPerson.$inject = ['$resource'];

    function SalesPerson ($resource) {
        var resourceUrl =  'api/sales-people/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
