'use strict';

angular.module('beerlib3kApp').controller('TypeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Type',
        function($scope, $stateParams, $modalInstance, entity, Type) {

        $scope.type = entity;
        $scope.load = function(id) {
            Type.get({id : id}, function(result) {
                $scope.type = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('beerlib3kApp:typeUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.type.id != null) {
                Type.update($scope.type, onSaveFinished);
            } else {
                Type.save($scope.type, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
