  $JQry(function() {
	  	var $spinner = $JQry("[data-service=waitUntilClosed]")
	  	
	  	if($spinner.length) {
		  	
			// AJAX parameters
		  
			var container = null,
			options = {
				requestHeaders : [ "ajax", "true", "bilto" ],
				method : "post",
				onSuccess : function(t) {
					onAjaxSuccess(t, null);
				}
			},
			eventToStop = null,
			callerId = null
			url = $spinner.data("url");
	
			directAjaxCall(container, options, url, eventToStop, callerId);
	  	}
  });
