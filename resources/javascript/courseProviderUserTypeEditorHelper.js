jQuery(window).load(function () {
	jQuery("#typeForm").validate({
		rules: {
			"typeForm:name" : {
				required: true
			}
		},
		messages: {
			"typeForm:typeForm" : CourseProviderUserTypeEditorHelper.FIELD_IS_REQUIRED,
		}
	});
});

var CourseProviderUserTypeEditorHelper = {
		FIELD_IS_REQUIRED: "Field is required!",
		SAVING: "Saving..."
};