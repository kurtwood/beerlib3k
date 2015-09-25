'use strict';

angular.module('beerlib3kApp').controller('BreweryDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Brewery',
        function($scope, $stateParams, $modalInstance, entity, Brewery) {

        $scope.brewery = entity;
        $scope.load = function(id) {
            Brewery.get({id : id}, function(result) {
                $scope.brewery = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('beerlib3kApp:breweryUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.brewery.id != null) {
                Brewery.update($scope.brewery, onSaveFinished);
            } else {
                Brewery.save($scope.brewery, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
