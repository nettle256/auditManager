/**
 * Created by Nettle on 2017/2/7.
 */

auditManagerApp
    .config(['$stateProvider', function($stateProvider) {
        $stateProvider
            .state('articles', {
                url: '/articles',
                templateUrl: '/html/article/articles',
                controller: 'articlesCtrl'
            });
    }])
    .controller('articlesCtrl', function ($scope, $http, themeService) {
        $scope.filterCondition = {
            deleted: false
        };

        $scope.themeMap = {};
        themeService.getThemeList().then(function (data) {
            $scope.themes = data;
            data.forEach(function (theme) {
                $scope.themeMap[theme.id] = theme;
            });
            $scope.themes.splice(0, 0, {id: 0, name: "显示无分类"});
            $scope.themes.splice(0, 0, {id: -1, name: "显示所有"});
        });

        $http
            .get(['api','news'].join('/'))
            .then(function (result) {
                $scope.newsList = result.data;
            }, function () {

            });

        $scope.deleteNews = function (news) {
            mdui.dialog({
                content: '<div class="mdui-typo"><p>确定要删除文章 <strong>' + news.title + '</strong> ？</p></div>',
                buttons: [
                    {
                        text: '取消'
                    },
                    {
                        text: '确认',
                        onClick: function(){
                            $http
                                .delete(['api','news',news.id].join('/'), {})
                                .then(function (result) {
                                    news.deleted = true;
                                    mdui.snackbar({
                                        message: '已删除文章'
                                    });
                                }, function (result) {
                                    mdui.snackbar({
                                        message: '删除文章' + news.title + '失败'
                                    });
                                });
                        }
                    }
                ]
            })
        };

        $scope.recoverNews = function (news) {
            mdui.dialog({
                content: '<div class="mdui-typo"><p>确定要恢复文章 <strong>' + news.title + '</strong> ？</p></div>',
                buttons: [
                    {
                        text: '取消'
                    },
                    {
                        text: '确认',
                        onClick: function(){
                            $http
                                .put(['api','news',news.id,'recover'].join('/'), {})
                                .then(function (result) {
                                    news.deleted = false;
                                    mdui.snackbar({
                                        message: '已恢复文章' + news.title
                                    });
                                }, function (result) {
                                    mdui.snackbar({
                                        message: '恢复文章' + news.title + '失败'
                                    });
                                });
                        }
                    }
                ]
            })
        };

        $scope.setFilter = function (type) {
            if (type == -1) {
                $scope.themefilterCondition = {
                };
            }   else {
                $scope.themefilterCondition = {
                    theme: type
                };
            }
        };

        $scope.setDeleteFilter = function (type) {
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
    });

