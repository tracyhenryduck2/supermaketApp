var url="";
var model;
var request;
var Denglu;



(function(){
  var app=angular.module('Denglu',[ ]);
        app.config(function($httpProvider) {
        $httpProvider.defaults.headers.put['Content-Type'] = 'application/x-www-form-urlencoded';
        $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
        
    // Override $http service's default transformRequest
    $httpProvider.defaults.transformRequest = [function(data) {
        /**
         * The workhorse; converts an object to x-www-form-urlencoded serialization.
         * @param {Object} obj
         * @return {String}
         */
         var param = function(obj) {
            var query = '';
            var name, value, fullSubName, subName, subValue, innerObj, i;
            
            for (name in obj) {
                value = obj[name];
                
                if (value instanceof Array) {
                    for (i = 0; i < value.length; ++i) {
                        subValue = value[i];
                        fullSubName = name + '[' + i + ']';
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += param(innerObj) + '&';
                    }
                } else if (value instanceof Object) {
                    for (subName in value) {
                        subValue = value[subName];
                        fullSubName = name + '[' + subName + ']';
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += param(innerObj) + '&';
                    }
                } else if (value !== undefined && value !== null) {
                    query += encodeURIComponent(name) + '='
                    + encodeURIComponent(value) + '&';
                }
            }
            
            return query.length ? query.substr(0, query.length - 1) : query;
        };
        
        return angular.isObject(data) && String(data) !== '[object File]'
        ? param(data)
        : data;
    }];
});
  app.controller('DengluController',['$http','$window','$scope',function($http,$window,$scope){
    Denglu=this;

    model=Denglu;
    request=$http;

 

   
   Denglu.clear=function(){
    Denglu.username='';
   }


    Denglu.submit=function(){


             if(Denglu.username==null || Denglu.username=="")
            {
                progress("Error","昵称不能为空");
                return;
            }


     
            progress("Show","请稍后...");

             $http({url:'http://'+url+'/super_market/app/SuperMarketNew!modNickname.action',method:'post',data:{"uid":Denglu.user.id,"nickname":Denglu.username}}).success(function(data){
               progress("Dismiss");
             if(data.result==1)
             {
                client.saveUserName(Denglu.username);
                client.saveNotGlobalInfo("update","1");
                progress("Success","修改成功","goPrevious();");
             }
             else 
             {
                progress("Error","修改失败");
             }
        }).error(function(data, status, headers, config){
            progress("Dismiss");
          if((status >= 200 && status < 300 ) || status === 304 || status === 1223 || status === 0)
          {
            progress("Error","网络访问出错!");
          }
        }) ;
    }



  }]);
})();


function addNativeOK(){
    url = getPath();
    model.user = getUser();
}

function extraAction(){
    model.submit();
}


