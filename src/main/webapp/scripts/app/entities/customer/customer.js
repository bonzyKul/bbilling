'use strict';

angular.module('bbillingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('customer', {
                parent: 'entity',
                url: '/customers',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.customer.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/customer/customers.html',
                        controller: 'CustomerController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('customer');
                        $translatePartialLoader.addPart('customerStatus');
                        $translatePartialLoader.addPart('customerType');
                        $translatePartialLoader.addPart('customerTier');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('customer.detail', {
                parent: 'entity',
                url: '/customer/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.customer.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/customer/customer-detail.html',
                        controller: 'CustomerDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('customer');
                        $translatePartialLoader.addPart('customerStatus');
                        $translatePartialLoader.addPart('customerType');
                        $translatePartialLoader.addPart('customerTier');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Customer', function($stateParams, Customer) {
                        return Customer.get({id : $stateParams.id});
                    }]
                }
            })
            .state('customer.new', {
                parent: 'customer',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/customer/customer-dialog.html',
                        controller: 'CustomerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    customerID: null,
                                    customerFName: null,
                                    customerMName: null,
                                    customerLName: null,
                                    customerStartDate: null,
                                    customerEndDate: null,
                                    customerStatus: null,
                                    customerType: null,
                                    customerEmail: null,
                                    customerTelNo: null,
                                    customerMobileNo: null,
                                    customerTier: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('customer', null, { reload: true });
                    }, function() {
                        $state.go('customer');
                    })
                }]
            })
            .state('customer.edit', {
                parent: 'customer',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/customer/customer-dialog.html',
                        controller: 'CustomerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Customer', function(Customer) {
                                return Customer.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('customer', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('customer.delete', {
                parent: 'customer',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/customer/customer-delete-dialog.html',
                        controller: 'CustomerDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Customer', function(Customer) {
                                return Customer.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('customer', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
