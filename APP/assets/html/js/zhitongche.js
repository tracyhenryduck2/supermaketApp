var url="";
var model;
var request;
var Jianguan;




(function(){
  var app=angular.module('Jianguan',[ ]);
  app.controller('JianguanController',['$http','$window',function($http,$window){
    Jianguan=this;
    model=Jianguan;
     request=$http;
   
	Jianguan.diaocha=function(){
        LoginTo("diaochabiao.html"); 
	}

	Jianguan.paihangbang=function(){
     client.open("paihangbang.html",1);
	}

	Jianguan.liuyan=function(){
		LoginTo("liuyanban.html"); 
	}

  Jianguan.gotoWeb=function(){
    client.gotoWeb();
  }
 
  }]);
})();

function addNativeOK(){
    url=getPath();

}
