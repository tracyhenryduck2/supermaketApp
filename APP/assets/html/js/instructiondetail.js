var url="";
var model;
var request;
var ContentDetail;


function LoadData(){
    creatMask();
    request.post('http://'+url+'/super_market/app/SuperMarket!getInstruction.action').success(function(data){
      
      model.detail=data[0];
      $("#content").html(model.detail.content);
      model.listShow.length=0;
      model.listShow.push(data[0]);
      removeMask();
      }).error(function(data,status,headers,config){
       if((status>=200&&status<300)||status===304||status===1223||status===0){
       $("body").html('<div class="da"><img src="images/bj2_03.png"  alt=""/></div><div class="db">网络请求失败</div><div class="dc">请检查您的网络<br>重新加载吧</div><div class="dd"><div class="newss22"><a onclick="reload()">重新加载</a></div></div>');
       }
       });
}

(function(){
  var app=angular.module('ContentDetail',[ ]);
  app.controller('ContentDetailController',['$http','$window',function($http,$window){
    ContentDetail=this;
    model=ContentDetail;
    request=$http; 
    ContentDetail.listShow=new Array();



    
    ContentDetail.setTitle=function(type){
       document.title = "排行榜说明";
  
    }

    ContentDetail.switchShoucang=function(){

    }


  }]);
})();


function addNativeOK(){
    url=getPath();
    //url="26.ztoas.com:88";
    model.url=url;
    model.user=getUser();
    model.setTitle(model.type);

  LoadData();
}
