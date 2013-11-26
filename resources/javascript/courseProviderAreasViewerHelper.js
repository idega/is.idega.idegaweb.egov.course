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

var CourseProviderAreasViewerHelper = {
	remove : function (row) {
		if (row == null || row.length <= 0 || row.nodeName != "TR") {
			return;
		}

		/* 
		 * I don't wait for callback. What is the point in waiting? 
		 * If this won't work, then we have a bug to be fixed
		 */
		var id = jQuery("input:hidden", row).val();
		LazyLoader.loadMultiple(['/dwr/engine.js', '/dwr/interface/CourseProviderAreasViewerBean.js'], function() {
			CourseProviderAreasViewerBean.remove(id);
		});

		jQuery(row).remove();
	}
};