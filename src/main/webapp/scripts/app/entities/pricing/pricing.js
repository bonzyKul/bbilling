'use strict';

angular.module('bbillingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('pricing', {
                parent: 'entity',
                url: '/pricings',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.pricing.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pricing/pricings.html',
                        controller: 'PricingController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pricing');
                        $translatePartialLoader.addPart('pricingType');
                        $translatePartialLoader.addPart('pricingRateType');
                        $translatePartialLoader.addPart('pricingUnitType');
                        $translatePartialLoader.addPart('pricingAmountType');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('pricing.detail', {
                parent: 'entity',
                url: '/pricing/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.pricing.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pricing/pricing-detail.html',
                        controller: 'PricingDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pricing');
                        $translatePartialLoader.addPart('pricingType');
                        $translatePartialLoader.addPart('pricingRateType');
                        $translatePartialLoader.addPart('pricingUnitType');
                        $translatePartialLoader.addPart('pricingAmountType');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Pricing', function($stateParams, Pricing) {
                        return Pricing.get({id : $stateParams.id});
                    }]
                }
            })
            .state('pricing.new', {
                parent: 'pricing',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/pricing/pricing-dialog.html',
                        controller: 'PricingDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    pricingType: null,
                                    pricingChargeAmount: null,
                                    pricingRateType: null,
                                    pricingUnit: null,
                                    pricingStartDate: null,
                                    pricingEndDate: null,
                                    pricingUnitType: null,
                                    pricingAmountType: null,
                                    pricingForStaff: false,
                                    pricingTaxIndicator: false,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('pricing', null, { reload: true });
                    }, function() {
                        $state.go('pricing');
                    })
                }]
            })
            .state('pricing.edit', {
                parent: 'pricing',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/pricing/pricing-dialog.html',
                        controller: 'PricingDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Pricing', function(Pricing) {
                                return Pricing.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('pricing', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('pricing.delete', {
                parent: 'pricing',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/pricing/pricing-delete-dialog.html',
                        controller: 'PricingDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Pricing', function(Pricing) {
                                return Pricing.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('pricing', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
