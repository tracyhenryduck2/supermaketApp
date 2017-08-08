/**
 * $.yxMobileSlider
 * @charset utf-8
 * @extends jquery.1.9.1
 * @fileOverview 创建一个焦点轮播插件，兼容PC端和移动端，若引用请保留出处，谢谢！
 * @author 李玉玺
 * @version 1.0
 * @date 2013-11-12
 * @example
 * $(".container").yxMobileSlider();
 */
            var _this2, s2;
            var startX2 , startY2; //触摸开始时手势横纵坐标 
            var temPos2; //滚动元素当前位置
            var iCurr2; //当前滚动屏幕数
            var timer2 = null; //计时器
            var oMover2; //滚动元素
            var oLi2 ; //滚动单元
            var num2; //滚动屏幕数
            var oPosition2; //触点位置
            var moveWidth2 ; //滚动宽度
            var oFocus2Container2;
            var oFocus2;


(function($){
    $.fn.yxMobileSlider2 = function(settings){
        var defaultSettings = {
            width: 640, //容器宽度
            height: 320, //容器高度
            during: 5000, //间隔时间
            speed:30, //滑动速度
            widthscale:1
        }
        settings = $.extend(true, {}, defaultSettings, settings);
        return this.each(function(){
            _this2 = $(this);
             s2 = settings;
             startX2 = 0;
             startY2 = 0; //触摸开始时手势横纵坐标 
             iCurr2 = 0; //当前滚动屏幕数
             timer2 = null; //计时器
             oMover2 = $("ul", _this2); //滚动元素
             oLi2 = $("li", oMover2); //滚动单元
             num2 = oLi2.length; //滚动屏幕数
             oPosition2 = {}; //触点位置
             moveWidth2 = s2.width; //滚动宽度
            var timer2_of_touch=0;  //触摸时间，用于判断是否为点击事件
            //初始化主体样式
            _this2.width(s2.width).height(s2.height).css({
                position: 'relative',
                overflow: 'hidden',
				margin:'0 auto'
            }); //设定容器宽高及样式
            oMover2.css({
                position: 'absolute',
                left: 0
            });
            oLi2.css({
                float: 'left',
                display: 'inline'
            });
            $("img", oLi2).css({
                width: '100%',
                height: '100%'
            });
            //初始化焦点容器及按钮
            _this2.append('<div class="focus"><div></div></div>');
            oFocus2Container2 = $(".focus");
            for (var i = 0; i < num2; i++) {
                $("div", oFocus2Container2).append("<span></span>");
            }
            console.log(num2);
            oFocus2 = $("span", oFocus2Container2);
            oFocus2Container2.css({
                minHeight: $(this).find('span').height() * 2,
                position: 'absolute',
                bottom: 0
//                background: 'rgba(0,0,0,0.5)'
            })
            $("span", oFocus2Container2).css({
                display: 'none',
                float: 'left',
                cursor: 'pointer',
            })
            $("div", oFocus2Container2).width(oFocus2.outerWidth(true) * num2).css({

                margin: '0 auto'
            });
            oFocus2.first().addClass("current");
            //页面加载或发生改变
            $(window).bind('resize load', function(){
                if (isMobile()) {
                    mobileSettings();
                    bindTochuEvent();
                }
                oLi2.width(_this2.width()).height(_this2.height());//设定滚动单元宽高
                oMover2.width(num2 * oLi2.width());
                oFocus2Container2.width(_this2.width()).height(_this2.height() * 0.15).css({
                    zIndex: 2
                });//设定焦点容器宽高样式
                _this2.fadeIn(300);
            });
            //页面加载完毕BANNER自动滚动
            autoMove();
            //PC机下焦点切换
            if (!isMobile()) {
                oFocus2.hover(function(){
                    iCurr2 = $(this).index() - 1;
                    stopMove();
                    doMove();

                }, function(){
                    autoMove();
                })
            }
            //自动运动
            function autoMove(){
                timer2 = setInterval(doMove, s2.during);
            }
            //停止自动运动
            function stopMove(){

                clearInterval(timer2);
            }
            //运动效果
            function doMove(){
                iCurr2 = iCurr2 >= num2 - 1 ? 0 : iCurr2 + 1;
                doAnimate(-moveWidth2 * iCurr2);
                oFocus2.eq(iCurr2).addClass("current").siblings().removeClass("current");
            }
            //绑定触摸事件
            function bindTochuEvent(){
                oMover2.get(0).addEventListener('touchstart', touchStartFunc, false);
                oMover2.get(0).addEventListener('touchmove', touchMoveFunc, false);
                oMover2.get(0).addEventListener('touchend', touchEndFunc, false);
            }
            //获取触点位置
            function touchPos(e){
                var touches = e.changedTouches, l = touches.length, touch, tagX, tagY;
                for (var i = 0; i < l; i++) {
                    touch = touches[i];
                    tagX = touch.clientX;
                    tagY = touch.clientY;
                }
                oPosition2.x = tagX;
                oPosition2.y = tagY;
                return oPosition2;
            }
            //触摸开始
            function touchStartFunc(e){
                clearInterval(timer2);
                touchPos(e);
                startX2 = oPosition2.x;
                startY2 = oPosition2.y;
                temPos2 = oMover2.position().left;
                timer2_of_touch=0;
            }
            //触摸移动 
            function touchMoveFunc(e){
                touchPos(e);
                var moveX = oPosition2.x - startX2;
                var moveY = oPosition2.y - startY2;
                if (Math.abs(moveY) < Math.abs(moveX)) {
                    e.preventDefault();
                    oMover2.css({
                        left: temPos2 + moveX
                    });
                }
                timer2_of_touch++;
            }
            //触摸结束
            function touchEndFunc(e){
                touchPos(e);
                var moveX = oPosition2.x - startX2;
                var moveY = oPosition2.y - startY2;
                if (Math.abs(moveY) < Math.abs(moveX)) {
                    if (moveX > 0) {
                        iCurr2--;
                        if (iCurr2 >= 0) {
                            var moveX = iCurr2 * moveWidth2;
                            doAnimate(-moveX, autoMove);
                        }
                        else {
                            doAnimate(0, autoMove);
                            iCurr2 = 0;
                        }
                    }
                    else {
                        iCurr2++;
                        if (iCurr2 < num2 && iCurr2 >= 0) {
                            var moveX = iCurr2 * moveWidth2;
                            doAnimate(-moveX, autoMove);
                        }
                        else {
                            iCurr2 = num2 - 1;
                            doAnimate(-(num2 - 1) * moveWidth2, autoMove);
                        }
                    }
                    oFocus2.eq(iCurr2).addClass("current").siblings().removeClass("current");
                }

                if(timer2_of_touch==0)
                {
                  console.log("aaa");autoMove();
                }
              
            }
            //移动设备基于屏幕宽度设置容器宽高
            function mobileSettings(){
                moveWidth2 = $(window).width()*s2.widthscale;
                var iScale = $(window).width()*s2.widthscale / s2.width;
                _this2.height(s2.height * iScale).width($(window).width()*s2.widthscale);
                oMover2.css({
                    left: -iCurr2 * moveWidth2
                });
            }
            //动画效果
            function doAnimate(iTarget, fn){
                oMover2.stop().animate({
                    left: iTarget
                }, _this2.speed , function(){
                    if (fn) 
                        fn();
                });
            }
            //判断是否是移动设备
            function isMobile(){
                if (navigator.userAgent.match(/Android/i) || navigator.userAgent.indexOf('iPhone') != -1 || navigator.userAgent.indexOf('iPod') != -1 || navigator.userAgent.indexOf('iPad') != -1) {
                    return true;
                }
                else {
                    return false;
                }
            }
        });
    }
})(jQuery);

function DomReload2(){
               oMover2 = $("ul", _this2); //滚动元素
               oLi2 = $("li", oMover2); //滚动单元
               num2 = oLi2.length; //滚动屏幕数
                moveWidth2 = $(window).width()*s2.widthscale;
                var iScale = $(window).width()*s2.widthscale / s2.width;
                _this2.height(s2.height * iScale).width($(window).width()*s2.widthscale);
                oMover2.css({
                    left: -iCurr2 * moveWidth2
                });
                oLi2.css({
                float: 'left',
                display: 'inline'
            });
                oLi2.width(_this2.width()).height(_this2.height());//设定滚动单元宽高
                oMover2.width(num2 * oLi2.width());

             
}
