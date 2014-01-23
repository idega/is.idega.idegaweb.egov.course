jQuery(window).load(function () {
	jQuery("#editorForm").validate({
		rules: {
			"editorForm:name": {
				required:	true
			}
		},
		messages: {
			"editorForm:name":	CourseProviderTypeEditorHelper.FIELD_IS_REQUIRED
		}
	});
});

var CourseProviderTypeEditorHelper = {
		FIELD_IS_REQUIRED: "Field is required!",
		SAVING: "Saving..."
};