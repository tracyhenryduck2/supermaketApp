function getUser()
{
    if(client.getUserJson()==null || client.getUserJson()=='')
    {
         return null;
    }
    else
    {
       var userJson=eval("("+client.getUserJson()+")");
        var user=userJson;
        return user;
    }

}

function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
   var parm = location.search; //获取url中"?"符后的字串
   var decparm = decodeURIComponent(parm);
     if(decparm.indexOf("?") == -1){
    decparm=client.getParm();
     }
     var r = decparm.substr(1).match(reg);
     if(r!=null)return  r[2]; return null;
}




// function GetRequest() {
//     var url = location.search; //获取url中"?"符后的字串
//     if (url.indexOf("?") == -1){
//         url=client.getParm();
//     }
//     var theRequest = new Object();
//     if (url.indexOf("?") != -1) {
//         var str = url.substr(1);
//         if (str.indexOf("&") != -1) {
//             strs = str.split("&");
//             for (var i = 0; i < strs.length; i++) {
//                 theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
//             }
//         } else {
//             theRequest[str.split("=")[0]] = unescape(str.split("=")[1]);
//         }
//     }
//     return theRequest;
// }
function getPath()
{
  return client.getIpPort();
}

function getAvatarPath(){
  return client.getavatarPort();
}

function getfilePath(){
  return client.getfilePort();
}

function goPrevious(){
  client.goBack(); 
}

function showAlert(title,message,method){

      client.confirm(title,message,method);
};
function creatMask(popDivId) { 
// 参数w为弹出页面的宽度,参数h为弹出页面的高度,参数s为弹出页面的路径 
var maskDiv = window.parent.document.createElement("div"); 
maskDiv.id = "maskDiv"; 
maskDiv.style.position = "fixed"; 
maskDiv.style.top = "0"; 
maskDiv.style.left = "0"; 
maskDiv.style.zIndex = 1000; 
maskDiv.style.backgroundColor = "#FFFFFF00"; 
maskDiv.style.filter = "alpha(opacity=70)"; 
maskDiv.style.opacity = "0.7"; 
maskDiv.style.width = "100%"; 
maskDiv.style.height = (window.parent.document.body.scrollHeight + 50) + "px"; 
maskDiv.innerHTML="<div style='margin:100px auto;width:20px;height:100px;position: relative;'><img  style='margin:100px auto;position: relative;' src='images/hook-spinner.gif' /></div>"
window.parent.document.body.appendChild(maskDiv); 
maskDiv.onmousedown = function() { 
return; 
};
}
function reload(){
  window.location.reload();
} 

function createLoading() { 
// 参数w为弹出页面的宽度,参数h为弹出页面的高度,参数s为弹出页面的路径 
var maskDiv = window.parent.document.createElement("div"); 
maskDiv.id = "loading"; 
maskDiv.style.position = "fixed"; 
maskDiv.style.top = "0"; 
maskDiv.style.left = "0"; 
maskDiv.style.zIndex = 10000; 
maskDiv.style.backgroundColor = "rgba(255, 255, 255, 0)"; 
maskDiv.style.filter = "alpha(opacity=70)"; 
maskDiv.style.opacity = "0.7"; 
maskDiv.style.width = "100%"; 
maskDiv.style.height = (window.parent.document.body.scrollHeight + 50) + "px"; 
maskDiv.innerHTML="<div style='margin:100px auto;width:70px;height:100px;position: relative;'><img  style='margin:100px auto;position: relative;' src='images/meloading.gif' /></div>"
window.parent.document.body.appendChild(maskDiv); 
maskDiv.onmousedown = function() { 
return; 
};
}

