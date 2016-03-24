(function() {


	/*获取数据*/
	function getData(paras) {

		paras = dealParas(paras,"getDetailData");

		var bridge = window.WebViewJavascriptBridge;

		try {
			bridge.callHandler('getData', paras, function(response) {
				response = dealResponse("getData",response);
				var data = JSON.parse(response);

				var successCallback = paras.success;
				var errorCallback = paras.error;

				if (response.length != 0) {
					 successCallback(data);
				} else {
					 errorCallback(data);
				}
			});
		} catch(exception) {
				errorCallback({errorMessage:'调用local方法失败'});
		}
	}

	/*点击图片*/
	function clickImage(paras) {

		paras = dealParas(paras,"clickImage");

		var bridge = window.WebViewJavascriptBridge;
		var shutViewCallback = paras.shutView;
		var successCallback = paras.success;
		var errorCallback = paras.error;
		try {
			bridge.callHandler('clickImage',paras,function(response) {
						response = dealResponse("clickImage",response);
						var data = JSON.parse(response);

						if (response.length == 0) {
							successCallback(data);
						} else {
							errorCallback(data);
						};
					});
		} catch(exception) {
			errorCallback({errorMessage:'调用local方法失败'});
		}
	}

	/*点击头像*/
	function clickAvatar(paras) {

		paras = dealParas(paras,"clickAvatar");

		var bridge = window.WebViewJavascriptBridge;

		var successCallback = paras.success;
		var errorCallback = paras.error;
		try {
		bridge.callHandler('clickAvatar',paras,function(response) {
					response = dealResponse("clickAvatar",response);

					var data = JSON.parse(response);

					if (response.length == 0) {
						successCallback(data);
					} else {
						errorCallback(data);
					};
				});
		} catch(exception) {
			errorCallback({errorMessage:'调用local方法失败'});
		}
	}

	/*显示toast提示语*/
	function toast(paras) {

		paras = dealParas(paras,"toast");

		var bridge = window.WebViewJavascriptBridge;
		bridge.callHandler('showToast',paras,function(response) {
					   });
	}


	//转为json格式数据,控制台输出
	function dealParas(paras,zhaoApi) {
		show2JSON("paras",paras);

		if("getDetailData" == zhaoApi){
			var jsApi  = paras;
			jsApi.zhaoApi = paras.api;
		}else{
			var data = paras;
			var jsApi = getJsApiObj(data);
			jsApi.zhaoApi = zhaoApi;
		}


		show2JSON("paras",jsApi);

		return jsApi;

	}


	//转换类型
	function getJsApiObj(data) {
		var foo = {
			data : data
		};
		return foo;
	}


	//以base64处理服务器端返回数据，并抛给html5端
	function dealResponse(tag,response) {
		//show2JSON("response",response);
		response = window.android.base64Decode(response);
		//show2JSON("response",response);

		return response;
	}



	//打印
	function show2JSON(tag,obj) {
		//		alert(obj);

		var str = "";
//		try{
//			str = "####tag: "+tag+" ####:"+ ZhaoJS.obj2String(obj);
//		}catch(exception){
//		}

		//		alert(str);

		//window.android.showSource(str);
	}




	/*获取环境信息*/
	function getEnvironment() {
		var message = JSON.parse(window.android.getEnv());
		return message;
	}

	/*作用域名*/
	window.BigAppNative = {
		getEnvironment: getEnvironment,
		getData: getData,
		clickAvatar: clickAvatar,
		clickImage: clickImage,
		toast: toast
	}
})();



