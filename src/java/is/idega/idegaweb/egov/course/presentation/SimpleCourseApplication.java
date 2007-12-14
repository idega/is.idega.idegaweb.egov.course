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
import is.idega.idegaweb.egov.course.business.CourseBusinessBean;
import is.idega.idegaweb.egov.course.business.CourseDWR;
import is.idega.idegaweb.egov.course.data.ApplicationHolder;
import is.idega.idegaweb.egov.course.data.Course;
import is.idega.idegaweb.egov.course.data.PriceHolder;

import java.rmi.RemoteException;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.creditcard.business.CreditCardAuthorizationException;
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
import com.idega.util.EmailValidator;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;
import com.idega.util.text.SocialSecurityNumber;
import com.idega.util.text.TextSoap;

public class SimpleCourseApplication extends ApplicationForm {

	private static final int ACTION_PHASE_1 = 1;
	private static final int ACTION_PHASE_2 = 2;
	private static final int ACTION_PHASE_3 = 3;
	private static final int ACTION_PHASE_4 = 4;
	private static final int ACTION_PHASE_5 = 5;
	private static final int ACTION_PHASE_6 = 6;
	private static final int ACTION_SAVE = 0;

	protected static final String PARAMETER_ACTION = "prm_action";
	protected static final String PARAMETER_BACK = "prm_back";

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

	private static final String PARAMETER_CREDITCARD_PAYMENT = "prm_creditcard_payment";
	private static final String PARAMETER_PAYER_OPTION = "prm_payer_option";
	private static final String PARAMETER_PAYER_NAME = "prm_payer_name";
	private static final String PARAMETER_PAYER_PERSONAL_ID = "prm_payer_personal_id";
	private static final String PARAMETER_NAME_ON_CARD = "prm_name_on_card";
	private static final String PARAMETER_CARD_TYPE = "prm_card_type";
	private static final String PARAMETER_CARD_NUMBER = "prm_card_number";
	private static final String PARAMETER_VALID_MONTH = "prm_valid_month";
	private static final String PARAMETER_VALID_YEAR = "prm_valid_year";
	private static final String PARAMETER_AMOUNT = "prm_amount";
	private static final String PARAMETER_CCV = "prm_ccv";

	private static final String SUB_ACTION = "prm_subact";

	private IWResourceBundle iwrb = null;

	private int numberOfPhases = 7;
	private boolean iUseSessionUser = false;
	private Object iSchoolTypePK = null;
	private boolean iCompanyRegistration = false;

	protected String getCaseCode() {
		return CourseConstants.CASE_CODE_KEY;
	}

	public String getBundleIdentifier() {
		return CourseBusinessBean.IW_BUNDLE_IDENTIFIER;
	}

