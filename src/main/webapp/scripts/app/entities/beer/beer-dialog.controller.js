'use strict';

angular.module('beerlib3kApp').controller('BeerDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Beer',
        function($scope, $stateParams, $modalInstance, entity, Beer) {

        $scope.beer = entity;
        $scope.load = function(id) {
            Beer.get({id : id}, function(result) {
                $scope.beer = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('beerlib3kApp:beerUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.beer.id != null) {
                Beer.update($scope.beer, onSaveFinished);
            } else {
                Beer.save($scope.beer, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
