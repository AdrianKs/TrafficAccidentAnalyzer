/**
 * 
 */

function refreshData(){
	var feedback = $.ajax({
		type: 'POST',
		url: 'http://http://localhost:4567/refreshTrafficData'
	}).success(function(data){
		setTimeout(() => {
			console.log(data);
		}, 10000);
	})
}