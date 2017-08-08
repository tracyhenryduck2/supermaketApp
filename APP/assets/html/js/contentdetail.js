var url="";
var model;
var request;
var ContentDetail;
function LoadData(){

    var ds = "";
    if(model.user!=null){
      ds="&uId="+model.user.id;
    }
    request.post('http://'+url+'/super_market/app/SuperMarket!getContentInfo.action?contentId='+ContentDetail.newsId+ds).success(function(data){
      // model.detail=data;
      // $("#content").html(model.detail.content);
      // model.listShow.length=0;
      // model.listShow.push(data);
      // if(model.detail.is_shoucang!=null){
      //   client.ShowShoucangFlag(model.detail.is_shoucang);
      // }
   console.log(data);

      model.detail=data;
      model.listShow.length=0;
      model.listShow.push(data);
      if(model.detail.is_shoucang!=null){
        client.ShowShoucangFlag(model.detail.is_shoucang);
      }


      if(data.content.indexOf("../../")!=-1)
      {
        str3 = data.content.toString().replace(/..\/..\//g, "http://"+url+"/");
        $("#content").html(str3);
      }else
      {
      $("#content").html(model.detail.content);
      }

      //增加a标签
      var dsa = data.content;
      var arr = new Array();
      while(dsa.indexOf('" alt="" />')!=-1&&dsa.indexOf('<img src="../../')!=-1)
      {
          var indexstart = dsa.indexOf('<img src="../../');
          var indexend = dsa.indexOf('" alt="" />');
          var substr = dsa.substring(indexstart+16,indexend);
          arr.push(substr);
          dsa = dsa.substring(indexend+11);
      }
      console.log(arr);
        var str = str3.replace(/<img/g, "<a class='tup'><img");
        var str2 =str.replace(/" alt="" \/>/g, '" alt="" /></a>');
        $("#content").html(str2);
       

       //增加图片链接
       var imgs = '';
           for(var i=0;i<arr.length;i++)
        {
          imgs+=("http://"+url+"/"+arr[i]+",");
        }

        for(var i=0;i<$(".tup").length;i++)
        {
           $(".tup").eq(i).click(function(){
             var index = $(this).index();
             client.ShowPicture(index,imgs);
           });
          //$(".tup").eq(i).attr("href","http://"+url+"/"+arr[i]);
        }











      }).error(function(data,status,headers,config){
       if((status>=200&&status<300)||status===304||status===1223||status===0){
       $("body").html('<div class="da"><img src="images/bj2_03.png"  alt=""/></div><div class="db">网络请求失败</div><div class="dc">请检查您的网络<br>重新加载吧</div><div class="dd"><div class="newss22"><a onclick="reload()">重新加载</a></div></div>');
       }
       });
}


(function(){
  var app=angular.module('ContentDetail',[ ]);
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

  app.controller('ContentDetailController',['$http','$window',function($http,$window){
    ContentDetail=this;
    model=ContentDetail;
    request=$http; 
    ContentDetail.listShow=new Array();



    
    ContentDetail.setTitle=function(type){
     if(type==1)       document.title = "政策法规";  
     else if(type==2)  document.title = "通知公告";   
     else if(type==3)  document.title = "监管动态";   
     else if(type==4)  document.title = "行情速递";   
     else if(type==5)  document.title = "小贴士";
     else if(type==6)  document.title = "排行榜说明";
     else if(type==7)  document.title = "销毁公示";  
     else if(type==8)  document.title = "抽检公示";  
     else if(type==9)  document.title = "自检公示";       
    }

    ContentDetail.changeStatus=function(flag){
          client.ShowShoucangFlag(flag);
    }

    ContentDetail.switchShoucang=function(){
         ContentDetail.user =getUser();
         if(ContentDetail.user==null){
          client.open("login.html?tohtml=",1);
         }
         else{
            if(ContentDetail.detail!=null&&ContentDetail.detail.is_shoucang!=null){
              if(ContentDetail.detail.is_shoucang==1){

                      $http({url:'http://'+url+'/super_market/app/SuperMarketNew!deletecollection.action',method:'post',data:{"uid":ContentDetail.user.id,"content_id":ContentDetail.newsId,"type":1}}).success(function(data){
                         if(data.result==1)
                         {
                          ContentDetail.detail.is_shoucang=0;
                            progress("Success","取消关注","ContentDetail.changeStatus(0);");
                         }
                         else 
                         {
                            progress("Error","取消关注失败");
                         }
                    }).error(function(data, status, headers, config){
                        progress("Dismiss");
                      if((status >= 200 && status < 300 ) || status === 304 || status === 1223 || status === 0)
                      {
                        progress("Error","网络访问出错!");
                      }
                    }) ;             
      

              }
              else{
                      $http({url:'http://'+url+'/super_market/app/SuperMarketNew!addcollection.action',method:'post',data:{"uid":ContentDetail.user.id,"content_id":ContentDetail.newsId,"type":1}}).success(function(data){
                         if(data.result==1)
                         {
                          ContentDetail.detail.is_shoucang=1;
                          var ds = ContentDetail.user.point + 2;
                          client.savePoint(ds);
                            progress("Success","已关注,恭喜获得2积分！","ContentDetail.changeStatus(1);");
                         }
                         else if(data.result==2)
                         {
                          ContentDetail.detail.is_shoucang=1;
                            progress("Success","已关注","ContentDetail.changeStatus(1);");
                         }
                         else
                         {
                            progress("Error","关注失败");
                         }
                    }).error(function(data, status, headers, config){
                        progress("Dismiss");
                      if((status >= 200 && status < 300 ) || status === 304 || status === 1223 || status === 0)
                      {
                        progress("Error","网络访问出错!");
                      }
                    }) ; 
              }
            }
         }
    }


  }]);
})();


function addNativeOK(){
    url=getPath();
    //url="26.ztoas.com:88";
    model.url=url;
    model.fileurl = getfilePath();
   model.user=getUser();
    //model.user={id:1};
    model.newsId=GetQueryString("newsId");
    model.type=GetQueryString("type");
    model.setTitle(model.type);
    LoadData();
}

function extraAction(){
  model.switchShoucang();
}

function onMyResume(){
      model.user =getUser();
      LoadData();
}