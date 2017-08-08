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
            var _this, s;
            var startX , startY; //触摸开始时手势横纵坐标 
            var temPos; //滚动元素当前位置
            var iCurr; //当前滚动屏幕数
            var timer = null; //计时器
            var oMover; //滚动元素
            var oLi ; //滚动单元
            var num; //滚动屏幕数
            var oPosition; //触点位置
            var moveWidth ; //滚动宽度
            var oFocusContainer;
            var oFocus;


(function($){
    $.fn.yxMobileSlider = function(settings){
        var defaultSettings = {
            width: 640, //容器宽度
            height: 320, //容器高度
            during: 5000, //间隔时间
            speed:30, //滑动速度
            widthscale:1
        }
        settings = $.extend(true, {}, defaultSettings, settings);
        return this.each(function(){
            _this = $(this);
             s = settings;
             startX = 0;
             startY = 0; //触摸开始时手势横纵坐标 
             iCurr = 0; //当前滚动屏幕数
             timer = null; //计时器
             oMover = $("ul", _this); //滚动元素
             oLi = $("li", oMover); //滚动单元
             num = oLi.length; //滚动屏幕数
             oPosition = {}; //触点位置
             moveWidth = s.width; //滚动宽度
            var timer_of_touch=0;  //触摸时间，用于判断是否为点击事件
            //初始化主体样式
            _this.width(s.width).height(s.height).css({
                position: 'relative',
                overflow: 'hidden',
				margin:'0 auto'
            }); //设定容器宽高及样式
            oMover.css({
                position: 'absolute',
                left: 0
            });
            oLi.css({
                float: 'left',
                display: 'inline'
            });
            $("img", oLi).css({
                width: '100%',
                height: '100%'
            });
            //初始化焦点容器及按钮
            _this.append('<div class="focus"><div></div></div>');
            oFocusContainer = $(".focus");
            for (var i = 0; i < num; i++) {
                $("div", oFocusContainer).append("<span></span>");
            }
            console.log(num);
            oFocus = $("span", oFocusContainer);
            oFocusContainer.css({
                minHeight: $(this).find('span').height() * 2,
                position: 'absolute',
                bottom: 0
//                background: 'rgba(0,0,0,0.5)'
            })
            $("span", oFocusContainer).css({
                display: 'none',
                float: 'left',
                cursor: 'pointer',
            })
            $("div", oFocusContainer).width(oFocus.outerWidth(true) * num).css({

                margin: '0 auto'
            });
            oFocus.first().addClass("current");
            //页面加载或发生改变
            $(window).bind('resize load', function(){
                if (isMobile()) {
                    mobileSettings();
                    bindTochuEvent();
                }
                oLi.width(_this.width()).height(_this.height());//设定滚动单元宽高
                oMover.width(num * oLi.width());
                oFocusContainer.width(_this.width()).height(_this.height() * 0.15).css({
                    zIndex: 2
                });//设定焦点容器宽高样式
                _this.fadeIn(300);
            });
            //页面加载完毕BANNER自动滚动
            autoMove();
            //PC机下焦点切换
            if (!isMobile()) {
                oFocus.hover(function(){
                    iCurr = $(this).index() - 1;
                    stopMove();
                    doMove();

                }, function(){
                    autoMove();
                })
            }
            //自动运动
            function autoMove(){
                timer = setInterval(doMove, s.during);
            }
            //停止自动运动
            function stopMove(){

                clearInterval(timer);
            }
            //运动效果
            function doMove(){
                iCurr = iCurr >= num - 1 ? 0 : iCurr + 1;
                doAnimate(-moveWidth * iCurr);
                oFocus.eq(iCurr).addClass("current").siblings().removeClass("current");
            }
            //绑定触摸事件
            function bindTochuEvent(){
                oMover.get(0).addEventListener('touchstart', touchStartFunc, false);
                oMover.get(0).addEventListener('touchmove', touchMoveFunc, false);
                oMover.get(0).addEventListener('touchend', touchEndFunc, false);
            }
            //获取触点位置
            function touchPos(e){
                var touches = e.changedTouches, l = touches.length, touch, tagX, tagY;
                for (var i = 0; i < l; i++) {
                    touch = touches[i];
                    tagX = touch.clientX;
                    tagY = touch.clientY;
                }
                oPosition.x = tagX;
                oPosition.y = tagY;
                return oPosition;
            }
            //触摸开始
            function touchStartFunc(e){
                clearInterval(timer);
                touchPos(e);
                startX = oPosition.x;
                startY = oPosition.y;
                temPos = oMover.position().left;
                timer_of_touch=0;
            }
            //触摸移动 
            function touchMoveFunc(e){
                touchPos(e);
                var moveX = oPosition.x - startX;
                var moveY = oPosition.y - startY;
                if (Math.abs(moveY) < Math.abs(moveX)) {
                    e.preventDefault();
                    oMover.css({
                        left: temPos + moveX
                    });
                }
                timer_of_touch++;
            }
            //触摸结束
            function touchEndFunc(e){
                touchPos(e);
                var moveX = oPosition.x - startX;
                var moveY = oPosition.y - startY;
                if (Math.abs(moveY) < Math.abs(moveX)) {
                    if (moveX > 0) {
                        iCurr--;
                        if (iCurr >= 0) {
                            var moveX = iCurr * moveWidth;
                            doAnimate(-moveX, autoMove);
                        }
                        else {
                            doAnimate(0, autoMove);
                            iCurr = 0;
                        }
                    }
                    else {
                        iCurr++;
                        if (iCurr < num && iCurr >= 0) {
                            var moveX = iCurr * moveWidth;
                            doAnimate(-moveX, autoMove);
                        }
                        else {
                            iCurr = num - 1;
                            doAnimate(-(num - 1) * moveWidth, autoMove);
                        }
                    }
                    oFocus.eq(iCurr).addClass("current").siblings().removeClass("current");
                }

                if(timer_of_touch==0)
                {
                  console.log("aaa");autoMove();
                }
              
            }
            //移动设备基于屏幕宽度设置容器宽高
            function mobileSettings(){
                moveWidth = $(window).width()*s.widthscale;
                var iScale = $(window).width()*s.widthscale / s.width;
                _this.height(s.height * iScale).width($(window).width()*s.widthscale);
                oMover.css({
                    left: -iCurr * moveWidth
                });
            }
            //动画效果
            function doAnimate(iTarget, fn){
                oMover.stop().animate({
                    left: iTarget
                }, _this.speed , function(){
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

function DomReload(){
               oMover = $("ul", _this); //滚动元素
               oLi = $("li", oMover); //滚动单元
               num = oLi.length; //滚动屏幕数
                moveWidth = $(window).width()*s.widthscale;
                var iScale = $(window).width()*s.widthscale / s.width;
                _this.height(s.height * iScale).width($(window).width()*s.widthscale);
                oMover.css({
                    left: -iCurr * moveWidth
                });
                oLi.css({
                float: 'left',
                display: 'inline'
            });
                oLi.width(_this.width()).height(_this.height());//设定滚动单元宽高
                oMover.width(num * oLi.width());

             
            //初始化焦点容器及按钮
            $("div", oFocusContainer).html("");
            for (var i = 0; i < num; i++) {
                $("div", oFocusContainer).append("<span></span>");
            }
            oFocus = $("span", oFocusContainer);
            oFocusContainer.css({
                minHeight: $(this).find('span').height() * 2,
                position: 'absolute',
                bottom: 0
            })
            $("span", oFocusContainer).css({
                display: '',
                float: 'left',
                cursor: 'pointer',
            })
            $("div", oFocusContainer).width(oFocus.outerWidth(true) * num).css({
                margin: '0 auto'
            });
            oFocus.first().addClass("current");
}
