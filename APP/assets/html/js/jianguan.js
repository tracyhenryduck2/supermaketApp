var url="";
var model;
var request;
var Jianguan;
function LoadData(){
    creatMask();
    request.post('http://'+url+'/super_market/app/SuperMarket!manageColumn.action').success(function(data){
        model.manageColumn = data; 
        console.log(model.manageColumn);
         LoadDataList(3);
         removeMask();
      }).error(function(data,status,headers,config){
       if((status>=200&&status<300)||status===304||status===1223||status===0){
       $("body").html('<div class="da"><img src="images/bj2_03.png"  alt=""/></div><div class="db">网络请求失败</div><div class="dc">请检查您的网络<br>重新加载吧</div><div class="dd"><div class="newss22"><a onclick="reload()">重新加载</a></div></div>');
       }
       });
}

function LoadDataList(id){
  creatMask();
       request.post('http://'+url+'/super_market/app/SuperMarket!manageList.action?id='+id).success(function(data){
        // model.managelist = data;

      if(id==1) model.zhenglist = data;
      else if(id==2) model.tonglist = data;
      else if(id==3) model.shilist = data;
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
    Jianguan.listId=3;

    Jianguan.change=function(id){
      Jianguan.listId=id;
      LoadDataList(id);
    }

    Jianguan.Detail=function(newsId,type){
      client.openShoucang("contentdetail.html?newsId="+newsId+"&type="+type);
    }

  }]);
})();

function addNativeOK(){
    url=getPath();
    //url="26.ztoas.com:88";
   
    model.url=url;
    LoadData();
}