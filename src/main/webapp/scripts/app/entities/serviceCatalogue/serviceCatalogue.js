'use strict';

angular.module('bbillingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('serviceCatalogue', {
                parent: 'entity',
                url: '/serviceCatalogues',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.serviceCatalogue.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/serviceCatalogue/serviceCatalogues.html',
                        controller: 'ServiceCatalogueController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('serviceCatalogue');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('serviceCatalogue.detail', {
                parent: 'entity',
                url: '/serviceCatalogue/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.serviceCatalogue.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/serviceCatalogue/serviceCatalogue-detail.html',
                        controller: 'ServiceCatalogueDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('serviceCatalogue');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ServiceCatalogue', function($stateParams, ServiceCatalogue) {
                        return ServiceCatalogue.get({id : $stateParams.id});
                    }]
                }
            })
            .state('serviceCatalogue.new', {
                parent: 'serviceCatalogue',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/serviceCatalogue/serviceCatalogue-dialog.html',
                        controller: 'ServiceCatalogueDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    serviceCode: null,
                                    serviceDescription: null,
                                    serviceStartDate: null,
                                    serviceEndDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('serviceCatalogue', null, { reload: true });
                    }, function() {
                        $state.go('serviceCatalogue');
                    })
                }]
            })
            .state('serviceCatalogue.edit', {
                parent: 'serviceCatalogue',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/serviceCatalogue/serviceCatalogue-dialog.html',
                        controller: 'ServiceCatalogueDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ServiceCatalogue', function(ServiceCatalogue) {
                                return ServiceCatalogue.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('serviceCatalogue', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('serviceCatalogue.delete', {
                parent: 'serviceCatalogue',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/serviceCatalogue/serviceCatalogue-delete-dialog.html',
                        controller: 'ServiceCatalogueDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ServiceCatalogue', function(ServiceCatalogue) {
                                return ServiceCatalogue.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('serviceCatalogue', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
