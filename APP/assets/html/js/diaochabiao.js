var url="";
var model;
var request;
var Research;
var code;
var APPID="wx5d4f201691879266";
var APPSECRET="56c0e33df3ae4ecec1e6b76d7ceaf886";
var Token;
var OPENID;
var secret;


function LoadData(){
    creatMask();
    request.post('http://'+url+'/super_market/app/SuperMarket!getQuestionnaire.action?surveyId=1').success(function(data){
      model.list = data;
      LoadStoreList();
      removeMask();
      }).error(function(data,status,headers,config){
       if((status>=200&&status<300)||status===304||status===1223||status===0){
       $("body").html('<div class="da"><img src="images/bj2_03.png"  alt=""/></div><div class="db">网络请求失败</div><div class="dc">请检查您的网络<br>重新加载吧</div><div class="dd"><div class="newss22"><a onclick="reload()">重新加载</a></div></div>');
       }
       });
}

function LoadStoreList(){
      request.post('http://'+url+'/super_market/app/SuperMarket!marketRank.action').success(function(data){
      model.storelist = data;
      console.log(data);
      setTimeout(function(){
        model.setRadioName(model.list,model.storelist);
      },1);
      
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
  var app=angular.module('Research',[ ]);
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

  app.controller('ResearchController',['$http','$window',function($http,$window){
    Research=this;
    model=Research;
    request=$http;
    Research.survey_result = new Array();

    Research.setRadioName=function(){
     var dsa = getByClass(document,"diaocha");
       for(var i=0;i<dsa.length;i++)
       {
           var dsa2 = getByClass(dsa[i],"store");
            for(var j=0;j<dsa2.length;j++)
            {
              
               for(var z=0;z<dsa2[j].getElementsByTagName("input").length;z++)
                 {
                  dsa2[j].getElementsByTagName("input")[z].setAttribute("name","chstt"+i+j);
                 } 
           }
       }
    }

   Research.checkSelectedNew=function(){
     Research.survey_result.length=0;
      for(var i=0;i<Research.list.length;i++)
      {
           var dsa = new Object();
           dsa.question_id = Research.list[i].id;
           dsa.type=Research.list[i].type;
           dsa.answer_array=new Array();



        for(var j=0;j<Research.storelist.length;j++)
        {
              var chkObjs = document.getElementsByName("chstt"+i+j);

                for(var z=0;z<chkObjs.length;z++){
                    if(chkObjs[z].checked){
                       var a= chkObjs[z].getAttribute("value");
                       var obj = new Object();
                      obj.answer_id=a;
                      obj.answer_of_storeid=Research.storelist[j].id;
                      dsa.answer_array.push(obj);
                            break;
                    }
                }
        }

         if(dsa.answer_array.length>0) Research.survey_result.push(dsa);
      }
      if(Research.survey_result.length==0) return false;
      else                                 return true;
   }

    Research.edit=function(){

        progress("Show");
        var flag= Research.checkSelectedNew();
        if(flag)
        {
            $http({url:'http://'+url+'/super_market/app/SuperMarket!submitSurveyResult.action',method:'post',data:{"survey_id":1,"survey_result":JSON.stringify(Research.survey_result),"imei":Research.imei,"user_id":Research.user.id}}).success(function(data){
              if(data.flag==true) 
            {
              progress("Success","提交成功","goPrevious()");
           
            }
            else 
            {
              progress("Error","提交失败");
            }
                
        }).error(function(data, status, headers, config){
          if((status >= 200 && status < 300 ) || status === 304 || status === 1223 || status === 0 || status===500)
          {
            progress("Error","网络请求失败");
          }
        }) ;
        }
        else
        {
           progress("Error","您未填任何选项");
        }


    }


  }]);
})();

function addNativeOK(){
    url=getPath();
    model.user=getUser();
    //url="t1.zed1.cn:88";
    model.url=url;    
    //model.imei="123";
    model.imei = client.getImei();
  //  secret=client.readGlobalInfo("secret");
 //   code= client.readNotGlobalInfo("code");
  //  initMyInfo();
    LoadData();
}