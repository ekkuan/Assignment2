(function() {
    'use strict';

    angular
        .module('assignment2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sales-person', {
            parent: 'entity',
            url: '/sales-person',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'assignment2App.salesPerson.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sales-person/sales-people.html',
                    controller: 'SalesPersonController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('salesPerson');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sales-person-detail', {
            parent: 'entity',
            url: '/sales-person/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'assignment2App.salesPerson.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sales-person/sales-person-detail.html',
                    controller: 'SalesPersonDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('salesPerson');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SalesPerson', function($stateParams, SalesPerson) {
                    return SalesPerson.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sales-person',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sales-person-detail.edit', {
            parent: 'sales-person-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sales-person/sales-person-dialog.html',
                    controller: 'SalesPersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SalesPerson', function(SalesPerson) {
                            return SalesPerson.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sales-person.new', {
            parent: 'sales-person',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sales-person/sales-person-dialog.html',
                    controller: 'SalesPersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                cust_id: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sales-person', null, { reload: 'sales-person' });
                }, function() {
                    $state.go('sales-person');
                });
            }]
        })
        .state('sales-person.edit', {
            parent: 'sales-person',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sales-person/sales-person-dialog.html',
                    controller: 'SalesPersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SalesPerson', function(SalesPerson) {
                            return SalesPerson.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sales-person', null, { reload: 'sales-person' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sales-person.delete', {
            parent: 'sales-person',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sales-person/sales-person-delete-dialog.html',
                    controller: 'SalesPersonDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SalesPerson', function(SalesPerson) {
                            return SalesPerson.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sales-person', null, { reload: 'sales-person' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
