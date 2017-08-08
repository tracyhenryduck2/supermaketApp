var url="";
var model;
var request;
var Wode;

(function(){
  var app=angular.module('Wode',[ ]);

   app.filter("PhoneFilter",function(){
                return function(input){
                    var out = "";

                    if(isNaN(input)) {
                      return "NAN";
                    }
                    else
                    {
                      
                      lphone = input.replace(/^(\d{3})\d{4}(\d+)/,"$1****$2");
                      return lphone;
                    }
                   
                }
            });

  app.controller('WodeController',['$http','$window','$scope',function($http,$window,$scope){
    Wode=this;
    model=Wode;
    request=$http;

    
    Wode.updatecode=function(){
       client.open("updatecode.html",1);
    }

    Wode.updateusername=function(){
        client.open("mingchengxiugai.html",1);
    }

    Wode.updateAvatar=function(){
        client.openphoto();
    }
    

    Wode.refresh=function(obj){
           $scope.$apply(function(){
            Wode.user = obj;  
           });
    }

    Wode.callbackAvatar=function(str){
           $scope.$apply(function(){
               Wode.user.avatar = str;
           });
    }

  }]);
})();

function addNativeOK(){
   model.url = getAvatarPath();
   model.user = getUser();
   model.refresh(getUser());
}


function onMyResume(){
     
      var ds = client.readNotGlobalInfo("update");
      if(ds!=null)
      {
        var user = getUser();
        client.saveNotGlobalInfo("update",null);
         model.refresh(user);
      }

}

    function refreshtouxiang(url){   
       model.callbackAvatar(url);
    }