	protected void present(IWContext iwc) {
		this.iwrb = getResourceBundle(iwc);

		try {
			switch (parseAction(iwc)) {
				case ACTION_PHASE_1:
					if (handleSubAction(iwc)) {

					}
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

				case ACTION_PHASE_6:
					showPhaseSix(iwc);
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

	private boolean handleSubAction(IWContext iwc) throws RemoteException {
		return iwc.isParameterSet(SUB_ACTION);
	}

	private Form getForm(int state) {
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
		if (state != ACTION_PHASE_5) {
			form.maintainParameter(PARAMETER_AGREEMENT);
			form.maintainParameter(PARAMETER_HAS_DYSLEXIA);
		}

		return form;
	}

	private void showPhaseOne(IWContext iwc) throws RemoteException {
		Form form = getForm(ACTION_PHASE_1);
		form.setId("course_step_1");
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_1)));

		super.getParentPage().addJavascriptURL("/dwr/interface/CourseDWRUtil.js");
		super.getParentPage().addJavascriptURL("/dwr/engine.js");
		super.getParentPage().addJavascriptURL("/dwr/util.js");

		StringBuffer script2 = new StringBuffer();
		script2.append("function setOptions(data) {\n").append("\tDWRUtil.removeAllOptions(\"" + PARAMETER_COURSE_TYPE + "\");\n").append("\tDWRUtil.addOptions(\"" + PARAMETER_COURSE_TYPE + "\", data);\n").append("}");

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

		script4.append("function setCourses(data) {\n").append("\tvar isEmpty = true;\n").append("\tfor (var prop in data) { isEmpty = false } \n").append("\tif (isEmpty == true) {\n").append("\t}\n").append("\tDWRUtil.removeAllRows(\"" + PARAMETER_COURSE_TABLE_ID + "\");\n").append("\tDWRUtil.addRows(\"" + PARAMETER_COURSE_TABLE_ID + "\", data, [getRadio, getName, getTimeframe, getProvider, getPrice], { rowCreator:function(options) { var row = document.createElement(\"tr\"); if (options.rowData.isfull) { row.className = \"isfull\" }; return row; }});\n").append("\tvar table = $(\"" + PARAMETER_COURSE_TABLE_ID + "\");\n").append("\tvar trs = table.childNodes;\n").append("\tfor (var rowNum = 0; rowNum < trs.length; rowNum++) {\n").append("\t\tvar currentRow = trs[rowNum];\n").append("\t\tvar tds = currentRow.childNodes;\n").append("\t\tfor (var colNum = 0; colNum < tds.length; colNum++) {\n").append("\t\t\tvar obj = tds[colNum].firstChild;\n").append("\t\t\tif (obj != null && obj.className == 'checkbox') {\n");

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
		script5.append("\n\t").append("var radio = document.createElement(\"input\");");
		script5.append("\n\t").append("radio.id = course.pk;");
		script5.append("\n\t").append("radio.type = \"radio\";");
		script5.append("\n\t").append("radio.className = \"checkbox\";");
		script5.append("\n\t").append("radio.name = \"" + PARAMETER_COURSE + "\";");
		script5.append("\n\t").append("radio.value = course.pk;");
		script5.append("\n\t").append("return radio;");
		//script5.append("\n").append("return '<input id=\"'+course.pk+'\" type=\"checkbox\" class=\"checkbox\" name=\"" + PARAMETER_COURSE + "\" value=\"'+course.pk+'\" />'; \n");
		script5.append("}\n");

		super.getParentPage().getAssociatedScript().addFunction("setOptions", script2.toString());
		super.getParentPage().getAssociatedScript().addFunction("changeValues", script.toString());
		super.getParentPage().getAssociatedScript().addFunction("getCourses", script3.toString());
		super.getParentPage().getAssociatedScript().addFunction("setCourses", script4.toString());
		super.getParentPage().getAssociatedScript().addFunction("setCourseID", script5.toString());

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
		cell = row.createHeaderCell();
		cell.setStyleClass("column4");
		cell.add(new Text(iwrb.getLocalizedString("price", "Price")));

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

				cell = row.createCell();
				cell.setStyleClass("column4");
				cell.add(new Text(course.getPrice()));
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
			setError(PARAMETER_COURSE, this.iwrb.getLocalizedString("must_select_course", "You have to select a course"));
		}

		if (hasErrors()) {
			showPhaseOne(iwc);
			return;
		}

