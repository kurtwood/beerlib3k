'use strict';

angular.module('beerlib3kApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('brewery', {
                parent: 'entity',
                url: '/brewerys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Brewerys'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/brewery/brewerys.html',
                        controller: 'BreweryController'
                    }
                },
                resolve: {
                }
            })
            .state('brewery.detail', {
                parent: 'entity',
                url: '/brewery/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Brewery'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/brewery/brewery-detail.html',
                        controller: 'BreweryDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Brewery', function($stateParams, Brewery) {
                        return Brewery.get({id : $stateParams.id});
                    }]
                }
            })
            .state('brewery.new', {
                parent: 'brewery',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/brewery/brewery-dialog.html',
                        controller: 'BreweryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {brewery_id: null, name: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('brewery', null, { reload: true });
                    }, function() {
                        $state.go('brewery');
                    })
                }]
            })
            .state('brewery.edit', {
                parent: 'brewery',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/brewery/brewery-dialog.html',
                        controller: 'BreweryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Brewery', function(Brewery) {
                                return Brewery.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('brewery', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
