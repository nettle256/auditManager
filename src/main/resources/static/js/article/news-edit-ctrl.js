/**
 * Created by Nettle on 2017/3/4.
 */

auditManagerApp
    .config(['$stateProvider', function($stateProvider) {
        $stateProvider
            .state('article_new', {
                url: '/articles/new',
                templateUrl: '/html/article/newsEdit',
                controller: 'newsEditCtrl'
            })
            .state('article_edit', {
                url: '/articles/{id:[0-9]+}/edit',
                templateUrl: '/html/article/newsEdit',
                controller: 'newsEditCtrl'
            });
    }])
    .controller('newsEditCtrl', function ($scope, $http, $stateParams, $state, Upload, themeService) {

        $scope.modified = false;
        themeService.getThemeList().then(function (data) {
            $scope.themes = data;
            $scope.themes.splice(0, 0, {id: 0, name: "无分类"});
        });

        if ($stateParams.id) {
            $scope.id = $stateParams.id;
            $scope.isNew = false;
            $http
                .get(['api','news',$scope.id].join('/'))
                .then(function (result) {
                    $scope.news = result.data;
                    if (!$scope.news.images || $scope.news.images.length < 1)
                        $scope.news.imagesDTO = [];
                    else $scope.news.imagesDTO = JSON.parse($scope.news.images);
                    if (!$scope.news.attachments || $scope.news.attachments.length < 1)
                        $scope.news.attachmentsDTO = [];
                    else $scope.news.attachmentsDTO = JSON.parse($scope.news.attachments);
                    $scope.modified = false;
                }, function (result) {
                });
        }   else {
            $scope.isNew = true;
            $scope.news = {
                name: '',
                content: '',
                theme: 0,
                imagesDTO: [],
                images: '',
                attachmentsDTO: [],
                attachments: ''
            }
        }

        $scope.deletePhoto = function (id) {
            mdui.dialog({
                title: '确定要删除该图片？',
                content: '<img src="/photo/' + $scope.news.imagesDTO[id].uri + '" style="max-width: 100%;" />',
                buttons: [
                    {
                        text: '取消'
                    },
                    {
                        text: '确认',
                        onClick: function(){
                            $scope.news.imagesDTO.splice(id, 1);
                            mdui.snackbar({
                                message: '已删除图片'
                            });
                        }
                    }
                ]
            })
        };

        $scope.deleteAttachment = function (id) {
            mdui.dialog({
                content: '<div class="mdui-typo"><p>确定要删除附件 <strong>' + $scope.news.attachmentsDTO[id].name + '</strong> ？</p></div>',
                buttons: [
                    {
                        text: '取消'
                    },
                    {
                        text: '确认',
                        onClick: function(){
                            $scope.news.attachmentsDTO.splice(id, 1);
                            mdui.snackbar({
                                message: '已删除附件'
                            });
                        }
                    }
                ]
            })
        };

        $scope.save = function() {
            $scope.news.images = JSON.stringify($scope.news.imagesDTO);
            $scope.news.attachments = JSON.stringify($scope.news.attachmentsDTO);
            if ($scope.isNew) {
                $http
                    .post(['api','news'].join('/'), $scope.news)
                    .then(function () {
                        $state.go('articles');
                    }, function () {
                        mdui.snackbar({
                            message: '新建文章失败'
                        });
                    });
            }   else {
                $http
                    .put(['api','news',$scope.news.id].join('/'), $scope.news)
                    .then(function () {
                        $state.go('articles');
                    }, function () {
                        mdui.snackbar({
                            message: '保存文章失败'
                        });
                    });
            }
        };

        $scope.cancel = function () {
            mdui.dialog({
                content: '确定要放弃' + ($scope.isNew ? '新建模板' : '修改'),
                buttons: [
                    {
                        text: '取消'
                    },
                    {
                        text: '确认',
                        onClick: function(){
                            $state.go('articles');
                        }
                    }
                ]
            })
        };

        // upload on file select or drop
        $scope.uploading = false;
        $scope.upload = function (file, type) {
            $scope.uploading = true;
            Upload.upload({
                url: 'api/upload/' + type,
                data: {file: file, 'username': $scope.username}
            }).then(function (resp) {
                // console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);
                mdui.snackbar({
                    message: '成功上传',
                    timeout: 1000
                });
                console.log(resp);
                if (type == "image") {
                    $scope.news.imagesDTO.push(resp.data);
                }   else if (type == "document") {
                    $scope.news.attachmentsDTO.push(resp.data);
                }
                $scope.uploading = false;
            }, function (resp) {
                // console.log('Error status: ' + resp.status);
                mdui.snackbar({
                    message: '上传失败'
                });
                $scope.uploading = false;
            }, function (evt) {
                $scope.progress = parseInt(100.0 * evt.loaded / evt.total);
                // var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                // console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
            });
        };

    });
