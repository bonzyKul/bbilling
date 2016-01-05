'use strict';

angular.module('bbillingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('bank', {
                parent: 'entity',
                url: '/banks',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.bank.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/bank/banks.html',
                        controller: 'BankController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('bank');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('bank.detail', {
                parent: 'entity',
                url: '/bank/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.bank.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/bank/bank-detail.html',
                        controller: 'BankDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('bank');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Bank', function($stateParams, Bank) {
                        return Bank.get({id : $stateParams.id});
                    }]
                }
            })
            .state('bank.new', {
                parent: 'bank',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/bank/bank-dialog.html',
                        controller: 'BankDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    bankCode: null,
                                    bankName: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('bank', null, { reload: true });
                    }, function() {
                        $state.go('bank');
                    })
                }]
            })
            .state('bank.edit', {
                parent: 'bank',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/bank/bank-dialog.html',
                        controller: 'BankDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Bank', function(Bank) {
                                return Bank.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('bank', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('bank.delete', {
                parent: 'bank',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/bank/bank-delete-dialog.html',
                        controller: 'BankDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Bank', function(Bank) {
                                return Bank.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('bank', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
