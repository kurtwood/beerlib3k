'use strict';

angular.module('beerlib3kApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('type', {
                parent: 'entity',
                url: '/types',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Types'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/type/types.html',
                        controller: 'TypeController'
                    }
                },
                resolve: {
                }
            })
            .state('type.detail', {
                parent: 'entity',
                url: '/type/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Type'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/type/type-detail.html',
                        controller: 'TypeDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Type', function($stateParams, Type) {
                        return Type.get({id : $stateParams.id});
                    }]
                }
            })
            .state('type.new', {
                parent: 'type',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/type/type-dialog.html',
                        controller: 'TypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {type_id: null, name: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('type', null, { reload: true });
                    }, function() {
                        $state.go('type');
                    })
                }]
            })
            .state('type.edit', {
                parent: 'type',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/type/type-dialog.html',
                        controller: 'TypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Type', function(Type) {
                                return Type.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('type', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
