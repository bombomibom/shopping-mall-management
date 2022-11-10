
/* 공통 - ajaxGet */
function ajaxGetType(requestUrl, methodName){
	$.ajax({
		url : requestUrl,
		type : "GET",
		success : function(ajaxResult){
			methodName(ajaxResult)
		},
		error : function(){
			
		}
	});
}

/* 공통 - ajaxPost */
function ajaxPostType(requestUrl, requestData, methodName, locationUrl){
	$.ajax({
		url : requestUrl,
		type : "POST",
		data : requestData,
		success : function(ajaxResult){
			methodName(ajaxResult);
			location.href=locationUrl;
		},
		error : function(){
			
		}
	});
}

/* userList */
function showUserList(ajaxResult){
	console.log(ajaxResult);
	for (var i = 0; i < ajaxResult.length; i++) {
		var str = '';
		str = str + '<tr>';
		for(var j = 0; j < Object.keys(ajaxResult[i]).length; j++){
			if(j == 0){
				str = str + '<td><a href="/form?mode=update&custNo='+Object.values(ajaxResult[i])[j]+'">' + Object.values(ajaxResult[i])[j] + '</a></td>';
			} else {
				str = str + '<td>' + Object.values(ajaxResult[i])[j] + '</td>';
			}
		}
		str = str + '</tr>';
		$('tbody').append(str);
	}
}

/* userSales */
function showUserSalesList(ajaxResult){
	console.log(ajaxResult);
	for (var i = 0; i < ajaxResult.length; i++) {
		var str = '';
		str = str + '<tr>';
		for(var j = 0; j < Object.keys(ajaxResult[i]).length; j++){
			str = str + '<td>' + Object.values(ajaxResult[i])[j] + '</td>';
		}
		str = str + '</tr>';
		$('tbody').append(str);
	}
}