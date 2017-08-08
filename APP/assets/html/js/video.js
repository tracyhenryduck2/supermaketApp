var url="";
var model;
var request;
var Video;
function LoadData(){
    creatMask();
    model.page=1;
    request.post('http://'+url+'/super_market/app/SuperMarket!marketVideoList.action?id='+Video.marketId).success(function(data){
       model.list = data;
      console.log(data);
      removeMask();
      }).error(function(data,status,headers,config){
       if((status>=200&&status<300)||status===304||status===1223||status===0){
       $("body").html('<div class="da"><img src="images/bj2_03.png"  alt=""/></div><div class="db">网络请求失败</div><div class="dc">请检查您的网络<br>重新加载吧</div><div class="dd"><div class="newss22"><a onclick="reload()">重新加载</a></div></div>');
       }
       });
}



(function(){
  var app=angular.module('Video',[ ]);
  app.controller('VideoController',['$http','$window',function($http,$window){
    Video=this;
    model=Video;
    request=$http;

    
    Video.gotoVideo=function(url){
      client.show(url);
    }
  }]);
})();

function addNativeOK(){
    url=getPath();  
    model.url=url;
    model.marketId=GetQueryString('marketId');  
    LoadData();
}