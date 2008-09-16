/*
 * $Id$
 * Created on Sep 28, 2007
 *
 * Copyright (C) 2007 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.egov.course.presentation;

import is.idega.idegaweb.egov.application.presentation.ApplicationForm;
import is.idega.idegaweb.egov.course.CourseConstants;
import is.idega.idegaweb.egov.course.business.CourseApplicationSession;
import is.idega.idegaweb.egov.course.business.CourseBusiness;
import is.idega.idegaweb.egov.course.business.CourseDWR;
import is.idega.idegaweb.egov.course.data.ApplicationHolder;
import is.idega.idegaweb.egov.course.data.Course;
import is.idega.idegaweb.egov.course.data.PriceHolder;

import java.rmi.RemoteException;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.accesscontrol.business.NotLoggedOnException;
import com.idega.core.builder.data.ICPage;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Span;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.CoreConstants;
import com.idega.util.EmailValidator;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.PresentationUtil;
import com.idega.util.text.Name;
import com.idega.util.text.SocialSecurityNumber;

public class SimpleCourseApplication extends ApplicationForm {

	private static final int ACTION_PHASE_1 = 1;
	private static final int ACTION_PHASE_2 = 2;
	private static final int ACTION_PHASE_3 = 3;
	private static final int ACTION_PHASE_4 = 4;
	private static final int ACTION_PHASE_5 = 5;
	private static final int ACTION_SAVE = 0;

	protected static final String PARAMETER_ACTION = "prm_action";
	protected static final String PARAMETER_TRIGGER_SAVE = "prm_trigger_save";

	private static final String PARAMETER_APPLICANT_OPTION = "prm_applicant_option";
	private static final String PARAMETER_PERSONAL_ID = "prm_personal_id";
	private static final String PARAMETER_COURSE_TABLE_ID = "prm_cou_course_table";

	private static final String PARAMETER_CATEGORY = "prm_sch_cat";
	private static final String PARAMETER_COURSE_TYPE = "prm_cou_type";
	private static final String PARAMETER_PROVIDER = "prm_provider_pk";
	private static final String PARAMETER_COURSE = "prm_cou_course";

	private static final String PARAMETER_HOME_PHONE = "prm_home_phone";
	private static final String PARAMETER_WORK_PHONE = "prm_work_phone";
	private static final String PARAMETER_MOBILE_PHONE = "prm_mobile_phone";
	private static final String PARAMETER_EMAIL = "prm_email";

	private static final String PARAMETER_AGREEMENT = "prm_agreement";
	private static final String PARAMETER_HAS_DYSLEXIA = "prm_has_dyslexia";

	private static final String PARAMETER_PAYER_NAME = "prm_payer_name";
	private static final String PARAMETER_PAYER_PERSONAL_ID = "prm_payer_personal_id";
	private static final String PARAMETER_AMOUNT = "prm_amount";
	private static final String PARAMETER_AMOUNT_OF_CERTIFICATE_FEES = "prm_amount_of_certificate_fees";
	private static final String PARAMETER_REFERENCE_NUMBER = "prm_reference_number";

	private static final String PARAMETER_APPLICANT_AGE = "prm_applicant_age";
	private static final String PARAMETER_TOO_LATE_APPLYING_FOR_CONTINUING_EDUCATION = "prm_too_late_applying_for_continuing_education";

	private IWResourceBundle iwrb = null;

	private int numberOfPhases = 6;
	private boolean iUseSessionUser = false;
	private Object iSchoolTypePK = null;
	private boolean iCompanyRegistration = false;

	protected String getCaseCode() {
		return CourseConstants.CASE_CODE_KEY;
	}

	public String getBundleIdentifier() {
		return CourseConstants.IW_BUNDLE_IDENTIFIER;
	}

	protected void present(IWContext iwc) {
		this.iwrb = getResourceBundle(iwc);

		try {
			switch (parseAction(iwc)) {
				case ACTION_PHASE_1:
					showPhaseOne(iwc);
					break;

				case ACTION_PHASE_2:
					showPhaseTwo(iwc);
					break;

				case ACTION_PHASE_3:
					showPhaseThree(iwc);
					break;

				case ACTION_PHASE_4:
					showPhaseFour(iwc);
					break;

				case ACTION_PHASE_5:
					showPhaseFive(iwc);
					break;

				case ACTION_SAVE:
					save(iwc);
					break;
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private Form getForm(IWContext iwc, int state) {
		Form form = new Form();
		if (state != ACTION_PHASE_1) {
			form.maintainParameter(PARAMETER_COURSE);
			form.maintainParameter(PARAMETER_CATEGORY);
			form.maintainParameter(PARAMETER_COURSE_TYPE);
			form.maintainParameter(PARAMETER_PROVIDER);
		}
		if (state != ACTION_PHASE_2) {
			form.maintainParameter(PARAMETER_APPLICANT_OPTION);
			form.maintainParameter(PARAMETER_PERSONAL_ID);
		}
		if (state != ACTION_PHASE_4) {
			form.maintainParameter(PARAMETER_AGREEMENT);
			if (!iwc.isParameterSet(PARAMETER_TRIGGER_SAVE)) {
				form.maintainParameter(PARAMETER_HAS_DYSLEXIA);
			}
		}

		return form;
	}

	private void showPhaseOne(IWContext iwc) throws RemoteException {
		Form form = getForm(iwc, ACTION_PHASE_1);
		form.setId("course_step_1");
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_1)));

		addDefaulScriptFiles(iwc);

		StringBuffer script2 = new StringBuffer();
		script2.append("function setOptions(data) {\n").append("\tif (document.getElementById(\"" + PARAMETER_COURSE_TYPE + "\") != null) { DWRUtil.removeAllOptions(\"" + PARAMETER_COURSE_TYPE + "\");}\n").append("\tDWRUtil.addOptions(\"" + PARAMETER_COURSE_TYPE + "\", data);\n").append("}");

		StringBuffer script = new StringBuffer();
		script.append("function changeValues() {\n").append(getSchoolTypePK() != null ? "\tvar val = '" + getSchoolTypePK() + "';\n" : "\tvar val = +$(\"" + PARAMETER_CATEGORY + "\").value;\n").append("\tvar TEST = CourseDWRUtil.getCourseTypesDWR(val, '" + iwc.getCurrentLocale().getCountry() + "', setOptions);\n").append("\tgetCourses();\n").append("}");

		StringBuffer script3 = new StringBuffer();
		script3.append("function getCourses() {\n").append("\tvar val = -1;\n").append(getSchoolTypePK() != null ? "\tvar val2 = '" + getSchoolTypePK() + "';\n" : "\tvar val2 = +$(\"" + PARAMETER_CATEGORY + "\").value;\n").append("\tvar val3 = +$(\"" + PARAMETER_COURSE_TYPE + "\") != null ? +$(\"" + PARAMETER_COURSE_TYPE + "\").value : -1;\n").append("\tvar TEST = CourseDWRUtil.getCoursesDWR(val, val2, val3, -1, '" + iwc.getCurrentLocale().getCountry() + "', " + String.valueOf(this.iUseSessionUser) + ", setCourses);\n").append("}");

		StringBuffer script4 = new StringBuffer();
		script4.append("var getName = function(course) { return course.name };\n");
		script4.append("var getPk = function(course) { return course.pk };");
		script4.append("var getFrom = function(course) { return course.from };\n");
		script4.append("var getTo = function(course) { return course.to };\n");
		script4.append("var getDescription = function(course) { return course.description };\n");
		script4.append("var getTimeframe = function(course) { return course.timeframe };\n");
		script4.append("var getDays = function(course) { return course.days };\n");
		script4.append("var getPrice = function(course) { return course.price };\n");
		script4.append("var getProvider = function(course) { return course.provider };\n");
		script4.append("var getRadioButton = function(course) { return getRadio(course); };\n");

		script4.append("function setCourses(data) {\n").append("\tvar isEmpty = true;\n").append("\tfor (var prop in data) { isEmpty = false } \n").append("\tif (isEmpty == true) {\n").append("\t}\n").append("\tDWRUtil.removeAllRows(\"" + PARAMETER_COURSE_TABLE_ID + "\");\n").append("\tDWRUtil.addRows(\"" + PARAMETER_COURSE_TABLE_ID + "\", data, [getRadio, getName, getTimeframe, getProvider], { rowCreator:function(options) { var row = document.createElement(\"tr\"); if (options.rowData.isfull) { row.className = \"isfull\" }; return row; }});\n").append("\tvar table = $(\"" + PARAMETER_COURSE_TABLE_ID + "\");\n").append("\tvar trs = table.childNodes;\n").append("\tfor (var rowNum = 0; rowNum < trs.length; rowNum++) {\n").append("\t\tvar currentRow = trs[rowNum];\n").append("\t\tvar tds = currentRow.childNodes;\n").append("\t\tfor (var colNum = 0; colNum < tds.length; colNum++) {\n").append("\t\t\tvar obj = tds[colNum].firstChild;\n").append("\t\t\tif (obj != null && obj.className == 'checkbox') {\n");

		Collection inrepps = getCourseApplicationSession(iwc).getUserApplications(getApplicant(iwc));
		if (inrepps != null && !inrepps.isEmpty()) {
			script4.append("\t\t\t\t if (");
			Iterator iter = inrepps.iterator();
			boolean first = true;
			while (iter.hasNext()) {
				if (!first) {
					script4.append(" || ");
				}
				script4.append("obj.id == " + ((ApplicationHolder) iter.next()).getCourse().getPrimaryKey().toString());
				first = false;
			}
			script4.append(") {obj.disabled=true;obj.checked=true}\n");
		}
		script4.append("\t\t\t}\n").append("\t\t\ttds[colNum].className=\"column\"+colNum;\n").append("\t\t}\n").append("\t\tvar tds3 = currentRow.childNodes;\n").append("\t}\n")

		.append("}");

		StringBuffer script5 = new StringBuffer();
		script5.append("function getRadio(course) { \n").append("\nvar sel = (course.pk == 0);\n");
		if (iwc.isIE()) {
			script5.append("\n\t").append("var radio = document.createElement('<input id=\"' + course.pk + '\" value=\"' + course.pk + '\" class=\"checkbox\" type=\"radio\" name=\"" + PARAMETER_COURSE + "\">');");
			
		}
		else {
			script5.append("\n\t").append("var radio = document.createElement(\"input\");");
			script5.append("\n\t").append("radio.id = course.pk;");
			script5.append("\n\t").append("radio.type = \"radio\";");
			script5.append("\n\t").append("radio.className = \"checkbox\";");
			script5.append("\n\t").append("radio.name = \"" + PARAMETER_COURSE + "\";");
			script5.append("\n\t").append("radio.value = course.pk;");
		}
		script5.append("\n\t").append("return radio;");
		script5.append("}\n");

		List jsActions = new ArrayList();
		jsActions.add(script2.toString());
		jsActions.add(script.toString());
		jsActions.add(script3.toString());
		jsActions.add(script4.toString());
		jsActions.add(script5.toString());
		PresentationUtil.addJavaScriptActionsToBody(iwc, jsActions);

		addErrors(iwc, form);

		Heading1 heading = new Heading1(this.iwrb.getLocalizedString("application.application_name", "Course application"));
		heading.setStyleClass("applicationHeading");
		form.add(heading);

		form.add(getPhasesHeader(iwrb.getLocalizedString("course", "Course"), 1, numberOfPhases, true));

		Layer info = new Layer(Layer.DIV);
		info.setStyleClass("info");
		form.add(info);

		Paragraph infoHelp = new Paragraph();
		infoHelp.setStyleClass("infoHelp");
		infoHelp.add(new Text(this.iwrb.getLocalizedString("application.application_help", "Below you can select the type of courses you want to register to if there are any available.<br />The courses are held at the main office unless otherwise stated.<br /><br />Please select the courses you want to register to and click 'Next'.")));
		info.add(infoHelp);

		heading = new Heading1(this.iwrb.getLocalizedString("application.select_course_type", "Select course type"));
		heading.setStyleClass("subHeader");
		heading.setStyleClass("topSubHeader");
		form.add(heading);

		Layer section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		Layer helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("application.select_course_type_help", "Please select the course type you want to register to.  After you have selected the available courses will be displayed below.")));
		section.add(helpLayer);

		boolean useDWR = iwc.getApplicationSettings().getBoolean(CourseConstants.PROPERTY_USE_DWR, true);

		Integer schoolTypePK = getSchoolTypePK() != null ? new Integer(getSchoolTypePK().toString()) : null;
		if (schoolTypePK == null) {
			if (iwc.isParameterSet(PARAMETER_CATEGORY)) {
				schoolTypePK = new Integer(iwc.getParameter(PARAMETER_CATEGORY));
				if (schoolTypePK.intValue() < 0) {
					schoolTypePK = null;
				}
			}

			Collection schoolTypes = getCourseBusiness(iwc).getAllSchoolTypes();
			DropdownMenu catMenu = new DropdownMenu(schoolTypes, PARAMETER_CATEGORY);
			catMenu.addMenuElementFirst("", iwrb.getLocalizedString("select_school_type", "Select school type"));
			catMenu.setId(PARAMETER_CATEGORY);
			catMenu.keepStatusOnAction(true);
			catMenu.setOnChange("changeValues();");

			Layer formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			Label label = new Label(new Span(new Text(this.iwrb.getLocalizedString("category", "Category"))), catMenu);
			formItem.add(label);
			formItem.add(catMenu);
			section.add(formItem);
		}

		DropdownMenu typeMenu = new DropdownMenu(PARAMETER_COURSE_TYPE);
		typeMenu.setId(PARAMETER_COURSE_TYPE);
		if (schoolTypePK != null) {
			Collection courseTypes = getCourseBusiness(iwc).getCourseTypes(schoolTypePK);
			typeMenu.addMenuElements(courseTypes);
		}
		typeMenu.addMenuElementFirst("-1", iwrb.getLocalizedString("select_course_type", "Select course type"));
		if (useDWR) {
			typeMenu.setOnChange("getCourses();");
		}
		else {
			typeMenu.setToSubmit();
		}
		typeMenu.keepStatusOnAction(true);

		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		Label label = new Label(new Span(new Text(this.iwrb.getLocalizedString("type", "Type"))), typeMenu);
		formItem.add(label);
		formItem.add(typeMenu);
		section.add(formItem);

		Integer courseTypePK = null;
		if (iwc.isParameterSet(PARAMETER_COURSE_TYPE)) {
			courseTypePK = new Integer(iwc.getParameter(PARAMETER_COURSE_TYPE));
			if (courseTypePK.intValue() < 0) {
				courseTypePK = null;
			}
		}

		Integer providerPK = null;
		if (iwc.isParameterSet(PARAMETER_PROVIDER)) {
			providerPK = new Integer(iwc.getParameter(PARAMETER_PROVIDER));
			if (providerPK.intValue() < 0) {
				providerPK = null;
			}
		}

		Collection courses = null;
		if (courseTypePK != null) {
			courses = getCourseBusiness(iwc).getCoursesDWR(providerPK != null ? providerPK.intValue() : -1, schoolTypePK.intValue(), courseTypePK.intValue(), -1, iwc.getCurrentLocale().getCountry(), this.iUseSessionUser);
		}

		Table2 table = new Table2();
		table.setStyleClass("courses");
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		row.setStyleClass("header");
		TableCell2 cell = row.createHeaderCell();
		cell.setStyleClass("column0");

		cell = row.createHeaderCell();
		cell.setStyleClass("column1");
		cell.add(new Text(iwrb.getLocalizedString("course", "Course")));
		cell = row.createHeaderCell();
		cell.setStyleClass("column2");
		cell.add(new Text(iwrb.getLocalizedString("date", "Date")));
		cell = row.createHeaderCell();
		cell.setStyleClass("column3");
		cell.add(new Text(iwrb.getLocalizedString("provider", "Provider")));

		group = table.createBodyRowGroup();
		group.setId(PARAMETER_COURSE_TABLE_ID);
		if (courses != null) {
			Iterator iter = courses.iterator();
			int counter = 0;
			while (iter.hasNext()) {
				row = group.createRow();
				if (counter++ % 2 == 0) {
					row.setStyleClass("even");
				}
				else {
					row.setStyleClass("odd");
				}
				CourseDWR course = (CourseDWR) iter.next();
				cell = row.createCell();
				cell.setStyleClass("column0");

				RadioButton radio = new RadioButton(PARAMETER_COURSE, course.getPk());
				radio.setStyleClass("checkbox");
				radio.keepStatusOnAction(true);
				cell.add(radio);

				cell = row.createCell();
				cell.setStyleClass("column1");
				cell.add(new Text(course.getName()));

				cell = row.createCell();
				cell.setStyleClass("column2");
				cell.add(new Text(course.getTimeframe()));

				cell = row.createCell();
				cell.setStyleClass("column3");
				cell.add(new Text(course.getProvider()));
			}
		}

		heading = new Heading1(this.iwrb.getLocalizedString("available_courses", "Available courses"));
		heading.setStyleClass("subHeader");
		form.add(heading);

		section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		section.setStyleClass("formTableSection");
		form.add(section);

		helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("application.select_course_help", "Please select the course you want to register to.")));
		section.add(helpLayer);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.add(table);
		section.add(formItem);

		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		Link link = getButtonLink(this.iwrb.getLocalizedString("next", "Next"));
		link.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_2));
		link.setToFormSubmit(form);
		bottom.add(link);

		add(form);
	}

	private void showPhaseTwo(IWContext iwc) throws RemoteException {
		if (!iwc.isParameterSet(PARAMETER_COURSE)) {
			setError(ACTION_PHASE_2, PARAMETER_COURSE, this.iwrb.getLocalizedString("must_select_course", "You have to select a course"));
		}

		if (hasErrors(ACTION_PHASE_2)) {
			showPhaseOne(iwc);
			return;
		}
		
		if (iwc.isParameterSet(PARAMETER_TRIGGER_SAVE)) {
			addApplication(iwc);
		}

		Form form = getForm(iwc, ACTION_PHASE_2);
		form.setId("course_step_2");
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_2)));

		addDefaulScriptFiles(iwc);

		addErrors(iwc, form);

		Heading1 heading = new Heading1(this.iwrb.getLocalizedString("application.application_name", "Course application"));
		heading.setStyleClass("applicationHeading");
		form.add(heading);

		form.add(getPhasesHeader(iwrb.getLocalizedString("applicant", "Applicant"), 2, numberOfPhases, true));

		Layer info = new Layer(Layer.DIV);
		info.setStyleClass("info");
		form.add(info);

		if (getCourseApplicationSession(iwc).getApplications().size() <= 0) {
			heading = new Heading1(this.iwrb.getLocalizedString("application.select_registrator", "Select registrator"));
			heading.setStyleClass("subHeader");
			heading.setStyleClass("topSubHeader");
			form.add(heading);
	
			Layer section = new Layer(Layer.DIV);
			section.setStyleClass("formSection");
			form.add(section);
	
			Layer helpLayer = new Layer(Layer.DIV);
			helpLayer.setStyleClass("helperText");
			helpLayer.add(new Text(this.iwrb.getLocalizedString("application.select_applicant_help", "Please select if you are registering yourself or on behalf of a company.  If you are registering on behalf of a company you will be able to enter company information later.")));
			section.add(helpLayer);
			
			RadioButton ownRegistration = new RadioButton(PARAMETER_APPLICANT_OPTION, Boolean.TRUE.toString());
			ownRegistration.setStyleClass("radiobutton");
			ownRegistration.setSelected(true);
			ownRegistration.keepStatusOnAction(true);
	
			RadioButton companyRegistration = new RadioButton(PARAMETER_APPLICANT_OPTION, Boolean.FALSE.toString());
			companyRegistration.setStyleClass("radiobutton");
			companyRegistration.keepStatusOnAction(true);
	
			Layer formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			formItem.setStyleClass("radioButtonItem");
			Label label = new Label(this.iwrb.getLocalizedString("application.own_registration", "Own registration"), ownRegistration);
			formItem.add(ownRegistration);
			formItem.add(label);
			section.add(formItem);
	
			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			formItem.setStyleClass("radioButtonItem");
			label = new Label(this.iwrb.getLocalizedString("application.company_registration", "Company registration"), companyRegistration);
			formItem.add(companyRegistration);
			formItem.add(label);
			section.add(formItem);
		}
		else {
			form.maintainParameter(PARAMETER_APPLICANT_OPTION);
		}
		
		heading = new Heading1(iwrb.getLocalizedString("type_social_security_number", "Type in social security number"));
		heading.setStyleClass("subHeader");
		form.add(heading);

		Layer section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		TextInput personalID = new TextInput(PARAMETER_PERSONAL_ID);
		personalID.setMaxlength(10);
		if (!iwc.isParameterSet(PARAMETER_TRIGGER_SAVE)) {
			personalID.keepStatusOnAction(true);
		}

		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		Label label = new Label(this.iwrb.getLocalizedString("social_security_number", "Social security number"), personalID);
		formItem.add(label);
		formItem.add(personalID);
		section.add(formItem);

		Layer helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("select_applicant_helper", "Please select an applicant by writing in the personal ID in the field to the left.")));
		section.add(helpLayer);

		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		Link link = getButtonLink(this.iwrb.getLocalizedString("back", "Back"));
		if (iwc.isParameterSet(PARAMETER_TRIGGER_SAVE)) {
			link.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_4));
		}
		else {
			link.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_1));
		}
		link.setToFormSubmit(form);
		bottom.add(link);

		link = getButtonLink(this.iwrb.getLocalizedString("next", "Next"));
		link.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_3));
		link.setToFormSubmit(form);
		bottom.add(link);

		add(form);
	}

	private void showPhaseThree(IWContext iwc) throws RemoteException {
		User applicant = getApplicant(iwc);
		if (applicant == null) {
			String personalID = iwc.isParameterSet(PARAMETER_PERSONAL_ID) ? iwc.getParameter(PARAMETER_PERSONAL_ID) : null;
			if (personalID == null) {
				setError(ACTION_PHASE_3, PARAMETER_PERSONAL_ID, this.iwrb.getLocalizedString("must_enter_personal_id", "You have to enter a personal ID"));
			}
			else {
				setError(ACTION_PHASE_3, PARAMETER_PERSONAL_ID, this.iwrb.getLocalizedString("no_user_found_with_personal_id", "No user found with personal ID") + ": " + personalID);
			}
		}
		else {
			//	Checking personal id
			if (!getUserBusiness(iwc).hasValidIcelandicSSN(applicant)) {
				setError(ACTION_PHASE_3, PARAMETER_PERSONAL_ID, this.iwrb.getLocalizedString("invalid_personal_id", "Invalid personal ID"));
			}
			//	Checking age
			Date dateOfBirth = applicant.getDateOfBirth();
			if (dateOfBirth == null) {
				dateOfBirth = getUserBusiness(iwc).getUserDateOfBirthFromPersonalId(applicant.getPersonalID());
			}
			if (dateOfBirth == null) {
				setError(ACTION_PHASE_3, PARAMETER_APPLICANT_AGE, this.iwrb.getLocalizedString("unknown_age_for_applicant", "Unkown age of applicant."));
			}
			else {
				Age age = new Age(dateOfBirth);
				if (age.getYears() < 19) {
					setError(ACTION_PHASE_3, PARAMETER_APPLICANT_AGE, this.iwrb.getLocalizedString("applicant_is_too_young", "Applicant is too young. Minimal age requirement is") + ": 19.");
				}
			}
			//	Checking if user already registered for current course
			Course course = getCourseBusiness(iwc).getCourse(iwc.getParameter(PARAMETER_COURSE));
			if (getCourseBusiness(iwc).isRegistered(applicant, course)) {
				setError(ACTION_PHASE_3, PARAMETER_PERSONAL_ID, this.iwrb.getLocalizedString("already_registered_to_course", "The selected applicant is already registered to the course you selected."));
			}
			//	Checking if user is not too late applying for "continuing education"
			IWTimestamp latestExpireDate = getCourseBusiness(iwc).getLatestExpirationDateOfCertificate(getCourseBusiness(iwc).getUserCertificatesByCourse(applicant, course));
			if (latestExpireDate != null) {
				IWTimestamp rightNow = IWTimestamp.RightNow();
				if (latestExpireDate.isEarlierThan(rightNow)) {
					latestExpireDate.setYear(latestExpireDate.getYear() + 2);
					if (latestExpireDate.isEarlierThan(rightNow)) {
						setError(PARAMETER_TOO_LATE_APPLYING_FOR_CONTINUING_EDUCATION, this.iwrb.getLocalizedString("too_late_applying_for_continuing_education", "Too late applying for continuing education."));
					}
				}
			}
		}

		if (hasErrors(ACTION_PHASE_3)) {
			showPhaseTwo(iwc);
			return;
		}

		Form form = getForm(iwc, ACTION_PHASE_3);
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_3)));

		addErrors(iwc, form);

		Heading1 heading = new Heading1(this.iwrb.getLocalizedString("application.application_name", "Course application"));
		heading.setStyleClass("applicationHeading");
		form.add(heading);

		form.add(getPhasesHeader(this.iwrb.getLocalizedString("application.applicant_information", "Applicant information"), 3, numberOfPhases));

		form.add(getPersonInfo(iwc, applicant));

		heading = new Heading1(this.iwrb.getLocalizedString("application.contact_information", "Contact information"));
		heading.setStyleClass("subHeader");
		heading.setStyleClass("topSubHeader");
		form.add(heading);

		Layer section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		Layer helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("application.contact_information_help", "Please enter contact information for the selected applicant.")));
		section.add(helpLayer);

		Phone homePhone = null;
		try {
			homePhone = getUserBusiness(iwc).getUsersHomePhone(applicant);
		}
		catch (NoPhoneFoundException e) {
			//No phone found...
		}

		TextInput input = new TextInput(PARAMETER_HOME_PHONE);
		if (homePhone != null) {
			input.setContent(homePhone.getNumber());
		}

		Layer formItem = new Layer();
		formItem.setStyleClass("formItem");
		Label label = new Label(this.iwrb.getLocalizedString("home_phone", "Home phone"), input);
		formItem.add(label);
		formItem.add(input);
		section.add(formItem);

		Phone workPhone = null;
		try {
			workPhone = getUserBusiness(iwc).getUsersWorkPhone(applicant);
		}
		catch (NoPhoneFoundException e) {
			//No phone found...
		}

		input = new TextInput(PARAMETER_WORK_PHONE);
		if (workPhone != null) {
			input.setContent(workPhone.getNumber());
		}

		formItem = new Layer();
		formItem.setStyleClass("formItem");
		label = new Label(this.iwrb.getLocalizedString("work_phone", "Work phone"), input);
		formItem.add(label);
		formItem.add(input);
		section.add(formItem);

		Phone mobilePhone = null;
		try {
			mobilePhone = getUserBusiness(iwc).getUsersMobilePhone(applicant);
		}
		catch (NoPhoneFoundException e) {
			//No phone found...
		}

		input = new TextInput(PARAMETER_MOBILE_PHONE);
		if (mobilePhone != null) {
			input.setContent(mobilePhone.getNumber());
		}

		formItem = new Layer();
		formItem.setStyleClass("formItem");
		label = new Label(this.iwrb.getLocalizedString("mobile_phone", "Mobile phone"), input);
		formItem.add(label);
		formItem.add(input);
		section.add(formItem);

		Email email = null;
		try {
			email = getUserBusiness(iwc).getUsersMainEmail(applicant);
		}
		catch (NoEmailFoundException e) {
			//No email found...
		}

		input = new TextInput(PARAMETER_EMAIL);
		if (email != null) {
			input.setContent(email.getEmailAddress());
		}

		formItem = new Layer();
		formItem.setStyleClass("formItem");
		label = new Label(this.iwrb.getLocalizedString("email", "Email"), input);
		formItem.add(label);
		formItem.add(input);
		section.add(formItem);

		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		Link link = getButtonLink(this.iwrb.getLocalizedString("back", "Back"));
		link.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_2));
		link.setToFormSubmit(form);
		bottom.add(link);

		link = getButtonLink(this.iwrb.getLocalizedString("next", "Next"));
		link.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_4));
		link.setToFormSubmit(form);
		bottom.add(link);

		add(form);
	}

	private void showPhaseFour(IWContext iwc) throws RemoteException {
		User applicant = getApplicant(iwc);
		if (!storeContactInfo(iwc, applicant)) {
			showPhaseThree(iwc);
			return;
		}

		Form form = getForm(iwc, ACTION_PHASE_4);
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_4)));
		form.add(new HiddenInput(PARAMETER_TRIGGER_SAVE, ""));

		addErrors(iwc, form);

		Heading1 heading = new Heading1(this.iwrb.getLocalizedString("application.application_name", "Course application"));
		heading.setStyleClass("applicationHeading");
		form.add(heading);

		form.add(getPhasesHeader(this.iwrb.getLocalizedString("application.applicant_information", "Applicant information"), 4, numberOfPhases));

		form.add(getPersonInfo(iwc, applicant));

		heading = new Heading1(this.iwrb.getLocalizedString("application.student_help_information", "Student help information"));
		heading.setStyleClass("subHeader");
		heading.setStyleClass("topSubHeader");
		form.add(heading);

		Layer section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		Layer helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("application.dyslexia_help", "Please select checkbox if student needs help on account of dyslexia.")));
		section.add(helpLayer);

		CheckBox hasDyslexia = new CheckBox(PARAMETER_HAS_DYSLEXIA, Boolean.TRUE.toString());
		hasDyslexia.setStyleClass("checkbox");
		hasDyslexia.keepStatusOnAction(true);

		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("radioButtonItem");
		Label label = new Label(new Span(new Text(this.iwrb.getLocalizedString("application.has_dyslexia", "Has dyslexia"))), hasDyslexia);
		formItem.add(hasDyslexia);
		formItem.add(label);
		section.add(formItem);

		if (getCourseApplicationSession(iwc).getApplications().size() <= 0) {
			heading = new Heading1(this.iwrb.getLocalizedString("application.agreement_info", "Agreement information"));
			heading.setStyleClass("subHeader");
			form.add(heading);
	
			section = new Layer(Layer.DIV);
			section.setStyleClass("formSection");
			form.add(section);
	
			CheckBox agree = new CheckBox(PARAMETER_AGREEMENT, Boolean.TRUE.toString());
			agree.setStyleClass("checkbox");
			agree.keepStatusOnAction(true);
	
			Paragraph paragraph = new Paragraph();
			paragraph.setStyleClass("agreement");
			paragraph.add(new Text(this.iwrb.getLocalizedString("application.agreement", "Agreement text")));
			section.add(paragraph);
	
			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			formItem.setStyleClass("radioButtonItem");
			formItem.setStyleClass("required");
			if (hasError(PARAMETER_AGREEMENT)) {
				formItem.setStyleClass("hasError");
			}
			label = new Label(new Span(new Text(this.iwrb.getLocalizedString("application.agree_terms", "Yes, I agree"))), agree);
			formItem.add(agree);
			formItem.add(label);
			section.add(formItem);
		}
		else {	
			form.add(new HiddenInput(PARAMETER_AGREEMENT, Boolean.TRUE.toString()));
		}
		
		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		Link link = getButtonLink(this.iwrb.getLocalizedString("back", "Back"));
		link.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_3));
		link.setToFormSubmit(form);
		bottom.add(link);

		link = getButtonLink(this.iwrb.getLocalizedString("next", "Next"));
		link.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_5));
		link.setToFormSubmit(form);
		bottom.add(link);

		if (iCompanyRegistration) {
			Link newAppl = getButtonLink(this.iwrb.getLocalizedString("register_another_applicant", "Another applicant"));
			newAppl.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_2));
			newAppl.setValueOnClick(PARAMETER_TRIGGER_SAVE, Boolean.TRUE.toString());
			newAppl.setToFormSubmit(form);
			bottom.add(newAppl);
		}
		
		add(form);
	}

	private void showPhaseFive(IWContext iwc) throws RemoteException {
		if (!iwc.isParameterSet(PARAMETER_AGREEMENT)) {
			setError(ACTION_PHASE_5, PARAMETER_AGREEMENT, this.iwrb.getLocalizedString("must_agree_terms", "You have to agree to the terms."));
		}

		if (hasErrors(ACTION_PHASE_5)) {
			showPhaseFour(iwc);
			return;
		}

		addApplication(iwc);

		Form form = getForm(iwc, ACTION_PHASE_5);
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_SAVE)));

		addErrors(iwc, form);

		addDefaulScriptFiles(iwc);

		StringBuffer script = new StringBuffer();
		script.append("function readUser() {\n\tvar id = DWRUtil.getValue(\"" + PARAMETER_PAYER_PERSONAL_ID + "\");\n\tCourseDWRUtil.getUserDWR(id, -1, 18, '" + iwc.getCurrentLocale().getCountry() + "', fillUser);\n}");

		StringBuffer script2 = new StringBuffer();
		script2.append("function fillUser(auser) {\n\tuser = auser;\n\tDWRUtil.setValues(user);\n}");

		PresentationUtil.addJavaScriptActionToBody(iwc, script.toString());
		PresentationUtil.addJavaScriptActionToBody(iwc, script2.toString());

		Heading1 heading = new Heading1(this.iwrb.getLocalizedString("application.application_name", "Course application"));
		heading.setStyleClass("applicationHeading");
		form.add(heading);

		form.add(getPhasesHeader(this.iwrb.getLocalizedString("application.payment_information", "Payment information"), 5, numberOfPhases));

		form.add(getPersonInfo(iwc, null));

		heading = new Heading1(this.iwrb.getLocalizedString("application.amount_information", "Amount information"));
		heading.setStyleClass("subHeader");
		heading.setStyleClass("topSubHeader");
		form.add(heading);

		Layer section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		section.setStyleClass("formTableSection");
		form.add(section);

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");

		Layer helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("application.amount_information_help", "This is the total amount due for all selected courses.")));
		section.add(helpLayer);

		Table2 table = new Table2();
		table.setStyleClass("courses");
		table.setStyleClass("coursesPhaseSix");
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		row.setStyleClass("header");

		TableCell2 cell = row.createHeaderCell();
		cell.setStyleClass("name");
		cell.add(new Text(iwrb.getLocalizedString("name", "Name")));

		cell = row.createHeaderCell();
		cell.setStyleClass("personalID");
		cell.add(new Text(iwrb.getLocalizedString("personal_id", "Personal ID")));

		cell = row.createHeaderCell();
		cell.setStyleClass("courseFee");
		cell.add(new Text(iwrb.getLocalizedString("course_fee", "Course fee")));

		String defaultCertificateFee = iwc.getApplicationSettings().getProperty(CourseConstants.DEFAULT_COURSE_CERTIFICATE_FEE);
		float certificateFee = 0;
		if (defaultCertificateFee == null) {
			iwc.getApplicationSettings().setProperty(CourseConstants.DEFAULT_COURSE_CERTIFICATE_FEE, String.valueOf(0));
		}
		else {
			try {
				certificateFee = Float.valueOf(defaultCertificateFee).floatValue();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		cell = row.createHeaderCell();
		cell.setStyleClass("certificateFee");
		cell.add(new Text(iwrb.getLocalizedString("certificate_fee", "Certificate fee")));

		cell = row.createHeaderCell();
		cell.setStyleClass("amount");
		cell.add(new Text(iwrb.getLocalizedString("amount", "Amount")));

		group = table.createBodyRowGroup();

		Map applications = getCourseApplicationSession(iwc).getApplications();
		SortedSet prices = getCourseBusiness(iwc).calculatePrices(applications);

		NumberFormat format = NumberFormat.getInstance(iwc.getCurrentLocale());
		float totalPrice = 0;
		int counter = 0;
		Iterator iter = prices.iterator();
		float certificateFees = 0;
		while (iter.hasNext()) {
			row = group.createRow();
			PriceHolder holder = (PriceHolder) iter.next();
			User user = holder.getUser();

			float price = holder.getPrice();
			totalPrice += price;

			Name name = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());
			cell = row.createCell();
			cell.setStyleClass("name");
			cell.add(new Text(name.getName(iwc.getCurrentLocale())));

			cell = row.createCell();
			cell.setStyleClass("personalID");
			cell.add(new Text(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale())));

			cell = row.createCell();
			cell.setStyleClass("courseFee");
			cell.add(new Text(format.format(price)));

			cell = row.createCell();
			cell.setStyleClass("certificateFee");
			if (holder.getCost() > 0) {
				cell.add(new Text(format.format(holder.getCost())));
				totalPrice += holder.getCost();
				price += holder.getCost();
			}
			else if (certificateFee >= 0) {
				cell.add(new Text(format.format(certificateFee)));
				totalPrice += certificateFee;
				price += certificateFee;
			}
			else {
				cell.add(Text.getNonBrakingSpace());
			}

			cell = row.createCell();
			cell.setStyleClass("amount");
			cell.add(new Text(format.format(price)));

			if (counter++ % 2 == 0) {
				row.setStyleClass("even");
			}
			else {
				row.setStyleClass("odd");
			}
		}

		group = table.createFooterRowGroup();
		row = group.createRow();

		cell = row.createCell();
		cell.setColumnSpan(3);
		cell.setStyleClass("totalPrice");
		cell.add(new Text(iwrb.getLocalizedString("total_amount", "Total amount")));

		cell = row.createCell();
		cell.setStyleClass("totalPrice");
		cell.setStyleClass("price");
		cell.add(new Text(format.format(totalPrice)));

		float amountDue = totalPrice;
		section.add(new HiddenInput(PARAMETER_AMOUNT, Float.toString(amountDue - certificateFees)));
		section.add(new HiddenInput(PARAMETER_AMOUNT_OF_CERTIFICATE_FEES, Float.toString(certificateFees)));

		section.add(table);

		heading = new Heading1(this.iwrb.getLocalizedString("application.reference_number_information", "Reference number information"));
		heading.setStyleClass("subHeader");
		form.add(heading);

		section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("application.reference_number_help", "Please enter information about reference number.")));
		section.add(helpLayer);

		TextInput referenceNumber = new TextInput(PARAMETER_REFERENCE_NUMBER, null);
		referenceNumber.keepStatusOnAction(true);

		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		Label label = new Label(this.iwrb.getLocalizedString("application.reference_number", "Reference number"), referenceNumber);
		formItem.add(label);
		formItem.add(referenceNumber);
		section.add(formItem);

		section.add(clearLayer);

		if (this.iCompanyRegistration || this.iUseSessionUser) {
			heading = new Heading1(this.iwrb.getLocalizedString("application.payer_information", "Payer information"));
			heading.setStyleClass("subHeader");
			form.add(heading);

			section = new Layer(Layer.DIV);
			section.setStyleClass("formSection");
			form.add(section);

			helpLayer = new Layer(Layer.DIV);
			helpLayer.setStyleClass("helperText");
			helpLayer.add(new Text(this.iwrb.getLocalizedString("application.other_payer_information_help", "Please enter informations about the destined payer, if other than yourself.")));
			section.add(helpLayer);

			TextInput payerName = new TextInput(PARAMETER_PAYER_NAME, null);
			payerName.setId("userName");
			payerName.keepStatusOnAction(true);

			TextInput payerPersonalID = new TextInput(PARAMETER_PAYER_PERSONAL_ID, null);
			payerPersonalID.setId(PARAMETER_PAYER_PERSONAL_ID);
			payerPersonalID.keepStatusOnAction(true);
			payerPersonalID.setMaxlength(10);
			payerPersonalID.setOnKeyUp("readUser();");
			payerPersonalID.setOnChange("readUser();");

			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			label = new Label(this.iwrb.getLocalizedString("application.payer_personal_id", "Personal ID"), payerPersonalID);
			formItem.add(label);
			formItem.add(payerPersonalID);
			section.add(formItem);

			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			label = new Label(this.iwrb.getLocalizedString("application.payer_name", "Name"), payerName);
			formItem.add(label);
			formItem.add(payerName);
			section.add(formItem);

			section.add(clearLayer);
		}
		else {
			User user = iwc.getCurrentUser();
			form.add(new HiddenInput(PARAMETER_PAYER_PERSONAL_ID, user.getPersonalID()));
			form.add(new HiddenInput(PARAMETER_PAYER_NAME, new Name(user.getFirstName(), user.getMiddleName(), user.getLastName()).getName(iwc.getCurrentLocale())));
		}

		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		Link back = getButtonLink(this.iwrb.getLocalizedString("back", "Back"));
		back.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_4));
		back.setToFormSubmit(form);
		bottom.add(back);

		Link next = getButtonLink(this.iwrb.getLocalizedString("next", "Next"));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SAVE));
		next.setOnClick("this.style.display='none';");
		next.setToFormSubmit(form);
		bottom.add(next);

		add(form);
	}

	private void addDefaulScriptFiles(IWContext iwc) {
		List scriptFiles = new ArrayList();
		scriptFiles.add("/dwr/interface/CourseDWRUtil.js");
		scriptFiles.add(CoreConstants.DWR_ENGINE_SCRIPT);
		scriptFiles.add(CoreConstants.DWR_UTIL_SCRIPT);
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, scriptFiles);
	}

	private void save(IWContext iwc) throws RemoteException {
		if (!iwc.isParameterSet(PARAMETER_PAYER_PERSONAL_ID)) {
			setError(PARAMETER_PAYER_PERSONAL_ID, this.iwrb.getLocalizedString("payer_personal_id_empty", "Payer personal ID is empty"));
		}
		else if (!SocialSecurityNumber.isValidSocialSecurityNumber(iwc.getParameter(PARAMETER_PAYER_PERSONAL_ID), iwc.getCurrentLocale())) {
			setError(PARAMETER_PAYER_PERSONAL_ID, this.iwrb.getLocalizedString("invalid_personal_id", "Invalid personal ID"));
		}
		else if (SocialSecurityNumber.isIndividualSocialSecurityNumber(iwc.getParameter(PARAMETER_PAYER_PERSONAL_ID), iwc.getCurrentLocale())) {
			Date dateOfBirth = SocialSecurityNumber.getDateFromSocialSecurityNumber(iwc.getParameter(PARAMETER_PAYER_PERSONAL_ID));
			Age age = new Age(dateOfBirth);
			if (age.getYears() < 18) {
				setError(PARAMETER_PAYER_PERSONAL_ID, this.iwrb.getLocalizedString("payer_must_be_at_least_18_years_old", "The payer you have selected is younger than 18 years old. Payers must be at least 18 years old or older."));
			}

			try {
				getUserBusiness(iwc).getUser(iwc.getParameter(PARAMETER_PAYER_PERSONAL_ID));
			}
			catch (FinderException e) {
				setError(PARAMETER_PAYER_PERSONAL_ID, this.iwrb.getLocalizedString("application_error.no_user_found_with_personal_id", "No user found with the personal ID you entered."));
			}
		}
		if (!iwc.isParameterSet(PARAMETER_PAYER_NAME)) {
			setError(PARAMETER_PAYER_NAME, this.iwrb.getLocalizedString("payer_name_empty", "Payer name is empty"));
		}

		if (hasErrors()) {
			showPhaseFive(iwc);
			return;
		}

		String payerPersonalID = iwc.isParameterSet(PARAMETER_PAYER_PERSONAL_ID) ? iwc.getParameter(PARAMETER_PAYER_PERSONAL_ID) : "";
		String payerName = iwc.isParameterSet(PARAMETER_PAYER_NAME) ? iwc.getParameter(PARAMETER_PAYER_NAME) : "";
		double amount = Double.parseDouble(iwc.getParameter(PARAMETER_AMOUNT));
		float certificateFees = Float.parseFloat(iwc.getParameter(PARAMETER_AMOUNT_OF_CERTIFICATE_FEES));
		String referenceNumber = iwc.getParameter(PARAMETER_REFERENCE_NUMBER);

		if (hasErrors()) {
			showPhaseFive(iwc);
			return;
		}

		is.idega.idegaweb.egov.course.data.CourseApplication application = getCourseBusiness(iwc).saveApplication(getCourseApplicationSession(iwc).getApplications(), -1, (float) amount, null, CourseConstants.PAYMENT_TYPE_BANK_TRANSFER, referenceNumber, payerName, payerPersonalID, getUser(iwc), iwc.getCurrentLocale(), certificateFees);

		if (hasErrors()) {
			showPhaseFive(iwc);
			return;
		}

		if (application != null) {
			getCourseApplicationSession(iwc).clear(iwc);
			Heading1 heading = new Heading1(this.iwrb.getLocalizedString("application.application_name", "Course application"));
			heading.setStyleClass("applicationHeading");
			add(heading);

			addPhasesReceipt(iwc, this.iwrb.getLocalizedString("application.receipt", "Application receipt"), this.iwrb.getLocalizedString("application.application_save_completed", "Application sent"), this.iwrb.getLocalizedString("application.application_send_confirmation", "Your course application has been received and will be processed."), 6, numberOfPhases);

			Layer clearLayer = new Layer(Layer.DIV);
			clearLayer.setStyleClass("Clear");
			add(clearLayer);

			Layer bottom = new Layer(Layer.DIV);
			bottom.setStyleClass("bottom");
			add(bottom);

			try {
				ICPage page = getUserBusiness(iwc).getHomePageForUser(iwc.getCurrentUser());
				Link link = getButtonLink(this.iwrb.getLocalizedString("my_page", "My page"));
				link.setStyleClass("homeButton");
				link.setPage(page);
				bottom.add(link);
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}

			Link receipt = getButtonLink(this.iwrb.getLocalizedString("receipt", "Receipt"));
			receipt.setWindowToOpen(CourseApplicationOverviewWindow.class);
			receipt.addParameter(getCourseBusiness(iwc).getSelectedCaseParameter(), application.getPrimaryKey().toString());
			bottom.add(receipt);
		}
		else {
			Heading1 heading = new Heading1(this.iwrb.getLocalizedString("application.application_name", "Course application"));
			heading.setStyleClass("applicationHeading");
			add(heading);

			add(getPhasesHeader(this.iwrb.getLocalizedString("application.submit_failed", "Application submit failed"), 7, numberOfPhases));
			add(getStopLayer(this.iwrb.getLocalizedString("application.submit_failed", "Application submit failed"), this.iwrb.getLocalizedString("application.submit_failed_info", "Application submit failed")));
		}
	}

	private int parseAction(IWContext iwc) {
		int action = ACTION_PHASE_1;
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}

		if (iwc.isParameterSet(PARAMETER_APPLICANT_OPTION)) {
			iCompanyRegistration = !Boolean.valueOf(iwc.getParameter(PARAMETER_APPLICANT_OPTION)).booleanValue();
		}

		return action;
	}

	private boolean addApplication(IWContext iwc) throws RemoteException {
		Object coursePK = iwc.getParameter(PARAMETER_COURSE);
		boolean hasDyslexia = iwc.isParameterSet(PARAMETER_HAS_DYSLEXIA);
		User applicant = getApplicant(iwc);

		if (coursePK != null && applicant != null) {
			ApplicationHolder holder = new ApplicationHolder();
			Course course = getCourseBusiness(iwc).getCourse(new Integer(coursePK.toString()));
			holder.setCourse(course);
			holder.setUser(getApplicant(iwc));
			holder.setHasDyslexia(hasDyslexia);

			getCourseApplicationSession(iwc).addApplication(applicant, holder);
		}

		return true;
	}

	private User getApplicant(IWContext iwc) {
		String personalID = iwc.getParameter(PARAMETER_PERSONAL_ID);

		if (personalID != null && !personalID.trim().equals("")) {
			try {
				return getUserBusiness(iwc).getUser(personalID);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private boolean storeContactInfo(IWContext iwc, User applicant) throws RemoteException {
		String homePhone = iwc.isParameterSet(PARAMETER_HOME_PHONE) ? iwc.getParameter(PARAMETER_HOME_PHONE) : null;
		String workPhone = iwc.isParameterSet(PARAMETER_WORK_PHONE) ? iwc.getParameter(PARAMETER_WORK_PHONE) : null;
		String mobilePhone = iwc.isParameterSet(PARAMETER_MOBILE_PHONE) ? iwc.getParameter(PARAMETER_MOBILE_PHONE) : null;
		String email = iwc.isParameterSet(PARAMETER_EMAIL) ? iwc.getParameter(PARAMETER_EMAIL) : null;
		if (email != null) {
			if (!EmailValidator.getInstance().validateEmail(email)) {
				setError(PARAMETER_EMAIL, iwrb.getLocalizedString("invalid_email", "Email is not valid"));
				return false;
			}
		}

		if (homePhone != null) {
			getUserBusiness(iwc).updateUserHomePhone(applicant, homePhone);
		}
		if (workPhone != null) {
			getUserBusiness(iwc).updateUserWorkPhone(applicant, workPhone);
		}
		if (mobilePhone != null) {
			getUserBusiness(iwc).updateUserMobilePhone(applicant, mobilePhone);
		}
		if (email != null) {
			try {
				getUserBusiness(iwc).updateUserMail(applicant, email);
			}
			catch (CreateException e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	private User getUser(IWContext iwc) throws RemoteException {
		try {
			return iwc.getCurrentUser();
		}
		catch (NotLoggedOnException nloe) {
			return null;
		}
	}

	private CourseBusiness getCourseBusiness(IWContext iwc) {
		try {
			return (CourseBusiness) IBOLookup.getServiceInstance(iwc, CourseBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	private CourseApplicationSession getCourseApplicationSession(IWContext iwc) {
		try {
			return (CourseApplicationSession) IBOLookup.getSessionInstance(iwc, CourseApplicationSession.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	private Object getSchoolTypePK() {
		return this.iSchoolTypePK;
	}

	public void setSchoolTypePK(String schoolTypePK) {
		this.iSchoolTypePK = schoolTypePK;
	}

	public void setUseSessionUser(boolean useSessionUser) {
		this.iUseSessionUser = useSessionUser;
	}
}