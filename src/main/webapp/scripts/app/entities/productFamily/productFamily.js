'use strict';

angular.module('bbillingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('productFamily', {
                parent: 'entity',
                url: '/productFamilys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.productFamily.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/productFamily/productFamilys.html',
                        controller: 'ProductFamilyController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('productFamily');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('productFamily.detail', {
                parent: 'entity',
                url: '/productFamily/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.productFamily.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/productFamily/productFamily-detail.html',
                        controller: 'ProductFamilyDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('productFamily');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ProductFamily', function($stateParams, ProductFamily) {
                        return ProductFamily.get({id : $stateParams.id});
                    }]
                }
            })
            .state('productFamily.new', {
                parent: 'productFamily',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/productFamily/productFamily-dialog.html',
                        controller: 'ProductFamilyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    productFamilyCode: null,
                                    productFamilyDesc: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('productFamily', null, { reload: true });
                    }, function() {
                        $state.go('productFamily');
                    })
                }]
            })
            .state('productFamily.edit', {
                parent: 'productFamily',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/productFamily/productFamily-dialog.html',
                        controller: 'ProductFamilyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ProductFamily', function(ProductFamily) {
                                return ProductFamily.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('productFamily', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('productFamily.delete', {
                parent: 'productFamily',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/productFamily/productFamily-delete-dialog.html',
                        controller: 'ProductFamilyDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ProductFamily', function(ProductFamily) {
                                return ProductFamily.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('productFamily', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