		Form form = getForm(ACTION_PHASE_2);
		form.setId("course_step_2");
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_2)));

		super.getParentPage().addJavascriptURL("/dwr/interface/CourseDWRUtil.js");
		super.getParentPage().addJavascriptURL("/dwr/engine.js");
		super.getParentPage().addJavascriptURL("/dwr/util.js");

		addErrors(iwc, form);

		Heading1 heading = new Heading1(this.iwrb.getLocalizedString("application.application_name", "Course application"));
		heading.setStyleClass("applicationHeading");
		form.add(heading);

		form.add(getPhasesHeader(iwrb.getLocalizedString("applicant", "Applicant"), 2, numberOfPhases, true));

		Layer info = new Layer(Layer.DIV);
		info.setStyleClass("info");
		form.add(info);

		heading = new Heading1(this.iwrb.getLocalizedString("application.select_course_type", "Select course type"));
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
		companyRegistration.setDisabled(true);

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

		heading = new Heading1(iwrb.getLocalizedString("type_social_security_number", "Type in social security number"));
		heading.setStyleClass("subHeader");
		form.add(heading);

		section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		TextInput personalID = new TextInput(PARAMETER_PERSONAL_ID);
		personalID.setMaxlength(10);
		personalID.keepStatusOnAction(true);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(this.iwrb.getLocalizedString("social_security_number", "Social security number"), personalID);
		formItem.add(label);
		formItem.add(personalID);
		section.add(formItem);

		helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("select_applicant_helper", "Please select an applicant by writing in the personal ID in the field to the left.")));
		section.add(helpLayer);

		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		Link link = getButtonLink(this.iwrb.getLocalizedString("next", "Next"));
		link.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_3));
		link.setToFormSubmit(form);
		bottom.add(link);

		link = getButtonLink(this.iwrb.getLocalizedString("back", "Back"));
		link.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_1));
		link.setToFormSubmit(form);
		bottom.add(link);

		add(form);
	}

	private void showPhaseThree(IWContext iwc) throws RemoteException {
		User applicant = getApplicant(iwc);
		if (applicant == null) {
			String personalID = iwc.isParameterSet(PARAMETER_PERSONAL_ID) ? iwc.getParameter(PARAMETER_PERSONAL_ID) : null;
			if (personalID != null) {
				setError(PARAMETER_PERSONAL_ID, this.iwrb.getLocalizedString("no_user_found_with_personal_id", "No user found with personal ID") + ": " + personalID);
			}
			else {
				setError(PARAMETER_PERSONAL_ID, this.iwrb.getLocalizedString("must_enter_personal_id", "You have to enter a personal ID"));
			}
		}
		else {
			Course course = getCourseBusiness(iwc).getCourse(iwc.getParameter(PARAMETER_COURSE));
			if (getCourseBusiness(iwc).isRegistered(applicant, course)) {
				setError(PARAMETER_PERSONAL_ID, this.iwrb.getLocalizedString("already_registered_to_course", "The selected applicant is already registered to the course you selected."));
			}
		}

		if (hasErrors()) {
			showPhaseTwo(iwc);
			return;
		}

		Form form = getForm(ACTION_PHASE_3);
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

		Link link = getButtonLink(this.iwrb.getLocalizedString("next", "Next"));
		link.setValueOnClick(PARAMETER_ACTION, String.valueOf(iCompanyRegistration ? ACTION_PHASE_4 : ACTION_PHASE_5));
		link.setToFormSubmit(form);
		bottom.add(link);

		link = getButtonLink(this.iwrb.getLocalizedString("back", "Back"));
		link.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_2));
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

		Form form = getForm(ACTION_PHASE_4);
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_4)));

		addErrors(iwc, form);

		Heading1 heading = new Heading1(this.iwrb.getLocalizedString("application.application_name", "Course application"));
		heading.setStyleClass("applicationHeading");
		form.add(heading);

		form.add(getPhasesHeader(this.iwrb.getLocalizedString("application.company_information", "Company information"), 4, numberOfPhases));

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

		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		Link link = getButtonLink(this.iwrb.getLocalizedString("next", "Next"));
		link.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_5));
		link.setToFormSubmit(form);
		bottom.add(link);

		link = getButtonLink(this.iwrb.getLocalizedString("back", "Back"));
		link.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_3));
		link.setToFormSubmit(form);
		bottom.add(link);

		add(form);
	}

	private void showPhaseFive(IWContext iwc) throws RemoteException {
		User applicant = getApplicant(iwc);
		if (!storeContactInfo(iwc, applicant)) {
			showPhaseThree(iwc);
			return;
		}

		Form form = getForm(ACTION_PHASE_5);
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_5)));

		addErrors(iwc, form);

		Heading1 heading = new Heading1(this.iwrb.getLocalizedString("application.application_name", "Course application"));
		heading.setStyleClass("applicationHeading");
		form.add(heading);

		form.add(getPhasesHeader(this.iwrb.getLocalizedString("application.applicant_information", "Applicant information"), 5, numberOfPhases));

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
		formItem.setStyleClass("required");
		Label label = new Label(new Span(new Text(this.iwrb.getLocalizedString("application.has_dyslexia", "Has dyslexia"))), hasDyslexia);
		formItem.add(hasDyslexia);
		formItem.add(label);
		section.add(formItem);

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

		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		Link link = getButtonLink(this.iwrb.getLocalizedString("next", "Next"));
		link.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_6));
		link.setToFormSubmit(form);
		bottom.add(link);

		link = getButtonLink(this.iwrb.getLocalizedString("back", "Back"));
		link.setValueOnClick(PARAMETER_ACTION, String.valueOf(iCompanyRegistration ? ACTION_PHASE_4 : ACTION_PHASE_3));
		link.setToFormSubmit(form);
		bottom.add(link);

		add(form);
	}

	private void showPhaseSix(IWContext iwc) throws RemoteException {
		if (!iwc.isParameterSet(PARAMETER_AGREEMENT)) {
			setError(PARAMETER_AGREEMENT, this.iwrb.getLocalizedString("must_agree_terms", "You have to agree to the terms."));
		}

		if (hasErrors()) {
			showPhaseFive(iwc);
			return;
		}

		addApplication(iwc);

		Form form = getForm(ACTION_PHASE_6);
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_SAVE)));
		form.add(new HiddenInput(PARAMETER_BACK, ""));

		addErrors(iwc, form);

		super.getParentPage().addJavascriptURL("/dwr/interface/CourseDWRUtil.js");
		super.getParentPage().addJavascriptURL("/dwr/engine.js");
		super.getParentPage().addJavascriptURL("/dwr/util.js");

		StringBuffer script = new StringBuffer();
		script.append("function readUser() {\n\tvar id = DWRUtil.util.getValue(\"" + PARAMETER_PAYER_PERSONAL_ID + "\");\n\tCourseDWRUtil.getUserDWR(id, -1, 18, '" + iwc.getCurrentLocale().getCountry() + "', fillUser);\n}");

		StringBuffer script2 = new StringBuffer();
		script2.append("function fillUser(auser) {\n\tuser = auser;\n\tDWRUtil.setValues(user);\n}");

		super.getParentPage().getAssociatedScript().addFunction("readUser", script.toString());
		super.getParentPage().getAssociatedScript().addFunction("fillUser", script2.toString());

		Heading1 heading = new Heading1(this.iwrb.getLocalizedString("application.application_name", "Course application"));
		heading.setStyleClass("applicationHeading");
		form.add(heading);

		form.add(getPhasesHeader(this.iwrb.getLocalizedString("application.payment_information", "Payment information"), 6, numberOfPhases));

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
		cell.setStyleClass("amount");
		cell.add(new Text(iwrb.getLocalizedString("amount", "Amount")));

		group = table.createBodyRowGroup();

		Map applications = getCourseApplicationSession(iwc).getApplications();
		SortedSet prices = getCourseBusiness(iwc).calculatePrices(applications);

		NumberFormat format = NumberFormat.getInstance(iwc.getCurrentLocale());
		float totalPrice = 0;
		int counter = 0;
		Iterator iter = prices.iterator();
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
		cell.setColumnSpan(2);
		cell.setStyleClass("totalPrice");
		cell.add(new Text(iwrb.getLocalizedString("total_amount", "Total amount")));

		cell = row.createCell();
		cell.setStyleClass("totalPrice");
		cell.setStyleClass("price");
		cell.add(new Text(format.format(totalPrice)));

		float amountDue = totalPrice;
		section.add(new HiddenInput(PARAMETER_AMOUNT, Float.toString(amountDue)));

		section.add(table);

		heading = new Heading1(this.iwrb.getLocalizedString("application.payment_information", "Payment information"));
		heading.setStyleClass("subHeader");
		form.add(heading);

		section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");

		boolean creditcardPayment = false;
		if (iwc.isParameterSet(PARAMETER_CREDITCARD_PAYMENT)) {
			creditcardPayment = new Boolean(iwc.getParameter(PARAMETER_CREDITCARD_PAYMENT)).booleanValue();
		}

		RadioButton creditcard = new RadioButton(PARAMETER_CREDITCARD_PAYMENT, Boolean.TRUE.toString());
		creditcard.setStyleClass("radiobutton");
		creditcard.setSelected(creditcardPayment);
		creditcard.keepStatusOnAction(true);
		creditcard.setToDisableOnClick(PARAMETER_CARD_TYPE, false, false);
		creditcard.setToDisableOnClick(PARAMETER_CARD_NUMBER, false);
		creditcard.setToDisableOnClick(PARAMETER_VALID_MONTH, false, false);
		creditcard.setToDisableOnClick(PARAMETER_VALID_YEAR, false, false);

		RadioButton giro = new RadioButton(PARAMETER_CREDITCARD_PAYMENT, Boolean.FALSE.toString());
		giro.setStyleClass("radiobutton");
		giro.setSelected(!creditcardPayment);
		giro.keepStatusOnAction(true);
		giro.setToDisableOnClick(PARAMETER_CARD_TYPE, true, false);
		giro.setToDisableOnClick(PARAMETER_CARD_NUMBER, true);
		giro.setToDisableOnClick(PARAMETER_VALID_MONTH, true, false);
		giro.setToDisableOnClick(PARAMETER_VALID_YEAR, true, false);

		helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("application.payment_information_help", "Please select which type of payment you will be using to pay for the service.")));
		section.add(helpLayer);

		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("radioButtonItem");
		Label label = new Label(this.iwrb.getLocalizedString("application.payment_giro", "Payment with giro"), giro);
		formItem.add(giro);
		formItem.add(label);
		section.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("radioButtonItem");
		label = new Label(this.iwrb.getLocalizedString("application.payment_creditcard", "Payment with creditcard"), creditcard);
		formItem.add(creditcard);
		formItem.add(label);
		section.add(formItem);

		section.add(clearLayer);

		heading = new Heading1(this.iwrb.getLocalizedString("application.payer_information", "Payer information"));
		heading.setStyleClass("subHeader");
		form.add(heading);

		section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("application.payer_information_help", "Please enter informations about the destined payer, if other than yourself.")));
		section.add(helpLayer);

		RadioButton loggedInUser = new RadioButton(PARAMETER_PAYER_OPTION, Boolean.TRUE.toString());
		loggedInUser.setStyleClass("radiobutton");
		loggedInUser.keepStatusOnAction(true);
		loggedInUser.setToDisableOnClick(PARAMETER_PAYER_NAME, true);
		loggedInUser.setToDisableOnClick(PARAMETER_PAYER_PERSONAL_ID, true);

		RadioButton otherPayer = new RadioButton(PARAMETER_PAYER_OPTION, Boolean.FALSE.toString());
		otherPayer.setStyleClass("radiobutton");
		otherPayer.keepStatusOnAction(true);
		otherPayer.setToDisableOnClick(PARAMETER_PAYER_NAME, false);
		otherPayer.setToDisableOnClick(PARAMETER_PAYER_PERSONAL_ID, false);

		if (iwc.isParameterSet(PARAMETER_PAYER_OPTION)) {
			boolean currentPayer = new Boolean(iwc.getParameter(PARAMETER_PAYER_OPTION)).booleanValue();
			loggedInUser.setSelected(currentPayer);
			otherPayer.setSelected(!currentPayer);
		}

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("radioButtonItem");
		label = new Label(this.iwrb.getLocalizedString("payer.applicant", "Applicant"), loggedInUser);
		formItem.add(loggedInUser);
		formItem.add(label);
		section.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("radioButtonItem");
		label = new Label(this.iwrb.getLocalizedString("payer.other_payer", "Other payer"), otherPayer);
		formItem.add(otherPayer);
		formItem.add(label);
		section.add(formItem);

		TextInput payerName = new TextInput(PARAMETER_PAYER_NAME, null);
		payerName.setId("userName");
		payerName.keepStatusOnAction(true);
		payerName.setDisabled(loggedInUser.getSelected());

		TextInput payerPersonalID = new TextInput(PARAMETER_PAYER_PERSONAL_ID, null);
		payerPersonalID.setId(PARAMETER_PAYER_PERSONAL_ID);
		payerPersonalID.keepStatusOnAction(true);
		payerPersonalID.setMaxlength(10);
		payerPersonalID.setDisabled(loggedInUser.getSelected());
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

		heading = new Heading1(this.iwrb.getLocalizedString("application.creditcard_information", "Creditcard information"));
		heading.setStyleClass("subHeader");
		form.add(heading);

		section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("application.creditcard_information_help", "If you have selected to pay by creditcard, please fill in the creditcard information.  All the fields are required.")));
		section.add(helpLayer);

		/*Collection images = getCourseBusiness(iwc).getCreditCardImages();
		Iterator iterator = images.iterator();
		while (iterator.hasNext()) {
			Image image = (Image) iterator.next();
			image.setStyleClass("creditCardImage");
			helpLayer.add(image);
		}*/

		TextInput cardOwner = new TextInput(PARAMETER_NAME_ON_CARD, null);
		cardOwner.keepStatusOnAction(true);
		cardOwner.setDisabled(!creditcardPayment);

		TextInput cardNumber = new TextInput(PARAMETER_CARD_NUMBER, null);
		cardNumber.setLength(16);
		cardNumber.setMaxlength(16);
		cardNumber.keepStatusOnAction(true);
		cardNumber.setDisabled(!creditcardPayment);

		TextInput ccNumber = new TextInput(PARAMETER_CCV, null);
		ccNumber.setLength(3);
		ccNumber.setMaxlength(3);
		ccNumber.keepStatusOnAction(true);
		ccNumber.setDisabled(!creditcardPayment);

		DropdownMenu validMonth = new DropdownMenu(PARAMETER_VALID_MONTH);
		validMonth.setWidth("45px");
		validMonth.keepStatusOnAction(true);
		for (int a = 1; a <= 12; a++) {
			validMonth.addMenuElement(TextSoap.addZero(a), TextSoap.addZero(a));
		}
		validMonth.setDisabled(!creditcardPayment);

		IWTimestamp stamp = new IWTimestamp();
		DropdownMenu validYear = new DropdownMenu(PARAMETER_VALID_YEAR);
		validYear.setWidth("60px");
		validYear.keepStatusOnAction(true);
		int year = stamp.getYear();
		for (int a = year; a <= year + 10; a++) {
			validYear.addMenuElement(String.valueOf(stamp.getYear()).substring(2), String.valueOf(stamp.getYear()));
			stamp.addYears(1);
		}
		validYear.setDisabled(!creditcardPayment);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("required");
		label = new Label(new Span(new Text(this.iwrb.getLocalizedString("application.card_owner", "Card owner"))), cardOwner);
		formItem.add(label);
		formItem.add(cardOwner);
		section.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("required");
		if (hasError(PARAMETER_CARD_NUMBER)) {
			formItem.setStyleClass("hasError");
		}
		label = new Label(new Span(new Text(this.iwrb.getLocalizedString("application.card_number", "Card number"))), cardNumber);
		formItem.add(label);
		formItem.add(cardNumber);
		section.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("required");
		label = new Label(new Span(new Text(this.iwrb.getLocalizedString("application.ccv_number", "Credit card verification number"))), ccNumber);
		formItem.add(label);
		formItem.add(ccNumber);
		section.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("required");
		label = new Label(new Span(new Text(this.iwrb.getLocalizedString("application.card_valid_time", "Card valid through"))), validMonth);
		formItem.add(label);
		formItem.add(validMonth);
		formItem.add(validYear);
		section.add(formItem);

		section.add(clearLayer);

		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		Link next = getButtonLink(this.iwrb.getLocalizedString("next", "Next"));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SAVE));
		next.setOnClick("this.style.display='none';");
		next.setToFormSubmit(form);
		bottom.add(next);

		Link back = getButtonLink(this.iwrb.getLocalizedString("back", "Back"));
		back.setValueOnClick(PARAMETER_BACK, Boolean.TRUE.toString());
		back.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_5));
		back.setToFormSubmit(form);
		bottom.add(back);

		add(form);
	}

	private void save(IWContext iwc) throws RemoteException {
		Boolean payerOption = iwc.isParameterSet(PARAMETER_PAYER_OPTION) ? new Boolean(iwc.getParameter(PARAMETER_PAYER_OPTION)) : null;
		if (payerOption == null) {
			setError(PARAMETER_PAYER_OPTION, this.iwrb.getLocalizedString("must_select_payer_option", "You have to select a payer option."));
		}
		else if (!payerOption.booleanValue()) {
			if (!iwc.isParameterSet(PARAMETER_PAYER_PERSONAL_ID)) {
				setError(PARAMETER_PAYER_PERSONAL_ID, this.iwrb.getLocalizedString("payer_personal_id_empty", "Payer personal ID is empty"));
			}
			else if (!SocialSecurityNumber.isValidSocialSecurityNumber(iwc.getParameter(PARAMETER_PAYER_PERSONAL_ID), iwc.getCurrentLocale())) {
				setError(PARAMETER_PAYER_PERSONAL_ID, this.iwrb.getLocalizedString("invalid_personal_id", "Invalid personal ID"));
			}
			else {
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
		}
		if (hasErrors()) {
			showPhaseSix(iwc);
			return;
		}

		String payerPersonalID = iwc.isParameterSet(PARAMETER_PAYER_PERSONAL_ID) ? iwc.getParameter(PARAMETER_PAYER_PERSONAL_ID) : "";
		String payerName = iwc.isParameterSet(PARAMETER_PAYER_NAME) ? iwc.getParameter(PARAMETER_PAYER_NAME) : "";
		double amount = Double.parseDouble(iwc.getParameter(PARAMETER_AMOUNT));

		boolean creditCardPayment = new Boolean(iwc.getParameter(PARAMETER_CREDITCARD_PAYMENT)).booleanValue();
		IWTimestamp paymentStamp = new IWTimestamp();
		String properties = null;
		if (creditCardPayment) {
			String nameOnCard = iwc.getParameter(PARAMETER_NAME_ON_CARD);
			String cardNumber = iwc.getParameter(PARAMETER_CARD_NUMBER);
			String expiresMonth = iwc.getParameter(PARAMETER_VALID_MONTH);
			String expiresYear = iwc.getParameter(PARAMETER_VALID_YEAR);
			String ccVerifyNumber = iwc.getParameter(PARAMETER_CCV);
			String referenceNumber = paymentStamp.getDateString("yyyyMMddHHmmssSSSS");

			try {
				properties = getCourseBusiness(iwc).authorizePayment(nameOnCard, cardNumber, expiresMonth, expiresYear, ccVerifyNumber, amount, "ISK", referenceNumber);
			}
			catch (CreditCardAuthorizationException e) {
				setError("", e.getLocalizedMessage(iwrb));
			}
		}

		if (hasErrors()) {
			showPhaseSix(iwc);
			return;
		}

		int merchantPK = Integer.parseInt(getIWApplicationContext().getApplicationSettings().getProperty(CourseConstants.PROPERTY_MERCHANT_PK, "-1"));
		String merchantType = getIWApplicationContext().getApplicationSettings().getProperty(CourseConstants.PROPERTY_MERCHANT_TYPE);
		is.idega.idegaweb.egov.course.data.CourseApplication application = getCourseBusiness(iwc).saveApplication(getCourseApplicationSession(iwc).getApplications(), merchantPK, (float) amount, merchantType, creditCardPayment ? CourseConstants.PAYMENT_TYPE_CARD : CourseConstants.PAYMENT_TYPE_GIRO, null, payerName, payerPersonalID, getUser(iwc), iwc.getCurrentLocale());
		if (application != null && creditCardPayment) {
			try {
				String authorizationCode = getCourseBusiness(iwc).finishPayment(properties);
				application.setReferenceNumber(authorizationCode);
				application.store();
			}
			catch (CreditCardAuthorizationException e) {
				setError("", e.getLocalizedMessage());
			}
		}

		if (hasErrors()) {
			showPhaseSix(iwc);
			return;
		}

		if (application != null) {
			getCourseApplicationSession(iwc).clear(iwc);
			Heading1 heading = new Heading1(this.iwrb.getLocalizedString("application.application_name", "Course application"));
			heading.setStyleClass("applicationHeading");
			add(heading);

			addPhasesReceipt(iwc, this.iwrb.getLocalizedString("application.receipt", "Application receipt"), this.iwrb.getLocalizedString("application.application_save_completed", "Application sent"), this.iwrb.getLocalizedString("application.application_send_confirmation", "Your course application has been received and will be processed."), 7, numberOfPhases);

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

		if (coursePK != null) {
			ApplicationHolder holder = new ApplicationHolder();
			Course course = getCourseBusiness(iwc).getCourse(new Integer(coursePK.toString()));
			holder.setCourse(course);
			holder.setUser(getApplicant(iwc));
			holder.setHasDyslexia(hasDyslexia);

			getCourseApplicationSession(iwc).addApplication(getApplicant(iwc), holder);
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