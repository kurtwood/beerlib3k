'use strict';

angular.module('beerlib3kApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('beer', {
                parent: 'entity',
                url: '/beers',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Beers'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/beer/beers.html',
                        controller: 'BeerController'
                    }
                },
                resolve: {
                }
            })
            .state('beer.detail', {
                parent: 'entity',
                url: '/beer/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Beer'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/beer/beer-detail.html',
                        controller: 'BeerDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Beer', function($stateParams, Beer) {
                        return Beer.get({id : $stateParams.id});
                    }]
                }
            })
            .state('beer.new', {
                parent: 'beer',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/beer/beer-dialog.html',
                        controller: 'BeerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {beer_id: null, name: null, description: null, type_id: null, brewery_id: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('beer', null, { reload: true });
                    }, function() {
                        $state.go('beer');
                    })
                }]
            })
            .state('beer.edit', {
                parent: 'beer',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/beer/beer-dialog.html',
                        controller: 'BeerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Beer', function(Beer) {
                                return Beer.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('beer', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
