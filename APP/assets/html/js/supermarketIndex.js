var url="";
var model;
var request;
var MarketIndex;
function LoadData(){
   creatMask();
    request.post('http://'+url+'/super_market/app/SuperMarket!marketIndex.action?id='+model.superId).success(function(data){
        console.log(data);
        model.picList=data.pic_list;
        model.videoList=data.videolist;
        if(model.picList.length==0||model.videoList.length==0)
         {
           alert("无图片或视频！");
         }
         else
         {
        model.videoList_sub=data.videolist.slice(0,4);
        model.infoList=data.informationlist;
        model.infoList_sub=data.informationlist.slice(0,2);
        model.introduce = data.introduce;
        var index = model.introduce.indexOf(">");
        $("#intro").html(model.introduce.substring(0,index+51)+"....");

      var str="";
      for(var i=0;i<model.picList.length;i++)
      {
        str+='<li><a><img class="banner_img" src="images/find-loading.svg"  alt=""  /></a></li>';
      }
      $("#ceshi").html(str);
      DomReload();
      model.LoadImg(model);
      $("#maincontent").css("display","block"); 
        removeMask();
         }

      }).error(function(data,status,headers,config){
       if((status>=200&&status<300)||status===304||status===1223||status===0){
       $("body").html('<div class="da"><img src="images/bj2_03.png"  alt=""/></div><div class="db">网络请求失败</div><div class="dc">请检查您的网络<br>重新加载吧</div><div class="dd"><div class="newss22"><a onclick="reload()">重新加载</a></div></div>');
       }
       });
}



(function(){
  var app=angular.module('MarketIndex',[ ]);
    app.controller('MarketIndexController',['$http','$window',function($http,$window){
    MarketIndex=this;
    model=MarketIndex;
    request=$http;
    MarketIndex.introduce = "";
    MarketIndex.flag=false;
    MarketIndex.wenzi="展开全文";
    //addNativeOK();
    MarketIndex.LoadImg=function(model){
      var strs = '';
       for(var i=0;i<model.picList.length;i++)
      {
         strs+=("http://"+model.url+"/supermarket_images/market/"+model.picList[i].pic_url+","); 
      }

      for(var i=0;i<model.picList.length;i++)
      {
        $(".banner_img").eq(i).attr("src","http://"+model.url+"/supermarket_images/market/"+model.picList[i].pic_url);
        $(".banner_img").eq(i).click(function(){
           var ds = $(this).index();
           client.ShowPicture(ds,strs);
        });

      //$(".banner_img").eq(i).parent().attr("href","http://"+model.url+"/supermarket_images/market/"+model.picList[i].pic_url);
      }
    }

    MarketIndex.superMarketNewsDetail=function(id){
        client.open("xinwendongtai.html?id="+id,1);
    }

    MarketIndex.gotoVideoList=function(marketId){
          client.open("shipinlist.html?marketId="+marketId,1);
    }
    MarketIndex.gotoxinxi=function(marketId){
          client.open("xinxidongtailist.html?marketId="+marketId,1);
    }
    MarketIndex.newsDetail=function(newsId){
        client.openShoucang("xinxidongtai.html?newsId="+newsId);
    }

    MarketIndex.gotoVideo=function(url){
      client.show(url);
    }

    MarketIndex.zk=function(){
     if( !MarketIndex.flag) {
      MarketIndex.wenzi="缩略全文";
      $("#intro").html(MarketIndex.introduce);
      MarketIndex.flag=true;
    }
    else
    {
      MarketIndex.wenzi="展开全文";
        var index = model.introduce.indexOf(">");
        $("#intro").html(model.introduce.substring(0,index+51)+"....");
      MarketIndex.flag=false;
    }
    }

  }]);
})();

function addNativeOK(){
    url=getPath();
    //url="26.ztoas.com:88";
    model.url=url;
    model.superId=GetQueryString("id");
    LoadData();
}