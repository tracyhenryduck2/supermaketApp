var url="";
var model;
var request;
var Jianguan;

function LoadData(){
    creatMask();
    request.post('http://'+url+'/super_market/app/SuperMarketNew!pointLog.action?userid='+model.user.id).success(function(data){
      model.list = data;
      if(model.list.length==0){
              $("#no-data").html("<div style='margin:40px auto;position: relative;text-align:center;width: 50%;'><span style='font-size:1em;color:rgb(141,141,141);'>您没有兑换记录哦!</span><img  style='margin-top: 50px;position: relative;height:auto' src='images/wujifen.png' width='162' height='166' /><p style='color:rgb(141,141,141);margin-top:40px'>如有疑问请联系客服</p><p style='color:black;;margin-top:4px'>0574-55334111</p></div>");
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
  app.controller('JianguanController',['$http','$window','$scope',function($http,$window,$scope){
    Jianguan=this;
    model=Jianguan;
     request=$http;



   

  }]);
})();

function addNativeOK(){
    url=getPath();
    model.user=getUser();
    LoadData();
}

function onResume(){
  
}


