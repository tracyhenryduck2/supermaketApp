var url="";
var model;
var request;
var Say;
var code;
var APPID="wx5d4f201691879266";
var APPSECRET="56c0e33df3ae4ecec1e6b76d7ceaf886";
var Token;
var OPENID;
var secret;
function LoadData(){
    creatMask();
    request.post('http://'+url+'/super_market/app/SuperMarket!getCommentListInfo.action').success(function(data){
      model.saylist=data;
     removeMask();  
      }).error(function(data,status,headers,config){
       if((status>=200&&status<300)||status===304||status===1223||status===0){
       $("body").html('<div class="da"><img src="images/bj2_03.png"  alt=""/></div><div class="db">网络请求失败</div><div class="dc">请检查您的网络<br>重新加载吧</div><div class="dd"><div class="newss22"><a onclick="reload()">重新加载</a></div></div>');
       }
       });
}

function LoadToken(){
   request.post('https://api.weixin.qq.com/sns/oauth2/access_token?appid='+APPID+'&secret='+APPSECRET+'&code='+code+'&grant_type=authorization_code').success(function(data){
      Token=data.access_token;
      OPENID=data.openid;
      var secretA = {access_token:Token,openid:OPENID};
      client.saveGlobalInfo("secret",JSON.stringify(secretA));
      LoadMyInfo();
     
      }).error(function(data,status,headers,config){
       if((status>=200&&status<300)||status===304||status===1223||status===0){
       $("body").html('<div class="da"><img src="images/bj2_03.png"  alt=""/></div><div class="db">网络请求失败</div><div class="dc">请检查您的网络<br>重新加载吧</div><div class="dd"><div class="newss22"><a onclick="reload()">重新加载</a></div></div>');
       }
       });
}
function LoadMyInfo(){
   if(Token!=null&&OPENID!=null)
   request.post('https://api.weixin.qq.com/sns/userinfo?access_token='+Token+'&openid='+OPENID).success(function(data){

      model.mypic_url=data.headimgurl;
      model.nickname=data.nickname;
      var myinfo = {picurl:model.mypic_url,nickname:model.nickname};
      client.saveGlobalInfo("myinfo",JSON.stringify(myinfo));
      }).error(function(data,status,headers,config){
       if((status>=200&&status<300)||status===304||status===1223||status===0){
       $("body").html('<div class="da"><img src="images/bj2_03.png"  alt=""/></div><div class="db">网络请求失败</div><div class="dc">请检查您的网络<br>重新加载吧</div><div class="dd"><div class="newss22"><a onclick="reload()">重新加载</a></div></div>');
       }
       });
}

function initMyInfo(){
      if(secret==null || secret=="") 
      {
        LoadToken();
      }
    else 
    {
     var inf = client.readGlobalInfo("myinfo");
     var obj = eval("("+inf+")");
     model.mypic_url = obj.picurl;
     model.nickname=obj.nickname;
    }
}


(function(){
  var app=angular.module('Say',[ ]);
   app.config(function($httpProvider){
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

 app.filter("nicknameFilter",function(){
                return function(input){
                    
                    if(input=="")
                    {
                      return "匿名评论";
                    }
                    else return input;

                    
                }
            });
 
 app.filter("headimgurlFilter",function(){
            return function(input){

                 if(input==null || input =="")
                  return "images/img_04.png";
                 else
                  return input;
            }
 });


  app.controller('SayController',['$http','$window',function($http,$window){
    Say=this;
    model=Say;
    request=$http;
    Say.listId=1;

    
    Say.submitComment=function(){
     if(Say.Mycomment==""||Say.Mycomment==undefined)
     {
      progress("Error","请填写评论");
     }
     else
     {
      
      progress("Show");
          $http({url:'http://'+url+'/super_market/app/SuperMarket!submitComment.action',method:'post',data:{"comment":Say.Mycomment,"headimgurl":Say.mypic_url,"nickname":Say.nickname}}).success(function(data){
              if(data.flag==true) 
            {
              progress("Success","评论成功","LoadData()");
            }
            else 
            {
              progress("Error","评论失败");
            }
                
        }).error(function(data, status, headers, config){
          if((status >= 200 && status < 300 ) || status === 304 || status === 1223 || status === 0)
          {
            progress("Error","网络请求失败");
          }
        }) ;
     }

    }

  }]);

})();

function addNativeOK(){
    url=getPath();
    //url="26.ztoas.com:88";
    model.url=url;
    secret=client.readGlobalInfo("secret");
    code= client.readNotGlobalInfo("code");
    initMyInfo();
    LoadData();
}