var url="";
var model;
var request;
var Xiaotieshi;
function LoadData(){
    creatMask();
    request.post('http://'+url+'/super_market/app/SuperMarket!getHQList.action?type='+model.type).success(function(data){
      model.list = data;
      if(model.list.length==0){
        
$("#no-data").html("<div style='margin:100px auto;position: relative;text-align:center;width: 50%;'><img  style='position: relative;height:auto' src='images/nodata.png' width='162' height='166' /><br/><span style='font-size:14px;color:rgb(141,141,141);'>没有相关公示信息!</span></div>");
      }
      else
$("#no-data").html("");
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
    Xiaotieshi.type=7;
    

    Xiaotieshi.detail=function(id,type){
       client.openShoucang("contentdetail.html?newsId="+id+"&type="+type);
    }


    Xiaotieshi.init=function(){

      $("#top-img").load(function(){
        var ds  = $("#top").css("height");
        var ds1 = $("#type-ban").css("height");
        
        var ads = parseInt(ds)+parseInt(ds1); 
        $("#content").css("margin-top",ads+"px");

      });

    }

    Xiaotieshi.switch=function(type){
        Xiaotieshi.type = type;
        LoadData(); 
    }
    Xiaotieshi.init();
    //addNativeOK();
  }]);
})();

function addNativeOK(){
    url=getPath();
    //url="26.ztoas.com:88";
    
    model.url=url;  
    LoadData();
}