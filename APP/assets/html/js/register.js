var url="";
var model;
var request;
var Denglu;


(function(){
  var app=angular.module('Denglu',[ ]);
        app.config(function($httpProvider) {
        $httpProvider.defaults.headers.put['Content-Type'] = 'application/x-www-form-urlencoded';
        $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
        
    // Override $http service's default transformRequest
    $httpProvider.defaults.transformRequest = [function(data) {
        /**
         * The workhorse; converts an object to x-www-form-urlencoded serialization.
         * @param {Object} obj
         * @return {String}
         */
         var param = function(obj) {
            var query = '';
            var name, value, fullSubName, subName, subValue, innerObj, i;
            
            for (name in obj) {
                value = obj[name];
                
                if (value instanceof Array) {
                    for (i = 0; i < value.length; ++i) {
                        subValue = value[i];
                        fullSubName = name + '[' + i + ']';
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += param(innerObj) + '&';
                    }
                } else if (value instanceof Object) {
                    for (subName in value) {
                        subValue = value[subName];
                        fullSubName = name + '[' + subName + ']';
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += param(innerObj) + '&';
                    }
                } else if (value !== undefined && value !== null) {
                    query += encodeURIComponent(name) + '='
                    + encodeURIComponent(value) + '&';
                }
            }
            
            return query.length ? query.substr(0, query.length - 1) : query;
        };
        
        return angular.isObject(data) && String(data) !== '[object File]'
        ? param(data)
        : data;
    }];
});
  app.controller('DengluController',['$http','$window','$scope',function($http,$window,$scope){
    Denglu=this;
    model=Denglu;
    request=$http;

    Denglu.smsable=1;
     Denglu.remainsecond=30;
     Denglu.timer=null;


    Denglu.openphoto=function(){
      client.openphoto();
    }

    Denglu.freshPic=function(picurl){

          $scope.$apply(function(){

              Denglu.avatar = picurl;
          });

      }


    Denglu.getSms=function(){
        
        if(Denglu.editphone==null||Denglu.editphone=="")
        {
          progress("Error","请输入手机号");
          return;
        }

        if(!(/^1[3|4|5|7|8][0-9]\d{4,8}$/).test(Denglu.editphone.toString()) || Denglu.editphone.toString().length!=11){

            progress("Error","手机号码格式错误!");
            return;
         }
           

       if(Denglu.smsable==1){

           
           Denglu.smsable=0;
           Denglu.timer =  setInterval(function(){

            $scope.$apply(function(){
                  if(Denglu.remainsecond>0){
                    Denglu.remainsecond--;
                 }
                 else {
                      clearInterval(Denglu.timer);
                      Denglu.smsable=1;
                      Denglu.remainsecond=30;
                 }
            });

            
           },1000);
        progress("Success","获取成功");

     Denglu.editacode = "000000";
     $("#yanzheng").val("000000");
        //   $http({url:'http://'+url+'/getaccode',method:'post',data:{"mobile":Denglu.editphone.toString()}}).success(function(data){
      
        //        progress("Dismiss");

        //      if(data.Code==0)
        //      {
        //       progress("Success","获取成功,请注意查收");
        //      }
        //      else 
        //      {
        //         progress("Error",data.Message);
        //      }
        // }).error(function(data, status, headers, config){
        //     progress("Dismiss");
        //   if((status >= 200 && status < 300 ) || status === 304 || status === 1223 || status === 0)
        //   {
        //     progress("Error","网络访问出错!");
        //   }
        // }) ;

       }
    }

    Denglu.submit=function(){

           if(Denglu.editphone==null || Denglu.editphone=='')
           {
             progress("Error","请输入手机号");
             return;
           }

           if(Denglu.password==null || Denglu.password=='')
           {
             progress("Error","请输入登录密码");
             return;
           }

           if(Denglu.confirmpassword==null || Denglu.confirmpassword=='')
           {
             progress("Error","请输入确认密码");
             return;
           }

           if(Denglu.password!=Denglu.confirmpassword){
             progress("Error","两次密码不一致");
             return;
           }


            progress("Show");

             $http({url:'http://'+url+'/super_market/app/SuperMarketNew!register.action',method:'post',data:{"username":Denglu.editphone, "avatar":Denglu.avatar,"password":Denglu.password,"nickname":Denglu.editphone}}).success(function(data){
              progress("Dismiss");
     
              if(data.result==1){
                  progress("Success","注册成功!","goPrevious();");
              }
              else if(data.result==2){
                  progress("Error","手机号已注册");
              }
              else {
                 progress("Error","注册失败");
              }


        }).error(function(data, status, headers, config){
            progress("Dismiss");
          if((status >= 200 && status < 300 ) || status === 304 || status === 1223 || status === 0)
          {
            progress("Error","网络访问出错!");
          }
        }) ;
    }


  }]);
})();


function addNativeOK(){
    url = getPath();
    model.url = getAvatarPath();
}



function refreshtouxiang(url){
        model.freshPic(url);
}