



(function (doc, win) {
	var docEl = doc.documentElement,
	resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
	recalc = function () {
		var clientWidth = docEl.clientWidth;
		if (!clientWidth) return;
		docEl.style.fontSize = 22 * (clientWidth / 750) + 'px';
        if(clientWidth < 700)
            docEl.style.fontSize = 24 * (clientWidth / 750) + 'px';
	};
	if (!doc.addEventListener) return;
	win.addEventListener(resizeEvt, recalc, false);
	doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);

    document.addEventListener('WebViewJavascriptBridgeReady', onBridgeReady, false);
    var bridge;

    function clickReplyBtnEvent(e) {
            bridge.callHandler('clickReplyBtnEvent', {'pid':e.attr('pid'), 'author':e.attr('author'),'tid':e.attr('tid'), 'dateline':e.attr('dateline'), 'content':e.attr('content')}, function(response) {
                       });
    }

    function clickImgEvent(e) {
        //获取到所有图片的地址
        var srcList = [];
        $(".expCont img").each(function(){
                               srcList.push($(this).attr("src"));
                        });
        bridge.callHandler('clickImgEvent', {'imgArray':srcList, 'selectedImg':e.attr('src')},  function(response) {
                           });
    }

function clickAvatarEvent(e) {

                           bridge.callHandler('clickAvatarEvent', {'uid':e.attr('uid')},  function(response) {
                                              });
}

    function onBridgeReady(event) {
    bridge = event.bridge;
    var uniqueId = 1;
    bridge.init(function(message, responseCallback) {
                var data = { 'Javascript Responds':'Wee!' };
                responseCallback(data);
                });
                $(".btnReply").bind("click",function(){
                                   clickReplyBtnEvent($(this));
                                    });
                                    $(".avatar").bind("click",function(){
                                                           clickAvatarEvent($(this));
                                                           });

                                   $(".expCont img").bind("click",function(){
                                    clickImgEvent($(this));
                                                       });
}
