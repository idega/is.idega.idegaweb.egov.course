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

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
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
import com.idega.user.data.User;

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

	private static final String PARAMETER_PERSONAL_ID = "prm_personal_id";
	private static final String PARAMETER_COURSE_TABLE_ID = "prm_cou_course_table";

	private static final String PARAMETER_CATEGORY = "prm_sch_cat";
	private static final String PARAMETER_COURSE_TYPE = "prm_cou_type";
	private static final String PARAMETER_PROVIDER = "prm_provider_pk";
	private static final String PARAMETER_COURSE = "prm_cou_course";

	private IWResourceBundle iwrb = null;

	private int numberOfPhases = 7;
	private boolean iUseSessionUser = false;
	private Object iSchoolTypePK = null;

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

	private Form getForm(int state) {
		Form form = new Form();

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
		script5.append("\n\t").append("if (course.isfull) { radio.disabled = true; }");
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

		form.add(getPhasesHeader(iwrb.getLocalizedString("course", "Course"), 1, numberOfPhases, true));

		Layer info = new Layer(Layer.DIV);
		info.setStyleClass("info");
		form.add(info);

		Paragraph infoHelp = new Paragraph();
		infoHelp.setStyleClass("infoHelp");
		infoHelp.add(new Text(this.iwrb.getLocalizedString("application.application_help", "Below you can select the type of courses you want to register to if there are any available.<br />The courses are held at the main office unless otherwise stated.<br /><br />Please select the courses you want to register to and click 'Next'.")));
		info.add(infoHelp);

		Heading1 heading = new Heading1(this.iwrb.getLocalizedString("application.select_course_type", "Select course type"));
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
			courses = getCourseBusiness(iwc).getCoursesDWR(providerPK != null ? providerPK.intValue() : -1, schoolTypePK.intValue(), courseTypePK.intValue(), ((Integer) getApplicant(iwc).getPrimaryKey()).intValue(), iwc.getCurrentLocale().getCountry(), this.iUseSessionUser);
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
				CheckBox checker = new CheckBox(PARAMETER_COURSE, course.getPk());
				checker.setStyleClass("checkbox");
				cell.add(checker);
				cell = row.createCell();
				cell.setStyleClass("column1");
				cell.add(new Text(course.getProvider()));
				cell = row.createCell();
				cell.setStyleClass("column2");
				cell.add(new Text(course.getName()));
				cell = row.createCell();
				cell.setStyleClass("column3");
				cell.add(new Text(course.getTimeframe()));
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
	}

	private void showPhaseThree(IWContext iwc) throws RemoteException {
	}

	private void showPhaseFour(IWContext iwc) throws RemoteException {
	}

	private void showPhaseFive(IWContext iwc) throws RemoteException {
	}

	private void showPhaseSix(IWContext iwc) throws RemoteException {
	}

	private void save(IWContext iwc) throws RemoteException {
	}

	private int parseAction(IWContext iwc) {
		int action = ACTION_PHASE_1;
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}

		return action;
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