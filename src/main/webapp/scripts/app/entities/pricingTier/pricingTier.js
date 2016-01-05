'use strict';

angular.module('bbillingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('pricingTier', {
                parent: 'entity',
                url: '/pricingTiers',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.pricingTier.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pricingTier/pricingTiers.html',
                        controller: 'PricingTierController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pricingTier');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('pricingTier.detail', {
                parent: 'entity',
                url: '/pricingTier/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.pricingTier.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pricingTier/pricingTier-detail.html',
                        controller: 'PricingTierDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pricingTier');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'PricingTier', function($stateParams, PricingTier) {
                        return PricingTier.get({id : $stateParams.id});
                    }]
                }
            })
            .state('pricingTier.new', {
                parent: 'pricingTier',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/pricingTier/pricingTier-dialog.html',
                        controller: 'PricingTierDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    pricingTierFrom: null,
                                    pricingTierTo: null,
                                    pricingTierValue: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('pricingTier', null, { reload: true });
                    }, function() {
                        $state.go('pricingTier');
                    })
                }]
            })
            .state('pricingTier.edit', {
                parent: 'pricingTier',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/pricingTier/pricingTier-dialog.html',
                        controller: 'PricingTierDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['PricingTier', function(PricingTier) {
                                return PricingTier.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('pricingTier', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('pricingTier.delete', {
                parent: 'pricingTier',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/pricingTier/pricingTier-delete-dialog.html',
                        controller: 'PricingTierDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['PricingTier', function(PricingTier) {
                                return PricingTier.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('pricingTier', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
