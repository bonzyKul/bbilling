'use strict';

angular.module('bbillingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('holiday', {
                parent: 'entity',
                url: '/holidays',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.holiday.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/holiday/holidays.html',
                        controller: 'HolidayController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('holiday');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('holiday.detail', {
                parent: 'entity',
                url: '/holiday/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'bbillingApp.holiday.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/holiday/holiday-detail.html',
                        controller: 'HolidayDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('holiday');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Holiday', function($stateParams, Holiday) {
                        return Holiday.get({id : $stateParams.id});
                    }]
                }
            })
            .state('holiday.new', {
                parent: 'holiday',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/holiday/holiday-dialog.html',
                        controller: 'HolidayDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    holidayDate: null,
                                    holidayDesc: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('holiday', null, { reload: true });
                    }, function() {
                        $state.go('holiday');
                    })
                }]
            })
            .state('holiday.edit', {
                parent: 'holiday',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/holiday/holiday-dialog.html',
                        controller: 'HolidayDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Holiday', function(Holiday) {
                                return Holiday.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('holiday', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('holiday.delete', {
                parent: 'holiday',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/holiday/holiday-delete-dialog.html',
                        controller: 'HolidayDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Holiday', function(Holiday) {
                                return Holiday.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('holiday', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
