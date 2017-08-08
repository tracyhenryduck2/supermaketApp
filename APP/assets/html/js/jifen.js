var url="";
var model;
var request;
var Jianguan;

function LoadData(){
    creatMask();
    request.post('http://'+url+'/super_market/app/SuperMarket!getHQList.action?type='+model.type).success(function(data){


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
    Jianguan.duihuan=function(){
      client.open("liwulist.html",1);
    }

    Jianguan.log=function(){
      client.open("jifenduihuandetail.html",1);
    }

    Jianguan.guize=function(){
      client.open("jifenguize.html",1);
    }


        Jianguan.refreshUser=function(user){
        $scope.$apply(function(){
        Jianguan.user = user;
        });
     }

  }]);
})();

function addNativeOK(){
    url=getPath();
    model.refreshUser(getUser());
}

function onResume(){
  
}

function extraAction(){
   client.open("jifendetail.html",1);
}
