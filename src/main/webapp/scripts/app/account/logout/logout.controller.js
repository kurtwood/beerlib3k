'use strict';

angular.module('beerlib3kApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
