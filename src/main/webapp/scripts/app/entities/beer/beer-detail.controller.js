'use strict';

angular.module('beerlib3kApp')
    .controller('BeerDetailController', function ($scope, $rootScope, $stateParams, entity, Beer) {
        $scope.beer = entity;
        $scope.load = function (id) {
            Beer.get({id: id}, function(result) {
                $scope.beer = result;
            });
        };
        $rootScope.$on('beerlib3kApp:beerUpdate', function(event, result) {
            $scope.beer = result;
        });
    });
