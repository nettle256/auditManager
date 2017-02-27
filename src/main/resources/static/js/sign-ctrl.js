/**
 * Created by Nettle on 2017/1/6.
 */

angular.module('signApp', [])
    .controller('signCtrl', ['$scope', '$http', function ($scope, $http) {
        $scope.newUser = {
            username: null,
            password: null,
            repeat: null
        };

        $scope.signIn = function () {
            $http
                .post(['api','login'].join('/'), $scope.user)
                .then(function (result) {
                    window.location.reload();
                }, function (result) {
                    $scope.message = result.data.message;
                });
        };

        $scope.checkPasswordSame = function () {
            $scope.isPasswordSame = ($scope.newUser.password === $scope.newUser.repeat);
        }
    }]);