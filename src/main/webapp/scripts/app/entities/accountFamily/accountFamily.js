'use strict';

angular.module('bbillingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('accountFamily', {
                parent: 'entity',
                url: '/accountFamilys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.accountFamily.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/accountFamily/accountFamilys.html',
                        controller: 'AccountFamilyController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('accountFamily');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('accountFamily.detail', {
                parent: 'entity',
                url: '/accountFamily/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.accountFamily.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/accountFamily/accountFamily-detail.html',
                        controller: 'AccountFamilyDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('accountFamily');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'AccountFamily', function($stateParams, AccountFamily) {
                        return AccountFamily.get({id : $stateParams.id});
                    }]
                }
            })
            .state('accountFamily.new', {
                parent: 'accountFamily',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/accountFamily/accountFamily-dialog.html',
                        controller: 'AccountFamilyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    accountFamilyCode: null,
                                    accountFamilyDesc: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('accountFamily', null, { reload: true });
                    }, function() {
                        $state.go('accountFamily');
                    })
                }]
            })
            .state('accountFamily.edit', {
                parent: 'accountFamily',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/accountFamily/accountFamily-dialog.html',
                        controller: 'AccountFamilyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['AccountFamily', function(AccountFamily) {
                                return AccountFamily.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('accountFamily', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('accountFamily.delete', {
                parent: 'accountFamily',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/accountFamily/accountFamily-delete-dialog.html',
                        controller: 'AccountFamilyDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['AccountFamily', function(AccountFamily) {
                                return AccountFamily.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('accountFamily', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
