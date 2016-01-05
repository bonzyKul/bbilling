'use strict';

angular.module('bbillingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('accounts', {
                parent: 'entity',
                url: '/accountss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.accounts.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/accounts/accountss.html',
                        controller: 'AccountsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('accounts');
                        $translatePartialLoader.addPart('accountType');
                        $translatePartialLoader.addPart('accountStatus');
                        $translatePartialLoader.addPart('accountLastBalType');
                        $translatePartialLoader.addPart('accountTier');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('accounts.detail', {
                parent: 'entity',
                url: '/accounts/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.accounts.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/accounts/accounts-detail.html',
                        controller: 'AccountsDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('accounts');
                        $translatePartialLoader.addPart('accountType');
                        $translatePartialLoader.addPart('accountStatus');
                        $translatePartialLoader.addPart('accountLastBalType');
                        $translatePartialLoader.addPart('accountTier');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Accounts', function($stateParams, Accounts) {
                        return Accounts.get({id : $stateParams.id});
                    }]
                }
            })
            .state('accounts.new', {
                parent: 'accounts',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/accounts/accounts-dialog.html',
                        controller: 'AccountsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    accountNumber: null,
                                    accountType: null,
                                    accountOpenedDate: null,
                                    accountClosedDate: null,
                                    accountStatus: null,
                                    accountCRTurnOver: null,
                                    accountDRTurnOver: null,
                                    accountAvailBal: null,
                                    accountLedgerBal: null,
                                    accountBalance: null,
                                    accountLastBalType: null,
                                    accountTier: null,
                                    accountChargingBal: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('accounts', null, { reload: true });
                    }, function() {
                        $state.go('accounts');
                    })
                }]
            })
            .state('accounts.edit', {
                parent: 'accounts',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/accounts/accounts-dialog.html',
                        controller: 'AccountsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Accounts', function(Accounts) {
                                return Accounts.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('accounts', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('accounts.delete', {
                parent: 'accounts',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/accounts/accounts-delete-dialog.html',
                        controller: 'AccountsDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Accounts', function(Accounts) {
                                return Accounts.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('accounts', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
