'use strict';

angular.module('bbillingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('accountAppHLD', {
                parent: 'entity',
                url: '/accountAppHLDs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.accountAppHLD.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/accountAppHLD/accountAppHLDs.html',
                        controller: 'AccountAppHLDController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('accountAppHLD');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('accountAppHLD.detail', {
                parent: 'entity',
                url: '/accountAppHLD/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.accountAppHLD.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/accountAppHLD/accountAppHLD-detail.html',
                        controller: 'AccountAppHLDDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('accountAppHLD');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'AccountAppHLD', function($stateParams, AccountAppHLD) {
                        return AccountAppHLD.get({id : $stateParams.id});
                    }]
                }
            })
            .state('accountAppHLD.new', {
                parent: 'accountAppHLD',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/accountAppHLD/accountAppHLD-dialog.html',
                        controller: 'AccountAppHLDDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    appHLDStartDate: null,
                                    appHLDEndDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('accountAppHLD', null, { reload: true });
                    }, function() {
                        $state.go('accountAppHLD');
                    })
                }]
            })
            .state('accountAppHLD.edit', {
                parent: 'accountAppHLD',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/accountAppHLD/accountAppHLD-dialog.html',
                        controller: 'AccountAppHLDDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['AccountAppHLD', function(AccountAppHLD) {
                                return AccountAppHLD.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('accountAppHLD', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('accountAppHLD.delete', {
                parent: 'accountAppHLD',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/accountAppHLD/accountAppHLD-delete-dialog.html',
                        controller: 'AccountAppHLDDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['AccountAppHLD', function(AccountAppHLD) {
                                return AccountAppHLD.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('accountAppHLD', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
