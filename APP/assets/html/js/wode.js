var url="";
var model;
var request;
var Jianguan;




(function(){
  var app=angular.module('Jianguan',[ ]);
  app.controller('JianguanController',['$http','$window','$scope',function($http,$window,$scope){
    Jianguan=this;
    model=Jianguan;
     request=$http;



     Jianguan.logout=function(){
            showAlert("确认","确认要退出账号?","Logout();");
     }

          Jianguan.shoucang=function(){
         LoginTo("shoucang.html");
     }

          Jianguan.youhuiquan=function(){
            client.showMsg("敬请期待");
     }


          Jianguan.gotomyliuyan=function(){
           LoginTo("myliuyan.html");
     }

          Jianguan.jifen=function(){
          LoginToEx("jifen.html","明细");
     }


          Jianguan.vip=function(){
             client.showMsg("敬请期待");
     }


          Jianguan.guanyu=function(){
                  client.open("aboutmarket.html",1); 
     }


          Jianguan.setting=function(){
                     LoginTo("wodezhanghu.html");
     }

    Jianguan.showpic=function(){

         if(Jianguan.user.avatar.indexOf("http")==-1)
         {
          client.ShowPicture(0,"http://"+Jianguan.url+"/"+Jianguan.user.avatar);
         }  
        else
         client.ShowPicture(0,Jianguan.user.avatar);
    }
    
     Jianguan.login=function(){
       client.open("login.html?tohtml=",1);
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
    model.url = getAvatarPath();
    model.refreshUser(getUser());
    LoadData();
}

function onResume(){
       model.refreshUser(getUser());
}

function Logout(){
    client.logout();
    model.refreshUser(getUser());
}