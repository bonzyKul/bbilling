'use strict';

angular.module('bbillingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('customerAddress', {
                parent: 'entity',
                url: '/customerAddresss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.customerAddress.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/customerAddress/customerAddresss.html',
                        controller: 'CustomerAddressController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('customerAddress');
                        $translatePartialLoader.addPart('customerAddressType');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('customerAddress.detail', {
                parent: 'entity',
                url: '/customerAddress/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.customerAddress.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/customerAddress/customerAddress-detail.html',
                        controller: 'CustomerAddressDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('customerAddress');
                        $translatePartialLoader.addPart('customerAddressType');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'CustomerAddress', function($stateParams, CustomerAddress) {
                        return CustomerAddress.get({id : $stateParams.id});
                    }]
                }
            })
            .state('customerAddress.new', {
                parent: 'customerAddress',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/customerAddress/customerAddress-dialog.html',
                        controller: 'CustomerAddressDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    customerAddType: null,
                                    customerAddLineOne: null,
                                    customerAddLineTwo: null,
                                    customerCity: null,
                                    customerState: null,
                                    customerAddZip: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('customerAddress', null, { reload: true });
                    }, function() {
                        $state.go('customerAddress');
                    })
                }]
            })
            .state('customerAddress.edit', {
                parent: 'customerAddress',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/customerAddress/customerAddress-dialog.html',
                        controller: 'CustomerAddressDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CustomerAddress', function(CustomerAddress) {
                                return CustomerAddress.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('customerAddress', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('customerAddress.delete', {
                parent: 'customerAddress',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/customerAddress/customerAddress-delete-dialog.html',
                        controller: 'CustomerAddressDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['CustomerAddress', function(CustomerAddress) {
                                return CustomerAddress.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('customerAddress', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
