var model;
var Login;
var request;
var url;
var code;

(function(){
    var app = angular.module('Login', []);
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

app.controller('LoginController',['$http','$window','$scope', function($http,$window,$scope){
    Login=this;
    model = Login;
    request=$http;
     
    Login.tohtml = GetQueryString("tohtml");
   


    Login.topickcode=function(){

    }

    Login.register=function(){
         client.open("register.html",1);
    }

    Login.weixinLogin=function(){
      progress("Show");
        client.LoginFromWX();
    }

    Login.LoginIn=function(){
           if(Login.username==null || Login.username=='')
           {
             progress("Error","请输入用户名");
             return;
           }

           if(Login.password==null || Login.password=='')
           {
             progress("Error","请输入密码");
             return;
           } 
     
            progress("Show");
             //$http({url:'http://' + '120.26.110.125/jncloud'+ '/app/Index!ajaxUserLogin.action',method:'post',data:{"uname":Login.username,"password":Login.password}}).success(function(data){
             $http({url:'http://' + url+ '/super_market/app/SuperMarketNew!login.action',method:'post',data:{"username":Login.username,"password":Login.password}}).success(function(data){
              progress("Dismiss");
             if(data.result==1)
             {
                client.setUserJson(JSON.stringify(data.user));
                client.setLoginInfo(Login.username,Login.password);
                progress("Success","登录成功!","goPrevious();");
                if(Login.tohtml!="") client.open(Login.tohtml,1);
             }
             else
             {
                progress("Error","登录失败");
             }
        }).error(function(data, status, headers, config){
            progress("Dismiss");
          if((status >= 200 && status < 300 ) || status === 304 || status === 1223 || status === 0)
          {
            progress("Error","网络访问出错!");
          }
        }) ;
    }

    Login.submitWeixinLogin=function(){

     
        progress("Show");
             $http({url:'https://api.weixin.qq.com/sns/oauth2/access_token',method:'post',data:{"appid":client.getApp_Id(),"secret":client.getApp_Secret(),"code":code, "grant_type":"authorization_code"}}).success(function(data){
     var  Token=data.access_token;
     var  OPENID=data.openid;

             $http({url:'https://api.weixin.qq.com/sns/userinfo',method:'post',data:{"access_token":Token,"openid":OPENID}}).success(function(data){
      var avatar=data.headimgurl;
      var nickname=data.nickname;
             
             $http({url:'http://'+url+'/super_market/app/SuperMarketNew!weixlogin.action',method:'post',data:{"openid":OPENID,"avatar":avatar,"nickname":nickname}}).success(function(data){

              progress("Dismiss");
          if(data.result==1){
                client.setUserJson(JSON.stringify(data.user));
                client.setWXLoginInfo(OPENID,avatar,nickname);
                progress("Success","登录成功!","goPrevious();");
                if(Login.tohtml!="") client.open(Login.tohtml,1);
          }
          else{
            progress("Error","登录失败");
          }
             

        }).error(function(data, status, headers, config){
            progress("Dismiss");
          if((status >= 200 && status < 300 ) || status === 304 || status === 1223 || status === 0)
          {
            progress("Error","网络访问出错!");
          }
        }) ;




        }).error(function(data, status, headers, config){
            progress("Dismiss");
          if((status >= 200 && status < 300 ) || status === 304 || status === 1223 || status === 0)
          {
            progress("Error","网络访问出错!");
          }
        }) ;


             


        }).error(function(data, status, headers, config){
            progress("Dismiss");
          if((status >= 200 && status < 300 ) || status === 304 || status === 1223 || status === 0)
          {
            progress("Error","网络访问出错!");
          }
        }) ;


    }


     Login.init=function(username,psw){
      $scope.$apply(function(){
         Login.username = username;
         Login.password = psw;
      });
    }

}]);
})();

function addNativeOK(){
	url=getPath();
   var uname = client.readGlobalInfo("username");
   var cd    = client.readGlobalInfo("password"); 
    model.init(uname,cd);
    code = client.readNotGlobalInfo("code");
    if(code!=null){
    
         client.saveNotGlobalInfo("code",null);
         model.submitWeixinLogin();
    }
}

function onMyResume(){
    progress("Dismiss");
    code = client.readNotGlobalInfo("code");
    if(code!=null){
    
         client.saveNotGlobalInfo("code",null);
         model.submitWeixinLogin();
    }

}