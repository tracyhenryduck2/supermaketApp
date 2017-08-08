var url="";
var model;
var request;
var Supermarket;
function LoadData(){
    creatMask();
    request.post('http://'+url+'/super_market/app/SuperMarketNew!suggestionlist.action?uid='+model.user.id).success(function(data){
         model.list= data;
        if(model.list.length==0){
      $("#no-data").html("<div style='margin:100px auto;width:200px;height:100px;position: relative;text-align:center;'><img  style='position: relative;' src='images/nodata.png'/><br/><span style='font-size:14px;color:rgb(141,141,141);'>没有相关留言!</span></div>");
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
  var app=angular.module('Supermarket',[ ]);
  app.controller('SupermarketController',['$http','$window',function($http,$window){
    Supermarket=this;
    model=Supermarket;
    request=$http;


  }]);

})();

function addNativeOK(){
    url=getPath();
    //url="26.ztoas.com:88";
    model.url=getAvatarPath();
    model.user= getUser();
    LoadData();
}