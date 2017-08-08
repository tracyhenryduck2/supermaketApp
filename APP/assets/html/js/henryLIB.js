function getByClass(oParent, sClass)
{
 var aEle=oParent.getElementsByTagName('*');
 var aResult=[];
 var re=new RegExp('\\b'+sClass+'\\b', 'i');
 var i=0;
 
 for(i=0;i<aEle.length;i++)
 {
  //if(aEle[i].className==sClass)
  //if(aEle[i].className.search(sClass)!=-1)
  if(re.test(aEle[i].className))
  {
   aResult.push(aEle[i]);
  }
 }
 
 return aResult;
}


//弹性运动原生函数，透明度需要改进，iTarget不带单位
    function startElasticMove(obj,json){
      clearInterval(obj.timer);
      var json2 = copy( json );
      obj.timer=setInterval(function(){
        var bStop=true;
        for(var attr in json){
          //var iSpeed=0;
          var iCur=0;
          if(attr=='opacity'){
            iCur=Math.round(parseFloat(getStyle(obj, attr))*100);
          }else{
            iCur=parseInt(getStyle(obj, attr));
          }

          json2[attr]+=(json[attr]-iCur)/6;
          json2[attr]*=0.75;

          //console.log(Math.round( iCur + json2[attr] ) );
          if( Math.abs(json2[attr])<1 && Math.abs(json[attr]-iCur)<=1 ){
            if(attr=='opacity')
            {
            	
              obj.style.filter='alpha(opacity:'+(json[attr])+')';
              obj.style.opacity=(json[attr])/100;
            }
            else
            {
              obj.style[attr] = json[attr]+'px';
              //console.log( json[attr] );
            }

            //iSpeed = 0;
          }else{
            bStop=false;
            if(attr=='opacity')
            {
              obj.style.filter='alpha(opacity:'+(iCur + json2[attr])+')';
              obj.style.opacity=(iCur + json2[attr])/100;
            }
            else
            {
              obj.style[attr] = Math.round( iCur + json2[attr] )+'px';
              console.log( iCur + json2[attr] )
            }                                                               
          }

        }
        //document.title=iCur+'px'+iSpeed;
        if(bStop){
          clearInterval(obj.timer);        
        }

      },30);
    }
      
      function getStyle(obj,attr){
        if(obj.currentStyle){
          return obj.currentStyle[attr];
        }else{
          return getComputedStyle(obj,false)[attr];
        }
      }

      function copy( obj ){
        var o = {};
        for(var i in obj ){
          o[i] = 0;
        }
        return o;
      }
/*运动框架，包括链式运动框架,iTarget不带单位*/
function startMove(obj, attr, iTarget, fn)
{
  clearInterval(obj.timer);
  obj.timer=setInterval(function (){
    //1.取当前的值
    var iCur=0;
    
    if(attr=='opacity')
    {
      iCur=parseInt(parseFloat(getStyle(obj, attr))*100);
    }
    else
    {
      iCur=parseInt(getStyle(obj, attr));
    }
    
    //2.算速度
    var iSpeed = (iTarget-iCur)/5;
    iSpeed=iSpeed>0?Math.ceil(iSpeed):Math.floor(iSpeed);
    
    //3.检测停止
    if(iCur==iTarget)
    {
      clearInterval(obj.timer);
      
      if(fn)
      {
        fn();
      }
    }
    else
    {
      if(attr=='opacity')
      {
        obj.style.filter='alpha(opacity:'+(iCur+iSpeed)+')';
        obj.style.opacity=(iCur+iSpeed)/100;
      }
      else
      {
        obj.style[attr]=iCur+iSpeed+'px';
      }
    }
  }, 30)
}      
      
function hasClass(obj, cls) {  
    return obj.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'));  
}  
  
function addClass(obj, cls) {  
    if (!this.hasClass(obj, cls)) obj.className += " " + cls;  
}  
  
function removeClass(obj, cls) {  
    if (hasClass(obj, cls)) {  
        var reg = new RegExp('(\\s|^)' + cls + '(\\s|$)');  
        obj.className = obj.className.replace(reg, ' ');  
    }  
}  
  
function toggleClass(obj,cls){  
    if(hasClass(obj,cls)){  
        removeClass(obj, cls);  
    }else{  
        addClass(obj, cls);  
    }  
}  

function insertAfter(newElement,targetElement){
    var parent=targetElement.parentNode;
     if(parent.lastChild== targetElement){
         parent.appendChild(newElement); 
    }else{ 
        parent.insertBefore(newElement,targetElement.nextSibling);
    }
}
      