/*通用扩展ajax的ERROR方法，方便异常提示处理*/
(function($){  
    //首先备份下jquery的ajax方法  
    var _ajax=$.ajax;  
    //重写jquery的ajax方法  
    $.ajax=function(opt){  
        //备份opt中error和success方法  
        var fn = {  
            error:function(XMLHttpRequest, textStatus, errorThrown){},  
            success:function(data, textStatus){}  
        }  
        if(opt.error){  
            fn.error=opt.error;  
        }  
        if(opt.success){  
            fn.success=opt.success;  
        }  
        //扩展增强处理  
        var _opt = $.extend(opt,{  
            error:function(XMLHttpRequest, textStatus, errorThrown){  
                //错误方法增强处理  
            	console.error('ajax加载服务端数据出错');
            	console.log(XMLHttpRequest.responseText);
				$("body").empty().append(XMLHttpRequest.responseText);
                fn.error(XMLHttpRequest, textStatus, errorThrown); 
            },  
            success:function(data, textStatus){  
                //成功回调方法增强处理  
                fn.success(data, textStatus); 
            },  
            beforeSend:function(XHR){  
                //提交前回调方法  
            },  
            complete:function(XHR, TS){  
                //请求完成后回调函数 (请求成功或失败之后均调用)。  
            }  
        });  
        return _ajax(_opt);  
    };  
})(jQuery);  
(function($){
	var thisScriptPath = $('script').filter(function(){
		return !!this.src.match('/static/js/common.js$');
	}).last()[0].src;
	$.common = {
			/*项目路径*/
			basePath:thisScriptPath.replace('/page/static/js/common.js', ''),		   
			/*平台合作状态*/
			state:[{
				     text: '终止',
				     value:0
					},{
						label: '合作',
						value: 1
			      }]
	}
})(jQuery);	

(function(){	/**日期功能拓展**/
	Date.prototype.format = function(formaterString){
		formaterString = formaterString || 'yyyy-mm-dd hh:ii:ss';
		var formatDateString = formaterString;
		var fullYear = this.getFullYear();	//完整年
		var month = this.getMonth() + 1;	//月 0~11;
		var date = this.getDate();	//日 1-31;
		var hours = this.getHours(); //时 0~23;
		var minutes = this.getMinutes(); //分;
		var seconds = this.getSeconds(); //秒;
		var add0str = function(num){
			return num>9?num:'0'+num;
		}
		formatDateString = formatDateString.replace(/(yyyy)|(YYYY)/g, fullYear); 
		formatDateString = formatDateString.replace(/(MM)|(mm)/g, add0str(month)); 
		formatDateString = formatDateString.replace(/(M)|(m)/g, month);  
		formatDateString = formatDateString.replace(/(DD)|(dd)/g, add0str(date));  
		formatDateString = formatDateString.replace(/(D)|(d)/g, date);  
		if(/P|p/.test(formatDateString)){
			formatDateString = formatDateString.replace(/(HH)|(hh)/g, add0str(hours>12?hours-12:hours));  
			formatDateString = formatDateString.replace(/(H)|(h)/g, hours>12?hours-12:hours);  
		}else{
			formatDateString = formatDateString.replace(/(HH)|(hh)/g, add0str(hours));  
			formatDateString = formatDateString.replace(/(H)|(h)/g, hours);  
		}
		formatDateString = formatDateString.replace(/(II)|(ii)/g, add0str(minutes));  
		formatDateString = formatDateString.replace(/(I)|(i)/g, minutes);  
		formatDateString = formatDateString.replace(/(SS)|(ss)/g, add0str(seconds));  
		formatDateString = formatDateString.replace(/(S)|(s)/g, seconds	);  
		formatDateString = formatDateString.replace(/P|p/g, hours>12?'下午':'上午');  
		return formatDateString;
	};
	/*字符串功能扩展*/
	String.prototype.replaceAll = function(s1,s2) { 
	    return this.replace(new RegExp(s1,"gm"),s2); 
	};
})();

//禁止backspace按键,直接后退到登陆页面
$(document).keydown(function (e) {
   var doPrevent;
   if (e.keyCode == 8) {
       var d = e.srcElement || e.target;
       if (d.tagName.toUpperCase() == 'INPUT' || d.tagName.toUpperCase() == 'TEXTAREA') {
           doPrevent = d.readOnly || d.disabled;
       }
       else
           doPrevent = true;
   }
   else
       doPrevent = false;

   if (doPrevent)
       e.preventDefault();
});
//将DATE 转换成2016-01-18 14:53:26 通用格式
//"YYYY-MM-DD HH:II:SS" bootstrap 时间格式化 
function dateFormatYYYYMMDDHHMMSS(date){
	if( date ){
		return new Date(date).format("YYYY-MM-DD HH:II:SS");
	} else {
		return "";
	}
}

//将DATE 转换成2016-01-18 通用格式
function dateFormatYYYYMMDD(date){
	if( date ){
		return new Date(date).format("YYYY-MM-DD");
	} else {
		return "";
	}
}
