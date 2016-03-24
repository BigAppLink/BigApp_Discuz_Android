(function() {
    //obj转String，如果obj中包含js，也可以正常输出
    function obj2String(o){
        var r=[];
        if(typeof o=="string"){
            return "\""+o.replace(/([\'\"\\])/g,"\\$1").replace(/(\n)/g,"\\n").replace(/(\r)/g,"\\r").replace(/(\t)/g,"\\t")+"\"";
        }
        if(typeof o=="object"){
            if(!o.sort){
                for(var i in o){
                    r.push(i+":"+obj2String(o[i]));
                }
                if(!!document.all&&!/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/.test(o.toString)){
                    r.push("toString:"+o.toString.toString());
                }
                r="{"+r.join()+"}";
            }else{
                for(var i=0;i<o.length;i++){
                    r.push(obj2String(o[i]))
                }
                r="["+r.join()+"]";
            }
            return r;
        }
        return o.toString();
    }


    //输出可alert方式的obj格式化数据
    function writeObj(obj){
        var description = "";
        for(var i in obj){
            var property=obj[i];
            description+=i+" = "+property+"\n";
        }
        alert(description);
    }

    //转为json格式数据
    function toJSONStr(obj) {
        var str = JSON.parse(obj);
        return str;
    }


    /*作用域名*/
    window.ZhaoJS = {
        obj2String: obj2String,
        writeObj: writeObj,
        toJSONStr: toJSONStr
    }
})();