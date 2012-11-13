if (EGovCoursesHelper == null) var EGovCoursesHelper = {};

EGovCoursesHelper.parameters = {};
EGovCoursesHelper.errors = false;

EGovCoursesHelper.initializeCourseEditor = function(parameters) {
	EGovCoursesHelper.parameters = parameters;
}

EGovCoursesHelper.validateCourseEndDate = function(id) {
	var value = jQuery(id).val();
	if (value == null || value == '')
		return;
	
	var toDatePicker = jQuery.datepicker._curInst;
	if (toDatePicker == null)
		return;
	
	var dateTo = new Date(toDatePicker.selectedYear, toDatePicker.selectedMonth, toDatePicker.selectedDay);
	var now = new Date();
	if (dateTo.getTime() <= now.getTime()) {
		EGovCoursesHelper.errors = true;
		humanMsg.displayMsg(EGovCoursesHelper.parameters.invalidEndDate, {
			timeout: 4000
		});
	} else
		EGovCoursesHelper.errors = false;
}

EGovCoursesHelper.saveCourse = function(formId, param, paramValue) {
	if (EGovCoursesHelper.errors)
		return false;
	
	var form = document.getElementById(formId);
	changeValue(form[param], paramValue);
	form.submit();
}