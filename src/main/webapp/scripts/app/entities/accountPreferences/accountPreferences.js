'use strict';

angular.module('bbillingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('accountPreferences', {
                parent: 'entity',
                url: '/accountPreferencess',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.accountPreferences.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/accountPreferences/accountPreferencess.html',
                        controller: 'AccountPreferencesController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('accountPreferences');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('accountPreferences.detail', {
                parent: 'entity',
                url: '/accountPreferences/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.accountPreferences.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/accountPreferences/accountPreferences-detail.html',
                        controller: 'AccountPreferencesDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('accountPreferences');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'AccountPreferences', function($stateParams, AccountPreferences) {
                        return AccountPreferences.get({id : $stateParams.id});
                    }]
                }
            })
            .state('accountPreferences.new', {
                parent: 'accountPreferences',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/accountPreferences/accountPreferences-dialog.html',
                        controller: 'AccountPreferencesDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    accountEStatement: false,
                                    accountNotification: false,
                                    accountLvlBilling: false,
                                    accountPrintStmt: false,
                                    accountRewardSuppInd: false,
                                    accountPackCnt: null,
                                    accountCFInd: false,
                                    accountPNCS: false,
                                    accountBillingDefault: false,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('accountPreferences', null, { reload: true });
                    }, function() {
                        $state.go('accountPreferences');
                    })
                }]
            })
            .state('accountPreferences.edit', {
                parent: 'accountPreferences',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/accountPreferences/accountPreferences-dialog.html',
                        controller: 'AccountPreferencesDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['AccountPreferences', function(AccountPreferences) {
                                return AccountPreferences.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('accountPreferences', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('accountPreferences.delete', {
                parent: 'accountPreferences',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/accountPreferences/accountPreferences-delete-dialog.html',
                        controller: 'AccountPreferencesDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['AccountPreferences', function(AccountPreferences) {
                                return AccountPreferences.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('accountPreferences', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
