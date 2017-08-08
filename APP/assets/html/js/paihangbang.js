var url="";
var model;
var request;
var Supermarket;
function LoadData(){
    creatMask();
    request.post('http://'+url+'/super_market/app/SuperMarket!marketRankTwo.action').success(function(data){
      if(data.goodlist==null || data.advancelist==null ||data.admonishinglist==null || (data.goodlist.length==0&&data.advancelist.length==0&&data.admonishinglist.length==0))
      $("body").html("<div style='margin:100px auto;width:200px;height:100px;position: relative;text-align:center;'><img  style='position: relative;' src='images/nodata.png'/><br/><span style='font-size:14px;color:rgb(141,141,141);'>没有更新排行榜内容!</span></div>");
      model.marketGoodList = data.goodlist;
      model.marketAdvanceList = data.advancelist;
      model.marketAdmonishingList = data.admonishinglist;
    console.log(data);
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
    
    Supermarket.gotoInstruction=function(){
      client.open("instructiondetail.html",1);
    }
    
    //addNativeOK();

  }]);

})();

function addNativeOK(){
    url=getPath();
    //url="26.ztoas.com:88";
    model.url=url;
    LoadData();
}