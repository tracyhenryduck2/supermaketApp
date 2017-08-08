var url="";
var model;
var request;
var Index;
function LoadData(){
    model.page=1;
    request.post('http://'+url+'/super_market/app/SuperMarket!getCarouselInfo.action').success(function(data){

      model.banner=data;
      console.log(model.banner);
      var str="";
      for(var i=0;i<model.banner.length;i++)
      {
        str+='<li><img class="banner_img" src="images/find-loading.svg"  alt=""  /></li>';
      }
      $("#ceshi").html(str);
      DomReload();
      
      for(var i=0;i<model.banner.length;i++)
      {
       $(".banner_img").eq(i).attr("src","http://"+model.url+"/supermarket_images/banner/"+model.banner[i].filename);

        if(model.banner[i].url.indexOf(".html")!=-1)
            $(".banner_img").eq(i).load(function(){
              //alert($(this));
                         $(this).click(function(){
               var index = $(".banner_img").index(this);
              client.open(model.banner[index].url,1);
             });
        });

      }


      }).error(function(data,status,headers,config){
       if((status>=200&&status<300)||status===304||status===1223||status===0){
       $("body").html('<div class="da"><img src="images/bj2_03.png"  alt=""/></div><div class="db">网络请求失败</div><div class="dc">请检查您的网络<br>重新加载吧</div><div class="dd"><div class="newss22"><a onclick="reload()">重新加载</a></div></div>');
       }
       });
}

function LoadDataList(){
       request.post('http://'+url+'/super_market/app/SuperMarket!marketHotList.action').success(function(data){
       model.manage=data.manage;
       model.market=data.market;
       model.zhenglist = model.manage[0].list.slice(0,3);
       model.tonglist = model.manage[1].list.slice(0,3);
       model.shilist = model.manage[2].list.slice(0,3);
       model.showLoading=false;
 
       var str="";
      for(var i=0;i<model.market.length;i++)
      {

        if(i%8==0){
       str+='<li><div class="sifenzhi"><img class="chao_img" src="images/shop_loading.jpg"></div>';
       }
       else {
       str+='<div class="sifenzhi"><img class="chao_img" src="images/shop_loading.jpg"></div>';  
       }

       if(i%8==7||i==model.market.length-1){
        str+='</li>';
       }

      }
      $("#ceshi2").html(str);
      DomReload2();
      for(var i=0;i<model.market.length;i++)
      {
       $(".chao_img").eq(i).attr("src","http://"+model.url+"/supermarket_images/market/"+model.market[i].head_pic);

            $(".chao_img").eq(i).load(function(){
              //alert($(this));
                         $(this).click(function(){
               var index = $(".chao_img").index(this);
               client.open("chaoshiindex.html?id="+model.market[index].id,1);
             });
        });

      }




      }).error(function(data,status,headers,config){
       if((status>=200&&status<300)||status===304||status===1223||status===0){
       $("body").html('<div class="da"><img src="images/bj2_03.png"  alt=""/></div><div class="db">网络请求失败</div><div class="dc">请检查您的网络<br>重新加载吧</div><div class="dd"><div class="newss22"><a onclick="reload()">重新加载</a></div></div>');
       }
       });

}


(function(){
  var app=angular.module('Index',[ ]);
  app.controller('IndexController',['$http','$window',function($http,$window){
    Index=this;
    model=Index;
    Index.showLoading=true;

    Index.cds=3;
    request=$http;

    Index.gotoMoreCity=function(){
     client.open("chaoshilist.html",1);
  }
    Index.gotoMorejianguan=function(){
    client.open("jianguan.html",1);
  }
    Index.change=function(index){
     Index.cds=index;
    }

    Index.gotoDetail=function(id,type){
      client.openShoucang("contentdetail.html?newsId="+id+"&type="+type);
    }





  }]);
})();

function addNativeOK(){
    url=getPath();
    //url="26.ztoas.com:88";
    model.url=url;
    LoadData();
    LoadDataList();
}