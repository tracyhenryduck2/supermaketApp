var url="";
var model;
var request;
var Jianguan;
function LoadData(){
    creatMask();
    request.post('http://'+url+'/super_market/app/SuperMarketNew!collectionlist.action?uid='+model.user.id).success(function(data){
        model.list = data; 
        if(model.list.length==0){
      $("#no-data").html("<div style='margin:100px auto;width:200px;height:100px;position: relative;text-align:center;'><img  style='position: relative;' src='images/nodata.png'/><br/><span style='font-size:14px;color:rgb(141,141,141);'>没有相关收藏内容!</span></div>");
        }
        else {
        $("#no-data").html("");
        }
         removeMask();
      }).error(function(data,status,headers,config){
       if((status>=200&&status<300)||status===304||status===1223||status===0){
       $("body").html('<div class="da"><img src="images/bj2_03.png"  alt=""/></div><div class="db">网络请求失败</div><div class="dc">请检查您的网络<br>重新加载吧</div><div class="dd"><div class="newss22"><a onclick="reload()">重新加载</a></div></div>');
       }
       });
}


(function(){
  var app=angular.module('Jianguan',[ ]);
  app.controller('JianguanController',['$http','$window',function($http,$window){
    Jianguan=this;
    model=Jianguan;
     request=$http;


    Jianguan.Detail=function(newsId,typeid,type){
      if(type==1)
      client.openShoucang("contentdetail.html?newsId="+newsId+"&type="+typeid);
    else 
       client.openShoucang("xinxidongtai.html?newsId="+newsId);
    }

  }]);
})();

function addNativeOK(){
    url=getPath();
    //url="26.ztoas.com:88";
    model.user = getUser();
    model.url=url;
    LoadData();
}

function onMyResume(){
    LoadData();
}