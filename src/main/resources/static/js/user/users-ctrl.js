/**
 * Created by Nettle on 2017/2/7.
 */

auditManagerApp
    .config(['$stateProvider', function($stateProvider) {
        $stateProvider
            .state('users', {
                url: '/users',
                templateUrl: '/html/user/users',
                controller: 'usersCtrl'
            });
    }])
    .controller('usersCtrl', function ($scope, $http) {

        $scope.filterCondition = {
            deleted: false
        };

        $http
            .get(['api','user','all'].join('/'))
            .then(function (result) {
                $scope.users = result.data;
            }, function (result) {
            });
        
        $scope.setFilter = function (type) {
            if (type == 'all') {
                $scope.filterCondition = {};
            }
            if (type == 'undeleted') {
                $scope.filterCondition = {
                    deleted: false
                };
            }
            if (type == 'deleted') {
                $scope.filterCondition = {
                    deleted: true
                };
            }
        };

        $scope.updateAuthority = function (user, type) {
            $http
                .put(['api','user',user.id,'authority'].join('/'), user)
                .then(function (result) {
                    user = result.data;
                }, function (result) {
                    mdui.snackbar({
                        message: '修改用户' + user.username + '权限失败'
                    });
                    user[type] = !user[type];
                });
        };

        $scope.resetPassword = function (user) {
            mdui.dialog({
                content: '<div class="mdui-typo"><p>确定要将用户 <strong>' + user.username + '</strong> 的密码重置为用户名？</p></div>',
                buttons: [
                    {
                        text: '取消'
                    },
                    {
                        text: '确认',
                        onClick: function(){
                            $http
                                .put(['api','user',user.id,'reset'].join('/'), {})
                                .then(function (result) {
                                    mdui.snackbar({
                                        message: '已重置用户' + user.username + '密码为用户名'
                                    });
                                }, function (result) {
                                    mdui.snackbar({
                                        message: '重置用户' + user.username + '密码失败'
                                    });
                                });
                        }
                    }
                ]
            })
        };

        $scope.deleteUser = function (user) {
            mdui.dialog({
                content: '<div class="mdui-typo"><p>确定要删除用户 <strong>' + user.username + '</strong> ？</p></div>',
                buttons: [
                    {
                        text: '取消'
                    },
                    {
                        text: '确认',
                        onClick: function(){
                            $http
                                .delete(['api','user',user.id].join('/'), {})
                                .then(function (result) {
                                    user.deleted = true;
                                    mdui.snackbar({
                                        message: '已删除用户' + user.username
                                    });
                                }, function (result) {
                                    mdui.snackbar({
                                        message: '删除用户' + user.username + '失败'
                                    });
                                });
                        }
                    }
                ]
            })
        };

        $scope.recoverUser = function (user) {
            mdui.dialog({
                content: '<div class="mdui-typo"><p>确定要恢复用户 <strong>' + user.username + '</strong> ？</p></div>',
                buttons: [
                    {
                        text: '取消'
                    },
                    {
                        text: '确认',
                        onClick: function(){
                            $http
                                .put(['api','user',user.id,'recover'].join('/'), {})
                                .then(function (result) {
                                    user.deleted = false;
                                    mdui.snackbar({
                                        message: '已恢复用户' + user.username
                                    });
                                }, function (result) {
                                    mdui.snackbar({
                                        message: '恢复用户' + user.username + '失败'
                                    });
                                });
                        }
                    }
                ]
            })
        };

        /*
         * new user
         */
        
        $scope.clearNewUser = function () {
            $scope.newUser = {
                username: '',
                articleSystem: false,
                posterSystem: false
            };
        };

        $scope.createUser = function () {
            $http
                .post(['api', 'user'].join('/'), $scope.newUser)
                .then(function (result) {
                    mdui.snackbar({
                        message: '成功创建新用户 ' + $scope.newUser.username
                    });
                    $scope.users.push(result.data);
                }, function (result) {
                    mdui.snackbar({
                        message: '创建新用户 ' + $scope.newUser.username + ' 失败'
                    });
                })
        }
    });