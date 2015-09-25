'use strict';

angular.module('beerlib3kApp')
    .controller('TypeController', function ($scope, Type, ParseLinks) {
        $scope.types = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Type.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.types.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.types = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Type.get({id: id}, function(result) {
                $scope.type = result;
                $('#deleteTypeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Type.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteTypeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.type = {type_id: null, name: null, id: null};
        };
    });
