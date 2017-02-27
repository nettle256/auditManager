/**
 * Created by Nettle on 2017/2/7.
 */

auditManagerApp
    .config(['$stateProvider', function($stateProvider) {
        $stateProvider
            .state('theme', {
                url: '/theme',
                templateUrl: '/html/article/theme',
                controller: 'themeCtrl'
            });
    }])
    .controller('themeCtrl', function ($scope, $http, themeService) {
        themeService.getThemeTree().then(function (data) {
            $scope.themeTree = data;
        });

        $scope.moduleName = {};

        $http
            .get(['api', 'module'].join('/'))
            .then(function (result) {
                $scope.modules = [{
                    name: '文章列表',
                    id: 0
                }].concat(result.data);
                $scope.modules.forEach(function (module) {
                    $scope.moduleName[module.id] = module.name;
                })
            }, function () {

            });

        $scope.clearNewTheme = function (father) {
            $scope.newTheme = {
                name: '',
                father: father ? father.id : null,
                module: 0,
                fatherObject: father
            }
        };

        $scope.createTheme = function () {
            $http
                .post(['api', 'theme'].join('/'), $scope.newTheme)
                .then(function (result) {
                    mdui.snackbar({
                        message: '成功创建栏目 ' + $scope.newTheme.name
                    });
                    if (result.data.father)
                        $scope.themeTree.forEach(function (theme) {
                            if (theme.id == result.data.father) {
                                if (!theme.child) theme.child = [];
                                theme.child.push(result.data);
                            }
                        });
                    else {
                        $scope.themeTree.push(result.data);
                    }

                }, function (result) {
                    mdui.snackbar({
                        message: '创建栏目 ' + $scope.newTheme.name + ' 失败'
                    });
                })
        };
        
        $scope.edit = function (theme) {
            $scope.editTheme = theme;
            $scope.editTheme.newName = $scope.editTheme.name;
            $scope.editTheme.newModule = $scope.editTheme.module;
        };
        
        $scope.updateTheme = function () {
            $http
                .put(['api','theme',$scope.editTheme.id].join('/'), {
                    name: $scope.editTheme.newName,
                    father: $scope.editTheme.father,
                    module: $scope.editTheme.newModule
                })
                .then(function (result) {
                    mdui.snackbar({
                        message: '成功更新栏目' + $scope.editTheme.name
                    });
                    $scope.editTheme.name = result.data.name;
                    $scope.editTheme.module = result.data.module;
                }, function (result) {
                    mdui.snackbar({
                        message: '编辑栏目 ' + $scope.editTheme.name + ' 失败'
                    });
                });
        };
        
        $scope.deleteTheme = function (theme) {
            mdui.dialog({
                content: '<div class="mdui-typo"><p>确定要删除栏目 <strong>' + theme.name + '</strong> ？</p></div>',
                buttons: [
                    {
                        text: '取消'
                    },
                    {
                        text: '确认',
                        onClick: function(){
                            $http
                                .delete(['api','theme',theme.id].join('/'), {})
                                .then(function (result) {
                                    theme.deleted = true;
                                    mdui.snackbar({
                                        message: '已删除栏目' + theme.name
                                    });
                                }, function (result) {
                                    mdui.snackbar({
                                        message: '删除栏目' + theme.name + '失败'
                                    });
                                });
                        }
                    }
                ]
            })
        }
    });