'use strict';

angular.module('beerlib3kApp')
    .controller('BreweryDetailController', function ($scope, $rootScope, $stateParams, entity, Brewery) {
        $scope.brewery = entity;
        $scope.load = function (id) {
            Brewery.get({id: id}, function(result) {
                $scope.brewery = result;
            });
        };
        $rootScope.$on('beerlib3kApp:breweryUpdate', function(event, result) {
            $scope.brewery = result;
        });
    });
