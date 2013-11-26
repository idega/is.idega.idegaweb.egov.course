jQuery(window).load(function () {
	jQuery("#courseProviderForm").validate({
		rules: {
			"courseProviderForm:centerName" : {
				required:	true
			},
			"courseProviderForm:zipcode" : {
				required:	true
			}
		},
		messages: {
			"courseProviderForm:centerName" :	CourseProviderEditorHelper.FIELD_IS_REQUIRED,
			"courseProviderForm:zipcode"	:	CourseProviderEditorHelper.FIELD_IS_REQUIRED
		}
	});
});

var CourseProviderEditorHelper = {
		FIELD_IS_REQUIRED: "Field is required!",
		SAVING: "Saving..."
};