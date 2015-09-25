'use strict';

angular.module('beerlib3kApp')
    .controller('TypeDetailController', function ($scope, $rootScope, $stateParams, entity, Type) {
        $scope.type = entity;
        $scope.load = function (id) {
            Type.get({id: id}, function(result) {
                $scope.type = result;
            });
        };
        $rootScope.$on('beerlib3kApp:typeUpdate', function(event, result) {
            $scope.type = result;
        });
    });
