'use strict';

angular.module('beerlib3kApp')
    .controller('BreweryController', function ($scope, Brewery, ParseLinks) {
        $scope.brewerys = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Brewery.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.brewerys.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.brewerys = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Brewery.get({id: id}, function(result) {
                $scope.brewery = result;
                $('#deleteBreweryConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Brewery.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteBreweryConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.brewery = {brewery_id: null, name: null, id: null};
        };
    });
