$JQry(function() {
	var $onlyofficePlaceholder = $JQry("#onlyoffice-placeholder");
	var onlyOfficeConfig = $onlyofficePlaceholder.data("onlyoffice-config");
	var config = onlyOfficeConfig;
	var docEditor = new DocsAPI.DocEditor("onlyoffice-placeholder", config);
});