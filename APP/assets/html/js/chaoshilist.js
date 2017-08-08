var url="";
var model;
var request;
var Supermarket;
function LoadData(){
    creatMask();
    request.post('http://'+url+'/super_market/app/SuperMarket!marketRank.action').success(function(data){
      model.marketList = data;
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
    Supermarket.listId=1;
    request=$http;


Supermarket.gotoSupermarketDetail=function(id){
  client.open("chaoshiindex.html?id="+id,0);
}

  }]);

})();

function addNativeOK(){
      url=getPath();
    //url="26.ztoas.com:88";
    Supermarket.url=url;
    LoadData();
}