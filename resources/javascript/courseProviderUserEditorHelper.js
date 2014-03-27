jQuery(window).load(function () {
	jQuery("#typeForm").validate({
		rules: {
			"typeForm:name" : {
				required: true
			},
			"typeForm:newPassword" : {
				required:	true
			},
			"typeForm:retypedPassword" : {
				required:	true,
				equalTo:	"input[id='typeForm:newPassword']"
			}
		},
		messages: {
			"typeForm:typeForm" : CourseProviderUserEditorHelper.FIELD_IS_REQUIRED,
			"typeForm:newPassword":		CourseProviderUserEditorHelper.FIELD_IS_REQUIRED,
			"passwordReset:retypedPassword":	CourseProviderUserEditorHelper.PASSWORDS_DO_NOT_MATCH
		}
	});

	jQuery("input[id='typeForm:email']").autocomplete({
		source: function(request, callback) {
			CourseProviderUserEditorHelper.getUserInfo(request, callback);
		},

		select: function(event, ui) {
			jQuery("input[id='typeForm:name']").val(ui.item.name);
			jQuery("input[id='typeForm:phone']").val(ui.item.phone);
			jQuery("input[id='typeForm:idegaUserId']").val(ui.item.primaryKey);
		}
	});
});

var CourseProviderUserEditorHelper = {
	FIELD_IS_REQUIRED: "Field is required!",
	SAVING: "Saving...",
	AVAILABLE_EMAILS: [],
	PASSWORDS_DO_NOT_MATCH: "Passwords do not match!",
	getUserInfo:	function(request, callback) {
		var response = [];
		var query = jQuery.ui.autocomplete.escapeRegex(request.term);
		var pattern = new RegExp(query);

		for (var index in CourseProviderUserEditorHelper.AVAILABLE_EMAILS) {
			if (pattern.test(CourseProviderUserEditorHelper.AVAILABLE_EMAILS[index].value)) {
				response.push(CourseProviderUserEditorHelper.AVAILABLE_EMAILS[index]);
			}
		}
		
		callback(response);
	},

	loadUserMails:	function(mail) {
		if (mail == null || mail.length > 2) {
			return null;
		}

		LazyLoader.loadMultiple(['/dwr/engine.js', '/dwr/interface/CourseProviderUsersViewerBean.js'], function() {
			CourseProviderUsersViewerBean.getAutocompleteMails(mail, {
				callback: function(addresses) {
					if (addresses == null || addresses.length <= 0) {
						return null;
					}

					for (var key in addresses) {
						CourseProviderUserEditorHelper.AVAILABLE_EMAILS.push({
							label:		key,
							value:		addresses[key].userEmail,
							phone:		addresses[key].userHomePhone,
							name:		addresses[key].userName,
							primaryKey:	addresses[key].userPK
						}); 
					} 

					return CourseProviderUserEditorHelper.AVAILABLE_EMAILS;
				}
			});
		});
	}
};