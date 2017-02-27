/**
 * Created by Nettle on 2017/2/7.
 */

auditManagerApp
    .config(['$stateProvider', function($stateProvider) {
        $stateProvider
            .state('profile', {
                url: '/profile',
                templateUrl: '/html/user/profile',
                controller: 'profileCtrl'
            });
    }])
    .controller('profileCtrl', function ($scope, $http) {

        $scope.pwd = {
            oldPassword: '',
            newPassword: '',
            repeatPassword: ''
        };

        $scope.saving = false;

        $scope.checkPasswordSame = function () {
            $scope.isPasswordSame = ($scope.pwd.newPassword === $scope.pwd.repeatPassword);
        };

        $scope.save = function () {
            $scope.saving = true;
            $http
                .put(['api', 'user'].join('/'), $scope.pwd)
                .then(function (result) {
                    mdui.snackbar({
                        message: '修改密码成功'
                    });
                    $scope.saving = false;
                }, function (result) {
                    mdui.snackbar({
                        message: '修改密码失败'
                    });
                    $scope.saving = false;
                });
        }

    });