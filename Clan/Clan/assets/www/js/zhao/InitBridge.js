(function() {
        function connectWebViewJavascriptBridge(callback) {
             if (window.WebViewJavascriptBridge) {
                 callback(WebViewJavascriptBridge)
             } else {
                 document.addEventListener(
                     'WebViewJavascriptBridgeReady'
                     , function() {
                         callback(WebViewJavascriptBridge)
                     },
                     false
                 );
             }
        }

        connectWebViewJavascriptBridge(function(bridge) {
            bridge.init(function(message, responseCallback) {
             console.log('JS got a message', message);
             var data = {
                 'Javascript Responds': 'Wee!'
             };
             console.log('JS responding with', data);
                responseCallback(data);
            });


			/*只看楼主*/
			bridge.registerHandler('viewAutherOnly', function(data, responseCallback){
								if (typeof BigAppH5.bigApi_h5_viewOnly == 'undefined') {
								alert('只看楼主的bigApi_h5_viewOnly方法不存在');
								} else {
								};
								var jsonData = JSON.parse(data);

								BigAppH5.bigApi_h5_viewOnly(jsonData);
								});
			/*跳页*/
			bridge.registerHandler('jumpAction', function(data, responseCallback){
								if(typeof BigAppH5.bigApi_h5_detailJump == 'undefined' ){
								alert('找不到方法 bigApi_h5_detailJump不存在');
								}else{
								};
								var jsonData = JSON.parse(data);

								BigAppH5.bigApi_h5_detailJump(jsonData);
								});

			/*获取公共信息*/
			bridge.registerHandler('getBigAppH5', function(data, responseCallback){
								responseCallback(BigAppH5);
								});

			/*获取公共信息*/
			bridge.registerHandler('getPostData', function(data, responseCallback){
								responseCallback(BigAppH5.postData);
								});

			//获取分享信息
			bridge.registerHandler('getShareInfo', function(data, responseCallback){
								responseCallback(BigAppH5.shareData);
								});

			//打印源码
			bridge.registerHandler('printSource', function(data, responseCallback){
								alert('11111'+BigAppH5.bigApi_h5_showSource());
								responseCallback(BigAppH5.bigApi_h5_showSource());
								});

			///*回帖成功*/
			bridge.registerHandler('replyPostComplete', function(data, responseCallback){
								responseCallback('success');

								var jsonData = JSON.parse(data);

								BigAppH5.bigApi_h5_detailReplyMain(jsonData);
								});

			})
})();




