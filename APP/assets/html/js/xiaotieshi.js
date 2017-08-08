var url="";
var model;
var request;
var Xiaotieshi;
function LoadData(){
    creatMask();
    request.post('http://'+url+'/super_market/app/SuperMarket!getHQList.action?type='+model.type).success(function(data){
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
  var app=angular.module('Xiaotieshi',[ ]);
  app.controller('XiaotieshiController',['$http','$window',function($http,$window){
    Xiaotieshi=this;
    model=Xiaotieshi;
    request=$http;

    

    Xiaotieshi.detail=function(id,type){
       client.open("contentdetail.html?newsId="+id+"&type="+type,1);
    }

  }]);
})();

function addNativeOK(){
    url=getPath();
    //url="192.168.1.37:8080";
    
    model.url=url;
    model.type=GetQueryString('type');
    
    LoadData();
}