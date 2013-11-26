jQuery(document).ready(function() {
	jQuery(".windowLink").fancybox({
		type:		"iframe",
		autoSize:	false,
		beforeShow:	function () {
			var iframe = jQuery(".fancybox-iframe");
			if (iframe != null && iframe.length > 0) {
				this.height = jQuery(iframe).contents().find('table.editorTable').height() + 30;
				this.width = jQuery(iframe).contents().find('table.editorTable').width() + 25;

				jQuery(iframe).load(function() {
					var saved = jQuery(this).contents().find(".savedText");
					if (saved != null && saved.length > 0) {
						parent.jQuery.fancybox.close();

						// On Firefox uncheck edit -> preferences -> advanced -> Tell me when a website...
						parent.location.reload(true);
					}
				});
			}
		}
	});
});

var CourseProviderUsersViewerHelper = {
	remove : function (row, courseProviderId) {
		if (row == null || row.length <= 0 || row.nodeName != "TR") {
			return;
		}

		/* 
		 * I don't wait for callback. What is the point in waiting? 
		 * If this won't work, then we have a bug to be fixed
		 */
		var id = jQuery("input:hidden", row).val();
		LazyLoader.loadMultiple(['/dwr/engine.js', '/dwr/interface/CourseProviderUsersViewerBean.js'], function() {
			CourseProviderUsersViewerBean.remove(id, courseProviderId);
		});

		jQuery(row).remove();
	},
	
	changeVisibility : function(checkBox) {
		if (!CourseProviderUsersViewerHelper.isElement("INPUT", checkBox)) {
			return false;
		}

		var cell = jQuery(checkBox).parent();
		if (CourseProviderUsersViewerHelper.isEmpty(cell)) {
			return false;
		}

		var row = cell.parent();
		if (CourseProviderUsersViewerHelper.isEmpty(row)) {
			return false;
		}

		var body = row.parent();
		if (CourseProviderUsersViewerHelper.isEmpty(body)) {
			return false;
		}

		var table = body.parent();
		if (CourseProviderUsersViewerHelper.isEmpty(table)) {
			return false;
		}

		var managedElements = table.siblings();
		if (managedElements == null || managedElements.length <= 0) {
			return false;
		}

		/* Hiding or showing tables */
		managedElements.each(function() {
			jQuery(this).toggle("fast");
		});
	},

	isElement : function(elementName, element) {
		if (element == null || element.length <= 0 || element.nodeName != elementName) {
			return false;
		} else {
			return true;
		}
	},

	isEmpty : function(element) {
		if (element == null || element.length <= 0) {
			return true;
		} else {
			return false;
		}
	}
};