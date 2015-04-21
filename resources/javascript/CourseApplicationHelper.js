function setProviders(data, callback) {
	closeAllLoadingMessages();
	dwr.util.removeAllOptions("prm_provider_pk");
	for (var i = 0; i < data.length; i++) {
		jQuery('#prm_provider_pk').append('<option value="' + data[i].id + '">' + data[i].value + '</option>');
	}
	
	if (callback) {
		callback();
	}
}