function createLoading2(popDivId) { 
// 参数w为弹出页面的宽度,参数h为弹出页面的高度,参数s为弹出页面的路径 
var maskDiv = window.parent.document.createElement("div"); 
maskDiv.id = "maskDiv"; 
maskDiv.style.position = "fixed"; 
maskDiv.style.top = "0"; 
maskDiv.style.left = "0"; 
maskDiv.style.zIndex = 1000; 
maskDiv.style.backgroundColor = "rgba(255, 255, 255, 0)";
maskDiv.style.filter = "alpha(opacity=70)"; 
maskDiv.style.opacity = "0.7"; 
maskDiv.style.width = "100%"; 
maskDiv.style.height = (window.parent.document.body.scrollHeight + 50) + "px"; 
maskDiv.innerHTML="<div style='margin:100px auto;width:20px;height:100px;position: relative;'><img  style='margin:100px auto;position: relative;' src='images/hook-spinner.gif' /></div>"
window.parent.document.body.appendChild(maskDiv); 
maskDiv.onmousedown = function() { 
return; 
};
} 



function removeMask()
{
  window.parent.document.body.removeChild(window.parent.document.getElementById("maskDiv"));  
}

function removeLoading(){
window.parent.document.body.removeChild(window.parent.document.getElementById("loading"));  
}

function progress(type,message,method){
      client.progress(type,message,method);

}

function goOutcheck(){
       if(userinfo!=null)
       {
       client.saveGlobalInfo("userinfo","");
       goPrevious();
       }
       else
      {

        clients.Exit();
      }

     }


function LoginTo(url){

    if(getUser()==null)
    {
     client.open("login.html?tohtml="+url,1);
    }
    else
    {
      client.open(url,1); 
    }
}

function LoginToEx(url,text){

    if(getUser()==null)
    {
     client.open("login.html?tohtml=",1);
    }
    else
    {
      client.openEx(url,text); 
    }
}

//扩展Date的format方法 
Date.prototype.format = function (format) { 
var o = { 
"M+": this.getMonth() + 1, 
"d+": this.getDate(), 
"h+": this.getHours(), 
"m+": this.getMinutes(), 
"s+": this.getSeconds(), 
"q+": Math.floor((this.getMonth() + 3) / 3), 
"S": this.getMilliseconds() 
} 
if (/(y+)/.test(format)) { 
format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length)); 
} 
for (var k in o) { 
if (new RegExp("(" + k + ")").test(format)) { 
format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length)); 
} 
} 
return format; 
} 
function getSmpFormatDateByLong(l, isFull) { 
return getSmpFormatDate(new Date(l), isFull); 
}
function getSmpFormatDate(date, isFull) { 
var pattern = ""; 
if (isFull == true || isFull == undefined) { 
pattern = "yyyy-MM-dd hh:mm"; 
} else { 
pattern = "yyyy-MM-dd"; 
} 
return getFormatDate(date, pattern); 
} 
function getFormatDate(date, pattern) { 
if (date == undefined) { 
date = new Date(); 
} 
if (pattern == undefined) { 
pattern = "yyyy-MM-dd hh:mm:ss"; 
} 
return date.format(pattern); 
}

function PhoneFilter(phone){
   var str = String(phone);
   var substr = str.substr(0,3);
   var substr2 = str.substr(8,10);
    
   var restr = substr + "*****" + substr2;
   return restr;
}

//0代表密码符合要求，1代表密码不包括两种以上的半角字符，2代表字符串包括全角字符（中文）
function chkHalf(str){
   var int_shuzi=0;
   var int_zifu=0;
   var int_fuhao=0;  

   if(str.length<6||str.length>20) return 3;
      
      for(var i=0;i<str.length;i++)     
          {        
            var strCode=str.charCodeAt(i);
            if(strCode<127)
            {
               if(strCode>=48&&strCode<=57)
               {
                 int_shuzi++;
               }
               else if((strCode>=65&&strCode<=90)||(strCode>=97&&strCode<=122))
               {
                int_zifu++;
               }
               else
               {
                int_fuhao++;
               }

            }
            else
            {
              return 2;
            }
              
          }
   var count = 0;  
     if(int_shuzi==0)  count++;
     if(int_zifu==0)    count++;
     if(int_fuhao==0)   count++;

    if(count<=1)  return 0;
    else          return 1;
}

function CheckMail(mail) {
 var filter  = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
 if (filter.test(mail)) return true;
 else {
 return false;}
}   