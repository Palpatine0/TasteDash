let Code = function(){
	var orderCode = ''
	for (var i = 0; i < 6; i++){
		orderCode += Math.floor(Math.random() * 10)
	}
	orderCode = new Date().getTime() + orderCode
	return orderCode
}

export{Code}