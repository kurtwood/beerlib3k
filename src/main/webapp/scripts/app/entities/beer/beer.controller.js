'use strict';

angular.module('beerlib3kApp')
    .controller('BeerController', function ($scope, Beer, ParseLinks) {
        $scope.beers = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Beer.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.beers.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.beers = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Beer.get({id: id}, function(result) {
                $scope.beer = result;
                $('#deleteBeerConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Beer.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteBeerConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.beer = {beer_id: null, name: null, description: null, type_id: null, brewery_id: null, id: null};
        };
    });
