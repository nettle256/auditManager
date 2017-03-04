/**
 * Created by Nettle on 2017/2/7.
 */

auditManagerApp
    .service('themeService', function ($http) {

        function deal(themeList) {
            var themeTree = [
                {"id":0,"name":"首页","father":null,"unmodifiable":true}
            ];
            var themeMap = {};
            themeList.forEach(function (theme) {
                themeMap[theme.id] = theme;
                if (!theme.father)
                    theme.child = [];
            });
            themeList.forEach(function (theme) {
                if (!theme.father)
                    themeTree.push(theme);
                else
                    if (themeMap[theme.father])
                        themeMap[theme.father].child.push(theme);
            });
            return themeTree;
        }

       this.getThemeTree = function() {
           return $http
               .get(['api', 'theme'].join('/'))
               .then(function (result) {
                   return deal(result.data);
               }, function (result) {

               });
       };
       
       this.getThemeList = function () {
           return $http
               .get(['api', 'theme'].join('/'))
               .then(function (result) {
                   var themeMap = {};
                   result.data.forEach(function (theme) {
                       themeMap[theme.id] = theme;
                       theme.hasChild = false;
                   });
                   result.data.forEach(function (theme) {
                      if (theme.father)
                          themeMap[theme.father].hasChild = true;
                   });
                   return result.data.filter(function (theme) {
                       return !theme.hasChild && theme.module == 0;
                   });
               }, function (result) {

               });
       };
    });